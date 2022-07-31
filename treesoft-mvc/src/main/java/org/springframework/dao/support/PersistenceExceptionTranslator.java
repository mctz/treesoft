package org.springframework.dao.support;

import org.springframework.dao.DataAccessException;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/dao/support/PersistenceExceptionTranslator.class */
public interface PersistenceExceptionTranslator {
    DataAccessException translateExceptionIfPossible(RuntimeException runtimeException);
}
