package com.springroll.orm.mdm;

import com.springroll.core.ILovProvider;

/**
 * Created by anishjoseph on 04/11/16.
 */
public class JavaLovSource implements ILovSource {
    private String name;
    private String source;
    private ILovProvider provider;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public ILovProvider getProvider() {
        return provider;
    }

    public void setProvider(ILovProvider provider) {
        this.provider = provider;
    }
}
