package de.leanovate.dose.billing.consul;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NodeInfo {
    @JsonProperty
    public String Node;

    @JsonProperty
    public String Address;

    @Override
    public String toString() {

        return "NodeInfo{" +
                "Node='" + Node + '\'' +
                ", Address='" + Address + '\'' +
                '}';
    }
}
