package org.springframework.dao;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/dao/CleanupFailureDataAccessException.class */
public class CleanupFailureDataAccessException extends NonTransientDataAccessException {
    public CleanupFailureDataAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
