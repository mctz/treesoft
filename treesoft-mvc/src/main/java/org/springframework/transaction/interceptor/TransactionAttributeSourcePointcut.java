package org.springframework.transaction.interceptor;

import java.io.Serializable;
import java.lang.reflect.Method;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.util.ObjectUtils;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/interceptor/TransactionAttributeSourcePointcut.class */
abstract class TransactionAttributeSourcePointcut extends StaticMethodMatcherPointcut implements Serializable {
    protected abstract TransactionAttributeSource getTransactionAttributeSource();

    public boolean matches(Method method, Class<?> targetClass) {
        TransactionAttributeSource tas = getTransactionAttributeSource();
        return tas == null || tas.getTransactionAttribute(method, targetClass) != null;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TransactionAttributeSourcePointcut)) {
            return false;
        }
        TransactionAttributeSourcePointcut otherPc = (TransactionAttributeSourcePointcut) other;
        return ObjectUtils.nullSafeEquals(getTransactionAttributeSource(), otherPc.getTransactionAttributeSource());
    }

    public int hashCode() {
        return TransactionAttributeSourcePointcut.class.hashCode();
    }

    public String toString() {
        return getClass().getName() + ": " + getTransactionAttributeSource();
    }
}
