package com.springrollexample.orm.entities;

import com.springroll.core.SetTime;
import com.springroll.orm.entities.AbstractEntity;
import com.springroll.orm.entities.MdmEntity;
import org.springframework.beans.factory.annotation.Configurable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by anishjoseph on 15/09/16.
 */
@Configurable
@Entity
@Table(name = "TestTableToCheckMdm")
public class TestTableToCheckMdm extends MdmEntity {
    private static final long serialVersionUID = -7367486828941464840L;

    @NotNull
    @Size(min = 2, max = 10)
    private String stringVariable;

    private boolean booleanVariable;
    private int intVariable;
    private float floatVariable;
    private long longVariable;
    private double doubleVariable;
    private short shortVariable;
    @NotNull
    private Boolean booleanCVariable;
    @NotNull
    private Integer intCVariable;
    @NotNull
    private Float floatCVariable;
    @NotNull
    private Long longCVariable;
    @NotNull
    private Double doubleCVariable;

    private BigDecimal bigDecimalVariable;
    private BigInteger bigIntegerVariable;

    @Enumerated(EnumType.STRING)
    private SetTime enumType;

    private LocalDate localDateVariable;
    private LocalDateTime localDateTimeVariable;

    public String getStringVariable() {
        return stringVariable;
    }

    public void setStringVariable(String stringVariable) {
        this.stringVariable = stringVariable;
    }

    public boolean isBooleanVariable() {
        return booleanVariable;
    }

    public void setBooleanVariable(boolean booleanVariable) {
        this.booleanVariable = booleanVariable;
    }

    public int getIntVariable() {
        return intVariable;
    }

    public void setIntVariable(int intVariable) {
        this.intVariable = intVariable;
    }

    public float getFloatVariable() {
        return floatVariable;
    }

    public void setFloatVariable(float floatVariable) {
        this.floatVariable = floatVariable;
    }

    public long getLongVariable() {
        return longVariable;
    }

    public void setLongVariable(long longVariable) {
        this.longVariable = longVariable;
    }

    public double getDoubleVariable() {
        return doubleVariable;
    }

    public void setDoubleVariable(double doubleVariable) {
        this.doubleVariable = doubleVariable;
    }

    public short getShortVariable() {
        return shortVariable;
    }

    public void setShortVariable(short shortVariable) {
        this.shortVariable = shortVariable;
    }

    public Boolean getBooleanCVariable() {
        return booleanCVariable;
    }

    public void setBooleanCVariable(Boolean booleanCVariable) {
        this.booleanCVariable = booleanCVariable;
    }

    public Integer getIntCVariable() {
        return intCVariable;
    }

    public void setIntCVariable(Integer intCVariable) {
        this.intCVariable = intCVariable;
    }

    public Float getFloatCVariable() {
        return floatCVariable;
    }

    public void setFloatCVariable(Float floatCVariable) {
        this.floatCVariable = floatCVariable;
    }

    public Long getLongCVariable() {
        return longCVariable;
    }

    public void setLongCVariable(Long longCVariable) {
        this.longCVariable = longCVariable;
    }

    public Double getDoubleCVariable() {
        return doubleCVariable;
    }

    public void setDoubleCVariable(Double doubleCVariable) {
        this.doubleCVariable = doubleCVariable;
    }

    public BigDecimal getBigDecimalVariable() {
        return bigDecimalVariable;
    }

    public void setBigDecimalVariable(BigDecimal bigDecimalVariable) {
        this.bigDecimalVariable = bigDecimalVariable;
    }

    public BigInteger getBigIntegerVariable() {
        return bigIntegerVariable;
    }

    public void setBigIntegerVariable(BigInteger bigIntegerVariable) {
        this.bigIntegerVariable = bigIntegerVariable;
    }

    public SetTime getEnumType() {
        return enumType;
    }

    public void setEnumType(SetTime enumType) {
        this.enumType = enumType;
    }

    public LocalDate getLocalDateVariable() {
        return localDateVariable;
    }

    public void setLocalDateVariable(LocalDate localDateVariable) {
        this.localDateVariable = localDateVariable;
    }

    public LocalDateTime getLocalDateTimeVariable() {
        return localDateTimeVariable;
    }

    public void setLocalDateTimeVariable(LocalDateTime localDateTimeVariable) {
        this.localDateTimeVariable = localDateTimeVariable;
    }
}
