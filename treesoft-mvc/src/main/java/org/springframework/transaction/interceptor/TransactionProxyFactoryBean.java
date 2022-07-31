package org.springframework.transaction.interceptor;

import java.util.Properties;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.AbstractSingletonProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.transaction.PlatformTransactionManager;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/interceptor/TransactionProxyFactoryBean.class */
public class TransactionProxyFactoryBean extends AbstractSingletonProxyFactoryBean implements BeanFactoryAware {
    private final TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
    private Pointcut pointcut;

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionInterceptor.setTransactionManager(transactionManager);
    }

    public void setTransactionAttributes(Properties transactionAttributes) {
        this.transactionInterceptor.setTransactionAttributes(transactionAttributes);
    }

    public void setTransactionAttributeSource(TransactionAttributeSource transactionAttributeSource) {
        this.transactionInterceptor.setTransactionAttributeSource(transactionAttributeSource);
    }

    public void setPointcut(Pointcut pointcut) {
        this.pointcut = pointcut;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.transactionInterceptor.setBeanFactory(beanFactory);
    }

    protected Object createMainInterceptor() {
        this.transactionInterceptor.afterPropertiesSet();
        if (this.pointcut != null) {
            return new DefaultPointcutAdvisor(this.pointcut, this.transactionInterceptor);
        }
        return new TransactionAttributeSourceAdvisor(this.transactionInterceptor);
    }
}
