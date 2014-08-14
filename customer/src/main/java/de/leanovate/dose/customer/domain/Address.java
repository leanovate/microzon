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
}
