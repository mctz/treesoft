package org.springframework.base.system.service;

import com.alibaba.fastjson.JSONArray;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.base.system.dao.InformixDao;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.utils.DateUtils;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/service/InformixService.class */
public class InformixService {
    @Autowired
    private InformixDao informixDao;

    public List<Map<String, Object>> getAllDataBaseForInformix(String databaseConfigId) throws Exception {
        return this.informixDao.getAllDataBaseForInformix(databaseConfigId);
    }

    public List<Map<String, Object>> getAllTablesForInformix(String dbName, String databaseConfigId) throws Exception {
        return this.informixDao.getAllTablesForInformix(dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getTableColumns3ForInformix(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.informixDao.getTableColumns3ForInformix(databaseName, tableName, databaseConfigId);
    }

    public String getViewSqlForInformix(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.informixDao.getViewSqlForInformix(databaseName, tableName, databaseConfigId);
    }

    public List<Map<String, Object>> getAllViewsForInformix(String dbName, String databaseConfigId) throws Exception {
        return this.informixDao.getAllViewsForInformix(dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getAllFuntionForInformix(String dbName, String databaseConfigId) throws Exception {
        return this.informixDao.getAllFuntionForInformix(dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getAllProcedureForInformix(String dbName, String databaseConfigId) throws Exception {
        return this.informixDao.getAllProcedureForInformix(dbName, databaseConfigId);
    }

    public Page<Map<String, Object>> getDataForInformix(Page<Map<String, Object>> page, String tableName, String dbName, String databaseConfigId) throws Exception {
        return this.informixDao.getDataForInformix(page, tableName, dbName, databaseConfigId);
    }

    public int updateRowsNewForInformix(String databaseName, String tableName, List<String> strList, String databaseConfigId) throws Exception {
        Iterator<String> it = strList.iterator();
        while (it.hasNext()) {
            String str1 = it.next();
            if (str1 == null || "".equals(str1)) {
                throw new Exception("数据不完整,保存失败!");
            }
            String sql = " update top 1 " + tableName + str1;
            LogUtil.i(String.valueOf(DateUtils.getDateTime()) + " update语句 =" + sql);
            this.informixDao.executeSqlNotRes(sql, databaseName, databaseConfigId);
        }
        return 0;
    }

    public boolean copyTableForInformix(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.informixDao.copyTableForInformix(databaseName, tableName, databaseConfigId);
    }

    public int updateTableNullAbleForInformix(String databaseName, String tableName, String column_name, String is_nullable, String databaseConfigId) throws Exception {
        return this.informixDao.updateTableNullAbleForInformix(databaseName, tableName, column_name, is_nullable, databaseConfigId);
    }

    public int savePrimaryKeyForInformix(String databaseName, String tableName, String column_name, String column_key, String databaseConfigId) throws Exception {
        return this.informixDao.savePrimaryKeyForInformix(databaseName, tableName, column_name, column_key, databaseConfigId);
    }

    public int saveDesginColumnForInformix(Map map, String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.informixDao.saveDesginColumnForInformix(map, databaseName, tableName, databaseConfigId);
    }

    @Transactional
    public int updateTableColumnForInformix(String updated, String databaseName, String tableName, String databaseConfigId) throws Exception {
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
                this.informixDao.updateTableColumnForInformix(maps, databaseName, tableName, "column_name", idValues, databaseConfigId);
            }
            return 0;
        }
        return 0;
    }

    public boolean dropTableForInformix(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.informixDao.dropTableForInformix(databaseName, tableName, databaseConfigId);
    }

    public int deleteTableColumnForInformix(String databaseName, String tableName, String[] ids, String databaseConfigId) throws Exception {
        return this.informixDao.deleteTableColumnForInformix(databaseName, tableName, ids, databaseConfigId);
    }

    public int saveRowsForInformix(Map map, String databaseName, String tableName, String databaseConfigId, String username, String ip) throws Exception {
        return this.informixDao.saveRowsForInformix(map, databaseName, tableName, databaseConfigId, username, ip);
    }

    public int saveNewTableForInformix(JSONArray insertArray, String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.informixDao.saveNewTableForInformix(insertArray, databaseName, tableName, databaseConfigId);
    }

    public String selectColumnTypeForInformix(String databaseName, String tableName, String column, String databaseConfigId) throws Exception {
        return this.informixDao.selectColumnTypeForInformix(databaseName, tableName, column, databaseConfigId);
    }

    public boolean renameTableForInformix(String databaseName, String tableName, String databaseConfigId, String newTableName) throws Exception {
        return this.informixDao.renameTableForInformix(databaseName, tableName, databaseConfigId, newTableName);
    }

    public int deleteRowsNewForInformix(String databaseName, String tableName, String primary_key, List<String> condition, String databaseConfigId, String username, String ip) throws Exception {
        return this.informixDao.deleteRowsNewForInformix(databaseName, tableName, primary_key, condition, databaseConfigId, username, ip);
    }

    public boolean insertByDataListForInformix(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId) throws Exception {
        return this.informixDao.insertByDataListForInformix(dataList, databaseName, table, databaseConfigId);
    }

    public boolean updateByDataListForInformix(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.informixDao.updateByDataListForInformix(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public boolean insertOrUpdateByDataListForInformix(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.informixDao.insertOrUpdateByDataListForInformix(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public boolean deleteByDataListForInformix(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.informixDao.deleteByDataListForInformix(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public List<Map<String, Object>> viewTableMessForInformix(String databaseName, String tableName, String databaseConfigId) throws Exception {
        List<Map<String, Object>> list = this.informixDao.viewTableMessForInformix(databaseName, tableName, databaseConfigId);
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
            String num = this.informixDao.getTableRows(databaseName, tableName, databaseConfigId);
            tempMap4.put("propValue", num);
            listAllColumn2.add(tempMap4);
        }
        return listAllColumn2;
    }

    public List<Map<String, Object>> exportDataToSQLForInformix(String databaseName, String tableName, List<String> condition, String databaseConfigId) throws Exception {
        return this.informixDao.exportDataToSQLForInformix(databaseName, tableName, condition, databaseConfigId);
    }

    public String createTableSQLForInformix(String tableName, String databaseName, String databaseConfigId) throws Exception {
        return this.informixDao.createTableSQLForInformix(tableName, databaseName, databaseConfigId);
    }

    public List<Map<String, Object>> selectAllDataFromSQLForInformix(String databaseName, String databaseConfigId, String sql) throws Exception {
        return this.informixDao.selectAllDataFromSQLForInformix(databaseName, databaseConfigId, sql);
    }

    public int executeSqlNotRes(String sql, String dbName, String databaseConfigId) throws Exception {
        return this.informixDao.executeSqlNotRes(sql, dbName, databaseConfigId);
    }

    public Map<String, Object> executeSqlHaveResForInformix(String sql, String dbName, String databaseConfigId, Page<Map<String, Object>> page) {
        String status;
        String mess;
        Map<String, Object> map = new HashMap<>();
        try {
            page = this.informixDao.executeSqlHaveResForInformix(page, sql, dbName, databaseConfigId);
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
        map.put("mess", mess);
        map.put("status", status);
        map.put("operator", page.getOperator());
        return map;
    }
}
