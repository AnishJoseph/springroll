package com.springroll.router;

import com.springroll.core.JobDefinitions;
import com.springroll.router.notification.NotificationAckDTO;
import com.springroll.router.notification.NotificationAckEvent;
import com.springroll.router.review.ReviewActionDTO;
import com.springroll.router.review.ReviewActionEvent;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by anishjoseph on 12/09/16.
 */
@Component()
public class InternalJobDefinitionBuilder {

    @PostConstruct
    public void init(){
        JobDefinitions.add(ReviewActionDTO.class, ReviewActionEvent.class);
        JobDefinitions.add(NotificationAckDTO.class, NotificationAckEvent.class);

    }
}
