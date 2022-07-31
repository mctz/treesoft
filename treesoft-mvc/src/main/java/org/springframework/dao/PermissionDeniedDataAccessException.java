package org.springframework.dao;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/dao/PermissionDeniedDataAccessException.class */
public class PermissionDeniedDataAccessException extends NonTransientDataAccessException {
    public PermissionDeniedDataAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
