package org.springframework.transaction;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/PlatformTransactionManager.class */
public interface PlatformTransactionManager {
    TransactionStatus getTransaction(TransactionDefinition transactionDefinition) throws TransactionException;

    void commit(TransactionStatus transactionStatus) throws TransactionException;

    void rollback(TransactionStatus transactionStatus) throws TransactionException;
}
