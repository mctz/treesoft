package org.springframework.transaction.config;

import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.NoRollbackRuleAttribute;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/config/TxAdviceBeanDefinitionParser.class */
class TxAdviceBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
    private static final String METHOD_ELEMENT = "method";
    private static final String METHOD_NAME_ATTRIBUTE = "name";
    private static final String ATTRIBUTES_ELEMENT = "attributes";
    private static final String TIMEOUT_ATTRIBUTE = "timeout";
    private static final String READ_ONLY_ATTRIBUTE = "read-only";
    private static final String PROPAGATION_ATTRIBUTE = "propagation";
    private static final String ISOLATION_ATTRIBUTE = "isolation";
    private static final String ROLLBACK_FOR_ATTRIBUTE = "rollback-for";
    private static final String NO_ROLLBACK_FOR_ATTRIBUTE = "no-rollback-for";

    protected Class<?> getBeanClass(Element element) {
        return TransactionInterceptor.class;
    }

    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        builder.addPropertyReference("transactionManager", TxNamespaceHandler.getTransactionManagerName(element));
        List<Element> txAttributes = DomUtils.getChildElementsByTagName(element, ATTRIBUTES_ELEMENT);
        if (txAttributes.size() > 1) {
            parserContext.getReaderContext().error("Element <attributes> is allowed at most once inside element <advice>", element);
        } else if (txAttributes.size() == 1) {
            Element attributeSourceElement = txAttributes.get(0);
            RootBeanDefinition attributeSourceDefinition = parseAttributeSource(attributeSourceElement, parserContext);
            builder.addPropertyValue("transactionAttributeSource", attributeSourceDefinition);
        } else {
            builder.addPropertyValue("transactionAttributeSource", new RootBeanDefinition("org.springframework.transaction.annotation.AnnotationTransactionAttributeSource"));
        }
    }

    private RootBeanDefinition parseAttributeSource(Element attrEle, ParserContext parserContext) {
        List<Element> methods = DomUtils.getChildElementsByTagName(attrEle, METHOD_ELEMENT);
        ManagedMap<TypedStringValue, RuleBasedTransactionAttribute> transactionAttributeMap = new ManagedMap<>(methods.size());
        transactionAttributeMap.setSource(parserContext.extractSource(attrEle));
        for (Element methodEle : methods) {
            String name = methodEle.getAttribute(METHOD_NAME_ATTRIBUTE);
            TypedStringValue nameHolder = new TypedStringValue(name);
            nameHolder.setSource(parserContext.extractSource(methodEle));
            RuleBasedTransactionAttribute attribute = new RuleBasedTransactionAttribute();
            String propagation = methodEle.getAttribute(PROPAGATION_ATTRIBUTE);
            String isolation = methodEle.getAttribute(ISOLATION_ATTRIBUTE);
            String timeout = methodEle.getAttribute(TIMEOUT_ATTRIBUTE);
            String readOnly = methodEle.getAttribute(READ_ONLY_ATTRIBUTE);
            if (StringUtils.hasText(propagation)) {
                attribute.setPropagationBehaviorName(DefaultTransactionDefinition.PREFIX_PROPAGATION + propagation);
            }
            if (StringUtils.hasText(isolation)) {
                attribute.setIsolationLevelName(DefaultTransactionDefinition.PREFIX_ISOLATION + isolation);
            }
            if (StringUtils.hasText(timeout)) {
                try {
                    attribute.setTimeout(Integer.parseInt(timeout));
                } catch (NumberFormatException e) {
                    parserContext.getReaderContext().error("Timeout must be an integer value: [" + timeout + "]", methodEle);
                }
            }
            if (StringUtils.hasText(readOnly)) {
                attribute.setReadOnly(Boolean.valueOf(methodEle.getAttribute(READ_ONLY_ATTRIBUTE)).booleanValue());
            }
            List<RollbackRuleAttribute> rollbackRules = new LinkedList<>();
            if (methodEle.hasAttribute(ROLLBACK_FOR_ATTRIBUTE)) {
                String rollbackForValue = methodEle.getAttribute(ROLLBACK_FOR_ATTRIBUTE);
                addRollbackRuleAttributesTo(rollbackRules, rollbackForValue);
            }
            if (methodEle.hasAttribute(NO_ROLLBACK_FOR_ATTRIBUTE)) {
                String noRollbackForValue = methodEle.getAttribute(NO_ROLLBACK_FOR_ATTRIBUTE);
                addNoRollbackRuleAttributesTo(rollbackRules, noRollbackForValue);
            }
            attribute.setRollbackRules(rollbackRules);
            transactionAttributeMap.put(nameHolder, attribute);
        }
        RootBeanDefinition attributeSourceDefinition = new RootBeanDefinition(NameMatchTransactionAttributeSource.class);
        attributeSourceDefinition.setSource(parserContext.extractSource(attrEle));
        attributeSourceDefinition.getPropertyValues().add("nameMap", transactionAttributeMap);
        return attributeSourceDefinition;
    }

    private void addRollbackRuleAttributesTo(List<RollbackRuleAttribute> rollbackRules, String rollbackForValue) {
        String[] exceptionTypeNames = StringUtils.commaDelimitedListToStringArray(rollbackForValue);
        for (String typeName : exceptionTypeNames) {
            rollbackRules.add(new RollbackRuleAttribute(StringUtils.trimWhitespace(typeName)));
        }
    }

    private void addNoRollbackRuleAttributesTo(List<RollbackRuleAttribute> rollbackRules, String noRollbackForValue) {
        String[] exceptionTypeNames = StringUtils.commaDelimitedListToStringArray(noRollbackForValue);
        for (String typeName : exceptionTypeNames) {
            rollbackRules.add(new NoRollbackRuleAttribute(StringUtils.trimWhitespace(typeName)));
        }
    }
}
