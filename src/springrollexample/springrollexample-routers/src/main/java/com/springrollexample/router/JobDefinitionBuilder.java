package com.springrollexample.router;

import com.springroll.core.JobDefinitions;
import com.springrollexample.router.businessvalidators.TestBusinessValidator;
import com.springrollexample.router.enrichers.TestEnricher;
import com.springrollexample.router.test.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by anishjoseph on 12/09/16.
 */
@Component
public class JobDefinitionBuilder {

    @Autowired
    TestEnricher testEnricher;

    @Autowired
    TestBusinessValidator validator;

    @PostConstruct public void init(){
        JobDefinitions.add(TestDTO.class, TestRootEvent.class, testEnricher, validator);
        JobDefinitions.add(SynchToAsynchDTO.class, TE_SynchFromAsynchSide.class);
    }
}
