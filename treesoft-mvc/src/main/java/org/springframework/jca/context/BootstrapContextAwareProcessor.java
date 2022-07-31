package org.springframework.jca.context;

import javax.resource.spi.BootstrapContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/jca/context/BootstrapContextAwareProcessor.class */
class BootstrapContextAwareProcessor implements BeanPostProcessor {
    private final BootstrapContext bootstrapContext;

    public BootstrapContextAwareProcessor(BootstrapContext bootstrapContext) {
        this.bootstrapContext = bootstrapContext;
    }

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (this.bootstrapContext != null && (bean instanceof BootstrapContextAware)) {
            ((BootstrapContextAware) bean).setBootstrapContext(this.bootstrapContext);
        }
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
