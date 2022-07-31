package org.springframework.base.system.dao;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Clob;
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
import java.util.Set;
import java.util.TreeSet;
import org.springframework.base.system.dbUtils.DBUtil2;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.utils.DataUtil;
import org.springframework.base.system.utils.DateUtils;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/dao/Dm7Dao.class */
public class Dm7Dao {
    @Autowired
    private LogDao logDao;
    @Autowired
    private ConfigDao configDao;

    public List<Map<String, Object>> getAllDataBaseForDm7(String databaseConfigId) throws Exception {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map0 = this.configDao.getConfigById(databaseConfigId);
        String databaseName = (String) map0.get("databaseName");
        Map<String, Object> map = new HashMap<>();
        map.put("SCHEMA_NAME", databaseName);
        list.add(map);
        return list;
    }

    public List<Map<String, Object>> getAllTablesForDm7(String dbName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        String sql = "select NAME AS TABLE_NAME from SYS.SYSOBJECTS WHERE SUBTYPE$='UTAB' AND SCHID IN (SELECT ID FROM SYS.SYSOBJECTS WHERE NAME='" + dbName + "') ORDER BY  TABLE_NAME ";
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        return list;
    }

    public List<Map<String, Object>> getTableColumnsForDM(String dbName, String tableName, String databaseConfigId) throws Exception {
        String sql = "SELECT  NAME AS TREESOFTPRIMARYKEY,NAME AS COLUMN_NAME,TYPE$ AS COLUMN_TYPE, TYPE$ AS DATA_TYPE, LENGTH$ AS CHARACTER_MAXIMUM_LENGTH ,CASE NULLABLE$ when 'Y' then 'YES' END as IS_NULLABLE, '' AS COLUMN_KEY, (select COMMENT$ FROM SYS.SYSCOLUMNCOMMENTS where SCHNAME='" + dbName + "' AND TVNAME ='" + tableName + "' AND COLNAME= SYS.SYSCOLUMNS.NAME ) AS COLUMN_COMMENT  from  SYS.SYSCOLUMNS WHERE ID IN ( select ID from  SYS.SYSOBJECTS WHERE NAME ='" + tableName + "' AND SCHID IN (select ID from SYS.SYSOBJECTS WHERE TYPE$ = 'SCH' AND NAME='" + dbName + "')) ";
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        try {
            List<Map<String, Object>> pkList = getTablePKForDM(dbName, tableName, databaseConfigId);
            if (pkList.size() >= 0) {
                Set<String> set = new TreeSet<>();
                Iterator<Map<String, Object>> it = pkList.iterator();
                while (it.hasNext()) {
                    Map<String, Object> map1 = it.next();
                    set.add(map1.get("COLUMN_NAME").toString());
                }
                Iterator<Map<String, Object>> it2 = list.iterator();
                while (it2.hasNext()) {
                    Map<String, Object> map2 = it2.next();
                    if (set.contains(map2.get("COLUMN_NAME"))) {
                        map2.put("COLUMN_KEY", "PRI");
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.e("取得指定表" + tableName + "全部的主键 出错，", e);
        }
        return list;
    }

    public List<Map<String, Object>> getTablePKForDM(String dbName, String tableName, String databaseConfigId) throws Exception {
        String sql = " SELECT  col.COLUMN_NAME, col.CONSTRAINT_NAME from all_constraints con,all_cons_columns col where  con.constraint_name=col.constraint_name and con.constraint_type='P' and col.table_name='" + tableName + "' and col.OWNER='" + dbName + "' ";
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        return list;
    }

    public List<Map<String, Object>> getAllViewsForDm7(String dbName, String databaseConfigId) throws Exception {
        String sql = "select NAME AS TABLE_NAME from SYS.SYSOBJECTS WHERE SUBTYPE$='VIEW' AND SCHID IN (SELECT ID FROM SYS.SYSOBJECTS WHERE NAME='" + dbName + "') ORDER BY  TABLE_NAME ";
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        return list;
    }

    public String getViewSqlForDm7(String databaseName, String tableName, String databaseConfigId) throws Exception {
        String sql = " SELECT DEFINITION FROM VIEWS WHERE SCHEMA_NAME = '" + databaseName + "' AND VIEW_NAME = '" + tableName + "'";
        String str = "";
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        if (list.size() == 1) {
            Map<String, Object> map = list.get(0);
            Clob sc = (Clob) map.get("DEFINITION");
            Reader is = sc.getCharacterStream();
            BufferedReader br = new BufferedReader(is);
            StringBuffer sb = new StringBuffer();
            for (String s = br.readLine(); s != null; s = br.readLine()) {
                sb.append(s);
            }
            String reString = sb.toString();
            str = "CREATE VIEW " + tableName + " AS " + reString + ";";
        }
        return str;
    }

    public List<Map<String, Object>> getAllFuntionForDm7(String dbName, String databaseConfigId) throws Exception {
        String sql = "select NAME AS ROUTINE_NAME from SYS.SYSOBJECTS WHERE SUBTYPE$='PROC'  AND INFO1='0' AND VALID='Y' AND SCHID IN (SELECT ID FROM SYS.SYSOBJECTS WHERE NAME='" + dbName + "') ORDER BY  ROUTINE_NAME ";
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        return list;
    }

    public List<Map<String, Object>> getAllProcedureForDm7(String dbName, String databaseConfigId) throws Exception {
        String sql = "select NAME AS PROC_NAME from SYS.SYSOBJECTS WHERE SUBTYPE$='PROC'  AND INFO1='1' AND VALID='Y' AND SCHID IN (SELECT ID FROM SYS.SYSOBJECTS WHERE NAME='" + dbName + "') ORDER BY  PROC_NAME ";
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        return list;
    }

    public Page<Map<String, Object>> getDataForDm7(Page<Map<String, Object>> page, String tableName, String dbName, String databaseConfigId) throws Exception {
        String sql2;
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        String orderBy = page.getOrderBy();
        String order = page.getOrder();
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        String sql = "select * from  " + dbName + "." + tableName;
        if (orderBy == null || orderBy.equals("")) {
            sql2 = "select  *  from  " + dbName + "." + tableName + "  LIMIT " + limitFrom + "," + pageSize;
        } else {
            sql2 = "select  *  from  " + dbName + "." + tableName + "  order by " + orderBy + " " + order + "  LIMIT " + limitFrom + "," + pageSize;
        }
        List<Map<String, Object>> list = db.queryForListForMySql(sql2);
        int rowCount = db.executeQueryForCountForMySQL(sql);
        List<Map<String, Object>> columns = getTableColumnsForDm7(dbName, tableName, databaseConfigId);
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

    public List<Map<String, Object>> getPrimaryKeyssForDm7(String databaseName, String tableName, String databaseConfigId) throws Exception {
        String sql = " SELECT COLUMN_NAME  FROM SYS.CONSTRAINTS  WHERE SCHEMA_NAME='" + databaseName + "' AND TABLE_NAME = '" + tableName.toUpperCase() + "' ";
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        return list;
    }

    public List<Map<String, Object>> getTableColumnsForDm7(String dbName, String tableName, String databaseConfigId) throws Exception {
        String sql = "select  * from   " + dbName + "." + tableName + " fetch first 1 rows only";
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForColumnOnly(sql);
        return list;
    }

    public Page<Map<String, Object>> executeSqlHaveResForDm7(Page<Map<String, Object>> page, String sql, String dbName, String databaseConfigId) throws Exception {
        int rowCount;
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        String sql2 = " select * from ( " + sql + " ) tab  LIMIT " + limitFrom + "," + pageSize;
        if (sql.indexOf("show") == 0 || sql.indexOf("SHOW") == 0 || sql.indexOf("explain") == 0 || sql.indexOf("EXPLAIN") == 0) {
            sql2 = sql;
        }
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        Date b1 = new Date();
        List<Map<String, Object>> list = db.queryForListForMySql(sql2);
        Date b2 = new Date();
        long executeTime = b2.getTime() - b1.getTime();
        if (list.size() < 10) {
            rowCount = list.size();
        } else {
            rowCount = db.executeQueryForCountForMySQL(sql);
        }
        List<Map<String, Object>> columns = executeSqlForColumnsForDm7(sql, dbName, databaseConfigId);
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

    public List<Map<String, Object>> executeSqlForColumnsForDm7(String sql, String dbName, String databaseConfigId) throws Exception {
        String sql2 = " select * from (" + sql + ") T limit 1 ";
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.executeSqlForColumns(sql2);
        return list;
    }

    public int updateTableNullAbleForDm7(String databaseName, String tableName, String column_name, String is_nullable, String databaseConfigId) throws Exception {
        String sql4;
        if (column_name != null && !column_name.equals("")) {
            DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
            if (is_nullable.equals("true")) {
                sql4 = " alter table  " + databaseName + "." + tableName + " alter column \"" + column_name + "\" SET  NULL";
            } else {
                sql4 = " alter table  " + databaseName + "." + tableName + " alter column \"" + column_name + "\" SET NOT NULL";
            }
            LogUtil.i("更新字段的is_nullable, sql=" + sql4);
            db.setupdateData(sql4);
            return 0;
        }
        return 0;
    }

    public String selectOneColumnTypeForDm7(String databaseName, String tableName, String column_name, String databaseConfigId) throws Exception {
        String sql = " SELECT CONCAT( CONCAT( CONCAT( DATA_TYPE_NAME ,'('  )  ,LENGTH   ) ,')'  )  as  COLUMN_TYPE   from  SYS.TABLE_COLUMNS  where SCHEMA_NAME='" + databaseName + "' AND  TABLE_NAME = '" + tableName + "' AND COLUMN_NAME = '" + column_name + "'";
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        return (String) list.get(0).get("COLUMN_TYPE");
    }

    public int savePrimaryKeyForDm7(String databaseName, String tableName, String column_name, String isSetting, String databaseConfigId) throws Exception {
        if (column_name != null && !column_name.equals("")) {
            new DBUtil2(databaseName, databaseConfigId);
            isSetting.equals("true");
            return 0;
        }
        return 0;
    }

    public List<Map<String, Object>> selectTablePrimaryKeyForDm7(String databaseName, String tableName, String databaseConfigId) throws Exception {
        String sql = "SELECT CONSTRAINT_NAME, COLUMN_NAME FROM  SYS.CONSTRAINTS WHERE  SCHEMA_NAME='" + databaseName + "' AND  TABLE_NAME = '" + tableName + "'";
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        new ArrayList();
        return list;
    }

    public int saveDesginColumnForDm7(Map<String, String> map, String databaseName, String tableName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        String sql = " alter table " + tableName + " add  ";
        String sql2 = String.valueOf(String.valueOf(sql) + map.get("COLUMN_NAME") + "  ") + map.get("DATA_TYPE");
        if (map.get("CHARACTER_MAXIMUM_LENGTH") != null && !map.get("CHARACTER_MAXIMUM_LENGTH").equals("")) {
            sql2 = String.valueOf(sql2) + " (" + map.get("CHARACTER_MAXIMUM_LENGTH") + ") ";
        }
        if (map.get("COLUMN_COMMENT") != null && !map.get("COLUMN_COMMENT").equals("")) {
            sql2 = String.valueOf(sql2) + " comment '" + map.get("COLUMN_COMMENT") + "'";
        }
        int y = db.setupdateData(sql2);
        return y;
    }

    public int updateTableColumnForDm7(Map<String, Object> map, String databaseName, String tableName, String columnName, String idValues, String databaseConfigId) throws Exception {
        if (columnName == null || "".equals(columnName)) {
            throw new Exception("数据不完整,保存失败!");
        }
        if (idValues == null || "".equals(idValues)) {
            throw new Exception("数据不完整,保存失败!");
        }
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        String old_field_name = (String) map.get("TREESOFTPRIMARYKEY");
        String column_name = (String) map.get("COLUMN_NAME");
        String data_type = (String) map.get("DATA_TYPE");
        String character_maximum_length = new StringBuilder().append(map.get("CHARACTER_MAXIMUM_LENGTH")).toString();
        String column_comment = (String) map.get("COLUMN_COMMENT");
        if (!old_field_name.endsWith(column_name)) {
            String sql = " ALTER TABLE " + tableName + " RENAME COLUMN " + old_field_name + " to  " + column_name;
            db.setupdateData(sql);
        }
        String sql2 = " alter table  " + tableName + " modify  " + column_name + " " + data_type;
        if (character_maximum_length != null && !character_maximum_length.equals("")) {
            sql2 = String.valueOf(sql2) + " (" + character_maximum_length + ")";
        }
        int y = db.setupdateData(sql2);
        if (column_comment != null && !column_comment.equals("")) {
            String sql4 = "  comment on column " + tableName + "." + column_name + " is '" + column_comment + "' ";
            db.setupdateData(sql4);
        }
        return y;
    }

    public int deleteTableColumnForDm7(String databaseName, String tableName, String[] ids, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        int y = 0;
        for (int i = 0; i < ids.length; i++) {
            String sql = " alter table   " + tableName + " drop (" + ids[i] + ")";
            y += db.setupdateData(sql);
        }
        return y;
    }

    public int saveRowsForDm7(Map<String, String> map, String databaseName, String tableName, String databaseConfigId, String username, String ip) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        String sql = " INSERT INTO " + tableName;
        String colums = " ";
        String values = " ";
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            colums = String.valueOf(colums) + entry.getKey() + ",";
            String columnType = selectColumnTypeForDm7(databaseName, tableName, entry.getKey(), databaseConfigId);
            String str = entry.getValue();
            if (str.equals("")) {
                values = String.valueOf(values) + " null ,";
            } else if (columnType.equals("NUMBER")) {
                values = String.valueOf(values) + entry.getValue() + ",";
            } else if (columnType.equals("NUMERIC")) {
                values = String.valueOf(values) + entry.getValue() + ",";
            } else if (columnType.equals("INTEGER")) {
                values = String.valueOf(values) + entry.getValue() + ",";
            } else if (columnType.equals("DECIMAL")) {
                values = String.valueOf(values) + entry.getValue() + ",";
            } else if (columnType.equals("DOUBLE")) {
                values = String.valueOf(values) + entry.getValue() + ",";
            } else if (columnType.equals("SMALLINT")) {
                values = String.valueOf(values) + entry.getValue() + ",";
            } else if (columnType.equals("FLOAT")) {
                values = String.valueOf(values) + entry.getValue() + ",";
            } else if (columnType.equals("BINARY_FLOAT")) {
                values = String.valueOf(values) + entry.getValue() + ",";
            } else if (columnType.equals("BINARY_DOUBLE")) {
                values = String.valueOf(values) + entry.getValue() + ",";
            } else {
                values = String.valueOf(values) + "'" + entry.getValue() + "',";
            }
        }
        String sql2 = String.valueOf(sql) + " (" + colums.substring(0, colums.length() - 1) + ") values (" + values.substring(0, values.length() - 1) + ")";
        System.out.println("sql=" + sql2);
        this.logDao.saveLog(sql2, username, ip, databaseName, databaseConfigId);
        int y = db.setupdateData(sql2);
        return y;
    }

    public String selectColumnTypeForDm7(String databaseName, String tableName, String column, String databaseConfigId) throws Exception {
        String sql = " select DATA_TYPE_NAME as DATA_TYPE  FROM SYS.TABLE_COLUMNS  where SCHEMA_NAME ='" + databaseName + "' AND TABLE_NAME ='" + tableName + "' AND COLUMN_NAME ='" + column + "' ";
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        return (String) list.get(0).get("DATA_TYPE");
    }

    public int deleteRowsNewForDm7(String databaseName, String tableName, String primary_key, List<String> condition, String databaseConfigId, String username, String ip) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        int y = 0;
        for (int i = 0; i < condition.size(); i++) {
            String whereStr = condition.get(i);
            String sql = " delete from  " + tableName + " where  1=1 " + whereStr;
            new Date();
            LogUtil.i("Dm7删除数据行, " + DateUtils.getDateTime() + ", sql=" + sql);
            this.logDao.saveLog(sql, username, ip, databaseName, databaseConfigId);
            y += db.setupdateData(sql);
        }
        return y;
    }

    public int saveNewTableForDm7(JSONArray insertArray, String databaseName, String tableName, String databaseConfigId) throws Exception {
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

    public List<Map<String, Object>> viewTableMessForDm7(String dbName, String tableName, String databaseConfigId) throws Exception {
        String sql = "select * from SYS.SYSOBJECTS WHERE SUBTYPE$='UTAB' AND  NAME='" + tableName + "'  ";
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        return list;
    }

    public List<Map<String, Object>> selectAllDataFromSQLForDm7(String databaseName, String databaseConfigId, String sql, int limitFrom, int pageSize) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListForDm7(sql, limitFrom, pageSize);
        return list;
    }

    public Integer getTableRowsForDm7(String dbName, String tableName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        String sql = " select count(*) as NUM from  " + dbName + "." + tableName;
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        Map<String, Object> map = list.get(0);
        Integer rowCount = Integer.valueOf(Integer.parseInt(map.get("NUM").toString()));
        return rowCount;
    }

    public List<Map<String, Object>> exportDataToSQLForDm7(String databaseName, String tableName, List<String> condition, String databaseConfigId) throws Exception {
        String sql = " select * from  " + tableName + " where   1=1  ";
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < condition.size(); i++) {
            List<Map<String, Object>> list2 = db.queryForListForMySql(String.valueOf(sql) + condition.get(i));
            list.add(list2.get(0));
        }
        return list;
    }

    public boolean renameTableForDm7(String databaseName, String tableName, String databaseConfigId, String newTableName) throws Exception {
        String sql4 = " ALTER TABLE " + databaseName + "." + tableName + " RENAME TO  " + newTableName;
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        db.setupdateData(sql4);
        return true;
    }

    public boolean copyTableForDm7(String databaseName, String tableName, String databaseConfigId) throws Exception {
        String sql4 = "create table " + databaseName + "." + tableName + "_" + DateUtils.getDateTimeString(new Date()) + " as select * from " + databaseName + "." + tableName;
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        db.setupdateData(sql4);
        return true;
    }

    public boolean dropTableForDm7(String databaseName, String tableName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        String sql = " drop table " + databaseName + "." + tableName;
        db.setupdateData(sql);
        LogUtil.i(String.valueOf(DateUtils.getDateTime()) + ",删除Dm7表,SQL =" + sql);
        return true;
    }

    public boolean insertByDataListForDm7(List<Map<String, Object>> dataList, String databaseName, String tableName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<String> sqlList = new ArrayList<>();
        Iterator<Map<String, Object>> it = dataList.iterator();
        while (it.hasNext()) {
            Map<String, Object> map4 = it.next();
            String insertSQL = "INSERT INTO " + tableName + " ";
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
                    values = String.valueOf(values) + "'" + tempValues.replace("'", "\\'").replace("\r\n", "\\r\\n").replace("\n\r", "\\n\\r").replace("\r", "\\r").replace("\n", "\\n") + "',";
                }
            }
            sqlList.add(String.valueOf(insertSQL) + " ( " + colums.substring(0, colums.length() - 1) + ") VALUES (" + values.substring(0, values.length() - 1) + " ) ");
        }
        db.updateExecuteBatch(sqlList);
        return true;
    }

    public boolean updateByDataListForDm7(List<Map<String, Object>> dataList, String databaseName, String tableName, String databaseConfigId, String qualification) throws Exception {
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

    public boolean insertOrUpdateByDataListForDm7(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
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
                insertByDataListForDm7(insertDataList, databaseName, table, databaseConfigId);
            }
            if (updateDataList.size() > 0) {
                updateByDataListForDm7(updateDataList, databaseName, table, databaseConfigId, qualification);
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

    public boolean insertOnlyByDataListForDm7(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
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
            } catch (Throwable th) {
                try {
                    rs.close();
                    stmt.close();
                    conn.close();
                } catch (SQLException e2) {
                }
                throw th;
            }
        } catch (Exception e3) {
            LogUtil.e(e3);
            try {
                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException e4) {
            }
        }
        if (insertDataList.size() > 0) {
            insertByDataListForDm7(insertDataList, databaseName, table, databaseConfigId);
            return true;
        }
        return true;
    }

    public boolean deleteByDataListForDm7(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        String[] whereColumn = qualification.split(",");
        Iterator<Map<String, Object>> it = dataList.iterator();
        while (it.hasNext()) {
            Map<String, Object> map4 = it.next();
            String sql = "delete from " + table + " where 1=1 ";
            for (String ss : whereColumn) {
                sql = String.valueOf(sql) + " and " + ss + "='" + map4.get(ss) + "' ";
            }
            System.out.println(sql);
            db.setupdateData(sql);
        }
        return true;
    }

    public String createTableSQLForDm7(String tableName, String databaseName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        String createTableSQL = "";
        List<Map<String, Object>> list4 = db.queryForListCommonMethod("SELECT convert(varchar(5000), TABLEDEF('" + databaseName + "','" + tableName + "'))  AS DEF ");
        if (list4.size() > 0) {
            Map<String, Object> mapp = list4.get(0);
            createTableSQL = (String) mapp.get("DEF");
        }
        return createTableSQL;
    }

    public int executeSqlNotRes(String sql, String dbName, String databaseConfigId) throws Exception {
        new HashMap();
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        int i = db.setupdateData(sql);
        return i;
    }

    public boolean checkSqlIsOneTableForDm7(String dbName, String sql, String databaseConfigId) {
        return false;
    }

    public boolean clearTableForDM(String databaseName, String tableName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        String sql = " delete from  " + databaseName + "." + tableName;
        db.setupdateData(sql);
        LogUtil.i(String.valueOf(DateUtils.getDateTime()) + ",清空表SQL =" + sql);
        return true;
    }
}
