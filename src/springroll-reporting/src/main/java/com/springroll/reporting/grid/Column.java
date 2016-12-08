package com.springroll.reporting.grid;

/**
 * Created by anishjoseph on 18/10/16.
 */
public class Column {
    private String title;
    private String type = "text";
    private String numberFormat;
    private String className;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        if(numberFormat != null) type = "num-fmt";
        this.type = type;
    }

    public String getNumberFormat() {
        return numberFormat;
    }

    public void setNumberFormat(String numberFormat) {
        type = "num-fmt";
        this.numberFormat = numberFormat;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
