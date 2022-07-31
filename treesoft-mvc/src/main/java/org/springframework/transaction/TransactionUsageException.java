package org.springframework.transaction;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/TransactionUsageException.class */
public class TransactionUsageException extends TransactionException {
    public TransactionUsageException(String msg) {
        super(msg);
    }

    public TransactionUsageException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
