package org.springframework.transaction.interceptor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/interceptor/RuleBasedTransactionAttribute.class */
public class RuleBasedTransactionAttribute extends DefaultTransactionAttribute implements Serializable {
    public static final String PREFIX_ROLLBACK_RULE = "-";
    public static final String PREFIX_COMMIT_RULE = "+";
    private static final Log logger = LogFactory.getLog(RuleBasedTransactionAttribute.class);
    private List<RollbackRuleAttribute> rollbackRules;

    public RuleBasedTransactionAttribute() {
    }

    public RuleBasedTransactionAttribute(RuleBasedTransactionAttribute other) {
        super(other);
        this.rollbackRules = new ArrayList(other.rollbackRules);
    }

    public RuleBasedTransactionAttribute(int propagationBehavior, List<RollbackRuleAttribute> rollbackRules) {
        super(propagationBehavior);
        this.rollbackRules = rollbackRules;
    }

    public void setRollbackRules(List<RollbackRuleAttribute> rollbackRules) {
        this.rollbackRules = rollbackRules;
    }

    public List<RollbackRuleAttribute> getRollbackRules() {
        if (this.rollbackRules == null) {
            this.rollbackRules = new LinkedList();
        }
        return this.rollbackRules;
    }

    @Override // org.springframework.transaction.interceptor.DefaultTransactionAttribute, org.springframework.transaction.interceptor.TransactionAttribute
    public boolean rollbackOn(Throwable ex) {
        if (logger.isTraceEnabled()) {
            logger.trace("Applying rules to determine whether transaction should rollback on " + ex);
        }
        RollbackRuleAttribute winner = null;
        int deepest = Integer.MAX_VALUE;
        if (this.rollbackRules != null) {
            for (RollbackRuleAttribute rule : this.rollbackRules) {
                int depth = rule.getDepth(ex);
                if (depth >= 0 && depth < deepest) {
                    deepest = depth;
                    winner = rule;
                }
            }
        }
        if (logger.isTraceEnabled()) {
            logger.trace("Winning rollback rule is: " + winner);
        }
        if (winner != null) {
            return !(winner instanceof NoRollbackRuleAttribute);
        }
        logger.trace("No relevant rollback rule found: applying default rules");
        return super.rollbackOn(ex);
    }

    @Override // org.springframework.transaction.support.DefaultTransactionDefinition
    public String toString() {
        StringBuilder result = getAttributeDescription();
        if (this.rollbackRules != null) {
            for (RollbackRuleAttribute rule : this.rollbackRules) {
                String sign = rule instanceof NoRollbackRuleAttribute ? PREFIX_COMMIT_RULE : PREFIX_ROLLBACK_RULE;
                result.append(',').append(sign).append(rule.getExceptionName());
            }
        }
        return result.toString();
    }
}
