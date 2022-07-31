package org.springframework.jca.context;

import javax.resource.NotSupportedException;
import javax.resource.ResourceException;
import javax.resource.spi.ActivationSpec;
import javax.resource.spi.BootstrapContext;
import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.ResourceAdapterInternalException;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.transaction.xa.XAResource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/jca/context/SpringContextResourceAdapter.class */
public class SpringContextResourceAdapter implements ResourceAdapter {
    public static final String CONFIG_LOCATION_DELIMITERS = ",; \t\n";
    public static final String DEFAULT_CONTEXT_CONFIG_LOCATION = "META-INF/applicationContext.xml";
    protected final Log logger = LogFactory.getLog(getClass());
    private String contextConfigLocation = DEFAULT_CONTEXT_CONFIG_LOCATION;
    private ConfigurableApplicationContext applicationContext;

    public void setContextConfigLocation(String contextConfigLocation) {
        this.contextConfigLocation = contextConfigLocation;
    }

    protected String getContextConfigLocation() {
        return this.contextConfigLocation;
    }

    protected ConfigurableEnvironment createEnvironment() {
        return new StandardEnvironment();
    }

    public void start(BootstrapContext bootstrapContext) throws ResourceAdapterInternalException {
        if (this.logger.isInfoEnabled()) {
            this.logger.info("Starting SpringContextResourceAdapter with BootstrapContext: " + bootstrapContext);
        }
        this.applicationContext = createApplicationContext(bootstrapContext);
    }

    protected ConfigurableApplicationContext createApplicationContext(BootstrapContext bootstrapContext) {
        ResourceAdapterApplicationContext applicationContext = new ResourceAdapterApplicationContext(bootstrapContext);
        applicationContext.setClassLoader(getClass().getClassLoader());
        String[] configLocations = StringUtils.tokenizeToStringArray(getContextConfigLocation(), CONFIG_LOCATION_DELIMITERS);
        if (configLocations != null) {
            loadBeanDefinitions(applicationContext, configLocations);
        }
        applicationContext.refresh();
        return applicationContext;
    }

    protected void loadBeanDefinitions(BeanDefinitionRegistry registry, String[] configLocations) {
        new XmlBeanDefinitionReader(registry).loadBeanDefinitions(configLocations);
    }

    public void stop() {
        this.logger.info("Stopping SpringContextResourceAdapter");
        this.applicationContext.close();
    }

    public void endpointActivation(MessageEndpointFactory messageEndpointFactory, ActivationSpec activationSpec) throws ResourceException {
        throw new NotSupportedException("SpringContextResourceAdapter does not support message endpoints");
    }

    public void endpointDeactivation(MessageEndpointFactory messageEndpointFactory, ActivationSpec activationSpec) {
    }

    public XAResource[] getXAResources(ActivationSpec[] activationSpecs) throws ResourceException {
        return null;
    }

    public boolean equals(Object obj) {
        return (obj instanceof SpringContextResourceAdapter) && ObjectUtils.nullSafeEquals(getContextConfigLocation(), ((SpringContextResourceAdapter) obj).getContextConfigLocation());
    }

    public int hashCode() {
        return ObjectUtils.nullSafeHashCode(getContextConfigLocation());
    }
}
