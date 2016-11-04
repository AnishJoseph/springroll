package com.springroll.core;

/**
 * Created by anishjoseph on 18/10/16.
 */
public class Lov {
    private String value;
    private String name;

    public Lov(String value) {
        this.value = value;
        this.name = value;
    }
    public Lov(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
