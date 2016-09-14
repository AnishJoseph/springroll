package com.springroll.router;

import com.springroll.core.DTOMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by anishjoseph on 12/09/16.
 */
@Component
public class DTOTypeBuilder {
    @Autowired
    DTOMeta dtoMeta;

    @PostConstruct
    public void init(){

    }
}
