package com.springroll.mdm;

import com.springroll.core.DTOBusinessValidator;
import com.springroll.core.DTOEnricher;
import com.springroll.core.IEvent;
import com.springroll.core.services.mdm.IMdmServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by anishjoseph on 09/11/16.
 */
@Service
public class MdmServiceFactoryImpl implements IMdmServiceFactory {
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
