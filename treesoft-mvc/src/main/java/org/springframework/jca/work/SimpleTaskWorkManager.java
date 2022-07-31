package org.springframework.jca.work;

import javax.resource.spi.work.ExecutionContext;
import javax.resource.spi.work.Work;
import javax.resource.spi.work.WorkAdapter;
import javax.resource.spi.work.WorkCompletedException;
import javax.resource.spi.work.WorkEvent;
import javax.resource.spi.work.WorkException;
import javax.resource.spi.work.WorkListener;
import javax.resource.spi.work.WorkManager;
import javax.resource.spi.work.WorkRejectedException;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.core.task.TaskTimeoutException;
import org.springframework.util.Assert;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/jca/work/SimpleTaskWorkManager.class */
public class SimpleTaskWorkManager implements WorkManager {
    private TaskExecutor syncTaskExecutor = new SyncTaskExecutor();
    private AsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();

    public void setSyncTaskExecutor(TaskExecutor syncTaskExecutor) {
        this.syncTaskExecutor = syncTaskExecutor;
    }

    public void setAsyncTaskExecutor(AsyncTaskExecutor asyncTaskExecutor) {
        this.asyncTaskExecutor = asyncTaskExecutor;
    }

    public void doWork(Work work) throws WorkException {
        doWork(work, Long.MAX_VALUE, null, null);
    }

    public void doWork(Work work, long startTimeout, ExecutionContext executionContext, WorkListener workListener) throws WorkException {
        Assert.state(this.syncTaskExecutor != null, "No 'syncTaskExecutor' set");
        executeWork(this.syncTaskExecutor, work, startTimeout, false, executionContext, workListener);
    }

    public long startWork(Work work) throws WorkException {
        return startWork(work, Long.MAX_VALUE, null, null);
    }

    public long startWork(Work work, long startTimeout, ExecutionContext executionContext, WorkListener workListener) throws WorkException {
        Assert.state(this.asyncTaskExecutor != null, "No 'asyncTaskExecutor' set");
        return executeWork(this.asyncTaskExecutor, work, startTimeout, true, executionContext, workListener);
    }

    public void scheduleWork(Work work) throws WorkException {
        scheduleWork(work, Long.MAX_VALUE, null, null);
    }

    public void scheduleWork(Work work, long startTimeout, ExecutionContext executionContext, WorkListener workListener) throws WorkException {
        Assert.state(this.asyncTaskExecutor != null, "No 'asyncTaskExecutor' set");
        executeWork(this.asyncTaskExecutor, work, startTimeout, false, executionContext, workListener);
    }

    /* JADX WARN: Type inference failed for: r0v5, types: [org.springframework.jca.work.SimpleTaskWorkManager$DelegatingWorkAdapter, java.lang.Runnable] */
    protected long executeWork(TaskExecutor taskExecutor, Work work, long startTimeout, boolean blockUntilStarted, ExecutionContext executionContext, WorkListener workListener) throws WorkException {
        if (executionContext != null && executionContext.getXid() != null) {
            throw new WorkException("SimpleTaskWorkManager does not supported imported XIDs: " + executionContext.getXid());
        }
        WorkListener workListenerToUse = workListener;
        if (workListenerToUse == null) {
            workListenerToUse = new WorkAdapter();
        }
        boolean isAsync = taskExecutor instanceof AsyncTaskExecutor;
        DelegatingWorkAdapter delegatingWorkAdapter = new DelegatingWorkAdapter(work, workListenerToUse, !isAsync);
        try {
            if (isAsync) {
                ((AsyncTaskExecutor) taskExecutor).execute((Runnable) delegatingWorkAdapter, startTimeout);
            } else {
                taskExecutor.execute((Runnable) delegatingWorkAdapter);
            }
            if (isAsync) {
                workListenerToUse.workAccepted(new WorkEvent(this, 1, work, (WorkException) null));
            }
            if (blockUntilStarted) {
                long acceptanceTime = System.currentTimeMillis();
                synchronized (delegatingWorkAdapter.monitor) {
                    while (!delegatingWorkAdapter.started) {
                        try {
                            delegatingWorkAdapter.monitor.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
                return System.currentTimeMillis() - acceptanceTime;
            }
            return -1L;
        } catch (TaskRejectedException ex) {
            WorkRejectedException workRejectedException = new WorkRejectedException("TaskExecutor rejected Work: " + work, ex);
            workRejectedException.setErrorCode("-1");
            workListenerToUse.workRejected(new WorkEvent(this, 2, work, workRejectedException));
            throw workRejectedException;
        } catch (TaskTimeoutException ex2) {
            WorkRejectedException workRejectedException2 = new WorkRejectedException("TaskExecutor rejected Work because of timeout: " + work, ex2);
            workRejectedException2.setErrorCode("1");
            workListenerToUse.workRejected(new WorkEvent(this, 2, work, workRejectedException2));
            throw workRejectedException2;
        } catch (Throwable ex3) {
            WorkException wex = new WorkException("TaskExecutor failed to execute Work: " + work, ex3);
            wex.setErrorCode("-1");
            throw wex;
        }
    }

    /* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/jca/work/SimpleTaskWorkManager$DelegatingWorkAdapter.class */
    public static class DelegatingWorkAdapter implements Work {
        private final Work work;
        private final WorkListener workListener;
        private final boolean acceptOnExecution;
        public final Object monitor = new Object();
        public boolean started = false;

        public DelegatingWorkAdapter(Work work, WorkListener workListener, boolean acceptOnExecution) {
            this.work = work;
            this.workListener = workListener;
            this.acceptOnExecution = acceptOnExecution;
        }

        public void run() {
            if (this.acceptOnExecution) {
                this.workListener.workAccepted(new WorkEvent(this, 1, this.work, (WorkException) null));
            }
            synchronized (this.monitor) {
                this.started = true;
                this.monitor.notify();
            }
            this.workListener.workStarted(new WorkEvent(this, 3, this.work, (WorkException) null));
            try {
                this.work.run();
                this.workListener.workCompleted(new WorkEvent(this, 4, this.work, (WorkException) null));
            } catch (Error err) {
                this.workListener.workCompleted(new WorkEvent(this, 4, this.work, new WorkCompletedException(err)));
                throw err;
            } catch (RuntimeException ex) {
                this.workListener.workCompleted(new WorkEvent(this, 4, this.work, new WorkCompletedException(ex)));
                throw ex;
            }
        }

        public void release() {
            this.work.release();
        }
    }
}
