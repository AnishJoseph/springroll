package com.springrollexample.router.test;

import com.springroll.core.IServiceDefinition;
import com.springroll.core.ServiceDTO;
import com.springrollexample.router.ApplicationServiceDefinitions;

/**
 * Created by anishjoseph on 30/10/16.
 */
public class TestServiceDTO extends TestDTO implements ServiceDTO {
    @Override
    public IServiceDefinition getProcessor() {
        return ApplicationServiceDefinitions.TEST_ROOT;
    }
}
