package com.springrollexample.router;

import com.springroll.core.*;
import com.springrollexample.router.businessvalidators.TestBusinessValidator;
import com.springrollexample.router.enrichers.TestEnricher;
import com.springrollexample.router.test.TE_SynchFromAsynchSide;
import com.springrollexample.router.test.TestRootEvent;

/**
 * Created by anishjoseph on 07/10/16.
 */
public enum ApplicationDTOProcessors implements IDTOProcessors {

    TEST_ROOT(TestRootEvent.class, TestBusinessValidator.class,TestEnricher.class),
    SYNCH_TO_ASYNCH(TE_SynchFromAsynchSide.class, null, null);

    private Class<? extends DTOEnricher> enricherClass;
    private Class<? extends DTOBusinessValidator> businessValidatorClass;

    private Class<? extends IEvent> eventClass;

    ApplicationDTOProcessors(Class<? extends IEvent> eventClass, Class<? extends DTOBusinessValidator> businessValidatorClass, Class<? extends DTOEnricher> enricherClass){
        this.eventClass = eventClass;
        this.businessValidatorClass = businessValidatorClass;
        this.enricherClass = enricherClass;
    }

    @Override public Class<? extends DTOEnricher> getEnricherClass() {
        return enricherClass;
    }

    @Override public Class<? extends DTOBusinessValidator> getBusinessValidatorClass() {
        return businessValidatorClass;
    }

    @Override public Class<? extends IEvent> getEventClass() {
        return eventClass;
    }

}
