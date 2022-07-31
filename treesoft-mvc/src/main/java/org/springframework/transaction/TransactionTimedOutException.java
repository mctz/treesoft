package org.springframework.transaction;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/TransactionTimedOutException.class */
public class TransactionTimedOutException extends TransactionException {
    public TransactionTimedOutException(String msg) {
        super(msg);
    }

    public TransactionTimedOutException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
