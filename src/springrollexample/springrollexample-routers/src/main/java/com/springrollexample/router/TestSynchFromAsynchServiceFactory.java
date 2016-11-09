package com.springrollexample.router;

import com.springroll.core.DTOBusinessValidator;
import com.springroll.core.DTOEnricher;
import com.springroll.core.IEvent;
import com.springrollexample.router.test.TE_SynchFromAsynchSide;
import org.springframework.stereotype.Service;

/**
 * Created by anishjoseph on 09/11/16.
 */
@Service public class TestSynchFromAsynchServiceFactory implements ITestSynchFromAsynchServiceFactory {
    @Override
    public DTOEnricher getServiceEnricher() {
        return null;
    }

    @Override
    public DTOBusinessValidator getBusinessValidator() {
        return null;
    }

    @Override
    public Class<? extends IEvent> getServiceEvent() {
        return TE_SynchFromAsynchSide.class;
    }
}
