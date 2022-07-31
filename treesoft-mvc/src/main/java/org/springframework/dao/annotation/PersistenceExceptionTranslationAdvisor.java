package org.springframework.dao.annotation;

import java.lang.annotation.Annotation;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.dao.support.PersistenceExceptionTranslationInterceptor;
import org.springframework.dao.support.PersistenceExceptionTranslator;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/dao/annotation/PersistenceExceptionTranslationAdvisor.class */
public class PersistenceExceptionTranslationAdvisor extends AbstractPointcutAdvisor {
    private final PersistenceExceptionTranslationInterceptor advice;
    private final AnnotationMatchingPointcut pointcut;

    public PersistenceExceptionTranslationAdvisor(PersistenceExceptionTranslator persistenceExceptionTranslator, Class<? extends Annotation> repositoryAnnotationType) {
        this.advice = new PersistenceExceptionTranslationInterceptor(persistenceExceptionTranslator);
        this.pointcut = new AnnotationMatchingPointcut(repositoryAnnotationType, true);
    }

    public PersistenceExceptionTranslationAdvisor(ListableBeanFactory beanFactory, Class<? extends Annotation> repositoryAnnotationType) {
        this.advice = new PersistenceExceptionTranslationInterceptor(beanFactory);
        this.pointcut = new AnnotationMatchingPointcut(repositoryAnnotationType, true);
    }

    public Advice getAdvice() {
        return this.advice;
    }

    public Pointcut getPointcut() {
        return this.pointcut;
    }
}
