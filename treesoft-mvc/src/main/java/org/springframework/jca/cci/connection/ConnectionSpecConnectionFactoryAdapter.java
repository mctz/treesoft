package org.springframework.jca.cci.connection;

import javax.resource.ResourceException;
import javax.resource.cci.Connection;
import javax.resource.cci.ConnectionSpec;
import org.springframework.core.NamedThreadLocal;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/jca/cci/connection/ConnectionSpecConnectionFactoryAdapter.class */
public class ConnectionSpecConnectionFactoryAdapter extends DelegatingConnectionFactory {
    private ConnectionSpec connectionSpec;
    private final ThreadLocal<ConnectionSpec> threadBoundSpec = new NamedThreadLocal("Current CCI ConnectionSpec");

    public void setConnectionSpec(ConnectionSpec connectionSpec) {
        this.connectionSpec = connectionSpec;
    }

    public void setConnectionSpecForCurrentThread(ConnectionSpec spec) {
        this.threadBoundSpec.set(spec);
    }

    public void removeConnectionSpecFromCurrentThread() {
        this.threadBoundSpec.remove();
    }

    @Override // org.springframework.jca.cci.connection.DelegatingConnectionFactory
    public final Connection getConnection() throws ResourceException {
        ConnectionSpec threadSpec = this.threadBoundSpec.get();
        if (threadSpec != null) {
            return doGetConnection(threadSpec);
        }
        return doGetConnection(this.connectionSpec);
    }

    protected Connection doGetConnection(ConnectionSpec spec) throws ResourceException {
        if (getTargetConnectionFactory() == null) {
            throw new IllegalStateException("targetConnectionFactory is required");
        }
        if (spec != null) {
            return getTargetConnectionFactory().getConnection(spec);
        }
        return getTargetConnectionFactory().getConnection();
    }
}
