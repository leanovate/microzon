package de.leanovate.dose.billing.consul;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ConsulLookup {
    private static ConcurrentHashMap<String, ConsulServiceLookup> serviceLookups = new ConcurrentHashMap<>();

    public static List<HealthInfo> lookup(final String serviceName) {

        return serviceLookups.computeIfAbsent(serviceName, ConsulServiceLookup::new).lookup();
    }

}
