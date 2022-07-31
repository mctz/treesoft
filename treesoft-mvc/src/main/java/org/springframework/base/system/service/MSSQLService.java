package org.springframework.base.system.service;

import com.alibaba.fastjson.JSONArray;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.base.system.dao.MSSQLDao;
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
import org.springframework.util.StringUtils;

@Service
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/service/MSSQLService.class */
public class MSSQLService {
    @Autowired
    private MSSQLDao msSQLDao;
    @Autowired
    private PermissionDao permissionDao;

    public List<Map<String, Object>> selectAllDataFromSQLForMSSQL(String databaseName, String databaseConfigId, String sql, int limitFrom, int pageSize) throws Exception {
        return this.msSQLDao.selectAllDataFromSQLForMSSQL(databaseName, databaseConfigId, sql, limitFrom, pageSize);
    }

    public String getProcedureSqlForMSSQL(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.msSQLDao.getProcedureSqlForMSSQL(databaseName, tableName, databaseConfigId);
    }

    public String selectColumnTypeForMSSQL(String databaseName, String tableName, String column, String databaseConfigId) throws Exception {
        return this.msSQLDao.selectOneColumnTypeForMSSQL(databaseName, tableName, column, databaseConfigId);
    }

    public List<Map<String, Object>> getAllProcedureForMSSQL(String dbName, String databaseConfigId) throws Exception {
        return this.msSQLDao.getAllProcedureForMSSQL(dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getAllDataBaseForMSSQL(String databaseConfigId) throws Exception {
        return this.msSQLDao.getAllDataBaseForMSSQL(databaseConfigId);
    }

    public List<Map<String, Object>> getAllTablesForMSSQL(String dbName, String databaseConfigId) throws Exception {
        return this.msSQLDao.getAllTablesForMSSQL(dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getTableColumns3ForMSSQL(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.msSQLDao.getTableColumns3ForMSSQL(databaseName, tableName, databaseConfigId);
    }

    public List<Map<String, Object>> getAllViewsForMSSQL(String dbName, String databaseConfigId) throws Exception {
        return this.msSQLDao.getAllViewsForMSSQL(dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getAllFuntionForMSSQL(String dbName, String databaseConfigId) throws Exception {
        return this.msSQLDao.getAllFuntionForMSSQL(dbName, databaseConfigId);
    }

    public Page<Map<String, Object>> getDataForMSSQL(Page<Map<String, Object>> page, String tableName, String dbName, String databaseConfigId) throws Exception {
        return this.msSQLDao.getDataForMSSQL(page, tableName, dbName, databaseConfigId);
    }

    public int deleteRowsNewForMSSQL(String databaseName, String tableName, String primary_key, List<String> condition, String databaseConfigId, String username, String ip) throws Exception {
        return this.msSQLDao.deleteRowsNewForMSSQL(databaseName, tableName, primary_key, condition, databaseConfigId, username, ip);
    }

    public String getViewSqlForMSSQL(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.msSQLDao.getViewSqlForMSSQL(databaseName, tableName, databaseConfigId);
    }

    public int saveRowsForMSSQL(Map map, String databaseName, String tableName, String databaseConfigId, String username, String ip) throws Exception {
        return this.msSQLDao.saveRowsForMSSQL(map, databaseName, tableName, databaseConfigId, username, ip);
    }

    public int updateRowsNewForMSSQL(String databaseName, String tableName, List<String> strList, String databaseConfigId) throws Exception {
        Iterator<String> it = strList.iterator();
        while (it.hasNext()) {
            String str1 = it.next();
            if (str1 == null || "".equals(str1)) {
                throw new Exception("数据不完整,保存失败!");
            }
            String sql = " update top (1) " + tableName + str1;
            LogUtil.i(String.valueOf(DateUtils.getDateTime()) + " update语句 =" + sql);
            this.permissionDao.executeSqlNotRes(sql, databaseName, databaseConfigId);
        }
        return 0;
    }

    public int saveDesginColumnForMSSQL(Map map, String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.msSQLDao.saveDesginColumnForMSSQL(map, databaseName, tableName, databaseConfigId);
    }

    @Transactional
    public int updateTableColumnForMSSQL(String updated, String databaseName, String tableName, String databaseConfigId) throws Exception {
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
                this.msSQLDao.updateTableColumnForMSSQL(maps, databaseName, tableName, "column_name", idValues, databaseConfigId);
            }
            return 0;
        }
        return 0;
    }

    public int deleteTableColumnForMSSQL(String databaseName, String tableName, String[] ids, String databaseConfigId) throws Exception {
        return this.msSQLDao.deleteTableColumnForMSSQL(databaseName, tableName, ids, databaseConfigId);
    }

    public int updateTableNullAbleForMSSQL(String databaseName, String tableName, String column_name, String is_nullable, String databaseConfigId) throws Exception {
        return this.msSQLDao.updateTableNullAbleForMSSQL(databaseName, tableName, column_name, is_nullable, databaseConfigId);
    }

    public int savePrimaryKeyForMSSQL(String databaseName, String tableName, String column_name, String column_key, String databaseConfigId) throws Exception {
        return this.msSQLDao.savePrimaryKeyForMSSQL(databaseName, tableName, column_name, column_key, databaseConfigId);
    }

    public boolean backupDatabaseExecuteForMSSQL(String databaseName, String tableName, String path, String databaseConfigId) throws Exception {
        return this.msSQLDao.backupDatabaseExecuteForMSSQL(databaseName, tableName, path, databaseConfigId);
    }

    public boolean copyTableForMSSQL(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.msSQLDao.copyTableForMSSQL(databaseName, tableName, databaseConfigId);
    }

    public List<Map<String, Object>> exportDataToSQLForMSSQL(String databaseName, String tableName, List<String> condition, String databaseConfigId) throws Exception {
        return this.msSQLDao.exportDataToSQLForMSSQL(databaseName, tableName, condition, databaseConfigId);
    }

    public boolean dropTableForMSSQL(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.msSQLDao.dropTableForMSSQL(databaseName, tableName, databaseConfigId);
    }

    public List<Map<String, Object>> viewTableMessForMSSQL(String databaseName, String tableName, String databaseConfigId) throws Exception {
        new ArrayList();
        List<Map<String, Object>> listAllColumn2 = new ArrayList<>();
        List<Map<String, Object>> listAllColumn = this.msSQLDao.viewTableMessForMSSQL(databaseName, tableName, databaseConfigId);
        if (listAllColumn.size() > 0) {
            Map<String, Object> map = listAllColumn.get(0);
            Map<String, Object> tempMap = new HashMap<>();
            tempMap.put("propName", "表名");
            tempMap.put("propValue", tableName);
            listAllColumn2.add(tempMap);
            Map<String, Object> tempMap2 = new HashMap<>();
            tempMap2.put("propName", "数据库");
            tempMap2.put("propValue", databaseName);
            listAllColumn2.add(tempMap2);
            Map<String, Object> tempMap4 = new HashMap<>();
            tempMap4.put("propName", "总记录数");
            tempMap4.put("propValue", map.get("rows"));
            listAllColumn2.add(tempMap4);
            Map<String, Object> tempMap6 = new HashMap<>();
            tempMap6.put("propName", "自动递增数值");
            tempMap6.put("propValue", map.get("indid"));
            listAllColumn2.add(tempMap6);
            Map<String, Object> tempMap7 = new HashMap<>();
            tempMap7.put("propName", "状态");
            tempMap7.put("propValue", map.get("status"));
            listAllColumn2.add(tempMap7);
            Map<String, Object> tempMap8 = new HashMap<>();
            tempMap8.put("propName", "刷新时间");
            tempMap8.put("propValue", map.get("refdate"));
            listAllColumn2.add(tempMap8);
            Map<String, Object> tempMap9 = new HashMap<>();
            tempMap9.put("propName", "创建时间");
            tempMap9.put("propValue", map.get("crdate"));
            listAllColumn2.add(tempMap9);
            Map<String, Object> tempMap11 = new HashMap<>();
            tempMap11.put("propName", "索引长度");
            tempMap11.put("propValue", map.get("indid"));
            listAllColumn2.add(tempMap11);
            Map<String, Object> tempMap12 = new HashMap<>();
            tempMap12.put("propName", "数据长度");
            tempMap12.put("propValue", map.get("rowcnt"));
            listAllColumn2.add(tempMap12);
            Map<String, Object> tempMap13 = new HashMap<>();
            tempMap13.put("propName", "最大数据长度");
            tempMap13.put("propValue", map.get("maxlen"));
            listAllColumn2.add(tempMap13);
        }
        return listAllColumn2;
    }

    public int saveNewTableForMSSQL(JSONArray insertArray, String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.msSQLDao.saveNewTableForMSSQL(insertArray, databaseName, tableName, databaseConfigId);
    }

    public boolean judgeTableExistsForMSSQL(Map<String, Object> jobMessageMap, String sourceDbType, String targetDbType) throws Exception {
        return this.msSQLDao.judgeTableExistsForMSSQL(jobMessageMap, sourceDbType, targetDbType);
    }

    public boolean insertByDataListForMSSQL(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId) throws Exception {
        return this.msSQLDao.insertByDataListForMSSQL(dataList, databaseName, table, databaseConfigId);
    }

    public boolean updateByDataListForMSSQL(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.msSQLDao.updateByDataListForMSSQL(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public boolean insertOrUpdateByDataListForMSSQL(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.msSQLDao.insertOrUpdateByDataListForMSSQL(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public boolean insertOnlyByDataListForMSSQL(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.msSQLDao.insertOnlyByDataListForMSSQL(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public boolean deleteByDataListForMSSQL(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.msSQLDao.deleteByDataListForMSSQL(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public String createTableSQLForMSSQL(String tableName, String databaseName, String databaseConfigId) throws Exception {
        return this.msSQLDao.createTableSQLForMSSQL(tableName, databaseName, databaseConfigId);
    }

    public Map<String, Object> executeSqlHaveResForMSSQL(String sql, String dbName, String databaseConfigId, Page<Map<String, Object>> page) {
        String status;
        String mess;
        Map<String, Object> map = new HashMap<>();
        Date b1 = new Date();
        try {
            page = this.msSQLDao.executeSqlHaveResForMSSQL(page, sql, dbName, databaseConfigId);
            mess = "执行成功！";
            status = "success";
        } catch (Exception e) {
            LogUtil.e("SQL执行出错,", e);
            mess = "执行失败, " + e.getMessage();
            status = "fail";
        }
        Date b2 = new Date();
        long y = b2.getTime() - b1.getTime();
        map.put("rows", page.getResult());
        map.put("total", Long.valueOf(page.getTotalCount()));
        map.put("columns", page.getColumns());
        map.put("primaryKey", page.getPrimaryKey());
        map.put("tableName", page.getTableName());
        map.put("totalCount", Long.valueOf(page.getTotalCount()));
        map.put("time", Long.valueOf(y));
        map.put("operator", page.getOperator());
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    public String dataListToStringForMSSQL(String tableName, Map<String, String> TableColumnType, List<Map<String, Object>> dataList) {
        StringBuffer sb = new StringBuffer();
        Iterator<Map<String, Object>> it = dataList.iterator();
        while (it.hasNext()) {
            Map<String, Object> map4 = it.next();
            String tempColumnName = "";
            sb.append("INSERT INTO " + tableName + " ( ");
            String values = "";
            Iterator<Map.Entry<String, Object>> it2 = map4.entrySet().iterator();
            while (it2.hasNext()) {
                Map.Entry<String, Object> entry = it2.next();
                String key = entry.getKey();
                tempColumnName = String.valueOf(tempColumnName) + key + ",";
                if (entry.getValue() == null) {
                    values = String.valueOf(values) + "null,";
                } else if (TableColumnType.get(key).equals("date") || TableColumnType.get(key).equals("datetime")) {
                    values = String.valueOf(values) + "'" + entry.getValue() + "',";
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
            sb.append(String.valueOf(tempColumnName.substring(0, tempColumnName.length() - 1)) + " ) VALUES ( " + values.substring(0, values.length() - 1) + " ); \r\n");
        }
        return sb.toString();
    }

    public Map<String, Object> executeSqlProcedureForMSSQL(String sql, String dbName, String databaseConfigId, Page<Map<String, Object>> page) {
        String status;
        Object mess;
        Map<String, Object> map = new HashMap<>();
        Date b1 = new Date();
        try {
            page = this.msSQLDao.executeSqlProcedureForMSSQL(page, sql, dbName, databaseConfigId);
            mess = "执行完成！";
            status = "success";
        } catch (Exception e) {
            LogUtil.e("SQL执行出错,", e);
            mess = "执行失败, " + e.getMessage();
            status = "fail";
        }
        Date b2 = new Date();
        long y = b2.getTime() - b1.getTime();
        if (page == null) {
            map.put("totalCount", "");
            map.put("time", Long.valueOf(y));
            map.put("mess", mess);
            map.put("status", status);
            return map;
        }
        map.put("rows", page.getResult());
        map.put("total", Long.valueOf(page.getTotalCount()));
        map.put("columns", page.getColumns());
        map.put("primaryKey", page.getPrimaryKey());
        if (!StringUtils.isEmpty(page.getPrimaryKey())) {
            map.put("tableName", page.getTableName());
        }
        map.put("totalCount", Long.valueOf(page.getTotalCount()));
        map.put("time", Long.valueOf(y));
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    public Page<Map<String, Object>> selectTableIndexsForMSSQL(Page<Map<String, Object>> page, DmsDto dto) throws Exception {
        return this.msSQLDao.selectTableIndexsForMSSQL(page, dto);
    }

    public boolean indexSaveForMSSQL(HttpServletRequest request, IdsDto dto, String username) throws Exception {
        return this.msSQLDao.indexSaveForMSSQL(request, dto, username);
    }

    public boolean indexDeleteForMSSQL(HttpServletRequest request, IdsDto dto, String username) throws Exception {
        return this.msSQLDao.indexDeleteForMSSQL(request, dto, username);
    }

    public Page<Map<String, Object>> selectTableTriggersForMSSQL(Page<Map<String, Object>> page, DmsDto dto) throws Exception {
        return this.msSQLDao.selectTableTriggersForMSSQL(page, dto);
    }

    public boolean triggerDeleteForMSSQL(HttpServletRequest request, IdsDto dto, String username) throws Exception {
        return this.msSQLDao.triggerDeleteForMSSQL(request, dto, username);
    }

    public Integer queryDatabaseStatusForMSSQLSession(String databaseName, String databaseConfigId) throws Exception {
        return this.msSQLDao.queryDatabaseStatusForMSSQLSession(databaseName, databaseConfigId);
    }
}
