package com.springrollexample.orm.entities;


import com.springroll.orm.entities.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by anishjoseph on 05/09/16.
 */

@Entity
@Table(name = "testanish")
public class Customer extends AbstractEntity {

    private String firstname;
    private String lastname;

    protected Customer() {}

    public Customer(String firstName, String lastName) {
        this.firstname = firstName;
        this.lastname = lastName;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    @Override
    public String toString() {
        return String.format("Customer[id=%d, firstName='%s', lastName='%s']", getID(), firstname, lastname);
    }

}
