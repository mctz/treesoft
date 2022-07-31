package org.springframework.transaction.jta;

import java.util.List;
import javax.transaction.Synchronization;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationUtils;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/jta/JtaAfterCompletionSynchronization.class */
public class JtaAfterCompletionSynchronization implements Synchronization {
    private final List<TransactionSynchronization> synchronizations;

    public JtaAfterCompletionSynchronization(List<TransactionSynchronization> synchronizations) {
        this.synchronizations = synchronizations;
    }

    public void beforeCompletion() {
    }

    public void afterCompletion(int status) {
        switch (status) {
            case 3:
                try {
                    TransactionSynchronizationUtils.invokeAfterCommit(this.synchronizations);
                    return;
                } finally {
                    TransactionSynchronizationUtils.invokeAfterCompletion(this.synchronizations, 0);
                }
            case 4:
                TransactionSynchronizationUtils.invokeAfterCompletion(this.synchronizations, 1);
                return;
            default:
                TransactionSynchronizationUtils.invokeAfterCompletion(this.synchronizations, 2);
                return;
        }
    }
}
