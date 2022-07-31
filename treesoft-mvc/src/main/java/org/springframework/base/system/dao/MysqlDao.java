package org.springframework.base.system.dao;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.base.system.dbUtils.DBUtil2;
import org.springframework.base.system.entity.DmsDto;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.service.SQLParserService;
import org.springframework.base.system.utils.DataUtil;
import org.springframework.base.system.utils.DateUtils;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.base.system.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/dao/MysqlDao.class */
public class MysqlDao {
    @Autowired
    private ConfigDao configDao;
    @Autowired
    private DataSynchronizeDao dataSynchronizeDao;
    @Autowired
    private LogDao logDao;
    @Autowired
    private SQLParserService sqlParserService;

    public List<Map<String, Object>> getAllDataBase(String databaseConfigId) throws Exception {
        Map<String, Object> map0 = this.configDao.getConfigById(databaseConfigId);
        Object databaseName = (String) map0.get("databaseName");
        String isDefaultView = (String) map0.get("isDefaultView");
        DBUtil2 db = new DBUtil2("information_schema", databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(" select * from  information_schema.schemata  ");
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
            if (!schema_name.equals("mysql") && !schema_name.equals("information_schema") && !schema_name.equals("performance_schema") && !schema_name.equals(databaseName)) {
                list2.add(map2);
            }
        }
        return list2;
    }

    public List<Map<String, Object>> getAllFuntionForMySql8(String dbName, String databaseConfigId) {
        List<Map<String, Object>> list = new ArrayList<>();
        return list;
    }

