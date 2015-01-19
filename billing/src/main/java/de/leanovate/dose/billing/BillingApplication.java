package de.leanovate.dose.billing;

import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.ClientTracer;
import com.github.kristofa.brave.EmptySpanCollectorImpl;
import com.github.kristofa.brave.EndPointSubmitter;
import com.github.kristofa.brave.ServerTracer;
import com.github.kristofa.brave.SpanCollector;
import com.github.kristofa.brave.TraceFilter;
import com.github.kristofa.brave.zipkin.ZipkinSpanCollector;
import com.mysql.jdbc.Driver;
import de.leanovate.dose.billing.connector.CartConnector;
import de.leanovate.dose.billing.connector.CustomerConnector;
import de.leanovate.dose.billing.connector.ProductConnector;
import de.leanovate.dose.billing.consul.ConsulLookup;
import de.leanovate.dose.billing.consul.HealthInfo;
import de.leanovate.dose.billing.logging.LoggingFilter;
import de.leanovate.dose.billing.logging.ServletTraceFilter;
import de.leanovate.dose.billing.logging.UnsignedRandom;
import de.leanovate.dose.billing.resources.OrderResource;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.DispatcherType;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public class BillingApplication extends Application<BillingConfiguration> {
    private final static Logger LOGGER = LoggerFactory.getLogger(BillingApplication.class);

    @Override
    public String getName() {

        return "billing";
    }

    @Override
    public void initialize(final Bootstrap<BillingConfiguration> bootstrap) {

    }

    @Override
    public void run(final BillingConfiguration configuration, final Environment environment) throws Exception {

        final DBI jdbi = createBDI(configuration, environment);

        final SpanCollector spanCollector = createSpanCollector();
        ClientTracer clientTracer = Brave.getClientTracer(spanCollector, Collections.<TraceFilter>emptyList());
        ServerTracer serverTracer = Brave.getServerTracer(spanCollector, Collections.<TraceFilter>emptyList());
        EndPointSubmitter endPointSubmitter = Brave.getEndPointSubmitter();

        fixRandom(clientTracer);

        endPointSubmitter.submit(InetAddress.getLocalHost().getHostAddress(), 80, "billing-service");

        final CartConnector cartConnector = new CartConnector(clientTracer);
        final CustomerConnector customerConnector = new CustomerConnector(clientTracer);
        final ProductConnector productConnector = new ProductConnector(clientTracer);

        environment.servlets().addFilter("tracing", new ServletTraceFilter(serverTracer)).addMappingForUrlPatterns(
                EnumSet.allOf(DispatcherType.class), true, "/*");
        environment.servlets().addFilter("logging", new LoggingFilter()).addMappingForUrlPatterns(
                EnumSet.allOf(DispatcherType.class), true, "/*");

        environment.jersey().register(new OrderResource(cartConnector, customerConnector, productConnector));
    }

    private DBI createBDI(final BillingConfiguration configuration, final Environment environment)
            throws ClassNotFoundException {

        final List<HealthInfo> healthinfos = ConsulLookup.lookup("mysql");

        if (healthinfos.isEmpty()) {
            throw new RuntimeException("No active mysql node found");
        }
        final HealthInfo first = healthinfos.get(0);

        final DBIFactory factory = new DBIFactory();
        final DataSourceFactory dataSourceFactory = new DataSourceFactory();
        dataSourceFactory.setDriverClass(Driver.class.getName());
        dataSourceFactory.setUrl("jdbc:mysql://" + first.Node.Address + ":" + first.Service.Port + "/billing");
        dataSourceFactory.setUser(configuration.databaseUser);
        dataSourceFactory.setPassword(configuration.databasePassword);
        return factory.build(environment, dataSourceFactory, "mysql");
    }

    private SpanCollector createSpanCollector() {

        final List<HealthInfo> healthinfos = ConsulLookup.lookup("zipkin-collector");

        if (healthinfos.isEmpty()) {
            LOGGER.warn("No active zipkin collector found");
            return new EmptySpanCollectorImpl();
        }

        final HealthInfo first = healthinfos.get(0);

        return new ZipkinSpanCollector(first.Node.Address, first.Service.Port);
    }

    private void fixRandom(ClientTracer clientTracer) throws Exception {

        // Superhack to allow interoperability between finagle (original) and brave (cheap copy)
        Field field = clientTracer.getClass().getDeclaredField("randomGenerator");
        field.setAccessible(true);
        field.set(clientTracer, new UnsignedRandom());
    }

    public static void main(String[] args) throws Exception {

        final BillingApplication application = new BillingApplication();

        application.run(args);
    }
}
