package com.springroll.router;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by anishjoseph on 15/09/16.
 */
@Service
public class JobManager {
    Map<Long, LegMonitor> legMonitorMap = new HashMap<>();

    public Long registerNewTransactionLeg(Long jobId){
        Long newLegId = 1l;
        LegMonitor legMonitor = legMonitorMap.get(jobId);
        if(legMonitor == null){
            legMonitor = new LegMonitor();
            synchronized (legMonitorMap){
//                cleanup();
                legMonitorMap.put(jobId, legMonitor);
            }
        } else {
            synchronized (legMonitorMap){
                newLegId = legMonitor.addRef();
            }
        }
        return newLegId;
    }

    private class LegMonitor {
        private Set<Long> refs = new HashSet<>();
        private Long legId = 1l;
        public LegMonitor(){
            refs.add(legId);
        }

        public Long addRef(){
            legId = legId + 1;
            refs.add(legId);
            return legId;
        }
        public LegStatus removeRef(Long legId){
            boolean removed = refs.remove(legId);
            if(!removed)return LegStatus.DOES_NOT_EXIST;
            if(refs.isEmpty())return LegStatus.EMPTIED;
            return LegStatus.REMOVED;
        }
    }
    private enum LegStatus {
        DOES_NOT_EXIST,
        EMPTIED,
        REMOVED
    }

}
