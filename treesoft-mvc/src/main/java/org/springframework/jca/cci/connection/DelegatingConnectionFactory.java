package org.springframework.jca.cci.connection;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.resource.ResourceException;
import javax.resource.cci.Connection;
import javax.resource.cci.ConnectionFactory;
import javax.resource.cci.ConnectionSpec;
import javax.resource.cci.RecordFactory;
import javax.resource.cci.ResourceAdapterMetaData;
import org.springframework.beans.factory.InitializingBean;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/jca/cci/connection/DelegatingConnectionFactory.class */
public class DelegatingConnectionFactory implements ConnectionFactory, InitializingBean {
    private ConnectionFactory targetConnectionFactory;

    public void setTargetConnectionFactory(ConnectionFactory targetConnectionFactory) {
        this.targetConnectionFactory = targetConnectionFactory;
    }

    public ConnectionFactory getTargetConnectionFactory() {
        return this.targetConnectionFactory;
    }

    public void afterPropertiesSet() {
        if (getTargetConnectionFactory() == null) {
            throw new IllegalArgumentException("Property 'targetConnectionFactory' is required");
        }
    }

    public Connection getConnection() throws ResourceException {
        return getTargetConnectionFactory().getConnection();
    }

    public Connection getConnection(ConnectionSpec connectionSpec) throws ResourceException {
        return getTargetConnectionFactory().getConnection(connectionSpec);
    }

    public RecordFactory getRecordFactory() throws ResourceException {
        return getTargetConnectionFactory().getRecordFactory();
    }

    public ResourceAdapterMetaData getMetaData() throws ResourceException {
        return getTargetConnectionFactory().getMetaData();
    }

    public Reference getReference() throws NamingException {
        return getTargetConnectionFactory().getReference();
    }

    public void setReference(Reference reference) {
        getTargetConnectionFactory().setReference(reference);
    }
}
