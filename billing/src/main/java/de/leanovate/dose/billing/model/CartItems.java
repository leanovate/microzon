package de.leanovate.dose.billing.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CartItems {

    @JsonProperty
    public List<CartItem> items;

    @JsonProperty
    public boolean valid;

    @JsonProperty
    public int totalCents;

    @Override
    public String toString() {

        return "CartItems{" +
                "items=" + items +
                ", valid=" + valid +
                ", totalCents=" + totalCents +
                '}';
    }
}
