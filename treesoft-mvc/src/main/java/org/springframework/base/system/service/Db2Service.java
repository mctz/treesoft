package org.springframework.base.system.service;

import com.alibaba.fastjson.JSONArray;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.base.system.dao.Db2Dao;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.utils.DataUtil;
import org.springframework.base.system.utils.DateUtils;
import org.springframework.base.system.utils.ExcelUtil;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/service/Db2Service.class */
public class Db2Service {
    @Autowired
    private Db2Dao db2Dao;

    public List<Map<String, Object>> getAllDataBaseForDb2(String databaseConfigId) throws Exception {
        return this.db2Dao.getAllDataBaseForDb2(databaseConfigId);
    }

    public List<Map<String, Object>> getAllTablesForDb2(String dbName, String databaseConfigId) throws Exception {
        return this.db2Dao.getAllTablesForDb2(dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getTableColumns3ForDb2(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.db2Dao.getTableColumns3ForDb2(databaseName, tableName, databaseConfigId);
    }

    public String getViewSqlForDb2(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.db2Dao.getViewSqlForDb2(databaseName, tableName, databaseConfigId);
    }

    public List<Map<String, Object>> getAllViewsForDb2(String dbName, String databaseConfigId) throws Exception {
        return this.db2Dao.getAllViewsForDb2(dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getAllFuntionForDb2(String dbName, String databaseConfigId) throws Exception {
        return new ArrayList();
    }

    public Page<Map<String, Object>> getDataForDb2(Page<Map<String, Object>> page, String tableName, String dbName, String databaseConfigId) throws Exception {
        return this.db2Dao.getDataForDb2(page, tableName, dbName, databaseConfigId);
    }

    public int updateRowsNewForDb2(String databaseName, String tableName, List<String> strList, String databaseConfigId) throws Exception {
        Iterator<String> it = strList.iterator();
        while (it.hasNext()) {
            String str1 = it.next();
            if (str1 == null || "".equals(str1)) {
                throw new Exception("数据不完整,保存失败!");
            }
            String sql = " update top 1 " + tableName + str1;
            LogUtil.i(String.valueOf(DateUtils.getDateTime()) + " update语句 =" + sql);
            this.db2Dao.executeSqlNotRes(sql, databaseName, databaseConfigId);
        }
        return 0;
    }

    public boolean copyTableForDb2(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.db2Dao.copyTableForDb2(databaseName, tableName, databaseConfigId);
    }

    public int updateTableNullAbleForDb2(String databaseName, String tableName, String column_name, String is_nullable, String databaseConfigId) throws Exception {
        return this.db2Dao.updateTableNullAbleForDb2(databaseName, tableName, column_name, is_nullable, databaseConfigId);
    }

    public int savePrimaryKeyForDb2(String databaseName, String tableName, String column_name, String column_key, String databaseConfigId) throws Exception {
        return this.db2Dao.savePrimaryKeyForDb2(databaseName, tableName, column_name, column_key, databaseConfigId);
    }

    public int saveDesginColumnForDb2(Map map, String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.db2Dao.saveDesginColumnForDb2(map, databaseName, tableName, databaseConfigId);
    }

    @Transactional
    public int updateTableColumnForDb2(String updated, String databaseName, String tableName, String databaseConfigId) throws Exception {
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
                this.db2Dao.updateTableColumnForDb2(maps, databaseName, tableName, "column_name", idValues, databaseConfigId);
            }
            return 0;
        }
        return 0;
    }

    public boolean dropTableForDb2(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.db2Dao.dropTableForDb2(databaseName, tableName, databaseConfigId);
    }

    public int deleteTableColumnForDb2(String databaseName, String tableName, String[] ids, String databaseConfigId) throws Exception {
        return this.db2Dao.deleteTableColumnForDb2(databaseName, tableName, ids, databaseConfigId);
    }

    public int saveRowsForDb2(Map map, String databaseName, String tableName, String databaseConfigId, String username, String ip) throws Exception {
        return this.db2Dao.saveRowsForDb2(map, databaseName, tableName, databaseConfigId, username, ip);
    }

    public int saveNewTableForDb2(JSONArray insertArray, String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.db2Dao.saveNewTableForDb2(insertArray, databaseName, tableName, databaseConfigId);
    }

    public String selectColumnTypeForDb2(String databaseName, String tableName, String column, String databaseConfigId) throws Exception {
        return this.db2Dao.selectColumnTypeForDb2(databaseName, tableName, column, databaseConfigId);
    }

    public boolean renameTableForDb2(String databaseName, String tableName, String databaseConfigId, String newTableName) throws Exception {
        return this.db2Dao.renameTableForDb2(databaseName, tableName, databaseConfigId, newTableName);
    }

    public int deleteRowsNewForDb2(String databaseName, String tableName, String primary_key, List<String> condition, String databaseConfigId, String username, String ip) throws Exception {
        return this.db2Dao.deleteRowsNewForDb2(databaseName, tableName, primary_key, condition, databaseConfigId, username, ip);
    }

    public boolean insertByDataListForDb2(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId) throws Exception {
        return this.db2Dao.insertByDataListForDb2(dataList, databaseName, table, databaseConfigId);
    }

    public boolean updateByDataListForDb2(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.db2Dao.updateByDataListForDb2(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public boolean insertOrUpdateByDataListForDb2(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.db2Dao.insertOrUpdateByDataListForDb2(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public boolean insertOnlyByDataListForDb2(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.db2Dao.insertOnlyByDataListForDb2(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public boolean deleteByDataListForDb2(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.db2Dao.deleteByDataListForDb2(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public List<Map<String, Object>> viewTableMessForDb2(String databaseName, String tableName, String databaseConfigId) throws Exception {
        List<Map<String, Object>> list = this.db2Dao.viewTableMessForDb2(databaseName, tableName, databaseConfigId);
        List<Map<String, Object>> listAllColumn2 = new ArrayList<>();
        if (list.size() > 0) {
            Map<String, Object> map = list.get(0);
            Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = it.next();
                Map<String, Object> tempMap = new HashMap<>();
                tempMap.put("propName", entry.getKey());
                if (entry.getKey().equals("CREATE_TIME") || entry.getKey().equals("ALTER_TIME") || entry.getKey().equals("INVALIDATE_TIME")) {
                    SimpleDateFormat sdf = new SimpleDateFormat(ExcelUtil.DATE_FORMAT);
                    tempMap.put("propValue", sdf.format(entry.getValue()));
                } else {
                    tempMap.put("propValue", entry.getValue());
                }
                listAllColumn2.add(tempMap);
            }
            Map<String, Object> tempMap4 = new HashMap<>();
            tempMap4.put("propName", "总记录数");
            Integer rowCount = this.db2Dao.getTableRowsForDb2(databaseName, tableName, databaseConfigId);
            tempMap4.put("propValue", rowCount);
            listAllColumn2.add(tempMap4);
        }
        return listAllColumn2;
    }

    public List<Map<String, Object>> exportDataToSQLForDb2(String databaseName, String tableName, List<String> condition, String databaseConfigId) throws Exception {
        return this.db2Dao.exportDataToSQLForDb2(databaseName, tableName, condition, databaseConfigId);
    }

    public String createTableSQLForDb2(String tableName, String databaseName, String databaseConfigId) throws Exception {
        return this.db2Dao.createTableSQLForDb2(tableName, databaseName, databaseConfigId);
    }

    public List<Map<String, Object>> selectAllDataFromSQLForDb2(String databaseName, String databaseConfigId, String sql, int limitFrom, int pageSize) throws Exception {
        return this.db2Dao.selectAllDataFromSQLForDb2(databaseName, databaseConfigId, sql, limitFrom, pageSize);
    }

    public int executeSqlNotRes(String sql, String dbName, String databaseConfigId) throws Exception {
        return this.db2Dao.executeSqlNotRes(sql, dbName, databaseConfigId);
    }

    public String dataListToStringForDb2(String tableName, Map<String, String> TableColumnType, List<Map<String, Object>> dataList) {
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

    public Map<String, Object> executeSqlHaveResForDb2(String sql, String dbName, String databaseConfigId, Page<Map<String, Object>> page) {
        String status;
        String mess;
        Map<String, Object> map = new HashMap<>();
        try {
            page = this.db2Dao.executeSqlHaveResForDb2(page, sql, dbName, databaseConfigId);
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
        map.put("tableName", page.getTableName());
        map.put("totalCount", Long.valueOf(page.getTotalCount()));
        map.put("time", page.getExecuteTime());
        map.put("operator", page.getOperator());
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }
}
