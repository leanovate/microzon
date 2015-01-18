package de.leanovate.dose.customer.consul;

import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

public class ConsulLookup {
    public static List<ServiceNode> lookup(final String serviceName) {

        try {
            final RestTemplate restTemplate = new RestTemplate();
            final ServiceNode[] serviceNodes = restTemplate
                    .getForObject("http://localhost:8500/v1/catalog/service/" + URLEncoder.encode(serviceName, "UTF-8"),
                            ServiceNode[].class);

            return Arrays.asList(serviceNodes);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