    public List<Map<String, Object>> getAllProc(String dbName, String databaseConfigId) throws Exception {
        String sql = " select NAME as PROC_NAME from mysql.proc where db = '" + dbName + "' and `type` = 'PROCEDURE' order by  NAME ";
        DBUtil2 db = new DBUtil2("mysql", databaseConfigId);
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            list = db.queryForListCommonMethod(sql);
        } catch (Exception e) {
            LogUtil.e("取得全部的存储过程 For MySQL 出错，", e);
        }
        return list;
    }

    public String selectOneColumnType(String databaseName, String tableName, String column_name, String databaseConfigId) throws Exception {
        String sql = " select COLUMN_TYPE  from information_schema.columns where   table_name='" + tableName + "' and table_schema='" + databaseName + "' and column_name='" + column_name + "'";
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        return (String) list.get(0).get("COLUMN_TYPE");
    }

    public int saveDesginColumn(Map<String, String> map, String databaseName, String tableName, String databaseConfigId, String username, String ip) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        String data_type = map.get("DATA_TYPE");
        String character_maximum_length = new StringBuilder(String.valueOf(map.get("CHARACTER_MAXIMUM_LENGTH"))).toString();
        String NUMERIC_SCALE = new StringBuilder(String.valueOf(map.get("NUMERIC_SCALE"))).toString();
        if (StringUtils.isEmpty(NUMERIC_SCALE)) {
            NUMERIC_SCALE = "0";
        }
        String sql = String.valueOf(String.valueOf(" alter table `" + databaseName + "`.`" + tableName + "` add column ") + map.get("COLUMN_NAME") + "  ") + map.get("DATA_TYPE");
        if (map.get("CHARACTER_MAXIMUM_LENGTH") != null && !map.get("CHARACTER_MAXIMUM_LENGTH").equals("")) {
            sql = (data_type.equals("float") || data_type.equals("double") || data_type.equals("decimal")) ? String.valueOf(sql) + " (" + character_maximum_length + ", " + NUMERIC_SCALE + ")" : String.valueOf(sql) + " (" + character_maximum_length + ") ";
        }
        if (map.get("COLUMN_COMMENT") != null && !map.get("COLUMN_COMMENT").equals("")) {
            sql = String.valueOf(sql) + " comment '" + map.get("COLUMN_COMMENT") + "'";
        }
        int y = db.setupdateData(sql);
        this.logDao.saveLog(sql, username, ip, databaseName, databaseConfigId);
        return y;
    }

    public Integer getTableRows(String dbName, String tableName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        String sql = " select count(*) as num from  `" + dbName + "`.`" + tableName + "`";
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        Map<String, Object> map = list.get(0);
        Integer rowCount = Integer.valueOf(Integer.parseInt(map.get("num").toString()));
        return rowCount;
    }

    public List<Map<String, Object>> getAllFuntion(String dbName, String databaseConfigId) {
        String sql = " select  name as ROUTINE_NAME from mysql.proc where db = '" + dbName + "' and `type` = 'FUNCTION' order by name  ";
        DBUtil2 db = new DBUtil2("mysql", databaseConfigId);
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            list = db.queryForListCommonMethod(sql);
        } catch (Exception e) {
            LogUtil.e("取得全部的函数集合 For MySQL 出错，", e);
        }
        return list;
    }

    public List<Map<String, Object>> getAllViews(String dbName, String databaseConfigId) throws Exception {
        String sql = " select TABLE_NAME   from information_schema.TABLES where table_schema='" + dbName + "' and table_type='VIEW' order by TABLE_NAME ";
        DBUtil2 db = new DBUtil2("information_schema", databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        return list;
    }

    public List<Map<String, Object>> getAllTables2(String dbName, String tableName, String databaseConfigId) throws Exception {
        String sql;
        DBUtil2 db = new DBUtil2("information_schema", databaseConfigId);
        String str = " select * from information_schema.TABLES where table_schema='" + dbName + "' and table_type='BASE TABLE' ";
        if (tableName.equals("")) {
            sql = " select TABLE_NAME,ENGINE, VERSION,ROW_FORMAT,TABLE_ROWS,AUTO_INCREMENT,TABLE_COLLATION,TABLE_COMMENT  from information_schema.TABLES where table_schema='" + dbName + "' and table_type='BASE TABLE' ";
        } else {
            sql = " select TABLE_NAME,ENGINE, VERSION,ROW_FORMAT,TABLE_ROWS,AUTO_INCREMENT,TABLE_COLLATION,TABLE_COMMENT  from information_schema.TABLES where table_schema='" + dbName + "' and  table_name='" + tableName + "'  and table_type='BASE TABLE' ";
        }
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        return list;
    }

    public List<Map<String, Object>> getAllTables(String dbName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2("information_schema", databaseConfigId);
        String sql = " select TABLE_NAME from information_schema.TABLES where table_schema='" + dbName + "' and table_type='BASE TABLE'  order by TABLE_NAME ";
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        return list;
    }

    public List<Map<String, Object>> getAllTrigger(String dbName, String databaseConfigId) throws Exception {
        String sql = "select TRIGGER_NAME  FROM information_schema.triggers   where TRIGGER_SCHEMA='" + dbName + "' order by TRIGGER_NAME ";
        DBUtil2 db = new DBUtil2("information_schema", databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        return list;
    }

    public List<Map<String, Object>> getAllEvent(String dbName, String databaseConfigId) throws Exception {
        String sql = "SELECT EVENT_NAME FROM information_schema.events WHERE  EVENT_SCHEMA='" + dbName + "' order by EVENT_NAME ";
        DBUtil2 db = new DBUtil2("information_schema", databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        return list;
    }

    public List<Map<String, Object>> getTableColumns(String dbName, String tableName, String databaseConfigId) throws Exception {
        String sql = "select * from  `" + dbName + "`.`" + tableName + "`  limit 1 ";
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForColumnOnly(sql);
        return list;
    }

    public List<Map<String, Object>> getTableColumns3(String dbName, String tableName, String databaseConfigId) throws Exception {
        String sql = " select column_name as TREESOFTPRIMARYKEY, COLUMN_NAME,COLUMN_TYPE , DATA_TYPE ,COLUMN_DEFAULT , CASE DATA_TYPE  WHEN 'tinyint' THEN    NUMERIC_PRECISION   WHEN  'smallint' THEN    NUMERIC_PRECISION  WHEN  'int' THEN   \t  NUMERIC_PRECISION  WHEN  'integer' THEN  \t  NUMERIC_PRECISION  WHEN  'bigint' THEN  \t  NUMERIC_PRECISION  WHEN  'float' THEN  \t  NUMERIC_PRECISION  WHEN  'double' THEN  \t  NUMERIC_PRECISION  WHEN  'decimal' THEN  \t  NUMERIC_PRECISION  ELSE   \t  CHARACTER_MAXIMUM_LENGTH    END AS CHARACTER_MAXIMUM_LENGTH,ifnull( NUMERIC_PRECISION,'' ) as NUMERIC_PRECISION ,ifnull( NUMERIC_SCALE,'' ) as NUMERIC_SCALE, IS_NULLABLE, COLUMN_KEY,extra, COLUMN_COMMENT  from information_schema.columns  WHERE  table_name='" + tableName + "' and table_schema='" + dbName + "' ";
        DBUtil2 db = new DBUtil2("information_schema", databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        return list;
    }

    public List<Map<String, Object>> getTableIndexForMySQL(String dbName, String tableName, String databaseConfigId) throws Exception {
        String sql = " SELECT non_unique,index_name, column_name  from  information_schema.statistics where table_schema = '" + dbName + "' and  table_name ='" + tableName + "'  and  index_name <>'PRIMARY'  order by index_name,seq_in_index;  ";
        DBUtil2 db = new DBUtil2("information_schema", databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        return list;
    }

    public Page<Map<String, Object>> getDataForMySql(Page<Map<String, Object>> page, String tableName, String dbName, String databaseConfigId) throws Exception {
        Iterator<Map<String, Object>> it;
        String sql2;
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        String orderBy = page.getOrderBy();
        String order = page.getOrder();
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        List<Map<String, Object>> list3 = getPrimaryKeyss(dbName, tableName, databaseConfigId);
        String primaryKey = "";
        while (list3.iterator().hasNext()) {
            primaryKey = String.valueOf(primaryKey) + it.next().get("COLUMN_NAME") + ",";
        }
        if (!StringUtils.isEmpty(primaryKey)) {
            primaryKey = primaryKey.substring(0, primaryKey.length() - 1);
        }
        String sql = "select count(*) from  `" + tableName + "`";
        if (StringUtils.isEmpty(orderBy)) {
            sql2 = "  select  *  from `" + tableName + "`  LIMIT " + limitFrom + "," + pageSize;
        } else {
            sql2 = "select  *  from `" + tableName + "`  order by " + orderBy + " " + order + "  LIMIT " + limitFrom + "," + pageSize;
        }
        List<Map<String, Object>> list = db.queryForListForMySql(sql2);
        int rowCount = db.executeQueryForCount(sql);
        List<Map<String, Object>> columns = getTableColumns(dbName, tableName, databaseConfigId);
        List<Map<String, Object>> tempList = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<>();
        map1.put("field", "treeSoftPrimaryKey");
        map1.put("checkbox", true);
        tempList.add(map1);
        Iterator<Map<String, Object>> it2 = columns.iterator();
        while (it2.hasNext()) {
            Map<String, Object> map = it2.next();
            Map<String, Object> map2 = new HashMap<>();
            map2.put("field", map.get("column_name"));
            map2.put("title", map.get("column_name"));
            map2.put("sortable", true);
            if (map.get("data_type").equals("DATETIME")) {
                map2.put("editor", "datetimebox");
            } else if (map.get("data_type").equals("INT") || map.get("data_type").equals("SMALLINT") || map.get("data_type").equals("TINYINT") || map.get("data_type").equals("BIGINT")) {
                map2.put("editor", "numberbox");
            } else if (map.get("data_type").equals("DOUBLE") || map.get("data_type").equals("DECIMAL")) {
                map2.put("editor", "numberbox");
            } else if (!map.get("data_type").equals("BLOB") && !map.get("data_type").equals("CLOB") && !map.get("data_type").equals("blob") && !map.get("data_type").equals("longblob") && !map.get("data_type").equals("mediumblob") && !map.get("data_type").equals("tinyblob") && !map.get("data_type").equals("VARBINARY") && !map.get("data_type").equals("BINARY")) {
                map2.put("editor", "text");
            }
            tempList.add(map2);
        }
        ObjectMapper mapper = new ObjectMapper();
        String jsonfromList = "[" + mapper.writeValueAsString(tempList) + "]";
        page.setTotalCount(rowCount);
        page.setResult(list);
        page.setColumns(jsonfromList);
        page.setPrimaryKey(primaryKey);
        if (StringUtils.isEmpty(primaryKey)) {
            page.setOperator("read");
        } else {
            page.setOperator("edit");
        }
        return page;
    }

    public Page<Map<String, Object>> executeSqlHaveResultForMySql(Page<Map<String, Object>> page, String sql, String dbName, String databaseConfigId) throws Exception {
        String sql2;
        int rowCount;
        List<Map<String, Object>> columns;
        String orderBy = page.getOrderBy();
        String order = page.getOrder();
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        Map<String, Object> map0 = this.configDao.getConfigById(databaseConfigId);
        String isRead = (String) map0.get("isRead");
        String sql3 = sql.trim();
        if (StringUtils.isEmpty(orderBy)) {
            sql2 = "  ( " + sql3 + " )  LIMIT " + limitFrom + "," + pageSize;
        } else {
            sql2 = " ( " + sql3 + " ) order by " + orderBy + " " + order + " LIMIT " + limitFrom + "," + pageSize;
        }
        if (sql3.toLowerCase().indexOf("show") == 0 || sql3.toLowerCase().indexOf("explain") == 0) {
            sql2 = sql3;
        }
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        Date b1 = new Date();
        List<Map<String, Object>> list = db.queryForListForMySql(sql2);
        Date b2 = new Date();
        long executeTime = b2.getTime() - b1.getTime();
        if (sql3.indexOf("show") == 0 || sql3.indexOf("SHOW") == 0 || sql3.indexOf("explain") == 0 || sql3.indexOf("EXPLAIN") == 0) {
            rowCount = list.size();
        } else if (list.size() < 10 && pageNo == 1) {
            rowCount = list.size();
        } else {
            rowCount = db.executeQueryForCountForMySQL(sql3);
        }
        List<Map<String, Object>> tempList = new ArrayList<>();
        String tableName = this.sqlParserService.parserTableNames(sql3, "MySQL");
        List<Map<String, Object>> primaryKeyList = getPrimaryKeyss(dbName, tableName, databaseConfigId);
        String primaryKey = "";
        Iterator<Map<String, Object>> it = primaryKeyList.iterator();
        while (it.hasNext()) {
            Map<String, Object> map3 = it.next();
            primaryKey = String.valueOf(primaryKey) + map3.get("COLUMN_NAME") + ",";
        }
        if (!StringUtils.isEmpty(primaryKey)) {
            primaryKey = primaryKey.substring(0, primaryKey.length() - 1);
        }
        new ArrayList();
        String tempStr = sql3.toLowerCase().replace(" ", "");
        if (tempStr.indexOf("show") == 0) {
            columns = db.executeSqlForColumns(sql3);
        } else {
            columns = db.executeSqlForColumns(" (" + sql3 + ")  limit  1");
        }
        Iterator<Map<String, Object>> it2 = columns.iterator();
        while (it2.hasNext()) {
            Map<String, Object> map = it2.next();
            Map<String, Object> map2 = new HashMap<>();
            map2.put("field", map.get("column_name"));
            map2.put("title", map.get("column_name"));
            map2.put("sortable", true);
            if (map.get("data_type").equals("DATETIME")) {
                map2.put("editor", "datetimebox");
            } else if (map.get("data_type").equals("INT") || map.get("data_type").equals("SMALLINT") || map.get("data_type").equals("TINYINT")) {
                map2.put("editor", "numberbox");
            } else if (map.get("data_type").equals("BIGINT")) {
                map2.put("editor", "numberbox");
            } else if (map.get("data_type").equals("DOUBLE")) {
                map2.put("editor", "numberbox");
            } else if (map.get("data_type").equals("BLOB") || map.get("data_type").equals("CLOB") || map.get("data_type").equals("blob") || map.get("data_type").equals("longblob") || map.get("data_type").equals("mediumblob") || map.get("data_type").equals("tinyblob")) {
                map2.put("editor", null);
            } else if (map.get("data_type").equals("VARBINARY") || map.get("data_type").equals("BINARY")) {
                map2.put("editor", null);
            } else {
                map2.put("editor", "text");
            }
            tempList.add(map2);
        }
        boolean isResultExistsPK = isResultExistsPK(primaryKeyList, columns);
        ObjectMapper mapper = new ObjectMapper();
        String jsonfromList = "[" + mapper.writeValueAsString(tempList) + "]";
        page.setTotalCount(rowCount);
        page.setResult(list);
        page.setColumns(jsonfromList);
        page.setPrimaryKey(primaryKey);
        page.setTableName(tableName);
        page.setExecuteTime(new StringBuilder(String.valueOf(executeTime)).toString());
        if (StringUtil.isNotEmpty(isRead) && isRead.equals("1")) {
            page.setOperator("read");
        } else if (!isResultExistsPK) {
            page.setOperator("read");
        } else if (StringUtil.isEmpty(primaryKey) || StringUtil.isEmpty(tableName)) {
            page.setOperator("read");
        } else {
            page.setOperator("edit");
        }
        return page;
    }

    public boolean isResultExistsPK(List<Map<String, Object>> primaryKeys, List<Map<String, Object>> columns) {
        if (primaryKeys == null || primaryKeys.size() == 0 || columns == null || columns.size() == 0) {
            return false;
        }
        List<String> tempList = new ArrayList<>();
        Iterator<Map<String, Object>> it = columns.iterator();
        while (it.hasNext()) {
            Map<String, Object> map = it.next();
            tempList.add(map.get("column_name").toString());
        }
        Iterator<Map<String, Object>> it2 = primaryKeys.iterator();
        while (it2.hasNext()) {
            Map<String, Object> mapPK = it2.next();
            String primaryKey = mapPK.get("COLUMN_NAME").toString();
            if (!tempList.contains(primaryKey)) {
                return false;
            }
        }
        return true;
    }

    public List<Map<String, Object>> getPrimaryKeyss(String databaseName, String tableName, String databaseConfigId) throws Exception {
        if (StringUtils.isEmpty(databaseName) || StringUtils.isEmpty(tableName)) {
            return new ArrayList();
        }
        String sql = " select COLUMN_NAME  from information_schema.columns where  table_name='" + tableName + "' and table_schema='" + databaseName + "' and column_key='PRI' ";
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        return list;
    }

    public int updateTableNullAble(String databaseName, String tableName, String column_name, String is_nullable, String databaseConfigId) throws Exception {
        String sql4;
        if (column_name != null && !column_name.equals("")) {
            DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
            String column_type = selectOneColumnType(databaseName, tableName, column_name, databaseConfigId);
            if (is_nullable.equals("true")) {
                sql4 = " alter table  " + databaseName + "." + tableName + " modify column " + column_name + " " + column_type + "  null ";
            } else {
                sql4 = " alter table  " + databaseName + "." + tableName + " modify column " + column_name + " " + column_type + " not null ";
            }
            db.setupdateData(sql4);
            return 0;
        }
        return 0;
    }

    public int upDownColumn(String databaseName, String tableName, String column_name, String column_name2, String databaseConfigId) throws Exception {
        String sql4;
        if (column_name != null && !column_name.equals("")) {
            DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
            String column_type = selectOneColumnType(databaseName, tableName, column_name, databaseConfigId);
            if (column_name2 == null || column_name2.equals("")) {
                sql4 = " alter table  " + databaseName + "." + tableName + " modify column " + column_name + " " + column_type + " first ";
            } else {
                sql4 = " alter table  " + databaseName + "." + tableName + " modify column " + column_name + " " + column_type + " after " + column_name2;
            }
            db.setupdateData(sql4);
            return 0;
        }
        return 0;
    }

    public int deleteTableColumn(String databaseName, String tableName, String[] ids, String databaseConfigId, String username, String ip) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        int y = 0;
        for (int i = 0; i < ids.length; i++) {
            String sql = " alter table   " + databaseName + "." + tableName + " drop column  " + ids[i];
            y += db.setupdateData(sql);
            this.logDao.saveLog(sql, username, ip, databaseName, databaseConfigId);
        }
        return y;
    }

    public int savePrimaryKey(String databaseName, String tableName, String column_name, String isSetting, String databaseConfigId) throws Exception {
        String sql4;
        if (column_name != null && !column_name.equals("")) {
            DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
            List<String> list2 = selectTablePrimaryKey(databaseName, tableName, databaseConfigId);
            if (isSetting.equals("true")) {
                list2.add(column_name);
            } else {
                list2.remove(column_name);
            }
            String tem = list2.toString();
            String primaryKey = tem.substring(1, tem.length() - 1);
            if (primaryKey.equals("")) {
                sql4 = " alter table  " + databaseName + "." + tableName + " drop primary key ";
            } else if (list2.size() == 1 && isSetting.equals("true")) {
                sql4 = " alter table  " + databaseName + "." + tableName + " add primary key (" + primaryKey + ")";
            } else {
                sql4 = " alter table  " + databaseName + "." + tableName + " drop primary key, add primary key (" + primaryKey + ")";
            }
            db.setupdateData(sql4);
            return 0;
        }
        return 0;
    }

    public List<String> selectTablePrimaryKey(String databaseName, String tableName, String databaseConfigId) throws Exception {
        String sql = " select column_name   from information_schema.columns where   table_name='" + tableName + "' and table_schema='" + databaseName + "'  and column_key='PRI' ";
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        List<String> list2 = new ArrayList<>();
        Iterator<Map<String, Object>> it = list.iterator();
        while (it.hasNext()) {
            Map<String, Object> map = it.next();
            list2.add((String) map.get("column_name"));
        }
        return list2;
    }

    public String getViewSql(String databaseName, String tableName, String databaseConfigId) throws Exception {
        String sql = " select  view_definition  from  information_schema.VIEWS  where  table_name='" + tableName + "' and table_schema='" + databaseName + "'  ";
        String str = "";
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        if (list.size() == 1) {
            Map<String, Object> map = list.get(0);
            str = (String) map.get("view_definition");
        }
        return str;
    }

    public List<Map<String, Object>> getTableColumns2(String databaseName, String tableName, String databaseConfigId) throws Exception {
        String sql = "select * from  `" + databaseName + "`.`" + tableName + "` limit 1";
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForColumnOnly(sql);
        return list;
    }

    public List<Map<String, Object>> selectAllDataFromSQLForMysql(String databaseName, String databaseConfigId, String sql, int limitFrom, int pageSize) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListForMySqlForExport(sql, limitFrom, pageSize);
        return list;
    }

    public boolean checkSqlIsOneTableForMySql(String dbName, String sql, String databaseConfigId) {
        String tableName = "";
        try {
            DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
            Pattern p = Pattern.compile("\\s+");
            Matcher m = p.matcher(sql);
            String temp = m.replaceAll(" ").toLowerCase();
            if (temp.indexOf("select * from") >= 0) {
                for (int y = 14; y < temp.length(); y++) {
                    String c = new StringBuilder(String.valueOf(temp.charAt(y))).toString();
                    if (c.equals(" ")) {
                        break;
                    }
                    tableName = String.valueOf(tableName) + c;
                }
                List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
                if (list.size() > 0) {
                    return true;
                }
                return false;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public int executeQueryForCountForMySQL(String sql, String databaseName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        return db.executeQueryForCountForMySQL(sql);
    }

    public int executeQueryForMaxKeyForMySQL(String sql, String databaseName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        Map<String, Object> map = list.get(0);
        int max_num = Integer.parseInt(map.get("max_num").toString());
        return max_num;
    }

    public String getProcedureSqlForMySql(String databaseName, String tableName, String databaseConfigId) throws Exception {
        String sql = " SELECT  convert( body using utf8 ) as body  FROM  mysql.PROC  where  name ='" + tableName + "'";
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        Map<String, Object> map = list.get(0);
        String defineSQL = map.get("body").toString();
        return defineSQL;
    }

    public String getViewSqlForMySql8(String databaseName, String tableName, String databaseConfigId) throws Exception {
        String sql = " select  VIEW_DEFINITION  from  information_schema.VIEWS  where  table_name='" + tableName + "' and table_schema='" + databaseName + "'  ";
        String str = "";
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        if (list.size() == 1) {
            Map<String, Object> map = list.get(0);
            str = (String) map.get("VIEW_DEFINITION");
        }
        return str;
    }

    public boolean backupDatabaseExecute(String databaseName, String tableName, String path, String databaseConfigId) throws Exception {
        BackDbForMySql ff = new BackDbForMySql();
        ff.readDataToFile(databaseName, tableName, path, databaseConfigId);
        return true;
    }

    public boolean copyTableForMySql(String databaseName, String tableName, String databaseConfigId) throws Exception {
        String sql4 = "create table " + databaseName + "." + tableName + "_" + DateUtils.getDateTimeString(new Date()) + "  select * from " + databaseName + "." + tableName;
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        db.setupdateData(sql4);
        return true;
    }

    public boolean renameTableForMySql(String databaseName, String tableName, String databaseConfigId, String newTableName) throws Exception {
        String renameSQL = " rename table  `" + tableName + "`  TO   `" + newTableName + "`";
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        db.setupdateData(renameSQL);
        return true;
    }

    public List<Map<String, Object>> exportDataToSQLForMySQL(String databaseName, String tableName, List<String> condition, String databaseConfigId) throws Exception {
        String sql = " select * from   `" + tableName + "`  where   1=1  ";
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < condition.size(); i++) {
            List<Map<String, Object>> list2 = db.queryForListForMySqlForExport(String.valueOf(sql) + condition.get(i), 0, 999999999);
            if (list2.size() > 0) {
                list.add(list2.get(0));
            }
        }
        return list;
    }

    public List<Map<String, Object>> viewTableMessForMySql(String dbName, String tableName, String databaseConfigId) throws Exception {
        String sql = " select  t1.* , t2.CHARACTER_SET_NAME  from information_schema.tables t1 left join   information_schema.COLLATION_CHARACTER_SET_APPLICABILITY  t2  on t1.table_collation = t2.collation_name where  t1.table_name='" + tableName + "' and  t1.table_schema='" + dbName + "'  ";
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        return list;
    }

    public boolean insertByDataListForMySQL(List<Map<String, Object>> dataList, String databaseName, String tableName, String databaseConfigId) throws Exception {
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
                    String tempValues = entry.getValue().toString();
                    values = String.valueOf(values) + "'" + tempValues.replace("\\", "\\\\").replace("'", "\\'").replace("\r\n", "\\r\\n").replace("\n\r", "\\n\\r").replace("\r", "\\r").replace("\n", "\\n") + "',";
                }
            }
            sqlList.add(String.valueOf(insertSQL) + " ( " + colums.substring(0, colums.length() - 1) + ") VALUES (" + values.substring(0, values.length() - 1) + " ) ");
            if (sqlList.size() > 2000) {
                db.updateExecuteBatch(sqlList);
                sqlList.clear();
            }
        }
        if (sqlList.size() > 0) {
            db.updateExecuteBatch(sqlList);
            return true;
        }
        return true;
    }

    public boolean judgeTableExistsForMySQL(Map<String, Object> jobMessageMap, String sourceDbType, String targetDbType) throws Exception {
        String sourceSQL;
        String str;
        String sourceConfigId = new StringBuilder().append(jobMessageMap.get("sourceConfigId")).toString();
        String sourceDataBase = new StringBuilder().append(jobMessageMap.get("sourceDataBase")).toString();
        String doSql = new StringBuilder().append(jobMessageMap.get("doSql")).toString();
        String targetConfigId = new StringBuilder().append(jobMessageMap.get("targetConfigId")).toString();
        String targetDataBase = new StringBuilder().append(jobMessageMap.get("targetDataBase")).toString();
        String targetTable = new StringBuilder().append(jobMessageMap.get("targetTable")).toString();
        String createSQL = "create table " + targetTable + " (";
        if (sourceDbType.equals("Hive2") || sourceDbType.equals("DB2") || sourceDbType.equals("ShenTong") || sourceDbType.equals("Informix")) {
            return false;
        }
        if (sourceDbType.equals("MySQL") || sourceDbType.equals("MariaDB") || sourceDbType.equals("MySQL8.0")) {
            sourceSQL = " select * from ( " + doSql + " ) tab  limit 1";
        } else if (sourceDbType.equals("Oracle")) {
            sourceSQL = " SELECT TAB.*, ROWNUM  FROM (" + doSql + " ) TAB  WHERE ROWNUM <= 1 ";
        } else if (sourceDbType.equals("SQL Server") || sourceDbType.equals("Cache") || sourceDbType.equals("Sybase")) {
            sourceSQL = " select top 1 *  FROM (" + doSql + " ) tab ";
        } else {
            sourceSQL = " select * from ( " + doSql + " ) tab  limit 1";
        }
        DBUtil2 sourceDB = new DBUtil2(sourceDataBase, sourceConfigId);
        List<Map<String, Object>> sourceList = sourceDB.queryForListCommonMethod(sourceSQL);
        if (sourceList.size() == 0) {
            return false;
        }
        Map<String, Object> sourceMap = sourceList.get(0);
        DBUtil2 targetDB = new DBUtil2(targetDataBase, targetConfigId);
        targetDB.getConnection();
        try {
            String targetSQL = " select * from " + targetTable + " where 1=2 ";
            targetDB.queryForListForMySql(targetSQL);
            return true;
        } catch (Exception e) {
            LogUtil.i("数据同步时，目标库不存在表 " + targetTable + ", 系统已自动新建该表，字段类型、数值精度可能与实际不符，请及时人工修正。");
            if (0 == 0) {
                Iterator<Map.Entry<String, Object>> it = sourceMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, Object> entry = it.next();
                    String columsName = new StringBuilder(String.valueOf(entry.getKey())).toString();
                    if (!sourceDbType.equals("Oracle") || !columsName.equals("ROWNUM")) {
                        if (entry.getValue() instanceof java.sql.Date) {
                            str = " date ,";
                        } else if ((entry.getValue() instanceof Time) || (entry.getValue() instanceof Date)) {
                            str = " datetime ,";
                        } else if (entry.getValue() instanceof Timestamp) {
                            str = " timestamp ,";
                        } else if ((entry.getValue() instanceof Integer) || (entry.getValue() instanceof BigInteger)) {
                            str = " int ,";
                        } else if (entry.getValue() instanceof BigDecimal) {
                            str = " decimal(15,5) ,";
                        } else if (entry.getValue() instanceof Float) {
                            str = " float ,";
                        } else if ((entry.getValue() instanceof Long) || (entry.getValue() instanceof Double)) {
                            str = " double ,";
                        } else if (entry.getValue() instanceof Boolean) {
                            str = " tinyint ,";
                        } else if (entry.getValue() instanceof Byte) {
                            str = " Blob ,";
                        } else {
                            str = " varchar(255) ,";
                        }
                        String columsType = str;
                        createSQL = String.valueOf(createSQL) + columsName + columsType;
                    }
                }
                String createSQL2 = String.valueOf(createSQL.substring(0, createSQL.length() - 1)) + " )";
                if (targetDbType.equals("MySQL") || targetDbType.equals("MariaDB") || targetDbType.equals("MySQL8.0") || targetDbType.equals("TiDB")) {
                    createSQL2 = String.valueOf(createSQL2) + " CHARACTER SET utf8 COLLATE utf8_general_ci ";
                }
                String dataSynchronizeId = new StringBuilder().append(jobMessageMap.get("id")).toString();
                targetDB.setupdateData(createSQL2);
                this.dataSynchronizeDao.dataSynchronizeLogSave("1", "数据同步时，目标库不存在表 " + targetTable + ", 系统已自动新建该表，字段类型、数值精度可能与实际不符，请及时人工修正。", dataSynchronizeId, "", "");
                return true;
            }
            return true;
        }
    }

    public boolean deleteByDataListForMySQL(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
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

    public String getProcSqlForSQL(String databaseName, String proc_name, String databaseConfigId) {
        String sql = " show create procedure  " + proc_name;
        String str = "";
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        try {
            List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
            if (list.size() == 1) {
                Map<String, Object> map = list.get(0);
                str = (String) map.get("Create Procedure");
            }
        } catch (Exception e) {
            str = "";
        }
        return str;
    }

    public Page<Map<String, Object>> dataStatisticsListForMySql(Page<Map<String, Object>> page, String tableName, String databaseName, String databaseConfigId) throws Exception {
        String sql;
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        String orderBy = page.getOrderBy();
        String order = page.getOrder();
        String sql2 = " select TABLE_SCHEMA ,TABLE_NAME ,ENGINE ,TABLE_ROWS ,  AVG_ROW_LENGTH,  case    when (1<(DATA_LENGTH/1024) and (DATA_LENGTH/1024) <1024) then concat( ROUND(DATA_LENGTH/1024),'KB')     when DATA_LENGTH/1024/1024 >=1 then concat( ROUND(DATA_LENGTH/1024/1024 ,2 ),'MB')  else    concat(DATA_LENGTH ,'B')  end as DATA_LENGTH, case    when (1< (INDEX_LENGTH/1024) and (INDEX_LENGTH/1024) <1024) then concat( ROUND( INDEX_LENGTH/1024),'KB')    when INDEX_LENGTH/1024/1024 >=1 then concat( ROUND( INDEX_LENGTH/1024/1024 ,2 ),'MB')  else  concat( INDEX_LENGTH ,'B')  end as INDEX_LENGTH, case    when (1<(DATA_LENGTH + INDEX_LENGTH)/1024 and (DATA_LENGTH + INDEX_LENGTH)/1024 <1024) then concat( ROUND( (DATA_LENGTH + INDEX_LENGTH) /1024),'KB')    when (DATA_LENGTH + INDEX_LENGTH)/1024/1024 >=1 then concat( ROUND( (DATA_LENGTH + INDEX_LENGTH) /1024/1024 ,2 ),'MB')  else    concat( (DATA_LENGTH + INDEX_LENGTH) ,'B')  end as TOTAL_LENGTH ,  DATE_FORMAT(CREATE_TIME, '%Y-%m-%d %H:%i:%S') as CREATE_TIME, TABLE_COLLATION  from  information_schema.tables  where table_schema='" + databaseName + "' and TABLE_TYPE='BASE TABLE' ";
        if (!StringUtils.isEmpty(tableName)) {
            sql2 = String.valueOf(sql2) + " and TABLE_NAME like '%" + tableName + "%' ";
        }
        int rowCount = db.executeQueryForCount(" select count(*) from (" + sql2 + ") t ");
        if (orderBy == null || orderBy.equals("")) {
            sql = String.valueOf(sql2) + "  limit " + limitFrom + "," + pageSize;
        } else {
            sql = String.valueOf(sql2) + " order by " + orderBy + " " + order + "  limit " + limitFrom + "," + pageSize;
        }
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        page.setTotalCount(rowCount);
        page.setResult(list);
        return page;
    }

    public int saveRows(Map<String, Object> map, String databaseName, String tableName, String databaseConfigId, String username, String ip) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        String sql = " insert into `" + databaseName + "`.`" + tableName + "`";
        String colums = " ";
        String values = " ";
        Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            colums = String.valueOf(colums) + "`" + entry.getKey() + "`,";
            String columnType = selectOneColumnType(databaseName, tableName, entry.getKey(), databaseConfigId);
            String str = new StringBuilder().append(entry.getValue()).toString();
            if (str.equals("")) {
                values = String.valueOf(values) + " null ,";
            } else if (columnType.indexOf("integer") >= 0 || columnType.indexOf("bit") >= 0 || columnType.indexOf("int") >= 0 || columnType.indexOf("float") >= 0) {
                values = String.valueOf(values) + entry.getValue() + ",";
            } else {
                String tempValue = new StringBuilder().append(entry.getValue()).toString();
                values = String.valueOf(values) + "'" + tempValue.replaceAll("'", "'").replaceAll("\\\\", "\\\\\\\\") + "',";
            }
        }
        String sql2 = String.valueOf(sql) + " (" + colums.substring(0, colums.length() - 1) + ") values (" + values.substring(0, values.length() - 1) + ")";
        this.logDao.saveLog(sql2, username, ip, databaseName, databaseConfigId);
        int y = db.setupdateData(sql2);
        return y;
    }

    public List<Map<String, Object>> allDataStatisticsListForMySql(String databaseName, String databaseConfigId, String tableName) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        String sql = " select TABLE_SCHEMA ,TABLE_NAME ,ENGINE ,TABLE_ROWS ,  AVG_ROW_LENGTH,  case    when (1<(DATA_LENGTH/1024) and (DATA_LENGTH/1024) <1024) then concat( ROUND(DATA_LENGTH/1024),'KB')     when DATA_LENGTH/1024/1024 >=1 then concat( ROUND(DATA_LENGTH/1024/1024 ,2 ),'MB')  else    concat(DATA_LENGTH ,'B')   end as DATA_LENGTH, case    when (1< (INDEX_LENGTH/1024) and (INDEX_LENGTH/1024) <1024) then concat( ROUND( INDEX_LENGTH/1024),'KB')    when INDEX_LENGTH/1024/1024 >=1 then concat( ROUND( INDEX_LENGTH/1024/1024 ,2 ),'MB')  else  concat( INDEX_LENGTH ,'B')   end as INDEX_LENGTH, case    when (1<(DATA_LENGTH + INDEX_LENGTH)/1024 and (DATA_LENGTH + INDEX_LENGTH)/1024 <1024) then concat( ROUND( (DATA_LENGTH + INDEX_LENGTH) /1024),'KB')    when (DATA_LENGTH + INDEX_LENGTH)/1024/1024 >=1 then concat( ROUND( (DATA_LENGTH + INDEX_LENGTH) /1024/1024 ,2 ),'MB')  else    concat( (DATA_LENGTH + INDEX_LENGTH) ,'B')  end as TOTAL_LENGTH ,  CREATE_TIME, TABLE_COLLATION  from  information_schema.tables  where table_schema='" + databaseName + "' and TABLE_TYPE='BASE TABLE' ";
        if (!StringUtils.isEmpty(tableName)) {
            sql = String.valueOf(sql) + " and TABLE_NAME like '%" + tableName + "%' ";
        }
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        return list;
    }

    public String createTableSQL(String tableName, String databaseName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        String createTableSQL = "";
        List<Map<String, Object>> list4 = db.queryForListCommonMethod("show create table `" + tableName + "`");
        if (list4.size() > 0) {
            Map<String, Object> mapp = list4.get(0);
            createTableSQL = (String) mapp.get("Create Table");
        }
        return createTableSQL;
    }

    public boolean insertOrUpdateByDataListForMySQL(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
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
            LogUtil.e("insertOrUpdateByDataListForMySQL error,", e3);
            try {
                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException e4) {
            }
        }
        if (insertDataList.size() > 0) {
            insertByDataListForMySQL(insertDataList, databaseName, table, databaseConfigId);
        }
        if (updateDataList.size() > 0) {
            updateByDataListForMySQL(updateDataList, databaseName, table, databaseConfigId, qualification);
            return true;
        }
        return true;
    }

    public boolean updateByDataListForMySQL(List<Map<String, Object>> dataList, String databaseName, String tableName, String databaseConfigId, String qualification) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<String> sqlList = new ArrayList<>();
        String[] whereColumn = qualification.split(",");
        Iterator<Map<String, Object>> it = dataList.iterator();
        while (it.hasNext()) {
            Map<String, Object> map4 = it.next();
            String updateSQL = "update `" + tableName + "`  set ";
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
                        updateSQL = String.valueOf(updateSQL2) + "'" + tempValues.replace("\\", "\\\\").replace("'", "\\'").replace("\r\n", "\\r\\n").replace("\n\r", "\\n\\r").replace("\r", "\\r").replace("\n", "\\n") + "',";
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

    public boolean insertOnlyByDataListForMySQL(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
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
                insertByDataListForMySQL(insertDataList, databaseName, table, databaseConfigId);
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

    public Map<String, Object> queryDatabaseStatus(String databaseName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(" show global status  ");
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> temp = list.get(i);
            String Variable_name = (String) temp.get("Variable_name");
            String Value = (String) temp.get("Value");
            map.put(Variable_name, Value);
        }
        return map;
    }

    public Integer queryDatabaseConnected(String databaseName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(" show status like 'Threads_connected%'  ");
        if (list.size() >= 0) {
            String Value = list.get(0).get("Value").toString();
            return Integer.valueOf(Integer.parseInt(Value));
        }
        return 0;
    }

    public int updateTableColumn(Map<String, Object> map, String databaseName, String tableName, String columnName, String idValues, String databaseConfigId, String username, String ip) throws Exception {
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
        String NUMERIC_SCALE = new StringBuilder().append(map.get("NUMERIC_SCALE")).toString();
        String column_comment = (String) map.get("COLUMN_COMMENT");
        if (StringUtils.isEmpty(NUMERIC_SCALE)) {
            NUMERIC_SCALE = "0";
        }
        if (!old_field_name.endsWith(column_name)) {
            String sql = String.valueOf(" alter table  " + databaseName + "." + tableName + " change ") + old_field_name + " " + column_name + " " + data_type;
            if (character_maximum_length != null && !character_maximum_length.equals("")) {
                sql = String.valueOf(sql) + " (" + character_maximum_length + ")";
            }
            db.setupdateData(sql);
        }
        String sql2 = " alter table  " + databaseName + "." + tableName + " modify column " + column_name + " " + data_type;
        if (character_maximum_length != null && !character_maximum_length.equals("")) {
            sql2 = (data_type.equals("float") || data_type.equals("double") || data_type.equals("decimal")) ? String.valueOf(sql2) + " (" + character_maximum_length + ", " + NUMERIC_SCALE + ")" : String.valueOf(sql2) + " (" + character_maximum_length + ")";
        }
        if (column_comment != null && !column_comment.equals("")) {
            sql2 = String.valueOf(sql2) + " comment '" + column_comment + "'";
        }
        int y = db.setupdateData(sql2);
        this.logDao.saveLog(sql2, username, ip, databaseName, databaseConfigId);
        return y;
    }

    public List<Map<String, Object>> queryExplainSQLForMysql(DmsDto dto) throws Exception {
        String sql;
        DBUtil2 db = new DBUtil2(dto.getDatabaseName(), dto.getDatabaseConfigId());
        String sql2 = dto.getSql();
        if (sql2.toLowerCase().indexOf("explain ") == 0) {
            sql = dto.getSql();
        } else {
            sql = " explain " + dto.getSql();
        }
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        return list;
    }
}
