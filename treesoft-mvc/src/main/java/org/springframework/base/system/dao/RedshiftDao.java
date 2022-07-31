package org.springframework.base.system.dao;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.base.system.dbUtils.DBUtil2;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.utils.DataUtil;
import org.springframework.base.system.utils.DateUtils;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/dao/RedshiftDao.class */
public class RedshiftDao {
    @Autowired
    private LogDao logDao;
    @Autowired
    private ConfigDao configDao;

    public List<Map<String, Object>> getAllDataBaseForRedshift(String databaseConfigId) throws Exception {
        Map<String, Object> map0 = this.configDao.getConfigById(databaseConfigId);
        String databaseName = (String) map0.get("databaseName");
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list2 = db.queryForListCommonMethod(" select  datname  from pg_database  where  datname not like 'template%'  order by datname ");
        List<Map<String, Object>> list3 = new ArrayList<>();
        Iterator<Map<String, Object>> it = list2.iterator();
        while (it.hasNext()) {
            Map<String, Object> map = it.next();
            map.put("SCHEMA_NAME", map.get("datname"));
            list3.add(map);
        }
        return list3;
    }

    public List<Map<String, Object>> getAllTablesForRedshift(String dbName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(" select  tablename  from pg_tables where schemaname not in ('pg_catalog','pg_internal','information_schema')  order by tablename ");
        List<Map<String, Object>> list3 = new ArrayList<>();
        Iterator<Map<String, Object>> it = list.iterator();
        while (it.hasNext()) {
            Map<String, Object> map = it.next();
            map.put("TABLE_NAME", map.get("tablename"));
            list3.add(map);
        }
        return list3;
    }

    public List<Map<String, Object>> getTableColumns3ForRedshift(String dbName, String tableName, String databaseConfigId) throws Exception {
        String sql = "   select t1.column_name as \"TREESOFTPRIMARYKEY\", t1.COLUMN_NAME as \"COLUMN_NAME\", t1.DATA_TYPE   as \"COLUMN_TYPE\" , t1.DATA_TYPE as \"DATA_TYPE\" , character_maximum_length as \"CHARACTER_MAXIMUM_LENGTH\" ,   t1.IS_NULLABLE as \"IS_NULLABLE\" ,  '' as \"COLUMN_COMMENT\" , CASE  WHEN t2.COLUMN_NAME IS NULL THEN ''  ELSE 'PRI'  END AS \"COLUMN_KEY\"   from information_schema.columns t1    left join information_schema.constraint_column_usage t2    on t1.table_name = t2.table_name  and t1.COLUMN_NAME = t2.COLUMN_NAME where  t1.table_name='" + tableName + "'    ";
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        return list;
    }

    public List<Map<String, Object>> getAllViewsForRedshift(String dbName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(" select viewname  from pg_views  where schemaname='public'  order by  viewname ");
        List<Map<String, Object>> list3 = new ArrayList<>();
        Iterator<Map<String, Object>> it = list.iterator();
        while (it.hasNext()) {
            Map<String, Object> map = it.next();
            map.put("TABLE_NAME", map.get("viewname"));
            list3.add(map);
        }
        return list3;
    }

    public List<Map<String, Object>> getAllFuntionForRedshift(String dbName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod("  select prosrc  from pg_proc where 1=2  ");
        List<Map<String, Object>> list3 = new ArrayList<>();
        Iterator<Map<String, Object>> it = list.iterator();
        while (it.hasNext()) {
            Map<String, Object> map = it.next();
            map.put("ROUTINE_NAME", map.get("prosrc"));
            list3.add(map);
        }
        return list3;
    }

