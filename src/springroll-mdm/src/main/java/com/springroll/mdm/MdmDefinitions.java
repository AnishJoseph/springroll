package com.springroll.mdm;

import com.springroll.core.exceptions.FixableException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by anishjoseph on 04/11/16.
 */
public class MdmDefinitions {
    private List<MdmDefinition> masters;
    private LovSource lovSources;
    private Map<String, ILovSource> nameToSourceMap = new HashMap<>();

    public void init(){
        if(lovSources.getJavaSources() != null){
            for (JavaLovSource source : lovSources.getJavaSources()) {
                nameToSourceMap.put(source.getName(), source);
            }
        }
        if(lovSources.getEnumSources() != null){
            for (EnumLovSource source : lovSources.getEnumSources()) {
                nameToSourceMap.put(source.getName(), source);
            }
        }
        if(lovSources.getNamedQuerySources() != null){
            for (NamedQueryLovSource source : lovSources.getNamedQuerySources()) {
                nameToSourceMap.put(source.getName(), source);
            }
        }

    }
    public List<MdmDefinition> getMasters() {
        return masters;
    }

    public void setMasters(List<MdmDefinition> masters) {
        this.masters = masters;
    }

    public LovSource getLovSources() {
        return lovSources;
    }

    public void setLovSources(LovSource lovSources) {
        this.lovSources = lovSources;
    }

    public ILovSource getLovSource(String sourceName){
        ILovSource source = nameToSourceMap.get(sourceName);
        if(source != null) return source;
        throw new FixableException("Unable to find LOV Source for name " + sourceName, "lov.source.missingdefinition", sourceName);
    }
}