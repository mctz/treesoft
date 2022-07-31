package org.springframework.jca.cci.core;

import java.sql.SQLException;
import javax.resource.ResourceException;
import javax.resource.cci.ConnectionFactory;
import javax.resource.cci.Interaction;
import org.springframework.dao.DataAccessException;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/jca/cci/core/InteractionCallback.class */
public interface InteractionCallback<T> {
    T doInInteraction(Interaction interaction, ConnectionFactory connectionFactory) throws ResourceException, SQLException, DataAccessException;
}
