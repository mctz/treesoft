package org.springframework.transaction.interceptor;

import java.io.Serializable;
import org.springframework.transaction.support.DelegatingTransactionDefinition;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/interceptor/DelegatingTransactionAttribute.class */
public abstract class DelegatingTransactionAttribute extends DelegatingTransactionDefinition implements TransactionAttribute, Serializable {
    private final TransactionAttribute targetAttribute;

    public DelegatingTransactionAttribute(TransactionAttribute targetAttribute) {
        super(targetAttribute);
        this.targetAttribute = targetAttribute;
    }

    @Override // org.springframework.transaction.interceptor.TransactionAttribute
    public String getQualifier() {
        return this.targetAttribute.getQualifier();
    }

    @Override // org.springframework.transaction.interceptor.TransactionAttribute
    public boolean rollbackOn(Throwable ex) {
        return this.targetAttribute.rollbackOn(ex);
    }
}
