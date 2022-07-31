package org.springframework.transaction.support;

import java.io.Flushable;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/support/TransactionSynchronization.class */
public interface TransactionSynchronization extends Flushable {
    public static final int STATUS_COMMITTED = 0;
    public static final int STATUS_ROLLED_BACK = 1;
    public static final int STATUS_UNKNOWN = 2;

    void suspend();

    void resume();

    @Override // java.io.Flushable
    void flush();

    void beforeCommit(boolean z);

    void beforeCompletion();

    void afterCommit();

    void afterCompletion(int i);
}
