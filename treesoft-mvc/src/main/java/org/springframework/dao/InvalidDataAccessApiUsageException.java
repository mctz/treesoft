package org.springframework.dao;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/dao/InvalidDataAccessApiUsageException.class */
public class InvalidDataAccessApiUsageException extends NonTransientDataAccessException {
    public InvalidDataAccessApiUsageException(String msg) {
        super(msg);
    }

    public InvalidDataAccessApiUsageException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
