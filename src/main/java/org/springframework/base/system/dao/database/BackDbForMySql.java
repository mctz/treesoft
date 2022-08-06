package org.springframework.base.system.dao.database;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.base.common.utils.DateUtil;
import org.springframework.base.system.core.BusiDataBaseUtil;
import org.springframework.base.system.core.SysDataBaseUtil;
import org.springframework.base.system.dao.PermissionDao;
import org.springframework.util.StopWatch;

public class BackDbForMySql extends AbstractBackDb
{
    private static final Logger logger = LoggerFactory.getLogger(BackDbForMySql.class);
    
    @Override
    public void readDataToFile(String databaseName, String tableName, String path, String databaseConfigId)
        throws Exception
    {
        StopWatch clock = new StopWatch();
        clock.start();
        String sql = "select ip, port from dms_config where id=?";
        Map<String, Object> map0 = new SysDataBaseUtil().queryForMap(sql, databaseConfigId);
        String ip = (String)map0.get("ip");
        String port = (String)map0.get("port");
        BusiDataBaseUtil db = new BusiDataBaseUtil(databaseName, databaseConfigId);
        String nowDateStr = DateUtil.getDateTimeString();
        fileAllPath = (path + "backup" + File.separator + databaseName + "_" + tableName + "_" + nowDateStr + ".sql");
        fileName = new File(fileAllPath);
        try
        {
            fos = new FileOutputStream(fileName);
            bos = new BufferedOutputStream(fos);
            List<Map<String, Object>> versions = db.queryForList(" select version() as version ");
            if (!versions.isEmpty())
            {
                version = ((String)(versions.get(0)).get("version"));
            }
            StringBuilder sb = new StringBuilder();
            sb.append("/* \r\n");
            sb.append("TreeSoft Data Transfer For MySQL \r\n");
            sb.append("Source Server         : " + ip + " \r\n");
            sb.append("Source Server Version : " + version + " \r\n");
            sb.append("Source Host           : " + ip + ":" + port + "\r\n");
            sb.append("Source Database       : " + databaseName + " \r\n");
            sb.append("web site: www.treesoft.cn \r\n");
            sb.append("Date: " + DateUtil.getDateTime() + " \r\n");
            sb.append("*/ \r\n");
            sb.append(" \r\n");
            sb.append("SET FOREIGN_KEY_CHECKS=0; ");
            createFile(sb.toString());
            PermissionDao pdao = new PermissionDao();
            List<Map<String, Object>> tableList = new ArrayList<>();
            if (tableName.equals(""))
            {
                tableList = pdao.getAllTables2(databaseName, "", databaseConfigId);
            }
            else
            {
                tableList = pdao.getAllTables2(databaseName, tableName, databaseConfigId);
            }
            String createTableSQL = "";
            int num = 0;
            List<Map<String, Object>> list4;
            Map<String, String> TableColumnType;
            for (Map<String, Object> map : tableList)
            {
                String table_name = (String)map.get("TABLE_NAME");
                list4 = db.queryForList("show create table `" + table_name + "`");
                if (list4.size() > 0)
                {
                    Map<String, Object> mapp = list4.get(0);
                    createTableSQL = (String)mapp.get("Create Table");
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append(" \r\n");
                sb2.append("-- -------------------------------- \r\n");
                sb2.append("-- Table structure for `" + table_name + "`\r\n");
                sb2.append("-- -------------------------------- \r\n");
                sb2.append("DROP TABLE IF EXISTS `" + table_name + "`; \r\n");
                sb2.append(createTableSQL);
                sb2.append(";\r\n");
                TableColumnType = new HashMap<>();
                List<Map<String, Object>> listTableColumn = pdao.getTableColumns3(databaseName, table_name, databaseConfigId);
                for (Map<String, Object> map3 : listTableColumn)
                {
                    TableColumnType.put((String)map3.get("COLUMN_NAME"), (String)map3.get("DATA_TYPE"));
                }
                createFile(sb2.toString());
                num++;
                String sql1 = "select count(*) from   `" + databaseName + "`.`" + table_name + "`";
                int rowCount = db.executeQueryForCount(sql1);
                createFile("-- ---------------------------- \r\n");
                createFile("-- Records of " + table_name + ", Total rows: " + rowCount + " \r\n");
                createFile("-- ---------------------------- \r\n");
                int limitFrom = 0;
                int pageSize = 20000;
                for (int yy = 0; yy < rowCount; yy += 20000)
                {
                    String sql3 = "select  *  from  `" + databaseName + "`.`" + table_name + "`  LIMIT " + limitFrom + "," + pageSize;
                    List<Map<String, Object>> list = db.queryForList(sql3);
                    String key = "";
                    String values = "";
                    String tempValues = "";
                    limitFrom += 20000;
                    for (Map<String, Object> map4 : list)
                    {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("INSERT INTO `" + table_name + "` VALUES (");
                        values = "";
                        for (Map.Entry<String, Object> entry : map4.entrySet())
                        {
                            key = entry.getKey();
                            if (entry.getValue() == null)
                            {
                                values = values + "null,";
                            }
                            else if ((TableColumnType.get(key).equals("int")) || (TableColumnType.get(key).equals("smallint")) || (TableColumnType.get(key).equals("tinyint"))
                                || (TableColumnType.get(key).equals("integer")) || (TableColumnType.get(key).equals("bit")) || (TableColumnType.get(key).equals("bigint"))
                                || (TableColumnType.get(key).equals("double")) || (TableColumnType.get(key).equals("float")) || (TableColumnType.get(key).equals("decimal"))
                                || (TableColumnType.get(key).equals("numeric")) || (TableColumnType.get(key).equals("mediumint")))
                            {
                                values = values + entry.getValue() + ",";
                            }
                            else if ((TableColumnType.get(key).equals("binary")) || (TableColumnType.get(key).equals("varbinary")))
                            {
                                values = values + "'" + entry.getValue() + "',";
                            }
                            else if ((TableColumnType.get(key).equals("blob")) || (TableColumnType.get(key).equals("tinyblob")) || (TableColumnType.get(key).equals("mediumblob"))
                                || (TableColumnType.get(key).equals("longblob")))
                            {
                                values = values + "'" + entry.getValue() + "',";
                            }
                            else
                            {
                                tempValues = (String)entry.getValue();
                                tempValues = tempValues.replace("'", "\\'");
                                tempValues = tempValues.replace("\\", "\\\\");
                                tempValues = tempValues.replace("\r\n", "\\r\\n");
                                tempValues = tempValues.replace("\n\r", "\\n\\r");
                                tempValues = tempValues.replace("\r", "\\r");
                                tempValues = tempValues.replace("\n", "\\n");
                                values = values + "'" + tempValues + "',";
                            }
                        }
                        sb3.append(values.substring(0, values.length() - 1));
                        sb3.append(" ); \r\n");
                        createFile(sb3.toString());
                        num++;
                        if (num >= 50000)
                        {
                            try
                            {
                                num = 0;
                                bos.flush();
                            }
                            catch (IOException e)
                            {
                                logger.error(e.getMessage(), e);
                            }
                        }
                    }
                }
                if (num >= 50000)
                {
                    logger.info("===============清缓存一次 {} ===========", num);
                    try
                    {
                        num = 0;
                        bos.flush();
                    }
                    catch (IOException e)
                    {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
            if (tableName.equals(""))
            {
                sb = new StringBuilder();
                List<Map<String, Object>> viewsList = pdao.getAllViews(databaseName, databaseConfigId);
                String viewName = "";
                for (Map<String, Object> map : viewsList)
                {
                    viewName = (String)map.get("TABLE_NAME");
                    sb.append("-- ---------------------------- \r\n");
                    sb.append("-- View structure for `" + viewName + "` \r\n");
                    sb.append("-- ---------------------------- \r\n");
                    sb.append("DROP VIEW IF EXISTS `" + viewName + "`; \r\n");
                    sb.append("CREATE VIEW `" + viewName + "` AS " + pdao.getViewSql(databaseName, viewName, databaseConfigId) + "; \r\n");
                }
                createFile(sb.toString());
                sb = new StringBuilder();
                List<Map<String, Object>> procsList = pdao.getAllFuntion(databaseName, databaseConfigId);
                for (Map<String, Object> map : procsList)
                {
                    String proc_name = (String)map.get("ROUTINE_NAME");
                    sb.append("-- ---------------------------- \r\n");
                    sb.append("-- Procedure structure for `" + proc_name + "` \r\n");
                    sb.append("-- ---------------------------- \r\n");
                    sb.append("DROP PROCEDURE IF EXISTS `" + proc_name + "`; \r\n");
                    sb.append("DELIMITER ;;\r\n");
                    sb.append(pdao.getProcSqlForSQL(databaseName, proc_name, databaseConfigId) + " ;;\r\n");
                    sb.append("DELIMITER ;\r\n");
                }
                createFile(sb.toString());
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw e;
        }
        finally
        {
            try
            {
                bos.flush();
                bos.close();
                fos.close();
            }
            catch (IOException iox)
            {
                logger.error(iox.getMessage(), iox);
            }
        }
        clock.stop();
        logger.info("----备份总耗时：{} 毫秒,", clock.getTotalTimeMillis());
    }
}
