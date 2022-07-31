package org.springframework.dao;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/dao/TypeMismatchDataAccessException.class */
public class TypeMismatchDataAccessException extends InvalidDataAccessResourceUsageException {
    public TypeMismatchDataAccessException(String msg) {
        super(msg);
    }

    public TypeMismatchDataAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
