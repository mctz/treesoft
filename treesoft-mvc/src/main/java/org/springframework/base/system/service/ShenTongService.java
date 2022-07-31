package org.springframework.base.system.service;

import com.alibaba.fastjson.JSONArray;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.base.system.dao.ShenTongDao;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.utils.DateUtils;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/service/ShenTongService.class */
public class ShenTongService {
    @Autowired
    private ShenTongDao shenTongDao;

    public List<Map<String, Object>> getAllDataBaseForShenTong(String databaseConfigId) throws Exception {
        return this.shenTongDao.getAllDataBaseForShenTong(databaseConfigId);
    }

    public List<Map<String, Object>> getAllTablesForShenTong(String dbName, String databaseConfigId) throws Exception {
        return this.shenTongDao.getAllTablesForShenTong(dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getTableColumns3ForShenTong(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.shenTongDao.getTableColumns3ForShenTong(databaseName, tableName, databaseConfigId);
    }

    public String getViewSqlForShenTong(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.shenTongDao.getViewSqlForShenTong(databaseName, tableName, databaseConfigId);
    }

    public List<Map<String, Object>> getAllViewsForShenTong(String dbName, String databaseConfigId) throws Exception {
        return this.shenTongDao.getAllViewsForShenTong(dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getAllFuntionForShenTong(String dbName, String databaseConfigId) throws Exception {
        return this.shenTongDao.getAllFuntionForShenTong(dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getAllProcedureForShenTong(String dbName, String databaseConfigId) throws Exception {
        return this.shenTongDao.getAllProcedureForShenTong(dbName, databaseConfigId);
    }

    public Page<Map<String, Object>> getDataForShenTong(Page<Map<String, Object>> page, String tableName, String dbName, String databaseConfigId) throws Exception {
        return this.shenTongDao.getDataForShenTong(page, tableName, dbName, databaseConfigId);
    }

    public int updateRowsNewForShenTong(String databaseName, String tableName, List<String> strList, String databaseConfigId) throws Exception {
        Iterator<String> it = strList.iterator();
        while (it.hasNext()) {
            String str1 = it.next();
            if (str1 == null || "".equals(str1)) {
                throw new Exception("数据不完整,保存失败!");
            }
            String sql = " update top 1 " + tableName + str1;
            LogUtil.i(String.valueOf(DateUtils.getDateTime()) + " update语句 =" + sql);
            this.shenTongDao.executeSqlNotRes(sql, databaseName, databaseConfigId);
        }
        return 0;
    }

    public boolean copyTableForShenTong(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.shenTongDao.copyTableForShenTong(databaseName, tableName, databaseConfigId);
    }

    public int updateTableNullAbleForShenTong(String databaseName, String tableName, String column_name, String is_nullable, String databaseConfigId) throws Exception {
        return this.shenTongDao.updateTableNullAbleForShenTong(databaseName, tableName, column_name, is_nullable, databaseConfigId);
    }

    public int savePrimaryKeyForShenTong(String databaseName, String tableName, String column_name, String column_key, String databaseConfigId) throws Exception {
        return this.shenTongDao.savePrimaryKeyForShenTong(databaseName, tableName, column_name, column_key, databaseConfigId);
    }

    public int saveDesginColumnForShenTong(Map map, String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.shenTongDao.saveDesginColumnForShenTong(map, databaseName, tableName, databaseConfigId);
    }

    @Transactional
    public int updateTableColumnForShenTong(String updated, String databaseName, String tableName, String databaseConfigId) throws Exception {
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
                this.shenTongDao.updateTableColumnForShenTong(maps, databaseName, tableName, "column_name", idValues, databaseConfigId);
            }
            return 0;
        }
        return 0;
    }

    public boolean dropTableForShenTong(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.shenTongDao.dropTableForShenTong(databaseName, tableName, databaseConfigId);
    }

    public boolean clearTableForShenTong(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.shenTongDao.clearTableForShenTong(databaseName, tableName, databaseConfigId);
    }

    public int deleteTableColumnForShenTong(String databaseName, String tableName, String[] ids, String databaseConfigId) throws Exception {
        return this.shenTongDao.deleteTableColumnForShenTong(databaseName, tableName, ids, databaseConfigId);
    }

    public int saveRowsForShenTong(Map map, String databaseName, String tableName, String databaseConfigId, String username, String ip) throws Exception {
        return this.shenTongDao.saveRowsForShenTong(map, databaseName, tableName, databaseConfigId, username, ip);
    }

    public int saveNewTableForShenTong(JSONArray insertArray, String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.shenTongDao.saveNewTableForShenTong(insertArray, databaseName, tableName, databaseConfigId);
    }

    public String selectColumnTypeForShenTong(String databaseName, String tableName, String column, String databaseConfigId) throws Exception {
        return this.shenTongDao.selectColumnTypeForShenTong(databaseName, tableName, column, databaseConfigId);
    }

    public boolean renameTableForShenTong(String databaseName, String tableName, String databaseConfigId, String newTableName) throws Exception {
        return this.shenTongDao.renameTableForShenTong(databaseName, tableName, databaseConfigId, newTableName);
    }

    public int deleteRowsNewForShenTong(String databaseName, String tableName, String primary_key, List<String> condition, String databaseConfigId, String username, String ip) throws Exception {
        return this.shenTongDao.deleteRowsNewForShenTong(databaseName, tableName, primary_key, condition, databaseConfigId, username, ip);
    }

    public boolean insertByDataListForShenTong(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId) throws Exception {
        return this.shenTongDao.insertByDataListForShenTong(dataList, databaseName, table, databaseConfigId);
    }

    public boolean updateByDataListForShenTong(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.shenTongDao.updateByDataListForShenTong(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public boolean insertOrUpdateByDataListForShenTong(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.shenTongDao.insertOrUpdateByDataListForShenTong(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public boolean insertOnlyByDataListForShenTong(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.shenTongDao.insertOnlyByDataListForShenTong(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public boolean deleteByDataListForShenTong(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.shenTongDao.deleteByDataListForShenTong(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public List<Map<String, Object>> viewTableMessForShenTong(String databaseName, String tableName, String databaseConfigId) throws Exception {
        List<Map<String, Object>> list = this.shenTongDao.viewTableMessForShenTong(databaseName, tableName, databaseConfigId);
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
            Integer num = this.shenTongDao.getTableRows(databaseName, tableName, databaseConfigId);
            tempMap4.put("propValue", num);
            listAllColumn2.add(tempMap4);
        }
        return listAllColumn2;
    }

    public List<Map<String, Object>> exportDataToSQLForShenTong(String databaseName, String tableName, List<String> condition, String databaseConfigId) throws Exception {
        return this.shenTongDao.exportDataToSQLForShenTong(databaseName, tableName, condition, databaseConfigId);
    }

    public String createTableSQLForShenTong(String tableName, String databaseName, String databaseConfigId) throws Exception {
        return this.shenTongDao.createTableSQLForShenTong(tableName, databaseName, databaseConfigId);
    }

    public List<Map<String, Object>> selectAllDataFromSQLForShenTong(String databaseName, String databaseConfigId, String sql, int limitFrom, int pageSize) throws Exception {
        return this.shenTongDao.selectAllDataFromSQLForShenTong(databaseName, databaseConfigId, sql, limitFrom, pageSize);
    }

    public boolean backupDatabaseExecuteForShenTong(String databaseName, String tableName, String path, String databaseConfigId) throws Exception {
        return this.shenTongDao.backupDatabaseExecuteForShenTong(databaseName, tableName, path, databaseConfigId);
    }

    public Map<String, Object> executeSqlNotRes(String sql, String dbName, String databaseConfigId) throws Exception {
        String status;
        String mess;
        Date b1 = new Date();
        int i = 0;
        try {
            i = this.shenTongDao.executeSqlNotRes(sql, dbName, databaseConfigId);
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

    public Map<String, Object> executeSqlHaveResForShenTong(String sql, String dbName, String databaseConfigId, Page<Map<String, Object>> page) {
        String status;
        String mess;
        Map<String, Object> map = new HashMap<>();
        try {
            page = this.shenTongDao.executeSqlHaveResForShenTong(page, sql, dbName, databaseConfigId);
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

    public int executeQueryForCountForShenTong(String sql, String databaseName, String databaseConfigId) throws Exception {
        return this.shenTongDao.executeQueryForCountForShenTong(sql, databaseName, databaseConfigId);
    }
}
