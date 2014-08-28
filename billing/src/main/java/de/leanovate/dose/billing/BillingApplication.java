package de.leanovate.dose.billing;

import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.ClientTracer;
import com.github.kristofa.brave.EndPointSubmitter;
import com.github.kristofa.brave.ServerTracer;
import com.github.kristofa.brave.SpanCollector;
import com.github.kristofa.brave.TraceFilter;
import com.github.kristofa.brave.jersey.ServletTraceFilter;
import com.github.kristofa.brave.zipkin.ZipkinSpanCollector;
import de.leanovate.dose.billing.connector.CartConnector;
import de.leanovate.dose.billing.connector.CustomerConnector;
import de.leanovate.dose.billing.connector.ProductConnector;
import de.leanovate.dose.billing.logging.LoggingFilter;
import de.leanovate.dose.billing.resources.OrderResource;
import io.dropwizard.Application;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;

import javax.servlet.DispatcherType;

import java.net.InetAddress;
import java.util.Collections;
import java.util.EnumSet;

public class BillingApplication extends Application<BillingConfiguration> {
    @Override
    public String getName() {

        return "billing";
    }

    @Override
    public void initialize(final Bootstrap<BillingConfiguration> bootstrap) {

    }

    @Override
    public void run(final BillingConfiguration configuration, final Environment environment) throws Exception {

        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, configuration.database, "mysql");

        final SpanCollector spanCollector = new ZipkinSpanCollector(configuration.zipkinHost, configuration.zipkinPort);
        final ClientTracer clientTracer = Brave.getClientTracer(spanCollector, Collections.<TraceFilter>emptyList());
        final ServerTracer serverTracer = Brave.getServerTracer(spanCollector, Collections.<TraceFilter>emptyList());
        final EndPointSubmitter endPointSubmitter = Brave.getEndPointSubmitter();

        endPointSubmitter.submit(InetAddress.getLocalHost().getHostAddress(), 80, "Billing");

        final CartConnector cartConnector = new CartConnector(configuration, clientTracer);
        final CustomerConnector customerConnector = new CustomerConnector(configuration, clientTracer);
        final ProductConnector productConnector = new ProductConnector(configuration, clientTracer);

        environment.servlets().addFilter("tracing", new ServletTraceFilter(serverTracer, endPointSubmitter)).addMappingForUrlPatterns(
                EnumSet.allOf(DispatcherType.class), true, "/*");
        environment.servlets().addFilter("logging", new LoggingFilter()).addMappingForUrlPatterns(
                EnumSet.allOf(DispatcherType.class), true, "/*");

        environment.jersey().register(new OrderResource(cartConnector, customerConnector, productConnector));
    }

    public static void main(String[] args) throws Exception {

        final BillingApplication application = new BillingApplication();

        application.run(args);
    }
}
