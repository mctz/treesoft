package org.springframework.jca.cci.connection;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import javax.resource.NotSupportedException;
import javax.resource.ResourceException;
import javax.resource.cci.Connection;
import javax.resource.cci.ConnectionFactory;
import javax.resource.cci.ConnectionSpec;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.util.Assert;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/jca/cci/connection/SingleConnectionFactory.class */
public class SingleConnectionFactory extends DelegatingConnectionFactory implements DisposableBean {
    private Connection target;
    private Connection connection;
    protected final Log logger = LogFactory.getLog(getClass());
    private final Object connectionMonitor = new Object();

    public SingleConnectionFactory() {
    }

    public SingleConnectionFactory(Connection target) {
        Assert.notNull(target, "Target Connection must not be null");
        this.target = target;
        this.connection = getCloseSuppressingConnectionProxy(target);
    }

    public SingleConnectionFactory(ConnectionFactory targetConnectionFactory) {
        Assert.notNull(targetConnectionFactory, "Target ConnectionFactory must not be null");
        setTargetConnectionFactory(targetConnectionFactory);
    }

    @Override // org.springframework.jca.cci.connection.DelegatingConnectionFactory
    public void afterPropertiesSet() {
        if (this.connection == null && getTargetConnectionFactory() == null) {
            throw new IllegalArgumentException("Connection or 'targetConnectionFactory' is required");
        }
    }

    @Override // org.springframework.jca.cci.connection.DelegatingConnectionFactory
    public Connection getConnection() throws ResourceException {
        Connection connection;
        synchronized (this.connectionMonitor) {
            if (this.connection == null) {
                initConnection();
            }
            connection = this.connection;
        }
        return connection;
    }

    @Override // org.springframework.jca.cci.connection.DelegatingConnectionFactory
    public Connection getConnection(ConnectionSpec connectionSpec) throws ResourceException {
        throw new NotSupportedException("SingleConnectionFactory does not support custom ConnectionSpec");
    }

    public void destroy() {
        resetConnection();
    }

    public void initConnection() throws ResourceException {
        if (getTargetConnectionFactory() == null) {
            throw new IllegalStateException("'targetConnectionFactory' is required for lazily initializing a Connection");
        }
        synchronized (this.connectionMonitor) {
            if (this.target != null) {
                closeConnection(this.target);
            }
            this.target = doCreateConnection();
            prepareConnection(this.target);
            if (this.logger.isInfoEnabled()) {
                this.logger.info("Established shared CCI Connection: " + this.target);
            }
            this.connection = getCloseSuppressingConnectionProxy(this.target);
        }
    }

    public void resetConnection() {
        synchronized (this.connectionMonitor) {
            if (this.target != null) {
                closeConnection(this.target);
            }
            this.target = null;
            this.connection = null;
        }
    }

    protected Connection doCreateConnection() throws ResourceException {
        return getTargetConnectionFactory().getConnection();
    }

    protected void prepareConnection(Connection con) throws ResourceException {
    }

    protected void closeConnection(Connection con) {
        try {
            con.close();
        } catch (Throwable ex) {
            this.logger.warn("Could not close shared CCI Connection", ex);
        }
    }

    protected Connection getCloseSuppressingConnectionProxy(Connection target) {
        return (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(), new Class[]{Connection.class}, new CloseSuppressingInvocationHandler(target));
    }

    /* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/jca/cci/connection/SingleConnectionFactory$CloseSuppressingInvocationHandler.class */
    public static class CloseSuppressingInvocationHandler implements InvocationHandler {
        private final Connection target;

        private CloseSuppressingInvocationHandler(Connection target) {
            this.target = target;
        }

        @Override // java.lang.reflect.InvocationHandler
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getName().equals("equals")) {
                return Boolean.valueOf(proxy == args[0]);
            } else if (method.getName().equals("hashCode")) {
                return Integer.valueOf(System.identityHashCode(proxy));
            } else {
                if (method.getName().equals("close")) {
                    return null;
                }
                try {
                    return method.invoke(this.target, args);
                } catch (InvocationTargetException ex) {
                    throw ex.getTargetException();
                }
            }
        }
    }
}