    public Page<Map<String, Object>> getDataForRedshift(Page<Map<String, Object>> page, String tableName, String dbName, String databaseConfigId) throws Exception {
        String sql2;
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        String orderBy = page.getOrderBy();
        String order = page.getOrder();
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        String sql = "select * from  " + tableName;
        if (orderBy == null || orderBy.equals("")) {
            sql2 = "select  *  from  " + tableName + "  LIMIT " + pageSize + " OFFSET  " + limitFrom;
        } else {
            sql2 = "select  *  from  " + tableName + " order by " + orderBy + " " + order + "  LIMIT " + pageSize + "  OFFSET " + limitFrom;
        }
        List<Map<String, Object>> list = db.queryForListForMySql(sql2);
        int rowCount = db.executeQueryForCountForMySQL(sql);
        List<Map<String, Object>> columns = getTableColumnsForRedshift(dbName, tableName, databaseConfigId);
        List<Map<String, Object>> tempList = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<>();
        map1.put("field", "treeSoftPrimaryKey");
        map1.put("checkbox", true);
        tempList.add(map1);
        Iterator<Map<String, Object>> it = columns.iterator();
        while (it.hasNext()) {
            Map<String, Object> map = it.next();
            Map<String, Object> map2 = new HashMap<>();
            map2.put("field", map.get("column_name"));
            map2.put("title", map.get("column_name"));
            map2.put("sortable", true);
            tempList.add(map2);
        }
        ObjectMapper mapper = new ObjectMapper();
        String jsonfromList = "[" + mapper.writeValueAsString(tempList) + "]";
        page.setTotalCount(rowCount);
        page.setResult(list);
        page.setColumns(jsonfromList);
        page.setPrimaryKey("");
        page.setOperator("read");
        return page;
    }

    public List<Map<String, Object>> getTableColumnsForRedshift(String dbName, String tableName, String databaseConfigId) throws Exception {
        String sql = "select  * from   " + tableName + " limit 1 ";
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForColumnOnly(sql);
        return list;
    }

    public Page<Map<String, Object>> executeSqlHaveResForRedshift(Page<Map<String, Object>> page, String sql, String dbName, String databaseConfigId) throws Exception {
        int rowCount;
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        String sql2 = "select  *  from  (" + sql + ") t  LIMIT " + pageSize + " OFFSET  " + limitFrom;
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        Date b1 = new Date();
        List<Map<String, Object>> list = db.queryForListForPostgreSQL(sql2);
        Date b2 = new Date();
        long executeTime = b2.getTime() - b1.getTime();
        if (list.size() < 10) {
            rowCount = list.size();
        } else {
            rowCount = db.executeQueryForCountForMySQL(sql);
        }
        List<Map<String, Object>> columns = executeSqlForColumnsForRedshift(sql, dbName, databaseConfigId);
        List<Map<String, Object>> tempList = new ArrayList<>();
        Iterator<Map<String, Object>> it = columns.iterator();
        while (it.hasNext()) {
            Map<String, Object> map = it.next();
            Map<String, Object> map2 = new HashMap<>();
            map2.put("field", map.get("column_name"));
            map2.put("title", map.get("column_name"));
            map2.put("editor", null);
            map2.put("sortable", false);
            tempList.add(map2);
        }
        ObjectMapper mapper = new ObjectMapper();
        String jsonfromList = "[" + mapper.writeValueAsString(tempList) + "]";
        page.setTotalCount(rowCount);
        page.setResult(list);
        page.setColumns(jsonfromList);
        page.setExecuteTime(new StringBuilder(String.valueOf(executeTime)).toString());
        page.setOperator("read");
        return page;
    }

    public List<Map<String, Object>> executeSqlForColumnsForRedshift(String sql, String dbName, String databaseConfigId) throws Exception {
        String sql2 = " select * from (" + sql + ") t   limit 1; ";
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.executeSqlForColumns(sql2);
        return list;
    }

    public int updateTableNullAbleForRedshift(String databaseName, String tableName, String column_name, String is_nullable, String databaseConfigId) throws Exception {
        String sql4;
        if (column_name != null && !column_name.equals("")) {
            DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
            if (is_nullable.equals("true")) {
                sql4 = " alter table  " + tableName + " alter column   " + column_name + " drop not null ";
            } else {
                sql4 = " alter table  " + tableName + " alter column   " + column_name + " set not null ";
            }
            db.setupdateData(sql4);
            return 0;
        }
        return 0;
    }

    public String selectOneColumnTypeForRedshift(String databaseName, String tableName, String column_name, String databaseConfigId) throws Exception {
        String sql = " SELECT CONCAT( CONCAT( CONCAT( DATA_TYPE_NAME ,'('  )  ,LENGTH   ) ,')'  )  as  COLUMN_TYPE   from  SYS.TABLE_COLUMNS  where SCHEMA_NAME='" + databaseName + "' AND  TABLE_NAME = '" + tableName + "' AND COLUMN_NAME = '" + column_name + "'";
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        return (String) list.get(0).get("COLUMN_TYPE");
    }

