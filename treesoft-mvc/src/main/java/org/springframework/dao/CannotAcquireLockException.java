package org.springframework.dao;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/dao/CannotAcquireLockException.class */
public class CannotAcquireLockException extends PessimisticLockingFailureException {
    public CannotAcquireLockException(String msg) {
        super(msg);
    }

    public CannotAcquireLockException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
