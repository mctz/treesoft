package org.springframework.jca.cci.object;

import java.sql.SQLException;
import javax.resource.ResourceException;
import javax.resource.cci.ConnectionFactory;
import javax.resource.cci.InteractionSpec;
import javax.resource.cci.Record;
import javax.resource.cci.RecordFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jca.cci.core.RecordCreator;
import org.springframework.jca.cci.core.RecordExtractor;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/jca/cci/object/MappingRecordOperation.class */
public abstract class MappingRecordOperation extends EisOperation {
    protected abstract Record createInputRecord(RecordFactory recordFactory, Object obj) throws ResourceException, DataAccessException;

    protected abstract Object extractOutputData(Record record) throws ResourceException, SQLException, DataAccessException;

    public MappingRecordOperation() {
    }

    public MappingRecordOperation(ConnectionFactory connectionFactory, InteractionSpec interactionSpec) {
        getCciTemplate().setConnectionFactory(connectionFactory);
        setInteractionSpec(interactionSpec);
    }

    public void setOutputRecordCreator(RecordCreator creator) {
        getCciTemplate().setOutputRecordCreator(creator);
    }

    public Object execute(Object inputObject) throws DataAccessException {
        return getCciTemplate().execute(getInteractionSpec(), new RecordCreatorImpl(inputObject), new RecordExtractorImpl());
    }

    /* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/jca/cci/object/MappingRecordOperation$RecordCreatorImpl.class */
    protected class RecordCreatorImpl implements RecordCreator {
        private final Object inputObject;

        public RecordCreatorImpl(Object inObject) {
            MappingRecordOperation.this = this$0;
            this.inputObject = inObject;
        }

        @Override // org.springframework.jca.cci.core.RecordCreator
        public Record createRecord(RecordFactory recordFactory) throws ResourceException, DataAccessException {
            return MappingRecordOperation.this.createInputRecord(recordFactory, this.inputObject);
        }
    }

    /* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/jca/cci/object/MappingRecordOperation$RecordExtractorImpl.class */
    protected class RecordExtractorImpl implements RecordExtractor<Object> {
        protected RecordExtractorImpl() {
            MappingRecordOperation.this = this$0;
        }

        @Override // org.springframework.jca.cci.core.RecordExtractor
        public Object extractData(Record record) throws ResourceException, SQLException, DataAccessException {
            return MappingRecordOperation.this.extractOutputData(record);
        }
    }
}
