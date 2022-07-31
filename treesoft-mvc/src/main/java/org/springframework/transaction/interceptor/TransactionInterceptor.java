package org.springframework.transaction.interceptor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Properties;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/interceptor/TransactionInterceptor.class */
public class TransactionInterceptor extends TransactionAspectSupport implements MethodInterceptor, Serializable {
    public TransactionInterceptor() {
    }

    public TransactionInterceptor(PlatformTransactionManager ptm, Properties attributes) {
        setTransactionManager(ptm);
        setTransactionAttributes(attributes);
    }

    public TransactionInterceptor(PlatformTransactionManager ptm, TransactionAttributeSource tas) {
        setTransactionManager(ptm);
        setTransactionAttributeSource(tas);
    }

    public Object invoke(final MethodInvocation invocation) throws Throwable {
        Class<?> targetClass = invocation.getThis() != null ? AopUtils.getTargetClass(invocation.getThis()) : null;
        return invokeWithinTransaction(invocation.getMethod(), targetClass, new InvocationCallback() { // from class: org.springframework.transaction.interceptor.TransactionInterceptor.1
            @Override // org.springframework.transaction.interceptor.TransactionAspectSupport.InvocationCallback
            public Object proceedWithInvocation() throws Throwable {
                return invocation.proceed();
            }
        });
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeObject(getTransactionManagerBeanName());
        oos.writeObject(getTransactionManager());
        oos.writeObject(getTransactionAttributeSource());
        oos.writeObject(getBeanFactory());
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        setTransactionManagerBeanName((String) ois.readObject());
        setTransactionManager((PlatformTransactionManager) ois.readObject());
        setTransactionAttributeSource((TransactionAttributeSource) ois.readObject());
        setBeanFactory((BeanFactory) ois.readObject());
    }
}
