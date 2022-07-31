package org.springframework.transaction;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/NoTransactionException.class */
public class NoTransactionException extends TransactionUsageException {
    public NoTransactionException(String msg) {
        super(msg);
    }

    public NoTransactionException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
