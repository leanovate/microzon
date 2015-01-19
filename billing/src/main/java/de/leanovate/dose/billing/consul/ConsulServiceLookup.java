package de.leanovate.dose.billing.consul;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConsulServiceLookup {
    private final static Logger LOGGER = LoggerFactory.getLogger(ConsulServiceLookup.class);

    private static final ScheduledExecutorService SCHEDULER = Executors.newScheduledThreadPool(2);

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    private static final HttpHost LOCAL_CONSUL = new HttpHost("localhost", 8500);

    private static final CloseableHttpClient httpClient =
            HttpClientBuilder.create().setMaxConnTotal(5).setMaxConnPerRoute(5).build();

    private final String serviceName;

    private volatile Optional<List<HealthInfo>> cachedResult = Optional.empty();

    private AtomicBoolean scheduled = new AtomicBoolean(false);

    public ConsulServiceLookup(final String serviceName) {

        this.serviceName = serviceName;
    }

    public List<HealthInfo> lookup() {

        if (cachedResult.isPresent()) {
            return cachedResult.get();
        } else {
            return performQuery();
        }
    }

    private List<HealthInfo> performQuery() {

        try {
            final HttpGet request =
                    new HttpGet("/v1/health/service/" + URLEncoder.encode(serviceName, "UTF-8") + "?passing");

            LOGGER.info("Perform consul lookup: " + request);

            try (CloseableHttpResponse response = httpClient.execute(LOCAL_CONSUL, request)) {
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    List<HealthInfo> healthInfos =
                            Arrays.asList(MAPPER.readValue(response.getEntity().getContent(), HealthInfo[].class));

                    LOGGER.info("Found services: " + healthInfos);

                    updateCache(healthInfos);

                    return healthInfos;
                } else {
                    LOGGER.error("Consul lookup failed with status: " + response.getStatusLine());
                    return Collections.emptyList();
                }
            }
        } catch (IOException e) {
            LOGGER.error("Consul lookup failed", e);
            throw new RuntimeException(e);
        }
    }

    private void updateCache(final List<HealthInfo> healthInfos) {

        cachedResult = Optional.of(healthInfos);

        if (!scheduled.getAndSet(true)) {
            SCHEDULER.schedule(() -> {
                scheduled.set(false);
                performQuery();
            }, 5, TimeUnit.SECONDS);
        }
    }
}
