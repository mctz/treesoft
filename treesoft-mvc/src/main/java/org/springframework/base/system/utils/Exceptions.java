package org.springframework.base.system.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/utils/Exceptions.class */
public class Exceptions {
    public static RuntimeException unchecked(Exception e) {
        if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        }
        return new RuntimeException(e);
    }

    public static String getStackTraceAsString(Exception e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    public static boolean isCausedBy(Exception ex, Class<? extends Exception>... clsArr) {
        Throwable th = ex;
        while (true) {
            Throwable cause = th;
            if (cause != null) {
                for (Class<? extends Exception> causeClass : clsArr) {
                    if (causeClass.isInstance(cause)) {
                        return true;
                    }
                }
                th = cause.getCause();
            } else {
                return false;
            }
        }
    }
}
