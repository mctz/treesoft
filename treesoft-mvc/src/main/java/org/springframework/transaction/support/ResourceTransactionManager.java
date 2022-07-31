package org.springframework.transaction.support;

import org.springframework.transaction.PlatformTransactionManager;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/support/ResourceTransactionManager.class */
public interface ResourceTransactionManager extends PlatformTransactionManager {
    Object getResourceFactory();
}
