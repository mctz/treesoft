package org.springframework.dao.support;

import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.util.Assert;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/dao/support/ChainedPersistenceExceptionTranslator.class */
public class ChainedPersistenceExceptionTranslator implements PersistenceExceptionTranslator {
    private final List<PersistenceExceptionTranslator> delegates = new ArrayList(4);

    public final void addDelegate(PersistenceExceptionTranslator pet) {
        Assert.notNull(pet, "PersistenceExceptionTranslator must not be null");
        this.delegates.add(pet);
    }

    public final PersistenceExceptionTranslator[] getDelegates() {
        return (PersistenceExceptionTranslator[]) this.delegates.toArray(new PersistenceExceptionTranslator[this.delegates.size()]);
    }

    @Override // org.springframework.dao.support.PersistenceExceptionTranslator
    public DataAccessException translateExceptionIfPossible(RuntimeException ex) {
        for (PersistenceExceptionTranslator pet : this.delegates) {
            DataAccessException translatedDex = pet.translateExceptionIfPossible(ex);
            if (translatedDex != null) {
                return translatedDex;
            }
        }
        return null;
    }
}
