package org.springframework.transaction.jta;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/jta/TransactionFactory.class */
public interface TransactionFactory {
    Transaction createTransaction(String str, int i) throws NotSupportedException, SystemException;

    boolean supportsResourceAdapterManagedTransactions();
}
