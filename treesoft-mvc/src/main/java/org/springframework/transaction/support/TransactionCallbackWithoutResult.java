package org.springframework.transaction.support;

import org.springframework.transaction.TransactionStatus;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/support/TransactionCallbackWithoutResult.class */
public abstract class TransactionCallbackWithoutResult implements TransactionCallback<Object> {
    protected abstract void doInTransactionWithoutResult(TransactionStatus transactionStatus);

    @Override // org.springframework.transaction.support.TransactionCallback
    public final Object doInTransaction(TransactionStatus status) {
        doInTransactionWithoutResult(status);
        return null;
    }
}
