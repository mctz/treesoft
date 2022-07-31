package org.springframework.transaction.annotation;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/annotation/Isolation.class */
public enum Isolation {
    DEFAULT(-1),
    READ_UNCOMMITTED(1),
    READ_COMMITTED(2),
    REPEATABLE_READ(4),
    SERIALIZABLE(8);
    
    private final int value;

    Isolation(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
