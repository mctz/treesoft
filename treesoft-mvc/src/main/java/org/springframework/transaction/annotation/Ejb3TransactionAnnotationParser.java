package org.springframework.transaction.annotation;

import java.io.Serializable;
import java.lang.reflect.AnnotatedElement;
import javax.ejb.ApplicationException;
import javax.ejb.TransactionAttributeType;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/annotation/Ejb3TransactionAnnotationParser.class */
public class Ejb3TransactionAnnotationParser implements TransactionAnnotationParser, Serializable {
    @Override // org.springframework.transaction.annotation.TransactionAnnotationParser
    public TransactionAttribute parseTransactionAnnotation(AnnotatedElement ae) {
        javax.ejb.TransactionAttribute ann = (javax.ejb.TransactionAttribute) ae.getAnnotation(javax.ejb.TransactionAttribute.class);
        if (ann != null) {
            return parseTransactionAnnotation(ann);
        }
        return null;
    }

    public TransactionAttribute parseTransactionAnnotation(javax.ejb.TransactionAttribute ann) {
        return new Ejb3TransactionAttribute(ann.value());
    }

    public boolean equals(Object other) {
        return this == other || (other instanceof Ejb3TransactionAnnotationParser);
    }

    public int hashCode() {
        return Ejb3TransactionAnnotationParser.class.hashCode();
    }

    /* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/annotation/Ejb3TransactionAnnotationParser$Ejb3TransactionAttribute.class */
    public static class Ejb3TransactionAttribute extends DefaultTransactionAttribute {
        public Ejb3TransactionAttribute(TransactionAttributeType type) {
            setPropagationBehaviorName(DefaultTransactionDefinition.PREFIX_PROPAGATION + type.name());
        }

        @Override // org.springframework.transaction.interceptor.DefaultTransactionAttribute, org.springframework.transaction.interceptor.TransactionAttribute
        public boolean rollbackOn(Throwable ex) {
            ApplicationException ann = ex.getClass().getAnnotation(ApplicationException.class);
            return ann != null ? ann.rollback() : super.rollbackOn(ex);
        }
    }
}
