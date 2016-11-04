package com.springroll.core;

/**
 * Created by anishjoseph on 18/10/16.
 */
public class Lov {
    private Object value;
    private String name;

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
