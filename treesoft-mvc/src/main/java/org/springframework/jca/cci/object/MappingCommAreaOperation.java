package org.springframework.jca.cci.object;

import java.io.IOException;
import javax.resource.cci.ConnectionFactory;
import javax.resource.cci.InteractionSpec;
import javax.resource.cci.Record;
import javax.resource.cci.RecordFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jca.cci.core.support.CommAreaRecord;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/jca/cci/object/MappingCommAreaOperation.class */
public abstract class MappingCommAreaOperation extends MappingRecordOperation {
    protected abstract byte[] objectToBytes(Object obj) throws IOException, DataAccessException;

    protected abstract Object bytesToObject(byte[] bArr) throws IOException, DataAccessException;

    public MappingCommAreaOperation() {
    }

    public MappingCommAreaOperation(ConnectionFactory connectionFactory, InteractionSpec interactionSpec) {
        super(connectionFactory, interactionSpec);
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [java.lang.Throwable, org.springframework.dao.DataRetrievalFailureException] */
    @Override // org.springframework.jca.cci.object.MappingRecordOperation
    protected final Record createInputRecord(RecordFactory recordFactory, Object inObject) {
        try {
            return new CommAreaRecord(objectToBytes(inObject));
        } catch (IOException ex) {
            throw new DataRetrievalFailureException("I/O exception during bytes conversion", ex);
        }
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [java.lang.Throwable, org.springframework.dao.DataRetrievalFailureException] */
    @Override // org.springframework.jca.cci.object.MappingRecordOperation
    protected final Object extractOutputData(Record record) throws DataAccessException {
        CommAreaRecord commAreaRecord = (CommAreaRecord) record;
        try {
            return bytesToObject(commAreaRecord.toByteArray());
        } catch (IOException ex) {
            throw new DataRetrievalFailureException("I/O exception during bytes conversion", ex);
        }
    }
}
