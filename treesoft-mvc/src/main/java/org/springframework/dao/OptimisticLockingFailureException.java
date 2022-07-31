package org.springframework.dao;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/dao/OptimisticLockingFailureException.class */
public class OptimisticLockingFailureException extends ConcurrencyFailureException {
    public OptimisticLockingFailureException(String msg) {
        super(msg);
    }

    public OptimisticLockingFailureException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
