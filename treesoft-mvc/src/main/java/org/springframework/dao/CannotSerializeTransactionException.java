package org.springframework.dao;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/dao/CannotSerializeTransactionException.class */
public class CannotSerializeTransactionException extends PessimisticLockingFailureException {
    public CannotSerializeTransactionException(String msg) {
        super(msg);
    }

    public CannotSerializeTransactionException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
