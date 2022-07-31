package org.springframework.transaction.interceptor;

import org.aopalliance.aop.Advice;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/interceptor/TransactionAttributeSourceAdvisor.class */
public class TransactionAttributeSourceAdvisor extends AbstractPointcutAdvisor {
    private TransactionInterceptor transactionInterceptor;
    private final TransactionAttributeSourcePointcut pointcut = new TransactionAttributeSourcePointcut() { // from class: org.springframework.transaction.interceptor.TransactionAttributeSourceAdvisor.1
        @Override // org.springframework.transaction.interceptor.TransactionAttributeSourcePointcut
        protected TransactionAttributeSource getTransactionAttributeSource() {
            if (TransactionAttributeSourceAdvisor.this.transactionInterceptor != null) {
                return TransactionAttributeSourceAdvisor.this.transactionInterceptor.getTransactionAttributeSource();
            }
            return null;
        }
    };

    public TransactionAttributeSourceAdvisor() {
    }

    public TransactionAttributeSourceAdvisor(TransactionInterceptor interceptor) {
        setTransactionInterceptor(interceptor);
    }

    public void setTransactionInterceptor(TransactionInterceptor interceptor) {
        this.transactionInterceptor = interceptor;
    }

    public void setClassFilter(ClassFilter classFilter) {
        this.pointcut.setClassFilter(classFilter);
    }

    public Advice getAdvice() {
        return this.transactionInterceptor;
    }

    public Pointcut getPointcut() {
        return this.pointcut;
    }
}
