package org.springframework.jca.support;

import javax.resource.ResourceException;
import javax.resource.spi.BootstrapContext;
import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.XATerminator;
import javax.resource.spi.work.WorkManager;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/jca/support/ResourceAdapterFactoryBean.class */
public class ResourceAdapterFactoryBean implements FactoryBean<ResourceAdapter>, InitializingBean, DisposableBean {
    private ResourceAdapter resourceAdapter;
    private BootstrapContext bootstrapContext;
    private WorkManager workManager;
    private XATerminator xaTerminator;

    public void setResourceAdapterClass(Class<?> resourceAdapterClass) {
        Assert.isAssignable(ResourceAdapter.class, resourceAdapterClass);
        this.resourceAdapter = (ResourceAdapter) BeanUtils.instantiateClass(resourceAdapterClass);
    }

    public void setResourceAdapter(ResourceAdapter resourceAdapter) {
        this.resourceAdapter = resourceAdapter;
    }

    public void setBootstrapContext(BootstrapContext bootstrapContext) {
        this.bootstrapContext = bootstrapContext;
    }

    public void setWorkManager(WorkManager workManager) {
        this.workManager = workManager;
    }

    public void setXaTerminator(XATerminator xaTerminator) {
        this.xaTerminator = xaTerminator;
    }

    public void afterPropertiesSet() throws ResourceException {
        if (this.resourceAdapter == null) {
            throw new IllegalArgumentException("'resourceAdapter' or 'resourceAdapterClass' is required");
        }
        if (this.bootstrapContext == null) {
            this.bootstrapContext = new SimpleBootstrapContext(this.workManager, this.xaTerminator);
        }
        this.resourceAdapter.start(this.bootstrapContext);
    }

    public ResourceAdapter getObject() {
        return this.resourceAdapter;
    }

    public Class<? extends ResourceAdapter> getObjectType() {
        return this.resourceAdapter != null ? this.resourceAdapter.getClass() : ResourceAdapter.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public void destroy() {
        this.resourceAdapter.stop();
    }
}
