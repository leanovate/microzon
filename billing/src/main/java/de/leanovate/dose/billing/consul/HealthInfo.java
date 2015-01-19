package de.leanovate.dose.billing.consul;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class HealthInfo {
    @JsonProperty
    public NodeInfo Node;
    @JsonProperty
    public ServiceInfo Service;
    @JsonProperty
    public List<CheckInfo> Checks;

    @Override
    public String toString() {

        return "HealthInfo{" +
                "Node=" + Node +
                ", Service=" + Service +
                ", Checks=" + Checks +
                '}';
    }
}
