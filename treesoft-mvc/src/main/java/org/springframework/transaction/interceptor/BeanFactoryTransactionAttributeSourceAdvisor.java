package org.springframework.transaction.interceptor;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/interceptor/BeanFactoryTransactionAttributeSourceAdvisor.class */
public class BeanFactoryTransactionAttributeSourceAdvisor extends AbstractBeanFactoryPointcutAdvisor {
    private TransactionAttributeSource transactionAttributeSource;
    private final TransactionAttributeSourcePointcut pointcut = new TransactionAttributeSourcePointcut() { // from class: org.springframework.transaction.interceptor.BeanFactoryTransactionAttributeSourceAdvisor.1
        @Override // org.springframework.transaction.interceptor.TransactionAttributeSourcePointcut
        protected TransactionAttributeSource getTransactionAttributeSource() {
            return BeanFactoryTransactionAttributeSourceAdvisor.this.transactionAttributeSource;
        }
    };

    public void setTransactionAttributeSource(TransactionAttributeSource transactionAttributeSource) {
        this.transactionAttributeSource = transactionAttributeSource;
    }

    public void setClassFilter(ClassFilter classFilter) {
        this.pointcut.setClassFilter(classFilter);
    }

    public Pointcut getPointcut() {
        return this.pointcut;
    }
}
