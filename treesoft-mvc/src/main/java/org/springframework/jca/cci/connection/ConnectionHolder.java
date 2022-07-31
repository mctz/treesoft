package org.springframework.jca.cci.connection;

import javax.resource.cci.Connection;
import org.springframework.transaction.support.ResourceHolderSupport;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/jca/cci/connection/ConnectionHolder.class */
public class ConnectionHolder extends ResourceHolderSupport {
    private final Connection connection;

    public ConnectionHolder(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return this.connection;
    }
}
