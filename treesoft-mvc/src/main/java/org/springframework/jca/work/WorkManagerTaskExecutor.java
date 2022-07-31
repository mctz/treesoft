package org.springframework.jca.work;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import javax.naming.NamingException;
import javax.resource.spi.BootstrapContext;
import javax.resource.spi.work.ExecutionContext;
import javax.resource.spi.work.Work;
import javax.resource.spi.work.WorkException;
import javax.resource.spi.work.WorkListener;
import javax.resource.spi.work.WorkManager;
import javax.resource.spi.work.WorkRejectedException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.core.task.TaskTimeoutException;
import org.springframework.jca.context.BootstrapContextAware;
import org.springframework.jndi.JndiLocatorSupport;
import org.springframework.scheduling.SchedulingException;
import org.springframework.scheduling.SchedulingTaskExecutor;
import org.springframework.util.Assert;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureTask;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/jca/work/WorkManagerTaskExecutor.class */
public class WorkManagerTaskExecutor extends JndiLocatorSupport implements AsyncListenableTaskExecutor, SchedulingTaskExecutor, WorkManager, BootstrapContextAware, InitializingBean {
    private WorkManager workManager;
    private String workManagerName;
    private boolean blockUntilStarted = false;
    private boolean blockUntilCompleted = false;
    private WorkListener workListener;

    public WorkManagerTaskExecutor() {
    }

    public WorkManagerTaskExecutor(WorkManager workManager) {
        setWorkManager(workManager);
    }

    public void setWorkManager(WorkManager workManager) {
        Assert.notNull(workManager, "WorkManager must not be null");
        this.workManager = workManager;
    }

    public void setWorkManagerName(String workManagerName) {
        this.workManagerName = workManagerName;
    }

    @Override // org.springframework.jca.context.BootstrapContextAware
    public void setBootstrapContext(BootstrapContext bootstrapContext) {
        Assert.notNull(bootstrapContext, "BootstrapContext must not be null");
        this.workManager = bootstrapContext.getWorkManager();
    }

    public void setBlockUntilStarted(boolean blockUntilStarted) {
        this.blockUntilStarted = blockUntilStarted;
    }

    public void setBlockUntilCompleted(boolean blockUntilCompleted) {
        this.blockUntilCompleted = blockUntilCompleted;
    }

    public void setWorkListener(WorkListener workListener) {
        this.workListener = workListener;
    }

    public void afterPropertiesSet() throws NamingException {
        if (this.workManager == null) {
            if (this.workManagerName != null) {
                this.workManager = (WorkManager) lookup(this.workManagerName, WorkManager.class);
            } else {
                this.workManager = getDefaultWorkManager();
            }
        }
    }

    protected WorkManager getDefaultWorkManager() {
        return new SimpleTaskWorkManager();
    }

    public void execute(Runnable task) {
        execute(task, Long.MAX_VALUE);
    }

    public void execute(Runnable task, long startTimeout) {
        Assert.state(this.workManager != null, "No WorkManager specified");
        DelegatingWork delegatingWork = new DelegatingWork(task);
        try {
            if (this.blockUntilCompleted) {
                if (startTimeout != Long.MAX_VALUE || this.workListener != null) {
                    this.workManager.doWork(delegatingWork, startTimeout, (ExecutionContext) null, this.workListener);
                } else {
                    this.workManager.doWork(delegatingWork);
                }
            } else if (this.blockUntilStarted) {
                if (startTimeout != Long.MAX_VALUE || this.workListener != null) {
                    this.workManager.startWork(delegatingWork, startTimeout, (ExecutionContext) null, this.workListener);
                } else {
                    this.workManager.startWork(delegatingWork);
                }
            } else if (startTimeout != Long.MAX_VALUE || this.workListener != null) {
                this.workManager.scheduleWork(delegatingWork, startTimeout, (ExecutionContext) null, this.workListener);
            } else {
                this.workManager.scheduleWork(delegatingWork);
            }
        } catch (WorkException ex) {
            throw new SchedulingException("Could not schedule task on JCA WorkManager", ex);
        } catch (WorkRejectedException ex2) {
            if ("1".equals(ex2.getErrorCode())) {
                throw new TaskTimeoutException("JCA WorkManager rejected task because of timeout: " + task, ex2);
            }
            throw new TaskRejectedException("JCA WorkManager rejected task: " + task, ex2);
        }
    }

    public Future<?> submit(Runnable task) {
        FutureTask<Object> future = new FutureTask<>(task, null);
        execute(future, Long.MAX_VALUE);
        return future;
    }

    public <T> Future<T> submit(Callable<T> task) {
        FutureTask<T> future = new FutureTask<>(task);
        execute(future, Long.MAX_VALUE);
        return future;
    }

    public ListenableFuture<?> submitListenable(Runnable task) {
        ListenableFutureTask<Object> future = new ListenableFutureTask<>(task, (Object) null);
        execute(future, Long.MAX_VALUE);
        return future;
    }

    public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
        ListenableFutureTask<T> future = new ListenableFutureTask<>(task);
        execute(future, Long.MAX_VALUE);
        return future;
    }

    public boolean prefersShortLivedTasks() {
        return true;
    }

    public void doWork(Work work) throws WorkException {
        this.workManager.doWork(work);
    }

    public void doWork(Work work, long delay, ExecutionContext executionContext, WorkListener workListener) throws WorkException {
        this.workManager.doWork(work, delay, executionContext, workListener);
    }

    public long startWork(Work work) throws WorkException {
        return this.workManager.startWork(work);
    }

    public long startWork(Work work, long delay, ExecutionContext executionContext, WorkListener workListener) throws WorkException {
        return this.workManager.startWork(work, delay, executionContext, workListener);
    }

    public void scheduleWork(Work work) throws WorkException {
        this.workManager.scheduleWork(work);
    }

    public void scheduleWork(Work work, long delay, ExecutionContext executionContext, WorkListener workListener) throws WorkException {
        this.workManager.scheduleWork(work, delay, executionContext, workListener);
    }
}
