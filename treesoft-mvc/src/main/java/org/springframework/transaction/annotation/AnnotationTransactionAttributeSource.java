package org.springframework.transaction.annotation;

import java.io.Serializable;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import org.springframework.transaction.interceptor.AbstractFallbackTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/annotation/AnnotationTransactionAttributeSource.class */
public class AnnotationTransactionAttributeSource extends AbstractFallbackTransactionAttributeSource implements Serializable {
    private static final boolean jta12Present = ClassUtils.isPresent("javax.transaction.Transactional", AnnotationTransactionAttributeSource.class.getClassLoader());
    private static final boolean ejb3Present = ClassUtils.isPresent("javax.ejb.TransactionAttribute", AnnotationTransactionAttributeSource.class.getClassLoader());
    private final boolean publicMethodsOnly;
    private final Set<TransactionAnnotationParser> annotationParsers;

    public AnnotationTransactionAttributeSource() {
        this(true);
    }

    public AnnotationTransactionAttributeSource(boolean publicMethodsOnly) {
        this.publicMethodsOnly = publicMethodsOnly;
        this.annotationParsers = new LinkedHashSet(2);
        this.annotationParsers.add(new SpringTransactionAnnotationParser());
        if (jta12Present) {
            this.annotationParsers.add(new JtaTransactionAnnotationParser());
        }
        if (ejb3Present) {
            this.annotationParsers.add(new Ejb3TransactionAnnotationParser());
        }
    }

    public AnnotationTransactionAttributeSource(TransactionAnnotationParser annotationParser) {
        this.publicMethodsOnly = true;
        Assert.notNull(annotationParser, "TransactionAnnotationParser must not be null");
        this.annotationParsers = Collections.singleton(annotationParser);
    }

    public AnnotationTransactionAttributeSource(TransactionAnnotationParser... annotationParsers) {
        this.publicMethodsOnly = true;
        Assert.notEmpty(annotationParsers, "At least one TransactionAnnotationParser needs to be specified");
        Set<TransactionAnnotationParser> parsers = new LinkedHashSet<>(annotationParsers.length);
        Collections.addAll(parsers, annotationParsers);
        this.annotationParsers = parsers;
    }

    public AnnotationTransactionAttributeSource(Set<TransactionAnnotationParser> annotationParsers) {
        this.publicMethodsOnly = true;
        Assert.notEmpty(annotationParsers, "At least one TransactionAnnotationParser needs to be specified");
        this.annotationParsers = annotationParsers;
    }

    @Override // org.springframework.transaction.interceptor.AbstractFallbackTransactionAttributeSource
    protected TransactionAttribute findTransactionAttribute(Method method) {
        return determineTransactionAttribute(method);
    }

    @Override // org.springframework.transaction.interceptor.AbstractFallbackTransactionAttributeSource
    protected TransactionAttribute findTransactionAttribute(Class<?> clazz) {
        return determineTransactionAttribute(clazz);
    }

    protected TransactionAttribute determineTransactionAttribute(AnnotatedElement ae) {
        for (TransactionAnnotationParser annotationParser : this.annotationParsers) {
            TransactionAttribute attr = annotationParser.parseTransactionAnnotation(ae);
            if (attr != null) {
                return attr;
            }
        }
        return null;
    }

    @Override // org.springframework.transaction.interceptor.AbstractFallbackTransactionAttributeSource
    protected boolean allowPublicMethodsOnly() {
        return this.publicMethodsOnly;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AnnotationTransactionAttributeSource)) {
            return false;
        }
        AnnotationTransactionAttributeSource otherTas = (AnnotationTransactionAttributeSource) other;
        return this.annotationParsers.equals(otherTas.annotationParsers) && this.publicMethodsOnly == otherTas.publicMethodsOnly;
    }

    public int hashCode() {
        return this.annotationParsers.hashCode();
    }
}
