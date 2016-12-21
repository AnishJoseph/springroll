package com.springroll.router.notification;

import com.springroll.core.DTOBusinessValidator;
import com.springroll.core.DTOEnricher;
import com.springroll.core.IEvent;
import com.springroll.core.services.notification.NotificationServiceFactory;
import org.springframework.stereotype.Service;

/**
 * Created by anishjoseph on 09/11/16.
 */
@Service
public class NotificationServiceFactoryImpl implements NotificationServiceFactory {

    @Override
    public DTOEnricher getServiceEnricher() {
        return null;
    }

    @Override
    public DTOBusinessValidator getBusinessValidator() {
        return null;
    }

    @Override
    public Class<? extends IEvent> getServiceEvent() {
        return NotificationAckEvent.class;
    }
}
