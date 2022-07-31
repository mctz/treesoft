package org.springframework.transaction.jta;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import org.springframework.util.Assert;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/jta/SimpleTransactionFactory.class */
public class SimpleTransactionFactory implements TransactionFactory {
    private final TransactionManager transactionManager;

    public SimpleTransactionFactory(TransactionManager transactionManager) {
        Assert.notNull(transactionManager, "TransactionManager must not be null");
        this.transactionManager = transactionManager;
    }

    @Override // org.springframework.transaction.jta.TransactionFactory
    public Transaction createTransaction(String name, int timeout) throws NotSupportedException, SystemException {
        if (timeout >= 0) {
            this.transactionManager.setTransactionTimeout(timeout);
        }
        this.transactionManager.begin();
        return new ManagedTransactionAdapter(this.transactionManager);
    }

    @Override // org.springframework.transaction.jta.TransactionFactory
    public boolean supportsResourceAdapterManagedTransactions() {
        return false;
    }
}
