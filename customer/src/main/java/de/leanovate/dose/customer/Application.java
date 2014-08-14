package de.leanovate.dose.customer;

import de.leanovate.dose.customer.filter.LoggingFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

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

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }
}
