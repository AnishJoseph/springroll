package com.springroll.orm.mdm;

/**
 * Created by anishjoseph on 04/11/16.
 */
public class NamedQueryLovSource implements ILovSource{
    private String name;
    private String source;

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

}
