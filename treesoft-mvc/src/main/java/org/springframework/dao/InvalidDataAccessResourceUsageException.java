package org.springframework.dao;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/dao/InvalidDataAccessResourceUsageException.class */
public class InvalidDataAccessResourceUsageException extends NonTransientDataAccessException {
    public InvalidDataAccessResourceUsageException(String msg) {
        super(msg);
    }

    public InvalidDataAccessResourceUsageException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
