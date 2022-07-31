package org.springframework.transaction.support;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.Constants;
import org.springframework.transaction.IllegalTransactionStateException;
import org.springframework.transaction.InvalidTimeoutException;
import org.springframework.transaction.NestedTransactionNotSupportedException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.TransactionSuspensionNotSupportedException;
import org.springframework.transaction.UnexpectedRollbackException;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/support/AbstractPlatformTransactionManager.class */
public abstract class AbstractPlatformTransactionManager implements PlatformTransactionManager, Serializable {
    public static final int SYNCHRONIZATION_ALWAYS = 0;
    public static final int SYNCHRONIZATION_ON_ACTUAL_TRANSACTION = 1;
    public static final int SYNCHRONIZATION_NEVER = 2;
    private static final Constants constants = new Constants(AbstractPlatformTransactionManager.class);
    protected transient Log logger = LogFactory.getLog(getClass());
    private int transactionSynchronization = 0;
    private int defaultTimeout = -1;
    private boolean nestedTransactionAllowed = false;
    private boolean validateExistingTransaction = false;
    private boolean globalRollbackOnParticipationFailure = true;
    private boolean failEarlyOnGlobalRollbackOnly = false;
    private boolean rollbackOnCommitFailure = false;

    protected abstract Object doGetTransaction() throws TransactionException;

    protected abstract void doBegin(Object obj, TransactionDefinition transactionDefinition) throws TransactionException;

    protected abstract void doCommit(DefaultTransactionStatus defaultTransactionStatus) throws TransactionException;

    protected abstract void doRollback(DefaultTransactionStatus defaultTransactionStatus) throws TransactionException;

    public final void setTransactionSynchronizationName(String constantName) {
        setTransactionSynchronization(constants.asNumber(constantName).intValue());
    }

    public final void setTransactionSynchronization(int transactionSynchronization) {
        this.transactionSynchronization = transactionSynchronization;
    }

