package com.springroll.orm.mdm;

import com.springroll.core.exceptions.FixableException;

import java.util.List;

/**
 * Created by anishjoseph on 04/11/16.
 */
public class MdmDefinitions {
    private List<MdmDefinition> masters;
    private List<LovSource> lovSources;

    public List<MdmDefinition> getMasters() {
        return masters;
    }

    public void setMasters(List<MdmDefinition> masters) {
        this.masters = masters;
    }

    public List<LovSource> getLovSources() {
        return lovSources;
    }

    public void setLovSources(List<LovSource> lovSources) {
        this.lovSources = lovSources;
    }

    public LovSource getLovSource(String sourceName){
        for (LovSource lovSource : lovSources) {
            if(lovSource.getName().equals(sourceName))return lovSource;
        }
        throw new FixableException("Unable to find LOV Source for name " + sourceName, "lov.source.missingdefinition", sourceName);
    }
}
