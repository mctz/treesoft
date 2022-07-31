package org.springframework.transaction;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/NestedTransactionNotSupportedException.class */
public class NestedTransactionNotSupportedException extends CannotCreateTransactionException {
    public NestedTransactionNotSupportedException(String msg) {
        super(msg);
    }

    public NestedTransactionNotSupportedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
