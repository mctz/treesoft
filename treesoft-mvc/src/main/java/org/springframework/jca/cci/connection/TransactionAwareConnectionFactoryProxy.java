package org.springframework.jca.cci.connection;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import javax.resource.ResourceException;
import javax.resource.cci.Connection;
import javax.resource.cci.ConnectionFactory;
import javax.resource.spi.IllegalStateException;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/jca/cci/connection/TransactionAwareConnectionFactoryProxy.class */
public class TransactionAwareConnectionFactoryProxy extends DelegatingConnectionFactory {
    public TransactionAwareConnectionFactoryProxy() {
    }

    public TransactionAwareConnectionFactoryProxy(ConnectionFactory targetConnectionFactory) {
        setTargetConnectionFactory(targetConnectionFactory);
        afterPropertiesSet();
    }

    @Override // org.springframework.jca.cci.connection.DelegatingConnectionFactory
    public Connection getConnection() throws ResourceException {
        Connection con = ConnectionFactoryUtils.doGetConnection(getTargetConnectionFactory());
        return getTransactionAwareConnectionProxy(con, getTargetConnectionFactory());
    }

    protected Connection getTransactionAwareConnectionProxy(Connection target, ConnectionFactory cf) {
        return (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(), new Class[]{Connection.class}, new TransactionAwareInvocationHandler(target, cf));
    }

    /* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/jca/cci/connection/TransactionAwareConnectionFactoryProxy$TransactionAwareInvocationHandler.class */
    public static class TransactionAwareInvocationHandler implements InvocationHandler {
        private final Connection target;
        private final ConnectionFactory connectionFactory;

        public TransactionAwareInvocationHandler(Connection target, ConnectionFactory cf) {
            this.target = target;
            this.connectionFactory = cf;
        }

        @Override // java.lang.reflect.InvocationHandler
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getName().equals("equals")) {
                return Boolean.valueOf(proxy == args[0]);
            } else if (method.getName().equals("hashCode")) {
                return Integer.valueOf(System.identityHashCode(proxy));
            } else {
                if (method.getName().equals("getLocalTransaction")) {
                    if (ConnectionFactoryUtils.isConnectionTransactional(this.target, this.connectionFactory)) {
                        throw new IllegalStateException("Local transaction handling not allowed within a managed transaction");
                    }
                } else if (method.getName().equals("close")) {
                    ConnectionFactoryUtils.doReleaseConnection(this.target, this.connectionFactory);
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
