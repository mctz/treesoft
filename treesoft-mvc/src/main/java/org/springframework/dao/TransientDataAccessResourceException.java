package org.springframework.dao;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/dao/TransientDataAccessResourceException.class */
public class TransientDataAccessResourceException extends TransientDataAccessException {
    public TransientDataAccessResourceException(String msg) {
        super(msg);
    }

    public TransientDataAccessResourceException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
