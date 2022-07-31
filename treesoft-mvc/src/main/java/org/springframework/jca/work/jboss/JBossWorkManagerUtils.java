package org.springframework.jca.work.jboss;

import java.lang.reflect.Method;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.naming.InitialContext;
import javax.resource.spi.work.WorkManager;
import org.springframework.util.Assert;

@Deprecated
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/jca/work/jboss/JBossWorkManagerUtils.class */
public abstract class JBossWorkManagerUtils {
    private static final String JBOSS_WORK_MANAGER_MBEAN_CLASS_NAME = "org.jboss.resource.work.JBossWorkManagerMBean";
    private static final String MBEAN_SERVER_CONNECTION_JNDI_NAME = "jmx/invoker/RMIAdaptor";
    private static final String DEFAULT_WORK_MANAGER_MBEAN_NAME = "jboss.jca:service=WorkManager";

    public static WorkManager getWorkManager() {
        return getWorkManager(DEFAULT_WORK_MANAGER_MBEAN_NAME);
    }

    public static WorkManager getWorkManager(String mbeanName) {
        Assert.hasLength(mbeanName, "JBossWorkManagerMBean name must not be empty");
        try {
            Class<?> mbeanClass = JBossWorkManagerUtils.class.getClassLoader().loadClass(JBOSS_WORK_MANAGER_MBEAN_CLASS_NAME);
            InitialContext jndiContext = new InitialContext();
            MBeanServerConnection mconn = (MBeanServerConnection) jndiContext.lookup(MBEAN_SERVER_CONNECTION_JNDI_NAME);
            ObjectName objectName = ObjectName.getInstance(mbeanName);
            Object workManagerMBean = MBeanServerInvocationHandler.newProxyInstance(mconn, objectName, mbeanClass, false);
            Method getInstanceMethod = workManagerMBean.getClass().getMethod("getInstance", new Class[0]);
            return (WorkManager) getInstanceMethod.invoke(workManagerMBean, new Object[0]);
        } catch (Exception ex) {
            throw new IllegalStateException("Could not initialize JBossWorkManagerTaskExecutor because JBoss API is not available", ex);
        }
    }
}
