package com.springrollexample.api.facade;

import java.io.Serializable;

/**
 * Created by anishjoseph on 08/09/16.
 */
public class Test implements Serializable {
    private String fName;
    private String lName;
    TestEnum testEnum;
    public Test(){}

    public Test(String fName, String lName) {
        this.fName = fName;
        this.lName = lName;
        testEnum = TestEnum.ANISH;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public TestEnum getTestEnum() {
        return testEnum;
    }

    public void setTestEnum(TestEnum testEnum) {
        this.testEnum = testEnum;
    }
}
