package org.springframework.transaction.support;

import org.springframework.transaction.NestedTransactionNotSupportedException;
import org.springframework.transaction.SavepointManager;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/support/DefaultTransactionStatus.class */
public class DefaultTransactionStatus extends AbstractTransactionStatus {
    private final Object transaction;
    private final boolean newTransaction;
    private final boolean newSynchronization;
    private final boolean readOnly;
    private final boolean debug;
    private final Object suspendedResources;

    public DefaultTransactionStatus(Object transaction, boolean newTransaction, boolean newSynchronization, boolean readOnly, boolean debug, Object suspendedResources) {
        this.transaction = transaction;
        this.newTransaction = newTransaction;
        this.newSynchronization = newSynchronization;
        this.readOnly = readOnly;
        this.debug = debug;
        this.suspendedResources = suspendedResources;
    }

    public Object getTransaction() {
        return this.transaction;
    }

    public boolean hasTransaction() {
        return this.transaction != null;
    }

    @Override // org.springframework.transaction.TransactionStatus
    public boolean isNewTransaction() {
        return hasTransaction() && this.newTransaction;
    }

    public boolean isNewSynchronization() {
        return this.newSynchronization;
    }

    public boolean isReadOnly() {
        return this.readOnly;
    }

    public boolean isDebug() {
        return this.debug;
    }

    public Object getSuspendedResources() {
        return this.suspendedResources;
    }

    @Override // org.springframework.transaction.support.AbstractTransactionStatus
    public boolean isGlobalRollbackOnly() {
        return (this.transaction instanceof SmartTransactionObject) && ((SmartTransactionObject) this.transaction).isRollbackOnly();
    }

    @Override // org.springframework.transaction.support.AbstractTransactionStatus, org.springframework.transaction.TransactionStatus, java.io.Flushable
    public void flush() {
        if (this.transaction instanceof SmartTransactionObject) {
            ((SmartTransactionObject) this.transaction).flush();
        }
    }

    /* JADX WARN: Type inference failed for: r0v5, types: [java.lang.Throwable, org.springframework.transaction.NestedTransactionNotSupportedException] */
    @Override // org.springframework.transaction.support.AbstractTransactionStatus
    protected SavepointManager getSavepointManager() {
        if (!isTransactionSavepointManager()) {
            throw new NestedTransactionNotSupportedException("Transaction object [" + getTransaction() + "] does not support savepoints");
        }
        return (SavepointManager) getTransaction();
    }

    public boolean isTransactionSavepointManager() {
        return getTransaction() instanceof SavepointManager;
    }
}
