package com.springroll.core;

import java.io.Serializable;

/**
 * Created by anishjoseph on 18/10/16.
 */
public class Lov implements Serializable {
    private Object value;
    private String label;

    public Lov() {
    }

    public Lov(Object value) {
        this.value = value;
        this.label = value.toString();
    }
    public Lov(Object value, String label) {
        this.value = value;
        this.label = label;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
