package org.springframework.transaction.config;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/config/JtaTransactionManagerBeanDefinitionParser.class */
public class JtaTransactionManagerBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
    protected String getBeanClassName(Element element) {
        return JtaTransactionManagerFactoryBean.resolveJtaTransactionManagerClassName();
    }

    protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext) {
        return "transactionManager";
    }
}
