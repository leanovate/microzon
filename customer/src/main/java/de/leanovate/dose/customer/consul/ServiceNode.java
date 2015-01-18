package de.leanovate.dose.customer.consul;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceNode {
    public String Node;

    public String Address;

    public String ServiceID;

    public String ServiceName;

    public List<String> ServiceTags;

    public int ServicePort;

    @Override
    public String toString() {

        return "ServiceNode{" +
                "Node='" + Node + '\'' +
                ", Address='" + Address + '\'' +
                ", ServiceID='" + ServiceID + '\'' +
                ", ServiceName='" + ServiceName + '\'' +
                ", ServiceTags=" + ServiceTags +
                ", ServicePort=" + ServicePort +
                '}';
    }
}
