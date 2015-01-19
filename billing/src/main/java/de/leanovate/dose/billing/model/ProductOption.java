package de.leanovate.dose.billing.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductOption {
    @JsonProperty
    public String name;

    @JsonProperty
    public String description;

    @JsonProperty
    public int priceInCent;

    @Override
    public String toString() {

        return "ProductOption{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", priceInCent=" + priceInCent +
                '}';
    }
}
