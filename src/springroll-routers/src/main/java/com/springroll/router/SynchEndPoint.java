package com.springroll.router;

import com.springroll.core.DTO;
import com.springroll.orm.entities.Job;
import org.apache.camel.EndpointInject;
import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anishjoseph on 09/09/16.
 */
@Component
public class SynchEndPoint {

//    private static final Logger logger = LoggerFactory.getLogger(SynchronousEndpoint.class);

    @EndpointInject(ref = "synchronousEndPoint")
    private ProducerTemplate synchronousEndPoint;

    public Long route(DTO payload){
        List<DTO> payloads = new ArrayList<DTO>(1);
        payloads.add(payload);
        return route(payloads);
    }
    public Long route(List<DTO> payloads){
        Job job = new Job();
        job.setPayloads(payloads);
        return (Long) synchronousEndPoint.sendBody(synchronousEndPoint.getDefaultEndpoint(), ExchangePattern.InOut, job);
    }
}