    public final int getTransactionSynchronization() {
        return this.transactionSynchronization;
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [java.lang.Throwable, org.springframework.transaction.InvalidTimeoutException] */
    public final void setDefaultTimeout(int defaultTimeout) {
        if (defaultTimeout < -1) {
            throw new InvalidTimeoutException("Invalid default timeout", defaultTimeout);
        }
        this.defaultTimeout = defaultTimeout;
    }

    public final int getDefaultTimeout() {
        return this.defaultTimeout;
    }

    public final void setNestedTransactionAllowed(boolean nestedTransactionAllowed) {
        this.nestedTransactionAllowed = nestedTransactionAllowed;
    }

    public final boolean isNestedTransactionAllowed() {
        return this.nestedTransactionAllowed;
    }

    public final void setValidateExistingTransaction(boolean validateExistingTransaction) {
        this.validateExistingTransaction = validateExistingTransaction;
    }

    public final boolean isValidateExistingTransaction() {
        return this.validateExistingTransaction;
    }

    public final void setGlobalRollbackOnParticipationFailure(boolean globalRollbackOnParticipationFailure) {
        this.globalRollbackOnParticipationFailure = globalRollbackOnParticipationFailure;
    }

    public final boolean isGlobalRollbackOnParticipationFailure() {
        return this.globalRollbackOnParticipationFailure;
    }

    public final void setFailEarlyOnGlobalRollbackOnly(boolean failEarlyOnGlobalRollbackOnly) {
        this.failEarlyOnGlobalRollbackOnly = failEarlyOnGlobalRollbackOnly;
    }

    public final boolean isFailEarlyOnGlobalRollbackOnly() {
        return this.failEarlyOnGlobalRollbackOnly;
    }

    public final void setRollbackOnCommitFailure(boolean rollbackOnCommitFailure) {
        this.rollbackOnCommitFailure = rollbackOnCommitFailure;
    }

    public final boolean isRollbackOnCommitFailure() {
        return this.rollbackOnCommitFailure;
    }

    /* JADX WARN: Type inference failed for: r0v44, types: [java.lang.Throwable, org.springframework.transaction.IllegalTransactionStateException] */
    /* JADX WARN: Type inference failed for: r0v45, types: [java.lang.Throwable, org.springframework.transaction.InvalidTimeoutException] */
    @Override // org.springframework.transaction.PlatformTransactionManager
    public final TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException {
        Object transaction = doGetTransaction();
        boolean debugEnabled = this.logger.isDebugEnabled();
        if (definition == null) {
            definition = new DefaultTransactionDefinition();
        }
        if (isExistingTransaction(transaction)) {
            return handleExistingTransaction(definition, transaction, debugEnabled);
        }
        if (definition.getTimeout() < -1) {
            throw new InvalidTimeoutException("Invalid transaction timeout", definition.getTimeout());
        }
        if (definition.getPropagationBehavior() == 2) {
            throw new IllegalTransactionStateException("No existing transaction found for transaction marked with propagation 'mandatory'");
        }
        if (definition.getPropagationBehavior() == 0 || definition.getPropagationBehavior() == 3 || definition.getPropagationBehavior() == 6) {
            SuspendedResourcesHolder suspendedResources = suspend(null);
            if (debugEnabled) {
                this.logger.debug("Creating new transaction with name [" + definition.getName() + "]: " + definition);
            }
            try {
                boolean newSynchronization = getTransactionSynchronization() != 2;
                DefaultTransactionStatus status = newTransactionStatus(definition, transaction, true, newSynchronization, debugEnabled, suspendedResources);
                doBegin(transaction, definition);
                prepareSynchronization(status, definition);
                return status;
            } catch (Error err) {
                resume(null, suspendedResources);
                throw err;
            } catch (RuntimeException ex) {
                resume(null, suspendedResources);
                throw ex;
            }
        }
        boolean newSynchronization2 = getTransactionSynchronization() == 0;
        return prepareTransactionStatus(definition, null, true, newSynchronization2, debugEnabled, null);
    }

    /* JADX WARN: Type inference failed for: r0v23, types: [java.lang.Throwable, org.springframework.transaction.IllegalTransactionStateException] */
    /* JADX WARN: Type inference failed for: r0v27, types: [java.lang.Throwable, org.springframework.transaction.IllegalTransactionStateException] */
    /* JADX WARN: Type inference failed for: r0v53, types: [java.lang.Throwable, org.springframework.transaction.NestedTransactionNotSupportedException] */
    /* JADX WARN: Type inference failed for: r0v85, types: [java.lang.Throwable, org.springframework.transaction.IllegalTransactionStateException] */
    private TransactionStatus handleExistingTransaction(TransactionDefinition definition, Object transaction, boolean debugEnabled) throws TransactionException {
        Integer currentIsolationLevel;
        if (definition.getPropagationBehavior() == 5) {
            throw new IllegalTransactionStateException("Existing transaction found for transaction marked with propagation 'never'");
        }
        if (definition.getPropagationBehavior() == 4) {
            if (debugEnabled) {
                this.logger.debug("Suspending current transaction");
            }
            Object suspendedResources = suspend(transaction);
            boolean newSynchronization = getTransactionSynchronization() == 0;
            return prepareTransactionStatus(definition, null, false, newSynchronization, debugEnabled, suspendedResources);
        } else if (definition.getPropagationBehavior() == 3) {
            if (debugEnabled) {
                this.logger.debug("Suspending current transaction, creating new transaction with name [" + definition.getName() + "]");
            }
            SuspendedResourcesHolder suspendedResources2 = suspend(transaction);
            try {
                boolean newSynchronization2 = getTransactionSynchronization() != 2;
                DefaultTransactionStatus status = newTransactionStatus(definition, transaction, true, newSynchronization2, debugEnabled, suspendedResources2);
                doBegin(transaction, definition);
                prepareSynchronization(status, definition);
                return status;
            } catch (Error beginErr) {
                resumeAfterBeginException(transaction, suspendedResources2, beginErr);
                throw beginErr;
            } catch (RuntimeException beginEx) {
                resumeAfterBeginException(transaction, suspendedResources2, beginEx);
                throw beginEx;
            }
        } else if (definition.getPropagationBehavior() == 6) {
            if (!isNestedTransactionAllowed()) {
                throw new NestedTransactionNotSupportedException("Transaction manager does not allow nested transactions by default - specify 'nestedTransactionAllowed' property with value 'true'");
            }
            if (debugEnabled) {
                this.logger.debug("Creating nested transaction with name [" + definition.getName() + "]");
            }
            if (useSavepointForNestedTransaction()) {
                DefaultTransactionStatus status2 = prepareTransactionStatus(definition, transaction, false, false, debugEnabled, null);
                status2.createAndHoldSavepoint();
                return status2;
            }
            boolean newSynchronization3 = getTransactionSynchronization() != 2;
            DefaultTransactionStatus status3 = newTransactionStatus(definition, transaction, true, newSynchronization3, debugEnabled, null);
            doBegin(transaction, definition);
            prepareSynchronization(status3, definition);
            return status3;
        } else {
            if (debugEnabled) {
                this.logger.debug("Participating in existing transaction");
            }
            if (isValidateExistingTransaction()) {
                if (definition.getIsolationLevel() != -1 && ((currentIsolationLevel = TransactionSynchronizationManager.getCurrentTransactionIsolationLevel()) == null || currentIsolationLevel.intValue() != definition.getIsolationLevel())) {
                    Constants isoConstants = DefaultTransactionDefinition.constants;
                    throw new IllegalTransactionStateException("Participating transaction with definition [" + definition + "] specifies isolation level which is incompatible with existing transaction: " + (currentIsolationLevel != null ? isoConstants.toCode(currentIsolationLevel, DefaultTransactionDefinition.PREFIX_ISOLATION) : "(unknown)"));
                } else if (!definition.isReadOnly() && TransactionSynchronizationManager.isCurrentTransactionReadOnly()) {
                    throw new IllegalTransactionStateException("Participating transaction with definition [" + definition + "] is not marked as read-only but existing transaction is");
                }
            }
            boolean newSynchronization4 = getTransactionSynchronization() != 2;
            return prepareTransactionStatus(definition, transaction, false, newSynchronization4, debugEnabled, null);
        }
    }

    public final DefaultTransactionStatus prepareTransactionStatus(TransactionDefinition definition, Object transaction, boolean newTransaction, boolean newSynchronization, boolean debug, Object suspendedResources) {
        DefaultTransactionStatus status = newTransactionStatus(definition, transaction, newTransaction, newSynchronization, debug, suspendedResources);
        prepareSynchronization(status, definition);
        return status;
    }

    protected DefaultTransactionStatus newTransactionStatus(TransactionDefinition definition, Object transaction, boolean newTransaction, boolean newSynchronization, boolean debug, Object suspendedResources) {
        boolean actualNewSynchronization = newSynchronization && !TransactionSynchronizationManager.isSynchronizationActive();
        return new DefaultTransactionStatus(transaction, newTransaction, actualNewSynchronization, definition.isReadOnly(), debug, suspendedResources);
    }

    protected void prepareSynchronization(DefaultTransactionStatus status, TransactionDefinition definition) {
        if (status.isNewSynchronization()) {
            TransactionSynchronizationManager.setActualTransactionActive(status.hasTransaction());
            TransactionSynchronizationManager.setCurrentTransactionIsolationLevel(definition.getIsolationLevel() != -1 ? Integer.valueOf(definition.getIsolationLevel()) : null);
            TransactionSynchronizationManager.setCurrentTransactionReadOnly(definition.isReadOnly());
            TransactionSynchronizationManager.setCurrentTransactionName(definition.getName());
            TransactionSynchronizationManager.initSynchronization();
        }
    }

    public int determineTimeout(TransactionDefinition definition) {
        if (definition.getTimeout() != -1) {
            return definition.getTimeout();
        }
        return this.defaultTimeout;
    }

    public final SuspendedResourcesHolder suspend(Object transaction) throws TransactionException {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            List<TransactionSynchronization> suspendedSynchronizations = doSuspendSynchronization();
            Object suspendedResources = null;
            if (transaction != null) {
                try {
                    suspendedResources = doSuspend(transaction);
                } catch (Error err) {
                    doResumeSynchronization(suspendedSynchronizations);
                    throw err;
                } catch (RuntimeException ex) {
                    doResumeSynchronization(suspendedSynchronizations);
                    throw ex;
                }
            }
            String name = TransactionSynchronizationManager.getCurrentTransactionName();
            TransactionSynchronizationManager.setCurrentTransactionName(null);
            boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
            TransactionSynchronizationManager.setCurrentTransactionReadOnly(false);
            Integer isolationLevel = TransactionSynchronizationManager.getCurrentTransactionIsolationLevel();
            TransactionSynchronizationManager.setCurrentTransactionIsolationLevel(null);
            boolean wasActive = TransactionSynchronizationManager.isActualTransactionActive();
            TransactionSynchronizationManager.setActualTransactionActive(false);
            return new SuspendedResourcesHolder(suspendedResources, suspendedSynchronizations, name, readOnly, isolationLevel, wasActive);
        } else if (transaction != null) {
            Object suspendedResources2 = doSuspend(transaction);
            return new SuspendedResourcesHolder(suspendedResources2);
        } else {
            return null;
        }
    }

