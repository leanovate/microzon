package de.leanovate.dose.billing.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class CreateOrder {
    @JsonProperty
    @NotEmpty
    public String customerId;

    @JsonProperty
    @NotEmpty
    public String cartId;

    @Override
    public String toString() {

        return "CreateOrder{" +
                "customerId='" + customerId + '\'' +
                ", cartId='" + cartId + '\'' +
                '}';
    }
}
