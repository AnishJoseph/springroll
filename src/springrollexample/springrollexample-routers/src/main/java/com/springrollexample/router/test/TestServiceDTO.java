package com.springrollexample.router.test;

import com.springroll.core.IDTOProcessors;
import com.springroll.core.ServiceDTO;
import com.springrollexample.router.ApplicationDTOProcessors;

/**
 * Created by anishjoseph on 30/10/16.
 */
public class TestServiceDTO extends TestDTO implements ServiceDTO {
    @Override
    public IDTOProcessors getProcessor() {
        return ApplicationDTOProcessors.TEST_ROOT;
    }
}
