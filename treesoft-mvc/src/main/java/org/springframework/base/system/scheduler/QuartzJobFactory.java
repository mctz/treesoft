package org.springframework.base.system.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.base.system.service.PermissionService;

@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class QuartzJobFactory implements Job
{
    private static final Logger logger = LoggerFactory.getLogger(QuartzJobFactory.class);
    
    private PermissionService permissionService;
    
    @Override
    public void execute(JobExecutionContext context)
        throws JobExecutionException
    {
        logger.info("数据交换任务运行开始-------- start --------");
        String dataSynchronizeId = "";
        try
        {
            Map<String, Object> jobMessageMap = (Map)context.getMergedJobDataMap().get("jobMessageMap");
            logger.info("数据交换任务运行时具体参数:" + jobMessageMap.get("id") + "," + jobMessageMap.get("name") + ", " + jobMessageMap.get("targetConfigId") + ", " + jobMessageMap.get("targetConfigId"));
            dataSynchronizeId = (String)jobMessageMap.get("id");
            String souceConfigId = (String)jobMessageMap.get("souceConfigId");
            String souceDataBase = (String)jobMessageMap.get("souceDataBase");
            String sql = (String)jobMessageMap.get("doSql");
            String targetConfigId = (String)jobMessageMap.get("targetConfigId");
            String targetDataBase = (String)jobMessageMap.get("targetDataBase");
            String targetTable = (String)jobMessageMap.get("targetTable");
            String operation = (String)jobMessageMap.get("operation");
            String qualification = (String)jobMessageMap.get("qualification");
            Map<String, Object> map = this.permissionService.getConfig(souceConfigId);
            String databaseType = (String)map.get("databaseType");
            Map<String, Object> map2 = this.permissionService.getConfig(targetConfigId);
            String targerDatabaseType = (String)map2.get("databaseType");
            List<Map<String, Object>> dataList = new ArrayList<>();
            if (databaseType.equals("MySql"))
            {
                int rowCount = this.permissionService.executeQueryForCount(sql, souceDataBase, souceConfigId);
                dataList = this.permissionService.selectAllDataFromSQLForMysql(souceDataBase, souceConfigId, sql);
            }
            if (databaseType.equals("MariaDB"))
            {
                dataList = this.permissionService.selectAllDataFromSQLForMysql(souceDataBase, souceConfigId, sql);
            }
            if (databaseType.equals("Oracle"))
            {
                dataList = this.permissionService.selectAllDataFromSQLForOracle(souceDataBase, souceConfigId, sql);
            }
            if (databaseType.equals("PostgreSQL"))
            {
                dataList = this.permissionService.selectAllDataFromSQLForPostgreSQL(souceDataBase, souceConfigId, sql);
            }
            if (databaseType.equals("MSSQL"))
            {
                dataList = this.permissionService.selectAllDataFromSQLForMSSQL(souceDataBase, souceConfigId, sql);
            }
            if (databaseType.equals("MongoDB"))
            {
                dataList = this.permissionService.selectAllDataFromSQLForMongoDB(souceDataBase, souceConfigId, sql);
            }
            if (databaseType.equals("Hive2"))
            {
                dataList = this.permissionService.selectAllDataFromSQLForHive2(souceDataBase, souceConfigId, sql);
            }
            if (operation.equals("0"))
            {
                if (targerDatabaseType.equals("MySql"))
                {
                    this.permissionService.insertByDataListForMySQL(dataList, targetDataBase, targetTable, targetConfigId);
                }
                if (targerDatabaseType.equals("MariaDB"))
                {
                    this.permissionService.insertByDataListForMySQL(dataList, targetDataBase, targetTable, targetConfigId);
                }
                if (targerDatabaseType.equals("Oracle"))
                {
                    this.permissionService.insertByDataListForOracle(dataList, targetDataBase, targetTable, targetConfigId);
                }
                if (targerDatabaseType.equals("PostgreSQL"))
                {
                    this.permissionService.insertByDataListForOracle(dataList, targetDataBase, targetTable, targetConfigId);
                }
                if (targerDatabaseType.equals("MSSQL"))
                {
                    this.permissionService.insertByDataListForMSSQL(dataList, targetDataBase, targetTable, targetConfigId);
                }
                if (targerDatabaseType.equals("MongoDB"))
                {
                    this.permissionService.insertByDataListForMongoDB(dataList, targetDataBase, targetTable, targetConfigId);
                }
                if (targerDatabaseType.equals("Hive2"))
                {
                    throw new Exception("Hive不支持该操作。");
                }
            }
            if (operation.equals("1"))
            {
                if (targerDatabaseType.equals("MySql"))
                {
                    this.permissionService.updateByDataListForMySQL(dataList, targetDataBase, targetTable, targetConfigId, qualification);
                }
                if (targerDatabaseType.equals("MariaDB"))
                {
                    this.permissionService.updateByDataListForMySQL(dataList, targetDataBase, targetTable, targetConfigId, qualification);
                }
                if (targerDatabaseType.equals("Oracle"))
                {
                    this.permissionService.updateByDataListForOracle(dataList, targetDataBase, targetTable, targetConfigId, qualification);
                }
                if (targerDatabaseType.equals("PostgreSQL"))
                {
                    this.permissionService.updateByDataListForOracle(dataList, targetDataBase, targetTable, targetConfigId, qualification);
                }
                if (targerDatabaseType.equals("MSSQL"))
                {
                    this.permissionService.updateByDataListForMSSQL(dataList, targetDataBase, targetTable, targetConfigId, qualification);
                }
                if (targerDatabaseType.equals("MongoDB"))
                {
                    this.permissionService.updateByDataListForMongoDB(dataList, targetDataBase, targetTable, targetConfigId, qualification);
                }
                if (targerDatabaseType.equals("Hive2"))
                {
                    throw new Exception("Hive不支持该操作。");
                }
            }
            if (operation.equals("2"))
            {
                if (targerDatabaseType.equals("MySql"))
                {
                    this.permissionService.insertOrUpdateByDataListForMySQL(dataList, targetDataBase, targetTable, targetConfigId, qualification);
                }
                if (targerDatabaseType.equals("MariaDB"))
                {
                    this.permissionService.updateByDataListForMySQL(dataList, targetDataBase, targetTable, targetConfigId, qualification);
                }
                if (targerDatabaseType.equals("Oracle"))
                {
                    this.permissionService.updateByDataListForOracle(dataList, targetDataBase, targetTable, targetConfigId, qualification);
                }
                if (targerDatabaseType.equals("PostgreSQL"))
                {
                    this.permissionService.updateByDataListForOracle(dataList, targetDataBase, targetTable, targetConfigId, qualification);
                }
                if (targerDatabaseType.equals("MSSQL"))
                {
                    this.permissionService.updateByDataListForMSSQL(dataList, targetDataBase, targetTable, targetConfigId, qualification);
                }
                if (targerDatabaseType.equals("MongoDB"))
                {
                    this.permissionService.updateByDataListForMongoDB(dataList, targetDataBase, targetTable, targetConfigId, qualification);
                }
                if (targerDatabaseType.equals("Hive2"))
                {
                    throw new Exception("Hive不支持该操作。");
                }
            }
            operation.equals("3");
            if (operation.equals("4"))
            {
                if (targerDatabaseType.equals("MySql"))
                {
                    this.permissionService.deleteByDataListForMySQL(dataList, targetDataBase, targetTable, targetConfigId, qualification);
                }
                if (targerDatabaseType.equals("MariaDB"))
                {
                    this.permissionService.deleteByDataListForMySQL(dataList, targetDataBase, targetTable, targetConfigId, qualification);
                }
                if (targerDatabaseType.equals("Oracle"))
                {
                    this.permissionService.deleteByDataListForOracle(dataList, targetDataBase, targetTable, targetConfigId, qualification);
                }
                if (targerDatabaseType.equals("PostgreSQL"))
                {
                    this.permissionService.deleteByDataListForOracle(dataList, targetDataBase, targetTable, targetConfigId, qualification);
                }
                if (targerDatabaseType.equals("MSSQL"))
                {
                    this.permissionService.deleteByDataListForMSSQL(dataList, targetDataBase, targetTable, targetConfigId, qualification);
                }
                if (targerDatabaseType.equals("MongoDB"))
                {
                    this.permissionService.deleteByDataListForMongoDB(dataList, targetDataBase, targetTable, targetConfigId, qualification);
                }
                if (targerDatabaseType.equals("Hive2"))
                {
                    throw new Exception("Hive不支持该操作。");
                }
            }
            this.permissionService.dataSynchronizeLogSave("1", "运行成功!", dataSynchronizeId);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            this.permissionService.dataSynchronizeLogSave("0", "运行失败!" + e.getMessage(), dataSynchronizeId);
        }
        logger.info("数据交换任务运行结束-------- end --------");
    }
    
    public void setPermissionService(PermissionService permissionService)
    {
        this.permissionService = permissionService;
    }
}
