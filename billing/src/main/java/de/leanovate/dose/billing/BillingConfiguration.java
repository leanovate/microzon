package de.leanovate.dose.billing;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

public class BillingConfiguration extends Configuration {
    @NotEmpty
    @JsonProperty
    public String databaseUser = "billing";

    @NotEmpty
    @JsonProperty
    public String databasePassword = "billing";
}
