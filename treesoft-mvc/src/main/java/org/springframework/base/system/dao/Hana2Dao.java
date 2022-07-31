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
import org.springframework.base.system.dbUtils.DBUtilJDBC;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.utils.DataUtil;
import org.springframework.base.system.utils.DateUtils;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/dao/Hana2Dao.class */
public class Hana2Dao {
    @Autowired
    private LogDao logDao;
    @Autowired
    private ConfigDao configDao;

    public List<Map<String, Object>> getAllDataBaseForHana2(String databaseConfigId) throws Exception {
        Map<String, Object> map12 = this.configDao.getConfigById(databaseConfigId);
        String databaseName = new StringBuilder().append(map12.get("databaseName")).toString();
        String isDefaultView = (String) map12.get("isDefaultView");
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            DBUtilJDBC db = new DBUtilJDBC(databaseName, databaseConfigId);
            list = db.queryForListCommonMethod(" SELECT SCHEMA_NAME FROM SYS.SCHEMAS name  ");
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        List<Map<String, Object>> list2 = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("SCHEMA_NAME", databaseName);
        list2.add(map);
        if ("1".equals(isDefaultView)) {
            return list2;
        }
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map2 = list.get(i);
            String schema_name = (String) map2.get("SCHEMA_NAME");
            if (!schema_name.equals(databaseName)) {
                list2.add(map2);
            }
        }
        return list2;
    }

    public List<Map<String, Object>> getAllTablesForHana2(String dbName, String databaseConfigId) throws Exception {
        DBUtilJDBC db = new DBUtilJDBC(dbName, databaseConfigId);
        String sql = " select TABLE_NAME  from  SYS.TABLES WHERE SCHEMA_NAME='" + dbName + "'  order by TABLE_NAME  ";
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        return list;
    }

    public List<Map<String, Object>> getTableColumns3ForHana2(String dbName, String tableName, String databaseConfigId) throws Exception {
        String sql = "select T1.COLUMN_NAME AS TREESOFTPRIMARYKEY ,T1.COLUMN_NAME ,T1.DATA_TYPE_NAME as COLUMN_TYPE ,T1.DATA_TYPE_NAME as DATA_TYPE, T1.LENGTH AS CHARACTER_MAXIMUM_LENGTH , CASE T1.IS_NULLABLE when 'TRUE' then 'YES' END as IS_NULLABLE ,CASE T2.IS_PRIMARY_KEY when 'TRUE' then 'PRI' END as COLUMN_KEY,  T1.COMMENTS as COLUMN_COMMENT  from  SYS.TABLE_COLUMNS T1 LEFT JOIN SYS.CONSTRAINTS T2  ON T1.SCHEMA_NAME = T2.SCHEMA_NAME AND T1.TABLE_NAME = T2.TABLE_NAME  AND T1.COLUMN_NAME = T2.COLUMN_NAME  where T1.SCHEMA_NAME='" + dbName + "' AND  T1.TABLE_NAME = '" + tableName + "' ORDER BY T1.POSITION ";
        DBUtilJDBC db = new DBUtilJDBC(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        return list;
    }

    public List<Map<String, Object>> getAllViewsForHana2(String dbName, String databaseConfigId) throws Exception {
        String sql = " select view_name as TABLE_NAME from SYS.VIEWS  WHERE SCHEMA_NAME='" + dbName + "' ";
        DBUtilJDBC db = new DBUtilJDBC(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        return list;
    }

    public String getViewSqlForHana2(String databaseName, String tableName, String databaseConfigId) throws Exception {
        String sql = " SELECT DEFINITION FROM SYS.VIEWS WHERE SCHEMA_NAME = '" + databaseName + "' AND VIEW_NAME = '" + tableName + "'";
        String str = "";
        DBUtilJDBC db = new DBUtilJDBC(databaseName, databaseConfigId);
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

    public List<Map<String, Object>> getAllFuntionForHana2(String dbName, String databaseConfigId) throws Exception {
        String sql = " select PROCEDURE_NAME as ROUTINE_NAME from  SYS.PROCEDURES WHERE SCHEMA_NAME='" + dbName + "' ";
        DBUtilJDBC db = new DBUtilJDBC(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        return list;
    }

    public Page<Map<String, Object>> getDataForHana2(Page<Map<String, Object>> page, String tableName, String dbName, String databaseConfigId) throws Exception {
        String sql2;
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        String orderBy = page.getOrderBy();
        String order = page.getOrder();
        DBUtilJDBC db = new DBUtilJDBC(dbName, databaseConfigId);
        String sql = "select * from  " + tableName;
        if (orderBy == null || orderBy.equals("")) {
            sql2 = "select  *  from  " + tableName;
        } else {
            sql2 = "select  *  from  " + tableName + " order by " + orderBy + " " + order;
        }
        List<Map<String, Object>> list = db.queryForListForHana2(sql2, limitFrom, pageSize);
        int rowCount = db.executeQueryForCountForHana2(sql);
        List<Map<String, Object>> columns = getTableColumnsForHana2(dbName, tableName, databaseConfigId);
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

    public int executeQueryForCountForHana2(String sql, String databaseName, String databaseConfigId) throws Exception {
        DBUtilJDBC db = new DBUtilJDBC(databaseName, databaseConfigId);
        return db.executeQueryForCountForHana2(sql);
    }

    public List<Map<String, Object>> getPrimaryKeyssForHana2(String databaseName, String tableName, String databaseConfigId) throws Exception {
        String sql = " SELECT COLUMN_NAME  FROM SYS.CONSTRAINTS  WHERE SCHEMA_NAME='" + databaseName + "' AND TABLE_NAME = '" + tableName.toUpperCase() + "' ";
        DBUtilJDBC db = new DBUtilJDBC(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        return list;
    }

    public List<Map<String, Object>> getTableColumnsForHana2(String dbName, String tableName, String databaseConfigId) throws Exception {
        String sql = "select top 1 * from   " + tableName;
        DBUtilJDBC db = new DBUtilJDBC(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForColumnOnly(sql);
        return list;
    }

    public Page<Map<String, Object>> executeSqlHaveResForHana2(Page<Map<String, Object>> page, String sql, String dbName, String databaseConfigId) throws Exception {
        int rowCount;
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        String sql2 = "select  *  from  (" + sql + ") t   ";
        DBUtilJDBC db = new DBUtilJDBC(dbName, databaseConfigId);
        Date b1 = new Date();
        List<Map<String, Object>> list = db.queryForListForHana2(sql2, limitFrom, pageSize);
        Date b2 = new Date();
        long executeTime = b2.getTime() - b1.getTime();
        if (list.size() < 10 && pageNo == 1) {
            rowCount = list.size();
        } else {
            rowCount = db.executeQueryForCountForHana2(sql);
        }
        List<Map<String, Object>> columns = executeSqlForColumnsForHana2(sql, dbName, databaseConfigId);
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

    public List<Map<String, Object>> executeSqlForColumnsForHana2(String sql, String dbName, String databaseConfigId) throws Exception {
        String sql2 = " select * from (" + sql + ") t   limit 1 ";
        DBUtilJDBC db = new DBUtilJDBC(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.executeSqlForColumns(sql2);
        return list;
    }

    public int updateTableNullAbleForHana2(String databaseName, String tableName, String column_name, String is_nullable, String databaseConfigId) throws Exception {
        String sql4;
        if (column_name != null && !column_name.equals("")) {
            DBUtilJDBC db = new DBUtilJDBC(databaseName, databaseConfigId);
            String column_type = selectOneColumnTypeForHana2(databaseName, tableName, column_name, databaseConfigId);
            if (is_nullable.equals("true")) {
                sql4 = " alter table  " + databaseName + "." + tableName + " alter(" + column_name + " " + column_type + "  NULL )";
            } else {
                sql4 = " alter table  " + databaseName + "." + tableName + " alter(" + column_name + " " + column_type + "  NOT NULL )";
            }
            LogUtil.i("更新字段的is_nullable, sql=" + sql4);
            db.setupdateData(sql4);
            return 0;
        }
        return 0;
    }

    public String selectOneColumnTypeForHana2(String databaseName, String tableName, String column_name, String databaseConfigId) throws Exception {
        String sql = " SELECT CONCAT( CONCAT( CONCAT( DATA_TYPE_NAME ,'('  )  ,LENGTH   ) ,')'  )  as  COLUMN_TYPE   from  SYS.TABLE_COLUMNS  where SCHEMA_NAME='" + databaseName + "' AND  TABLE_NAME = '" + tableName + "' AND COLUMN_NAME = '" + column_name + "'";
        DBUtilJDBC db = new DBUtilJDBC(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        return (String) list.get(0).get("COLUMN_TYPE");
    }

    public int savePrimaryKeyForHana2(String databaseName, String tableName, String column_name, String isSetting, String databaseConfigId) throws Exception {
        if (column_name != null && !column_name.equals("")) {
            DBUtilJDBC db = new DBUtilJDBC(databaseName, databaseConfigId);
            List<Map<String, Object>> list2 = selectTablePrimaryKeyForHana2(databaseName, tableName, databaseConfigId);
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
                String sql42 = " alter table " + tableName + " add primary key (" + primaryKey + ") ";
                db.setupdateData(sql42);
                return 0;
            }
            return 0;
        }
        return 0;
    }

    public List<Map<String, Object>> selectTablePrimaryKeyForHana2(String databaseName, String tableName, String databaseConfigId) throws Exception {
        String sql = "SELECT CONSTRAINT_NAME, COLUMN_NAME FROM  SYS.CONSTRAINTS WHERE  SCHEMA_NAME='" + databaseName + "' AND  TABLE_NAME = '" + tableName + "'";
        DBUtilJDBC db = new DBUtilJDBC(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        new ArrayList();
        return list;
    }

    public int saveDesginColumnForHana2(Map<String, String> map, String databaseName, String tableName, String databaseConfigId) throws Exception {
        DBUtilJDBC db = new DBUtilJDBC(databaseName, databaseConfigId);
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

    public int updateTableColumnForHana2(Map<String, Object> map, String databaseName, String tableName, String columnName, String idValues, String databaseConfigId) throws Exception {
        if (columnName == null || "".equals(columnName)) {
            throw new Exception("数据不完整,保存失败!");
        }
        if (idValues == null || "".equals(idValues)) {
            throw new Exception("数据不完整,保存失败!");
        }
        DBUtilJDBC db = new DBUtilJDBC(databaseName, databaseConfigId);
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

    public int deleteTableColumnForHana2(String databaseName, String tableName, String[] ids, String databaseConfigId) throws Exception {
        DBUtilJDBC db = new DBUtilJDBC(databaseName, databaseConfigId);
        int y = 0;
        for (int i = 0; i < ids.length; i++) {
            String sql = " alter table   " + tableName + " drop (" + ids[i] + ")";
            y += db.setupdateData(sql);
        }
        return y;
    }

    public int saveRowsForHana2(Map<String, String> map, String databaseName, String tableName, String databaseConfigId, String username, String ip) throws Exception {
        DBUtilJDBC db = new DBUtilJDBC(databaseName, databaseConfigId);
        String sql = " INSERT INTO " + tableName;
        String colums = " ";
        String values = " ";
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            colums = String.valueOf(colums) + entry.getKey() + ",";
            String columnType = selectColumnTypeForHana2(databaseName, tableName, entry.getKey(), databaseConfigId);
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
        this.logDao.saveLog(sql2, username, ip, databaseName, databaseConfigId);
        int y = db.setupdateData(sql2);
        return y;
    }

    public String selectColumnTypeForHana2(String databaseName, String tableName, String column, String databaseConfigId) throws Exception {
        String sql = " select DATA_TYPE_NAME as DATA_TYPE  FROM SYS.TABLE_COLUMNS  where SCHEMA_NAME ='" + databaseName + "' AND TABLE_NAME ='" + tableName + "' AND COLUMN_NAME ='" + column + "' ";
        DBUtilJDBC db = new DBUtilJDBC(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        return (String) list.get(0).get("DATA_TYPE");
    }

    public int deleteRowsNewForHana2(String databaseName, String tableName, String primary_key, List<String> condition, String databaseConfigId, String username, String ip) throws Exception {
        DBUtilJDBC db = new DBUtilJDBC(databaseName, databaseConfigId);
        int y = 0;
        for (int i = 0; i < condition.size(); i++) {
            String whereStr = condition.get(i);
            String sql = " delete from  " + tableName + " where  1=1 " + whereStr;
            new Date();
            LogUtil.i("HANA删除数据行, " + DateUtils.getDateTime() + ", sql=" + sql);
            this.logDao.saveLog(sql, username, ip, databaseName, databaseConfigId);
            y += db.setupdateData(sql);
        }
        return y;
    }

    public int saveNewTableForHana2(JSONArray insertArray, String databaseName, String tableName, String databaseConfigId) throws Exception {
        String sql;
        DBUtilJDBC db = new DBUtilJDBC(databaseName, databaseConfigId);
        String sql2 = " create table \"" + tableName + "\" (  ";
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
                sql2 = String.valueOf(sql2) + " NOT NULL  ";
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

    public List<Map<String, Object>> viewTableMessForHana2(String dbName, String tableName, String databaseConfigId) throws Exception {
        String sql = "  select *  from SYS.TABLES   where SCHEMA_NAME ='" + dbName + "' AND TABLE_NAME ='" + tableName + "' ";
        DBUtilJDBC db = new DBUtilJDBC(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        return list;
    }

    public List<Map<String, Object>> selectAllDataFromSQLForHana2(String databaseName, String databaseConfigId, String sql, int limitFrom, int pageSize) throws Exception {
        DBUtilJDBC db = new DBUtilJDBC(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListForHana2(sql, limitFrom, pageSize);
        return list;
    }

    public Integer getTableRowsForHana2(String dbName, String tableName, String databaseConfigId) throws Exception {
        DBUtilJDBC db = new DBUtilJDBC(dbName, databaseConfigId);
        String sql = " select count(*) as NUM from  " + dbName + "." + tableName;
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        Map<String, Object> map = list.get(0);
        Integer num = Integer.valueOf(Integer.parseInt(map.get("NUM").toString()));
        return num;
    }

    public List<Map<String, Object>> exportDataToSQLForHana2(String databaseName, String tableName, List<String> condition, String databaseConfigId) throws Exception {
        String sql = " select * from  " + tableName + " where   1=1  ";
        DBUtilJDBC db = new DBUtilJDBC(databaseName, databaseConfigId);
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < condition.size(); i++) {
            List<Map<String, Object>> list2 = db.queryForListForHana2(String.valueOf(sql) + condition.get(i), 0, 999999999);
            list.add(list2.get(0));
        }
        return list;
    }

    public boolean renameTableForHana2(String databaseName, String tableName, String databaseConfigId, String newTableName) throws Exception {
        String sql4 = " RENAME TABLE  " + databaseName + "." + tableName + " TO " + newTableName;
        DBUtilJDBC db = new DBUtilJDBC(databaseName, databaseConfigId);
        db.setupdateData(sql4);
        return true;
    }

    public boolean copyTableForHana2(String databaseName, String tableName, String databaseConfigId) throws Exception {
        String sql4 = "CREATE  TABLE " + databaseName + "." + tableName + "_" + DateUtils.getDateTimeString(new Date()) + " AS (SELECT * FROM " + databaseName + "." + tableName + ")";
        DBUtilJDBC db = new DBUtilJDBC(databaseName, databaseConfigId);
        db.setupdateData(sql4);
        return true;
    }

    public boolean insertByDataListForHana2(List<Map<String, Object>> dataList, String databaseName, String tableName, String databaseConfigId) throws Exception {
        DBUtilJDBC db = new DBUtilJDBC(databaseName, databaseConfigId);
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

    public boolean updateByDataListForHana2(List<Map<String, Object>> dataList, String databaseName, String tableName, String databaseConfigId, String qualification) throws Exception {
        DBUtilJDBC db = new DBUtilJDBC(databaseName, databaseConfigId);
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

    public boolean insertOrUpdateByDataListForHana2(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        String value;
        List<Map<String, Object>> insertDataList = new ArrayList<>();
        List<Map<String, Object>> updateDataList = new ArrayList<>();
        DBUtilJDBC db = new DBUtilJDBC(databaseName, databaseConfigId);
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
                insertByDataListForHana2(insertDataList, databaseName, table, databaseConfigId);
            }
            if (updateDataList.size() > 0) {
                updateByDataListForHana2(updateDataList, databaseName, table, databaseConfigId, qualification);
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

    public boolean insertOnlyByDataListForHana2(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        String value;
        List<Map<String, Object>> insertDataList = new ArrayList<>();
        DBUtilJDBC db = new DBUtilJDBC(databaseName, databaseConfigId);
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
            insertByDataListForHana2(insertDataList, databaseName, table, databaseConfigId);
            return true;
        }
        return true;
    }

    public boolean deleteByDataListForHana2(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        DBUtilJDBC db = new DBUtilJDBC(databaseName, databaseConfigId);
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

    public String createTableSQLForHana2(String tableName, String databaseName, String databaseConfigId) throws Exception {
        new DBUtilJDBC(databaseName, databaseConfigId);
        StringBuffer sb2 = new StringBuffer();
        sb2.append("CREATE TABLE \"" + tableName + "\" ( \r\n");
        String primary_key_list = "";
        String tableColumnStr = "";
        Map<String, String> TableColumnType = new HashMap<>();
        List<Map<String, Object>> listTableColumn = getTableColumns3ForHana2(databaseName, tableName, databaseConfigId);
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
        return sb2.toString();
    }

    public int executeSqlNotRes(String sql, String dbName, String databaseConfigId) throws Exception {
        new HashMap();
        DBUtilJDBC db = new DBUtilJDBC(dbName, databaseConfigId);
        int i = db.setupdateData(sql);
        return i;
    }

    public boolean checkSqlIsOneTableForHana2(String dbName, String sql, String databaseConfigId) {
        return false;
    }
}