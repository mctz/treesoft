package org.springframework.transaction.support;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.scope.ScopedObject;
import org.springframework.core.InfrastructureProxy;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/support/TransactionSynchronizationUtils.class */
public abstract class TransactionSynchronizationUtils {
    private static final Log logger = LogFactory.getLog(TransactionSynchronizationUtils.class);
    private static final boolean aopAvailable = ClassUtils.isPresent("org.springframework.aop.scope.ScopedObject", TransactionSynchronizationUtils.class.getClassLoader());

    public static boolean sameResourceFactory(ResourceTransactionManager tm, Object resourceFactory) {
        return unwrapResourceIfNecessary(tm.getResourceFactory()).equals(unwrapResourceIfNecessary(resourceFactory));
    }

    public static Object unwrapResourceIfNecessary(Object resource) {
        Assert.notNull(resource, "Resource must not be null");
        Object resourceRef = resource;
        if (resourceRef instanceof InfrastructureProxy) {
            resourceRef = ((InfrastructureProxy) resourceRef).getWrappedObject();
        }
        if (aopAvailable) {
            resourceRef = ScopedProxyUnwrapper.unwrapIfNecessary(resourceRef);
        }
        return resourceRef;
    }

    public static void triggerFlush() {
        for (TransactionSynchronization synchronization : TransactionSynchronizationManager.getSynchronizations()) {
            synchronization.flush();
        }
    }

    public static void triggerBeforeCommit(boolean readOnly) {
        for (TransactionSynchronization synchronization : TransactionSynchronizationManager.getSynchronizations()) {
            synchronization.beforeCommit(readOnly);
        }
    }

    public static void triggerBeforeCompletion() {
        for (TransactionSynchronization synchronization : TransactionSynchronizationManager.getSynchronizations()) {
            try {
                synchronization.beforeCompletion();
            } catch (Throwable tsex) {
                logger.error("TransactionSynchronization.beforeCompletion threw exception", tsex);
            }
        }
    }

    public static void triggerAfterCommit() {
        invokeAfterCommit(TransactionSynchronizationManager.getSynchronizations());
    }

    public static void invokeAfterCommit(List<TransactionSynchronization> synchronizations) {
        if (synchronizations != null) {
            for (TransactionSynchronization synchronization : synchronizations) {
                synchronization.afterCommit();
            }
        }
    }

    public static void triggerAfterCompletion(int completionStatus) {
        List<TransactionSynchronization> synchronizations = TransactionSynchronizationManager.getSynchronizations();
        invokeAfterCompletion(synchronizations, completionStatus);
    }

    public static void invokeAfterCompletion(List<TransactionSynchronization> synchronizations, int completionStatus) {
        if (synchronizations != null) {
            for (TransactionSynchronization synchronization : synchronizations) {
                try {
                    synchronization.afterCompletion(completionStatus);
                } catch (Throwable tsex) {
                    logger.error("TransactionSynchronization.afterCompletion threw exception", tsex);
                }
            }
        }
    }

    /* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/support/TransactionSynchronizationUtils$ScopedProxyUnwrapper.class */
    public static class ScopedProxyUnwrapper {
        private ScopedProxyUnwrapper() {
        }

        public static Object unwrapIfNecessary(Object resource) {
            if (resource instanceof ScopedObject) {
                return ((ScopedObject) resource).getTargetObject();
            }
            return resource;
        }
    }
}
