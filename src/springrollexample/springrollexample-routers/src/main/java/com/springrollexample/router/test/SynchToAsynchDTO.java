package com.springrollexample.router.test;

import com.springroll.core.IServiceDefinition;
import com.springrollexample.router.ApplicationServiceDefinitions;

/**
 * Created by anishjoseph on 11/09/16.
 */
public class SynchToAsynchDTO extends TestServiceDTO {
    private static final long serialVersionUID = 1L;

    @Override
    public IServiceDefinition getServiceDefinition() {
        return ApplicationServiceDefinitions.SYNCH_TO_ASYNCH;
    }

}
