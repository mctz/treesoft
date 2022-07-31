package org.springframework.jca.work;

import javax.resource.spi.work.Work;
import org.springframework.util.Assert;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/jca/work/DelegatingWork.class */
public class DelegatingWork implements Work {
    private final Runnable delegate;

    public DelegatingWork(Runnable delegate) {
        Assert.notNull(delegate, "Delegate must not be null");
        this.delegate = delegate;
    }

    public final Runnable getDelegate() {
        return this.delegate;
    }

    public void run() {
        this.delegate.run();
    }

    public void release() {
    }
}
