package org.springframework.base.system.service;

import com.alibaba.fastjson.JSONArray;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.base.system.dao.PermissionDao;
import org.springframework.base.system.dao.PostgreSQLDao;
import org.springframework.base.system.entity.DmsDto;
import org.springframework.base.system.entity.IdsDto;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.utils.DataUtil;
import org.springframework.base.system.utils.DateUtils;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/service/PostgreSQLService.class */
public class PostgreSQLService {
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private PostgreSQLDao postgreSQLDao;

    public List<Map<String, Object>> selectAllDataFromSQLForPostgreSQL(String databaseName, String databaseConfigId, String sql, int limitFrom, int pageSize) throws Exception {
        return this.postgreSQLDao.selectAllDataFromSQLForPostgreSQL(databaseName, databaseConfigId, sql, limitFrom, pageSize);
    }

    public String selectColumnTypeForPostgreSQL(String databaseName, String tableName, String column, String databaseConfigId) throws Exception {
        return this.postgreSQLDao.selectColumnTypeForPostgreSQL(databaseName, tableName, column, databaseConfigId);
    }

    public List<Map<String, Object>> getAllDataBaseForPostgreSQL(String databaseConfigId) throws Exception {
        return this.postgreSQLDao.getAllDataBaseForPostgreSQL(databaseConfigId);
    }

