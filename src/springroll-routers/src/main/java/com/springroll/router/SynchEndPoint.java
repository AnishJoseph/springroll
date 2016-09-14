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

    public Long route(List<? extends DTO> payloads, Principal principal){
        JobMeta jobMeta = new JobMeta(payloads, principal, null, null, true, true);
        UserContextFactory.setUserContextInThreadScope(principal, null, null);
        return (Long) synchronousEndPoint.sendBody(synchronousEndPoint.getDefaultEndpoint(), ExchangePattern.InOut, jobMeta);
    }
    public Long routeSynchronous(List<? extends DTO> payloads, Principal principal){
        JobMeta jobMeta = new JobMeta(payloads, principal, null, null, true, false);
        return (Long) synchronousEndPoint.sendBody(synchronousEndPoint.getDefaultEndpoint(), ExchangePattern.InOut, jobMeta);
    }
    public Long routeSynchronous(List<? extends DTO> payloads, Long jobId, Principal principal){
        JobMeta jobMeta = new JobMeta(payloads, principal, jobId, null, true, false);
        return (Long) synchronousEndPoint.sendBody(synchronousEndPoint.getDefaultEndpoint(), ExchangePattern.InOut, jobMeta);
    }

}