    public int savePrimaryKeyForRedshift(String databaseName, String tableName, String column_name, String isSetting, String databaseConfigId) throws Exception {
        if (column_name != null && !column_name.equals("")) {
            DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
            List<Map<String, Object>> list2 = selectTablePrimaryKeyForRedshift(databaseName, tableName, databaseConfigId);
            List<String> list3 = new ArrayList<>();
            Iterator<Map<String, Object>> it = list2.iterator();
            while (it.hasNext()) {
                Map map = it.next();
                list3.add((String) map.get("COLUMN_NAME"));
            }
            if (isSetting.equals("true")) {
                list3.add(column_name);
            } else {
                list3.remove(column_name);
            }
            String tem = list3.toString();
            String primaryKey = tem.substring(1, tem.length() - 1);
            if (list2.size() > 0) {
                String temp = (String) list2.get(0).get("CONSTRAINT_NAME");
                String sql4 = " alter table   " + tableName + " drop constraint  " + temp;
                db.setupdateData(sql4);
            }
            if (!primaryKey.equals("")) {
                String sql42 = " alter table " + tableName + " add   primary key (" + primaryKey + ") ";
                db.setupdateData(sql42);
                return 0;
            }
            return 0;
        }
        return 0;
    }

    public List<Map<String, Object>> selectTablePrimaryKeyForRedshift(String databaseName, String tableName, String databaseConfigId) throws Exception {
        String sql = " select pg_constraint.conname as \"CONSTRAINT_NAME\" ,pg_attribute.attname as \"COLUMN_NAME\" ,pg_type.typname as typename from   pg_constraint  inner join pg_class   on pg_constraint.conrelid = pg_class.oid    inner join pg_attribute on pg_attribute.attrelid = pg_class.oid    and  pg_attribute.attnum = pg_constraint.conkey[1]   inner join pg_type on pg_type.oid = pg_attribute.atttypid    where pg_class.relname = '" + tableName + "'    and pg_constraint.contype='p' ";
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        new ArrayList();
        return list;
    }

    public int saveDesginColumnForRedshift(Map<String, String> map, String databaseName, String tableName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        String sql = " alter table " + ("\"" + tableName + "\"") + " add  ";
        String sql2 = String.valueOf(String.valueOf(sql) + "\"" + map.get("COLUMN_NAME") + "\"  ") + map.get("DATA_TYPE");
        if (map.get("CHARACTER_MAXIMUM_LENGTH") != null && !map.get("CHARACTER_MAXIMUM_LENGTH").equals("")) {
            sql2 = String.valueOf(sql2) + " (" + map.get("CHARACTER_MAXIMUM_LENGTH") + ") ";
        }
        if (map.get("COLUMN_COMMENT") != null && !map.get("COLUMN_COMMENT").equals("")) {
            sql2 = String.valueOf(sql2) + " comment '" + map.get("COLUMN_COMMENT") + "'";
        }
        int y = db.setupdateData(sql2);
        return y;
    }

    public int updateTableColumnForRedshift(Map<String, Object> map, String databaseName, String tableName, String columnName, String idValues, String databaseConfigId) throws Exception {
        if (columnName == null || "".equals(columnName)) {
            throw new Exception("数据不完整,保存失败!");
        }
        if (idValues == null || "".equals(idValues)) {
            throw new Exception("数据不完整,保存失败!");
        }
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        String tableName2 = "\"" + tableName + "\"";
        String old_field_name = "\"" + map.get("TREESOFTPRIMARYKEY") + "\"";
        String column_name = "\"" + map.get("COLUMN_NAME") + "\"";
        String data_type = (String) map.get("DATA_TYPE");
        String character_maximum_length = new StringBuilder().append(map.get("CHARACTER_MAXIMUM_LENGTH")).toString();
        String column_comment = (String) map.get("COLUMN_COMMENT");
        if (!old_field_name.endsWith(column_name)) {
            String sql = " ALTER TABLE " + tableName2 + " RENAME COLUMN " + old_field_name + " to  " + column_name;
            db.setupdateData(sql);
        }
        String sql2 = " alter table  " + tableName2 + " alter column  " + column_name + " type " + data_type;
        if (character_maximum_length != null && !character_maximum_length.equals("")) {
            sql2 = String.valueOf(sql2) + " (" + character_maximum_length + ")";
        }
        int y = db.setupdateData(sql2);
        if (column_comment != null && !column_comment.equals("")) {
            String sql4 = "  comment on column " + tableName2 + "." + column_name + " is '" + column_comment + "' ";
            db.setupdateData(sql4);
        }
        return y;
    }

