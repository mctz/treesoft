package org.springframework.transaction.support;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/support/CallbackPreferringPlatformTransactionManager.class */
public interface CallbackPreferringPlatformTransactionManager extends PlatformTransactionManager {
    <T> T execute(TransactionDefinition transactionDefinition, TransactionCallback<T> transactionCallback) throws TransactionException;
}
