package org.springframework.transaction.annotation;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/annotation/Propagation.class */
public enum Propagation {
    REQUIRED(0),
    SUPPORTS(1),
    MANDATORY(2),
    REQUIRES_NEW(3),
    NOT_SUPPORTED(4),
    NEVER(5),
    NESTED(6);
    
    private final int value;

    Propagation(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
