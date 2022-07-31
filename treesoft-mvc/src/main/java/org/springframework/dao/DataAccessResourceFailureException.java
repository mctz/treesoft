package org.springframework.dao;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/dao/DataAccessResourceFailureException.class */
public class DataAccessResourceFailureException extends NonTransientDataAccessResourceException {
    public DataAccessResourceFailureException(String msg) {
        super(msg);
    }

    public DataAccessResourceFailureException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
