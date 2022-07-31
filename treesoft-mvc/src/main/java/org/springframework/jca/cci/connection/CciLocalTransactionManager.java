package org.springframework.jca.cci.connection;

import javax.resource.NotSupportedException;
import javax.resource.ResourceException;
import javax.resource.cci.Connection;
import javax.resource.cci.ConnectionFactory;
import javax.resource.spi.LocalTransactionException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.ResourceTransactionManager;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/jca/cci/connection/CciLocalTransactionManager.class */
public class CciLocalTransactionManager extends AbstractPlatformTransactionManager implements ResourceTransactionManager, InitializingBean {
    private ConnectionFactory connectionFactory;

    public CciLocalTransactionManager() {
    }

    public CciLocalTransactionManager(ConnectionFactory connectionFactory) {
        setConnectionFactory(connectionFactory);
        afterPropertiesSet();
    }

    public void setConnectionFactory(ConnectionFactory cf) {
        if (cf instanceof TransactionAwareConnectionFactoryProxy) {
            this.connectionFactory = ((TransactionAwareConnectionFactoryProxy) cf).getTargetConnectionFactory();
        } else {
            this.connectionFactory = cf;
        }
    }

    public ConnectionFactory getConnectionFactory() {
        return this.connectionFactory;
    }

    public void afterPropertiesSet() {
        if (getConnectionFactory() == null) {
            throw new IllegalArgumentException("Property 'connectionFactory' is required");
        }
    }

    @Override // org.springframework.transaction.support.ResourceTransactionManager
    public Object getResourceFactory() {
        return getConnectionFactory();
    }

    @Override // org.springframework.transaction.support.AbstractPlatformTransactionManager
    protected Object doGetTransaction() {
        CciLocalTransactionObject txObject = new CciLocalTransactionObject();
        ConnectionHolder conHolder = (ConnectionHolder) TransactionSynchronizationManager.getResource(getConnectionFactory());
        txObject.setConnectionHolder(conHolder);
        return txObject;
    }

    @Override // org.springframework.transaction.support.AbstractPlatformTransactionManager
    protected boolean isExistingTransaction(Object transaction) {
        CciLocalTransactionObject txObject = (CciLocalTransactionObject) transaction;
        return txObject.getConnectionHolder() != null;
    }

