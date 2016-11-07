package com.springroll.mdm;


import java.util.List;

/**
 * Created by anishjoseph on 04/11/16.
 */
public class LovSource {
    private List<EnumLovSource> enumSources;
    private List<JavaLovSource> javaSources;
    private List<NamedQueryLovSource> namedQuerySources;

    public List<EnumLovSource> getEnumSources() {
        return enumSources;
    }

    public void setEnumSources(List<EnumLovSource> enumSources) {
        this.enumSources = enumSources;
    }

    public List<JavaLovSource> getJavaSources() {
        return javaSources;
    }

    public void setJavaSources(List<JavaLovSource> javaSources) {
        this.javaSources = javaSources;
    }

    public List<NamedQueryLovSource> getNamedQuerySources() {
        return namedQuerySources;
    }

    public void setNamedQuerySources(List<NamedQueryLovSource> namedQuerySources) {
        this.namedQuerySources = namedQuerySources;
    }
}
