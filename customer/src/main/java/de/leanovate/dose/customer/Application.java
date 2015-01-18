package de.leanovate.dose.customer;

import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.ClientTracer;
import com.github.kristofa.brave.EmptySpanCollectorImpl;
import com.github.kristofa.brave.EndPointSubmitter;
import com.github.kristofa.brave.ServerTracer;
import com.github.kristofa.brave.SpanCollector;
import com.github.kristofa.brave.TraceFilter;
import com.github.kristofa.brave.zipkin.ZipkinSpanCollector;
import de.leanovate.dose.customer.consul.ConsulLookup;
import de.leanovate.dose.customer.consul.ServiceNode;
import de.leanovate.dose.customer.filter.LoggingFilter;
import de.leanovate.dose.customer.filter.ServletTraceFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application {
    private final static Logger LOG = LoggerFactory.getLogger(Application.class);

    @Bean
    public FilterRegistrationBean loggingFilter() {

        final FilterRegistrationBean filterRegistration = new FilterRegistrationBean();

        filterRegistration.setFilter(new LoggingFilter());
        filterRegistration.setUrlPatterns(Arrays.asList("/*"));
        return filterRegistration;
    }

    @Bean
    public FilterRegistrationBean traceFilter(ServerTracer serverTracer, EndPointSubmitter endPointSubmitter)
            throws UnknownHostException {

        endPointSubmitter.submit(InetAddress.getLocalHost().getHostAddress(), 80, "customer-service");
        final FilterRegistrationBean filterRegistration = new FilterRegistrationBean();

        filterRegistration.setFilter(new ServletTraceFilter(serverTracer));
        filterRegistration.setUrlPatterns(Arrays.asList("/*"));
        return filterRegistration;
    }

    @Bean
    public ServerTracer serverTracer(SpanCollector spanCollector) {

        return Brave.getServerTracer(spanCollector, Collections.<TraceFilter>emptyList());
    }

    @Bean
    public ClientTracer clientTracer(SpanCollector spanCollector) {

        return Brave.getClientTracer(spanCollector, Collections.<TraceFilter>emptyList());
    }

    @Bean
    @ConfigurationProperties
    public SpanCollector spanCollector() {

        final List<ServiceNode> zipkinNodes = ConsulLookup.lookup("zipkin-collector");

        if (zipkinNodes.isEmpty()) {
            LOG.warn("No zipkin collector nodes found");
            return new EmptySpanCollectorImpl();
        } else {
            final ServiceNode first = zipkinNodes.get(0);

            return new ZipkinSpanCollector(first.Address, first.ServicePort);
        }
    }

    @Bean
    public EndPointSubmitter endPointSubmitter() {

        return Brave.getEndPointSubmitter();
    }

    public static void main(String[] args) throws Exception {

        final List<ServiceNode> mysqlNodes = ConsulLookup.lookup("mysql");

        if (mysqlNodes.isEmpty()) {
            LOG.error("No mysql node found");
            return;
        } else {
            final ServiceNode first = mysqlNodes.get(0);
            LOG.info("Using mysql node: " + first);
            System.setProperty("spring.datasource.url",
                    "jdbc:mysql://" + first.Address + ":" + first.ServicePort + "/customer");
        }

        SpringApplication.run(Application.class, args);
    }
}
