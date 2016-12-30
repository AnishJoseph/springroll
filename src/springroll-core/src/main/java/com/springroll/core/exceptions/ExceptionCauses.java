package com.springroll.core.exceptions;

import com.springroll.core.IEvent;

/**
 * Created by anishjoseph on 29/12/16.
 */
public class ExceptionCauses {
    public IEvent causingEvent;
    public Throwable caused;
    public ExceptionCauses(){}
    public ExceptionCauses(IEvent evt, Throwable throwable){
        caused = throwable;
        causingEvent = evt;
    }

}
