package org.springframework.jca.cci.core;

import java.sql.SQLException;
import javax.resource.ResourceException;
import javax.resource.cci.Connection;
import javax.resource.cci.ConnectionFactory;
import org.springframework.dao.DataAccessException;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/jca/cci/core/ConnectionCallback.class */
public interface ConnectionCallback<T> {
    T doInConnection(Connection connection, ConnectionFactory connectionFactory) throws ResourceException, SQLException, DataAccessException;
}
