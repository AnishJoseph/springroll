package com.springrollexample.router;

import com.springroll.core.DTOBusinessValidator;
import com.springroll.core.DTOEnricher;
import com.springroll.core.IEvent;
import com.springrollexample.router.businessvalidators.TestBusinessValidator;
import com.springrollexample.router.test.TestRootEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by anishjoseph on 09/11/16.
 */
@Service
public class TestRootServiceFactoryImpl implements TestRootServiceFactory {
    @Autowired
    TestBusinessValidator testBusinessValidator;
    @Override
    public DTOEnricher getServiceEnricher() {
        return null;
    }

    @Override
    public DTOBusinessValidator getBusinessValidator() {
        return testBusinessValidator;
    }

    @Override
    public Class<? extends IEvent> getServiceEvent() {
        return TestRootEvent.class;
    }
}
