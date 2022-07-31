package org.springframework.base.system.service;

import com.alibaba.fastjson.JSONArray;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.base.system.dao.KingbaseDao;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.utils.DateUtils;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/service/KingbaseService.class */
public class KingbaseService {
    @Autowired
    private KingbaseDao kingbaseDao;

    public List<Map<String, Object>> getAllDataBaseForKingbase(String databaseConfigId) throws Exception {
        return this.kingbaseDao.getAllDataBaseForKingbase(databaseConfigId);
    }

    public List<Map<String, Object>> getAllTablesForKingbase(String dbName, String databaseConfigId) throws Exception {
        return this.kingbaseDao.getAllTablesForKingbase(dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getTableColumns3ForKingbase(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.kingbaseDao.getTableColumns3ForKingbase(databaseName, tableName, databaseConfigId);
    }

    public String getViewSqlForKingbase(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.kingbaseDao.getViewSqlForKingbase(databaseName, tableName, databaseConfigId);
    }

    public List<Map<String, Object>> getAllViewsForKingbase(String dbName, String databaseConfigId) throws Exception {
        return this.kingbaseDao.getAllViewsForKingbase(dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getAllFuntionForKingbase(String dbName, String databaseConfigId) throws Exception {
        return this.kingbaseDao.getAllFuntionForKingbase(dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getAllProcedureForKingbase(String dbName, String databaseConfigId) throws Exception {
        return this.kingbaseDao.getAllProcedureForKingbase(dbName, databaseConfigId);
    }

    public Page<Map<String, Object>> getDataForKingbase(Page<Map<String, Object>> page, String tableName, String dbName, String databaseConfigId) throws Exception {
        return this.kingbaseDao.getDataForKingbase(page, tableName, dbName, databaseConfigId);
    }

    public int updateRowsNewForKingbase(String databaseName, String tableName, List<String> strList, String databaseConfigId) throws Exception {
        Iterator<String> it = strList.iterator();
        while (it.hasNext()) {
            String str1 = it.next();
            if (str1 == null || "".equals(str1)) {
                throw new Exception("数据不完整,保存失败!");
            }
            String sql = " update top 1 " + tableName + str1;
            LogUtil.i(String.valueOf(DateUtils.getDateTime()) + " update语句 =" + sql);
            this.kingbaseDao.executeSqlNotRes(sql, databaseName, databaseConfigId);
        }
        return 0;
    }

    public boolean copyTableForKingbase(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.kingbaseDao.copyTableForKingbase(databaseName, tableName, databaseConfigId);
    }

    public int updateTableNullAbleForKingbase(String databaseName, String tableName, String column_name, String is_nullable, String databaseConfigId) throws Exception {
        return this.kingbaseDao.updateTableNullAbleForKingbase(databaseName, tableName, column_name, is_nullable, databaseConfigId);
    }

    public int savePrimaryKeyForKingbase(String databaseName, String tableName, String column_name, String column_key, String databaseConfigId) throws Exception {
        return this.kingbaseDao.savePrimaryKeyForKingbase(databaseName, tableName, column_name, column_key, databaseConfigId);
    }

    public int saveDesginColumnForKingbase(Map map, String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.kingbaseDao.saveDesginColumnForKingbase(map, databaseName, tableName, databaseConfigId);
    }

    @Transactional
    public int updateTableColumnForKingbase(String updated, String databaseName, String tableName, String databaseConfigId) throws Exception {
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
                this.kingbaseDao.updateTableColumnForKingbase(maps, databaseName, tableName, "column_name", idValues, databaseConfigId);
            }
            return 0;
        }
        return 0;
    }

    public boolean dropTableForKingbase(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.kingbaseDao.dropTableForKingbase(databaseName, tableName, databaseConfigId);
    }

    public int deleteTableColumnForKingbase(String databaseName, String tableName, String[] ids, String databaseConfigId) throws Exception {
        return this.kingbaseDao.deleteTableColumnForKingbase(databaseName, tableName, ids, databaseConfigId);
    }

    public int saveRowsForKingbase(Map map, String databaseName, String tableName, String databaseConfigId, String username, String ip) throws Exception {
        return this.kingbaseDao.saveRowsForKingbase(map, databaseName, tableName, databaseConfigId, username, ip);
    }

    public int saveNewTableForKingbase(JSONArray insertArray, String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.kingbaseDao.saveNewTableForKingbase(insertArray, databaseName, tableName, databaseConfigId);
    }

    public String selectColumnTypeForKingbase(String databaseName, String tableName, String column, String databaseConfigId) throws Exception {
        return this.kingbaseDao.selectColumnTypeForKingbase(databaseName, tableName, column, databaseConfigId);
    }

    public boolean renameTableForKingbase(String databaseName, String tableName, String databaseConfigId, String newTableName) throws Exception {
        return this.kingbaseDao.renameTableForKingbase(databaseName, tableName, databaseConfigId, newTableName);
    }

    public int deleteRowsNewForKingbase(String databaseName, String tableName, String primary_key, List<String> condition, String databaseConfigId, String username, String ip) throws Exception {
        return this.kingbaseDao.deleteRowsNewForKingbase(databaseName, tableName, primary_key, condition, databaseConfigId, username, ip);
    }

    public boolean insertByDataListForKingbase(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId) throws Exception {
        return this.kingbaseDao.insertByDataListForKingbase(dataList, databaseName, table, databaseConfigId);
    }

    public boolean updateByDataListForKingbase(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.kingbaseDao.updateByDataListForKingbase(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public boolean insertOrUpdateByDataListForKingbase(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.kingbaseDao.insertOrUpdateByDataListForKingbase(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public boolean insertOnlyByDataListForKingbase(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.kingbaseDao.insertOnlyByDataListForKingbase(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public boolean deleteByDataListForKingbase(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.kingbaseDao.deleteByDataListForKingbase(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public List<Map<String, Object>> viewTableMessForKingbase(String databaseName, String tableName, String databaseConfigId) throws Exception {
        List<Map<String, Object>> list = this.kingbaseDao.viewTableMessForKingbase(databaseName, tableName, databaseConfigId);
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
            Integer rowCount = this.kingbaseDao.getTableRowsForKingbase(databaseName, tableName, databaseConfigId);
            tempMap4.put("propValue", rowCount);
            listAllColumn2.add(tempMap4);
        }
        return listAllColumn2;
    }

    public List<Map<String, Object>> exportDataToSQLForKingbase(String databaseName, String tableName, List<String> condition, String databaseConfigId) throws Exception {
        return this.kingbaseDao.exportDataToSQLForKingbase(databaseName, tableName, condition, databaseConfigId);
    }

    public String createTableSQLForKingbase(String tableName, String databaseName, String databaseConfigId) throws Exception {
        return this.kingbaseDao.createTableSQLForKingbase(tableName, databaseName, databaseConfigId);
    }

    public List<Map<String, Object>> selectAllDataFromSQLForKingbase(String databaseName, String databaseConfigId, String sql, int limitFrom, int pageSize) throws Exception {
        return this.kingbaseDao.selectAllDataFromSQLForKingbase(databaseName, databaseConfigId, sql, limitFrom, pageSize);
    }

    public int executeSqlNotRes(String sql, String dbName, String databaseConfigId) throws Exception {
        return this.kingbaseDao.executeSqlNotRes(sql, dbName, databaseConfigId);
    }

    public Map<String, Object> executeSqlHaveResForKingbase(String sql, String dbName, String databaseConfigId, Page<Map<String, Object>> page) {
        String status;
        String mess;
        Map<String, Object> map = new HashMap<>();
        try {
            page = this.kingbaseDao.executeSqlHaveResForKingbase(page, sql, dbName, databaseConfigId);
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
