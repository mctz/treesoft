package org.springframework.transaction.interceptor;

import java.io.Serializable;
import java.lang.reflect.Method;
import org.springframework.util.Assert;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/interceptor/CompositeTransactionAttributeSource.class */
public class CompositeTransactionAttributeSource implements TransactionAttributeSource, Serializable {
    private final TransactionAttributeSource[] transactionAttributeSources;

    public CompositeTransactionAttributeSource(TransactionAttributeSource[] transactionAttributeSources) {
        Assert.notNull(transactionAttributeSources, "TransactionAttributeSource array must not be null");
        this.transactionAttributeSources = transactionAttributeSources;
    }

    public final TransactionAttributeSource[] getTransactionAttributeSources() {
        return this.transactionAttributeSources;
    }

    @Override // org.springframework.transaction.interceptor.TransactionAttributeSource
    public TransactionAttribute getTransactionAttribute(Method method, Class<?> targetClass) {
        TransactionAttributeSource[] transactionAttributeSourceArr;
        for (TransactionAttributeSource tas : this.transactionAttributeSources) {
            TransactionAttribute ta = tas.getTransactionAttribute(method, targetClass);
            if (ta != null) {
                return ta;
            }
        }
        return null;
    }
}
