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

public class BackDbForPostgreSQL extends AbstractBackDb
{
    private static final Logger logger = LoggerFactory.getLogger(BackDbForPostgreSQL.class);
    
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
            List<Map<String, Object>> versions = db.queryForList(" select version() ");
            if (!versions.isEmpty())
            {
                version = ((String)(versions.get(0)).get("version"));
            }
            StringBuilder sb = new StringBuilder();
            sb.append("/* \r\n");
            sb.append("TreeSoft Data Transfer For PostgreSQL \r\n");
            sb.append(version + " \r\n");
            sb.append("Source Server         : " + ip + " \r\n");
            sb.append("Source Host           : " + ip + ":" + port + "\r\n");
            sb.append("Source Database       : " + databaseName + " \r\n");
            sb.append("web site: www.treesoft.cn \r\n");
            sb.append("Date: " + DateUtil.getDateTime() + " \r\n");
            sb.append("*/ \r\n");
            sb.append(" \r\n");
            createFile(sb.toString());
            PermissionDao pdao = new PermissionDao();
            List<Map<String, Object>> tableList = new ArrayList<>();
            if (tableName.equals(""))
            {
                tableList = pdao.getAllTablesForPostgreSQL(databaseName, databaseConfigId);
            }
            else
            {
                Map<String, Object> map = new HashMap<>();
                map.put("TABLE_NAME", tableName);
                tableList.add(map);
            }
            int num = 0;
            for (Map<String, Object> map : tableList)
            {
                String table_name = (String)map.get("TABLE_NAME");
                StringBuilder sb2 = new StringBuilder();
                sb2.append("-- -------------------------------- \r\n");
                sb2.append("-- Table structure for \"" + table_name + "\"\r\n");
                sb2.append("-- -------------------------------- \r\n");
                sb2.append("DROP TABLE IF EXISTS  \"" + table_name + "\"; \r\n");
                sb2.append("CREATE TABLE \"" + table_name + "\" ( \r\n");
                String primary_key_list = "";
                String tableColumnStr = "";
                Map<String, String> TableColumnType = new HashMap<>();
                List<Map<String, Object>> listTableColumn = pdao.getTableColumns3ForPostgreSQL(databaseName, table_name, databaseConfigId);
                for (Map<String, Object> map3 : listTableColumn)
                {
                    TableColumnType.put((String)map3.get("COLUMN_NAME"), (String)map3.get("DATA_TYPE"));
                    sb2.append("  " + map3.get("COLUMN_NAME") + " " + map3.get("COLUMN_TYPE"));
                    tableColumnStr = tableColumnStr + "\"" + map3.get("COLUMN_NAME") + "\",";
                    if ((map3.get("COLUMN_KEY") != null) && (map3.get("COLUMN_KEY").equals("PRI")))
                    {
                        primary_key_list = primary_key_list + map3.get("COLUMN_NAME") + ",";
                    }
                    if (map3.get("IS_NULLABLE").equals("NO"))
                    {
                        sb2.append(" NOT NULL ");
                    }
                    sb2.append(",\r\n");
                }
                if ("".equals(primary_key_list))
                {
                    sb2.delete(sb2.length() - 3, sb2.length() - 1);
                    sb2.append("  \r\n");
                }
                else
                {
                    sb2.append("PRIMARY KEY (" + primary_key_list.substring(0, primary_key_list.length() - 1) + " )  \r\n");
                }
                sb2.append("); \r\n ");
                sb2.append("  \r\n");
                createFile(sb2.toString());
                num++;
                String sql1 = "select count(*) as count from   \"" + table_name + "\"";
                int rowCount = db.executeQueryForCountForPostgesSQL(sql1);
                createFile("-- ---------------------------- \r\n");
                createFile("-- Records of " + table_name + ", Total rows: " + rowCount + " \r\n");
                createFile("-- ---------------------------- \r\n");
                int limitFrom = 0;
                int pageSize = 20000;
                for (int yy = 0; yy < rowCount; yy += 20000)
                {
                    String sql3 = "select  " + tableColumnStr.substring(0, tableColumnStr.length() - 1) + "  from  \"" + table_name + "\"  LIMIT " + pageSize + " OFFSET  " + limitFrom;
                    List<Map<String, Object>> list = db.queryForList(sql3);
                    String key = "";
                    String values = "";
                    String tempValues = "";
                    limitFrom += 20000;
                    for (Map<String, Object> map4 : list)
                    {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("INSERT INTO \"" + table_name + "\" (" + tableColumnStr.substring(0, tableColumnStr.length() - 1) + ")  VALUES (");
                        values = "";
                        for (Map.Entry<String, Object> entry : map4.entrySet())
                        {
                            key = entry.getKey();
                            if (entry.getValue() == null)
                            {
                                values = values + "null,";
                            }
                            else if ((TableColumnType.get(key).equals("date")) || (TableColumnType.get(key).equals("datetime")) || (TableColumnType.get(key).equals("timestamp")))
                            {
                                values = values + "to_date( '" + entry.getValue() + "','YYYY-MM-DD HH24:MI:SS'),";
                            }
                            else if ((TableColumnType.get(key).equals("int4")) || (TableColumnType.get(key).equals("smallint")) || (TableColumnType.get(key).equals("tinyint"))
                                || (TableColumnType.get(key).equals("integer")) || (TableColumnType.get(key).equals("bit")) || (TableColumnType.get(key).equals("real"))
                                || (TableColumnType.get(key).equals("bigint")) || (TableColumnType.get(key).equals("long")) || (TableColumnType.get(key).equals("float4"))
                                || (TableColumnType.get(key).equals("decimal")) || (TableColumnType.get(key).equals("numeric")) || (TableColumnType.get(key).equals("mediumint")))
                            {
                                values = values + entry.getValue() + ",";
                            }
                            else if ((TableColumnType.get(key).equals("binary")) || (TableColumnType.get(key).equals("varbinary")) || (TableColumnType.get(key).equals("blob"))
                                || (TableColumnType.get(key).equals("tinyblob")) || (TableColumnType.get(key).equals("mediumblob")) || (TableColumnType.get(key).equals("longblob")))
                            {
                                byte[] ss = (byte[])entry.getValue();
                                if (ss.length == 0)
                                {
                                    values = values + "null,";
                                }
                                else
                                {
                                    values = values + "0x" + bytesToHexString(ss) + ",";
                                }
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
