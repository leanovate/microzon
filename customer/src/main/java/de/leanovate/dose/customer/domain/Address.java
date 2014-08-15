package de.leanovate.dose.customer.domain;

import org.hibernate.annotations.NaturalId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Address {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    @NaturalId
    private Customer customer;

    @Column(nullable = true)
    private String company;

    @Column(nullable = true)
    private String firstName;

    @Column(nullable = true)
    private String lastName;

    @Column
    private String street;

    @Column
    private String zip;

    @Column
    private String city;

    public Long getId() {

        return id;
    }

    public Customer getCustomer() {

        return customer;
    }

    public void setCustomer(final Customer customer) {

        this.customer = customer;
    }

    public String getCompany() {

        return company;
    }

    public void setCompany(final String company) {

        this.company = company;
    }

    public String getFirstName() {

        return firstName;
    }

    public void setFirstName(final String firstName) {

        this.firstName = firstName;
    }

    public String getLastName() {

        return lastName;
    }

    public void setLastName(final String lastName) {

        this.lastName = lastName;
    }

    public String getStreet() {

        return street;
    }

    public void setStreet(final String street) {

        this.street = street;
    }

    public String getZip() {

        return zip;
    }

    public void setZip(final String zip) {

        this.zip = zip;
    }

    public String getCity() {

        return city;
    }

    public void setCity(final String city) {

        this.city = city;
    }
}