    public List<Map<String, Object>> getAllTablesForPostgreSQL(String dbName, String databaseConfigId) throws Exception {
        return this.postgreSQLDao.getAllTablesForPostgreSQL(dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getTableColumns3ForPostgreSQL(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.postgreSQLDao.getTableColumns3ForPostgreSQL(databaseName, tableName, databaseConfigId);
    }

    public List<Map<String, Object>> getAllViewsForPostgreSQL(String dbName, String databaseConfigId) throws Exception {
        return this.postgreSQLDao.getAllViewsForPostgreSQL(dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getAllFuntionForPostgreSQL(String dbName, String databaseConfigId) throws Exception {
        return this.postgreSQLDao.getAllFuntionForPostgreSQL(dbName, databaseConfigId);
    }

    public Page<Map<String, Object>> getDataForPostgreSQL(Page<Map<String, Object>> page, String tableName, String dbName, String databaseConfigId) throws Exception {
        return this.postgreSQLDao.getDataForPostgreSQL(page, tableName, dbName, databaseConfigId);
    }

    public int deleteRowsNewForPostgreSQL(String databaseName, String tableName, String primary_key, List<String> condition, String databaseConfigId, String username, String ip) throws Exception {
        return this.postgreSQLDao.deleteRowsNewForPostgreSQL(databaseName, tableName, primary_key, condition, databaseConfigId, username, ip);
    }

    public int saveRowsForPostgreSQL(Map map, String databaseName, String tableName, String databaseConfigId, String username, String ip) throws Exception {
        return this.postgreSQLDao.saveRowsForPostgreSQL(map, databaseName, tableName, databaseConfigId, username, ip);
    }

    public int updateRowsNewForPostgreSQL(String databaseName, String tableName, List<String> strList, String databaseConfigId) throws Exception {
        String tableName2 = "\"" + tableName + "\"";
        Iterator<String> it = strList.iterator();
        while (it.hasNext()) {
            String str1 = it.next();
            if (str1 == null || "".equals(str1)) {
                throw new Exception("数据不完整,保存失败!");
            }
            String sql = " update  " + tableName2 + str1;
            LogUtil.i(String.valueOf(DateUtils.getDateTime()) + " update语句 =" + sql);
            this.permissionDao.executeSqlNotRes(sql, databaseName, databaseConfigId);
        }
        return 0;
    }

    public int saveDesginColumnForPostgreSQL(Map map, String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.postgreSQLDao.saveDesginColumnForPostgreSQL(map, databaseName, tableName, databaseConfigId);
    }

    @Transactional
    public int updateTableColumnForPostgreSQL(String updated, String databaseName, String tableName, String databaseConfigId) throws Exception {
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
                this.postgreSQLDao.updateTableColumnForPostgreSQL(maps, databaseName, tableName, "column_name", idValues, databaseConfigId);
            }
            return 0;
        }
        return 0;
    }

    public int deleteTableColumnForPostgreSQL(String databaseName, String tableName, String[] ids, String databaseConfigId) throws Exception {
        return this.postgreSQLDao.deleteTableColumnForPostgreSQL(databaseName, tableName, ids, databaseConfigId);
    }

    public boolean dropTableForPostgreSQL(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.postgreSQLDao.dropTableForPostgreSQL(databaseName, tableName, databaseConfigId);
    }

    public int updateTableNullAbleForPostgreSQL(String databaseName, String tableName, String column_name, String is_nullable, String databaseConfigId) throws Exception {
        return this.postgreSQLDao.updateTableNullAbleForPostgreSQL(databaseName, tableName, column_name, is_nullable, databaseConfigId);
    }

    public int savePrimaryKeyForPostgreSQL(String databaseName, String tableName, String column_name, String column_key, String databaseConfigId) throws Exception {
        return this.postgreSQLDao.savePrimaryKeyForPostgreSQL(databaseName, tableName, column_name, column_key, databaseConfigId);
    }

    public String getViewSqlForPostgreSQL(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.postgreSQLDao.getViewSqlForPostgreSQL(databaseName, tableName, databaseConfigId);
    }

    public boolean backupDatabaseExecuteForPostgreSQL(String databaseName, String tableName, String path, String databaseConfigId) throws Exception {
        return this.postgreSQLDao.backupDatabaseExecuteForPostgreSQL(databaseName, tableName, path, databaseConfigId);
    }

    public boolean renameTableForPostgreSQL(String databaseName, String tableName, String databaseConfigId, String newTableName) throws Exception {
        return this.postgreSQLDao.renameTableForPostgreSQL(databaseName, tableName, databaseConfigId, newTableName);
    }

    public List<Map<String, Object>> exportDataToSQLForPostgreSQL(String databaseName, String tableName, List<String> condition, String databaseConfigId) throws Exception {
        return this.postgreSQLDao.exportDataToSQLForPostgreSQL(databaseName, tableName, condition, databaseConfigId);
    }

    public boolean copyTableForPostgreSQL(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.postgreSQLDao.copyTableForPostgreSQL(databaseName, tableName, databaseConfigId);
    }

    public int saveNewTableForPostgreSQL(JSONArray insertArray, String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.postgreSQLDao.saveNewTableForPostgreSQL(insertArray, databaseName, tableName, databaseConfigId);
    }

    public List<Map<String, Object>> viewTableMessForPostgreSQL(String databaseName, String tableName, String databaseConfigId) throws Exception {
        List<Map<String, Object>> list = this.postgreSQLDao.viewTableMessForPostgreSQL(databaseName, tableName, databaseConfigId);
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
        }
        return listAllColumn2;
    }

    public boolean updateByDataListForPostgreSQL(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.postgreSQLDao.updateByDataListForPostgreSQL(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public Map<String, Object> queryDatabaseStatusForPostgreSQL(String databaseName, String databaseConfigId) throws Exception {
        return this.postgreSQLDao.queryDatabaseStatusForPostgreSQL(databaseName, databaseConfigId);
    }

    public int queryDatabaseStatusForPostgreSQLConn(String databaseName, String databaseConfigId) throws Exception {
        return this.postgreSQLDao.queryDatabaseStatusForPostgreSQLConn(databaseName, databaseConfigId);
    }

    public boolean insertOrUpdateByDataListForPostgreSQL(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.postgreSQLDao.insertOrUpdateByDataListForPostgreSQL(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public boolean insertOnlyByDataListForPostgreSQL(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.postgreSQLDao.insertOnlyByDataListForPostgreSQL(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public String createTableSQLForPostgreSQL(String tableName, String databaseName, String databaseConfigId) throws Exception {
        return this.postgreSQLDao.createTableSQLForPostgreSQL(tableName, databaseName, databaseConfigId);
    }

    public boolean deleteByDataListForPostgreSQL(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.postgreSQLDao.deleteByDataListForPostgreSQL(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public String dataListToStringForPostgreSQL(String tableName, Map<String, String> TableColumnType, List<Map<String, Object>> dataList) {
        StringBuffer sb = new StringBuffer();
        Iterator<Map<String, Object>> it = dataList.iterator();
        while (it.hasNext()) {
            Map<String, Object> map4 = it.next();
            String tempColumnName = "";
            sb.append("INSERT INTO \"" + tableName + "\" ( ");
            String values = "";
            Iterator<Map.Entry<String, Object>> it2 = map4.entrySet().iterator();
            while (it2.hasNext()) {
                Map.Entry<String, Object> entry = it2.next();
                String key = entry.getKey();
                tempColumnName = String.valueOf(tempColumnName) + "\"" + key + "\",";
                if (entry.getValue() == null) {
                    values = String.valueOf(values) + "null,";
                } else if (TableColumnType.get(key).equals("date") || TableColumnType.get(key).equals("datetime") || TableColumnType.get(key).equals("timestamp")) {
                    values = String.valueOf(values) + "to_date( '" + entry.getValue() + "','YYYY-MM-DD HH24:MI:SS'),";
                } else if (TableColumnType.get(key).equals("int") || TableColumnType.get(key).equals("smallint") || TableColumnType.get(key).equals("tinyint") || TableColumnType.get(key).equals("integer") || TableColumnType.get(key).equals("bit") || TableColumnType.get(key).equals("real") || TableColumnType.get(key).equals("bigint") || TableColumnType.get(key).equals("long") || TableColumnType.get(key).equals("float") || TableColumnType.get(key).equals("decimal") || TableColumnType.get(key).equals("numeric") || TableColumnType.get(key).equals("mediumint")) {
                    values = String.valueOf(values) + entry.getValue() + ",";
                } else if (TableColumnType.get(key).equals("binary") || TableColumnType.get(key).equals("varbinary") || TableColumnType.get(key).equals("blob") || TableColumnType.get(key).equals("tinyblob") || TableColumnType.get(key).equals("mediumblob") || TableColumnType.get(key).equals("longblob")) {
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
            sb.append(String.valueOf(tempColumnName.substring(0, tempColumnName.length() - 1)) + " ) VALUES ( " + values.substring(0, values.length() - 1) + " ); \r\n");
        }
        return sb.toString();
    }

    public Map<String, Object> executeSqlHaveResForPostgreSQL(String sql, String dbName, String databaseConfigId, Page<Map<String, Object>> page) {
        String status;
        String mess;
        Map<String, Object> map = new HashMap<>();
        try {
            page = this.postgreSQLDao.executeSqlHaveResForPostgreSQL(page, sql, dbName, databaseConfigId);
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

    public Page<Map<String, Object>> selectTableIndexsForPostgreSQL(Page<Map<String, Object>> page, DmsDto dto) throws Exception {
        return this.postgreSQLDao.selectTableIndexsForPostgreSQL(page, dto);
    }

    public boolean indexSaveForPostgreSQL(HttpServletRequest request, IdsDto dto, String username) throws Exception {
        return this.postgreSQLDao.indexSaveForPostgreSQL(request, dto, username);
    }

    public boolean indexDeleteForPostgreSQL(HttpServletRequest request, IdsDto dto, String username) throws Exception {
        return this.postgreSQLDao.indexDeleteForPostgreSQL(request, dto, username);
    }

    public Page<Map<String, Object>> selectTableTriggersForPostgreSQL(Page<Map<String, Object>> page, DmsDto dto) throws Exception {
        return this.postgreSQLDao.selectTableTriggersForPostgreSQL(page, dto);
    }

    public boolean triggerDeleteForPostgreSQL(HttpServletRequest request, IdsDto dto, String username) throws Exception {
        return this.postgreSQLDao.triggerDeleteForPostgreSQL(request, dto, username);
    }
}
