package org.springframework.dao;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/dao/EmptyResultDataAccessException.class */
public class EmptyResultDataAccessException extends IncorrectResultSizeDataAccessException {
    public EmptyResultDataAccessException(int expectedSize) {
        super(expectedSize, 0);
    }

    public EmptyResultDataAccessException(String msg, int expectedSize) {
        super(msg, expectedSize, 0);
    }

    public EmptyResultDataAccessException(String msg, int expectedSize, Throwable ex) {
        super(msg, expectedSize, 0, ex);
    }
}
