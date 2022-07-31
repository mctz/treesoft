package org.springframework.transaction.support;

import java.lang.reflect.UndeclaredThrowableException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.TransactionSystemException;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/support/TransactionTemplate.class */
public class TransactionTemplate extends DefaultTransactionDefinition implements TransactionOperations, InitializingBean {
    protected final Log logger = LogFactory.getLog(getClass());
    private PlatformTransactionManager transactionManager;

    public TransactionTemplate() {
    }

    public TransactionTemplate(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public TransactionTemplate(PlatformTransactionManager transactionManager, TransactionDefinition transactionDefinition) {
        super(transactionDefinition);
        this.transactionManager = transactionManager;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public PlatformTransactionManager getTransactionManager() {
        return this.transactionManager;
    }

    public void afterPropertiesSet() {
        if (this.transactionManager == null) {
            throw new IllegalArgumentException("Property 'transactionManager' is required");
        }
    }

    @Override // org.springframework.transaction.support.TransactionOperations
    public <T> T execute(TransactionCallback<T> action) throws TransactionException {
        if (this.transactionManager instanceof CallbackPreferringPlatformTransactionManager) {
            return (T) ((CallbackPreferringPlatformTransactionManager) this.transactionManager).execute(this, action);
        }
        TransactionStatus status = this.transactionManager.getTransaction(this);
        try {
            T result = action.doInTransaction(status);
            this.transactionManager.commit(status);
            return result;
        } catch (Error err) {
            rollbackOnException(status, err);
            throw err;
        } catch (RuntimeException ex) {
            rollbackOnException(status, ex);
            throw ex;
        } catch (Exception ex2) {
            rollbackOnException(status, ex2);
            throw new UndeclaredThrowableException(ex2, "TransactionCallback threw undeclared checked exception");
        }
    }

    /* JADX WARN: Type inference failed for: r7v2, types: [java.lang.Throwable, org.springframework.transaction.TransactionSystemException] */
    private void rollbackOnException(TransactionStatus status, Throwable ex) throws TransactionException {
        this.logger.debug("Initiating transaction rollback on application exception", ex);
        try {
            this.transactionManager.rollback(status);
        } catch (Error err) {
            this.logger.error("Application exception overridden by rollback error", ex);
            throw err;
        } catch (RuntimeException ex2) {
            this.logger.error("Application exception overridden by rollback exception", ex);
            throw ex2;
        } catch (TransactionSystemException e) {
            this.logger.error("Application exception overridden by rollback exception", ex);
            e.initApplicationException(ex);
            throw e;
        }
    }
}
