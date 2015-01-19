package de.leanovate.dose.billing.connector;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kristofa.brave.ClientTracer;
import com.github.kristofa.brave.httpclient.BraveHttpRequestInterceptor;
import com.github.kristofa.brave.httpclient.BraveHttpResponseInterceptor;
import com.google.common.base.Optional;
import de.leanovate.dose.billing.consul.ConsulLookup;
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
import java.util.List;
import java.util.stream.Collectors;

public class CartConnector {
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    private final CloseableHttpClient client;

    public CartConnector(final ClientTracer clientTracer) {

        this.client = HttpClientBuilder.create()
                .addInterceptorFirst(new LoggingHttpRequestInterceptor())
                .addInterceptorFirst(new BraveHttpRequestInterceptor(clientTracer, Optional.of("Cart")))
                .addInterceptorFirst(new BraveHttpResponseInterceptor(clientTracer))
                .setMaxConnTotal(10)
                .setMaxConnPerRoute(10)
                .build();

    }

    public CartItems getCartItems(final String cartId) throws IOException {

        return ServiceFailover.retry(getEnpointUrls(), url -> {
            HttpGet request = new HttpGet(url + "/carts/" + URLEncoder.encode(cartId, "UTF-8") + "/items");
            try (CloseableHttpResponse response = client.execute(request)) {
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    return MAPPER.readValue(response.getEntity().getContent(), CartItems.class);
                }
                throw new IOException("Request to cart service failed. Status=" + response.getStatusLine());
            }
        });
    }

    private List<String> getEnpointUrls() {

        return ConsulLookup.lookup("cart-service").stream()
                .map(healthInfo -> "http://" + healthInfo.Node.Address + ":" + healthInfo.Service.Port)
                .collect(Collectors.toList());
    }
}
