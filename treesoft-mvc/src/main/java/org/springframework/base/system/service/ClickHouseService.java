package org.springframework.base.system.service;

import com.alibaba.fastjson.JSONArray;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.base.system.dao.ClickHouseDao;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.utils.DateUtils;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/service/ClickHouseService.class */
public class ClickHouseService {
    @Autowired
    private ClickHouseDao clickHouseDao;

    public List<Map<String, Object>> getAllDataBaseForClickHouse(String databaseConfigId) throws Exception {
        return this.clickHouseDao.getAllDataBaseForClickHouse(databaseConfigId);
    }

    public List<Map<String, Object>> getAllTablesForClickHouse(String dbName, String databaseConfigId) throws Exception {
        return this.clickHouseDao.getAllTablesForClickHouse(dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getTableColumns3ForClickHouse(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.clickHouseDao.getTableColumns3ForClickHouse(databaseName, tableName, databaseConfigId);
    }

    public List<Map<String, Object>> getAllFuntionForClickHouse(String dbName, String databaseConfigId) throws Exception {
        return new ArrayList();
    }

    public Page<Map<String, Object>> getDataForClickHouse(Page<Map<String, Object>> page, String tableName, String dbName, String databaseConfigId) throws Exception {
        return this.clickHouseDao.getDataForClickHouse(page, tableName, dbName, databaseConfigId);
    }

    public int updateRowsNewForClickHouse(String databaseName, String tableName, List<String> strList, String databaseConfigId) throws Exception {
        Iterator<String> it = strList.iterator();
        while (it.hasNext()) {
            String str1 = it.next();
            if (str1 == null || "".equals(str1)) {
                throw new Exception("数据不完整,保存失败!");
            }
            String sql = " update top 1 " + tableName + str1;
            LogUtil.i(String.valueOf(DateUtils.getDateTime()) + " update语句 =" + sql);
            this.clickHouseDao.executeSqlNotRes(sql, databaseName, databaseConfigId);
        }
        return 0;
    }

    public boolean copyTableForClickHouse(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.clickHouseDao.copyTableForClickHouse(databaseName, tableName, databaseConfigId);
    }

    public int updateTableNullAbleForClickHouse(String databaseName, String tableName, String column_name, String is_nullable, String databaseConfigId) throws Exception {
        return this.clickHouseDao.updateTableNullAbleForClickHouse(databaseName, tableName, column_name, is_nullable, databaseConfigId);
    }

    public int savePrimaryKeyForClickHouse(String databaseName, String tableName, String column_name, String column_key, String databaseConfigId) throws Exception {
        return this.clickHouseDao.savePrimaryKeyForClickHouse(databaseName, tableName, column_name, column_key, databaseConfigId);
    }

    public int saveDesginColumnForClickHouse(Map map, String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.clickHouseDao.saveDesginColumnForClickHouse(map, databaseName, tableName, databaseConfigId);
    }

    @Transactional
    public int updateTableColumnForClickHouse(String updated, String databaseName, String tableName, String databaseConfigId) throws Exception {
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
                this.clickHouseDao.updateTableColumnForClickHouse(maps, databaseName, tableName, "column_name", idValues, databaseConfigId);
            }
            return 0;
        }
        return 0;
    }

    public boolean dropTableForClickHouse(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.clickHouseDao.dropTableForClickHouse(databaseName, tableName, databaseConfigId);
    }

    public int deleteTableColumnForClickHouse(String databaseName, String tableName, String[] ids, String databaseConfigId) throws Exception {
        return this.clickHouseDao.deleteTableColumnForClickHouse(databaseName, tableName, ids, databaseConfigId);
    }

    public int saveRowsForClickHouse(Map map, String databaseName, String tableName, String databaseConfigId, String username, String ip) throws Exception {
        return this.clickHouseDao.saveRowsForClickHouse(map, databaseName, tableName, databaseConfigId, username, ip);
    }

    public int saveNewTableForClickHouse(JSONArray insertArray, String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.clickHouseDao.saveNewTableForClickHouse(insertArray, databaseName, tableName, databaseConfigId);
    }

    public String selectColumnTypeForClickHouse(String databaseName, String tableName, String column, String databaseConfigId) throws Exception {
        return this.clickHouseDao.selectColumnTypeForClickHouse(databaseName, tableName, column, databaseConfigId);
    }

    public boolean renameTableForClickHouse(String databaseName, String tableName, String databaseConfigId, String newTableName) throws Exception {
        return this.clickHouseDao.renameTableForClickHouse(databaseName, tableName, databaseConfigId, newTableName);
    }

    public int deleteRowsNewForClickHouse(String databaseName, String tableName, String primary_key, List<String> condition, String databaseConfigId, String username, String ip) throws Exception {
        return this.clickHouseDao.deleteRowsNewForClickHouse(databaseName, tableName, primary_key, condition, databaseConfigId, username, ip);
    }

    public boolean insertByDataListForClickHouse(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId) throws Exception {
        return this.clickHouseDao.insertByDataListForClickHouse(dataList, databaseName, table, databaseConfigId);
    }

    public boolean updateByDataListForClickHouse(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.clickHouseDao.updateByDataListForClickHouse(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public boolean insertOrUpdateByDataListForClickHouse(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.clickHouseDao.insertOrUpdateByDataListForClickHouse(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public boolean insertOnlyByDataListForClickHouse(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.clickHouseDao.insertOnlyByDataListForClickHouse(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public boolean deleteByDataListForClickHouse(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.clickHouseDao.deleteByDataListForClickHouse(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public List<Map<String, Object>> viewTableMessForClickHouse(String databaseName, String tableName, String databaseConfigId) throws Exception {
        List<Map<String, Object>> list = this.clickHouseDao.viewTableMessForClickHouse(databaseName, tableName, databaseConfigId);
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
            String num = this.clickHouseDao.getTableRows(databaseName, tableName, databaseConfigId);
            tempMap4.put("propValue", num);
            listAllColumn2.add(tempMap4);
        }
        return listAllColumn2;
    }

    public List<Map<String, Object>> exportDataToSQLForClickHouse(String databaseName, String tableName, List<String> condition, String databaseConfigId) throws Exception {
        return this.clickHouseDao.exportDataToSQLForClickHouse(databaseName, tableName, condition, databaseConfigId);
    }

    public String createTableSQLForClickHouse(String tableName, String databaseName, String databaseConfigId) throws Exception {
        return this.clickHouseDao.createTableSQLForClickHouse(tableName, databaseName, databaseConfigId);
    }

    public List<Map<String, Object>> selectAllDataFromSQLForClickHouse(String databaseName, String databaseConfigId, String sql, int limitFrom, int pageSize) throws Exception {
        return this.clickHouseDao.selectAllDataFromSQLForClickHouse(databaseName, databaseConfigId, sql, limitFrom, pageSize);
    }

    public int executeSqlNotRes(String sql, String dbName, String databaseConfigId) throws Exception {
        return this.clickHouseDao.executeSqlNotRes(sql, dbName, databaseConfigId);
    }

    public Map<String, Object> executeSqlHaveResForClickHouse(String sql, String dbName, String databaseConfigId, Page<Map<String, Object>> page) {
        String status;
        String mess;
        Map<String, Object> map = new HashMap<>();
        try {
            page = this.clickHouseDao.executeSqlHaveResForClickHouse(page, sql, dbName, databaseConfigId);
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
