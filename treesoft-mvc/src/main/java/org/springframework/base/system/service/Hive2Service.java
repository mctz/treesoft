package org.springframework.base.system.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.base.system.dao.Hive2Dao;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/service/Hive2Service.class */
public class Hive2Service {
    @Autowired
    private Hive2Dao hive2Dao;

    public List<Map<String, Object>> selectAllDataFromSQLForHive2(String databaseName, String databaseConfigId, String sql, int limitFrom, int pageSize) throws Exception {
        return this.hive2Dao.selectAllDataFromSQLForHive2(databaseName, databaseConfigId, sql, limitFrom, pageSize);
    }

    public List<Map<String, Object>> getAllDataBaseForHive2(String databaseConfigId) throws Exception {
        return this.hive2Dao.getAllDataBaseForHive2(databaseConfigId);
    }

    public List<Map<String, Object>> getAllViewsForHive2(String dbName, String databaseConfigId) throws Exception {
        return this.hive2Dao.getAllViewsForHive2(dbName, databaseConfigId);
    }

    public Page<Map<String, Object>> getDataForHive2(Page<Map<String, Object>> page, String tableName, String dbName, String databaseConfigId) throws Exception {
        return this.hive2Dao.getDataForHive2(page, tableName, dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getAllTablesForHive2(String databaseName, String databaseConfigId) throws Exception {
        return this.hive2Dao.getAllTablesForHive2(databaseName, databaseConfigId);
    }

    public boolean renameTableForHive2(String databaseName, String tableName, String databaseConfigId, String newTableName) throws Exception {
        return this.hive2Dao.renameTableForHive2(databaseName, tableName, databaseConfigId, newTableName);
    }

    public List<Map<String, Object>> viewTableMessForHive2(String databaseName, String tableName, String databaseConfigId) throws Exception {
        List<Map<String, Object>> list = this.hive2Dao.viewTableMessForHive2(databaseName, tableName, databaseConfigId);
        List<Map<String, Object>> listAllColumn2 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            Map<String, Object> tempMap = new HashMap<>();
            String col_name = (String) map.get("col_name");
            String data_type = (String) map.get("data_type");
            if (data_type != null) {
                data_type = data_type.trim();
                if (data_type.equals("numFiles")) {
                    col_name = data_type;
                    data_type = (String) map.get("comment");
                }
                if (data_type.equals("numRows")) {
                    col_name = data_type;
                    data_type = (String) map.get("comment");
                }
                if (data_type.equals("rawDataSize")) {
                    col_name = data_type;
                    data_type = (String) map.get("comment");
                }
                if (data_type.equals("totalSize")) {
                    col_name = data_type;
                    data_type = (String) map.get("comment");
                }
                if (data_type.equals("transient_lastDdlTime")) {
                    col_name = data_type;
                    data_type = (String) map.get("comment");
                }
                if (data_type.equals("field.delim")) {
                    col_name = data_type;
                    data_type = (String) map.get("comment");
                }
                if (data_type.equals("serialization.format")) {
                    col_name = data_type;
                    data_type = (String) map.get("comment");
                }
            }
            tempMap.put("propName", col_name);
            tempMap.put("propValue", data_type);
            listAllColumn2.add(tempMap);
        }
        return listAllColumn2;
    }

    public boolean insertByDataListForHive2(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId) throws Exception {
        return this.hive2Dao.insertByDataListForHive2(dataList, databaseName, table, databaseConfigId);
    }

    public Map<String, Object> executeSqlHaveResForHive2(String sql, String dbName, String databaseConfigId, Page<Map<String, Object>> page) {
        String status;
        String mess;
        Map<String, Object> map = new HashMap<>();
        Date b1 = new Date();
        try {
            page = this.hive2Dao.executeSqlHaveResForHive2(page, sql, dbName, databaseConfigId);
            mess = "执行成功！";
            status = "success";
        } catch (Exception e) {
            LogUtil.e("SQL执行出错,", e);
            mess = e.getMessage();
            status = "fail";
        }
        Date b2 = new Date();
        long y = b2.getTime() - b1.getTime();
        map.put("rows", page.getResult());
        map.put("total", Long.valueOf(page.getTotalCount()));
        map.put("columns", page.getColumns());
        map.put("tableName", "");
        map.put("totalCount", Long.valueOf(page.getTotalCount()));
        map.put("time", Long.valueOf(y));
        map.put("operator", page.getOperator());
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }
}
