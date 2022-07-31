package org.springframework.jca.cci;

import java.sql.SQLException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/jca/cci/InvalidResultSetAccessException.class */
public class InvalidResultSetAccessException extends InvalidDataAccessResourceUsageException {
    public InvalidResultSetAccessException(String msg, SQLException ex) {
        super(ex.getMessage(), ex);
    }
}
