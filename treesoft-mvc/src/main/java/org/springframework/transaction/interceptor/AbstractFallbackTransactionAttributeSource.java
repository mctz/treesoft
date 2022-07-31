package org.springframework.transaction.interceptor;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/interceptor/AbstractFallbackTransactionAttributeSource.class */
public abstract class AbstractFallbackTransactionAttributeSource implements TransactionAttributeSource {
    private static final TransactionAttribute NULL_TRANSACTION_ATTRIBUTE = new DefaultTransactionAttribute();
    protected final Log logger = LogFactory.getLog(getClass());
    final Map<Object, TransactionAttribute> attributeCache = new ConcurrentHashMap(1024);

    protected abstract TransactionAttribute findTransactionAttribute(Method method);

    protected abstract TransactionAttribute findTransactionAttribute(Class<?> cls);

    @Override // org.springframework.transaction.interceptor.TransactionAttributeSource
    public TransactionAttribute getTransactionAttribute(Method method, Class<?> targetClass) {
        Object cacheKey = getCacheKey(method, targetClass);
        Object cached = this.attributeCache.get(cacheKey);
        if (cached != null) {
            if (cached == NULL_TRANSACTION_ATTRIBUTE) {
                return null;
            }
            return (TransactionAttribute) cached;
        }
        TransactionAttribute txAtt = computeTransactionAttribute(method, targetClass);
        if (txAtt == null) {
            this.attributeCache.put(cacheKey, NULL_TRANSACTION_ATTRIBUTE);
        } else {
            if (this.logger.isDebugEnabled()) {
                Class<?> classToLog = targetClass != null ? targetClass : method.getDeclaringClass();
                this.logger.debug("Adding transactional method '" + classToLog.getSimpleName() + "." + method.getName() + "' with attribute: " + txAtt);
            }
            this.attributeCache.put(cacheKey, txAtt);
        }
        return txAtt;
    }

    protected Object getCacheKey(Method method, Class<?> targetClass) {
        return new DefaultCacheKey(method, targetClass);
    }

    private TransactionAttribute computeTransactionAttribute(Method method, Class<?> targetClass) {
        if (allowPublicMethodsOnly() && !Modifier.isPublic(method.getModifiers())) {
            return null;
        }
        Class<?> userClass = ClassUtils.getUserClass(targetClass);
        Method specificMethod = BridgeMethodResolver.findBridgedMethod(ClassUtils.getMostSpecificMethod(method, userClass));
        TransactionAttribute txAtt = findTransactionAttribute(specificMethod);
        if (txAtt != null) {
            return txAtt;
        }
        TransactionAttribute txAtt2 = findTransactionAttribute(specificMethod.getDeclaringClass());
        if (txAtt2 != null) {
            return txAtt2;
        }
        if (specificMethod != method) {
            TransactionAttribute txAtt3 = findTransactionAttribute(method);
            if (txAtt3 != null) {
                return txAtt3;
            }
            return findTransactionAttribute(method.getDeclaringClass());
        }
        return null;
    }

    protected boolean allowPublicMethodsOnly() {
        return false;
    }

    /* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/interceptor/AbstractFallbackTransactionAttributeSource$DefaultCacheKey.class */
    public static class DefaultCacheKey {
        private final Method method;
        private final Class<?> targetClass;

        public DefaultCacheKey(Method method, Class<?> targetClass) {
            this.method = method;
            this.targetClass = targetClass;
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof DefaultCacheKey)) {
                return false;
            }
            DefaultCacheKey otherKey = (DefaultCacheKey) other;
            return this.method.equals(otherKey.method) && ObjectUtils.nullSafeEquals(this.targetClass, otherKey.targetClass);
        }

        public int hashCode() {
            return this.method.hashCode();
        }
    }
}
