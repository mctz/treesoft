package org.springframework.transaction.interceptor;

import org.springframework.transaction.support.DefaultTransactionDefinition;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/interceptor/DefaultTransactionAttribute.class */
public class DefaultTransactionAttribute extends DefaultTransactionDefinition implements TransactionAttribute {
    private String qualifier;

    public DefaultTransactionAttribute() {
    }

    public DefaultTransactionAttribute(TransactionAttribute other) {
        super(other);
    }

    public DefaultTransactionAttribute(int propagationBehavior) {
        super(propagationBehavior);
    }

    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }

    @Override // org.springframework.transaction.interceptor.TransactionAttribute
    public String getQualifier() {
        return this.qualifier;
    }

    public boolean rollbackOn(Throwable ex) {
        return (ex instanceof RuntimeException) || (ex instanceof Error);
    }

    public final StringBuilder getAttributeDescription() {
        StringBuilder result = getDefinitionDescription();
        if (this.qualifier != null) {
            result.append("; '").append(this.qualifier).append("'");
        }
        return result;
    }
}
