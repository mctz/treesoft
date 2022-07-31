package org.springframework.base.system.service;

import com.alibaba.fastjson.JSONArray;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.base.system.dao.RedshiftDao;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.utils.DateUtils;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/service/RedshiftService.class */
public class RedshiftService {
    @Autowired
    private RedshiftDao redshiftDao;

    public List<Map<String, Object>> getAllDataBaseForRedshift(String databaseConfigId) throws Exception {
        return this.redshiftDao.getAllDataBaseForRedshift(databaseConfigId);
    }

    public List<Map<String, Object>> getAllTablesForRedshift(String dbName, String databaseConfigId) throws Exception {
        return this.redshiftDao.getAllTablesForRedshift(dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getTableColumns3ForRedshift(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.redshiftDao.getTableColumns3ForRedshift(databaseName, tableName, databaseConfigId);
    }

    public List<Map<String, Object>> getAllViewsForRedshift(String dbName, String databaseConfigId) throws Exception {
        return this.redshiftDao.getAllViewsForRedshift(dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getAllFuntionForRedshift(String dbName, String databaseConfigId) throws Exception {
        return new ArrayList();
    }

    public Page<Map<String, Object>> getDataForRedshift(Page<Map<String, Object>> page, String tableName, String dbName, String databaseConfigId) throws Exception {
        return this.redshiftDao.getDataForRedshift(page, tableName, dbName, databaseConfigId);
    }

    public int updateRowsNewForRedshift(String databaseName, String tableName, List<String> strList, String databaseConfigId) throws Exception {
        Iterator<String> it = strList.iterator();
        while (it.hasNext()) {
            String str1 = it.next();
            if (str1 == null || "".equals(str1)) {
                throw new Exception("数据不完整,保存失败!");
            }
            String sql = " update top 1 " + tableName + str1;
            LogUtil.i(String.valueOf(DateUtils.getDateTime()) + " update语句 =" + sql);
            this.redshiftDao.executeSqlNotRes(sql, databaseName, databaseConfigId);
        }
        return 0;
    }

    public boolean copyTableForRedshift(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.redshiftDao.copyTableForRedshift(databaseName, tableName, databaseConfigId);
    }

    public int updateTableNullAbleForRedshift(String databaseName, String tableName, String column_name, String is_nullable, String databaseConfigId) throws Exception {
        return this.redshiftDao.updateTableNullAbleForRedshift(databaseName, tableName, column_name, is_nullable, databaseConfigId);
    }

    public int savePrimaryKeyForRedshift(String databaseName, String tableName, String column_name, String column_key, String databaseConfigId) throws Exception {
        return this.redshiftDao.savePrimaryKeyForRedshift(databaseName, tableName, column_name, column_key, databaseConfigId);
    }

    public int saveDesginColumnForRedshift(Map map, String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.redshiftDao.saveDesginColumnForRedshift(map, databaseName, tableName, databaseConfigId);
    }

    @Transactional
    public int updateTableColumnForRedshift(String updated, String databaseName, String tableName, String databaseConfigId) throws Exception {
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
                this.redshiftDao.updateTableColumnForRedshift(maps, databaseName, tableName, "column_name", idValues, databaseConfigId);
            }
            return 0;
        }
        return 0;
    }

    public boolean dropTableForRedshift(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.redshiftDao.dropTableForRedshift(databaseName, tableName, databaseConfigId);
    }

    public int deleteTableColumnForRedshift(String databaseName, String tableName, String[] ids, String databaseConfigId) throws Exception {
        return this.redshiftDao.deleteTableColumnForRedshift(databaseName, tableName, ids, databaseConfigId);
    }

    public int saveRowsForRedshift(Map map, String databaseName, String tableName, String databaseConfigId, String username, String ip) throws Exception {
        return this.redshiftDao.saveRowsForRedshift(map, databaseName, tableName, databaseConfigId, username, ip);
    }

    public int saveNewTableForRedshift(JSONArray insertArray, String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.redshiftDao.saveNewTableForRedshift(insertArray, databaseName, tableName, databaseConfigId);
    }

    public String selectColumnTypeForRedshift(String databaseName, String tableName, String column, String databaseConfigId) throws Exception {
        return this.redshiftDao.selectColumnTypeForRedshift(databaseName, tableName, column, databaseConfigId);
    }

    public boolean renameTableForRedshift(String databaseName, String tableName, String databaseConfigId, String newTableName) throws Exception {
        return this.redshiftDao.renameTableForRedshift(databaseName, tableName, databaseConfigId, newTableName);
    }

    public int deleteRowsNewForRedshift(String databaseName, String tableName, String primary_key, List<String> condition, String databaseConfigId, String username, String ip) throws Exception {
        return this.redshiftDao.deleteRowsNewForRedshift(databaseName, tableName, primary_key, condition, databaseConfigId, username, ip);
    }

    public boolean insertByDataListForRedshift(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId) throws Exception {
        return this.redshiftDao.insertByDataListForRedshift(dataList, databaseName, table, databaseConfigId);
    }

    public boolean updateByDataListForRedshift(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.redshiftDao.updateByDataListForRedshift(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public boolean insertOrUpdateByDataListForRedshift(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.redshiftDao.insertOrUpdateByDataListForRedshift(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public boolean insertOnlyByDataListForRedshift(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.redshiftDao.insertOnlyByDataListForRedshift(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public boolean deleteByDataListForRedshift(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.redshiftDao.deleteByDataListForRedshift(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public List<Map<String, Object>> viewTableMessForRedshift(String databaseName, String tableName, String databaseConfigId) throws Exception {
        List<Map<String, Object>> list = this.redshiftDao.viewTableMessForRedshift(databaseName, tableName, databaseConfigId);
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
            String num = this.redshiftDao.getTableRows(databaseName, tableName, databaseConfigId);
            tempMap4.put("propValue", num);
            listAllColumn2.add(tempMap4);
        }
        return listAllColumn2;
    }

    public List<Map<String, Object>> exportDataToSQLForRedshift(String databaseName, String tableName, List<String> condition, String databaseConfigId) throws Exception {
        return this.redshiftDao.exportDataToSQLForRedshift(databaseName, tableName, condition, databaseConfigId);
    }

    public String createTableSQLForRedshift(String tableName, String databaseName, String databaseConfigId) throws Exception {
        return this.redshiftDao.createTableSQLForRedshift(tableName, databaseName, databaseConfigId);
    }

    public Map<String, Object> queryDatabaseStatusForRedshift(String databaseName, String databaseConfigId) throws Exception {
        return this.redshiftDao.queryDatabaseStatusForRedshift(databaseName, databaseConfigId);
    }

    public List<Map<String, Object>> selectAllDataFromSQLForRedshift(String databaseName, String databaseConfigId, String sql, int limitFrom, int pageSize) throws Exception {
        return this.redshiftDao.selectAllDataFromSQLForRedshift(databaseName, databaseConfigId, sql, limitFrom, pageSize);
    }

    public int executeSqlNotRes(String sql, String dbName, String databaseConfigId) throws Exception {
        return this.redshiftDao.executeSqlNotRes(sql, dbName, databaseConfigId);
    }

    public Map<String, Object> executeSqlHaveResForRedshift(String sql, String dbName, String databaseConfigId, Page<Map<String, Object>> page) {
        String status;
        String mess;
        Map<String, Object> map = new HashMap<>();
        try {
            page = this.redshiftDao.executeSqlHaveResForRedshift(page, sql, dbName, databaseConfigId);
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
