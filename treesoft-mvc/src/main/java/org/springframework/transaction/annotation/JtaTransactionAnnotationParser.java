package org.springframework.transaction.annotation;

import java.io.Serializable;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import javax.transaction.Transactional;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.transaction.interceptor.NoRollbackRuleAttribute;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/annotation/JtaTransactionAnnotationParser.class */
public class JtaTransactionAnnotationParser implements TransactionAnnotationParser, Serializable {
    @Override // org.springframework.transaction.annotation.TransactionAnnotationParser
    public TransactionAttribute parseTransactionAnnotation(AnnotatedElement ae) {
        AnnotationAttributes ann = AnnotatedElementUtils.getAnnotationAttributes(ae, Transactional.class.getName());
        if (ann != null) {
            return parseTransactionAnnotation(ann);
        }
        return null;
    }

    public TransactionAttribute parseTransactionAnnotation(Transactional ann) {
        return parseTransactionAnnotation(AnnotationUtils.getAnnotationAttributes(ann, false, false));
    }

    protected TransactionAttribute parseTransactionAnnotation(AnnotationAttributes attributes) {
        RuleBasedTransactionAttribute rbta = new RuleBasedTransactionAttribute();
        rbta.setPropagationBehaviorName(DefaultTransactionDefinition.PREFIX_PROPAGATION + attributes.getEnum("value").toString());
        ArrayList<RollbackRuleAttribute> rollBackRules = new ArrayList<>();
        Class<?>[] rbf = attributes.getClassArray("rollbackOn");
        for (Class<?> rbRule : rbf) {
            RollbackRuleAttribute rule = new RollbackRuleAttribute(rbRule);
            rollBackRules.add(rule);
        }
        Class<?>[] nrbf = attributes.getClassArray("dontRollbackOn");
        for (Class<?> rbRule2 : nrbf) {
            NoRollbackRuleAttribute rule2 = new NoRollbackRuleAttribute(rbRule2);
            rollBackRules.add(rule2);
        }
        rbta.getRollbackRules().addAll(rollBackRules);
        return rbta;
    }

    public boolean equals(Object other) {
        return this == other || (other instanceof JtaTransactionAnnotationParser);
    }

    public int hashCode() {
        return JtaTransactionAnnotationParser.class.hashCode();
    }
}
