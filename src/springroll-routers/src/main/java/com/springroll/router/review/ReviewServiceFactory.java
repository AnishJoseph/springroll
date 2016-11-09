package com.springroll.router.review;

import com.springroll.core.DTOBusinessValidator;
import com.springroll.core.DTOEnricher;
import com.springroll.core.IEvent;
import com.springroll.core.IServiceFactory;
import com.springroll.core.services.IReviewServiceFactory;
import org.springframework.stereotype.Service;

/**
 * Created by anishjoseph on 09/11/16.
 */
@Service
public class ReviewServiceFactory implements IReviewServiceFactory {

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
        return ReviewActionEvent.class;
    }
}
