package org.springframework.jca.context;

import javax.resource.spi.BootstrapContext;
import javax.resource.spi.work.WorkManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.util.Assert;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/jca/context/ResourceAdapterApplicationContext.class */
public class ResourceAdapterApplicationContext extends GenericApplicationContext {
    private final BootstrapContext bootstrapContext;

    public ResourceAdapterApplicationContext(BootstrapContext bootstrapContext) {
        Assert.notNull(bootstrapContext, "BootstrapContext must not be null");
        this.bootstrapContext = bootstrapContext;
    }

    protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        beanFactory.addBeanPostProcessor(new BootstrapContextAwareProcessor(this.bootstrapContext));
        beanFactory.ignoreDependencyInterface(BootstrapContextAware.class);
        beanFactory.registerResolvableDependency(BootstrapContext.class, this.bootstrapContext);
        beanFactory.registerResolvableDependency(WorkManager.class, new ObjectFactory<WorkManager>() { // from class: org.springframework.jca.context.ResourceAdapterApplicationContext.1
            public WorkManager getObject() {
                return ResourceAdapterApplicationContext.this.bootstrapContext.getWorkManager();
            }
        });
    }
}
