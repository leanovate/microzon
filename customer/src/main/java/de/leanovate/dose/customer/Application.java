package de.leanovate.dose.customer;

import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.ClientTracer;
import com.github.kristofa.brave.EndPointSubmitter;
import com.github.kristofa.brave.ServerTracer;
import com.github.kristofa.brave.SpanCollector;
import com.github.kristofa.brave.TraceFilter;
import com.github.kristofa.brave.zipkin.ZipkinSpanCollector;
import de.leanovate.dose.customer.filter.LoggingFilter;
import de.leanovate.dose.customer.filter.ServletTraceFilter;
import org.springframework.beans.factory.annotation.Value;
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

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application {

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

        endPointSubmitter.submit(InetAddress.getLocalHost().getHostAddress(), 80, "Customer");
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
    public SpanCollector spanCollector(@Value("${zipkin.host}") String zipkinHost, @Value("${zipkin.port}") int zipkinPort) {

        return new ZipkinSpanCollector(zipkinHost, zipkinPort);
    }

    @Bean
    public EndPointSubmitter endPointSubmitter() {

        return Brave.getEndPointSubmitter();
    }

    public static void main(String[] args) throws Exception {

        SpringApplication.run(Application.class, args);
    }
}
