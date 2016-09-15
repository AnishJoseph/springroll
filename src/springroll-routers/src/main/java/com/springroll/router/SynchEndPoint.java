package com.springroll.router;

import com.springroll.core.UserContextFactory;
import com.springroll.core.DTO;
import com.springroll.core.Principal;
import org.apache.camel.EndpointInject;
import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by anishjoseph on 09/09/16.
 */
@Component
public class SynchEndPoint {

    private static final Logger logger = LoggerFactory.getLogger(SynchEndPoint.class);

    @EndpointInject(ref = "synchronousEndPoint")
    private ProducerTemplate synchronousEndPoint;

    /* This SHOULD only be called from the synch side - eventCreator will put the Event on JMS - the resulting event will be processed asynchronously */
    public Long route(List<? extends DTO> payloads, Principal principal){
        JobMeta jobMeta = new JobMeta(payloads, principal, null, null, null, true, true);
        UserContextFactory.setUserContextInThreadScope(principal, null, null);
        return (Long) synchronousEndPoint.sendBody(synchronousEndPoint.getDefaultEndpoint(), ExchangePattern.InOut, jobMeta);
    }
    /* This SHOULD only be called from the synch side - eventCreator will put the Event directly to Dynamic Router JMS - the resulting event will be processed synchronously since it does not go via JMS*/
    public Long routeSynchronous(List<? extends DTO> payloads, Principal principal){
        JobMeta jobMeta = new JobMeta(payloads, principal, null, null, null, true, false);
        UserContextFactory.setUserContextInThreadScope(principal, null, null);
        return (Long) synchronousEndPoint.sendBody(synchronousEndPoint.getDefaultEndpoint(), ExchangePattern.InOut, jobMeta);
    }
    /* This SHOULD only be called from the asynch side - the routing will be done in the context of the existing jobId */
    public Long routeSynchronous(List<? extends DTO> payloads){
        JobMeta jobMeta = new JobMeta(payloads, UserContextFactory.getPrincipal(), UserContextFactory.getJobId(), UserContextFactory.getLegId(), null, true, false);
        return (Long) synchronousEndPoint.sendBody(synchronousEndPoint.getDefaultEndpoint(), ExchangePattern.InOut, jobMeta);
    }

}