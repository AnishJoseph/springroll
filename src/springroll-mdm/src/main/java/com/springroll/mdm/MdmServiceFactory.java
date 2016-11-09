package com.springroll.mdm;

import com.springroll.core.DTOBusinessValidator;
import com.springroll.core.DTOEnricher;
import com.springroll.core.IEvent;
import com.springroll.core.IServiceFactory;
import com.springroll.core.services.IMdmServiceFactory;
import com.springroll.router.MdmBusinessValidator;
import com.springroll.router.events.MdmEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by anishjoseph on 09/11/16.
 */
@Service
public class MdmServiceFactory implements IMdmServiceFactory {
    @Autowired MdmServiceEnricher enricher;
    @Autowired MdmBusinessValidator businessValidator;

    @Override
    public DTOEnricher getServiceEnricher() {
        return enricher;
    }

    @Override
    public DTOBusinessValidator getBusinessValidator() {
        return businessValidator;
    }

    @Override
    public Class<? extends IEvent> getServiceEvent() {
        return MdmEvent.class;
    }
}
