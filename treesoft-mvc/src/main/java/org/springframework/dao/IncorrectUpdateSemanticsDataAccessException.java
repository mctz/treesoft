package org.springframework.dao;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/dao/IncorrectUpdateSemanticsDataAccessException.class */
public class IncorrectUpdateSemanticsDataAccessException extends InvalidDataAccessResourceUsageException {
    public IncorrectUpdateSemanticsDataAccessException(String msg) {
        super(msg);
    }

    public IncorrectUpdateSemanticsDataAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public boolean wasDataUpdated() {
        return true;
    }
}
