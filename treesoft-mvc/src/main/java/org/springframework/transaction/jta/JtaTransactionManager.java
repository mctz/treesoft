package org.springframework.transaction.jta;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Properties;
import javax.naming.NamingException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.InvalidTransactionException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.UserTransaction;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jndi.JndiTemplate;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.HeuristicCompletionException;
import org.springframework.transaction.IllegalTransactionStateException;
import org.springframework.transaction.InvalidIsolationLevelException;
import org.springframework.transaction.NestedTransactionNotSupportedException;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionSuspensionNotSupportedException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/jta/JtaTransactionManager.class */
public class JtaTransactionManager extends AbstractPlatformTransactionManager implements TransactionFactory, InitializingBean, Serializable {
    public static final String DEFAULT_USER_TRANSACTION_NAME = "java:comp/UserTransaction";
    public static final String[] FALLBACK_TRANSACTION_MANAGER_NAMES = {"java:comp/TransactionManager", "java:appserver/TransactionManager", "java:pm/TransactionManager", "java:/TransactionManager"};
    public static final String DEFAULT_TRANSACTION_SYNCHRONIZATION_REGISTRY_NAME = "java:comp/TransactionSynchronizationRegistry";
    private transient JndiTemplate jndiTemplate;
    private transient UserTransaction userTransaction;
    private String userTransactionName;
    private boolean autodetectUserTransaction;
    private boolean cacheUserTransaction;
    private boolean userTransactionObtainedFromJndi;
    private transient TransactionManager transactionManager;
    private String transactionManagerName;
    private boolean autodetectTransactionManager;
    private transient TransactionSynchronizationRegistry transactionSynchronizationRegistry;
    private String transactionSynchronizationRegistryName;
    private boolean autodetectTransactionSynchronizationRegistry;
    private boolean allowCustomIsolationLevels;

    public JtaTransactionManager() {
        this.jndiTemplate = new JndiTemplate();
        this.autodetectUserTransaction = true;
        this.cacheUserTransaction = true;
        this.userTransactionObtainedFromJndi = false;
        this.autodetectTransactionManager = true;
        this.autodetectTransactionSynchronizationRegistry = true;
        this.allowCustomIsolationLevels = false;
        setNestedTransactionAllowed(true);
    }

    public JtaTransactionManager(UserTransaction userTransaction) {
        this();
        Assert.notNull(userTransaction, "UserTransaction must not be null");
        this.userTransaction = userTransaction;
    }

    public JtaTransactionManager(UserTransaction userTransaction, TransactionManager transactionManager) {
        this();
        Assert.notNull(userTransaction, "UserTransaction must not be null");
        Assert.notNull(transactionManager, "TransactionManager must not be null");
        this.userTransaction = userTransaction;
        this.transactionManager = transactionManager;
    }

    public JtaTransactionManager(TransactionManager transactionManager) {
        this();
        Assert.notNull(transactionManager, "TransactionManager must not be null");
        this.transactionManager = transactionManager;
        this.userTransaction = buildUserTransaction(transactionManager);
    }

    public void setJndiTemplate(JndiTemplate jndiTemplate) {
        Assert.notNull(jndiTemplate, "JndiTemplate must not be null");
        this.jndiTemplate = jndiTemplate;
    }

    public JndiTemplate getJndiTemplate() {
        return this.jndiTemplate;
    }

    public void setJndiEnvironment(Properties jndiEnvironment) {
        this.jndiTemplate = new JndiTemplate(jndiEnvironment);
    }

    public Properties getJndiEnvironment() {
        return this.jndiTemplate.getEnvironment();
    }

    public void setUserTransaction(UserTransaction userTransaction) {
        this.userTransaction = userTransaction;
    }

    public UserTransaction getUserTransaction() {
        return this.userTransaction;
    }

    public void setUserTransactionName(String userTransactionName) {
        this.userTransactionName = userTransactionName;
    }

    public void setAutodetectUserTransaction(boolean autodetectUserTransaction) {
        this.autodetectUserTransaction = autodetectUserTransaction;
    }

