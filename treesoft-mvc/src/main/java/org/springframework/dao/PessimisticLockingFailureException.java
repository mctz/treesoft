package org.springframework.dao;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/dao/PessimisticLockingFailureException.class */
public class PessimisticLockingFailureException extends ConcurrencyFailureException {
    public PessimisticLockingFailureException(String msg) {
        super(msg);
    }

    public PessimisticLockingFailureException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
