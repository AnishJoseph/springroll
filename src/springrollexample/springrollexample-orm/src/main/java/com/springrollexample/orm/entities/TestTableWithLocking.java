package com.springrollexample.orm.entities;

import com.springroll.orm.entities.AbstractEntity;
import org.springframework.beans.factory.annotation.Configurable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * Created by anishjoseph on 15/09/16.
 */
@Configurable
@Entity
@Table(name = "Test_Table_With_Locking")
public class TestTableWithLocking extends AbstractEntity {
    private static final long serialVersionUID = -7367486828941464840L;
    @Version
    @Column(name = "VERSION")
    private Long version;


    public TestTableWithLocking() {
    }

    @Column(name = "FIELD1", length = 100)
    protected String field1;

    @Column(name = "FIELD2", length = 100)
    protected String field2;

    @Column(name = "FIELD3", length = 100)
    protected String field3;

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

    public String getField3() {
        return field3;
    }

    public void setField3(String field3) {
        this.field3 = field3;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
