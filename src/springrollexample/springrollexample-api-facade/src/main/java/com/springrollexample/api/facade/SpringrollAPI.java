package com.springrollexample.api.facade;

import com.springroll.api.facade.AbstractAPI;
import com.springrollexample.orm.entities.Customer;
import com.springrollexample.orm.helpers.CustomerRepository;
import com.springrollexample.router.test.TestDTO;
import com.springrollexample.router.test.TestRootEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@Transactional
@RestController
public class SpringrollAPI extends AbstractAPI {
    private static final Logger logger = LoggerFactory.getLogger(SpringrollAPI.class);

    @Autowired
    CustomerRepository customerRepository;

    @RequestMapping(value = "/testPipelineSimple", method = RequestMethod.GET)
    public Long testPipelineSimple() {
        TestDTO testDTO = new TestDTO();
        testDTO.setTestCase(1);
        testDTO.setTestLocation(0);
        testDTO.setTestType(TestDTO.TestType.HAPPY_FLOW);
        TestRootEvent testRootEvent = new TestRootEvent();
        testRootEvent.setPayload(testDTO);
        Customer customer = new Customer("a", "b");
        customerRepository.save(customer);
        return route(testDTO);
    }
}
