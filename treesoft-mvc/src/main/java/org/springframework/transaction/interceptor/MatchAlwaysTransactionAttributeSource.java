package org.springframework.transaction.interceptor;

import java.io.Serializable;
import java.lang.reflect.Method;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/interceptor/MatchAlwaysTransactionAttributeSource.class */
public class MatchAlwaysTransactionAttributeSource implements TransactionAttributeSource, Serializable {
    private TransactionAttribute transactionAttribute = new DefaultTransactionAttribute();

    public void setTransactionAttribute(TransactionAttribute transactionAttribute) {
        this.transactionAttribute = transactionAttribute;
    }

    @Override // org.springframework.transaction.interceptor.TransactionAttributeSource
    public TransactionAttribute getTransactionAttribute(Method method, Class<?> targetClass) {
        if (method == null || ClassUtils.isUserLevelMethod(method)) {
            return this.transactionAttribute;
        }
        return null;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MatchAlwaysTransactionAttributeSource)) {
            return false;
        }
        MatchAlwaysTransactionAttributeSource otherTas = (MatchAlwaysTransactionAttributeSource) other;
        return ObjectUtils.nullSafeEquals(this.transactionAttribute, otherTas.transactionAttribute);
    }

    public int hashCode() {
        return MatchAlwaysTransactionAttributeSource.class.hashCode();
    }

    public String toString() {
        return getClass().getName() + ": " + this.transactionAttribute;
    }
}
