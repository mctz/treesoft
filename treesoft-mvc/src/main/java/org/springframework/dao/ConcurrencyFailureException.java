package org.springframework.dao;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/dao/ConcurrencyFailureException.class */
public class ConcurrencyFailureException extends TransientDataAccessException {
    public ConcurrencyFailureException(String msg) {
        super(msg);
    }

    public ConcurrencyFailureException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
