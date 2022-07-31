package org.springframework.base.system.service;

import com.alibaba.fastjson.JSONArray;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.base.system.dao.SybaseDao;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.utils.DateUtils;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/service/SybaseService.class */
public class SybaseService {
    @Autowired
    private SybaseDao sybaseDao;

    public List<Map<String, Object>> getAllDataBaseForSybase(String databaseConfigId) throws Exception {
        return this.sybaseDao.getAllDataBaseForSybase(databaseConfigId);
    }

    public List<Map<String, Object>> getAllTablesForSybase(String dbName, String databaseConfigId) throws Exception {
        return this.sybaseDao.getAllTablesForSybase(dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getTableColumns3ForSybase(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.sybaseDao.getTableColumns3ForSybase(databaseName, tableName, databaseConfigId);
    }

    public String getViewSqlForSybase(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.sybaseDao.getViewSqlForSybase(databaseName, tableName, databaseConfigId);
    }

    public List<Map<String, Object>> getAllViewsForSybase(String dbName, String databaseConfigId) throws Exception {
        return this.sybaseDao.getAllViewsForSybase(dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getAllFuntionForSybase(String dbName, String databaseConfigId) throws Exception {
        return this.sybaseDao.getAllFuntionForSybase(dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getAllProcedureForSybase(String dbName, String databaseConfigId) throws Exception {
        return this.sybaseDao.getAllProcedureForSybase(dbName, databaseConfigId);
    }

    public Page<Map<String, Object>> getDataForSybase(Page<Map<String, Object>> page, String tableName, String dbName, String databaseConfigId) throws Exception {
        return this.sybaseDao.getDataForSybase(page, tableName, dbName, databaseConfigId);
    }

    public int updateRowsNewForSybase(String databaseName, String tableName, List<String> strList, String databaseConfigId) throws Exception {
        Iterator<String> it = strList.iterator();
        while (it.hasNext()) {
            String str1 = it.next();
            if (str1 == null || "".equals(str1)) {
                throw new Exception("数据不完整,保存失败!");
            }
            String sql = " update top 1 " + tableName + str1;
            LogUtil.i(String.valueOf(DateUtils.getDateTime()) + " update语句 =" + sql);
            this.sybaseDao.executeSqlNotRes(sql, databaseName, databaseConfigId);
        }
        return 0;
    }

    public boolean copyTableForSybase(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.sybaseDao.copyTableForSybase(databaseName, tableName, databaseConfigId);
    }

    public int updateTableNullAbleForSybase(String databaseName, String tableName, String column_name, String is_nullable, String databaseConfigId) throws Exception {
        return this.sybaseDao.updateTableNullAbleForSybase(databaseName, tableName, column_name, is_nullable, databaseConfigId);
    }

    public int savePrimaryKeyForSybase(String databaseName, String tableName, String column_name, String column_key, String databaseConfigId) throws Exception {
        return this.sybaseDao.savePrimaryKeyForSybase(databaseName, tableName, column_name, column_key, databaseConfigId);
    }

    public int saveDesginColumnForSybase(Map map, String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.sybaseDao.saveDesginColumnForSybase(map, databaseName, tableName, databaseConfigId);
    }

    @Transactional
    public int updateTableColumnForSybase(String updated, String databaseName, String tableName, String databaseConfigId) throws Exception {
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
                this.sybaseDao.updateTableColumnForSybase(maps, databaseName, tableName, "column_name", idValues, databaseConfigId);
            }
            return 0;
        }
        return 0;
    }

    public boolean dropTableForSybase(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.sybaseDao.dropTableForSybase(databaseName, tableName, databaseConfigId);
    }

    public boolean clearTableForSybase(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.sybaseDao.clearTableForSybase(databaseName, tableName, databaseConfigId);
    }

    public int deleteTableColumnForSybase(String databaseName, String tableName, String[] ids, String databaseConfigId) throws Exception {
        return this.sybaseDao.deleteTableColumnForSybase(databaseName, tableName, ids, databaseConfigId);
    }

    public int saveRowsForSybase(Map map, String databaseName, String tableName, String databaseConfigId, String username, String ip) throws Exception {
        return this.sybaseDao.saveRowsForSybase(map, databaseName, tableName, databaseConfigId, username, ip);
    }

    public int saveNewTableForSybase(JSONArray insertArray, String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.sybaseDao.saveNewTableForSybase(insertArray, databaseName, tableName, databaseConfigId);
    }

    public String selectColumnTypeForSybase(String databaseName, String tableName, String column, String databaseConfigId) throws Exception {
        return this.sybaseDao.selectColumnTypeForSybase(databaseName, tableName, column, databaseConfigId);
    }

    public boolean renameTableForSybase(String databaseName, String tableName, String databaseConfigId, String newTableName) throws Exception {
        return this.sybaseDao.renameTableForSybase(databaseName, tableName, databaseConfigId, newTableName);
    }

    public int deleteRowsNewForSybase(String databaseName, String tableName, String primary_key, List<String> condition, String databaseConfigId, String username, String ip) throws Exception {
        return this.sybaseDao.deleteRowsNewForSybase(databaseName, tableName, primary_key, condition, databaseConfigId, username, ip);
    }

    public boolean insertByDataListForSybase(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId) throws Exception {
        return this.sybaseDao.insertByDataListForSybase(dataList, databaseName, table, databaseConfigId);
    }

    public boolean updateByDataListForSybase(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.sybaseDao.updateByDataListForSybase(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public boolean insertOrUpdateByDataListForSybase(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.sybaseDao.insertOrUpdateByDataListForSybase(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public boolean deleteByDataListForSybase(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.sybaseDao.deleteByDataListForSybase(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public List<Map<String, Object>> viewTableMessForSybase(String databaseName, String tableName, String databaseConfigId) throws Exception {
        List<Map<String, Object>> list = this.sybaseDao.viewTableMessForSybase(databaseName, tableName, databaseConfigId);
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
            String num = this.sybaseDao.getTableRows(databaseName, tableName, databaseConfigId);
            tempMap4.put("propValue", num);
            listAllColumn2.add(tempMap4);
        }
        return listAllColumn2;
    }

    public List<Map<String, Object>> exportDataToSQLForSybase(String databaseName, String tableName, List<String> condition, String databaseConfigId) throws Exception {
        return this.sybaseDao.exportDataToSQLForSybase(databaseName, tableName, condition, databaseConfigId);
    }

    public String createTableSQLForSybase(String tableName, String databaseName, String databaseConfigId) throws Exception {
        return this.sybaseDao.createTableSQLForSybase(tableName, databaseName, databaseConfigId);
    }

    public List<Map<String, Object>> selectAllDataFromSQLForSybase(String databaseName, String databaseConfigId, String sql, int limitFrom, int pageSize) throws Exception {
        return this.sybaseDao.selectAllDataFromSQLForSybase(databaseName, databaseConfigId, sql, limitFrom, pageSize);
    }

    public int executeSqlNotRes(String sql, String dbName, String databaseConfigId) throws Exception {
        return this.sybaseDao.executeSqlNotRes(sql, dbName, databaseConfigId);
    }

    public Map<String, Object> executeSqlHaveResForSybase(String sql, String dbName, String databaseConfigId, Page<Map<String, Object>> page) {
        String status;
        String mess;
        Map<String, Object> map = new HashMap<>();
        try {
            page = this.sybaseDao.executeSqlHaveResForSybase(page, sql, dbName, databaseConfigId);
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
        map.put("mess", mess);
        map.put("status", status);
        map.put("operator", page.getOperator());
        return map;
    }
}
