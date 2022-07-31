package org.springframework.jca.cci.connection;

import javax.resource.ResourceException;
import javax.resource.cci.Connection;
import javax.resource.cci.ConnectionFactory;
import javax.resource.cci.ConnectionSpec;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jca.cci.CannotGetCciConnectionException;
import org.springframework.transaction.support.ResourceHolderSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/jca/cci/connection/ConnectionFactoryUtils.class */
public abstract class ConnectionFactoryUtils {
    private static final Log logger = LogFactory.getLog(ConnectionFactoryUtils.class);

    public static Connection getConnection(ConnectionFactory cf) throws CannotGetCciConnectionException {
        return getConnection(cf, null);
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Throwable, org.springframework.jca.cci.CannotGetCciConnectionException] */
    public static Connection getConnection(ConnectionFactory cf, ConnectionSpec spec) throws CannotGetCciConnectionException {
        try {
            if (spec != null) {
                Assert.notNull(cf, "No ConnectionFactory specified");
                return cf.getConnection(spec);
            }
            return doGetConnection(cf);
        } catch (ResourceException ex) {
            throw new CannotGetCciConnectionException("Could not get CCI Connection", ex);
        }
    }

    public static Connection doGetConnection(ConnectionFactory cf) throws ResourceException {
        Assert.notNull(cf, "No ConnectionFactory specified");
        ConnectionHolder conHolder = (ConnectionHolder) TransactionSynchronizationManager.getResource(cf);
        if (conHolder != null) {
            return conHolder.getConnection();
        }
        logger.debug("Opening CCI Connection");
        Connection con = cf.getConnection();
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            logger.debug("Registering transaction synchronization for CCI Connection");
            ConnectionHolder conHolder2 = new ConnectionHolder(con);
            conHolder2.setSynchronizedWithTransaction(true);
            TransactionSynchronizationManager.registerSynchronization(new ConnectionSynchronization(conHolder2, cf));
            TransactionSynchronizationManager.bindResource(cf, conHolder2);
        }
        return con;
    }

    public static boolean isConnectionTransactional(Connection con, ConnectionFactory cf) {
        ConnectionHolder conHolder;
        return (cf == null || (conHolder = (ConnectionHolder) TransactionSynchronizationManager.getResource(cf)) == null || conHolder.getConnection() != con) ? false : true;
    }

    public static void releaseConnection(Connection con, ConnectionFactory cf) {
        try {
            doReleaseConnection(con, cf);
        } catch (ResourceException ex) {
            logger.debug("Could not close CCI Connection", ex);
        } catch (Throwable ex2) {
            logger.debug("Unexpected exception on closing CCI Connection", ex2);
        }
    }

    public static void doReleaseConnection(Connection con, ConnectionFactory cf) throws ResourceException {
        if (con == null || isConnectionTransactional(con, cf)) {
            return;
        }
        con.close();
    }

    /* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/jca/cci/connection/ConnectionFactoryUtils$ConnectionSynchronization.class */
    public static class ConnectionSynchronization extends ResourceHolderSynchronization<ConnectionHolder, ConnectionFactory> {
        public ConnectionSynchronization(ConnectionHolder connectionHolder, ConnectionFactory connectionFactory) {
            super(connectionHolder, connectionFactory);
        }

        public void releaseResource(ConnectionHolder resourceHolder, ConnectionFactory resourceKey) {
            ConnectionFactoryUtils.releaseConnection(resourceHolder.getConnection(), resourceKey);
        }
    }
}