    public void setCacheUserTransaction(boolean cacheUserTransaction) {
        this.cacheUserTransaction = cacheUserTransaction;
    }

    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public TransactionManager getTransactionManager() {
        return this.transactionManager;
    }

    public void setTransactionManagerName(String transactionManagerName) {
        this.transactionManagerName = transactionManagerName;
    }

    public void setAutodetectTransactionManager(boolean autodetectTransactionManager) {
        this.autodetectTransactionManager = autodetectTransactionManager;
    }

    public void setTransactionSynchronizationRegistry(TransactionSynchronizationRegistry transactionSynchronizationRegistry) {
        this.transactionSynchronizationRegistry = transactionSynchronizationRegistry;
    }

    public TransactionSynchronizationRegistry getTransactionSynchronizationRegistry() {
        return this.transactionSynchronizationRegistry;
    }

    public void setTransactionSynchronizationRegistryName(String transactionSynchronizationRegistryName) {
        this.transactionSynchronizationRegistryName = transactionSynchronizationRegistryName;
    }

    public void setAutodetectTransactionSynchronizationRegistry(boolean autodetectTransactionSynchronizationRegistry) {
        this.autodetectTransactionSynchronizationRegistry = autodetectTransactionSynchronizationRegistry;
    }

    public void setAllowCustomIsolationLevels(boolean allowCustomIsolationLevels) {
        this.allowCustomIsolationLevels = allowCustomIsolationLevels;
    }

    public void afterPropertiesSet() throws TransactionSystemException {
        initUserTransactionAndTransactionManager();
        checkUserTransactionAndTransactionManager();
        initTransactionSynchronizationRegistry();
    }

    public void initUserTransactionAndTransactionManager() throws TransactionSystemException {
        if (this.userTransaction == null) {
            if (StringUtils.hasLength(this.userTransactionName)) {
                this.userTransaction = lookupUserTransaction(this.userTransactionName);
                this.userTransactionObtainedFromJndi = true;
            } else {
                this.userTransaction = retrieveUserTransaction();
                if (this.userTransaction == null && this.autodetectUserTransaction) {
                    this.userTransaction = findUserTransaction();
                }
            }
        }
        if (this.transactionManager == null) {
            if (StringUtils.hasLength(this.transactionManagerName)) {
                this.transactionManager = lookupTransactionManager(this.transactionManagerName);
            } else {
                this.transactionManager = retrieveTransactionManager();
                if (this.transactionManager == null && this.autodetectTransactionManager) {
                    this.transactionManager = findTransactionManager(this.userTransaction);
                }
            }
        }
        if (this.userTransaction == null && this.transactionManager != null) {
            this.userTransaction = buildUserTransaction(this.transactionManager);
        }
    }

    protected void checkUserTransactionAndTransactionManager() throws IllegalStateException {
        if (this.userTransaction != null) {
            if (this.logger.isInfoEnabled()) {
                this.logger.info("Using JTA UserTransaction: " + this.userTransaction);
            }
            if (this.transactionManager != null) {
                if (this.logger.isInfoEnabled()) {
                    this.logger.info("Using JTA TransactionManager: " + this.transactionManager);
                    return;
                }
                return;
            }
            this.logger.warn("No JTA TransactionManager found: transaction suspension not available");
            return;
        }
        throw new IllegalStateException("No JTA UserTransaction available - specify either 'userTransaction' or 'userTransactionName' or 'transactionManager' or 'transactionManagerName'");
    }

    protected void initTransactionSynchronizationRegistry() {
        if (this.transactionSynchronizationRegistry == null) {
            if (StringUtils.hasLength(this.transactionSynchronizationRegistryName)) {
                this.transactionSynchronizationRegistry = lookupTransactionSynchronizationRegistry(this.transactionSynchronizationRegistryName);
            } else {
                this.transactionSynchronizationRegistry = retrieveTransactionSynchronizationRegistry();
                if (this.transactionSynchronizationRegistry == null && this.autodetectTransactionSynchronizationRegistry) {
                    this.transactionSynchronizationRegistry = findTransactionSynchronizationRegistry(this.userTransaction, this.transactionManager);
                }
            }
        }
        if (this.transactionSynchronizationRegistry != null && this.logger.isInfoEnabled()) {
            this.logger.info("Using JTA TransactionSynchronizationRegistry: " + this.transactionSynchronizationRegistry);
        }
    }

