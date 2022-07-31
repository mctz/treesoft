package org.springframework.transaction.interceptor;

import org.springframework.transaction.TransactionDefinition;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/interceptor/TransactionAttribute.class */
public interface TransactionAttribute extends TransactionDefinition {
    String getQualifier();

    boolean rollbackOn(Throwable th);
}
