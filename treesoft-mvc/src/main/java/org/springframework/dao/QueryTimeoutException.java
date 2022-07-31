package org.springframework.dao;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/dao/QueryTimeoutException.class */
public class QueryTimeoutException extends TransientDataAccessException {
    public QueryTimeoutException(String msg) {
        super(msg);
    }

    public QueryTimeoutException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