    /* JADX WARN: Type inference failed for: r0v4, types: [java.lang.Throwable, org.springframework.transaction.TransactionSystemException] */
    /* JADX WARN: Type inference failed for: r0v6, types: [java.lang.Throwable, org.springframework.transaction.CannotCreateTransactionException] */
    /* JADX WARN: Type inference failed for: r0v8, types: [java.lang.Throwable, org.springframework.transaction.CannotCreateTransactionException] */
    @Override // org.springframework.transaction.support.AbstractPlatformTransactionManager
    protected void doBegin(Object transaction, TransactionDefinition definition) {
        CciLocalTransactionObject txObject = (CciLocalTransactionObject) transaction;
        Connection con = null;
        try {
            con = getConnectionFactory().getConnection();
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Acquired Connection [" + con + "] for local CCI transaction");
            }
            txObject.setConnectionHolder(new ConnectionHolder(con));
            txObject.getConnectionHolder().setSynchronizedWithTransaction(true);
            con.getLocalTransaction().begin();
            int timeout = determineTimeout(definition);
            if (timeout != -1) {
                txObject.getConnectionHolder().setTimeoutInSeconds(timeout);
            }
            TransactionSynchronizationManager.bindResource(getConnectionFactory(), txObject.getConnectionHolder());
        } catch (LocalTransactionException ex) {
            ConnectionFactoryUtils.releaseConnection(con, getConnectionFactory());
            throw new CannotCreateTransactionException("Could not begin local CCI transaction", ex);
        } catch (NotSupportedException ex2) {
            ConnectionFactoryUtils.releaseConnection(con, getConnectionFactory());
            throw new CannotCreateTransactionException("CCI Connection does not support local transactions", ex2);
        } catch (Throwable ex3) {
            ConnectionFactoryUtils.releaseConnection(con, getConnectionFactory());
            throw new TransactionSystemException("Unexpected failure on begin of CCI local transaction", ex3);
        }
    }

    @Override // org.springframework.transaction.support.AbstractPlatformTransactionManager
    protected Object doSuspend(Object transaction) {
        CciLocalTransactionObject txObject = (CciLocalTransactionObject) transaction;
        txObject.setConnectionHolder(null);
        return TransactionSynchronizationManager.unbindResource(getConnectionFactory());
    }

    @Override // org.springframework.transaction.support.AbstractPlatformTransactionManager
    protected void doResume(Object transaction, Object suspendedResources) {
        ConnectionHolder conHolder = (ConnectionHolder) suspendedResources;
        TransactionSynchronizationManager.bindResource(getConnectionFactory(), conHolder);
    }

    protected boolean isRollbackOnly(Object transaction) throws TransactionException {
        CciLocalTransactionObject txObject = (CciLocalTransactionObject) transaction;
        return txObject.getConnectionHolder().isRollbackOnly();
    }

    /* JADX WARN: Type inference failed for: r0v8, types: [java.lang.Throwable, org.springframework.transaction.TransactionSystemException] */
    /* JADX WARN: Type inference failed for: r0v9, types: [java.lang.Throwable, org.springframework.transaction.TransactionSystemException] */
    @Override // org.springframework.transaction.support.AbstractPlatformTransactionManager
    protected void doCommit(DefaultTransactionStatus status) {
        CciLocalTransactionObject txObject = (CciLocalTransactionObject) status.getTransaction();
        Connection con = txObject.getConnectionHolder().getConnection();
        if (status.isDebug()) {
            this.logger.debug("Committing CCI local transaction on Connection [" + con + "]");
        }
        try {
            con.getLocalTransaction().commit();
        } catch (ResourceException ex) {
            throw new TransactionSystemException("Unexpected failure on commit of CCI local transaction", ex);
        } catch (LocalTransactionException ex2) {
            throw new TransactionSystemException("Could not commit CCI local transaction", ex2);
        }
    }

    /* JADX WARN: Type inference failed for: r0v8, types: [java.lang.Throwable, org.springframework.transaction.TransactionSystemException] */
    /* JADX WARN: Type inference failed for: r0v9, types: [java.lang.Throwable, org.springframework.transaction.TransactionSystemException] */
    @Override // org.springframework.transaction.support.AbstractPlatformTransactionManager
    protected void doRollback(DefaultTransactionStatus status) {
        CciLocalTransactionObject txObject = (CciLocalTransactionObject) status.getTransaction();
        Connection con = txObject.getConnectionHolder().getConnection();
        if (status.isDebug()) {
            this.logger.debug("Rolling back CCI local transaction on Connection [" + con + "]");
        }
        try {
            con.getLocalTransaction().rollback();
        } catch (ResourceException ex) {
            throw new TransactionSystemException("Unexpected failure on rollback of CCI local transaction", ex);
        } catch (LocalTransactionException ex2) {
            throw new TransactionSystemException("Could not roll back CCI local transaction", ex2);
        }
    }

    @Override // org.springframework.transaction.support.AbstractPlatformTransactionManager
    protected void doSetRollbackOnly(DefaultTransactionStatus status) {
        CciLocalTransactionObject txObject = (CciLocalTransactionObject) status.getTransaction();
        if (status.isDebug()) {
            this.logger.debug("Setting CCI local transaction [" + txObject.getConnectionHolder().getConnection() + "] rollback-only");
        }
        txObject.getConnectionHolder().setRollbackOnly();
    }

    @Override // org.springframework.transaction.support.AbstractPlatformTransactionManager
    protected void doCleanupAfterCompletion(Object transaction) {
        CciLocalTransactionObject txObject = (CciLocalTransactionObject) transaction;
        TransactionSynchronizationManager.unbindResource(getConnectionFactory());
        txObject.getConnectionHolder().clear();
        Connection con = txObject.getConnectionHolder().getConnection();
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Releasing CCI Connection [" + con + "] after transaction");
        }
        ConnectionFactoryUtils.releaseConnection(con, getConnectionFactory());
    }

    /* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/jca/cci/connection/CciLocalTransactionManager$CciLocalTransactionObject.class */
    private static class CciLocalTransactionObject {
        private ConnectionHolder connectionHolder;

        private CciLocalTransactionObject() {
        }

        public void setConnectionHolder(ConnectionHolder connectionHolder) {
            this.connectionHolder = connectionHolder;
        }

        public ConnectionHolder getConnectionHolder() {
            return this.connectionHolder;
        }
    }
}
