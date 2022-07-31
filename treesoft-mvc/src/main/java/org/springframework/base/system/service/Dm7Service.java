package org.springframework.base.system.service;

import com.alibaba.fastjson.JSONArray;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.base.system.dao.Dm7Dao;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.utils.DateUtils;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/service/Dm7Service.class */
public class Dm7Service {
    @Autowired
    private Dm7Dao dm7Dao;

    public List<Map<String, Object>> getAllDataBaseForDm7(String databaseConfigId) throws Exception {
        return this.dm7Dao.getAllDataBaseForDm7(databaseConfigId);
    }

    public List<Map<String, Object>> getAllTablesForDm7(String dbName, String databaseConfigId) throws Exception {
        return this.dm7Dao.getAllTablesForDm7(dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getTableColumns3ForDm7(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.dm7Dao.getTableColumnsForDM(databaseName, tableName, databaseConfigId);
    }

    public String getViewSqlForDm7(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.dm7Dao.getViewSqlForDm7(databaseName, tableName, databaseConfigId);
    }

    public List<Map<String, Object>> getAllViewsForDm7(String dbName, String databaseConfigId) throws Exception {
        return this.dm7Dao.getAllViewsForDm7(dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getAllFuntionForDm7(String dbName, String databaseConfigId) throws Exception {
        return this.dm7Dao.getAllFuntionForDm7(dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getAllProcedureForDm7(String dbName, String databaseConfigId) throws Exception {
        return this.dm7Dao.getAllProcedureForDm7(dbName, databaseConfigId);
    }

    public Page<Map<String, Object>> getDataForDm7(Page<Map<String, Object>> page, String tableName, String dbName, String databaseConfigId) throws Exception {
        return this.dm7Dao.getDataForDm7(page, tableName, dbName, databaseConfigId);
    }

    public int updateRowsNewForDm7(String databaseName, String tableName, List<String> strList, String databaseConfigId) throws Exception {
        Iterator<String> it = strList.iterator();
        while (it.hasNext()) {
            String str1 = it.next();
            if (str1 == null || "".equals(str1)) {
                throw new Exception("数据不完整,保存失败!");
            }
            String sql = " update top 1 " + tableName + str1;
            LogUtil.i(String.valueOf(DateUtils.getDateTime()) + " update语句 =" + sql);
            this.dm7Dao.executeSqlNotRes(sql, databaseName, databaseConfigId);
        }
        return 0;
    }

    public boolean copyTableForDm7(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.dm7Dao.copyTableForDm7(databaseName, tableName, databaseConfigId);
    }

    public int updateTableNullAbleForDm7(String databaseName, String tableName, String column_name, String is_nullable, String databaseConfigId) throws Exception {
        return this.dm7Dao.updateTableNullAbleForDm7(databaseName, tableName, column_name, is_nullable, databaseConfigId);
    }

    public int savePrimaryKeyForDm7(String databaseName, String tableName, String column_name, String column_key, String databaseConfigId) throws Exception {
        return this.dm7Dao.savePrimaryKeyForDm7(databaseName, tableName, column_name, column_key, databaseConfigId);
    }

    public int saveDesginColumnForDm7(Map map, String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.dm7Dao.saveDesginColumnForDm7(map, databaseName, tableName, databaseConfigId);
    }

    @Transactional
    public int updateTableColumnForDm7(String updated, String databaseName, String tableName, String databaseConfigId) throws Exception {
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
                this.dm7Dao.updateTableColumnForDm7(maps, databaseName, tableName, "column_name", idValues, databaseConfigId);
            }
            return 0;
        }
        return 0;
    }

    public boolean dropTableForDm7(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.dm7Dao.dropTableForDm7(databaseName, tableName, databaseConfigId);
    }

    public int deleteTableColumnForDm7(String databaseName, String tableName, String[] ids, String databaseConfigId) throws Exception {
        return this.dm7Dao.deleteTableColumnForDm7(databaseName, tableName, ids, databaseConfigId);
    }

    public int saveRowsForDm7(Map map, String databaseName, String tableName, String databaseConfigId, String username, String ip) throws Exception {
        return this.dm7Dao.saveRowsForDm7(map, databaseName, tableName, databaseConfigId, username, ip);
    }

    public int saveNewTableForDm7(JSONArray insertArray, String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.dm7Dao.saveNewTableForDm7(insertArray, databaseName, tableName, databaseConfigId);
    }

    public String selectColumnTypeForDm7(String databaseName, String tableName, String column, String databaseConfigId) throws Exception {
        return this.dm7Dao.selectColumnTypeForDm7(databaseName, tableName, column, databaseConfigId);
    }

    public boolean renameTableForDm7(String databaseName, String tableName, String databaseConfigId, String newTableName) throws Exception {
        return this.dm7Dao.renameTableForDm7(databaseName, tableName, databaseConfigId, newTableName);
    }

    public int deleteRowsNewForDm7(String databaseName, String tableName, String primary_key, List<String> condition, String databaseConfigId, String username, String ip) throws Exception {
        return this.dm7Dao.deleteRowsNewForDm7(databaseName, tableName, primary_key, condition, databaseConfigId, username, ip);
    }

    public boolean insertByDataListForDm7(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId) throws Exception {
        return this.dm7Dao.insertByDataListForDm7(dataList, databaseName, table, databaseConfigId);
    }

    public boolean updateByDataListForDm7(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.dm7Dao.updateByDataListForDm7(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public boolean insertOrUpdateByDataListForDm7(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.dm7Dao.insertOrUpdateByDataListForDm7(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public boolean insertOnlyByDataListForDm7(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.dm7Dao.insertOnlyByDataListForDm7(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public boolean deleteByDataListForDm7(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.dm7Dao.deleteByDataListForDm7(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public List<Map<String, Object>> viewTableMessForDm7(String databaseName, String tableName, String databaseConfigId) throws Exception {
        List<Map<String, Object>> list = this.dm7Dao.viewTableMessForDm7(databaseName, tableName, databaseConfigId);
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
            Integer rowCount = this.dm7Dao.getTableRowsForDm7(databaseName, tableName, databaseConfigId);
            tempMap4.put("propValue", rowCount);
            listAllColumn2.add(tempMap4);
        }
        return listAllColumn2;
    }

    public List<Map<String, Object>> exportDataToSQLForDm7(String databaseName, String tableName, List<String> condition, String databaseConfigId) throws Exception {
        return this.dm7Dao.exportDataToSQLForDm7(databaseName, tableName, condition, databaseConfigId);
    }

    public String createTableSQLForDm7(String tableName, String databaseName, String databaseConfigId) throws Exception {
        return this.dm7Dao.createTableSQLForDm7(tableName, databaseName, databaseConfigId);
    }

    public List<Map<String, Object>> selectAllDataFromSQLForDm7(String databaseName, String databaseConfigId, String sql, int limitFrom, int pageSize) throws Exception {
        return this.dm7Dao.selectAllDataFromSQLForDm7(databaseName, databaseConfigId, sql, limitFrom, pageSize);
    }

    public int executeSqlNotRes(String sql, String dbName, String databaseConfigId) throws Exception {
        return this.dm7Dao.executeSqlNotRes(sql, dbName, databaseConfigId);
    }

    public Map<String, Object> executeSqlHaveResForDm7(String sql, String dbName, String databaseConfigId, Page<Map<String, Object>> page) {
        String status;
        String mess;
        Map<String, Object> map = new HashMap<>();
        try {
            page = this.dm7Dao.executeSqlHaveResForDm7(page, sql, dbName, databaseConfigId);
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

    public boolean clearTableForDM(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.dm7Dao.clearTableForDM(databaseName, tableName, databaseConfigId);
    }

    public List<Map<String, Object>> getTableColumnsForDM(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.dm7Dao.getTableColumnsForDM(databaseName, tableName, databaseConfigId);
    }
}