    public int deleteTableColumnForRedshift(String databaseName, String tableName, String[] ids, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        int y = 0;
        for (int i = 0; i < ids.length; i++) {
            String sql = " alter table   " + tableName + " drop (" + ids[i] + ")";
            y += db.setupdateData(sql);
        }
        return y;
    }

    public int saveRowsForRedshift(Map<String, String> map, String databaseName, String tableName, String databaseConfigId, String username, String ip) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        String sql = " insert into   " + tableName + " ";
        String colums = " ";
        String values = " ";
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            colums = String.valueOf(colums) + "\"" + entry.getKey() + "\",";
            String columnType = selectColumnTypeForRedshift(databaseName, tableName, entry.getKey(), databaseConfigId);
            String str = entry.getValue();
            if (str.equals("")) {
                values = String.valueOf(values) + " null ,";
            } else if (columnType.equals("integer")) {
                values = String.valueOf(values) + entry.getValue() + " ,";
            } else if (columnType.equals("numeric")) {
                values = String.valueOf(values) + entry.getValue() + " ,";
            } else if (columnType.equals("real")) {
                values = String.valueOf(values) + entry.getValue() + " ,";
            } else {
                values = String.valueOf(values) + "'" + entry.getValue() + "',";
            }
        }
        String sql2 = String.valueOf(sql) + " (" + colums.substring(0, colums.length() - 1) + ") values (" + values.substring(0, values.length() - 1) + ")";
        this.logDao.saveLog(sql2, username, ip, databaseName, databaseConfigId);
        int y = db.setupdateData(sql2);
        return y;
    }

    public String selectColumnTypeForRedshift(String databaseName, String tableName, String column, String databaseConfigId) throws Exception {
        String sql = " select data_type  from  information_schema.columns  where    table_name ='" + tableName + "' AND COLUMN_NAME ='" + column + "' ";
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        return (String) list.get(0).get("data_type");
    }

    public int deleteRowsNewForRedshift(String databaseName, String tableName, String primary_key, List<String> condition, String databaseConfigId, String username, String ip) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        int y = 0;
        for (int i = 0; i < condition.size(); i++) {
            String whereStr = condition.get(i);
            String sql = " delete from  " + tableName + " where  1=1 " + whereStr;
            new Date();
            LogUtil.i("Redshift删除数据行, " + DateUtils.getDateTime() + ", sql=" + sql);
            this.logDao.saveLog(sql, username, ip, databaseName, databaseConfigId);
            y += db.setupdateData(sql);
        }
        return y;
    }

    public int saveNewTableForRedshift(JSONArray insertArray, String databaseName, String tableName, String databaseConfigId) throws Exception {
        String sql;
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        String sql2 = " create table " + tableName + " (  ";
        String PRIMARY_KEY = "";
        for (int i = 0; i < insertArray.size(); i++) {
            Map<String, Object> map1 = (Map) insertArray.get(i);
            sql2 = String.valueOf(String.valueOf(sql2) + "\"" + map1.get("COLUMN_NAME") + "\"  ") + map1.get("DATA_TYPE") + " ";
            String dataLength = "";
            if (!StringUtils.isEmpty(map1.get("CHARACTER_MAXIMUM_LENGTH"))) {
                dataLength = "(" + map1.get("CHARACTER_MAXIMUM_LENGTH") + ") ";
            }
            if (!StringUtils.isEmpty(map1.get("NUMERIC_SCALE"))) {
                dataLength = "(" + map1.get("CHARACTER_MAXIMUM_LENGTH") + "," + map1.get("NUMERIC_SCALE") + ") ";
            }
            if (!StringUtils.isEmpty(dataLength)) {
                sql2 = String.valueOf(sql2) + dataLength;
            }
            if (map1.get("IS_NULLABLE").equals("")) {
                sql2 = String.valueOf(sql2) + " NOT NULL ";
            }
            if (!map1.get("COLUMN_COMMENT").equals("")) {
                sql2 = String.valueOf(sql2) + " COMMENT '" + map1.get("COLUMN_COMMENT") + "' ";
            }
            if (map1.get("COLUMN_KEY").equals("PRI")) {
                PRIMARY_KEY = String.valueOf(PRIMARY_KEY) + map1.get("COLUMN_NAME") + ",";
            }
            if (i < insertArray.size() - 1) {
                sql2 = String.valueOf(sql2) + " ,";
            }
        }
        if (!PRIMARY_KEY.equals("")) {
            sql = String.valueOf(sql2) + ", PRIMARY KEY (" + PRIMARY_KEY.substring(0, PRIMARY_KEY.length() - 1) + ") ) ";
        } else {
            sql = String.valueOf(sql2) + " ) ";
        }
        int y = db.setupdateData(sql);
        return y;
    }

    public List<Map<String, Object>> viewTableMessForRedshift(String dbName, String tableName, String databaseConfigId) throws Exception {
        String sql = " select * from pg_tables  where tablename='" + tableName + "'  ";
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        return list;
    }

    public List<Map<String, Object>> selectAllDataFromSQLForRedshift(String databaseName, String databaseConfigId, String sql, int limitFrom, int pageSize) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListForPostgreSQLForExport(sql, limitFrom, pageSize);
        return list;
    }

    public String getTableRows(String dbName, String tableName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        String sql = " select count(*) as num from  " + tableName;
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        Map<String, Object> map = list.get(0);
        String str = String.valueOf(map.get("num"));
        return str;
    }

    public List<Map<String, Object>> exportDataToSQLForRedshift(String databaseName, String tableName, List<String> condition, String databaseConfigId) throws Exception {
        String sql = " select * from  " + tableName + " where   1=1  ";
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < condition.size(); i++) {
            List<Map<String, Object>> list2 = db.queryForListForMySql(String.valueOf(sql) + condition.get(i));
            list.add(list2.get(0));
        }
        return list;
    }

    public boolean renameTableForRedshift(String databaseName, String tableName, String databaseConfigId, String newTableName) throws Exception {
        String sql4 = " alter table " + ("\"" + tableName + "\"") + " rename to  " + ("\"" + newTableName + "\"");
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        db.setupdateData(sql4);
        return true;
    }

    public boolean copyTableForRedshift(String databaseName, String tableName, String databaseConfigId) throws Exception {
        String sql4 = "create table \"" + tableName + "_" + DateUtils.getDateTimeString(new Date()) + "\" as select * from \"" + tableName + "\"";
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        db.setupdateData(sql4);
        return true;
    }

    public boolean dropTableForRedshift(String databaseName, String tableName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        String sql = " drop  table " + ("\"" + tableName + "\"");
        db.setupdateData(sql);
        LogUtil.i(String.valueOf(DateUtils.getDateTime()) + ",删除redshift表SQL =" + sql);
        return true;
    }

    public boolean insertByDataListForRedshift(List<Map<String, Object>> dataList, String databaseName, String tableName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<String> sqlList = new ArrayList<>();
        Iterator<Map<String, Object>> it = dataList.iterator();
        while (it.hasNext()) {
            Map<String, Object> map4 = it.next();
            String insertSQL = "insert into " + tableName + " ";
            String colums = " ";
            String values = "";
            Iterator<Map.Entry<String, Object>> it2 = map4.entrySet().iterator();
            while (it2.hasNext()) {
                Map.Entry<String, Object> entry = it2.next();
                colums = String.valueOf(colums) + entry.getKey() + ",";
                if (entry.getValue() == null) {
                    values = String.valueOf(values) + "null,";
                } else if ((entry.getValue() instanceof java.sql.Date) || (entry.getValue() instanceof Time) || (entry.getValue() instanceof Timestamp)) {
                    values = String.valueOf(values) + "'" + entry.getValue() + "',";
                } else if ((entry.getValue() instanceof Integer) || (entry.getValue() instanceof Float) || (entry.getValue() instanceof Long) || (entry.getValue() instanceof BigInteger) || (entry.getValue() instanceof Double) || (entry.getValue() instanceof BigDecimal)) {
                    values = String.valueOf(values) + entry.getValue() + ",";
                } else if (entry.getValue() instanceof Boolean) {
                    values = String.valueOf(values) + entry.getValue() + ",";
                } else if (entry.getValue() instanceof Byte) {
                    byte[] ss = (byte[]) entry.getValue();
                    if (ss.length == 0) {
                        values = String.valueOf(values) + "null,";
                    } else {
                        values = String.valueOf(values) + "0x" + DataUtil.bytesToHexString(ss) + ",";
                    }
                } else if (entry.getValue() instanceof ArrayList) {
                    values = String.valueOf(values) + "'" + entry.getValue().toString() + "',";
                } else {
                    String tempValues = (String) entry.getValue();
                    values = String.valueOf(values) + "'" + tempValues.replace("\\", "\\\\").replace("'", "\\'").replace("\r\n", "\\r\\n").replace("\n\r", "\\n\\r").replace("\r", "\\r").replace("\n", "\\n") + "',";
                }
            }
            sqlList.add(String.valueOf(insertSQL) + " ( " + colums.substring(0, colums.length() - 1) + ") VALUES (" + values.substring(0, values.length() - 1) + " ) ");
        }
        db.updateExecuteBatch(sqlList);
        return true;
    }

    public boolean updateByDataListForRedshift(List<Map<String, Object>> dataList, String databaseName, String tableName, String databaseConfigId, String qualification) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<String> sqlList = new ArrayList<>();
        String[] whereColumn = qualification.split(",");
        Iterator<Map<String, Object>> it = dataList.iterator();
        while (it.hasNext()) {
            Map<String, Object> map4 = it.next();
            String updateSQL = "update " + tableName + "  set ";
            Iterator<Map.Entry<String, Object>> it2 = map4.entrySet().iterator();
            while (it2.hasNext()) {
                Map.Entry<String, Object> entry = it2.next();
                String colums = entry.getKey();
                if (!Arrays.asList(whereColumn).contains(colums)) {
                    String updateSQL2 = String.valueOf(updateSQL) + entry.getKey() + "=";
                    if (entry.getValue() == null) {
                        updateSQL = String.valueOf(updateSQL2) + "null,";
                    } else if ((entry.getValue() instanceof java.sql.Date) || (entry.getValue() instanceof Time) || (entry.getValue() instanceof Timestamp)) {
                        updateSQL = String.valueOf(updateSQL2) + "'" + entry.getValue() + "',";
                    } else if ((entry.getValue() instanceof Integer) || (entry.getValue() instanceof Float) || (entry.getValue() instanceof Long) || (entry.getValue() instanceof BigInteger) || (entry.getValue() instanceof Double) || (entry.getValue() instanceof BigDecimal) || (entry.getValue() instanceof Short)) {
                        updateSQL = String.valueOf(updateSQL2) + entry.getValue() + ",";
                    } else if (entry.getValue() instanceof Boolean) {
                        updateSQL = String.valueOf(updateSQL2) + entry.getValue() + ",";
                    } else if (entry.getValue() instanceof Byte) {
                        byte[] ss = (byte[]) entry.getValue();
                        if (ss.length == 0) {
                            updateSQL = String.valueOf(updateSQL2) + "null,";
                        } else {
                            updateSQL = String.valueOf(updateSQL2) + "0x" + DataUtil.bytesToHexString(ss) + ",";
                        }
                    } else if (entry.getValue() instanceof ArrayList) {
                        updateSQL = String.valueOf(updateSQL2) + "'" + entry.getValue().toString() + "',";
                    } else {
                        String tempValues = (String) entry.getValue();
                        updateSQL = String.valueOf(updateSQL2) + "'" + tempValues.replace("'", "\\'").replace("\r\n", "\\r\\n").replace("\n\r", "\\n\\r").replace("\r", "\\r").replace("\n", "\\n") + "',";
                    }
                }
            }
            String updateSQL3 = updateSQL.substring(0, updateSQL.length() - 1);
            String whereStr = " where 1=1 ";
            for (String ss2 : whereColumn) {
                whereStr = String.valueOf(whereStr) + " and " + ss2 + "='" + map4.get(ss2) + "' ";
            }
            sqlList.add(String.valueOf(updateSQL3) + whereStr);
        }
        db.updateExecuteBatch(sqlList);
        return true;
    }

    public boolean insertOrUpdateByDataListForRedshift(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        String value;
        List<Map<String, Object>> insertDataList = new ArrayList<>();
        List<Map<String, Object>> updateDataList = new ArrayList<>();
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        String[] whereColumn = qualification.split(",");
        Connection conn = db.getConnectionForQuartzJob();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            try {
                stmt = conn.createStatement();
                Iterator<Map<String, Object>> it = dataList.iterator();
                while (it.hasNext()) {
                    Map<String, Object> map4 = it.next();
                    rs = null;
                    String sql = "select * from " + table + " where 1=1 ";
                    for (String ss : whereColumn) {
                        if (map4.containsKey(ss)) {
                            value = map4.get(ss).toString();
                        } else if (map4.containsKey(ss.toUpperCase())) {
                            value = map4.get(ss.toUpperCase()).toString();
                        } else {
                            value = map4.get(ss.toLowerCase()).toString();
                        }
                        sql = String.valueOf(sql) + " and " + ss + "='" + value + "' ";
                    }
                    rs = stmt.executeQuery(sql);
                    if (rs.next()) {
                        updateDataList.add(map4);
                    } else {
                        insertDataList.add(map4);
                    }
                }
                try {
                    rs.close();
                    stmt.close();
                    conn.close();
                } catch (SQLException e) {
                }
            } catch (Exception e2) {
                LogUtil.e(e2);
                try {
                    rs.close();
                    stmt.close();
                    conn.close();
                } catch (SQLException e3) {
                }
            }
            if (insertDataList.size() > 0) {
                insertByDataListForRedshift(insertDataList, databaseName, table, databaseConfigId);
            }
            if (updateDataList.size() > 0) {
                updateByDataListForRedshift(updateDataList, databaseName, table, databaseConfigId, qualification);
                return true;
            }
            return true;
        } catch (Throwable th) {
            try {
                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException e4) {
            }
            throw th;
        }
    }

    public boolean insertOnlyByDataListForRedshift(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        String value;
        List<Map<String, Object>> insertDataList = new ArrayList<>();
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        String[] whereColumn = qualification.split(",");
        Connection conn = db.getConnectionForQuartzJob();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            try {
                stmt = conn.createStatement();
                Iterator<Map<String, Object>> it = dataList.iterator();
                while (it.hasNext()) {
                    Map<String, Object> map4 = it.next();
                    rs = null;
                    String sql = "select * from " + table + " where 1=1 ";
                    for (String ss : whereColumn) {
                        if (map4.containsKey(ss)) {
                            value = map4.get(ss).toString();
                        } else if (map4.containsKey(ss.toUpperCase())) {
                            value = map4.get(ss.toUpperCase()).toString();
                        } else {
                            value = map4.get(ss.toLowerCase()).toString();
                        }
                        sql = String.valueOf(sql) + " and " + ss + "='" + value + "' ";
                    }
                    rs = stmt.executeQuery(sql);
                    if (!rs.next()) {
                        insertDataList.add(map4);
                    }
                }
                try {
                    rs.close();
                    stmt.close();
                    conn.close();
                } catch (SQLException e) {
                }
            } catch (Exception e2) {
                LogUtil.e(e2);
                try {
                    rs.close();
                    stmt.close();
                    conn.close();
                } catch (SQLException e3) {
                }
            }
            if (insertDataList.size() > 0) {
                insertByDataListForRedshift(insertDataList, databaseName, table, databaseConfigId);
                return true;
            }
            return true;
        } catch (Throwable th) {
            try {
                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException e4) {
            }
            throw th;
        }
    }

    public boolean deleteByDataListForRedshift(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        String[] whereColumn = qualification.split(",");
        Iterator<Map<String, Object>> it = dataList.iterator();
        while (it.hasNext()) {
            Map<String, Object> map4 = it.next();
            String sql = "delete from " + table + " where 1=1 ";
            for (String ss : whereColumn) {
                sql = String.valueOf(sql) + " and " + ss + "='" + map4.get(ss) + "' ";
            }
            db.setupdateData(sql);
        }
        return true;
    }

    public String createTableSQLForRedshift(String tableName, String databaseName, String databaseConfigId) throws Exception {
        new DBUtil2(databaseName, databaseConfigId);
        StringBuffer sb2 = new StringBuffer();
        sb2.append("create table  " + tableName + "  ( \r\n");
        String primary_key_list = "";
        String tableColumnStr = "";
        Map<String, String> TableColumnType = new HashMap<>();
        List<Map<String, Object>> listTableColumn = getTableColumns3ForRedshift(databaseName, tableName, databaseConfigId);
        Iterator<Map<String, Object>> it = listTableColumn.iterator();
        while (it.hasNext()) {
            Map<String, Object> map3 = it.next();
            TableColumnType.put((String) map3.get("COLUMN_NAME"), (String) map3.get("DATA_TYPE"));
            sb2.append("  " + map3.get("COLUMN_NAME") + " " + map3.get("COLUMN_TYPE"));
            tableColumnStr = String.valueOf(tableColumnStr) + "\"" + map3.get("COLUMN_NAME") + "\",";
            if (map3.get("COLUMN_KEY") != null && map3.get("COLUMN_KEY").equals("PRI")) {
                primary_key_list = String.valueOf(primary_key_list) + map3.get("COLUMN_NAME") + ",";
            }
            if (map3.get("IS_NULLABLE").equals("NO")) {
                sb2.append(" NOT NULL ");
            }
            sb2.append(",\r\n");
        }
        if ("".equals(primary_key_list)) {
            sb2.delete(sb2.length() - 3, sb2.length() - 1);
            sb2.append("  \r\n");
        } else {
            sb2.append("PRIMARY KEY (" + primary_key_list.substring(0, primary_key_list.length() - 1) + " )  \r\n");
        }
        sb2.append("); \r\n ");
        sb2.append("  \r\n");
        return sb2.toString();
    }

    public int executeSqlNotRes(String sql, String dbName, String databaseConfigId) throws Exception {
        new HashMap();
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        int i = db.setupdateData(sql);
        return i;
    }

    public boolean checkSqlIsOneTableForRedshift(String dbName, String sql, String databaseConfigId) {
        return false;
    }

    public Map<String, Object> queryDatabaseStatusForRedshift(String databaseName, String databaseConfigId) throws Exception {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> mm1 = queryDatabaseSQPSForRedshift(databaseName, databaseConfigId);
        map.put("SESSIONS", queryDatabaseStatusForRedshiftConn(databaseName, databaseConfigId));
        map.put("dbSize", queryDatabaseStatusForRedshiftDBSize(databaseName, databaseConfigId));
        map.put("LOCK", queryDatabaseStatusForRedshiftLocks(databaseName, databaseConfigId));
        map.put("tableTotal", queryDatabaseStatusForRedshiftTableTotal(databaseName, databaseConfigId));
        map.put("tableSpaceSize", queryDatabaseTableSpaceForRedshift(databaseName, databaseConfigId));
        map.putAll(mm1);
        return map;
    }

    public String queryDatabaseTableSpaceForRedshift(String databaseName, String databaseConfigId) {
        new DBUtil2(databaseName, databaseConfigId);
        return "0";
    }

    public String queryDatabaseStatusForRedshiftTableTotal(String databaseName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(" select  tablename   from pg_tables  where schemaname='public'  ");
        return String.valueOf(list.size());
    }

    public String queryDatabaseStatusForRedshiftConn(String databaseName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod("select count(*) as connections from pg_stat_activity");
        String value = "0";
        if (list.size() > 0) {
            Map<String, Object> temp = list.get(0);
            value = new StringBuilder().append(temp.get("connections")).toString();
        }
        return value;
    }

    public String queryDatabaseStatusForRedshiftLocks(String databaseName, String databaseConfigId) throws Exception {
        return "0";
    }

    public String queryDatabaseStatusForRedshiftDBSize(String databaseName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        String sql = " select pg_size_pretty(pg_database_size('" + databaseName + "'))";
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        String value = "0";
        if (list.size() > 0) {
            Map<String, Object> temp = list.get(0);
            value = new StringBuilder().append(temp.get("pg_size_pretty")).toString();
        }
        return value;
    }

    public Map<String, Object> queryDatabaseSQPSForRedshift(String databaseName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> list = db.queryForListCommonMethod("  select sum(seq_tup_read) as select ,sum(n_tup_ins) as insert ,sum(n_tup_upd) as update ,sum(n_tup_del) as delete from pg_stat_user_tables ");
        if (list.size() > 0) {
            map = list.get(0);
        }
        return map;
    }
}
