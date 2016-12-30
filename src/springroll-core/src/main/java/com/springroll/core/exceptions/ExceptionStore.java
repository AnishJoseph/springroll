package com.springroll.core.exceptions;

import com.springroll.core.ContextStore;
import com.springroll.core.IEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by anishjoseph on 29/12/16.
 */
public class ExceptionStore {
    private static Map<String, ExceptionCauses> causesMap = new HashMap<>();

    public static void setExceptionCauses(Throwable caused, IEvent causingEvent){
        String key =  makeKey(causingEvent);
        if(causesMap.containsKey(key))return;
        synchronized (causesMap){
            causesMap.put(key, new ExceptionCauses(causingEvent,caused));
        }
    }
    public static ExceptionCauses getCausedExceptionDetails(IEvent event){
        String key =  makeKey(event);
        synchronized (causesMap){
            return causesMap.remove(key);
        }
    }

    private static String makeKey(IEvent event){
        if(event == null)
            return "" + ContextStore.getJobId() + ":" + ContextStore.getLegId();

        return "" + event.getJobId() + ":" + event.getLegId();

    }

}
