package org.springframework.transaction.jta;

import javax.transaction.Synchronization;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/jta/SpringJtaSynchronizationAdapter.class */
public class SpringJtaSynchronizationAdapter implements Synchronization {
    protected static final Log logger = LogFactory.getLog(SpringJtaSynchronizationAdapter.class);
    private final TransactionSynchronization springSynchronization;
    private UserTransaction jtaTransaction;
    private boolean beforeCompletionCalled;

    public SpringJtaSynchronizationAdapter(TransactionSynchronization springSynchronization) {
        this.beforeCompletionCalled = false;
        Assert.notNull(springSynchronization, "TransactionSynchronization must not be null");
        this.springSynchronization = springSynchronization;
    }

    public SpringJtaSynchronizationAdapter(TransactionSynchronization springSynchronization, UserTransaction jtaUserTransaction) {
        this(springSynchronization);
        if (jtaUserTransaction != null && !jtaUserTransaction.getClass().getName().startsWith("weblogic.")) {
            this.jtaTransaction = jtaUserTransaction;
        }
    }

    public SpringJtaSynchronizationAdapter(TransactionSynchronization springSynchronization, TransactionManager jtaTransactionManager) {
        this(springSynchronization);
        if (jtaTransactionManager != null && !jtaTransactionManager.getClass().getName().startsWith("weblogic.")) {
            this.jtaTransaction = new UserTransactionAdapter(jtaTransactionManager);
        }
    }

    public void beforeCompletion() {
        try {
            try {
                boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
                this.springSynchronization.beforeCommit(readOnly);
                this.beforeCompletionCalled = true;
                this.springSynchronization.beforeCompletion();
            } catch (Error err) {
                setRollbackOnlyIfPossible();
                throw err;
            } catch (RuntimeException ex) {
                setRollbackOnlyIfPossible();
                throw ex;
            }
        } catch (Throwable th) {
            this.beforeCompletionCalled = true;
            this.springSynchronization.beforeCompletion();
            throw th;
        }
    }

    private void setRollbackOnlyIfPossible() {
        if (this.jtaTransaction != null) {
            try {
                this.jtaTransaction.setRollbackOnly();
                return;
            } catch (UnsupportedOperationException ex) {
                logger.debug("JTA transaction handle does not support setRollbackOnly method - relying on JTA provider to mark the transaction as rollback-only based on the exception thrown from beforeCompletion", ex);
                return;
            } catch (Throwable ex2) {
                logger.error("Could not set JTA transaction rollback-only", ex2);
                return;
            }
        }
        logger.debug("No JTA transaction handle available and/or running on WebLogic - relying on JTA provider to mark the transaction as rollback-only based on the exception thrown from beforeCompletion");
    }

    public void afterCompletion(int status) {
        if (!this.beforeCompletionCalled) {
            this.springSynchronization.beforeCompletion();
        }
        switch (status) {
            case 3:
                this.springSynchronization.afterCompletion(0);
                return;
            case 4:
                this.springSynchronization.afterCompletion(1);
                return;
            default:
                this.springSynchronization.afterCompletion(2);
                return;
        }
    }
}
