package org.springframework.transaction.interceptor;

import java.beans.PropertyEditorSupport;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.StringUtils;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/interceptor/TransactionAttributeEditor.class */
public class TransactionAttributeEditor extends PropertyEditorSupport {
    public void setAsText(String text) throws IllegalArgumentException {
        if (StringUtils.hasLength(text)) {
            String[] tokens = StringUtils.commaDelimitedListToStringArray(text);
            RuleBasedTransactionAttribute attr = new RuleBasedTransactionAttribute();
            for (String str : tokens) {
                String token = StringUtils.trimWhitespace(str.trim());
                if (StringUtils.containsWhitespace(token)) {
                    throw new IllegalArgumentException("Transaction attribute token contains illegal whitespace: [" + token + "]");
                }
                if (token.startsWith(DefaultTransactionDefinition.PREFIX_PROPAGATION)) {
                    attr.setPropagationBehaviorName(token);
                } else if (token.startsWith(DefaultTransactionDefinition.PREFIX_ISOLATION)) {
                    attr.setIsolationLevelName(token);
                } else if (token.startsWith(DefaultTransactionDefinition.PREFIX_TIMEOUT)) {
                    String value = token.substring(DefaultTransactionDefinition.PREFIX_TIMEOUT.length());
                    attr.setTimeout(Integer.parseInt(value));
                } else if (token.equals(DefaultTransactionDefinition.READ_ONLY_MARKER)) {
                    attr.setReadOnly(true);
                } else if (token.startsWith(RuleBasedTransactionAttribute.PREFIX_COMMIT_RULE)) {
                    attr.getRollbackRules().add(new NoRollbackRuleAttribute(token.substring(1)));
                } else if (token.startsWith(RuleBasedTransactionAttribute.PREFIX_ROLLBACK_RULE)) {
                    attr.getRollbackRules().add(new RollbackRuleAttribute(token.substring(1)));
                } else {
                    throw new IllegalArgumentException("Invalid transaction attribute token: [" + token + "]");
                }
            }
            setValue(attr);
            return;
        }
        setValue(null);
    }
}
