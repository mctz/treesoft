package org.springframework.transaction.annotation;

import org.springframework.transaction.PlatformTransactionManager;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/annotation/TransactionManagementConfigurer.class */
public interface TransactionManagementConfigurer {
    PlatformTransactionManager annotationDrivenTransactionManager();
}