    protected UserTransaction buildUserTransaction(TransactionManager transactionManager) {
        if (transactionManager instanceof UserTransaction) {
            return (UserTransaction) transactionManager;
        }
        return new UserTransactionAdapter(transactionManager);
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [java.lang.Throwable, org.springframework.transaction.TransactionSystemException] */
    protected UserTransaction lookupUserTransaction(String userTransactionName) throws TransactionSystemException {
        try {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Retrieving JTA UserTransaction from JNDI location [" + userTransactionName + "]");
            }
            return (UserTransaction) getJndiTemplate().lookup(userTransactionName, UserTransaction.class);
        } catch (NamingException ex) {
            throw new TransactionSystemException("JTA UserTransaction is not available at JNDI location [" + userTransactionName + "]", ex);
        }
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [java.lang.Throwable, org.springframework.transaction.TransactionSystemException] */
    protected TransactionManager lookupTransactionManager(String transactionManagerName) throws TransactionSystemException {
        try {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Retrieving JTA TransactionManager from JNDI location [" + transactionManagerName + "]");
            }
            return (TransactionManager) getJndiTemplate().lookup(transactionManagerName, TransactionManager.class);
        } catch (NamingException ex) {
            throw new TransactionSystemException("JTA TransactionManager is not available at JNDI location [" + transactionManagerName + "]", ex);
        }
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [java.lang.Throwable, org.springframework.transaction.TransactionSystemException] */
    protected TransactionSynchronizationRegistry lookupTransactionSynchronizationRegistry(String registryName) throws TransactionSystemException {
        try {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Retrieving JTA TransactionSynchronizationRegistry from JNDI location [" + registryName + "]");
            }
            return (TransactionSynchronizationRegistry) getJndiTemplate().lookup(registryName, TransactionSynchronizationRegistry.class);
        } catch (NamingException ex) {
            throw new TransactionSystemException("JTA TransactionSynchronizationRegistry is not available at JNDI location [" + registryName + "]", ex);
        }
    }

    protected UserTransaction retrieveUserTransaction() throws TransactionSystemException {
        return null;
    }

    protected TransactionManager retrieveTransactionManager() throws TransactionSystemException {
        return null;
    }

    protected TransactionSynchronizationRegistry retrieveTransactionSynchronizationRegistry() throws TransactionSystemException {
        return null;
    }

