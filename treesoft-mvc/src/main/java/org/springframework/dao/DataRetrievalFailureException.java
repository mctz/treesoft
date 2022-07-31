package org.springframework.dao;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/dao/DataRetrievalFailureException.class */
public class DataRetrievalFailureException extends NonTransientDataAccessException {
    public DataRetrievalFailureException(String msg) {
        super(msg);
    }

    public DataRetrievalFailureException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
