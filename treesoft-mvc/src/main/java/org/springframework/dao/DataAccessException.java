package org.springframework.dao;

import org.springframework.core.NestedRuntimeException;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/dao/DataAccessException.class */
public abstract class DataAccessException extends NestedRuntimeException {
    public DataAccessException(String msg) {
        super(msg);
    }

    public DataAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
