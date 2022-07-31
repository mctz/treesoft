package org.springframework.jca.support;

import javax.resource.ResourceException;
import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ManagedConnectionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/jca/support/LocalConnectionFactoryBean.class */
public class LocalConnectionFactoryBean implements FactoryBean<Object>, InitializingBean {
    private ManagedConnectionFactory managedConnectionFactory;
    private ConnectionManager connectionManager;
    private Object connectionFactory;

    public void setManagedConnectionFactory(ManagedConnectionFactory managedConnectionFactory) {
        this.managedConnectionFactory = managedConnectionFactory;
    }

    public void setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public void afterPropertiesSet() throws ResourceException {
        if (this.managedConnectionFactory == null) {
            throw new IllegalArgumentException("Property 'managedConnectionFactory' is required");
        }
        if (this.connectionManager != null) {
            this.connectionFactory = this.managedConnectionFactory.createConnectionFactory(this.connectionManager);
        } else {
            this.connectionFactory = this.managedConnectionFactory.createConnectionFactory();
        }
    }

    public Object getObject() {
        return this.connectionFactory;
    }

    public Class<?> getObjectType() {
        if (this.connectionFactory != null) {
            return this.connectionFactory.getClass();
        }
        return null;
    }

    public boolean isSingleton() {
        return true;
    }
}
