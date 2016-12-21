package com.springrollexample.router.test;

import com.springroll.core.ServiceDefinition;
import com.springroll.core.ServiceDTO;
import com.springrollexample.router.ApplicationServiceDefinitions;

/**
 * Created by anishjoseph on 30/10/16.
 */
public class TestServiceDTO extends TestDTO implements ServiceDTO {
    @Override
    public ServiceDefinition getServiceDefinition() {
        return ApplicationServiceDefinitions.TEST_ROOT;
    }
}
