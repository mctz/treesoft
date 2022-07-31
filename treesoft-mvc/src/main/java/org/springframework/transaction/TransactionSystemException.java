package org.springframework.transaction;

import org.springframework.util.Assert;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/TransactionSystemException.class */
public class TransactionSystemException extends TransactionException {
    private Throwable applicationException;

    public TransactionSystemException(String msg) {
        super(msg);
    }

    public TransactionSystemException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public void initApplicationException(Throwable ex) {
        Assert.notNull(ex, "Application exception must not be null");
        if (this.applicationException != null) {
            throw new IllegalStateException("Already holding an application exception: " + this.applicationException);
        }
        this.applicationException = ex;
    }

    public final Throwable getApplicationException() {
        return this.applicationException;
    }

    public Throwable getOriginalException() {
        return this.applicationException != null ? this.applicationException : getCause();
    }

    public boolean contains(Class<?> exType) {
        return super.contains(exType) || (exType != null && exType.isInstance(this.applicationException));
    }
}
