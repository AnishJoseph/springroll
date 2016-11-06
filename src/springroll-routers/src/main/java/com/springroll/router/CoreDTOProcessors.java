package com.springroll.router;

import com.springroll.core.DTOBusinessValidator;
import com.springroll.core.DTOEnricher;
import com.springroll.core.IDTOProcessors;
import com.springroll.core.IEvent;
import com.springroll.router.notification.MdmEvent;
import com.springroll.router.notification.NotificationAckEvent;
import com.springroll.router.review.ReviewActionEvent;

/**
 * Created by anishjoseph on 07/10/16.
 */
public enum  CoreDTOProcessors implements IDTOProcessors {
    REVIEW(ReviewActionEvent.class, null, null),
    NOTIFICATION_ACK(NotificationAckEvent.class, null, null),
    MDM(MdmEvent.class, DummyBusinessValidator.class, DummyEnricher.class),
    NULL(null, null, null);

    private Class<? extends DTOEnricher> enricherClass;
    private Class<? extends DTOBusinessValidator> businessValidatorClass;

    private Class<? extends IEvent> eventClass;

    CoreDTOProcessors(Class<? extends IEvent> eventClass, Class<? extends DTOBusinessValidator> businessValidatorClass, Class<? extends DTOEnricher> enricherClass){
        this.eventClass = eventClass;
        this.businessValidatorClass = businessValidatorClass;
        this.enricherClass = enricherClass;
    }

    @Override public Class<? extends DTOEnricher> getEnricherClass() {
        return enricherClass;
    }

    @Override public Class<? extends DTOBusinessValidator> getBusinessValidatorClass() {
        return businessValidatorClass;
    }

    @Override public Class<? extends IEvent> getEventClass() {
        return eventClass;
    }
}
