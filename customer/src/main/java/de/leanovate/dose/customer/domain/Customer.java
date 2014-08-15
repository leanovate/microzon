package de.leanovate.dose.customer.domain;

import de.leanovate.dose.customer.model.RegistrationRequest;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import java.io.Serializable;
import java.util.Set;

@Entity
public class Customer implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @NaturalId
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = true)
    private String firstName;

    @Column(nullable = true)
    private String lastName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "city")
    private Set<Address> addresses;

    protected Customer() {

    }

    public Customer(final RegistrationRequest registration) {

        this.email = registration.email;
        this.password = registration.password;
    }

    public Long getId() {

        return id;
    }

    public Long getCustomerId() {

        return id;
    }

    public String getEmail() {

        return email;
    }

    public String getFirstName() {

        return firstName;
    }

    public String getLastName() {

        return lastName;
    }

    public boolean checkPassword(String password) {
        System.out.println(">>> "  + this.password + " " + password +" " + this.password.equals(password));
        return this.password.equals(password);
    }
}
