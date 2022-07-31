package org.springframework.base.system.scheduler;

import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.base.common.utils.DateUtil;
import org.springframework.base.system.service.PermissionService;
import org.springframework.base.system.service.TaskService;

@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class QuartzJobFactoryForTask implements Job
{
    private static final Logger log = Logger.getLogger("");
    
    private TaskService taskService;
    
    private PermissionService permissionService;
    
    @Override
    public void execute(JobExecutionContext context)
        throws JobExecutionException
    {
        log.info("定时任务运行开始-------- start --------");
        String dataSynchronizeId = "";
        try
        {
            Map<String, Object> jobMessageMap = (Map)context.getMergedJobDataMap().get("jobMessageMap");
            log.info("定时任务运行时具体参数:" + jobMessageMap.get("id") + "," + jobMessageMap.get("name") + ", " + jobMessageMap.get("targetConfigId") + ", " + jobMessageMap.get("targetConfigId") + " ,"
                + DateUtil.getDateTimeString());
            dataSynchronizeId = (String)jobMessageMap.get("id");
            String souceConfigId = (String)jobMessageMap.get("souceConfigId");
            String souceDataBase = (String)jobMessageMap.get("souceDataBase");
            String sql = (String)jobMessageMap.get("doSql");
            this.permissionService.executeSqlNotRes(sql, souceDataBase, souceConfigId);
            this.taskService.taskLogSave("1", "运行成功!", dataSynchronizeId);
        }
        catch (Exception e)
        {
            log.error("定时任务运行异常," + e.getMessage(), e);
            this.permissionService.dataSynchronizeLogSave("0", "运行失败!" + e.getMessage(), dataSynchronizeId);
        }
        log.info("定时任务运行结束-------- end --------");
    }
    
    public void setPermissionService(PermissionService permissionService)
    {
        this.permissionService = permissionService;
    }
    
    public void setTaskService(TaskService taskService)
    {
        this.taskService = taskService;
    }
}
