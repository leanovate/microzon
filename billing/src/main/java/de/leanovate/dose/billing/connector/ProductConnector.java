package de.leanovate.dose.billing.connector;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kristofa.brave.ClientTracer;
import com.github.kristofa.brave.httpclient.BraveHttpRequestInterceptor;
import com.github.kristofa.brave.httpclient.BraveHttpResponseInterceptor;
import com.google.common.base.Optional;
import de.leanovate.dose.billing.consul.ConsulLookup;
import de.leanovate.dose.billing.logging.LoggingHttpRequestInterceptor;
import de.leanovate.dose.billing.model.ActiveProduct;
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

public class ProductConnector {
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    private final CloseableHttpClient client;

    public ProductConnector(final ClientTracer clientTracer) {

        this.client = HttpClientBuilder.create()
                .addInterceptorFirst(new LoggingHttpRequestInterceptor())
                .addInterceptorLast(new BraveHttpRequestInterceptor(clientTracer, Optional.of("Product")))
                .addInterceptorLast(new BraveHttpResponseInterceptor(clientTracer))
                .setMaxConnTotal(10)
                .setMaxConnPerRoute(10)
                .build();
    }

    public ActiveProduct getProduct(final String productId) throws IOException {

        return ServiceFailover.retry(getEnpointUrls(), url -> {
            HttpGet request = new HttpGet(url + "/products/" + URLEncoder.encode(productId, "UTF-8"));
            try (CloseableHttpResponse response = client.execute(request)) {
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    return MAPPER.readValue(response.getEntity().getContent(), ActiveProduct.class);
                }
                throw new IOException("Request to customer service failed. Status=" + response.getStatusLine());
            }
        });
    }

    private List<String> getEnpointUrls() {

        return ConsulLookup.lookup("product-service").stream()
                .map(healthInfo -> "http://" + healthInfo.Node.Address + ":" + healthInfo.Service.Port)
                .collect(Collectors.toList());
    }

}
