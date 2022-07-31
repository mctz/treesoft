package org.springframework.transaction.jta;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import javax.transaction.xa.XAResource;
import org.springframework.util.Assert;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/jta/ManagedTransactionAdapter.class */
public class ManagedTransactionAdapter implements Transaction {
    private final TransactionManager transactionManager;

    public ManagedTransactionAdapter(TransactionManager transactionManager) throws SystemException {
        Assert.notNull(transactionManager, "TransactionManager must not be null");
        this.transactionManager = transactionManager;
    }

    public final TransactionManager getTransactionManager() {
        return this.transactionManager;
    }

    public void commit() throws RollbackException, HeuristicMixedException, HeuristicRollbackException, SecurityException, SystemException {
        this.transactionManager.commit();
    }

    public void rollback() throws SystemException {
        this.transactionManager.rollback();
    }

    public void setRollbackOnly() throws SystemException {
        this.transactionManager.setRollbackOnly();
    }

    public int getStatus() throws SystemException {
        return this.transactionManager.getStatus();
    }

    public boolean enlistResource(XAResource xaRes) throws RollbackException, SystemException {
        return this.transactionManager.getTransaction().enlistResource(xaRes);
    }

    public boolean delistResource(XAResource xaRes, int flag) throws SystemException {
        return this.transactionManager.getTransaction().delistResource(xaRes, flag);
    }

    public void registerSynchronization(Synchronization sync) throws RollbackException, SystemException {
        this.transactionManager.getTransaction().registerSynchronization(sync);
    }
}
