package org.springframework.jca.work.glassfish;

import java.lang.reflect.Method;
import javax.resource.spi.work.WorkManager;
import org.springframework.jca.work.WorkManagerTaskExecutor;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/jca/work/glassfish/GlassFishWorkManagerTaskExecutor.class */
public class GlassFishWorkManagerTaskExecutor extends WorkManagerTaskExecutor {
    private static final String WORK_MANAGER_FACTORY_CLASS = "com.sun.enterprise.connectors.work.WorkManagerFactory";
    private final Method getWorkManagerMethod;

    public GlassFishWorkManagerTaskExecutor() {
        try {
            Class<?> wmf = getClass().getClassLoader().loadClass(WORK_MANAGER_FACTORY_CLASS);
            this.getWorkManagerMethod = wmf.getMethod("getWorkManager", String.class);
        } catch (Exception ex) {
            throw new IllegalStateException("Could not initialize GlassFishWorkManagerTaskExecutor because GlassFish API is not available", ex);
        }
    }

    public void setThreadPoolName(String threadPoolName) {
        WorkManager wm = (WorkManager) ReflectionUtils.invokeMethod(this.getWorkManagerMethod, (Object) null, new Object[]{threadPoolName});
        if (wm == null) {
            throw new IllegalArgumentException("Specified thread pool name '" + threadPoolName + "' does not correspond to an actual pool definition in GlassFish. Check your configuration!");
        }
        setWorkManager(wm);
    }

    @Override // org.springframework.jca.work.WorkManagerTaskExecutor
    protected WorkManager getDefaultWorkManager() {
        return (WorkManager) ReflectionUtils.invokeMethod(this.getWorkManagerMethod, (Object) null, new Object[]{null});
    }
}
