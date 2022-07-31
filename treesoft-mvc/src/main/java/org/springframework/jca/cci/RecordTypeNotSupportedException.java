package org.springframework.jca.cci;

import javax.resource.ResourceException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/jca/cci/RecordTypeNotSupportedException.class */
public class RecordTypeNotSupportedException extends InvalidDataAccessResourceUsageException {
    public RecordTypeNotSupportedException(String msg, ResourceException ex) {
        super(msg, ex);
    }
}
