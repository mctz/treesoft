package org.springframework.jca.work.jboss;

import javax.resource.spi.work.WorkManager;
import org.springframework.jca.work.WorkManagerTaskExecutor;

@Deprecated
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/jca/work/jboss/JBossWorkManagerTaskExecutor.class */
public class JBossWorkManagerTaskExecutor extends WorkManagerTaskExecutor {
    public void setWorkManagerMBeanName(String mbeanName) {
        setWorkManager(JBossWorkManagerUtils.getWorkManager(mbeanName));
    }

    @Override // org.springframework.jca.work.WorkManagerTaskExecutor
    protected WorkManager getDefaultWorkManager() {
        return JBossWorkManagerUtils.getWorkManager();
    }
}
