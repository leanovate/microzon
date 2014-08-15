package de.leanovate.dose.customer.model;

public class RegistrationResponse {
    public boolean successful;

    public Long customerId;

    public RegistrationResponse() {

    }

    public RegistrationResponse(boolean successful, Long customerId) {

        this.successful = successful;
        this.customerId = customerId;
    }
}
