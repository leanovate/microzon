package de.leanovate.dose.billing.consul;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CheckInfo {
    @JsonProperty
    public String Node;

    @JsonProperty
    public String CheckID;

    @JsonProperty
    public String Name;

    @JsonProperty
    public String Status;

    @JsonProperty
    public String Notes;

    @JsonProperty
    public String Output;

    @JsonProperty
    public String ServiceID;

    @JsonProperty
    public String ServiceName;

    @Override
    public String toString() {

        return "CheckInfo{" +
                "Node='" + Node + '\'' +
                ", CheckID='" + CheckID + '\'' +
                ", Name='" + Name + '\'' +
                ", Status='" + Status + '\'' +
                ", Notes='" + Notes + '\'' +
                ", Output='" + Output + '\'' +
                ", ServiceID='" + ServiceID + '\'' +
                ", ServiceName='" + ServiceName + '\'' +
                '}';
    }
}
