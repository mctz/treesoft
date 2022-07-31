package org.springframework.transaction.support;

import org.springframework.transaction.TransactionStatus;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/support/TransactionCallback.class */
public interface TransactionCallback<T> {
    T doInTransaction(TransactionStatus transactionStatus);
}
