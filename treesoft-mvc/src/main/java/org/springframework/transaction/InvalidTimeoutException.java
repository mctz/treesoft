package org.springframework.transaction;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/InvalidTimeoutException.class */
public class InvalidTimeoutException extends TransactionUsageException {
    private int timeout;

    public InvalidTimeoutException(String msg, int timeout) {
        super(msg);
        this.timeout = timeout;
    }

    public int getTimeout() {
        return this.timeout;
    }
}
