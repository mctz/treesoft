package org.springframework.dao.annotation;

import java.lang.annotation.Annotation;
import org.springframework.aop.framework.AbstractAdvisingBeanPostProcessor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/dao/annotation/PersistenceExceptionTranslationPostProcessor.class */
public class PersistenceExceptionTranslationPostProcessor extends AbstractAdvisingBeanPostProcessor implements BeanFactoryAware {
    private Class<? extends Annotation> repositoryAnnotationType = Repository.class;

    public void setRepositoryAnnotationType(Class<? extends Annotation> repositoryAnnotationType) {
        Assert.notNull(repositoryAnnotationType, "'repositoryAnnotationType' must not be null");
        this.repositoryAnnotationType = repositoryAnnotationType;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        if (!(beanFactory instanceof ListableBeanFactory)) {
            throw new IllegalArgumentException("Cannot use PersistenceExceptionTranslator autodetection without ListableBeanFactory");
        }
        this.advisor = new PersistenceExceptionTranslationAdvisor((ListableBeanFactory) beanFactory, this.repositoryAnnotationType);
    }
}
