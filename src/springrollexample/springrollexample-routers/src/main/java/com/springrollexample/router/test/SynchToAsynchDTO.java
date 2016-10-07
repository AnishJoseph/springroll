package com.springrollexample.router.test;

import com.springroll.core.IDTOProcessors;
import com.springrollexample.router.ApplicationDTOProcessors;

/**
 * Created by anishjoseph on 11/09/16.
 */
public class SynchToAsynchDTO extends TestDTO {
    private static final long serialVersionUID = 1L;

    @Override
    public IDTOProcessors getProcessor() {
        return ApplicationDTOProcessors.SYNCH_TO_ASYNCH;
    }

}
