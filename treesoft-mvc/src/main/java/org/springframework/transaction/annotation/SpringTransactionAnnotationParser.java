package org.springframework.transaction.annotation;

import java.io.Serializable;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.transaction.interceptor.NoRollbackRuleAttribute;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/annotation/SpringTransactionAnnotationParser.class */
public class SpringTransactionAnnotationParser implements TransactionAnnotationParser, Serializable {
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
        Propagation propagation = (Propagation) attributes.getEnum("propagation");
        rbta.setPropagationBehavior(propagation.value());
        Isolation isolation = (Isolation) attributes.getEnum("isolation");
        rbta.setIsolationLevel(isolation.value());
        rbta.setTimeout(attributes.getNumber("timeout").intValue());
        rbta.setReadOnly(attributes.getBoolean(DefaultTransactionDefinition.READ_ONLY_MARKER));
        rbta.setQualifier(attributes.getString("value"));
        ArrayList<RollbackRuleAttribute> rollBackRules = new ArrayList<>();
        Class<?>[] rbf = attributes.getClassArray("rollbackFor");
        for (Class<?> rbRule : rbf) {
            RollbackRuleAttribute rule = new RollbackRuleAttribute(rbRule);
            rollBackRules.add(rule);
        }
        String[] rbfc = attributes.getStringArray("rollbackForClassName");
        for (String rbRule2 : rbfc) {
            RollbackRuleAttribute rule2 = new RollbackRuleAttribute(rbRule2);
            rollBackRules.add(rule2);
        }
        Class<?>[] nrbf = attributes.getClassArray("noRollbackFor");
        for (Class<?> rbRule3 : nrbf) {
            NoRollbackRuleAttribute rule3 = new NoRollbackRuleAttribute(rbRule3);
            rollBackRules.add(rule3);
        }
        String[] nrbfc = attributes.getStringArray("noRollbackForClassName");
        for (String rbRule4 : nrbfc) {
            NoRollbackRuleAttribute rule4 = new NoRollbackRuleAttribute(rbRule4);
            rollBackRules.add(rule4);
        }
        rbta.getRollbackRules().addAll(rollBackRules);
        return rbta;
    }

    public boolean equals(Object other) {
        return this == other || (other instanceof SpringTransactionAnnotationParser);
    }

    public int hashCode() {
        return SpringTransactionAnnotationParser.class.hashCode();
    }
}
