package org.springframework.transaction.config;

import org.springframework.aop.config.AopNamespaceUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.transaction.interceptor.BeanFactoryTransactionAttributeSourceAdvisor;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.w3c.dom.Element;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/config/AnnotationDrivenBeanDefinitionParser.class */
public class AnnotationDrivenBeanDefinitionParser implements BeanDefinitionParser {
    @Deprecated
    public static final String TRANSACTION_ADVISOR_BEAN_NAME = "org.springframework.transaction.config.internalTransactionAdvisor";
    @Deprecated
    public static final String TRANSACTION_ASPECT_BEAN_NAME = "org.springframework.transaction.config.internalTransactionAspect";

    public BeanDefinition parse(Element element, ParserContext parserContext) {
        String mode = element.getAttribute("mode");
        if ("aspectj".equals(mode)) {
            registerTransactionAspect(element, parserContext);
            return null;
        }
        AopAutoProxyConfigurer.configureAutoProxyCreator(element, parserContext);
        return null;
    }

    private void registerTransactionAspect(Element element, ParserContext parserContext) {
        if (!parserContext.getRegistry().containsBeanDefinition("org.springframework.transaction.config.internalTransactionAspect")) {
            RootBeanDefinition def = new RootBeanDefinition();
            def.setBeanClassName(TransactionManagementConfigUtils.TRANSACTION_ASPECT_CLASS_NAME);
            def.setFactoryMethodName("aspectOf");
            registerTransactionManager(element, def);
            parserContext.registerBeanComponent(new BeanComponentDefinition(def, "org.springframework.transaction.config.internalTransactionAspect"));
        }
    }

    public static void registerTransactionManager(Element element, BeanDefinition def) {
        def.getPropertyValues().add("transactionManagerBeanName", TxNamespaceHandler.getTransactionManagerName(element));
    }

    /* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/config/AnnotationDrivenBeanDefinitionParser$AopAutoProxyConfigurer.class */
    private static class AopAutoProxyConfigurer {
        private AopAutoProxyConfigurer() {
        }

        public static void configureAutoProxyCreator(Element element, ParserContext parserContext) {
            AopNamespaceUtils.registerAutoProxyCreatorIfNecessary(parserContext, element);
            if (!parserContext.getRegistry().containsBeanDefinition("org.springframework.transaction.config.internalTransactionAdvisor")) {
                Object eleSource = parserContext.extractSource(element);
                RootBeanDefinition sourceDef = new RootBeanDefinition("org.springframework.transaction.annotation.AnnotationTransactionAttributeSource");
                sourceDef.setSource(eleSource);
                sourceDef.setRole(2);
                String sourceName = parserContext.getReaderContext().registerWithGeneratedName(sourceDef);
                RootBeanDefinition interceptorDef = new RootBeanDefinition(TransactionInterceptor.class);
                interceptorDef.setSource(eleSource);
                interceptorDef.setRole(2);
                AnnotationDrivenBeanDefinitionParser.registerTransactionManager(element, interceptorDef);
                interceptorDef.getPropertyValues().add("transactionAttributeSource", new RuntimeBeanReference(sourceName));
                String interceptorName = parserContext.getReaderContext().registerWithGeneratedName(interceptorDef);
                RootBeanDefinition advisorDef = new RootBeanDefinition(BeanFactoryTransactionAttributeSourceAdvisor.class);
                advisorDef.setSource(eleSource);
                advisorDef.setRole(2);
                advisorDef.getPropertyValues().add("transactionAttributeSource", new RuntimeBeanReference(sourceName));
                advisorDef.getPropertyValues().add("adviceBeanName", interceptorName);
                if (element.hasAttribute("order")) {
                    advisorDef.getPropertyValues().add("order", element.getAttribute("order"));
                }
                parserContext.getRegistry().registerBeanDefinition("org.springframework.transaction.config.internalTransactionAdvisor", advisorDef);
                CompositeComponentDefinition compositeDef = new CompositeComponentDefinition(element.getTagName(), eleSource);
                compositeDef.addNestedComponent(new BeanComponentDefinition(sourceDef, sourceName));
                compositeDef.addNestedComponent(new BeanComponentDefinition(interceptorDef, interceptorName));
                compositeDef.addNestedComponent(new BeanComponentDefinition(advisorDef, "org.springframework.transaction.config.internalTransactionAdvisor"));
                parserContext.registerComponent(compositeDef);
            }
        }
    }
}
