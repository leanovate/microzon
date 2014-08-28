package de.leanovate.dose.billing;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class BillingConfiguration extends Configuration {
    @NotEmpty
    @JsonProperty
    public String customerBaseUrl = "http://192.168.254.12";

    @NotEmpty
    @JsonProperty
    public String productBaseUrl = "http://192.168.254.13";

    @NotEmpty
    @JsonProperty
    public String cartBaseUrl = "http://192.168.254.14";

    @NotEmpty
    @JsonProperty
    public String zipkinHost = "192.168.254.20";

    @JsonProperty
    public int zipkinPort =9410;

    @Valid
    @NotNull
    @JsonProperty("database")
    public DataSourceFactory database = new DataSourceFactory();
}
