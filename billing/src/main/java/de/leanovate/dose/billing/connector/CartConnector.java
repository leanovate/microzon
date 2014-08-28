package de.leanovate.dose.billing.connector;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kristofa.brave.ClientTracer;
import com.github.kristofa.brave.httpclient.BraveHttpRequestInterceptor;
import com.github.kristofa.brave.httpclient.BraveHttpResponseInterceptor;
import com.google.common.base.Optional;
import de.leanovate.dose.billing.BillingConfiguration;
import de.leanovate.dose.billing.logging.LoggingHttpRequestInterceptor;
import de.leanovate.dose.billing.model.CartItems;
import io.dropwizard.jackson.Jackson;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.net.URLEncoder;

public class CartConnector {
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    private final String baseUrl;
    private final CloseableHttpClient client;

    public CartConnector(final BillingConfiguration configuration, final ClientTracer clientTracer) {

        this.baseUrl = configuration.cartBaseUrl;
        this.client = HttpClientBuilder.create()
                .addInterceptorFirst(new LoggingHttpRequestInterceptor())
                .addInterceptorLast(new BraveHttpRequestInterceptor(clientTracer, Optional.of("Cart")))
                .addInterceptorLast(new BraveHttpResponseInterceptor(clientTracer))
                .setMaxConnTotal(10)
                .setMaxConnPerRoute(10)
                .build();

    }

    public CartItems getCartItems(final String cartId) throws IOException {

        HttpGet request = new HttpGet(baseUrl + "/carts/" + URLEncoder.encode(cartId, "UTF-8") + "/items");
        try (CloseableHttpResponse response = client.execute(request)) {
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return MAPPER.readValue(response.getEntity().getContent(), CartItems.class);
            }
            throw new IOException("Request to cart service failed. Status=" + response.getStatusLine());
        }

    }
}
