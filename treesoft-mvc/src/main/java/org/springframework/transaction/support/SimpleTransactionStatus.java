package org.springframework.transaction.support;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/support/SimpleTransactionStatus.class */
public class SimpleTransactionStatus extends AbstractTransactionStatus {
    private final boolean newTransaction;

    public SimpleTransactionStatus() {
        this(true);
    }

    public SimpleTransactionStatus(boolean newTransaction) {
        this.newTransaction = newTransaction;
    }

    @Override // org.springframework.transaction.TransactionStatus
    public boolean isNewTransaction() {
        return this.newTransaction;
    }
}
