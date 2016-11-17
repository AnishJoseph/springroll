package com.springroll.core;

/**
 * Created by anishjoseph on 17/11/16.
 */
public class BusinessValidationViolations {
    private int dtoIndex;
    private String field;
    private String message;

    public BusinessValidationViolations(int dtoIndex, String field, String message) {
        this.dtoIndex = dtoIndex;
        this.field = field;
        this.message = message;
    }

    public int getDtoIndex() {
        return dtoIndex;
    }

    public void setDtoIndex(int dtoIndex) {
        this.dtoIndex = dtoIndex;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
