package org.springframework.transaction.interceptor;

import java.lang.reflect.Method;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/interceptor/TransactionAttributeSource.class */
public interface TransactionAttributeSource {
    TransactionAttribute getTransactionAttribute(Method method, Class<?> cls);
}