    public final void resume(Object transaction, SuspendedResourcesHolder resourcesHolder) throws TransactionException {
        if (resourcesHolder == null) {
            return;
        }
        Object suspendedResources = resourcesHolder.suspendedResources;
        if (suspendedResources != null) {
            doResume(transaction, suspendedResources);
        }
        List<TransactionSynchronization> suspendedSynchronizations = resourcesHolder.suspendedSynchronizations;
        if (suspendedSynchronizations == null) {
            return;
        }
        TransactionSynchronizationManager.setActualTransactionActive(resourcesHolder.wasActive);
        TransactionSynchronizationManager.setCurrentTransactionIsolationLevel(resourcesHolder.isolationLevel);
        TransactionSynchronizationManager.setCurrentTransactionReadOnly(resourcesHolder.readOnly);
        TransactionSynchronizationManager.setCurrentTransactionName(resourcesHolder.name);
        doResumeSynchronization(suspendedSynchronizations);
    }

    private void resumeAfterBeginException(Object transaction, SuspendedResourcesHolder suspendedResources, Throwable beginEx) {
        try {
            resume(transaction, suspendedResources);
        } catch (Error resumeErr) {
            this.logger.error("Inner transaction begin exception overridden by outer transaction resume exception", beginEx);
            throw resumeErr;
        } catch (RuntimeException resumeEx) {
            this.logger.error("Inner transaction begin exception overridden by outer transaction resume exception", beginEx);
            throw resumeEx;
        }
    }

