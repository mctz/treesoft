package org.springframework.transaction.jta;

import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.support.SmartTransactionObject;
import org.springframework.transaction.support.TransactionSynchronizationUtils;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/jta/JtaTransactionObject.class */
public class JtaTransactionObject implements SmartTransactionObject {
    private final UserTransaction userTransaction;

    public JtaTransactionObject(UserTransaction userTransaction) {
        this.userTransaction = userTransaction;
    }

    public final UserTransaction getUserTransaction() {
        return this.userTransaction;
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [java.lang.Throwable, org.springframework.transaction.TransactionSystemException] */
    @Override // org.springframework.transaction.support.SmartTransactionObject
    public boolean isRollbackOnly() {
        if (this.userTransaction == null) {
            return false;
        }
        try {
            int jtaStatus = this.userTransaction.getStatus();
            return jtaStatus == 1 || jtaStatus == 4;
        } catch (SystemException ex) {
            throw new TransactionSystemException("JTA failure on getStatus", ex);
        }
    }

    @Override // org.springframework.transaction.support.SmartTransactionObject, java.io.Flushable
    public void flush() {
        TransactionSynchronizationUtils.triggerFlush();
    }
}
