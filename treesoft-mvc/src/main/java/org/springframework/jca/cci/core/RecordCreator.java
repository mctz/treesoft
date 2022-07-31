package org.springframework.jca.cci.core;

import javax.resource.ResourceException;
import javax.resource.cci.Record;
import javax.resource.cci.RecordFactory;
import org.springframework.dao.DataAccessException;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/jca/cci/core/RecordCreator.class */
public interface RecordCreator {
    Record createRecord(RecordFactory recordFactory) throws ResourceException, DataAccessException;
}
