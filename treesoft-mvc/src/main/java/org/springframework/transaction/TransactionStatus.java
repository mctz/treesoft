package org.springframework.transaction;

import java.io.Flushable;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/TransactionStatus.class */
public interface TransactionStatus extends SavepointManager, Flushable {
    boolean isNewTransaction();

    boolean hasSavepoint();

    void setRollbackOnly();

    boolean isRollbackOnly();

    @Override // java.io.Flushable
    void flush();

    boolean isCompleted();
}
