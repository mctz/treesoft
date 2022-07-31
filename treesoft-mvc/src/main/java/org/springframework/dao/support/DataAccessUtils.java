package org.springframework.dao.support;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.TypeMismatchDataAccessException;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.NumberUtils;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/dao/support/DataAccessUtils.class */
public abstract class DataAccessUtils {
    /* JADX WARN: Type inference failed for: r0v8, types: [java.lang.Throwable, org.springframework.dao.IncorrectResultSizeDataAccessException] */
    public static <T> T singleResult(Collection<T> results) throws IncorrectResultSizeDataAccessException {
        int size = results != null ? results.size() : 0;
        if (size == 0) {
            return null;
        }
        if (results.size() > 1) {
            throw new IncorrectResultSizeDataAccessException(1, size);
        }
        return results.iterator().next();
    }

    /* JADX WARN: Type inference failed for: r0v8, types: [java.lang.Throwable, org.springframework.dao.IncorrectResultSizeDataAccessException] */
    /* JADX WARN: Type inference failed for: r0v9, types: [java.lang.Throwable, org.springframework.dao.EmptyResultDataAccessException] */
    public static <T> T requiredSingleResult(Collection<T> results) throws IncorrectResultSizeDataAccessException {
        int size = results != null ? results.size() : 0;
        if (size == 0) {
            throw new EmptyResultDataAccessException(1);
        }
        if (results.size() > 1) {
            throw new IncorrectResultSizeDataAccessException(1, size);
        }
        return results.iterator().next();
    }

    /* JADX WARN: Type inference failed for: r0v8, types: [java.lang.Throwable, org.springframework.dao.IncorrectResultSizeDataAccessException] */
    public static <T> T uniqueResult(Collection<T> results) throws IncorrectResultSizeDataAccessException {
        int size = results != null ? results.size() : 0;
        if (size == 0) {
            return null;
        }
        if (!CollectionUtils.hasUniqueObject(results)) {
            throw new IncorrectResultSizeDataAccessException(1, size);
        }
        return results.iterator().next();
    }

    /* JADX WARN: Type inference failed for: r0v8, types: [java.lang.Throwable, org.springframework.dao.IncorrectResultSizeDataAccessException] */
    /* JADX WARN: Type inference failed for: r0v9, types: [java.lang.Throwable, org.springframework.dao.EmptyResultDataAccessException] */
    public static <T> T requiredUniqueResult(Collection<T> results) throws IncorrectResultSizeDataAccessException {
        int size = results != null ? results.size() : 0;
        if (size == 0) {
            throw new EmptyResultDataAccessException(1);
        }
        if (!CollectionUtils.hasUniqueObject(results)) {
            throw new IncorrectResultSizeDataAccessException(1, size);
        }
        return results.iterator().next();
    }

    /* JADX WARN: Type inference failed for: r0v10, types: [java.lang.Throwable, org.springframework.dao.TypeMismatchDataAccessException] */
    /* JADX WARN: Type inference failed for: r0v13, types: [java.lang.Throwable, org.springframework.dao.TypeMismatchDataAccessException] */
    public static <T> T objectResult(Collection<?> results, Class<T> requiredType) throws IncorrectResultSizeDataAccessException, TypeMismatchDataAccessException {
        Object result = requiredUniqueResult(results);
        Object result2 = result;
        if (requiredType != null) {
            boolean isInstance = requiredType.isInstance(result);
            result2 = result;
            if (!isInstance) {
                if (String.class.equals(requiredType)) {
                    result2 = result.toString();
                } else if (Number.class.isAssignableFrom(requiredType) && Number.class.isInstance(result)) {
                    try {
                        result2 = NumberUtils.convertNumberToTargetClass((Number) result, requiredType);
                    } catch (IllegalArgumentException ex) {
                        throw new TypeMismatchDataAccessException(ex.getMessage());
                    }
                } else {
                    throw new TypeMismatchDataAccessException("Result object is of type [" + result.getClass().getName() + "] and could not be converted to required type [" + requiredType.getName() + "]");
                }
            }
        }
        return (T) result2;
    }

    public static int intResult(Collection<?> results) throws IncorrectResultSizeDataAccessException, TypeMismatchDataAccessException {
        return ((Number) objectResult(results, Number.class)).intValue();
    }

    public static long longResult(Collection<?> results) throws IncorrectResultSizeDataAccessException, TypeMismatchDataAccessException {
        return ((Number) objectResult(results, Number.class)).longValue();
    }

    public static RuntimeException translateIfNecessary(RuntimeException rawException, PersistenceExceptionTranslator pet) {
        Assert.notNull(pet, "PersistenceExceptionTranslator must not be null");
        DataAccessException dex = pet.translateExceptionIfPossible(rawException);
        return (RuntimeException)(dex != null ? dex : rawException);
    }
}
