package org.springframework.jca.cci.core;

import javax.resource.cci.InteractionSpec;
import javax.resource.cci.Record;
import org.springframework.dao.DataAccessException;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/jca/cci/core/CciOperations.class */
public interface CciOperations {
    <T> T execute(ConnectionCallback<T> connectionCallback) throws DataAccessException;

    <T> T execute(InteractionCallback<T> interactionCallback) throws DataAccessException;

    Record execute(InteractionSpec interactionSpec, Record record) throws DataAccessException;

    void execute(InteractionSpec interactionSpec, Record record, Record record2) throws DataAccessException;

    Record execute(InteractionSpec interactionSpec, RecordCreator recordCreator) throws DataAccessException;

    <T> T execute(InteractionSpec interactionSpec, Record record, RecordExtractor<T> recordExtractor) throws DataAccessException;

    <T> T execute(InteractionSpec interactionSpec, RecordCreator recordCreator, RecordExtractor<T> recordExtractor) throws DataAccessException;
}
