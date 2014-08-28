package de.leanovate.dose.billing.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ActiveProduct {
    @JsonProperty
    public String id;

    @JsonProperty
    public String name;

    @JsonProperty
    public String description;

    @JsonProperty
    public List<ProductOption> options;

    @JsonProperty
    public List<String> categories;

    @JsonProperty
    public List<ImageRef> images;
}