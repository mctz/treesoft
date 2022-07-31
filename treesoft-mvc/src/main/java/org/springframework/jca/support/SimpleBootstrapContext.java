package org.springframework.jca.support;

import java.util.Timer;
import javax.resource.spi.BootstrapContext;
import javax.resource.spi.UnavailableException;
import javax.resource.spi.XATerminator;
import javax.resource.spi.work.WorkManager;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/jca/support/SimpleBootstrapContext.class */
public class SimpleBootstrapContext implements BootstrapContext {
    private WorkManager workManager;
    private XATerminator xaTerminator;

    public SimpleBootstrapContext(WorkManager workManager) {
        this.workManager = workManager;
    }

    public SimpleBootstrapContext(WorkManager workManager, XATerminator xaTerminator) {
        this.workManager = workManager;
        this.xaTerminator = xaTerminator;
    }

    public WorkManager getWorkManager() {
        if (this.workManager == null) {
            throw new IllegalStateException("No WorkManager available");
        }
        return this.workManager;
    }

    public XATerminator getXATerminator() {
        return this.xaTerminator;
    }

    public Timer createTimer() throws UnavailableException {
        return new Timer();
    }
}
