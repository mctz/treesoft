package org.springframework.transaction.annotation;

import java.lang.reflect.AnnotatedElement;
import org.springframework.transaction.interceptor.TransactionAttribute;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/annotation/TransactionAnnotationParser.class */
public interface TransactionAnnotationParser {
    TransactionAttribute parseTransactionAnnotation(AnnotatedElement annotatedElement);
}
