package org.springframework.transaction.support;

import java.io.Flushable;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/support/SmartTransactionObject.class */
public interface SmartTransactionObject extends Flushable {
    boolean isRollbackOnly();

    @Override // java.io.Flushable
    void flush();
}
