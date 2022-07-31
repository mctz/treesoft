package org.springframework.transaction.support;

import org.springframework.transaction.support.ResourceHolder;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/support/ResourceHolderSynchronization.class */
public abstract class ResourceHolderSynchronization<H extends ResourceHolder, K> implements TransactionSynchronization {
    private final H resourceHolder;
    private final K resourceKey;
    private volatile boolean holderActive = true;

    public ResourceHolderSynchronization(H resourceHolder, K resourceKey) {
        this.resourceHolder = resourceHolder;
        this.resourceKey = resourceKey;
    }

    @Override // org.springframework.transaction.support.TransactionSynchronization
    public void suspend() {
        if (this.holderActive) {
            TransactionSynchronizationManager.unbindResource(this.resourceKey);
        }
    }

    @Override // org.springframework.transaction.support.TransactionSynchronization
    public void resume() {
        if (this.holderActive) {
            TransactionSynchronizationManager.bindResource(this.resourceKey, this.resourceHolder);
        }
    }

    @Override // org.springframework.transaction.support.TransactionSynchronization, java.io.Flushable
    public void flush() {
        flushResource(this.resourceHolder);
    }

    @Override // org.springframework.transaction.support.TransactionSynchronization
    public void beforeCommit(boolean readOnly) {
    }

    @Override // org.springframework.transaction.support.TransactionSynchronization
    public void beforeCompletion() {
        if (shouldUnbindAtCompletion()) {
            TransactionSynchronizationManager.unbindResource(this.resourceKey);
            this.holderActive = false;
            if (shouldReleaseBeforeCompletion()) {
                releaseResource(this.resourceHolder, this.resourceKey);
            }
        }
    }

    @Override // org.springframework.transaction.support.TransactionSynchronization
    public void afterCommit() {
        if (!shouldReleaseBeforeCompletion()) {
            processResourceAfterCommit(this.resourceHolder);
        }
    }

    @Override // org.springframework.transaction.support.TransactionSynchronization
    public void afterCompletion(int status) {
        boolean releaseNecessary;
        if (shouldUnbindAtCompletion()) {
            if (this.holderActive) {
                this.holderActive = false;
                TransactionSynchronizationManager.unbindResourceIfPossible(this.resourceKey);
                this.resourceHolder.unbound();
                releaseNecessary = true;
            } else {
                releaseNecessary = shouldReleaseAfterCompletion(this.resourceHolder);
            }
            if (releaseNecessary) {
                releaseResource(this.resourceHolder, this.resourceKey);
            }
        } else {
            cleanupResource(this.resourceHolder, this.resourceKey, status == 0);
        }
        this.resourceHolder.reset();
    }

    protected boolean shouldUnbindAtCompletion() {
        return true;
    }

    protected boolean shouldReleaseBeforeCompletion() {
        return true;
    }

    protected boolean shouldReleaseAfterCompletion(H resourceHolder) {
        return !shouldReleaseBeforeCompletion();
    }

    protected void flushResource(H resourceHolder) {
    }

    protected void processResourceAfterCommit(H resourceHolder) {
    }

    protected void releaseResource(H resourceHolder, K resourceKey) {
    }

    protected void cleanupResource(H resourceHolder, K resourceKey, boolean committed) {
    }
}
