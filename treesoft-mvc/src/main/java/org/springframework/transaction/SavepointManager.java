package org.springframework.transaction;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/SavepointManager.class */
public interface SavepointManager {
    Object createSavepoint() throws TransactionException;

    void rollbackToSavepoint(Object obj) throws TransactionException;

    void releaseSavepoint(Object obj) throws TransactionException;
}
