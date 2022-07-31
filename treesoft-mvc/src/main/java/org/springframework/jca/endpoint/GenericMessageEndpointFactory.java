package org.springframework.jca.endpoint;

import javax.resource.ResourceException;
import javax.resource.spi.UnavailableException;
import javax.resource.spi.endpoint.MessageEndpoint;
import javax.transaction.xa.XAResource;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DelegatingIntroductionInterceptor;
import org.springframework.jca.endpoint.AbstractMessageEndpointFactory;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/jca/endpoint/GenericMessageEndpointFactory.class */
public class GenericMessageEndpointFactory extends AbstractMessageEndpointFactory {
    private Object messageListener;

    public void setMessageListener(Object messageListener) {
        this.messageListener = messageListener;
    }

    @Override // org.springframework.jca.endpoint.AbstractMessageEndpointFactory
    public MessageEndpoint createEndpoint(XAResource xaResource) throws UnavailableException {
        GenericMessageEndpoint endpoint = super.createEndpoint(xaResource);
        ProxyFactory proxyFactory = new ProxyFactory(this.messageListener);
        DelegatingIntroductionInterceptor introduction = new DelegatingIntroductionInterceptor(endpoint);
        introduction.suppressInterface(MethodInterceptor.class);
        proxyFactory.addAdvice(introduction);
        return (MessageEndpoint) proxyFactory.getProxy();
    }

    @Override // org.springframework.jca.endpoint.AbstractMessageEndpointFactory
    protected AbstractMessageEndpoint createEndpointInternal() throws UnavailableException {
        return new GenericMessageEndpoint();
    }

    /* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/jca/endpoint/GenericMessageEndpointFactory$GenericMessageEndpoint.class */
    private class GenericMessageEndpoint extends AbstractMessageEndpoint implements MethodInterceptor {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        private GenericMessageEndpoint() {
            super();
            GenericMessageEndpointFactory.this = r4;
        }

        public Object invoke(MethodInvocation methodInvocation) throws Throwable {
            boolean declaresException;
            boolean applyDeliveryCalls = !hasBeforeDeliveryBeenCalled();
            if (applyDeliveryCalls) {
                try {
                    beforeDelivery(null);
                } catch (ResourceException ex) {
                    if (ReflectionUtils.declaresException(methodInvocation.getMethod(), ex.getClass())) {
                        throw ex;
                    }
                    throw new InternalResourceException(ex);
                }
            }
            try {
                Object proceed = methodInvocation.proceed();
                if (applyDeliveryCalls) {
                    try {
                        afterDelivery();
                    } catch (ResourceException ex2) {
                        if (ReflectionUtils.declaresException(methodInvocation.getMethod(), ex2.getClass())) {
                            throw ex2;
                        }
                        throw new InternalResourceException(ex2);
                    }
                }
                return proceed;
            } finally {
                try {
                } catch (Throwable th) {
                    if (applyDeliveryCalls) {
                        try {
                        } catch (ResourceException ex3) {
                            if (declaresException) {
                                throw ex3;
                            }
                            throw new InternalResourceException(ex3);
                        }
                    }
                    throw th;
                }
            }
        }

        @Override // org.springframework.jca.endpoint.AbstractMessageEndpointFactory.AbstractMessageEndpoint
        protected ClassLoader getEndpointClassLoader() {
            return GenericMessageEndpointFactory.this.messageListener.getClass().getClassLoader();
        }
    }

    /* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/jca/endpoint/GenericMessageEndpointFactory$InternalResourceException.class */
    public static class InternalResourceException extends RuntimeException {
        protected InternalResourceException(ResourceException cause) {
            super((Throwable) cause);
        }
    }
}
