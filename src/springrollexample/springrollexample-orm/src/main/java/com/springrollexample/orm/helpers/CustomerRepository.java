package com.springrollexample.orm.helpers;

/**
 * Created by anishjoseph on 05/09/16.
 */

import com.springroll.orm.helpers.AbstractEntityRepository;
import com.springrollexample.orm.entities.Customer;

import java.util.List;


public interface CustomerRepository extends AbstractEntityRepository<Customer> {

    List<Customer> findByLastname(String lastName);
}
