package de.leanovate.dose.billing.connector;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kristofa.brave.ClientTracer;
import com.github.kristofa.brave.httpclient.BraveHttpRequestInterceptor;
import com.github.kristofa.brave.httpclient.BraveHttpResponseInterceptor;
import com.google.common.base.Optional;
import de.leanovate.dose.billing.consul.ConsulLookup;
import de.leanovate.dose.billing.logging.LoggingHttpRequestInterceptor;
import de.leanovate.dose.billing.model.Customer;
import io.dropwizard.jackson.Jackson;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerConnector {
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    private final CloseableHttpClient client;

    public CustomerConnector(final ClientTracer clientTracer) {

        this.client = HttpClientBuilder.create()
                .addInterceptorFirst(new LoggingHttpRequestInterceptor())
                .addInterceptorLast(new BraveHttpRequestInterceptor(clientTracer, Optional.of("Customer")))
                .addInterceptorLast(new BraveHttpResponseInterceptor(clientTracer))
                .setMaxConnTotal(10)
                .setMaxConnPerRoute(10)
                .build();
    }

    public Customer getCustomer(final String customerId) throws IOException {

        return ServiceFailover.retry(getEnpointUrls(), url -> {
            HttpGet request = new HttpGet(url + "/customers/" + URLEncoder.encode(customerId, "UTF-8"));
            try (CloseableHttpResponse response = client.execute(request)) {
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    return MAPPER.readValue(response.getEntity().getContent(), Customer.class);
                }
                throw new IOException("Request to customer service failed. Status=" + response.getStatusLine());
            }
        });
    }

    private List<String> getEnpointUrls() {

        return ConsulLookup.lookup("customer-service").stream()
                .map(healthInfo -> "http://" + healthInfo.Node.Address + ":" + healthInfo.Service.Port)
                .collect(Collectors.toList());
    }
}
