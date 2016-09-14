package com.springrollexample.router;

import com.springroll.core.DTOMeta;
import com.springrollexample.router.test.TestDTO;
import com.springrollexample.router.test.TestRootEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by anishjoseph on 12/09/16.
 */
@Component
public class DTOMetaBuilder {
    @Autowired
    DTOMeta dtoMeta;

    public DTOMetaBuilder(){
        System.out.println("hello");
    }

    @PostConstruct public void init(){
        dtoMeta.addDTOType(TestDTO.class, TestRootEvent.class);

    }
}
