package de.leanovate.dose.billing.consul;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ServiceInfo {
    @JsonProperty
    public String ID;

    @JsonProperty
    public String Service;

    @JsonProperty
    public List<String> Tags;

    @JsonProperty
    public int Port;

    @Override
    public String toString() {

        return "ServiceInfo{" +
                "ID='" + ID + '\'' +
                ", Service='" + Service + '\'' +
                ", Tags=" + Tags +
                ", Port=" + Port +
                '}';
    }
}
