package de.leanovate.dose.billing.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Customer {
    @JsonProperty
    public String customerId;

    @JsonProperty
    public String email;

    @JsonProperty
    public String password;

    @JsonProperty
    public String firstName;

    @JsonProperty
    public String lastName;
}
