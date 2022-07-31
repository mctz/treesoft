package org.springframework.base.system.service;

import com.alibaba.fastjson.JSONArray;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.base.system.dao.CacheDao;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.utils.DateUtils;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/service/CacheService.class */
public class CacheService {
    @Autowired
    private CacheDao cacheDao;

    public List<Map<String, Object>> getAllDataBaseForCache(String databaseConfigId) throws Exception {
        return this.cacheDao.getAllDataBaseForCache(databaseConfigId);
    }

    public List<Map<String, Object>> getAllTablesForCache(String dbName, String databaseConfigId) throws Exception {
        return this.cacheDao.getAllTablesForCache(dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getAllViewsForCache(String dbName, String databaseConfigId) throws Exception {
        return this.cacheDao.getAllViewsForCache(dbName, databaseConfigId);
    }

    public String getViewSqlForCache(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.cacheDao.getViewSqlForCache(databaseName, tableName, databaseConfigId);
    }

    public List<Map<String, Object>> getAllFuntionForCache(String dbName, String databaseConfigId) throws Exception {
        return new ArrayList();
    }

    public Page<Map<String, Object>> getDataForCache(Page<Map<String, Object>> page, String tableName, String dbName, String databaseConfigId) throws Exception {
        return this.cacheDao.getDataForCache(page, tableName, dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getTableColumns3ForCache(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.cacheDao.getTableColumns3ForCache(databaseName, tableName, databaseConfigId);
    }

    public int updateRowsNewForCache(String databaseName, String tableName, List<String> strList, String databaseConfigId) throws Exception {
        Iterator<String> it = strList.iterator();
        while (it.hasNext()) {
            String str1 = it.next();
            if (str1 == null || "".equals(str1)) {
                throw new Exception("数据不完整,保存失败!");
            }
            String sql = " update top 1 " + tableName + str1;
            LogUtil.i(String.valueOf(DateUtils.getDateTime()) + " update语句 =" + sql);
            this.cacheDao.executeSqlNotRes(sql, databaseName, databaseConfigId);
        }
        return 0;
    }

    public boolean copyTableForCache(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.cacheDao.copyTableForCache(databaseName, tableName, databaseConfigId);
    }

    public int updateTableNullAbleForCache(String databaseName, String tableName, String column_name, String is_nullable, String databaseConfigId) throws Exception {
        return this.cacheDao.updateTableNullAbleForCache(databaseName, tableName, column_name, is_nullable, databaseConfigId);
    }

    public int savePrimaryKeyForCache(String databaseName, String tableName, String column_name, String column_key, String databaseConfigId) throws Exception {
        return this.cacheDao.savePrimaryKeyForCache(databaseName, tableName, column_name, column_key, databaseConfigId);
    }

    public int saveDesginColumnForCache(Map map, String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.cacheDao.saveDesginColumnForCache(map, databaseName, tableName, databaseConfigId);
    }

    @Transactional
    public int updateTableColumnForCache(String updated, String databaseName, String tableName, String databaseConfigId) throws Exception {
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
                this.cacheDao.updateTableColumnForCache(maps, databaseName, tableName, "column_name", idValues, databaseConfigId);
            }
            return 0;
        }
        return 0;
    }

    public int deleteTableColumnForCache(String databaseName, String tableName, String[] ids, String databaseConfigId) throws Exception {
        return this.cacheDao.deleteTableColumnForCache(databaseName, tableName, ids, databaseConfigId);
    }

    public int saveRowsForCache(Map map, String databaseName, String tableName, String databaseConfigId, String username, String ip) throws Exception {
        return this.cacheDao.saveRowsForCache(map, databaseName, tableName, databaseConfigId, username, ip);
    }

    public int saveNewTableForCache(JSONArray insertArray, String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.cacheDao.saveNewTableForCache(insertArray, databaseName, tableName, databaseConfigId);
    }

    public String selectColumnTypeForCache(String databaseName, String tableName, String column, String databaseConfigId) throws Exception {
        return this.cacheDao.selectColumnTypeForCache(databaseName, tableName, column, databaseConfigId);
    }

    public boolean renameTableForCache(String databaseName, String tableName, String databaseConfigId, String newTableName) throws Exception {
        return this.cacheDao.renameTableForCache(databaseName, tableName, databaseConfigId, newTableName);
    }

    public boolean dropTableForCache(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.cacheDao.dropTableForCache(databaseName, tableName, databaseConfigId);
    }

    public int deleteRowsNewForCache(String databaseName, String tableName, String primary_key, List<String> condition, String databaseConfigId, String username, String ip) throws Exception {
        return this.cacheDao.deleteRowsNewForCache(databaseName, tableName, primary_key, condition, databaseConfigId, username, ip);
    }

    public boolean insertByDataListForCache(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId) throws Exception {
        return this.cacheDao.insertByDataListForCache(dataList, databaseName, table, databaseConfigId);
    }

    public boolean updateByDataListForCache(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.cacheDao.updateByDataListForCache(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public boolean insertOrUpdateByDataListForCache(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.cacheDao.insertOrUpdateByDataListForCache(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public boolean insertOnlyByDataListForCache(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.cacheDao.insertOnlyByDataListForCache(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public boolean deleteByDataListForCache(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.cacheDao.deleteByDataListForCache(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public List<Map<String, Object>> viewTableMessForCache(String databaseName, String tableName, String databaseConfigId) throws Exception {
        List<Map<String, Object>> list = this.cacheDao.viewTableMessForCache(databaseName, tableName, databaseConfigId);
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
            String num = this.cacheDao.getTableRows(databaseName, tableName, databaseConfigId);
            tempMap4.put("propValue", num);
            listAllColumn2.add(tempMap4);
        }
        return listAllColumn2;
    }

    public List<Map<String, Object>> exportDataToSQLForCache(String databaseName, String tableName, List<String> condition, String databaseConfigId) throws Exception {
        return this.cacheDao.exportDataToSQLForCache(databaseName, tableName, condition, databaseConfigId);
    }

    public String createTableSQLForCache(String tableName, String databaseName, String databaseConfigId) throws Exception {
        return this.cacheDao.createTableSQLForCache(tableName, databaseName, databaseConfigId);
    }

    public List<Map<String, Object>> selectAllDataFromSQLForCache(String databaseName, String databaseConfigId, String sql, int limitFrom, int pageSize) throws Exception {
        return this.cacheDao.selectAllDataFromSQLForCache(databaseName, databaseConfigId, sql, limitFrom, pageSize);
    }

    public Map<String, Object> executeSqlNotRes(String sql, String dbName, String databaseConfigId) throws Exception {
        String status;
        String mess;
        Date b1 = new Date();
        int i = 0;
        try {
            i = this.cacheDao.executeSqlNotRes(sql, dbName, databaseConfigId);
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

    public Map<String, Object> executeSqlHaveResForCache(String sql, String dbName, String databaseConfigId, Page<Map<String, Object>> page) {
        String status;
        String mess;
        Map<String, Object> map = new HashMap<>();
        try {
            page = this.cacheDao.executeSqlHaveResForCache(page, sql, dbName, databaseConfigId);
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
}
