package com.springrollexample.router;

import com.springroll.core.DTOMeta;
import com.springrollexample.router.enrichers.TestEnricher;
import com.springrollexample.router.test.*;
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

    @Autowired
    TestEnricher testEnricher;

    @PostConstruct public void init(){
        dtoMeta.addDTOType(TestDTO.class, TestRootEvent.class, testEnricher);
        dtoMeta.addDTOType(SynchToAsynchDTO.class, TE_SynchFromAsynchSide.class);

    }
}
