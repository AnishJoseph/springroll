package com.springroll.notification;

import com.springroll.core.LocaleFactory;
import com.springroll.core.SpringrollSecurity;
import com.springroll.core.exceptions.DebugInfo;
import com.springroll.core.services.notification.DismissibleAlertMessage;
import com.springroll.core.services.notification.AlertType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anishjoseph on 05/10/16.
 */
public class SpringrollExceptionAlertMessage extends AbstractAlertMessage implements DismissibleAlertMessage {
    private String serviceMessageKey = "ui.exception.service.msg";
    private String[] serviceMessageArgs = new String[1];
    private String messageKey;
    private String[] args = new String[]{};
    private String serviceEventName;
    private String eventThatCausedException;
    private Long jobId;
    private Long transactionLegId;
    private List<SpringrollExceptionDebugInfo> springrollExceptionDebugInfoList = new ArrayList<>();
    {
        alertType = AlertType.ERROR;
    }

    public SpringrollExceptionAlertMessage() {
    }

    public SpringrollExceptionAlertMessage(DebugInfo debugInfo, String notificationReceivers, String initiator) {
        this.serviceMessageArgs[0] = debugInfo.getServiceName();
        this.serviceEventName = debugInfo.getServiceEventName();
        this.eventThatCausedException = debugInfo.getEventThatCausedException();
        this.messageKey = debugInfo.getSpringrollException() == null ? debugInfo.getCauses().get(0): debugInfo.getSpringrollException().getMessageKey();
        this.args = debugInfo.getSpringrollException() == null ? null : debugInfo.getSpringrollException().getMessageArguments();
        this.jobId = debugInfo.getJobId();
        this.transactionLegId = debugInfo.getTransactionLegId();
        setNotificationReceivers(notificationReceivers);
        setInitiator(initiator);
        for (int i = 0; i < debugInfo.getExceptions().size(); i++) {
            springrollExceptionDebugInfoList.add(new SpringrollExceptionDebugInfo(debugInfo.getCauses().get(i), debugInfo.getExceptions().get(i), debugInfo.getStackTraceElements().get(i)));

        }
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public String getServiceMessageKey() {
        return serviceMessageKey;
    }

    public void setServiceMessageKey(String serviceMessageKey) {
        this.serviceMessageKey = serviceMessageKey;
    }

    public String[] getServiceMessageArgs() {
        return serviceMessageArgs;
    }

    public void setServiceMessageArgs(String[] serviceMessageArgs) {
        this.serviceMessageArgs = serviceMessageArgs;
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

    public List<SpringrollExceptionDebugInfo> getSpringrollExceptionDebugInfoList() {
        return springrollExceptionDebugInfoList;
    }

    public void setSpringrollExceptionDebugInfoList(List<SpringrollExceptionDebugInfo> springrollExceptionDebugInfoList) {
        this.springrollExceptionDebugInfoList = springrollExceptionDebugInfoList;
    }

    @Override
    public String getMessage() {
        return LocaleFactory.getLocalizedMessage(SpringrollSecurity.getUser().getLocale(), messageKey, args);
    }

    public class SpringrollExceptionDebugInfo implements Serializable {
        private String message;
        private String exception;
        private String declaringClass;
        private String methodName;
        private String fileName;
        private int    lineNumber;
        public SpringrollExceptionDebugInfo(String message, String exception, StackTraceElement stackTraceElement) {
            this.message = message;
            this.exception = exception;
            this.declaringClass = stackTraceElement.getClassName();
            this.methodName = stackTraceElement.getMethodName();
            this.fileName = stackTraceElement.getLineNumber() == -2 ? "Native Method" : stackTraceElement.getFileName() == null ? "Unknown Source" : stackTraceElement.getFileName();
            this.lineNumber = stackTraceElement.getLineNumber();
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getException() {
            return exception;
        }

        public void setException(String exception) {
            this.exception = exception;
        }

        public String getDeclaringClass() {
            return declaringClass;
        }

        public void setDeclaringClass(String declaringClass) {
            this.declaringClass = declaringClass;
        }

        public String getMethodName() {
            return methodName;
        }

        public void setMethodName(String methodName) {
            this.methodName = methodName;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public int getLineNumber() {
            return lineNumber;
        }

        public void setLineNumber(int lineNumber) {
            this.lineNumber = lineNumber;
        }
    }
}
