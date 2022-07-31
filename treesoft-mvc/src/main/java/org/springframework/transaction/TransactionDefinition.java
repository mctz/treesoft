package org.springframework.transaction;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/TransactionDefinition.class */
public interface TransactionDefinition {
    public static final int PROPAGATION_REQUIRED = 0;
    public static final int PROPAGATION_SUPPORTS = 1;
    public static final int PROPAGATION_MANDATORY = 2;
    public static final int PROPAGATION_REQUIRES_NEW = 3;
    public static final int PROPAGATION_NOT_SUPPORTED = 4;
    public static final int PROPAGATION_NEVER = 5;
    public static final int PROPAGATION_NESTED = 6;
    public static final int ISOLATION_DEFAULT = -1;
    public static final int ISOLATION_READ_UNCOMMITTED = 1;
    public static final int ISOLATION_READ_COMMITTED = 2;
    public static final int ISOLATION_REPEATABLE_READ = 4;
    public static final int ISOLATION_SERIALIZABLE = 8;
    public static final int TIMEOUT_DEFAULT = -1;

    int getPropagationBehavior();

    int getIsolationLevel();

    int getTimeout();

    boolean isReadOnly();

    String getName();
}
