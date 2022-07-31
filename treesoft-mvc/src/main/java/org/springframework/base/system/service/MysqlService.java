package org.springframework.base.system.service;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.base.system.dao.MysqlDao;
import org.springframework.base.system.entity.DmsDto;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.utils.DateUtils;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/service/MysqlService.class */
public class MysqlService {
    @Autowired
    private MysqlDao mysqlDao;

    public List<Map<String, Object>> getAllDataBase(String databaseConfigId) throws Exception {
        return this.mysqlDao.getAllDataBase(databaseConfigId);
    }

    public List<Map<String, Object>> getAllTables(String dbName, String databaseConfigId) throws Exception {
        return this.mysqlDao.getAllTables(dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getAllViews(String dbName, String databaseConfigId) throws Exception {
        return this.mysqlDao.getAllViews(dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getTableColumns(String dbName, String tableName, String databaseConfigId) throws Exception {
        return this.mysqlDao.getTableColumns(dbName, tableName, databaseConfigId);
    }

    public List<Map<String, Object>> getAllFuntion(String dbName, String databaseConfigId) throws Exception {
        return this.mysqlDao.getAllFuntion(dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getAllFuntionForMySql8(String dbName, String databaseConfigId) throws Exception {
        return this.mysqlDao.getAllFuntionForMySql8(dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getAllProc(String dbName, String databaseConfigId) throws Exception {
        return this.mysqlDao.getAllProc(dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getAllTrigger(String dbName, String databaseConfigId) throws Exception {
        return this.mysqlDao.getAllTrigger(dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getAllEvent(String dbName, String databaseConfigId) throws Exception {
        return this.mysqlDao.getAllEvent(dbName, databaseConfigId);
    }

    public int saveRows(Map map, String databaseName, String tableName, String databaseConfigId, String username, String ip) throws Exception {
        return this.mysqlDao.saveRows(map, databaseName, tableName, databaseConfigId, username, ip);
    }

    public List<Map<String, Object>> getTableColumns3(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.mysqlDao.getTableColumns3(databaseName, tableName, databaseConfigId);
    }

    public int saveDesginColumn(Map map, String databaseName, String tableName, String databaseConfigId, String username, String ip) throws Exception {
        return this.mysqlDao.saveDesginColumn(map, databaseName, tableName, databaseConfigId, username, ip);
    }

    public int deleteTableColumn(String databaseName, String tableName, String[] ids, String databaseConfigId, String username, String ip) throws Exception {
        return this.mysqlDao.deleteTableColumn(databaseName, tableName, ids, databaseConfigId, username, ip);
    }

    public int savePrimaryKey(String databaseName, String tableName, String column_name, String column_key, String databaseConfigId) throws Exception {
        return this.mysqlDao.savePrimaryKey(databaseName, tableName, column_name, column_key, databaseConfigId);
    }

    public List<Map<String, Object>> selectAllDataFromSQLForMysql(String databaseName, String databaseConfigId, String sql, int limitFrom, int pageSize) throws Exception {
        return this.mysqlDao.selectAllDataFromSQLForMysql(databaseName, databaseConfigId, sql, limitFrom, pageSize);
    }

    public Page<Map<String, Object>> getDataForMySql(Page<Map<String, Object>> page, String tableName, String dbName, String databaseConfigId) throws Exception {
        return this.mysqlDao.getDataForMySql(page, tableName, dbName, databaseConfigId);
    }

    public String getProcedureSqlForMySql(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.mysqlDao.getProcedureSqlForMySql(databaseName, tableName, databaseConfigId);
    }

    public String getViewSqlForMySql8(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.mysqlDao.getViewSqlForMySql8(databaseName, tableName, databaseConfigId);
    }

    public String selectColumnTypeForMySql(String databaseName, String tableName, String column, String databaseConfigId) throws Exception {
        return this.mysqlDao.selectOneColumnType(databaseName, tableName, column, databaseConfigId);
    }

    public boolean copyTableForMySql(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.mysqlDao.copyTableForMySql(databaseName, tableName, databaseConfigId);
    }

    public boolean renameTableForMySql(String databaseName, String tableName, String databaseConfigId, String newTableName) throws Exception {
        return this.mysqlDao.renameTableForMySql(databaseName, tableName, databaseConfigId, newTableName);
    }

    public List<Map<String, Object>> exportDataToSQLForMySQL(String databaseName, String tableName, List<String> condition, String databaseConfigId) throws Exception {
        return this.mysqlDao.exportDataToSQLForMySQL(databaseName, tableName, condition, databaseConfigId);
    }

    public boolean backupDatabaseExecute(String databaseName, String tableName, String path, String databaseConfigId) throws Exception {
        return this.mysqlDao.backupDatabaseExecute(databaseName, tableName, path, databaseConfigId);
    }

    public List<Map<String, Object>> viewTableMessForMySql(String databaseName, String tableName, String databaseConfigId) throws Exception {
        new ArrayList();
        List<Map<String, Object>> listAllColumn2 = new ArrayList<>();
        List<Map<String, Object>> listAllColumn = this.mysqlDao.viewTableMessForMySql(databaseName, tableName, databaseConfigId);
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
            Integer rowCount = this.mysqlDao.getTableRows(databaseName, tableName, databaseConfigId);
            tempMap4.put("propValue", rowCount);
            listAllColumn2.add(tempMap4);
            Map<String, Object> tempMap5 = new HashMap<>();
            tempMap5.put("propName", "表类型");
            tempMap5.put("propValue", map.get("ENGINE"));
            listAllColumn2.add(tempMap5);
            Map<String, Object> tempMap6 = new HashMap<>();
            tempMap6.put("propName", "自动递增数值");
            tempMap6.put("propValue", map.get("AUTO_INCREMENT"));
            listAllColumn2.add(tempMap6);
            Map<String, Object> tempMap7 = new HashMap<>();
            tempMap7.put("propName", "栏格式");
            tempMap7.put("propValue", map.get("ROW_FORMAT"));
            listAllColumn2.add(tempMap7);
            Map<String, Object> tempMap8 = new HashMap<>();
            tempMap8.put("propName", "刷新时间");
            if (map.get("UPDATE_TIME") != null) {
                tempMap8.put("propValue", DateUtils.formatDateTime((Date) map.get("UPDATE_TIME")));
            } else {
                tempMap8.put("propValue", "");
            }
            listAllColumn2.add(tempMap8);
            Map<String, Object> tempMap9 = new HashMap<>();
            tempMap9.put("propName", "创建时间");
            if (map.get("CREATE_TIME") != null) {
                tempMap9.put("propValue", DateUtils.formatDateTime((Date) map.get("CREATE_TIME")));
            } else {
                tempMap9.put("propValue", "");
            }
            listAllColumn2.add(tempMap9);
            Map<String, Object> tempMap10 = new HashMap<>();
            tempMap10.put("propName", "校验时间");
            tempMap10.put("propValue", map.get("CHECK_TIME"));
            listAllColumn2.add(tempMap10);
            Map<String, Object> tempMap11 = new HashMap<>();
            tempMap11.put("propName", "索引长度");
            tempMap11.put("propValue", map.get("INDEX_LENGTH"));
            listAllColumn2.add(tempMap11);
            Map<String, Object> tempMap12 = new HashMap<>();
            tempMap12.put("propName", "数据长度");
            tempMap12.put("propValue", map.get("DATA_LENGTH"));
            listAllColumn2.add(tempMap12);
            Map<String, Object> tempMap13 = new HashMap<>();
            tempMap13.put("propName", "最大数据长度");
            tempMap13.put("propValue", map.get("MAX_DATA_LENGTH"));
            listAllColumn2.add(tempMap13);
            Map<String, Object> tempMap14 = new HashMap<>();
            tempMap14.put("propName", "数据空闲");
            tempMap14.put("propValue", map.get("DATA_FREE"));
            listAllColumn2.add(tempMap14);
            Map<String, Object> tempMap15 = new HashMap<>();
            tempMap15.put("propName", "整理");
            tempMap15.put("propValue", map.get("TABLE_COLLATION"));
            listAllColumn2.add(tempMap15);
            Map<String, Object> tempMap18 = new HashMap<>();
            tempMap18.put("propName", "字符集");
            tempMap18.put("propValue", map.get("CHARACTER_SET_NAME"));
            listAllColumn2.add(tempMap18);
            Map<String, Object> tempMap16 = new HashMap<>();
            tempMap16.put("propName", "创建选项");
            tempMap16.put("propValue", map.get("CREATE_OPTIONS"));
            listAllColumn2.add(tempMap16);
            Map<String, Object> tempMap17 = new HashMap<>();
            tempMap17.put("propName", "注释");
            tempMap17.put("propValue", map.get("TABLE_COMMENT"));
            listAllColumn2.add(tempMap17);
        }
        return listAllColumn2;
    }

    public int updateTableColumn(String updated, String databaseName, String tableName, String databaseConfigId, String username, String ip) throws Exception {
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
                this.mysqlDao.updateTableColumn(maps, databaseName, tableName, "column_name", idValues, databaseConfigId, username, ip);
            }
            return 0;
        }
        return 0;
    }

    public String getViewSql(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.mysqlDao.getViewSql(databaseName, tableName, databaseConfigId);
    }

    public List<Map<String, Object>> getTableColumns2(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.mysqlDao.getTableColumns2(databaseName, tableName, databaseConfigId);
    }

    public int updateTableNullAble(String databaseName, String tableName, String column_name, String is_nullable, String databaseConfigId) throws Exception {
        return this.mysqlDao.updateTableNullAble(databaseName, tableName, column_name, is_nullable, databaseConfigId);
    }

    public int upDownColumn(String databaseName, String tableName, String column_name, String column_name2, String databaseConfigId) throws Exception {
        return this.mysqlDao.upDownColumn(databaseName, tableName, column_name, column_name2, databaseConfigId);
    }

    public boolean insertByDataListForMySQL(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId) throws Exception {
        return this.mysqlDao.insertByDataListForMySQL(dataList, databaseName, table, databaseConfigId);
    }

    public boolean judgeTableExistsForMySQL(Map<String, Object> jobMessageMap, String sourceDbType, String targetDbType) throws Exception {
        return this.mysqlDao.judgeTableExistsForMySQL(jobMessageMap, sourceDbType, targetDbType);
    }

    public boolean updateByDataListForMySQL(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.mysqlDao.updateByDataListForMySQL(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public boolean insertOrUpdateByDataListForMySQL(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.mysqlDao.insertOrUpdateByDataListForMySQL(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public boolean insertOnlyByDataListForMySQL(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.mysqlDao.insertOnlyByDataListForMySQL(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public boolean deleteByDataListForMySQL(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.mysqlDao.deleteByDataListForMySQL(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public Page<Map<String, Object>> dataStatisticsListForMySql(Page<Map<String, Object>> page, String tableName, String databaseName, String databaseConfigId) throws Exception {
        return this.mysqlDao.dataStatisticsListForMySql(page, tableName, databaseName, databaseConfigId);
    }

    public List<Map<String, Object>> allDataStatisticsListForMySql(String databaseName, String databaseConfigId, String tableName) throws Exception {
        return this.mysqlDao.allDataStatisticsListForMySql(databaseName, databaseConfigId, tableName);
    }

    public String createTableSQL(String tableName, String databaseName, String databaseConfigId) throws Exception {
        return this.mysqlDao.createTableSQL(tableName, databaseName, databaseConfigId);
    }

    public int executeQueryForCountForMySQL(String sql, String databaseName, String databaseConfigId) throws Exception {
        return this.mysqlDao.executeQueryForCountForMySQL(sql, databaseName, databaseConfigId);
    }

    public int executeQueryForMaxKeyForMySQL(String sql, String databaseName, String databaseConfigId) throws Exception {
        return this.mysqlDao.executeQueryForMaxKeyForMySQL(sql, databaseName, databaseConfigId);
    }

    public Map<String, Object> queryDatabaseStatus(String databaseName, String databaseConfigId) throws Exception {
        return this.mysqlDao.queryDatabaseStatus(databaseName, databaseConfigId);
    }

    public Map<String, Object> queryExplainSQLForMysql(DmsDto dto) throws Exception {
        Date b1 = new Date();
        Map<String, Object> resultMap = new HashMap<>();
        List<Map<String, Object>> list = this.mysqlDao.queryExplainSQLForMysql(dto);
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

    public String dataListToStringForMySQL(String tableName, Map<String, String> TableColumnType, List<Map<String, Object>> dataList) {
        StringBuffer sb = new StringBuffer();
        Iterator<Map<String, Object>> it = dataList.iterator();
        while (it.hasNext()) {
            Map<String, Object> map2 = it.next();
            String tempColumnName = "";
            String values = "";
            sb.append(" INSERT INTO " + tableName + " (");
            Iterator<Map.Entry<String, Object>> it2 = map2.entrySet().iterator();
            while (it2.hasNext()) {
                Map.Entry<String, Object> entry = it2.next();
                String key = entry.getKey();
                tempColumnName = String.valueOf(tempColumnName) + key + ",";
                if (entry.getValue() == null) {
                    values = String.valueOf(values) + "null,";
                } else if (TableColumnType.get(key).equals("int") || TableColumnType.get(key).equals("smallint") || TableColumnType.get(key).equals("tinyint") || TableColumnType.get(key).equals("integer") || TableColumnType.get(key).equals("bit") || TableColumnType.get(key).equals("bigint") || TableColumnType.get(key).equals("double") || TableColumnType.get(key).equals("float") || TableColumnType.get(key).equals("decimal") || TableColumnType.get(key).equals("numeric") || TableColumnType.get(key).equals("mediumint")) {
                    values = String.valueOf(values) + entry.getValue() + ",";
                } else if (TableColumnType.get(key).equals("binary") || TableColumnType.get(key).equals("varbinary") || TableColumnType.get(key).equals("blob") || TableColumnType.get(key).equals("tinyblob") || TableColumnType.get(key).equals("mediumblob") || TableColumnType.get(key).equals("longblob")) {
                    values = String.valueOf(values) + entry.getValue() + ",";
                } else if (TableColumnType.get(key).equals("date") || TableColumnType.get(key).equals("datetime")) {
                    values = String.valueOf(values) + "'" + entry.getValue() + "',";
                } else {
                    String tempValues = (String) entry.getValue();
                    values = String.valueOf(values) + "'" + tempValues.replace("'", "\\'").replace("\r\n", "\\r\\n").replace("\n\r", "\\n\\r").replace("\r", "\\r").replace("\n", "\\n") + "',";
                }
            }
            sb.append(String.valueOf(tempColumnName.substring(0, tempColumnName.length() - 1)) + " ) VALUES ( " + values.substring(0, values.length() - 1) + " ); \r\n");
        }
        return sb.toString();
    }

    public Map<String, Object> executeSqlHaveResultForMySql(String sql, String dbName, String databaseConfigId, Page<Map<String, Object>> page) {
        String status;
        String mess;
        Map<String, Object> map = new HashMap<>();
        try {
            page = this.mysqlDao.executeSqlHaveResultForMySql(page, sql, dbName, databaseConfigId);
            mess = "执行成功！";
            status = "success";
        } catch (Exception e) {
            LogUtil.e("SQL执行出错, ", e);
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
}
