package org.springframework.jca.cci;

import javax.resource.ResourceException;
import org.springframework.dao.DataAccessResourceFailureException;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/jca/cci/CannotCreateRecordException.class */
public class CannotCreateRecordException extends DataAccessResourceFailureException {
    public CannotCreateRecordException(String msg, ResourceException ex) {
        super(msg, ex);
    }
}
