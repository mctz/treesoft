package org.springframework.jca.context;

import javax.resource.spi.BootstrapContext;
import org.springframework.beans.factory.Aware;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/jca/context/BootstrapContextAware.class */
public interface BootstrapContextAware extends Aware {
    void setBootstrapContext(BootstrapContext bootstrapContext);
}
