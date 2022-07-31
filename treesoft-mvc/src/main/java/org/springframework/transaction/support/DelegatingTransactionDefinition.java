package org.springframework.transaction.support;

import java.io.Serializable;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.util.Assert;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/support/DelegatingTransactionDefinition.class */
public abstract class DelegatingTransactionDefinition implements TransactionDefinition, Serializable {
    private final TransactionDefinition targetDefinition;

    public DelegatingTransactionDefinition(TransactionDefinition targetDefinition) {
        Assert.notNull(targetDefinition, "Target definition must not be null");
        this.targetDefinition = targetDefinition;
    }

    @Override // org.springframework.transaction.TransactionDefinition
    public int getPropagationBehavior() {
        return this.targetDefinition.getPropagationBehavior();
    }

    @Override // org.springframework.transaction.TransactionDefinition
    public int getIsolationLevel() {
        return this.targetDefinition.getIsolationLevel();
    }

    @Override // org.springframework.transaction.TransactionDefinition
    public int getTimeout() {
        return this.targetDefinition.getTimeout();
    }

    @Override // org.springframework.transaction.TransactionDefinition
    public boolean isReadOnly() {
        return this.targetDefinition.isReadOnly();
    }

    @Override // org.springframework.transaction.TransactionDefinition
    public String getName() {
        return this.targetDefinition.getName();
    }

    public boolean equals(Object obj) {
        return this.targetDefinition.equals(obj);
    }

    public int hashCode() {
        return this.targetDefinition.hashCode();
    }

    public String toString() {
        return this.targetDefinition.toString();
    }
}
