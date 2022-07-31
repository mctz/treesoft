package org.springframework.transaction.jta;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.transaction.InvalidTransactionException;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionSystemException;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/jta/WebLogicJtaTransactionManager.class */
public class WebLogicJtaTransactionManager extends JtaTransactionManager {
    private static final String USER_TRANSACTION_CLASS_NAME = "weblogic.transaction.UserTransaction";
    private static final String CLIENT_TRANSACTION_MANAGER_CLASS_NAME = "weblogic.transaction.ClientTransactionManager";
    private static final String TRANSACTION_CLASS_NAME = "weblogic.transaction.Transaction";
    private static final String TRANSACTION_HELPER_CLASS_NAME = "weblogic.transaction.TransactionHelper";
    private static final String ISOLATION_LEVEL_KEY = "ISOLATION LEVEL";
    private boolean weblogicUserTransactionAvailable;
    private Method beginWithNameMethod;
    private Method beginWithNameAndTimeoutMethod;
    private boolean weblogicTransactionManagerAvailable;
    private Method forceResumeMethod;
    private Method setPropertyMethod;
    private Object transactionHelper;

    @Override // org.springframework.transaction.jta.JtaTransactionManager
    public void afterPropertiesSet() throws TransactionSystemException {
        super.afterPropertiesSet();
        loadWebLogicTransactionClasses();
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Throwable, org.springframework.transaction.TransactionSystemException] */
    /* JADX WARN: Type inference failed for: r0v2, types: [java.lang.Throwable, org.springframework.transaction.TransactionSystemException] */
    @Override // org.springframework.transaction.jta.JtaTransactionManager
    protected UserTransaction retrieveUserTransaction() throws TransactionSystemException {
        loadWebLogicTransactionHelper();
        try {
            this.logger.debug("Retrieving JTA UserTransaction from WebLogic TransactionHelper");
            Method getUserTransactionMethod = this.transactionHelper.getClass().getMethod("getUserTransaction", new Class[0]);
            return (UserTransaction) getUserTransactionMethod.invoke(this.transactionHelper, new Object[0]);
        } catch (InvocationTargetException ex) {
            throw new TransactionSystemException("WebLogic's TransactionHelper.getUserTransaction() method failed", ex.getTargetException());
        } catch (Exception ex2) {
            throw new TransactionSystemException("Could not invoke WebLogic's TransactionHelper.getUserTransaction() method", ex2);
        }
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Throwable, org.springframework.transaction.TransactionSystemException] */
    /* JADX WARN: Type inference failed for: r0v2, types: [java.lang.Throwable, org.springframework.transaction.TransactionSystemException] */
    @Override // org.springframework.transaction.jta.JtaTransactionManager
    protected TransactionManager retrieveTransactionManager() throws TransactionSystemException {
        loadWebLogicTransactionHelper();
        try {
            this.logger.debug("Retrieving JTA TransactionManager from WebLogic TransactionHelper");
            Method getTransactionManagerMethod = this.transactionHelper.getClass().getMethod("getTransactionManager", new Class[0]);
            return (TransactionManager) getTransactionManagerMethod.invoke(this.transactionHelper, new Object[0]);
        } catch (InvocationTargetException ex) {
            throw new TransactionSystemException("WebLogic's TransactionHelper.getTransactionManager() method failed", ex.getTargetException());
        } catch (Exception ex2) {
            throw new TransactionSystemException("Could not invoke WebLogic's TransactionHelper.getTransactionManager() method", ex2);
        }
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [java.lang.Throwable, org.springframework.transaction.TransactionSystemException] */
    /* JADX WARN: Type inference failed for: r0v3, types: [java.lang.Throwable, org.springframework.transaction.TransactionSystemException] */
    private void loadWebLogicTransactionHelper() throws TransactionSystemException {
        if (this.transactionHelper == null) {
            try {
                Class<?> transactionHelperClass = getClass().getClassLoader().loadClass(TRANSACTION_HELPER_CLASS_NAME);
                Method getTransactionHelperMethod = transactionHelperClass.getMethod("getTransactionHelper", new Class[0]);
                this.transactionHelper = getTransactionHelperMethod.invoke(null, new Object[0]);
                this.logger.debug("WebLogic TransactionHelper found");
            } catch (InvocationTargetException ex) {
                throw new TransactionSystemException("WebLogic's TransactionHelper.getTransactionHelper() method failed", ex.getTargetException());
            } catch (Exception ex2) {
                throw new TransactionSystemException("Could not initialize WebLogicJtaTransactionManager because WebLogic API classes are not available", ex2);
            }
        }
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [java.lang.Throwable, org.springframework.transaction.TransactionSystemException] */
    private void loadWebLogicTransactionClasses() throws TransactionSystemException {
        try {
            Class<?> userTransactionClass = getClass().getClassLoader().loadClass(USER_TRANSACTION_CLASS_NAME);
            this.weblogicUserTransactionAvailable = userTransactionClass.isInstance(getUserTransaction());
            if (this.weblogicUserTransactionAvailable) {
                this.beginWithNameMethod = userTransactionClass.getMethod("begin", String.class);
                this.beginWithNameAndTimeoutMethod = userTransactionClass.getMethod("begin", String.class, Integer.TYPE);
                this.logger.info("Support for WebLogic transaction names available");
            } else {
                this.logger.info("Support for WebLogic transaction names not available");
            }
            Class<?> transactionManagerClass = getClass().getClassLoader().loadClass(CLIENT_TRANSACTION_MANAGER_CLASS_NAME);
            this.logger.debug("WebLogic ClientTransactionManager found");
            this.weblogicTransactionManagerAvailable = transactionManagerClass.isInstance(getTransactionManager());
            if (this.weblogicTransactionManagerAvailable) {
                Class<?> transactionClass = getClass().getClassLoader().loadClass(TRANSACTION_CLASS_NAME);
                this.forceResumeMethod = transactionManagerClass.getMethod("forceResume", Transaction.class);
                this.setPropertyMethod = transactionClass.getMethod("setProperty", String.class, Serializable.class);
                this.logger.debug("Support for WebLogic forceResume available");
            } else {
                this.logger.warn("Support for WebLogic forceResume not available");
            }
        } catch (Exception ex) {
            throw new TransactionSystemException("Could not initialize WebLogicJtaTransactionManager because WebLogic API classes are not available", ex);
        }
    }

    /* JADX WARN: Type inference failed for: r0v10, types: [java.lang.Throwable, org.springframework.transaction.TransactionSystemException] */
    /* JADX WARN: Type inference failed for: r0v26, types: [java.lang.Throwable, org.springframework.transaction.TransactionSystemException] */
    /* JADX WARN: Type inference failed for: r0v27, types: [java.lang.Throwable, org.springframework.transaction.TransactionSystemException] */
    /* JADX WARN: Type inference failed for: r0v9, types: [java.lang.Throwable, org.springframework.transaction.TransactionSystemException] */
    @Override // org.springframework.transaction.jta.JtaTransactionManager
    protected void doJtaBegin(JtaTransactionObject txObject, TransactionDefinition definition) throws NotSupportedException, SystemException {
        int timeout = determineTimeout(definition);
        if (this.weblogicUserTransactionAvailable && definition.getName() != null) {
            try {
                if (timeout > -1) {
                    this.beginWithNameAndTimeoutMethod.invoke(txObject.getUserTransaction(), definition.getName(), Integer.valueOf(timeout));
                } else {
                    this.beginWithNameMethod.invoke(txObject.getUserTransaction(), definition.getName());
                }
            } catch (InvocationTargetException ex) {
                throw new TransactionSystemException("WebLogic's UserTransaction.begin() method failed", ex.getTargetException());
            } catch (Exception ex2) {
                throw new TransactionSystemException("Could not invoke WebLogic's UserTransaction.begin() method", ex2);
            }
        } else {
            applyTimeout(txObject, timeout);
            txObject.getUserTransaction().begin();
        }
        if (this.weblogicTransactionManagerAvailable) {
            if (definition.getIsolationLevel() != -1) {
                try {
                    Transaction tx = getTransactionManager().getTransaction();
                    Integer isolationLevel = Integer.valueOf(definition.getIsolationLevel());
                    this.setPropertyMethod.invoke(tx, ISOLATION_LEVEL_KEY, isolationLevel);
                    return;
                } catch (InvocationTargetException ex3) {
                    throw new TransactionSystemException("WebLogic's Transaction.setProperty(String, Serializable) method failed", ex3.getTargetException());
                } catch (Exception ex4) {
                    throw new TransactionSystemException("Could not invoke WebLogic's Transaction.setProperty(String, Serializable) method", ex4);
                }
            }
            return;
        }
        applyIsolationLevel(txObject, definition.getIsolationLevel());
    }

    /* JADX WARN: Type inference failed for: r0v5, types: [java.lang.Throwable, org.springframework.transaction.TransactionSystemException] */
    /* JADX WARN: Type inference failed for: r0v6, types: [java.lang.Throwable, org.springframework.transaction.TransactionSystemException] */
    @Override // org.springframework.transaction.jta.JtaTransactionManager
    protected void doJtaResume(JtaTransactionObject txObject, Object suspendedTransaction) throws InvalidTransactionException, SystemException {
        try {
            getTransactionManager().resume((Transaction) suspendedTransaction);
        } catch (InvalidTransactionException ex) {
            if (!this.weblogicTransactionManagerAvailable) {
                throw ex;
            }
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Standard JTA resume threw InvalidTransactionException: " + ex.getMessage() + " - trying WebLogic JTA forceResume");
            }
            try {
                this.forceResumeMethod.invoke(getTransactionManager(), suspendedTransaction);
            } catch (InvocationTargetException ex2) {
                throw new TransactionSystemException("WebLogic's TransactionManager.forceResume(Transaction) method failed", ex2.getTargetException());
            } catch (Exception ex22) {
                throw new TransactionSystemException("Could not access WebLogic's TransactionManager.forceResume(Transaction) method", ex22);
            }
        }
    }

    @Override // org.springframework.transaction.jta.JtaTransactionManager, org.springframework.transaction.jta.TransactionFactory
    public Transaction createTransaction(String name, int timeout) throws NotSupportedException, SystemException {
        if (this.weblogicUserTransactionAvailable && name != null) {
            try {
                if (timeout >= 0) {
                    this.beginWithNameAndTimeoutMethod.invoke(getUserTransaction(), name, Integer.valueOf(timeout));
                } else {
                    this.beginWithNameMethod.invoke(getUserTransaction(), name);
                }
                return new ManagedTransactionAdapter(getTransactionManager());
            } catch (InvocationTargetException ex) {
                if (ex.getTargetException() instanceof NotSupportedException) {
                    throw ex.getTargetException();
                }
                if (ex.getTargetException() instanceof SystemException) {
                    throw ex.getTargetException();
                }
                if (ex.getTargetException() instanceof RuntimeException) {
                    throw ((RuntimeException) ex.getTargetException());
                }
                throw new SystemException("WebLogic's begin() method failed with an unexpected error: " + ex.getTargetException());
            } catch (Exception ex2) {
                throw new SystemException("Could not invoke WebLogic's UserTransaction.begin() method: " + ex2);
            }
        }
        return super.createTransaction(name, timeout);
    }
}
