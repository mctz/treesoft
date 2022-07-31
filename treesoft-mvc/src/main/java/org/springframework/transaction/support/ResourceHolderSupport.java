package org.springframework.transaction.support;

import java.util.Date;
import org.springframework.transaction.TransactionTimedOutException;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/support/ResourceHolderSupport.class */
public abstract class ResourceHolderSupport implements ResourceHolder {
    private Date deadline;
    private boolean synchronizedWithTransaction = false;
    private boolean rollbackOnly = false;
    private int referenceCount = 0;
    private boolean isVoid = false;

    public void setSynchronizedWithTransaction(boolean synchronizedWithTransaction) {
        this.synchronizedWithTransaction = synchronizedWithTransaction;
    }

    public boolean isSynchronizedWithTransaction() {
        return this.synchronizedWithTransaction;
    }

    public void setRollbackOnly() {
        this.rollbackOnly = true;
    }

    public boolean isRollbackOnly() {
        return this.rollbackOnly;
    }

    public void setTimeoutInSeconds(int seconds) {
        setTimeoutInMillis(seconds * 1000);
    }

    public void setTimeoutInMillis(long millis) {
        this.deadline = new Date(System.currentTimeMillis() + millis);
    }

    public boolean hasTimeout() {
        return this.deadline != null;
    }

    public Date getDeadline() {
        return this.deadline;
    }

    public int getTimeToLiveInSeconds() {
        double diff = getTimeToLiveInMillis() / 1000.0d;
        int secs = (int) Math.ceil(diff);
        checkTransactionTimeout(secs <= 0);
        return secs;
    }

    public long getTimeToLiveInMillis() throws TransactionTimedOutException {
        if (this.deadline == null) {
            throw new IllegalStateException("No timeout specified for this resource holder");
        }
        long timeToLive = this.deadline.getTime() - System.currentTimeMillis();
        checkTransactionTimeout(timeToLive <= 0);
        return timeToLive;
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [java.lang.Throwable, org.springframework.transaction.TransactionTimedOutException] */
    private void checkTransactionTimeout(boolean deadlineReached) throws TransactionTimedOutException {
        if (deadlineReached) {
            setRollbackOnly();
            throw new TransactionTimedOutException("Transaction timed out: deadline was " + this.deadline);
        }
    }

    public void requested() {
        this.referenceCount++;
    }

    public void released() {
        this.referenceCount--;
    }

    public boolean isOpen() {
        return this.referenceCount > 0;
    }

    public void clear() {
        this.synchronizedWithTransaction = false;
        this.rollbackOnly = false;
        this.deadline = null;
    }

    @Override // org.springframework.transaction.support.ResourceHolder
    public void reset() {
        clear();
        this.referenceCount = 0;
    }

    @Override // org.springframework.transaction.support.ResourceHolder
    public void unbound() {
        this.isVoid = true;
    }

    @Override // org.springframework.transaction.support.ResourceHolder
    public boolean isVoid() {
        return this.isVoid;
    }
}
