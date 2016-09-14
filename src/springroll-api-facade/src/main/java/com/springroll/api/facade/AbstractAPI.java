package com.springroll.api.facade;

import com.springroll.router.SynchEndPoint;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by anishjoseph on 12/09/16.
 */
public class AbstractAPI {
    @Autowired
    protected SynchEndPoint synchEndPoint;

}
