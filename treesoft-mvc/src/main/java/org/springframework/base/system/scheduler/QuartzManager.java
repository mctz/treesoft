package org.springframework.base.system.scheduler;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.base.system.service.PermissionService;
import org.springframework.base.system.service.TaskService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component("quartzManager")
public class QuartzManager implements ApplicationContextAware
{
    private static final Logger logger = LoggerFactory.getLogger(QuartzManager.class);
    
    private static SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();
    
    private static String JOB_GROUP_NAME = "MY_JOBGROUP_NAME";
    
    private static String TRIGGER_GROUP_NAME = "MY_TRIGGERGROUP_NAME";
    
    @Resource
    private PermissionService permissionService;
    
    @Resource
    private TaskService taskService;
    
    private ApplicationContext webApplicationContext;
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
        throws BeansException
    {
        this.webApplicationContext = applicationContext;
    }
    
    @PostConstruct
    public void init()
    {
        String dataSynchronizeId = "";
        String taskId = "";
        try
        {
            List<Map<String, Object>> list3 = this.permissionService.getDataSynchronizeList2("0");
            List<Map<String, Object>> taskList = this.taskService.getTaskList2("0");
            for (Map<String, Object> job : list3)
            {
                dataSynchronizeId = (String)job.get("id");
                addJob(dataSynchronizeId, QuartzJobFactory.class, job.get("cron").toString(), job);
                this.permissionService.dataSynchronizeUpdateStatus(dataSynchronizeId, "1");
            }
            for (Map<String, Object> job : taskList)
            {
                taskId = (String)job.get("id");
                addJob(taskId, QuartzJobFactoryForTask.class, job.get("cron").toString(), job);
                this.taskService.taskUpdateStatus(taskId, "1");
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
    }
    
    public void addJob(String jobName, Class cls, String time, Object jobMessageMap)
    {
        try
        {
            Scheduler sched = gSchedulerFactory.getScheduler();
            JobDetail job = JobBuilder.newJob(cls).withIdentity(jobName, JOB_GROUP_NAME).build();
            JobDataMap jobMap = new JobDataMap();
            jobMap.put("taskService", this.taskService);
            jobMap.put("permissionService", this.permissionService);
            jobMap.put("webApplicationContext", this.webApplicationContext);
            job.getJobDataMap().put("jobMessageMap", jobMessageMap);
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(time);
            Trigger trigger = TriggerBuilder.newTrigger().usingJobData(jobMap).withIdentity(jobName, TRIGGER_GROUP_NAME).withSchedule(scheduleBuilder).build();
            sched.scheduleJob(job, trigger);
            if (!sched.isShutdown())
            {
                sched.start();
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public void addJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName, Class jobClass, String time)
    {
        try
        {
            Scheduler sched = gSchedulerFactory.getScheduler();
            JobDetail job = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).build();
            JobDataMap jobMap = new JobDataMap();
            jobMap.put("taskService", this.taskService);
            jobMap.put("permissionService", this.permissionService);
            jobMap.put("webApplicationContext", this.webApplicationContext);
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(time);
            Trigger trigger = TriggerBuilder.newTrigger().usingJobData(jobMap).withIdentity(triggerName, triggerGroupName).withSchedule(scheduleBuilder).build();
            sched.scheduleJob(job, trigger);
            if (!sched.isShutdown())
            {
                sched.start();
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public void addJobOne(String jobName, Class cls, Object jobMessageMap)
    {
        try
        {
            Scheduler sched = gSchedulerFactory.getScheduler();
            JobDetail job = JobBuilder.newJob(cls).withIdentity(jobName, JOB_GROUP_NAME).build();
            JobDataMap jobMap = new JobDataMap();
            jobMap.put("taskService", this.taskService);
            jobMap.put("permissionService", this.permissionService);
            jobMap.put("webApplicationContext", this.webApplicationContext);
            job.getJobDataMap().put("jobMessageMap", jobMessageMap);
            Trigger trigger = TriggerBuilder.newTrigger()
                .usingJobData(jobMap)
                .withIdentity(jobName, TRIGGER_GROUP_NAME)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(3).withRepeatCount(0))
                .build();
            sched.scheduleJob(job, trigger);
            if (!sched.isShutdown())
            {
                sched.start();
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public static void modifyJobTime(String jobName, String time)
    {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, TRIGGER_GROUP_NAME);
        try
        {
            Scheduler sched = gSchedulerFactory.getScheduler();
            CronTrigger trigger = (CronTrigger)sched.getTrigger(triggerKey);
            if (trigger == null)
            {
                return;
            }
            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(time))
            {
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(time);
                trigger = (CronTrigger)trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
                sched.rescheduleJob(triggerKey, trigger);
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public static void modifyJobTime(String triggerName, String triggerGroupName, String time)
    {
        TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
        try
        {
            Scheduler sched = gSchedulerFactory.getScheduler();
            CronTrigger trigger = (CronTrigger)sched.getTrigger(triggerKey);
            if (trigger == null)
            {
                return;
            }
            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(time))
            {
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(time);
                trigger = (CronTrigger)trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
                sched.resumeTrigger(triggerKey);
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public static void removeJob(String jobName)
    {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, TRIGGER_GROUP_NAME);
        JobKey jobKey = JobKey.jobKey(jobName, JOB_GROUP_NAME);
        try
        {
            Scheduler sched = gSchedulerFactory.getScheduler();
            Trigger trigger = sched.getTrigger(triggerKey);
            if (trigger == null)
            {
                return;
            }
            sched.pauseTrigger(triggerKey);
            sched.unscheduleJob(triggerKey);
            sched.deleteJob(jobKey);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public static void removeJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName)
    {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, triggerGroupName);
        JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
        try
        {
            Scheduler sched = gSchedulerFactory.getScheduler();
            sched.pauseTrigger(triggerKey);
            sched.unscheduleJob(triggerKey);
            sched.deleteJob(jobKey);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public static void pauseJob(String jobName, String jobGroupName)
    {
        JobKey jobKey = JobKey.jobKey(jobName, jobName);
        try
        {
            Scheduler sched = gSchedulerFactory.getScheduler();
            sched.pauseJob(jobKey);
        }
        catch (SchedulerException e)
        {
            logger.error(e.getMessage(), e);
        }
    }
    
    public static void pauseJob(String jobName)
    {
        JobKey jobKey = JobKey.jobKey(jobName, JOB_GROUP_NAME);
        try
        {
            Scheduler sched = gSchedulerFactory.getScheduler();
            sched.pauseJob(jobKey);
        }
        catch (SchedulerException e)
        {
            logger.error(e.getMessage(), e);
        }
    }
    
    public static void startJobs()
    {
        try
        {
            Scheduler sched = gSchedulerFactory.getScheduler();
            sched.start();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public static void shutdownJobs()
    {
        try
        {
            Scheduler sched = gSchedulerFactory.getScheduler();
            if (!sched.isShutdown())
            {
                sched.shutdown();
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