    private List<TransactionSynchronization> doSuspendSynchronization() {
        List<TransactionSynchronization> suspendedSynchronizations = TransactionSynchronizationManager.getSynchronizations();
        for (TransactionSynchronization synchronization : suspendedSynchronizations) {
            synchronization.suspend();
        }
        TransactionSynchronizationManager.clearSynchronization();
        return suspendedSynchronizations;
    }

    private void doResumeSynchronization(List<TransactionSynchronization> suspendedSynchronizations) {
        TransactionSynchronizationManager.initSynchronization();
        for (TransactionSynchronization synchronization : suspendedSynchronizations) {
            synchronization.resume();
            TransactionSynchronizationManager.registerSynchronization(synchronization);
        }
    }

    /* JADX WARN: Type inference failed for: r0v16, types: [java.lang.Throwable, org.springframework.transaction.UnexpectedRollbackException] */
    /* JADX WARN: Type inference failed for: r0v26, types: [java.lang.Throwable, org.springframework.transaction.IllegalTransactionStateException] */
    @Override // org.springframework.transaction.PlatformTransactionManager
    public final void commit(TransactionStatus status) throws TransactionException {
        if (status.isCompleted()) {
            throw new IllegalTransactionStateException("Transaction is already completed - do not call commit or rollback more than once per transaction");
        }
        DefaultTransactionStatus defStatus = (DefaultTransactionStatus) status;
        if (defStatus.isLocalRollbackOnly()) {
            if (defStatus.isDebug()) {
                this.logger.debug("Transactional code has requested rollback");
            }
            processRollback(defStatus);
        } else if (!shouldCommitOnGlobalRollbackOnly() && defStatus.isGlobalRollbackOnly()) {
            if (defStatus.isDebug()) {
                this.logger.debug("Global transaction is marked as rollback-only but transactional code requested commit");
            }
            processRollback(defStatus);
            if (status.isNewTransaction() || isFailEarlyOnGlobalRollbackOnly()) {
                throw new UnexpectedRollbackException("Transaction rolled back because it has been marked as rollback-only");
            }
        } else {
            processCommit(defStatus);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v31, types: [java.lang.Throwable, org.springframework.transaction.UnexpectedRollbackException] */
    /* JADX WARN: Type inference failed for: r7v2, types: [java.lang.Throwable, org.springframework.transaction.TransactionException] */
    /* JADX WARN: Type inference failed for: r7v3, types: [java.lang.Throwable, org.springframework.transaction.UnexpectedRollbackException] */
    private void processCommit(DefaultTransactionStatus status) throws TransactionException {
        boolean beforeCompletionInvoked = false;
        try {
            try {
                try {
                    prepareForCommit(status);
                    triggerBeforeCommit(status);
                    triggerBeforeCompletion(status);
                    beforeCompletionInvoked = true;
                    boolean globalRollbackOnly = false;
                    if (status.isNewTransaction() || isFailEarlyOnGlobalRollbackOnly()) {
                        globalRollbackOnly = status.isGlobalRollbackOnly();
                    }
                    if (status.hasSavepoint()) {
                        if (status.isDebug()) {
                            this.logger.debug("Releasing transaction savepoint");
                        }
                        status.releaseHeldSavepoint();
                    } else if (status.isNewTransaction()) {
                        if (status.isDebug()) {
                            this.logger.debug("Initiating transaction commit");
                        }
                        doCommit(status);
                    }
                    if (globalRollbackOnly) {
                        throw new UnexpectedRollbackException("Transaction silently rolled back because it has been marked as rollback-only");
                    }
                    triggerAfterCommit(status);
                    triggerAfterCompletion(status, 0);
                } catch (Error err) {
                    if (!beforeCompletionInvoked) {
                        triggerBeforeCompletion(status);
                    }
                    doRollbackOnCommitException(status, err);
                    throw err;
                } catch (UnexpectedRollbackException e) {
                    triggerAfterCompletion(status, 1);
                    throw e;
                }
            } catch (RuntimeException ex) {
                if (!beforeCompletionInvoked) {
                    triggerBeforeCompletion(status);
                }
                doRollbackOnCommitException(status, ex);
                throw ex;
            } catch (TransactionException e2) {
                if (isRollbackOnCommitFailure()) {
                    doRollbackOnCommitException(status, e2);
                } else {
                    triggerAfterCompletion(status, 2);
                }
                throw e2;
            }
        } finally {
            cleanupAfterCompletion(status);
        }
    }

    /* JADX WARN: Type inference failed for: r0v5, types: [java.lang.Throwable, org.springframework.transaction.IllegalTransactionStateException] */
    @Override // org.springframework.transaction.PlatformTransactionManager
    public final void rollback(TransactionStatus status) throws TransactionException {
        if (status.isCompleted()) {
            throw new IllegalTransactionStateException("Transaction is already completed - do not call commit or rollback more than once per transaction");
        }
        DefaultTransactionStatus defStatus = (DefaultTransactionStatus) status;
        processRollback(defStatus);
    }

    private void processRollback(DefaultTransactionStatus status) {
        try {
            try {
                triggerBeforeCompletion(status);
                if (status.hasSavepoint()) {
                    if (status.isDebug()) {
                        this.logger.debug("Rolling back transaction to savepoint");
                    }
                    status.rollbackToHeldSavepoint();
                } else if (status.isNewTransaction()) {
                    if (status.isDebug()) {
                        this.logger.debug("Initiating transaction rollback");
                    }
                    doRollback(status);
                } else if (status.hasTransaction()) {
                    if (status.isLocalRollbackOnly() || isGlobalRollbackOnParticipationFailure()) {
                        if (status.isDebug()) {
                            this.logger.debug("Participating transaction failed - marking existing transaction as rollback-only");
                        }
                        doSetRollbackOnly(status);
                    } else if (status.isDebug()) {
                        this.logger.debug("Participating transaction failed - letting transaction originator decide on rollback");
                    }
                } else {
                    this.logger.debug("Should roll back transaction but cannot - no transaction available");
                }
                triggerAfterCompletion(status, 1);
            } catch (Error err) {
                triggerAfterCompletion(status, 2);
                throw err;
            } catch (RuntimeException ex) {
                triggerAfterCompletion(status, 2);
                throw ex;
            }
        } finally {
            cleanupAfterCompletion(status);
        }
    }

    private void doRollbackOnCommitException(DefaultTransactionStatus status, Throwable ex) throws TransactionException {
        try {
            if (status.isNewTransaction()) {
                if (status.isDebug()) {
                    this.logger.debug("Initiating transaction rollback after commit exception", ex);
                }
                doRollback(status);
            } else if (status.hasTransaction() && isGlobalRollbackOnParticipationFailure()) {
                if (status.isDebug()) {
                    this.logger.debug("Marking existing transaction as rollback-only after commit exception", ex);
                }
                doSetRollbackOnly(status);
            }
            triggerAfterCompletion(status, 1);
        } catch (Error rberr) {
            this.logger.error("Commit exception overridden by rollback exception", ex);
            triggerAfterCompletion(status, 2);
            throw rberr;
        } catch (RuntimeException rbex) {
            this.logger.error("Commit exception overridden by rollback exception", ex);
            triggerAfterCompletion(status, 2);
            throw rbex;
        }
    }

    public final void triggerBeforeCommit(DefaultTransactionStatus status) {
        if (status.isNewSynchronization()) {
            if (status.isDebug()) {
                this.logger.trace("Triggering beforeCommit synchronization");
            }
            TransactionSynchronizationUtils.triggerBeforeCommit(status.isReadOnly());
        }
    }

    public final void triggerBeforeCompletion(DefaultTransactionStatus status) {
        if (status.isNewSynchronization()) {
            if (status.isDebug()) {
                this.logger.trace("Triggering beforeCompletion synchronization");
            }
            TransactionSynchronizationUtils.triggerBeforeCompletion();
        }
    }

    private void triggerAfterCommit(DefaultTransactionStatus status) {
        if (status.isNewSynchronization()) {
            if (status.isDebug()) {
                this.logger.trace("Triggering afterCommit synchronization");
            }
            TransactionSynchronizationUtils.triggerAfterCommit();
        }
    }

    private void triggerAfterCompletion(DefaultTransactionStatus status, int completionStatus) {
        if (status.isNewSynchronization()) {
            List<TransactionSynchronization> synchronizations = TransactionSynchronizationManager.getSynchronizations();
            if (!status.hasTransaction() || status.isNewTransaction()) {
                if (status.isDebug()) {
                    this.logger.trace("Triggering afterCompletion synchronization");
                }
                invokeAfterCompletion(synchronizations, completionStatus);
            } else if (!synchronizations.isEmpty()) {
                registerAfterCompletionWithExistingTransaction(status.getTransaction(), synchronizations);
            }
        }
    }

    public final void invokeAfterCompletion(List<TransactionSynchronization> synchronizations, int completionStatus) {
        TransactionSynchronizationUtils.invokeAfterCompletion(synchronizations, completionStatus);
    }

    private void cleanupAfterCompletion(DefaultTransactionStatus status) {
        status.setCompleted();
        if (status.isNewSynchronization()) {
            TransactionSynchronizationManager.clear();
        }
        if (status.isNewTransaction()) {
            doCleanupAfterCompletion(status.getTransaction());
        }
        if (status.getSuspendedResources() != null) {
            if (status.isDebug()) {
                this.logger.debug("Resuming suspended transaction after completion of inner transaction");
            }
            resume(status.getTransaction(), (SuspendedResourcesHolder) status.getSuspendedResources());
        }
    }

    protected boolean isExistingTransaction(Object transaction) throws TransactionException {
        return false;
    }

    protected boolean useSavepointForNestedTransaction() {
        return true;
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [java.lang.Throwable, org.springframework.transaction.TransactionSuspensionNotSupportedException] */
    protected Object doSuspend(Object transaction) throws TransactionException {
        throw new TransactionSuspensionNotSupportedException("Transaction manager [" + getClass().getName() + "] does not support transaction suspension");
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [java.lang.Throwable, org.springframework.transaction.TransactionSuspensionNotSupportedException] */
    protected void doResume(Object transaction, Object suspendedResources) throws TransactionException {
        throw new TransactionSuspensionNotSupportedException("Transaction manager [" + getClass().getName() + "] does not support transaction suspension");
    }

    protected boolean shouldCommitOnGlobalRollbackOnly() {
        return false;
    }

    protected void prepareForCommit(DefaultTransactionStatus status) {
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [java.lang.Throwable, org.springframework.transaction.IllegalTransactionStateException] */
    protected void doSetRollbackOnly(DefaultTransactionStatus status) throws TransactionException {
        throw new IllegalTransactionStateException("Participating in existing transactions is not supported - when 'isExistingTransaction' returns true, appropriate 'doSetRollbackOnly' behavior must be provided");
    }

    protected void registerAfterCompletionWithExistingTransaction(Object transaction, List<TransactionSynchronization> synchronizations) throws TransactionException {
        this.logger.debug("Cannot register Spring after-completion synchronization with existing transaction - processing Spring after-completion callbacks immediately, with outcome status 'unknown'");
        invokeAfterCompletion(synchronizations, 2);
    }

    protected void doCleanupAfterCompletion(Object transaction) {
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        this.logger = LogFactory.getLog(getClass());
    }

    /* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/support/AbstractPlatformTransactionManager$SuspendedResourcesHolder.class */
    public static class SuspendedResourcesHolder {
        private final Object suspendedResources;
        private List<TransactionSynchronization> suspendedSynchronizations;
        private String name;
        private boolean readOnly;
        private Integer isolationLevel;
        private boolean wasActive;

        private SuspendedResourcesHolder(Object suspendedResources) {
            this.suspendedResources = suspendedResources;
        }

        private SuspendedResourcesHolder(Object suspendedResources, List<TransactionSynchronization> suspendedSynchronizations, String name, boolean readOnly, Integer isolationLevel, boolean wasActive) {
            this.suspendedResources = suspendedResources;
            this.suspendedSynchronizations = suspendedSynchronizations;
            this.name = name;
            this.readOnly = readOnly;
            this.isolationLevel = isolationLevel;
            this.wasActive = wasActive;
        }
    }
}
