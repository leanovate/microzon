package de.leanovate.dose.billing.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CartItem {
    @JsonProperty
    public String cartId;

    @JsonProperty
    public int position;

    @JsonProperty
    public String productId;

    @JsonProperty
    public String productOption;

    @JsonProperty
    public int amount;

    @JsonProperty
    public int priceInCent;
}
