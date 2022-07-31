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
import org.springframework.base.system.utils.DataUtil;
import org.springframework.base.system.utils.DateUtils;
import org.springframework.base.system.utils.LogUtil;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/dao/BackDbForPostgreSQL.class */
public class BackDbForPostgreSQL {
    private FileOutputStream fos;
    private BufferedOutputStream bos;
    private File fileName = null;
    private String fileAllPath = "";
    private String version = "";
    Map map = new HashMap();

    public void readDataToFile(String databaseName, String tableName, String path, String databaseConfigId) throws Exception {
        long totalStart = System.currentTimeMillis();
        DBUtil db0 = new DBUtil();
        String sql = "  select id, name, database_type as databaseType , database_name as databaseName, user_name as userName,  password, port, ip ,url ,is_default as isDefault from  treesoft_config where id='" + databaseConfigId + "'";
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
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            List<Map<String, Object>> versions = db.queryForListCommonMethod(" select version() ");
            if (versions.size() > 0) {
                this.version = (String) versions.get(0).get("version");
            }
            StringBuffer sb = new StringBuffer();
            sb.append("/* \r\n");
            sb.append("TreeSoft Data Transfer For PostgreSQL \r\n");
            sb.append(this.version + " \r\n");
            sb.append("Source Server         : " + ip + " \r\n");
            sb.append("Source Host           : " + ip + ":" + port + "\r\n");
            sb.append("Source Database       : " + databaseName + " \r\n");
            sb.append("web site: www.treesoft.cn \r\n");
            sb.append("Date: " + DateUtils.getDateTime() + " \r\n");
            sb.append("*/ \r\n");
            sb.append(" \r\n");
            createFile(sb.toString());
            PostgreSQLDao pdao = new PostgreSQLDao();
            List<Map<String, Object>> tableList = new ArrayList<>();
            if (tableName.equals("")) {
                tableList = pdao.getAllTablesForPostgreSQL(databaseName, databaseConfigId);
            } else {
                Map<String, Object> map = new HashMap<>();
                map.put("TABLE_NAME", tableName);
                tableList.add(map);
            }
            int num = 0;
            Iterator<Map<String, Object>> it = tableList.iterator();
            while (it.hasNext()) {
                String table_name = (String) it.next().get("TABLE_NAME");
                StringBuffer sb2 = new StringBuffer();
                sb2.append("-- -------------------------------- \r\n");
                sb2.append("-- Table structure for \"" + table_name + "\"\r\n");
                sb2.append("-- -------------------------------- \r\n");
                sb2.append("DROP TABLE IF EXISTS  \"" + table_name + "\"; \r\n");
                sb2.append("CREATE TABLE \"" + table_name + "\" ( \r\n");
                String primary_key_list = "";
                String tableColumnStr = "";
                Map<String, String> TableColumnType = new HashMap<>();
                List<Map<String, Object>> listTableColumn = pdao.getTableColumns3ForPostgreSQL(databaseName, table_name, databaseConfigId);
                Iterator<Map<String, Object>> it2 = listTableColumn.iterator();
                while (it2.hasNext()) {
                    Map<String, Object> map3 = it2.next();
                    TableColumnType.put((String) map3.get("COLUMN_NAME"), (String) map3.get("DATA_TYPE"));
                    sb2.append("  " + map3.get("COLUMN_NAME") + " " + map3.get("COLUMN_TYPE"));
                    tableColumnStr = String.valueOf(tableColumnStr) + "\"" + map3.get("COLUMN_NAME") + "\",";
                    if (map3.get("COLUMN_KEY") != null && map3.get("COLUMN_KEY").equals("PRI")) {
                        primary_key_list = String.valueOf(primary_key_list) + map3.get("COLUMN_NAME") + ",";
                    }
                    if (map3.get("IS_NULLABLE").equals("NO")) {
                        sb2.append(" NOT NULL ");
                    }
                    sb2.append(",\r\n");
                }
                if ("".equals(primary_key_list)) {
                    sb2.delete(sb2.length() - 3, sb2.length() - 1);
                    sb2.append("  \r\n");
                } else {
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
                for (int yy = 0; yy < rowCount; yy += 20000) {
                    String sql3 = "select  " + tableColumnStr.substring(0, tableColumnStr.length() - 1) + "  from  \"" + table_name + "\"  LIMIT 20000 OFFSET  " + limitFrom;
                    List<Map<String, Object>> list = db.queryForListForPostgreSQL(sql3);
                    limitFrom += 20000;
                    Iterator<Map<String, Object>> it3 = list.iterator();
                    while (it3.hasNext()) {
                        Map<String, Object> map4 = it3.next();
                        StringBuffer sb3 = new StringBuffer();
                        sb3.append("INSERT INTO \"" + table_name + "\" (" + tableColumnStr.substring(0, tableColumnStr.length() - 1) + ")  VALUES (");
                        String values = "";
                        Iterator<Map.Entry<String, Object>> it4 = map4.entrySet().iterator();
                        while (it4.hasNext()) {
                            Map.Entry<String, Object> entry = it4.next();
                            String key = entry.getKey();
                            if (entry.getValue() == null) {
                                values = String.valueOf(values) + "null,";
                            } else if (TableColumnType.get(key).equals("date") || TableColumnType.get(key).equals("datetime") || TableColumnType.get(key).equals("timestamp")) {
                                values = String.valueOf(values) + "to_date( '" + entry.getValue() + "','YYYY-MM-DD HH24:MI:SS'),";
                            } else if (TableColumnType.get(key).equals("int4") || TableColumnType.get(key).equals("smallint") || TableColumnType.get(key).equals("tinyint") || TableColumnType.get(key).equals("integer") || TableColumnType.get(key).equals("bit") || TableColumnType.get(key).equals("real") || TableColumnType.get(key).equals("bigint") || TableColumnType.get(key).equals("long") || TableColumnType.get(key).equals("float4") || TableColumnType.get(key).equals("decimal") || TableColumnType.get(key).equals("numeric") || TableColumnType.get(key).equals("mediumint")) {
                                values = String.valueOf(values) + entry.getValue() + ",";
                            } else if (TableColumnType.get(key).equals("binary") || TableColumnType.get(key).equals("varbinary") || TableColumnType.get(key).equals("blob") || TableColumnType.get(key).equals("tinyblob") || TableColumnType.get(key).equals("mediumblob") || TableColumnType.get(key).equals("longblob")) {
                                byte[] ss = (byte[]) entry.getValue();
                                if (ss.length == 0) {
                                    values = String.valueOf(values) + "null,";
                                } else {
                                    values = String.valueOf(values) + "0x" + DataUtil.bytesToHexString(ss) + ",";
                                }
                            } else {
                                String tempValues = (String) entry.getValue();
                                values = String.valueOf(values) + "'" + tempValues.replace("\\", "\\\\").replace("'", "\\'").replace("\r\n", "\\r\\n").replace("\n\r", "\\n\\r").replace("\r", "\\r").replace("\n", "\\n") + "',";
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
                            } catch (IOException e3) {
                                e3.printStackTrace();
                            }
                        }
                    }
                }
                if (num >= 50000) {
                    System.out.println("===============清缓存一次" + num + "===========");
                    try {
                        num = 0;
                        this.bos.flush();
                    } catch (IOException e4) {
                        e4.printStackTrace();
                    }
                }
            }
            try {
                this.bos.flush();
                this.bos.close();
                this.fos.close();
            } catch (IOException iox) {
                System.err.println(iox);
            }
            long totalEnd = System.currentTimeMillis();
            LogUtil.i("----备份总耗时：" + (totalEnd - totalStart) + "毫秒," + DateUtils.getDateTime());
        } catch (Throwable th) {
            try {
                this.bos.flush();
                this.bos.close();
                this.fos.close();
            } catch (IOException iox2) {
                System.err.println(iox2);
            }
            throw th;
        }
    }

    public void createFile(String s) {
        try {
            this.bos.write(s.getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
