package com.springrollexample.api.facade;

import com.springroll.api.facade.AbstractAPI;
import com.springrollexample.router.test.TestDTO;
import com.springrollexample.router.test.TestRootEvent;
import com.springrollexample.router.test.TestServiceDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
public class SpringrollExampleAPI extends AbstractAPI {
    private static final Logger logger = LoggerFactory.getLogger(SpringrollExampleAPI.class);

    @RequestMapping(value = "/testPipelineSimple", method = RequestMethod.POST)
    public Long testPipelineSimple(@RequestBody TestServiceDTO testDTO ) {
        testDTO.setTestType(TestDTO.TestType.EXCEPTION);
        TestRootEvent testRootEvent = new TestRootEvent();
        testRootEvent.setPayload(testDTO);

        return route(testDTO);
    }
    @RequestMapping(value = "/testCompetingThreads", method = RequestMethod.GET)
    public Long testCompetingThreads() {
        TestServiceDTO testDTO = new TestServiceDTO();
        testDTO.setTestCase(1);
        testDTO.setTestLocation(3);
        testDTO.setTestType(TestDTO.TestType.OPTIMISTIC_LOCKING_COMPETING_THREADS);
        TestRootEvent testRootEvent = new TestRootEvent();
        testRootEvent.setPayload(testDTO);
        route(testDTO);
        testDTO = new TestServiceDTO();
        testDTO.setTestCase(2);
        testDTO.setTestLocation(1);
        testDTO.setTestType(TestDTO.TestType.OPTIMISTIC_LOCKING_COMPETING_THREADS);
        testRootEvent = new TestRootEvent();
        testRootEvent.setPayload(testDTO);
        return route(testDTO);
    }

}
