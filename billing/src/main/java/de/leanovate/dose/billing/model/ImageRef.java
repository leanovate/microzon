package de.leanovate.dose.billing.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ImageRef {
    @JsonProperty
    public String thumbnail;

    @JsonProperty
    public String preview;

    @JsonProperty
    public String original;
}
