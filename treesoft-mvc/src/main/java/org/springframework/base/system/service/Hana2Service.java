package org.springframework.base.system.service;

import com.alibaba.fastjson.JSONArray;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.base.system.dao.Hana2Dao;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.utils.DataUtil;
import org.springframework.base.system.utils.DateUtils;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/service/Hana2Service.class */
public class Hana2Service {
    @Autowired
    private Hana2Dao hana2Dao;

    public List<Map<String, Object>> getAllDataBaseForHana2(String databaseConfigId) throws Exception {
        return this.hana2Dao.getAllDataBaseForHana2(databaseConfigId);
    }

    public List<Map<String, Object>> getAllTablesForHana2(String dbName, String databaseConfigId) throws Exception {
        return this.hana2Dao.getAllTablesForHana2(dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getTableColumns3ForHana2(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.hana2Dao.getTableColumns3ForHana2(databaseName, tableName, databaseConfigId);
    }

    public String getViewSqlForHana2(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.hana2Dao.getViewSqlForHana2(databaseName, tableName, databaseConfigId);
    }

    public List<Map<String, Object>> getAllViewsForHana2(String dbName, String databaseConfigId) throws Exception {
        return this.hana2Dao.getAllViewsForHana2(dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getAllFuntionForHana2(String dbName, String databaseConfigId) throws Exception {
        return new ArrayList();
    }

    public Page<Map<String, Object>> getDataForHana2(Page<Map<String, Object>> page, String tableName, String dbName, String databaseConfigId) throws Exception {
        return this.hana2Dao.getDataForHana2(page, tableName, dbName, databaseConfigId);
    }

    public int updateRowsNewForHana2(String databaseName, String tableName, List<String> strList, String databaseConfigId) throws Exception {
        Iterator<String> it = strList.iterator();
        while (it.hasNext()) {
            String str1 = it.next();
            if (str1 == null || "".equals(str1)) {
                throw new Exception("数据不完整,保存失败!");
            }
            String sql = " update top 1 " + tableName + str1;
            LogUtil.i(String.valueOf(DateUtils.getDateTime()) + " update语句 =" + sql);
            this.hana2Dao.executeSqlNotRes(sql, databaseName, databaseConfigId);
        }
        return 0;
    }

    public boolean copyTableForHana2(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.hana2Dao.copyTableForHana2(databaseName, tableName, databaseConfigId);
    }

    public int updateTableNullAbleForHana2(String databaseName, String tableName, String column_name, String is_nullable, String databaseConfigId) throws Exception {
        return this.hana2Dao.updateTableNullAbleForHana2(databaseName, tableName, column_name, is_nullable, databaseConfigId);
    }

    public int savePrimaryKeyForHana2(String databaseName, String tableName, String column_name, String column_key, String databaseConfigId) throws Exception {
        return this.hana2Dao.savePrimaryKeyForHana2(databaseName, tableName, column_name, column_key, databaseConfigId);
    }

    public int saveDesginColumnForHana2(Map map, String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.hana2Dao.saveDesginColumnForHana2(map, databaseName, tableName, databaseConfigId);
    }

    @Transactional
    public int updateTableColumnForHana2(String updated, String databaseName, String tableName, String databaseConfigId) throws Exception {
        if (updated != null) {
            JSONArray updateArray = JSONArray.parseArray(updated);
            for (int i = 0; i < updateArray.size(); i++) {
                Map<String, Object> map1 = (Map) updateArray.get(i);
                Map<String, Object> maps = new HashMap<>();
                Iterator<String> it = map1.keySet().iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    maps.put(key, map1.get(key));
                }
                String idValues = new StringBuilder().append(maps.get("TREESOFTPRIMARYKEY")).toString();
                this.hana2Dao.updateTableColumnForHana2(maps, databaseName, tableName, "column_name", idValues, databaseConfigId);
            }
            return 0;
        }
        return 0;
    }

    public int deleteTableColumnForHana2(String databaseName, String tableName, String[] ids, String databaseConfigId) throws Exception {
        return this.hana2Dao.deleteTableColumnForHana2(databaseName, tableName, ids, databaseConfigId);
    }

    public int saveRowsForHana2(Map map, String databaseName, String tableName, String databaseConfigId, String username, String ip) throws Exception {
        return this.hana2Dao.saveRowsForHana2(map, databaseName, tableName, databaseConfigId, username, ip);
    }

    public int saveNewTableForHana2(JSONArray insertArray, String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.hana2Dao.saveNewTableForHana2(insertArray, databaseName, tableName, databaseConfigId);
    }

    public String selectColumnTypeForHana2(String databaseName, String tableName, String column, String databaseConfigId) throws Exception {
        return this.hana2Dao.selectColumnTypeForHana2(databaseName, tableName, column, databaseConfigId);
    }

    public boolean renameTableForHana2(String databaseName, String tableName, String databaseConfigId, String newTableName) throws Exception {
        return this.hana2Dao.renameTableForHana2(databaseName, tableName, databaseConfigId, newTableName);
    }

    public int deleteRowsNewForHana2(String databaseName, String tableName, String primary_key, List<String> condition, String databaseConfigId, String username, String ip) throws Exception {
        return this.hana2Dao.deleteRowsNewForHana2(databaseName, tableName, primary_key, condition, databaseConfigId, username, ip);
    }

    public boolean insertByDataListForHana2(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId) throws Exception {
        return this.hana2Dao.insertByDataListForHana2(dataList, databaseName, table, databaseConfigId);
    }

    public boolean updateByDataListForHana2(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.hana2Dao.updateByDataListForHana2(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public boolean insertOrUpdateByDataListForHana2(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.hana2Dao.insertOrUpdateByDataListForHana2(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public boolean insertOnlyByDataListForHana2(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.hana2Dao.insertOnlyByDataListForHana2(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public boolean deleteByDataListForHana2(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.hana2Dao.deleteByDataListForHana2(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public List<Map<String, Object>> viewTableMessForHana2(String databaseName, String tableName, String databaseConfigId) throws Exception {
        List<Map<String, Object>> list = this.hana2Dao.viewTableMessForHana2(databaseName, tableName, databaseConfigId);
        List<Map<String, Object>> listAllColumn2 = new ArrayList<>();
        if (list.size() > 0) {
            Map<String, Object> map = list.get(0);
            Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = it.next();
                Map<String, Object> tempMap = new HashMap<>();
                tempMap.put("propName", entry.getKey());
                tempMap.put("propValue", entry.getValue());
                listAllColumn2.add(tempMap);
            }
            Map<String, Object> tempMap4 = new HashMap<>();
            tempMap4.put("propName", "总记录数");
            Integer num = this.hana2Dao.getTableRowsForHana2(databaseName, tableName, databaseConfigId);
            tempMap4.put("propValue", num);
            listAllColumn2.add(tempMap4);
        }
        return listAllColumn2;
    }

    public List<Map<String, Object>> exportDataToSQLForHana2(String databaseName, String tableName, List<String> condition, String databaseConfigId) throws Exception {
        return this.hana2Dao.exportDataToSQLForHana2(databaseName, tableName, condition, databaseConfigId);
    }

    public String createTableSQLForHana2(String tableName, String databaseName, String databaseConfigId) throws Exception {
        return this.hana2Dao.createTableSQLForHana2(tableName, databaseName, databaseConfigId);
    }

    public List<Map<String, Object>> selectAllDataFromSQLForHana2(String databaseName, String databaseConfigId, String sql, int limitFrom, int pageSize) throws Exception {
        return this.hana2Dao.selectAllDataFromSQLForHana2(databaseName, databaseConfigId, sql, limitFrom, pageSize);
    }

    public Map<String, Object> executeSqlNotRes(String sql, String dbName, String databaseConfigId) throws Exception {
        String status;
        String mess;
        Date b1 = new Date();
        int i = 0;
        try {
            i = this.hana2Dao.executeSqlNotRes(sql, dbName, databaseConfigId);
            mess = "执行成功！";
            LogUtil.i(String.valueOf(DateUtils.getDateTime()) + ",SQL执行 ,dbName=" + dbName + ",SQL=" + sql);
            status = "success";
        } catch (Exception e) {
            LogUtil.e("SQL执行出错, ", e);
            mess = "执行失败, " + e.getMessage();
            status = "fail";
        }
        Date b2 = new Date();
        long y = b2.getTime() - b1.getTime();
        Map<String, Object> map = new HashMap<>();
        map.put("totalCount", Integer.valueOf(i));
        map.put("time", Long.valueOf(y));
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    public String dataListToStringForHana2(String tableName, Map<String, String> TableColumnType, List<Map<String, Object>> dataList) {
        StringBuffer sb = new StringBuffer();
        Iterator<Map<String, Object>> it = dataList.iterator();
        while (it.hasNext()) {
            Map<String, Object> map4 = it.next();
            String tempColumnName = "";
            sb.append(" INSERT INTO " + tableName + " (");
            String values = "";
            Iterator<Map.Entry<String, Object>> it2 = map4.entrySet().iterator();
            while (it2.hasNext()) {
                Map.Entry<String, Object> entry = it2.next();
                String key = entry.getKey();
                tempColumnName = String.valueOf(tempColumnName) + key + ",";
                if (!key.equals("RN")) {
                    if (entry.getValue() == null) {
                        values = String.valueOf(values) + "null,";
                    } else if (TableColumnType.get(key).equals("DATE") || TableColumnType.get(key).equals("TIME") || TableColumnType.get(key).equals("TIMESTAMP")) {
                        values = String.valueOf(values) + "'" + entry.getValue() + "',";
                    } else if (TableColumnType.get(key).equals("INT") || TableColumnType.get(key).equals("SMALLINT") || TableColumnType.get(key).equals("TINYINT") || TableColumnType.get(key).equals("INTEGER") || TableColumnType.get(key).equals("bit") || TableColumnType.get(key).equals("NUMBER") || TableColumnType.get(key).equals("BIGINT") || TableColumnType.get(key).equals("LONG") || TableColumnType.get(key).equals("FLOAT") || TableColumnType.get(key).equals("DECIMAL") || TableColumnType.get(key).equals("NUMERIC") || TableColumnType.get(key).equals("MEDIUMINT")) {
                        values = String.valueOf(values) + entry.getValue() + ",";
                    } else if (TableColumnType.get(key).equals("BINARY") || TableColumnType.get(key).equals("VARBINARY") || TableColumnType.get(key).equals("BLOB") || TableColumnType.get(key).equals("TINYBLOB") || TableColumnType.get(key).equals("MEDIUMBLOB") || TableColumnType.get(key).equals("LONGBLOB")) {
                        byte[] ss = (byte[]) entry.getValue();
                        if (ss.length == 0) {
                            values = String.valueOf(values) + "null,";
                        } else {
                            values = String.valueOf(values) + "0x" + DataUtil.bytesToHexString(ss) + ",";
                        }
                    } else {
                        String tempValues = (String) entry.getValue();
                        values = String.valueOf(values) + "'" + tempValues.replace("'", "\\'").replace("\r\n", "\\r\\n").replace("\n\r", "\\n\\r").replace("\r", "\\r").replace("\n", "\\n") + "',";
                    }
                }
            }
            sb.append(String.valueOf(tempColumnName.substring(0, tempColumnName.length() - 1)) + " ) VALUES ( " + values.substring(0, values.length() - 1) + " ); \r\n");
        }
        return sb.toString();
    }

    public Map<String, Object> executeSqlHaveResForHana2(String sql, String dbName, String databaseConfigId, Page<Map<String, Object>> page) {
        String status;
        String mess;
        Map<String, Object> map = new HashMap<>();
        try {
            page = this.hana2Dao.executeSqlHaveResForHana2(page, sql, dbName, databaseConfigId);
            mess = "执行成功！";
            status = "success";
        } catch (Exception e) {
            LogUtil.e("SQL执行出错,", e);
            mess = "执行失败, " + e.getMessage();
            status = "fail";
        }
        map.put("rows", page.getResult());
        map.put("total", Long.valueOf(page.getTotalCount()));
        map.put("columns", page.getColumns());
        map.put("primaryKey", page.getPrimaryKey());
        map.put("totalCount", Long.valueOf(page.getTotalCount()));
        map.put("time", page.getExecuteTime());
        map.put("operator", page.getOperator());
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    public int executeQueryForCountForHana2(String sql, String databaseName, String databaseConfigId) throws Exception {
        return this.hana2Dao.executeQueryForCountForHana2(sql, databaseName, databaseConfigId);
    }
}
