package de.leanovate.dose.customer.model;

public class LoginResponse {
    public boolean successful;

    public Long customerId;

    public LoginResponse() {

    }

    public LoginResponse(boolean successful, Long customerId) {

        this.successful = successful;
        this.customerId = customerId;
    }
}
