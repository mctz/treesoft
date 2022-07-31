package org.springframework.base.system.service;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.base.system.dao.OracleDao;
import org.springframework.base.system.dao.PermissionDao;
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
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/service/OracleService.class */
public class OracleService {
    @Autowired
    private OracleDao oracleDao;
    @Autowired
    private PermissionDao permissionDao;

    public List<Map<String, Object>> selectAllDataFromSQLForOracle(String databaseName, String databaseConfigId, String sql, int limitFrom, int pageSize) throws Exception {
        return this.oracleDao.selectAllDataFromSQLForOracle(databaseName, databaseConfigId, sql, limitFrom, pageSize);
    }

    public List<Map<String, Object>> getAllDataBaseForOracle(String databaseConfigId) throws Exception {
        return this.oracleDao.getAllDataBaseForOracle(databaseConfigId);
    }

    public List<Map<String, Object>> getAllTablesForOracle(String dbName, String databaseConfigId) throws Exception {
        return this.oracleDao.getAllTablesForOracle(dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getTableColumns3ForOracle(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.oracleDao.getTableColumns3ForOracle(databaseName, tableName, databaseConfigId);
    }

    public String getViewSqlForOracle(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.oracleDao.getViewSqlForOracle(databaseName, tableName, databaseConfigId);
    }

    public String getFunctionSqlForOracle(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.oracleDao.getFunctionSqlForOracle(databaseName, tableName, databaseConfigId);
    }

    public String getProcedureSqlForOracle(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.oracleDao.getProcedureSqlForOracle(databaseName, tableName, databaseConfigId);
    }

    public String getPackageSqlForOracle(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.oracleDao.getPackageSqlForOracle(databaseName, tableName, databaseConfigId);
    }

    public String getPackageBodySqlForOracle(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.oracleDao.getPackageBodySqlForOracle(databaseName, tableName, databaseConfigId);
    }

    public List<Map<String, Object>> getAllViewsForOracle(String dbName, String databaseConfigId) throws Exception {
        return this.oracleDao.getAllViewsForOracle(dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getAllFuntionForOracle(String dbName, String databaseConfigId) throws Exception {
        return this.oracleDao.getAllFuntionForOracle(dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getAllProcedureForOracle(String dbName, String databaseConfigId) throws Exception {
        return this.oracleDao.getAllProcedureForOracle(dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getAllPackageForOracle(String dbName, String databaseConfigId) throws Exception {
        return this.oracleDao.getAllPackageForOracle(dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getAllPackageBobyForOracle(String dbName, String databaseConfigId) throws Exception {
        return this.oracleDao.getAllPackageBobyForOracle(dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getAllSynonymsForOracle(String dbName, String databaseConfigId) throws Exception {
        return this.oracleDao.getAllSynonymsForOracle(dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getSequenceForOracle(String dbName, String databaseConfigId) throws Exception {
        return this.oracleDao.getSequenceForOracle(dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getIndexForOracle(String dbName, String databaseConfigId) throws Exception {
        return this.oracleDao.getIndexForOracle(dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getDBLinkForOracle(String dbName, String databaseConfigId) throws Exception {
        return this.oracleDao.getDBLinkForOracle(dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getUserForOracle(String dbName, String databaseConfigId) throws Exception {
        return this.oracleDao.getUserForOracle(dbName, databaseConfigId);
    }

    public Page<Map<String, Object>> getDataForOracle(Page<Map<String, Object>> page, String tableName, String dbName, String databaseConfigId) throws Exception {
        return this.oracleDao.getDataForOracle(page, tableName, dbName, databaseConfigId);
    }

    public int updateRowsNewForOracle(String databaseName, String tableName, List<String> strList, String databaseConfigId) throws Exception {
        Iterator<String> it = strList.iterator();
        while (it.hasNext()) {
            String str1 = it.next();
            if (str1 == null || "".equals(str1)) {
                throw new Exception("数据不完整,保存失败!");
            }
            String sql = " update  " + tableName + str1;
            LogUtil.i(String.valueOf(DateUtils.getDateTime()) + " update语句 =" + sql);
            this.permissionDao.executeSqlNotRes(sql, databaseName, databaseConfigId);
        }
        return 0;
    }

    public int updateTableNullAbleForOracle(String databaseName, String tableName, String column_name, String is_nullable, String databaseConfigId) throws Exception {
        return this.oracleDao.updateTableNullAbleForOracle(databaseName, tableName, column_name, is_nullable, databaseConfigId);
    }

    public int savePrimaryKeyForOracle(String databaseName, String tableName, String column_name, String column_key, String databaseConfigId) throws Exception {
        return this.oracleDao.savePrimaryKeyForOracle(databaseName, tableName, column_name, column_key, databaseConfigId);
    }

    public int saveDesginColumnForOracle(Map map, String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.oracleDao.saveDesginColumnForOracle(map, databaseName, tableName, databaseConfigId);
    }

    @Transactional
    public int updateTableColumnForOracle(String updated, String databaseName, String tableName, String databaseConfigId) throws Exception {
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
                this.oracleDao.updateTableColumnForOracle(maps, databaseName, tableName, "column_name", idValues, databaseConfigId);
            }
            return 0;
        }
        return 0;
    }

    public int deleteTableColumnForOracle(String databaseName, String tableName, String[] ids, String databaseConfigId) throws Exception {
        return this.oracleDao.deleteTableColumnForOracle(databaseName, tableName, ids, databaseConfigId);
    }

    public int saveRowsForOracle(Map map, String databaseName, String tableName, String databaseConfigId, String username, String ip) throws Exception {
        return this.oracleDao.saveRowsForOracle(map, databaseName, tableName, databaseConfigId, username, ip);
    }

    public String selectColumnTypeForOracle(String databaseName, String tableName, String column, String databaseConfigId) throws Exception {
        return this.oracleDao.selectColumnTypeForOracle(databaseName, tableName, column, databaseConfigId);
    }

    public int deleteRowsNewForOracle(String databaseName, String tableName, String primary_key, List<String> condition, String databaseConfigId, String username, String ip) throws Exception {
        return this.oracleDao.deleteRowsNewForOracle(databaseName, tableName, primary_key, condition, databaseConfigId, username, ip);
    }

    public boolean backupDatabaseExecuteForOracle(String databaseName, String tableName, String path, String databaseConfigId) throws Exception {
        return this.oracleDao.backupDatabaseExecuteForOracle(databaseName, tableName, path, databaseConfigId);
    }

    public boolean copyTableForOracle(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.oracleDao.copyTableForOracle(databaseName, tableName, databaseConfigId);
    }

    public boolean renameTableForOracle(String databaseName, String tableName, String databaseConfigId, String newTableName) throws Exception {
        return this.oracleDao.renameTableForOracle(databaseName, tableName, databaseConfigId, newTableName);
    }

    public List<Map<String, Object>> exportDataToSQLForOracle(String databaseName, String tableName, List<String> condition, String databaseConfigId) throws Exception {
        return this.oracleDao.exportDataToSQLForOracle(databaseName, tableName, condition, databaseConfigId);
    }

    public boolean dropTableForOracle(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.oracleDao.dropTableForOracle(databaseName, tableName, databaseConfigId);
    }

    public List<Map<String, Object>> viewTableMessForOracle(String databaseName, String tableName, String databaseConfigId) throws Exception {
        List<Map<String, Object>> list = this.oracleDao.viewTableMessForOracle(databaseName, tableName, databaseConfigId);
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

    public Map<String, Object> queryDatabaseStatusForOracle(String databaseName, String databaseConfigId) throws Exception {
        return this.oracleDao.queryDatabaseStatusForOracle(databaseName, databaseConfigId);
    }

    public Integer queryDatabaseStatusForOracleSession(String databaseName, String databaseConfigId) throws Exception {
        return this.oracleDao.queryDatabaseStatusForOracleSession(databaseName, databaseConfigId);
    }

    public List<Map<String, Object>> queryTableSpaceForOracle(String databaseName, String databaseConfigId) throws Exception {
        return this.oracleDao.queryTableSpaceForOracle(databaseName, databaseConfigId);
    }

    public List<Map<String, Object>> monitorItemValueForOracle(String databaseName, String databaseConfigId) throws Exception {
        return this.oracleDao.monitorItemValueForOracle(databaseName, databaseConfigId);
    }

    public int saveNewTableForOracle(JSONArray insertArray, String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.oracleDao.saveNewTableForOracle(insertArray, databaseName, tableName, databaseConfigId);
    }

    public boolean judgeTableExistsForOracle(Map<String, Object> jobMessageMap, String sourceDbType, String targetDbType) throws Exception {
        return this.oracleDao.judgeTableExistsForOracle(jobMessageMap, sourceDbType, targetDbType);
    }

    public boolean insertByDataListForOracle(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId) throws Exception {
        return this.oracleDao.insertByDataListForOracle(dataList, databaseName, table, databaseConfigId);
    }

    public boolean updateByDataListForOracle(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.oracleDao.updateByDataListForOracle(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public boolean insertOrUpdateByDataListForOracle(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.oracleDao.insertOrUpdateByDataListForOracle(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public boolean insertOnlyByDataListForOracle(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.oracleDao.insertOnlyByDataListForOracle(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public boolean deleteByDataListForOracle(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.oracleDao.deleteByDataListForOracle(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public String createTableSQLForOracle(String tableName, String databaseName, String databaseConfigId) throws Exception {
        return this.oracleDao.createTableSQLForOracle(tableName, databaseName, databaseConfigId);
    }

    public Map<String, Object> executeSqlHaveResForOracle(String sql, String dbName, String databaseConfigId, Page<Map<String, Object>> page) {
        String status;
        String mess;
        Map<String, Object> map = new HashMap<>();
        try {
            page = this.oracleDao.executeSqlHaveResForOracle(page, sql, dbName, databaseConfigId);
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
        map.put("operator", page.getOperator());
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    public String dataListToStringForOracle(String tableName, Map<String, String> TableColumnType, List<Map<String, Object>> dataList) {
        StringBuffer sb = new StringBuffer();
        Iterator<Map<String, Object>> it = dataList.iterator();
        while (it.hasNext()) {
            Map<String, Object> map4 = it.next();
            String tempColumnName = "";
            sb.append(" INSERT INTO " + tableName + " (");
            String values = "";
            Iterator<Map.Entry<String, Object>> it2 = map4.entrySet().iterator();
            while (it2.hasNext()) {
                Map.Entry<String, Object> entry = it2.next();
                String key = entry.getKey();
                tempColumnName = String.valueOf(tempColumnName) + key + ",";
                if (!key.equals("RN")) {
                    if (entry.getValue() == null) {
                        values = String.valueOf(values) + "null,";
                    } else if (TableColumnType.get(key).indexOf("TIMESTAMP") >= 0) {
                        values = String.valueOf(values) + "to_timestamp( '" + entry.getValue() + "','YYYY-MM-DD HH24:MI:SS'),";
                    } else if (TableColumnType.get(key).equals("DATE") || TableColumnType.get(key).equals("DATETIME")) {
                        values = String.valueOf(values) + "to_date( '" + entry.getValue() + "','YYYY-MM-DD HH24:MI:SS'),";
                    } else if (TableColumnType.get(key).equals("int") || TableColumnType.get(key).equals("smallint") || TableColumnType.get(key).equals("tinyint") || TableColumnType.get(key).equals("integer") || TableColumnType.get(key).equals("bit") || TableColumnType.get(key).equals("NUMBER") || TableColumnType.get(key).equals("bigint") || TableColumnType.get(key).equals("long") || TableColumnType.get(key).equals("float") || TableColumnType.get(key).equals("decimal") || TableColumnType.get(key).equals("numeric") || TableColumnType.get(key).equals("mediumint")) {
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
            }
            sb.append(String.valueOf(tempColumnName.substring(0, tempColumnName.length() - 1)) + " ) VALUES ( " + values.substring(0, values.length() - 1) + " ); \r\n");
        }
        return sb.toString();
    }

    public Map<String, Object> queryExplainSQLForOracle(DmsDto dto) throws Exception {
        Date b1 = new Date();
        Map<String, Object> resultMap = new HashMap<>();
        List<Map<String, Object>> list = this.oracleDao.queryExplainSQLForOracle(dto);
        Date b2 = new Date();
        List<Map<String, Object>> columnList = new ArrayList<>();
        if (list.size() > 0) {
            Map<String, Object> oneRow = list.get(0);
            Iterator<Map.Entry<String, Object>> it = oneRow.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> m = it.next();
                Map<String, Object> map2 = new HashMap<>();
                map2.put("field", m.getKey());
                map2.put("title", m.getKey());
                map2.put("sortable", false);
                columnList.add(map2);
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        String jsonfromList = "[" + mapper.writeValueAsString(columnList) + "]";
        long executeTime = b2.getTime() - b1.getTime();
        resultMap.put("rows", list);
        resultMap.put("total", Integer.valueOf(list.size()));
        resultMap.put("columns", jsonfromList);
        resultMap.put("primaryKey", null);
        resultMap.put("tableName", null);
        resultMap.put("totalCount", Integer.valueOf(list.size()));
        resultMap.put("time", Long.valueOf(executeTime));
        resultMap.put("mess", "执行成功");
        resultMap.put("status", "success");
        return resultMap;
    }

    public Page<Map<String, Object>> selectTableIndexsForOracle(Page<Map<String, Object>> page, DmsDto dto) throws Exception {
        return this.oracleDao.selectTableIndexsForOracle(page, dto);
    }

    public boolean indexSaveForOracle(HttpServletRequest request, IdsDto dto, String username) throws Exception {
        return this.oracleDao.indexSaveForOracle(request, dto, username);
    }

    public boolean indexDeleteForOracle(HttpServletRequest request, IdsDto dto, String username) throws Exception {
        return this.oracleDao.indexDeleteForOracle(request, dto, username);
    }

    public Page<Map<String, Object>> selectTableTriggersForOracle(Page<Map<String, Object>> page, DmsDto dto) throws Exception {
        return this.oracleDao.selectTableTriggersForOracle(page, dto);
    }

    public boolean triggerDeleteForOracle(HttpServletRequest request, IdsDto dto, String username) throws Exception {
        return this.oracleDao.triggerDeleteForOracle(request, dto, username);
    }
}
