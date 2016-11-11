package com.springroll.core;

import java.io.Serializable;

/**
 * Created by anishjoseph on 18/10/16.
 */
public class Lov implements Serializable {
    private Object value;
    private String name;

    public Lov() {
    }

    public Lov(Object value) {
        this.value = value;
        this.name = value.toString();
    }
    public Lov(Object value, String name) {
        this.value = value;
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
