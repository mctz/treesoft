package org.springframework.transaction.support;

import java.io.Serializable;
import org.springframework.core.Constants;
import org.springframework.transaction.TransactionDefinition;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/support/DefaultTransactionDefinition.class */
public class DefaultTransactionDefinition implements TransactionDefinition, Serializable {
    public static final String PREFIX_PROPAGATION = "PROPAGATION_";
    public static final String PREFIX_ISOLATION = "ISOLATION_";
    public static final String PREFIX_TIMEOUT = "timeout_";
    public static final String READ_ONLY_MARKER = "readOnly";
    static final Constants constants = new Constants(TransactionDefinition.class);
    private int propagationBehavior;
    private int isolationLevel;
    private int timeout;
    private boolean readOnly;
    private String name;

    public DefaultTransactionDefinition() {
        this.propagationBehavior = 0;
        this.isolationLevel = -1;
        this.timeout = -1;
        this.readOnly = false;
    }

    public DefaultTransactionDefinition(TransactionDefinition other) {
        this.propagationBehavior = 0;
        this.isolationLevel = -1;
        this.timeout = -1;
        this.readOnly = false;
        this.propagationBehavior = other.getPropagationBehavior();
        this.isolationLevel = other.getIsolationLevel();
        this.timeout = other.getTimeout();
        this.readOnly = other.isReadOnly();
        this.name = other.getName();
    }

    public DefaultTransactionDefinition(int propagationBehavior) {
        this.propagationBehavior = 0;
        this.isolationLevel = -1;
        this.timeout = -1;
        this.readOnly = false;
        this.propagationBehavior = propagationBehavior;
    }

    public final void setPropagationBehaviorName(String constantName) throws IllegalArgumentException {
        if (constantName == null || !constantName.startsWith(PREFIX_PROPAGATION)) {
            throw new IllegalArgumentException("Only propagation constants allowed");
        }
        setPropagationBehavior(constants.asNumber(constantName).intValue());
    }

    public final void setPropagationBehavior(int propagationBehavior) {
        if (!constants.getValues(PREFIX_PROPAGATION).contains(Integer.valueOf(propagationBehavior))) {
            throw new IllegalArgumentException("Only values of propagation constants allowed");
        }
        this.propagationBehavior = propagationBehavior;
    }

    @Override // org.springframework.transaction.TransactionDefinition
    public final int getPropagationBehavior() {
        return this.propagationBehavior;
    }

    public final void setIsolationLevelName(String constantName) throws IllegalArgumentException {
        if (constantName == null || !constantName.startsWith(PREFIX_ISOLATION)) {
            throw new IllegalArgumentException("Only isolation constants allowed");
        }
        setIsolationLevel(constants.asNumber(constantName).intValue());
    }

    public final void setIsolationLevel(int isolationLevel) {
        if (!constants.getValues(PREFIX_ISOLATION).contains(Integer.valueOf(isolationLevel))) {
            throw new IllegalArgumentException("Only values of isolation constants allowed");
        }
        this.isolationLevel = isolationLevel;
    }

    @Override // org.springframework.transaction.TransactionDefinition
    public final int getIsolationLevel() {
        return this.isolationLevel;
    }

    public final void setTimeout(int timeout) {
        if (timeout < -1) {
            throw new IllegalArgumentException("Timeout must be a positive integer or TIMEOUT_DEFAULT");
        }
        this.timeout = timeout;
    }

    @Override // org.springframework.transaction.TransactionDefinition
    public final int getTimeout() {
        return this.timeout;
    }

    public final void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    @Override // org.springframework.transaction.TransactionDefinition
    public final boolean isReadOnly() {
        return this.readOnly;
    }

    public final void setName(String name) {
        this.name = name;
    }

    @Override // org.springframework.transaction.TransactionDefinition
    public final String getName() {
        return this.name;
    }

    public boolean equals(Object other) {
        return (other instanceof TransactionDefinition) && toString().equals(other.toString());
    }

    public int hashCode() {
        return toString().hashCode();
    }

    public String toString() {
        return getDefinitionDescription().toString();
    }

    public final StringBuilder getDefinitionDescription() {
        StringBuilder result = new StringBuilder();
        result.append(constants.toCode(Integer.valueOf(this.propagationBehavior), PREFIX_PROPAGATION));
        result.append(',');
        result.append(constants.toCode(Integer.valueOf(this.isolationLevel), PREFIX_ISOLATION));
        if (this.timeout != -1) {
            result.append(',');
            result.append(PREFIX_TIMEOUT).append(this.timeout);
        }
        if (this.readOnly) {
            result.append(',');
            result.append(READ_ONLY_MARKER);
        }
        return result;
    }
}
