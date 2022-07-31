package org.springframework.transaction.interceptor;

import java.lang.reflect.Method;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.core.NamedThreadLocal;
import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.support.CallbackPreferringPlatformTransactionManager;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.util.StringUtils;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/interceptor/TransactionAspectSupport.class */
public abstract class TransactionAspectSupport implements BeanFactoryAware, InitializingBean {
    private static final ThreadLocal<TransactionInfo> transactionInfoHolder = new NamedThreadLocal("Current aspect-driven transaction");
    private final ConcurrentHashMap<String, PlatformTransactionManager> transactionManagerCache = new ConcurrentHashMap<>();
    protected final Log logger = LogFactory.getLog(getClass());
    private String transactionManagerBeanName;
    private PlatformTransactionManager transactionManager;
    private TransactionAttributeSource transactionAttributeSource;
    private BeanFactory beanFactory;

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/interceptor/TransactionAspectSupport$InvocationCallback.class */
    public interface InvocationCallback {
        Object proceedWithInvocation() throws Throwable;
    }

    protected static TransactionInfo currentTransactionInfo() throws NoTransactionException {
        return transactionInfoHolder.get();
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [java.lang.Throwable, org.springframework.transaction.NoTransactionException] */
    public static TransactionStatus currentTransactionStatus() throws NoTransactionException {
        TransactionInfo info = currentTransactionInfo();
        if (info == null || info.transactionStatus == null) {
            throw new NoTransactionException("No transaction aspect-managed TransactionStatus in scope");
        }
        return info.transactionStatus;
    }

    public void setTransactionManagerBeanName(String transactionManagerBeanName) {
        this.transactionManagerBeanName = transactionManagerBeanName;
    }

    public final String getTransactionManagerBeanName() {
        return this.transactionManagerBeanName;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public PlatformTransactionManager getTransactionManager() {
        return this.transactionManager;
    }

    public void setTransactionAttributes(Properties transactionAttributes) {
        NameMatchTransactionAttributeSource tas = new NameMatchTransactionAttributeSource();
        tas.setProperties(transactionAttributes);
        this.transactionAttributeSource = tas;
    }

    public void setTransactionAttributeSources(TransactionAttributeSource[] transactionAttributeSources) {
        this.transactionAttributeSource = new CompositeTransactionAttributeSource(transactionAttributeSources);
    }

    public void setTransactionAttributeSource(TransactionAttributeSource transactionAttributeSource) {
        this.transactionAttributeSource = transactionAttributeSource;
    }

    public TransactionAttributeSource getTransactionAttributeSource() {
        return this.transactionAttributeSource;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public final BeanFactory getBeanFactory() {
        return this.beanFactory;
    }

    public void afterPropertiesSet() {
        if (this.transactionManager == null && this.beanFactory == null) {
            throw new IllegalStateException("Setting the property 'transactionManager' or running in a ListableBeanFactory is required");
        }
        if (this.transactionAttributeSource == null) {
            throw new IllegalStateException("Either 'transactionAttributeSource' or 'transactionAttributes' is required: If there are no transactional methods, then don't use a transaction aspect.");
        }
    }

    public Object invokeWithinTransaction(Method method, Class<?> targetClass, final InvocationCallback invocation) throws Throwable {
        final TransactionAttribute txAttr = getTransactionAttributeSource().getTransactionAttribute(method, targetClass);
        final PlatformTransactionManager tm = determineTransactionManager(txAttr);
        final String joinpointIdentification = methodIdentification(method, targetClass);
        if (txAttr == null || !(tm instanceof CallbackPreferringPlatformTransactionManager)) {
            TransactionInfo txInfo = createTransactionIfNecessary(tm, txAttr, joinpointIdentification);
            try {
                Object retVal = invocation.proceedWithInvocation();
                cleanupTransactionInfo(txInfo);
                commitTransactionAfterReturning(txInfo);
                return retVal;
            } finally {
                try {
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        try {
            Object result = ((CallbackPreferringPlatformTransactionManager) tm).execute(txAttr, new TransactionCallback<Object>() { // from class: org.springframework.transaction.interceptor.TransactionAspectSupport.1
                @Override // org.springframework.transaction.support.TransactionCallback
                public Object doInTransaction(TransactionStatus status) {
                    TransactionInfo txInfo2 = TransactionAspectSupport.this.prepareTransactionInfo(tm, txAttr, joinpointIdentification, status);
                    try {
                        Object proceedWithInvocation = invocation.proceedWithInvocation();
                        TransactionAspectSupport.this.cleanupTransactionInfo(txInfo2);
                        return proceedWithInvocation;
                    } catch (Throwable ex) {
                        try {
                            if (txAttr.rollbackOn(ex)) {
                                if (ex instanceof RuntimeException) {
                                    throw ((RuntimeException) ex);
                                }
                                throw new ThrowableHolderException(ex);
                            }
                            ThrowableHolder throwableHolder = new ThrowableHolder(ex);
                            TransactionAspectSupport.this.cleanupTransactionInfo(txInfo2);
                            return throwableHolder;
                        } catch (Throwable th2) {
                            TransactionAspectSupport.this.cleanupTransactionInfo(txInfo2);
                            throw th2;
                        }
                    }
                }
            });
            if (result instanceof ThrowableHolder) {
                throw ((ThrowableHolder) result).getThrowable();
            }
            return result;
        } catch (ThrowableHolderException ex) {
            throw ex.getCause();
        }
    }

    protected PlatformTransactionManager determineTransactionManager(TransactionAttribute txAttr) {
        if (this.transactionManager != null || this.beanFactory == null || txAttr == null) {
            return this.transactionManager;
        }
        String qualifier = txAttr.getQualifier();
        if (StringUtils.hasLength(qualifier)) {
            PlatformTransactionManager txManager = this.transactionManagerCache.get(qualifier);
            if (txManager == null) {
                txManager = (PlatformTransactionManager) BeanFactoryAnnotationUtils.qualifiedBeanOfType(this.beanFactory, PlatformTransactionManager.class, qualifier);
                this.transactionManagerCache.putIfAbsent(qualifier, txManager);
            }
            return txManager;
        } else if (this.transactionManagerBeanName != null) {
            PlatformTransactionManager txManager2 = this.transactionManagerCache.get(this.transactionManagerBeanName);
            if (txManager2 == null) {
                txManager2 = (PlatformTransactionManager) this.beanFactory.getBean(this.transactionManagerBeanName, PlatformTransactionManager.class);
                this.transactionManagerCache.putIfAbsent(this.transactionManagerBeanName, txManager2);
            }
            return txManager2;
        } else {
            this.transactionManager = (PlatformTransactionManager) this.beanFactory.getBean(PlatformTransactionManager.class);
            return this.transactionManager;
        }
    }

    protected String methodIdentification(Method method, Class<?> targetClass) {
        String simpleMethodId = methodIdentification(method);
        if (simpleMethodId != null) {
            return simpleMethodId;
        }
        return (targetClass != null ? targetClass : method.getDeclaringClass()).getName() + "." + method.getName();
    }

    @Deprecated
    protected String methodIdentification(Method method) {
        return null;
    }

    @Deprecated
    protected TransactionInfo createTransactionIfNecessary(Method method, Class<?> targetClass) {
        TransactionAttribute txAttr = getTransactionAttributeSource().getTransactionAttribute(method, targetClass);
        PlatformTransactionManager tm = determineTransactionManager(txAttr);
        return createTransactionIfNecessary(tm, txAttr, methodIdentification(method, targetClass));
    }

    protected TransactionInfo createTransactionIfNecessary(PlatformTransactionManager tm, TransactionAttribute txAttr, final String joinpointIdentification) {
        if (txAttr != null && txAttr.getName() == null) {
            txAttr = new DelegatingTransactionAttribute(txAttr) { // from class: org.springframework.transaction.interceptor.TransactionAspectSupport.2
                @Override // org.springframework.transaction.support.DelegatingTransactionDefinition, org.springframework.transaction.TransactionDefinition
                public String getName() {
                    return joinpointIdentification;
                }
            };
        }
        TransactionStatus status = null;
        if (txAttr != null) {
            if (tm != null) {
                status = tm.getTransaction(txAttr);
            } else if (this.logger.isDebugEnabled()) {
                this.logger.debug("Skipping transactional joinpoint [" + joinpointIdentification + "] because no transaction manager has been configured");
            }
        }
        return prepareTransactionInfo(tm, txAttr, joinpointIdentification, status);
    }

    protected TransactionInfo prepareTransactionInfo(PlatformTransactionManager tm, TransactionAttribute txAttr, String joinpointIdentification, TransactionStatus status) {
        TransactionInfo txInfo = new TransactionInfo(tm, txAttr, joinpointIdentification);
        if (txAttr != null) {
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("Getting transaction for [" + txInfo.getJoinpointIdentification() + "]");
            }
            txInfo.newTransactionStatus(status);
        } else if (this.logger.isTraceEnabled()) {
            this.logger.trace("Don't need to create transaction for [" + joinpointIdentification + "]: This method isn't transactional.");
        }
        txInfo.bindToThread();
        return txInfo;
    }

    protected void commitTransactionAfterReturning(TransactionInfo txInfo) {
        if (txInfo != null && txInfo.hasTransaction()) {
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("Completing transaction for [" + txInfo.getJoinpointIdentification() + "]");
            }
            txInfo.getTransactionManager().commit(txInfo.getTransactionStatus());
        }
    }

    /* JADX WARN: Type inference failed for: r7v2, types: [java.lang.Throwable, org.springframework.transaction.TransactionSystemException] */
    /* JADX WARN: Type inference failed for: r7v5, types: [java.lang.Throwable, org.springframework.transaction.TransactionSystemException] */
    protected void completeTransactionAfterThrowing(TransactionInfo txInfo, Throwable ex) {
        if (txInfo != null && txInfo.hasTransaction()) {
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("Completing transaction for [" + txInfo.getJoinpointIdentification() + "] after exception: " + ex);
            }
            if (txInfo.transactionAttribute.rollbackOn(ex)) {
                try {
                    txInfo.getTransactionManager().rollback(txInfo.getTransactionStatus());
                    return;
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
            try {
                txInfo.getTransactionManager().commit(txInfo.getTransactionStatus());
            } catch (Error err2) {
                this.logger.error("Application exception overridden by commit error", ex);
                throw err2;
            } catch (RuntimeException ex22) {
                this.logger.error("Application exception overridden by commit exception", ex);
                throw ex22;
            } catch (TransactionSystemException e2) {
                this.logger.error("Application exception overridden by commit exception", ex);
                e2.initApplicationException(ex);
                throw e2;
            }
        }
    }

    protected void cleanupTransactionInfo(TransactionInfo txInfo) {
        if (txInfo == null) {
            return;
        }
        txInfo.restoreThreadLocalStatus();
    }

    /* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/interceptor/TransactionAspectSupport$TransactionInfo.class */
    public final class TransactionInfo {
        private final PlatformTransactionManager transactionManager;
        private final TransactionAttribute transactionAttribute;
        private final String joinpointIdentification;
        private TransactionStatus transactionStatus;
        private TransactionInfo oldTransactionInfo;

        public TransactionInfo(PlatformTransactionManager transactionManager, TransactionAttribute transactionAttribute, String joinpointIdentification) {
            TransactionAspectSupport.this = this$0;
            this.transactionManager = transactionManager;
            this.transactionAttribute = transactionAttribute;
            this.joinpointIdentification = joinpointIdentification;
        }

        public PlatformTransactionManager getTransactionManager() {
            return this.transactionManager;
        }

        public TransactionAttribute getTransactionAttribute() {
            return this.transactionAttribute;
        }

        public String getJoinpointIdentification() {
            return this.joinpointIdentification;
        }

        public void newTransactionStatus(TransactionStatus status) {
            this.transactionStatus = status;
        }

        public TransactionStatus getTransactionStatus() {
            return this.transactionStatus;
        }

        public boolean hasTransaction() {
            return this.transactionStatus != null;
        }

        public void bindToThread() {
            this.oldTransactionInfo = (TransactionInfo) TransactionAspectSupport.transactionInfoHolder.get();
            TransactionAspectSupport.transactionInfoHolder.set(this);
        }

        public void restoreThreadLocalStatus() {
            TransactionAspectSupport.transactionInfoHolder.set(this.oldTransactionInfo);
        }

        public String toString() {
            return this.transactionAttribute.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/interceptor/TransactionAspectSupport$ThrowableHolder.class */
    public static class ThrowableHolder {
        private final Throwable throwable;

        public ThrowableHolder(Throwable throwable) {
            this.throwable = throwable;
        }

        public final Throwable getThrowable() {
            return this.throwable;
        }
    }

    /* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/interceptor/TransactionAspectSupport$ThrowableHolderException.class */
    private static class ThrowableHolderException extends RuntimeException {
        public ThrowableHolderException(Throwable throwable) {
            super(throwable);
        }

        @Override // java.lang.Throwable
        public String toString() {
            return getCause().toString();
        }
    }
}
