package com.springroll.router;

import com.springroll.orm.entities.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by anishjoseph on 09/09/16.
 */
@Component
public class StartSynchFlow {
    public Object on(Job job){
        return job;
    }
}
