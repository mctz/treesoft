package org.springframework.dao;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/dao/NonTransientDataAccessResourceException.class */
public class NonTransientDataAccessResourceException extends NonTransientDataAccessException {
    public NonTransientDataAccessResourceException(String msg) {
        super(msg);
    }

    public NonTransientDataAccessResourceException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
