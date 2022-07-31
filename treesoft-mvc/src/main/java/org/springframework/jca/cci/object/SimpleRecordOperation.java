package org.springframework.jca.cci.object;

import javax.resource.cci.ConnectionFactory;
import javax.resource.cci.InteractionSpec;
import javax.resource.cci.Record;
import org.springframework.dao.DataAccessException;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/jca/cci/object/SimpleRecordOperation.class */
public class SimpleRecordOperation extends EisOperation {
    public SimpleRecordOperation() {
    }

    public SimpleRecordOperation(ConnectionFactory connectionFactory, InteractionSpec interactionSpec) {
        getCciTemplate().setConnectionFactory(connectionFactory);
        setInteractionSpec(interactionSpec);
    }

    public Record execute(Record inputRecord) throws DataAccessException {
        return getCciTemplate().execute(getInteractionSpec(), inputRecord);
    }

    public void execute(Record inputRecord, Record outputRecord) throws DataAccessException {
        getCciTemplate().execute(getInteractionSpec(), inputRecord, outputRecord);
    }
}
