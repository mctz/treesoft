package org.springframework.transaction.interceptor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.PatternMatchUtils;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/interceptor/MethodMapTransactionAttributeSource.class */
public class MethodMapTransactionAttributeSource implements TransactionAttributeSource, BeanClassLoaderAware, InitializingBean {
    private Map<String, TransactionAttribute> methodMap;
    protected final Log logger = LogFactory.getLog(getClass());
    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
    private boolean eagerlyInitialized = false;
    private boolean initialized = false;
    private final Map<Method, TransactionAttribute> transactionAttributeMap = new HashMap();
    private final Map<Method, String> methodNameMap = new HashMap();

    public void setMethodMap(Map<String, TransactionAttribute> methodMap) {
        this.methodMap = methodMap;
    }

    public void setBeanClassLoader(ClassLoader beanClassLoader) {
        this.beanClassLoader = beanClassLoader;
    }

    public void afterPropertiesSet() {
        initMethodMap(this.methodMap);
        this.eagerlyInitialized = true;
        this.initialized = true;
    }

    protected void initMethodMap(Map<String, TransactionAttribute> methodMap) {
        if (methodMap != null) {
            for (Map.Entry<String, TransactionAttribute> entry : methodMap.entrySet()) {
                addTransactionalMethod(entry.getKey(), entry.getValue());
            }
        }
    }

    public void addTransactionalMethod(String name, TransactionAttribute attr) {
        Assert.notNull(name, "Name must not be null");
        int lastDotIndex = name.lastIndexOf(".");
        if (lastDotIndex == -1) {
            throw new IllegalArgumentException("'" + name + "' is not a valid method name: format is FQN.methodName");
        }
        String className = name.substring(0, lastDotIndex);
        String methodName = name.substring(lastDotIndex + 1);
        Class<?> clazz = ClassUtils.resolveClassName(className, this.beanClassLoader);
        addTransactionalMethod(clazz, methodName, attr);
    }

    public void addTransactionalMethod(Class<?> clazz, String mappedName, TransactionAttribute attr) {
        Assert.notNull(clazz, "Class must not be null");
        Assert.notNull(mappedName, "Mapped name must not be null");
        String name = clazz.getName() + '.' + mappedName;
        Method[] methods = clazz.getDeclaredMethods();
        List<Method> matchingMethods = new ArrayList<>();
        for (Method method : methods) {
            if (isMatch(method.getName(), mappedName)) {
                matchingMethods.add(method);
            }
        }
        if (matchingMethods.isEmpty()) {
            throw new IllegalArgumentException("Couldn't find method '" + mappedName + "' on class [" + clazz.getName() + "]");
        }
        for (Method method2 : matchingMethods) {
            String regMethodName = this.methodNameMap.get(method2);
            if (regMethodName == null || (!regMethodName.equals(name) && regMethodName.length() <= name.length())) {
                if (this.logger.isDebugEnabled() && regMethodName != null) {
                    this.logger.debug("Replacing attribute for transactional method [" + method2 + "]: current name '" + name + "' is more specific than '" + regMethodName + "'");
                }
                this.methodNameMap.put(method2, name);
                addTransactionalMethod(method2, attr);
            } else if (this.logger.isDebugEnabled()) {
                this.logger.debug("Keeping attribute for transactional method [" + method2 + "]: current name '" + name + "' is not more specific than '" + regMethodName + "'");
            }
        }
    }

    public void addTransactionalMethod(Method method, TransactionAttribute attr) {
        Assert.notNull(method, "Method must not be null");
        Assert.notNull(attr, "TransactionAttribute must not be null");
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Adding transactional method [" + method + "] with attribute [" + attr + "]");
        }
        this.transactionAttributeMap.put(method, attr);
    }

    protected boolean isMatch(String methodName, String mappedName) {
        return PatternMatchUtils.simpleMatch(mappedName, methodName);
    }

    @Override // org.springframework.transaction.interceptor.TransactionAttributeSource
    public TransactionAttribute getTransactionAttribute(Method method, Class<?> targetClass) {
        TransactionAttribute transactionAttribute;
        if (this.eagerlyInitialized) {
            return this.transactionAttributeMap.get(method);
        }
        synchronized (this.transactionAttributeMap) {
            if (!this.initialized) {
                initMethodMap(this.methodMap);
                this.initialized = true;
            }
            transactionAttribute = this.transactionAttributeMap.get(method);
        }
        return transactionAttribute;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MethodMapTransactionAttributeSource)) {
            return false;
        }
        MethodMapTransactionAttributeSource otherTas = (MethodMapTransactionAttributeSource) other;
        return ObjectUtils.nullSafeEquals(this.methodMap, otherTas.methodMap);
    }

    public int hashCode() {
        return MethodMapTransactionAttributeSource.class.hashCode();
    }

    public String toString() {
        return getClass().getName() + ": " + this.methodMap;
    }
}
