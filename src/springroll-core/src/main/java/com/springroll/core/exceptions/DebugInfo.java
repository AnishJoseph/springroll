package com.springroll.core.exceptions;

import java.io.Serializable;
import java.util.List;

/**
 * Created by anishjoseph on 28/12/16.
 */
public class DebugInfo implements Serializable {
    private SpringrollException springrollException = null;
    private List<String> exceptions;
    private List<String> causes;
    private List<StackTraceElement> stackTraceElements;
    private String serviceName;
    private String serviceEventName;
    private String eventThatCausedException;
    private Long jobId;
    private Long transactionLegId;

    public DebugInfo(SpringrollException springrollException, List<String> causes, List<String> exceptions, List<StackTraceElement> stackTraceElements, String serviceName, String serviceEventName, String eventThatCausedException, long jobId, Long transactionLegId) {
        this.springrollException = springrollException;
        this.causes = causes;
        this.stackTraceElements = stackTraceElements;
        this.serviceName = serviceName;
        this.serviceEventName = serviceEventName;
        this.eventThatCausedException = eventThatCausedException;
        this.exceptions = exceptions;
        this.transactionLegId = transactionLegId;
        this.jobId = jobId;
    }

    public SpringrollException getSpringrollException() {
        return springrollException;
    }

    public void setSpringrollException(SpringrollException springrollException) {
        this.springrollException = springrollException;
    }

    public List<String> getCauses() {
        return causes;
    }

    public void setCauses(List<String> causes) {
        this.causes = causes;
    }

    public List<StackTraceElement> getStackTraceElements() {
        return stackTraceElements;
    }

    public void setStackTraceElements(List<StackTraceElement> stackTraceElements) {
        this.stackTraceElements = stackTraceElements;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceEventName() {
        return serviceEventName;
    }

    public void setServiceEventName(String serviceEventName) {
        this.serviceEventName = serviceEventName;
    }

    public String getEventThatCausedException() {
        return eventThatCausedException;
    }

    public void setEventThatCausedException(String eventThatCausedException) {
        this.eventThatCausedException = eventThatCausedException;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Long getTransactionLegId() {
        return transactionLegId;
    }

    public void setTransactionLegId(Long transactionLegId) {
        this.transactionLegId = transactionLegId;
    }

    public List<String> getExceptions() {
        return exceptions;
    }

    public void setExceptions(List<String> exceptions) {
        this.exceptions = exceptions;
    }
}
