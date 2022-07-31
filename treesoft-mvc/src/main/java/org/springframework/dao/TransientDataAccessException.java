package org.springframework.dao;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/dao/TransientDataAccessException.class */
public abstract class TransientDataAccessException extends DataAccessException {
    public TransientDataAccessException(String msg) {
        super(msg);
    }

    public TransientDataAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
