package org.springframework.transaction;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/CannotCreateTransactionException.class */
public class CannotCreateTransactionException extends TransactionException {
    public CannotCreateTransactionException(String msg) {
        super(msg);
    }

    public CannotCreateTransactionException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