    protected UserTransaction findUserTransaction() {
        try {
            UserTransaction ut = (UserTransaction) getJndiTemplate().lookup(DEFAULT_USER_TRANSACTION_NAME, UserTransaction.class);
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("JTA UserTransaction found at default JNDI location [" + DEFAULT_USER_TRANSACTION_NAME + "]");
            }
            this.userTransactionObtainedFromJndi = true;
            return ut;
        } catch (NamingException ex) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("No JTA UserTransaction found at default JNDI location [" + DEFAULT_USER_TRANSACTION_NAME + "]", ex);
                return null;
            }
            return null;
        }
    }

    protected TransactionManager findTransactionManager(UserTransaction ut) {
        String[] strArr;
        if (ut instanceof TransactionManager) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("JTA UserTransaction object [" + ut + "] implements TransactionManager");
            }
            return (TransactionManager) ut;
        }
        for (String jndiName : FALLBACK_TRANSACTION_MANAGER_NAMES) {
            try {
                TransactionManager tm = (TransactionManager) getJndiTemplate().lookup(jndiName, TransactionManager.class);
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("JTA TransactionManager found at fallback JNDI location [" + jndiName + "]");
                }
                return tm;
            } catch (NamingException ex) {
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("No JTA TransactionManager found at fallback JNDI location [" + jndiName + "]", ex);
                }
            }
        }
        return null;
    }

    protected TransactionSynchronizationRegistry findTransactionSynchronizationRegistry(UserTransaction ut, TransactionManager tm) throws TransactionSystemException {
        if (this.userTransactionObtainedFromJndi) {
            try {
                TransactionSynchronizationRegistry tsr = (TransactionSynchronizationRegistry) getJndiTemplate().lookup(DEFAULT_TRANSACTION_SYNCHRONIZATION_REGISTRY_NAME, TransactionSynchronizationRegistry.class);
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("JTA TransactionSynchronizationRegistry found at default JNDI location [" + DEFAULT_TRANSACTION_SYNCHRONIZATION_REGISTRY_NAME + "]");
                }
                return tsr;
            } catch (NamingException ex) {
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("No JTA TransactionSynchronizationRegistry found at default JNDI location [" + DEFAULT_TRANSACTION_SYNCHRONIZATION_REGISTRY_NAME + "]", ex);
                }
            }
        }
        if (ut instanceof TransactionSynchronizationRegistry) {
            return (TransactionSynchronizationRegistry) ut;
        }
        if (tm instanceof TransactionSynchronizationRegistry) {
            return (TransactionSynchronizationRegistry) tm;
        }
        return null;
    }

    /* JADX WARN: Type inference failed for: r0v9, types: [java.lang.Throwable, org.springframework.transaction.CannotCreateTransactionException] */
    @Override // org.springframework.transaction.support.AbstractPlatformTransactionManager
    protected Object doGetTransaction() {
        UserTransaction ut = getUserTransaction();
        if (ut == null) {
            throw new CannotCreateTransactionException("No JTA UserTransaction available - programmatic PlatformTransactionManager.getTransaction usage not supported");
        }
        if (!this.cacheUserTransaction) {
            ut = lookupUserTransaction(this.userTransactionName != null ? this.userTransactionName : DEFAULT_USER_TRANSACTION_NAME);
        }
        return doGetJtaTransaction(ut);
    }

    protected JtaTransactionObject doGetJtaTransaction(UserTransaction ut) {
        return new JtaTransactionObject(ut);
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [java.lang.Throwable, org.springframework.transaction.TransactionSystemException] */
    @Override // org.springframework.transaction.support.AbstractPlatformTransactionManager
    protected boolean isExistingTransaction(Object transaction) {
        JtaTransactionObject txObject = (JtaTransactionObject) transaction;
        try {
            return txObject.getUserTransaction().getStatus() != 6;
        } catch (SystemException ex) {
            throw new TransactionSystemException("JTA failure on getStatus", ex);
        }
    }

    @Override // org.springframework.transaction.support.AbstractPlatformTransactionManager
    protected boolean useSavepointForNestedTransaction() {
        return false;
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [java.lang.Throwable, org.springframework.transaction.CannotCreateTransactionException] */
    /* JADX WARN: Type inference failed for: r0v3, types: [java.lang.Throwable, org.springframework.transaction.NestedTransactionNotSupportedException] */
    /* JADX WARN: Type inference failed for: r0v4, types: [java.lang.Throwable, org.springframework.transaction.NestedTransactionNotSupportedException] */
    @Override // org.springframework.transaction.support.AbstractPlatformTransactionManager
    protected void doBegin(Object transaction, TransactionDefinition definition) {
        JtaTransactionObject txObject = (JtaTransactionObject) transaction;
        try {
            doJtaBegin(txObject, definition);
        } catch (NotSupportedException ex) {
            throw new NestedTransactionNotSupportedException("JTA implementation does not support nested transactions", ex);
        } catch (UnsupportedOperationException ex2) {
            throw new NestedTransactionNotSupportedException("JTA implementation does not support nested transactions", ex2);
        } catch (SystemException ex3) {
            throw new CannotCreateTransactionException("JTA failure on begin", ex3);
        }
    }

    protected void doJtaBegin(JtaTransactionObject txObject, TransactionDefinition definition) throws NotSupportedException, SystemException {
        applyIsolationLevel(txObject, definition.getIsolationLevel());
        int timeout = determineTimeout(definition);
        applyTimeout(txObject, timeout);
        txObject.getUserTransaction().begin();
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [java.lang.Throwable, org.springframework.transaction.InvalidIsolationLevelException] */
    public void applyIsolationLevel(JtaTransactionObject txObject, int isolationLevel) throws InvalidIsolationLevelException, SystemException {
        if (!this.allowCustomIsolationLevels && isolationLevel != -1) {
            throw new InvalidIsolationLevelException("JtaTransactionManager does not support custom isolation levels by default - switch 'allowCustomIsolationLevels' to 'true'");
        }
    }

    public void applyTimeout(JtaTransactionObject txObject, int timeout) throws SystemException {
        if (timeout > -1) {
            txObject.getUserTransaction().setTransactionTimeout(timeout);
        }
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [java.lang.Throwable, org.springframework.transaction.TransactionSystemException] */
    @Override // org.springframework.transaction.support.AbstractPlatformTransactionManager
    protected Object doSuspend(Object transaction) {
        JtaTransactionObject txObject = (JtaTransactionObject) transaction;
        try {
            return doJtaSuspend(txObject);
        } catch (SystemException ex) {
            throw new TransactionSystemException("JTA failure on suspend", ex);
        }
    }

    /* JADX WARN: Type inference failed for: r0v5, types: [java.lang.Throwable, org.springframework.transaction.TransactionSuspensionNotSupportedException] */
    protected Object doJtaSuspend(JtaTransactionObject txObject) throws SystemException {
        if (getTransactionManager() == null) {
            throw new TransactionSuspensionNotSupportedException("JtaTransactionManager needs a JTA TransactionManager for suspending a transaction: specify the 'transactionManager' or 'transactionManagerName' property");
        }
        return getTransactionManager().suspend();
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [java.lang.Throwable, org.springframework.transaction.TransactionSystemException] */
    /* JADX WARN: Type inference failed for: r0v3, types: [java.lang.Throwable, org.springframework.transaction.TransactionSystemException] */
    /* JADX WARN: Type inference failed for: r0v4, types: [java.lang.Throwable, org.springframework.transaction.IllegalTransactionStateException] */
    @Override // org.springframework.transaction.support.AbstractPlatformTransactionManager
    protected void doResume(Object transaction, Object suspendedResources) {
        JtaTransactionObject txObject = (JtaTransactionObject) transaction;
        try {
            doJtaResume(txObject, suspendedResources);
        } catch (IllegalStateException ex) {
            throw new TransactionSystemException("Unexpected internal transaction state", ex);
        } catch (InvalidTransactionException ex2) {
            throw new IllegalTransactionStateException("Tried to resume invalid JTA transaction", ex2);
        } catch (SystemException ex3) {
            throw new TransactionSystemException("JTA failure on resume", ex3);
        }
    }

    /* JADX WARN: Type inference failed for: r0v4, types: [java.lang.Throwable, org.springframework.transaction.TransactionSuspensionNotSupportedException] */
    protected void doJtaResume(JtaTransactionObject txObject, Object suspendedTransaction) throws InvalidTransactionException, SystemException {
        if (getTransactionManager() == null) {
            throw new TransactionSuspensionNotSupportedException("JtaTransactionManager needs a JTA TransactionManager for suspending a transaction: specify the 'transactionManager' or 'transactionManagerName' property");
        }
        getTransactionManager().resume((Transaction) suspendedTransaction);
    }

    @Override // org.springframework.transaction.support.AbstractPlatformTransactionManager
    protected boolean shouldCommitOnGlobalRollbackOnly() {
        return true;
    }

    /* JADX WARN: Type inference failed for: r0v13, types: [java.lang.Throwable, org.springframework.transaction.UnexpectedRollbackException] */
    /* JADX WARN: Type inference failed for: r0v23, types: [java.lang.Throwable, org.springframework.transaction.UnexpectedRollbackException] */
    /* JADX WARN: Type inference failed for: r0v3, types: [java.lang.Throwable, org.springframework.transaction.TransactionSystemException] */
    /* JADX WARN: Type inference failed for: r0v4, types: [java.lang.Throwable, org.springframework.transaction.TransactionSystemException] */
    /* JADX WARN: Type inference failed for: r0v5, types: [java.lang.Throwable, org.springframework.transaction.HeuristicCompletionException] */
    /* JADX WARN: Type inference failed for: r0v6, types: [java.lang.Throwable, org.springframework.transaction.HeuristicCompletionException] */
    /* JADX WARN: Type inference failed for: r0v7, types: [java.lang.Throwable, org.springframework.transaction.UnexpectedRollbackException] */
    @Override // org.springframework.transaction.support.AbstractPlatformTransactionManager
    protected void doCommit(DefaultTransactionStatus status) {
        JtaTransactionObject txObject = (JtaTransactionObject) status.getTransaction();
        try {
            int jtaStatus = txObject.getUserTransaction().getStatus();
            if (jtaStatus == 6) {
                throw new UnexpectedRollbackException("JTA transaction already completed - probably rolled back");
            }
            if (jtaStatus == 4) {
                try {
                    txObject.getUserTransaction().rollback();
                } catch (IllegalStateException ex) {
                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug("Rollback failure with transaction already marked as rolled back: " + ex);
                    }
                }
                throw new UnexpectedRollbackException("JTA transaction already rolled back (probably due to a timeout)");
            }
            txObject.getUserTransaction().commit();
        } catch (HeuristicMixedException ex2) {
            throw new HeuristicCompletionException(3, ex2);
        } catch (SystemException ex3) {
            throw new TransactionSystemException("JTA failure on commit", ex3);
        } catch (IllegalStateException ex4) {
            throw new TransactionSystemException("Unexpected internal transaction state", ex4);
        } catch (RollbackException ex5) {
            throw new UnexpectedRollbackException("JTA transaction unexpectedly rolled back (maybe due to a timeout)", ex5);
        } catch (HeuristicRollbackException ex6) {
            throw new HeuristicCompletionException(2, ex6);
        }
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [java.lang.Throwable, org.springframework.transaction.TransactionSystemException] */
    /* JADX WARN: Type inference failed for: r0v9, types: [java.lang.Throwable, org.springframework.transaction.TransactionSystemException] */
    @Override // org.springframework.transaction.support.AbstractPlatformTransactionManager
    protected void doRollback(DefaultTransactionStatus status) {
        JtaTransactionObject txObject = (JtaTransactionObject) status.getTransaction();
        try {
            int jtaStatus = txObject.getUserTransaction().getStatus();
            if (jtaStatus != 6) {
                try {
                    txObject.getUserTransaction().rollback();
                } catch (IllegalStateException ex) {
                    if (jtaStatus == 4) {
                        if (this.logger.isDebugEnabled()) {
                            this.logger.debug("Rollback failure with transaction already marked as rolled back: " + ex);
                        }
                    } else {
                        throw new TransactionSystemException("Unexpected internal transaction state", ex);
                    }
                }
            }
        } catch (SystemException ex2) {
            throw new TransactionSystemException("JTA failure on rollback", ex2);
        }
    }

    /* JADX WARN: Type inference failed for: r0v5, types: [java.lang.Throwable, org.springframework.transaction.TransactionSystemException] */
    /* JADX WARN: Type inference failed for: r0v6, types: [java.lang.Throwable, org.springframework.transaction.TransactionSystemException] */
    @Override // org.springframework.transaction.support.AbstractPlatformTransactionManager
    protected void doSetRollbackOnly(DefaultTransactionStatus status) {
        JtaTransactionObject txObject = (JtaTransactionObject) status.getTransaction();
        if (status.isDebug()) {
            this.logger.debug("Setting JTA transaction rollback-only");
        }
        try {
            int jtaStatus = txObject.getUserTransaction().getStatus();
            if (jtaStatus != 6 && jtaStatus != 4) {
                txObject.getUserTransaction().setRollbackOnly();
            }
        } catch (SystemException ex) {
            throw new TransactionSystemException("JTA failure on setRollbackOnly", ex);
        } catch (IllegalStateException ex2) {
            throw new TransactionSystemException("Unexpected internal transaction state", ex2);
        }
    }

    /* JADX WARN: Type inference failed for: r0v15, types: [java.lang.Throwable, org.springframework.transaction.TransactionSystemException] */
    @Override // org.springframework.transaction.support.AbstractPlatformTransactionManager
    protected void registerAfterCompletionWithExistingTransaction(Object transaction, List<TransactionSynchronization> synchronizations) {
        JtaTransactionObject txObject = (JtaTransactionObject) transaction;
        this.logger.debug("Registering after-completion synchronization with existing JTA transaction");
        try {
            doRegisterAfterCompletionWithJtaTransaction(txObject, synchronizations);
        } catch (Exception ex) {
            if ((ex instanceof RollbackException) || (ex.getCause() instanceof RollbackException)) {
                this.logger.debug("Participating in existing JTA transaction that has been marked for rollback: cannot register Spring after-completion callbacks with outer JTA transaction - immediately performing Spring after-completion callbacks with outcome status 'rollback'. Original exception: " + ex);
                invokeAfterCompletion(synchronizations, 1);
                return;
            }
            this.logger.debug("Participating in existing JTA transaction, but unexpected internal transaction state encountered: cannot register Spring after-completion callbacks with outer JTA transaction - processing Spring after-completion callbacks with outcome status 'unknown'Original exception: " + ex);
            invokeAfterCompletion(synchronizations, 2);
        } catch (SystemException ex2) {
            throw new TransactionSystemException("JTA failure on registerSynchronization", ex2);
        }
    }

    protected void doRegisterAfterCompletionWithJtaTransaction(JtaTransactionObject txObject, List<TransactionSynchronization> synchronizations) throws RollbackException, SystemException {
        int jtaStatus = txObject.getUserTransaction().getStatus();
        if (jtaStatus == 6) {
            throw new RollbackException("JTA transaction already completed - probably rolled back");
        }
        if (jtaStatus == 4) {
            throw new RollbackException("JTA transaction already rolled back (probably due to a timeout)");
        }
        if (this.transactionSynchronizationRegistry != null) {
            this.transactionSynchronizationRegistry.registerInterposedSynchronization(new JtaAfterCompletionSynchronization(synchronizations));
        } else if (getTransactionManager() != null) {
            Transaction transaction = getTransactionManager().getTransaction();
            if (transaction == null) {
                throw new IllegalStateException("No JTA Transaction available");
            }
            transaction.registerSynchronization(new JtaAfterCompletionSynchronization(synchronizations));
        } else {
            this.logger.warn("Participating in existing JTA transaction, but no JTA TransactionManager available: cannot register Spring after-completion callbacks with outer JTA transaction - processing Spring after-completion callbacks with outcome status 'unknown'");
            invokeAfterCompletion(synchronizations, 2);
        }
    }

    @Override // org.springframework.transaction.jta.TransactionFactory
    public Transaction createTransaction(String name, int timeout) throws NotSupportedException, SystemException {
        TransactionManager tm = getTransactionManager();
        Assert.state(tm != null, "No JTA TransactionManager available");
        if (timeout >= 0) {
            tm.setTransactionTimeout(timeout);
        }
        tm.begin();
        return new ManagedTransactionAdapter(tm);
    }

    @Override // org.springframework.transaction.jta.TransactionFactory
    public boolean supportsResourceAdapterManagedTransactions() {
        return false;
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        this.jndiTemplate = new JndiTemplate();
        initUserTransactionAndTransactionManager();
        initTransactionSynchronizationRegistry();
    }
}
