package org.springframework.transaction.support;

import org.springframework.core.Ordered;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/support/TransactionSynchronizationAdapter.class */
public abstract class TransactionSynchronizationAdapter implements TransactionSynchronization, Ordered {
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

    @Override // org.springframework.transaction.support.TransactionSynchronization
    public void suspend() {
    }

    @Override // org.springframework.transaction.support.TransactionSynchronization
    public void resume() {
    }

    @Override // org.springframework.transaction.support.TransactionSynchronization, java.io.Flushable
    public void flush() {
    }

    @Override // org.springframework.transaction.support.TransactionSynchronization
    public void beforeCommit(boolean readOnly) {
    }

    @Override // org.springframework.transaction.support.TransactionSynchronization
    public void beforeCompletion() {
    }

    @Override // org.springframework.transaction.support.TransactionSynchronization
    public void afterCommit() {
    }

    @Override // org.springframework.transaction.support.TransactionSynchronization
    public void afterCompletion(int status) {
    }
}
