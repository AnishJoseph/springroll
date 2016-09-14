package com.springrollexample.router.test;

import com.springroll.core.DTO;

/**
 * Created by anishjoseph on 11/09/16.
 */
public class TestDTO implements DTO {
    private static final long serialVersionUID = 1L;
    private String thread;
    private String valueToWrite;
    private int testLocation;
    private int testCase;
    private TestType testType;
    private String testLocationEventName;


    public String getThread() {
        return thread;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }

    public String getValueToWrite() {
        return valueToWrite;
    }

    public void setValueToWrite(String valueToWrite) {
        this.valueToWrite = valueToWrite;
    }

    public int getTestLocation() {
        return testLocation;
    }

    public void setTestLocation(int testLocation) {
        this.testLocation = testLocation;
    }

    public int getTestCase() {
        return testCase;
    }

    public void setTestCase(int testCase) {
        this.testCase = testCase;
    }

    public TestType getTestType() {
        return testType;
    }

    public void setTestType(TestType testType) {
        this.testType = testType;
    }

    public String getTestLocationEventName() {
        return testLocationEventName;
    }

    public void setTestLocationEventName(String testLocationEventName) {
        this.testLocationEventName = testLocationEventName;
    }

    public enum TestType {
        OPTIMISTIC_LOCKING_DB_DEADLOCK,
        OPTIMISTIC_LOCKING_COMPETING_THREADS,
        EXCEPTION,
        HAPPY_FLOW
    }
}
