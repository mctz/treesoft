package org.springframework.base.system.dao;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.base.system.dbUtils.DBUtil;
import org.springframework.base.system.dbUtils.DBUtil2;
import org.springframework.base.system.utils.DateUtils;
import org.springframework.base.system.utils.LogUtil;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/dao/BackDbForMySql.class */
public class BackDbForMySql {
    private FileOutputStream fos;
    private BufferedOutputStream bos;
    private File fileName = null;
    private String fileAllPath = "";
    private String version = "";
    Map map = new HashMap();

    public void readDataToFile(String databaseName, String tableName, String path, String databaseConfigId) throws Exception {
        List<Map<String, Object>> tableList;
        long totalStart = System.currentTimeMillis();
        DBUtil db0 = new DBUtil();
        String sql = " select id, name, database_type as databaseType , database_name as databaseName, user_name as userName,  password, port, ip ,url ,is_default as isDefault from  treesoft_config where id='" + databaseConfigId + "'";
        List<Map<String, Object>> list0 = db0.executeQuery(sql);
        Map<String, Object> map0 = list0.get(0);
        String ip = new StringBuilder().append(map0.get("ip")).toString();
        String port = new StringBuilder().append(map0.get("port")).toString();
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        new Date();
        String nowDateStr = DateUtils.getDateTimeString(new Date());
        this.fileAllPath = String.valueOf(path) + File.separator + databaseName + "@" + tableName + "_" + nowDateStr + ".sql";
        this.fileName = new File(this.fileAllPath);
        try {
            try {
                try {
                    this.fos = new FileOutputStream(this.fileName);
                    this.bos = new BufferedOutputStream(this.fos);
                } catch (Throwable th) {
                    try {
                        this.bos.flush();
                        this.bos.close();
                        this.fos.close();
                    } catch (IOException iox) {
                        System.err.println(iox);
                    }
                    throw th;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            List<Map<String, Object>> versions = db.queryForListCommonMethod(" select version() as version ");
            if (versions.size() > 0) {
                this.version = (String) versions.get(0).get("version");
            }
            StringBuffer sb = new StringBuffer();
            sb.append("/* \r\n");
            sb.append("TreeSoft Data Transfer For MySQL \r\n");
            sb.append("Source Server         : " + ip + " \r\n");
            sb.append("Source Server Version : " + this.version + " \r\n");
            sb.append("Source Host           : " + ip + ":" + port + "\r\n");
            sb.append("Source Database       : " + databaseName + " \r\n");
            sb.append("web site: www.treesoft.cn \r\n");
            sb.append("Date: " + DateUtils.getDateTime() + " \r\n");
            sb.append("*/ \r\n");
            sb.append(" \r\n");
            sb.append("SET FOREIGN_KEY_CHECKS=0; ");
            createFile(sb.toString());
            MysqlDao pdao = new MysqlDao();
            new ArrayList();
            if (tableName.equals("")) {
                tableList = pdao.getAllTables2(databaseName, "", databaseConfigId);
            } else {
                tableList = pdao.getAllTables2(databaseName, tableName, databaseConfigId);
            }
            String createTableSQL = "";
            int num = 0;
            Iterator<Map<String, Object>> it = tableList.iterator();
            while (it.hasNext()) {
                Map<String, Object> map = it.next();
                String table_name = (String) map.get("TABLE_NAME");
                List<Map<String, Object>> list4 = db.queryForListCommonMethod("show create table `" + table_name + "`");
                if (list4.size() > 0) {
                    Map<String, Object> mapp = list4.get(0);
                    createTableSQL = (String) mapp.get("Create Table");
                }
                StringBuffer sb2 = new StringBuffer();
                sb2.append(" \r\n");
                sb2.append("-- -------------------------------- \r\n");
                sb2.append("-- Table structure for `" + table_name + "`\r\n");
                sb2.append("-- -------------------------------- \r\n");
                sb2.append("DROP TABLE IF EXISTS `" + table_name + "`; \r\n");
                sb2.append(createTableSQL);
                sb2.append(";\r\n");
                Map<String, String> TableColumnType = new HashMap<>();
                List<Map<String, Object>> listTableColumn = pdao.getTableColumns3(databaseName, table_name, databaseConfigId);
                Iterator<Map<String, Object>> it2 = listTableColumn.iterator();
                while (it2.hasNext()) {
                    Map<String, Object> map3 = it2.next();
                    TableColumnType.put((String) map3.get("COLUMN_NAME"), (String) map3.get("DATA_TYPE"));
                }
                createFile(sb2.toString());
                num++;
                String sql1 = "select count(*) from   `" + databaseName + "`.`" + table_name + "`";
                int rowCount = db.executeQueryForCount(sql1);
                createFile("-- ---------------------------- \r\n");
                createFile("-- Records of " + table_name + ", Total rows: " + rowCount + " \r\n");
                createFile("-- ---------------------------- \r\n");
                int limitFrom = 0;
                for (int yy = 0; yy < rowCount; yy += 20000) {
                    String sql3 = "select  *  from  `" + table_name + "`";
                    List<Map<String, Object>> list = db.queryForListForMySqlForExport(sql3, limitFrom, 20000);
                    limitFrom += 20000;
                    Iterator<Map<String, Object>> it3 = list.iterator();
                    while (it3.hasNext()) {
                        Map<String, Object> map4 = it3.next();
                        StringBuffer sb3 = new StringBuffer();
                        sb3.append("INSERT INTO `" + table_name + "` VALUES (");
                        String values = "";
                        Iterator<Map.Entry<String, Object>> it4 = map4.entrySet().iterator();
                        while (it4.hasNext()) {
                            Map.Entry<String, Object> entry = it4.next();
                            String key = entry.getKey();
                            if (entry.getValue() == null) {
                                values = String.valueOf(values) + "null,";
                            } else if (TableColumnType.get(key).equals("int") || TableColumnType.get(key).equals("smallint") || TableColumnType.get(key).equals("tinyint") || TableColumnType.get(key).equals("integer") || TableColumnType.get(key).equals("bit") || TableColumnType.get(key).equals("bigint") || TableColumnType.get(key).equals("double") || TableColumnType.get(key).equals("float") || TableColumnType.get(key).equals("decimal") || TableColumnType.get(key).equals("numeric") || TableColumnType.get(key).equals("mediumint")) {
                                values = String.valueOf(values) + entry.getValue() + ",";
                            } else if (TableColumnType.get(key).equals("binary") || TableColumnType.get(key).equals("varbinary")) {
                                values = String.valueOf(values) + "'" + entry.getValue() + "',";
                            } else if (TableColumnType.get(key).equals("blob") || TableColumnType.get(key).equals("tinyblob") || TableColumnType.get(key).equals("mediumblob") || TableColumnType.get(key).equals("longblob")) {
                                values = String.valueOf(values) + entry.getValue() + ",";
                            } else if (TableColumnType.get(key).equals("date") || TableColumnType.get(key).equals("time")) {
                                String tempValues = new StringBuilder().append(entry.getValue()).toString();
                                values = String.valueOf(values) + "'" + tempValues + "',";
                            } else {
                                String tempValues2 = entry.getValue().toString();
                                values = String.valueOf(values) + "'" + tempValues2.replace("\\", "\\\\").replace("'", "\\'").replace("\r\n", "\\r\\n").replace("\n\r", "\\n\\r").replace("\r", "\\r").replace("\n", "\\n") + "',";
                            }
                        }
                        sb3.append(values.substring(0, values.length() - 1));
                        sb3.append(" ); \r\n");
                        createFile(sb3.toString());
                        num++;
                        if (num >= 50000) {
                            try {
                                num = 0;
                                this.bos.flush();
                            } catch (IOException e2) {
                                e2.printStackTrace();
                            }
                        }
                    }
                }
                if (num >= 50000) {
                    System.out.println("===============清缓存一次" + num + "===========");
                    try {
                        num = 0;
                        this.bos.flush();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                }
            }
            if (tableName.equals("")) {
                StringBuffer sb4 = new StringBuffer();
                List<Map<String, Object>> viewsList = pdao.getAllViews(databaseName, databaseConfigId);
                Iterator<Map<String, Object>> it5 = viewsList.iterator();
                while (it5.hasNext()) {
                    Map<String, Object> map2 = it5.next();
                    String viewName = (String) map2.get("TABLE_NAME");
                    sb4.append("-- ---------------------------- \r\n");
                    sb4.append("-- View structure for `" + viewName + "` \r\n");
                    sb4.append("-- ---------------------------- \r\n");
                    sb4.append("DROP VIEW IF EXISTS `" + viewName + "`; \r\n");
                    sb4.append("CREATE VIEW `" + viewName + "` AS " + pdao.getViewSql(databaseName, viewName, databaseConfigId) + "; \r\n");
                }
                createFile(sb4.toString());
                StringBuffer sb5 = new StringBuffer();
                List<Map<String, Object>> procsList = pdao.getAllFuntion(databaseName, databaseConfigId);
                Iterator<Map<String, Object>> it6 = procsList.iterator();
                while (it6.hasNext()) {
                    Map<String, Object> map5 = it6.next();
                    String proc_name = (String) map5.get("ROUTINE_NAME");
                    sb5.append("-- ---------------------------- \r\n");
                    sb5.append("-- Procedure structure for `" + proc_name + "` \r\n");
                    sb5.append("-- ---------------------------- \r\n");
                    sb5.append("DROP PROCEDURE IF EXISTS `" + proc_name + "`; \r\n");
                    sb5.append("DELIMITER ;;\r\n");
                    sb5.append(String.valueOf(pdao.getProcSqlForSQL(databaseName, proc_name, databaseConfigId)) + " ;;\r\n");
                    sb5.append("DELIMITER ;\r\n");
                }
                createFile(sb5.toString());
            }
            try {
                this.bos.flush();
                this.bos.close();
                this.fos.close();
            } catch (IOException iox2) {
                System.err.println(iox2);
            }
        } catch (Exception e4) {
            e4.printStackTrace();
            try {
                this.bos.flush();
                this.bos.close();
                this.fos.close();
            } catch (IOException iox3) {
                System.err.println(iox3);
            }
        }
        long totalEnd = System.currentTimeMillis();
        LogUtil.i("----备份总耗时：" + (totalEnd - totalStart) + "毫秒," + DateUtils.getDateTime());
    }

    public void createFile(String s) {
        try {
            this.bos.write(s.getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
