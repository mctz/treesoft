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
import javax.servlet.http.HttpServletRequest;
import org.springframework.base.system.dbUtils.DBUtil2;
import org.springframework.base.system.entity.DmsDto;
import org.springframework.base.system.entity.IdsDto;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.service.SQLParserService;
import org.springframework.base.system.utils.DataUtil;
import org.springframework.base.system.utils.DateUtils;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.base.system.utils.NetworkUtil;
import org.springframework.base.system.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/dao/OracleDao.class */
public class OracleDao {
    @Autowired
    private ConfigDao configDao;
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private DataSynchronizeDao dataSynchronizeDao;
    @Autowired
    private LogDao logDao;
    @Autowired
    private SQLParserService sqlParserService;

    public List<Map<String, Object>> getAllDataBaseForOracle(String databaseConfigId) throws Exception {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map0 = this.configDao.getConfigById(databaseConfigId);
        String databaseName = (String) map0.get("databaseName");
        Map<String, Object> map = new HashMap<>();
        map.put("SCHEMA_NAME", databaseName);
        list.add(map);
        return list;
    }

    public List<Map<String, Object>> getAllTablesForOracle(String dbName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(" select TABLE_NAME  from  user_tables  order by TABLE_NAME ");
        return list;
    }

    public List<Map<String, Object>> getTableColumns3ForOracle(String dbName, String tableName, String databaseConfigId) throws Exception {
        String sql = "select t1.column_name as TREESOFTPRIMARYKEY, t1.COLUMN_NAME,  nvl2( t1.CHAR_COL_DECL_LENGTH,  t1.DATA_TYPE||'(' ||CHAR_COL_DECL_LENGTH||')',t1.DATA_TYPE ) as COLUMN_TYPE ,t1.data_type,   CASE t1.DATA_TYPE     when 'NUMBER' then t1.DATA_PRECISION    when 'LONG' then null     ELSE t1.data_length  END as CHARACTER_MAXIMUM_LENGTH, t1.DATA_SCALE as NUMERIC_SCALE ,CASE t1.nullable when 'Y' then 'YES' END as IS_NULLABLE  ,  nvl2(t3.column_name ,'PRI' ,'')  as COLUMN_KEY,  t2.comments as COLUMN_COMMENT  from user_tab_columns  t1   left join user_col_comments t2  on  t1.table_name = t2.table_name and t1.COLUMN_NAME = t2.COLUMN_NAME   left join   (select a.table_name, a.column_name   from user_cons_columns a, user_constraints b    where a.constraint_name = b.constraint_name    and b.constraint_type = 'P' ) t3    on t1.TABLE_NAME = t3.table_name  and t1.COLUMN_NAME = t3.COLUMN_NAME    where   t1.table_name= '" + tableName + "' order by t1.COLUMN_ID ";
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        return list;
    }

    public List<Map<String, Object>> getAllViewsForOracle(String dbName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(" select view_name as TABLE_NAME from  user_views  order by view_name ");
        return list;
    }

    public String getViewSqlForOracle(String databaseName, String tableName, String databaseConfigId) throws Exception {
        String sql = " select TEXT from all_views where view_name = '" + tableName + "'";
        String str = "";
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        if (list.size() == 1) {
            Map<String, Object> map = list.get(0);
            str = "create view " + tableName + " as " + ((String) map.get("TEXT")) + ";";
        }
        return str;
    }

    public String getFunctionSqlForOracle(String databaseName, String tableName, String databaseConfigId) throws Exception {
        String sql = " select TEXT from all_source where type='FUNCTION' and  name = '" + tableName + "'";
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        String defineSQL = "create or replace ";
        for (int i = 0; i < list.size(); i++) {
            defineSQL = String.valueOf(defineSQL) + list.get(i).get("TEXT").toString();
        }
        return defineSQL;
    }

    public String getProcedureSqlForOracle(String databaseName, String tableName, String databaseConfigId) throws Exception {
        String sql = " select TEXT from all_source where type='PROCEDURE' and  name ='" + tableName + "'";
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        String defineSQL = "create or replace ";
        for (int i = 0; i < list.size(); i++) {
            defineSQL = String.valueOf(defineSQL) + list.get(i).get("TEXT").toString();
        }
        return defineSQL;
    }

    public String getPackageSqlForOracle(String databaseName, String tableName, String databaseConfigId) throws Exception {
        String sql = " select TEXT from all_source where type='PACKAGE' and  name ='" + tableName + "'";
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        String defineSQL = "create or replace ";
        for (int i = 0; i < list.size(); i++) {
            defineSQL = String.valueOf(defineSQL) + list.get(i).get("TEXT").toString();
        }
        return defineSQL;
    }

    public String getPackageBodySqlForOracle(String databaseName, String tableName, String databaseConfigId) throws Exception {
        String sql = " select TEXT from all_source where type='PACKAGE BODY' and  name ='" + tableName + "'";
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        String defineSQL = "create or replace ";
        for (int i = 0; i < list.size(); i++) {
            defineSQL = String.valueOf(defineSQL) + list.get(i).get("TEXT").toString();
        }
        return defineSQL;
    }

    public List<Map<String, Object>> getAllFuntionForOracle(String dbName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(" select object_name as ROUTINE_NAME from  user_objects where object_type='FUNCTION' order by object_name  ");
        return list;
    }

    public List<Map<String, Object>> getAllProcedureForOracle(String dbName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(" select object_name as PROC_NAME from  user_objects where object_type='PROCEDURE'  order by object_name ");
        return list;
    }

    public List<Map<String, Object>> getAllPackageForOracle(String dbName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(" select object_name as PACKAGE_NAME from  user_objects where object_type='PACKAGE'  order by object_name ");
        return list;
    }

    public List<Map<String, Object>> getAllPackageBobyForOracle(String dbName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(" select object_name as PACKAGEBODY_NAME from  user_objects where object_type='PACKAGE BODY'  order by  object_name ");
        return list;
    }

    public List<Map<String, Object>> getAllSynonymsForOracle(String dbName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(" select object_name as SYNONYMS_NAME from  user_objects where object_type='SYNONYM'  order by object_name ");
        return list;
    }

    public List<Map<String, Object>> getSequenceForOracle(String dbName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(" select object_name as SEQUENCE_NAME from  user_objects where object_type='SEQUENCE'  order by object_name ");
        return list;
    }

    public List<Map<String, Object>> getIndexForOracle(String dbName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(" select object_name as INDEX_NAME from  user_objects where object_type='INDEX'  order by object_name ");
        return list;
    }

    public List<Map<String, Object>> getDBLinkForOracle(String dbName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(" select object_name as DBLINK_NAME from  user_objects where object_type='DATABASE LINK'  order by object_name ");
        return list;
    }

    public List<Map<String, Object>> getUserForOracle(String dbName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(" select USERNAME as USER_NAME from all_users   order by USERNAME ");
        return list;
    }

    public Integer getTableRowsForOracle(String dbName, String tableName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        String sql = " select count(*) as NUM from  " + tableName;
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        Map<String, Object> map = list.get(0);
        Integer rowCount = Integer.valueOf(Integer.parseInt(map.get("NUM").toString()));
        return rowCount;
    }

    public Page<Map<String, Object>> getDataForOracle(Page<Map<String, Object>> page, String tableName, String dbName, String databaseConfigId) throws Exception {
        Iterator<Map<String, Object>> it;
        String sql2;
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        int i = limitFrom + pageSize;
        String orderBy = page.getOrderBy();
        String order = page.getOrder();
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        List<Map<String, Object>> list3 = getPrimaryKeyssForOracle(dbName, tableName, databaseConfigId);
        String tem = "";
        while (list3.iterator().hasNext()) {
            tem = String.valueOf(tem) + it.next().get("COLUMN_NAME") + ",";
        }
        String primaryKey = "";
        if (!tem.equals("")) {
            primaryKey = tem.substring(0, tem.length() - 1);
        }
        String str = "select * from  " + tableName;
        if (orderBy == null || orderBy.equals("")) {
            sql2 = " select * from " + tableName;
        } else {
            sql2 = " select * from " + tableName + " order by " + orderBy + " " + order;
        }
        List<Map<String, Object>> list = db.queryForListForOracle(sql2, limitFrom, pageSize);
        int rowCount = getTableRowsForOracle(dbName, tableName, databaseConfigId).intValue();
        List<Map<String, Object>> columns = getTableColumnsForOracle(dbName, tableName, databaseConfigId);
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
            if (map.get("data_type").equals("DATETIME") || map.get("data_type").equals("DATE") || map.get("data_type").equals("TIMESTAMP")) {
                map2.put("editor", "datetimebox");
            } else if (map.get("data_type").equals("INT") || map.get("data_type").equals("SMALLINT") || map.get("data_type").equals("TINYINT")) {
                map2.put("editor", "numberbox");
            } else if (map.get("data_type").equals("DOUBLE")) {
                map2.put("editor", "numberbox");
            } else {
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

    public List<Map<String, Object>> getPrimaryKeyssForOracle(String databaseName, String tableName, String databaseConfigId) throws Exception {
        if (StringUtils.isEmpty(databaseName) || StringUtils.isEmpty(tableName)) {
            return new ArrayList();
        }
        String sql = "  select  COLUMN_NAME   from   user_cons_columns  where   constraint_name= (select  constraint_name  from user_constraints  where table_name = '" + tableName.toUpperCase() + "' and constraint_type ='P') ";
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        return list;
    }

    public List<Map<String, Object>> getTableColumnsForOracle(String dbName, String tableName, String databaseConfigId) throws Exception {
        String sql = "select  * from   " + tableName + " where rownum =1 ";
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForColumnOnly(sql);
        return list;
    }

    public Page<Map<String, Object>> executeSqlHaveResForOracle(Page<Map<String, Object>> page, String sql, String dbName, String databaseConfigId) throws Exception {
        int rowCount;
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        Map<String, Object> map0 = this.configDao.getConfigById(databaseConfigId);
        String isRead = (String) map0.get("isRead");
        Date b1 = new Date();
        String orderBy = page.getOrderBy();
        String order = page.getOrder();
        if (!StringUtils.isEmpty(orderBy)) {
            sql = String.valueOf(sql) + "  order by " + orderBy + " " + order;
        }
        List<Map<String, Object>> list = db.queryForListForOracle(sql, limitFrom, pageSize);
        Date b2 = new Date();
        long executeTime = b2.getTime() - b1.getTime();
        if (list.size() < 20 && pageNo == 1) {
            rowCount = list.size();
        } else {
            rowCount = db.executeQueryForCountForOracle(sql);
        }
        List<Map<String, Object>> columns = executeSqlForColumnsForOracle(sql, dbName, databaseConfigId);
        List<Map<String, Object>> tempList = new ArrayList<>();
        Iterator<Map<String, Object>> it = columns.iterator();
        while (it.hasNext()) {
            Map<String, Object> map = it.next();
            Map<String, Object> map2 = new HashMap<>();
            map2.put("field", map.get("column_name"));
            map2.put("title", map.get("column_name"));
            map2.put("sortable", true);
            if (map.get("data_type").equals("DATETIME") || map.get("data_type").equals("DATE") || map.get("data_type").equals("TIMESTAMP(6)") || map.get("data_type").toString().indexOf("TIMESTAMP") == 0) {
                map2.put("editor", "datetimebox");
            } else if (map.get("data_type").equals("INTEGER") || map.get("data_type").equals("NUMBER") || map.get("data_type").equals("FLOAT") || map.get("data_type").equals("SMALLINT") || map.get("data_type").equals("TINYINT") || map.get("data_type").equals("DOUBLE")) {
                map2.put("editor", "numberbox");
            } else if (map.get("data_type").equals("BLOB") || map.get("data_type").equals("CLOB")) {
                map2.put("editor", null);
            } else if (map.get("data_type").equals("VARBINARY") || map.get("data_type").equals("BINARY") || map.get("data_type").equals("MEDIUMBLOB") || map.get("data_type").equals("LONGBLOB")) {
                map2.put("editor", null);
            } else {
                map2.put("editor", "text");
            }
            tempList.add(map2);
        }
        String tableName = this.sqlParserService.parserTableNames(sql, "Oracle");
        String primaryKey = "";
        List<Map<String, Object>> primaryKeyList = getPrimaryKeyssForOracle(dbName, tableName, databaseConfigId);
        String tem = "";
        Iterator<Map<String, Object>> it2 = primaryKeyList.iterator();
        while (it2.hasNext()) {
            Map<String, Object> map3 = it2.next();
            tem = String.valueOf(tem) + map3.get("COLUMN_NAME") + ",";
        }
        if (!StringUtils.isEmpty(tem)) {
            primaryKey = tem.substring(0, tem.length() - 1);
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

    public List<Map<String, Object>> executeSqlForColumnsForOracle(String sql, String dbName, String databaseConfigId) throws Exception {
        String sql2 = " select * from (" + sql + ") where  rownum = 1 ";
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.executeSqlForColumns(sql2);
        return list;
    }

    public int updateTableNullAbleForOracle(String databaseName, String tableName, String column_name, String is_nullable, String databaseConfigId) throws Exception {
        String sql4;
        if (column_name != null && !column_name.equals("")) {
            DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
            if (is_nullable.equals("true")) {
                sql4 = " alter table  " + tableName + " modify   " + column_name + "  null ";
            } else {
                sql4 = " alter table  " + tableName + " modify   " + column_name + "  not null ";
            }
            db.setupdateData(sql4);
            return 0;
        }
        return 0;
    }

    public int savePrimaryKeyForOracle(String databaseName, String tableName, String column_name, String isSetting, String databaseConfigId) throws Exception {
        if (column_name != null && !column_name.equals("")) {
            DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
            List<Map<String, Object>> list2 = selectTablePrimaryKeyForOracle(databaseName, tableName, databaseConfigId);
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

    public List<Map<String, Object>> selectTablePrimaryKeyForOracle(String databaseName, String tableName, String databaseConfigId) throws Exception {
        String sql = " select a.CONSTRAINT_NAME,  a.COLUMN_NAME  from user_cons_columns a, user_constraints b  where a.constraint_name = b.constraint_name   and b.constraint_type = 'P'  and a.table_name = '" + tableName + "' ";
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        return list;
    }

    public int saveDesginColumnForOracle(Map<String, String> map, String databaseName, String tableName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        String data_type = map.get("DATA_TYPE");
        String character_maximum_length = new StringBuilder(String.valueOf(map.get("CHARACTER_MAXIMUM_LENGTH"))).toString();
        String numeric_scale = new StringBuilder(String.valueOf(map.get("NUMERIC_SCALE"))).toString();
        if (StringUtils.isEmpty(numeric_scale)) {
            numeric_scale = "0";
        }
        String sql = String.valueOf(String.valueOf(" alter table " + tableName + " add  ") + map.get("COLUMN_NAME") + "  ") + map.get("DATA_TYPE");
        if (map.get("CHARACTER_MAXIMUM_LENGTH") != null && !map.get("CHARACTER_MAXIMUM_LENGTH").equals("")) {
            sql = (data_type.equals("NUMBER") || data_type.equals("number")) ? String.valueOf(sql) + " (" + character_maximum_length + "," + numeric_scale + ") " : String.valueOf(sql) + " (" + character_maximum_length + ") ";
        }
        if (map.get("COLUMN_COMMENT") != null && !map.get("COLUMN_COMMENT").equals("")) {
            sql = String.valueOf(sql) + " comment '" + map.get("COLUMN_COMMENT") + "'";
        }
        int y = db.setupdateData(sql);
        return y;
    }

    public int updateTableColumnForOracle(Map<String, Object> map, String databaseName, String tableName, String columnName, String idValues, String databaseConfigId) throws Exception {
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
        String NUMERIC_SCALE = new StringBuilder().append(map.get("NUMERIC_SCALE")).toString();
        if (StringUtils.isEmpty(NUMERIC_SCALE)) {
            NUMERIC_SCALE = "0";
        }
        if (!old_field_name.endsWith(column_name)) {
            String sql = " ALTER TABLE " + tableName + " RENAME COLUMN " + old_field_name + " to  " + column_name;
            db.setupdateData(sql);
        }
        String sql2 = " alter table  " + tableName + " modify  " + column_name + " " + data_type;
        if (character_maximum_length != null && !character_maximum_length.equals("")) {
            if (data_type.equals("NUMBER") || data_type.equals("number")) {
                sql2 = String.valueOf(sql2) + " (" + character_maximum_length + "," + NUMERIC_SCALE + " )";
            } else if (!data_type.equals("date") && !data_type.equals("timestamp")) {
                sql2 = String.valueOf(sql2) + " (" + character_maximum_length + ")";
            }
        }
        int y = db.setupdateData(sql2);
        if (column_comment != null && !column_comment.equals("")) {
            String sql4 = "  comment on column " + tableName + "." + column_name + " is '" + column_comment + "' ";
            db.setupdateData(sql4);
        }
        return y;
    }

    public int deleteTableColumnForOracle(String databaseName, String tableName, String[] ids, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        int y = 0;
        for (int i = 0; i < ids.length; i++) {
            String sql = " alter table   " + tableName + " drop (" + ids[i] + ")";
            y += db.setupdateData(sql);
        }
        return y;
    }

    public int saveRowsForOracle(Map<String, String> map, String databaseName, String tableName, String databaseConfigId, String username, String ip) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        String sql = " insert into  " + tableName;
        String colums = " ";
        String values = " ";
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            if (!entry.getKey().equals("RN")) {
                colums = String.valueOf(colums) + entry.getKey() + ",";
                String columnType = selectColumnTypeForOracle(databaseName, tableName, entry.getKey(), databaseConfigId);
                if (StringUtils.isEmpty(entry.getValue())) {
                    values = String.valueOf(values) + " null ,";
                } else if (columnType.equals("DATE")) {
                    values = String.valueOf(values) + " to_date('" + entry.getValue() + "' ,'yyyy-mm-dd hh24:mi:ss') ,";
                } else if (columnType.indexOf("TIMESTAMP") >= 0) {
                    values = String.valueOf(values) + " to_date('" + entry.getValue() + "' ,'yyyy-mm-dd hh24:mi:ss') ,";
                } else if (columnType.equals("NUMBER")) {
                    values = String.valueOf(values) + String.valueOf(entry.getValue()) + ",";
                } else if (columnType.equals("INTEGER")) {
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
        }
        String sql2 = String.valueOf(sql) + " (" + colums.substring(0, colums.length() - 1) + ") values (" + values.substring(0, values.length() - 1) + ")";
        this.logDao.saveLog(sql2, username, ip, databaseName, databaseConfigId);
        int y = db.setupdateData(sql2);
        return y;
    }

    public String selectColumnTypeForOracle(String databaseName, String tableName, String column, String databaseConfigId) throws Exception {
        String sql = " select DATA_TYPE from user_tab_columns where table_name ='" + tableName + "' AND COLUMN_NAME ='" + column + "' ";
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        return (String) list.get(0).get("DATA_TYPE");
    }

    public int deleteRowsNewForOracle(String databaseName, String tableName, String primary_key, List<String> condition, String databaseConfigId, String username, String ip) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        int y = 0;
        for (int i = 0; i < condition.size(); i++) {
            String whereStr = condition.get(i);
            String sql = " delete from  " + tableName + " where  1=1 " + whereStr + " and rownum=1 ";
            LogUtil.i(String.valueOf(DateUtils.getDateTime()) + ",删除数据SQL =" + sql);
            this.logDao.saveLog(sql, username, ip, databaseName, databaseConfigId);
            y += db.setupdateData(sql);
        }
        return y;
    }

    public List<Map<String, Object>> selectAllDataFromSQLForOracle(String databaseName, String databaseConfigId, String sql, int limitFrom, int pageSize) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListForOracleForExport(sql, limitFrom, pageSize);
        return list;
    }

    public boolean backupDatabaseExecuteForOracle(String databaseName, String tableName, String path, String databaseConfigId) throws Exception {
        BackDbForOracle bdo = new BackDbForOracle();
        bdo.readDataToFile(databaseName, tableName, path, databaseConfigId);
        return true;
    }

    public boolean copyTableForOracle(String databaseName, String tableName, String databaseConfigId) throws Exception {
        String sql4 = "create table " + tableName + "_" + DateUtils.getDateTimeString(new Date()) + " as select * from " + tableName;
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        db.setupdateData(sql4);
        return true;
    }

    public boolean renameTableForOracle(String databaseName, String tableName, String databaseConfigId, String newTableName) throws Exception {
        String sql4 = " alter table " + tableName + " rename to  " + newTableName;
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        db.setupdateData(sql4);
        return true;
    }

    public List<Map<String, Object>> exportDataToSQLForOracle(String databaseName, String tableName, List<String> condition, String databaseConfigId) throws Exception {
        String sql = " select * from  " + tableName + " where   1=1  ";
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < condition.size(); i++) {
            List<Map<String, Object>> list2 = db.queryForListForOracleForExport(String.valueOf(sql) + condition.get(i), 0, 999999999);
            list.add(list2.get(0));
        }
        return list;
    }

    public boolean dropTableForOracle(String databaseName, String tableName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        String sql = " drop  table " + tableName;
        db.setupdateData(sql);
        LogUtil.i(String.valueOf(DateUtils.getDateTime()) + ",删除Oracle表,SQL =" + sql);
        return true;
    }

    public List<Map<String, Object>> viewTableMessForOracle(String dbName, String tableName, String databaseConfigId) throws Exception {
        String sql = "  select *  from user_tables   where table_name ='" + tableName + "' ";
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        return list;
    }

    public int saveNewTableForOracle(JSONArray insertArray, String databaseName, String tableName, String databaseConfigId) throws Exception {
        String sql;
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        String sql2 = " create table " + tableName + " (  ";
        String PRIMARY_KEY = "";
        List<String> commentStr = new ArrayList<>();
        for (int i = 0; i < insertArray.size(); i++) {
            Map<String, Object> map1 = (Map) insertArray.get(i);
            sql2 = String.valueOf(String.valueOf(sql2) + map1.get("COLUMN_NAME") + " ") + map1.get("DATA_TYPE") + " ";
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
                commentStr.add(" COMMENT ON COLUMN  " + tableName + "." + map1.get("COLUMN_NAME") + " IS  '" + map1.get("COLUMN_COMMENT") + "'");
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
        for (int i2 = 0; i2 < commentStr.size(); i2++) {
            db.setupdateData(commentStr.get(i2));
        }
        return y;
    }

    public String queryDatabaseStatusForOracleHitRadio(String databaseName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod("select  floor(sum(pinhits)/sum(pins)*100 ) AS HIT_RADIO from v$librarycache");
        String value = "0";
        if (list.size() > 0) {
            Map<String, Object> temp = list.get(0);
            value = new StringBuilder().append(temp.get("HIT_RADIO")).toString();
        }
        return value;
    }

    public String queryDatabaseStatusForOracleLogBuffer(String databaseName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod("select floor((select value  from v$sysstat where name in( 'redo buffer allocation retries'))/ (select value  from v$sysstat where name in('redo entries' ) )) as LOG_BUFFER from dual");
        String value = "0";
        if (list.size() > 0) {
            Map<String, Object> temp = list.get(0);
            value = new StringBuilder().append(temp.get("LOG_BUFFER")).toString();
        }
        return value;
    }

    public String queryDatabaseStatusForOracleLock(String databaseName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod("select count(*) as LOCKS from v$locked_object ");
        String value = "0";
        if (list.size() > 0) {
            Map<String, Object> temp = list.get(0);
            value = new StringBuilder().append(temp.get("LOCKS")).toString();
        }
        return value;
    }

    public String queryDatabaseStatusForOraclePhyrds(String databaseName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod("select sum(f.phyrds) PHYRDS from v$filestat f, dba_data_files df where f.file# = df.file_id ");
        String value = "0";
        if (list.size() > 0) {
            Map<String, Object> temp = list.get(0);
            value = new StringBuilder().append(temp.get("PHYRDS")).toString();
        }
        return value;
    }

    public String queryDatabaseStatusForOraclePhywrts(String databaseName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod("select sum(f.phywrts) PHYWRTS from v$filestat f, dba_data_files df where f.file# = df.file_id ");
        String value = "0";
        if (list.size() > 0) {
            Map<String, Object> temp = list.get(0);
            value = new StringBuilder().append(temp.get("PHYWRTS")).toString();
        }
        return value;
    }

    public List<Map<String, Object>> queryTableSpaceForOracle(String databaseName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(" SELECT a.tablespace_name TABLESPACE_NAME ,ROUND( free / (1024 * 1024 ),2) TABLESPACE_SIZE_FREE,ROUND( total / (1024 * 1024 ),2) TABLESPACE_SIZE,   ROUND( (total - free) / (1024 * 1024  ),2) TABLESPACE_SIZE_USED FROM (SELECT tablespace_name, SUM(bytes) free FROM dba_free_space GROUP BY tablespace_name) a, (SELECT tablespace_name, SUM(bytes) total FROM dba_data_files GROUP BY tablespace_name) b WHERE a.tablespace_name = b.tablespace_name  ");
        return list;
    }

    public boolean judgeTableExistsForOracle(Map<String, Object> jobMessageMap, String sourceDbType, String targetDbType) throws Exception {
        String sourceSQL;
        String str;
        String sourceConfigId = new StringBuilder().append(jobMessageMap.get("sourceConfigId")).toString();
        String sourceDataBase = new StringBuilder().append(jobMessageMap.get("sourceDataBase")).toString();
        String doSql = new StringBuilder().append(jobMessageMap.get("doSql")).toString();
        String targetConfigId = new StringBuilder().append(jobMessageMap.get("targetConfigId")).toString();
        String targetDataBase = new StringBuilder().append(jobMessageMap.get("targetDataBase")).toString();
        String targetTable = new StringBuilder().append(jobMessageMap.get("targetTable")).toString();
        String createSQL = "create table " + targetTable + " (";
        if (sourceDbType.equals("MySQL") || sourceDbType.equals("MariaDB") || sourceDbType.equals("MySQL8.0") || sourceDbType.equals("TiDB")) {
            sourceSQL = " select * from ( " + doSql + " ) tab  limit 1";
        } else if (sourceDbType.equals("Oracle")) {
            sourceSQL = " SELECT TAB.*, ROWNUM  FROM (" + doSql + " ) TAB  WHERE ROWNUM <= 1 ";
        } else if (sourceDbType.equals("SQL Server")) {
            sourceSQL = " SELECT top 1 *  FROM (" + doSql + " ) TAB ";
        } else if (sourceDbType.equals("Cache") || sourceDbType.equals("Sybase")) {
            sourceSQL = " SELECT top 1 *  FROM (" + doSql + " ) TAB ";
        } else {
            sourceSQL = " select * from ( " + doSql + " ) tab  limit 1";
        }
        DBUtil2 sourceDB = new DBUtil2(sourceDataBase, sourceConfigId);
        List<Map<String, Object>> sourceList = sourceDB.queryForListCommonMethod(sourceSQL);
        if (sourceList.size() == 0) {
            return false;
        }
        Map<String, Object> sourceMap = sourceList.get(0);
        if (sourceDbType.equals("Oracle")) {
            sourceMap.remove("ROWNUM");
        }
        DBUtil2 targetDB = new DBUtil2(targetDataBase, targetConfigId);
        targetDB.getConnection();
        try {
            String targetSQL = " select * from " + targetTable + " where 1=2 ";
            targetDB.queryForListForMySql(targetSQL);
            return true;
        } catch (Exception e) {
            LogUtil.e("数据同步时，目标库不存在表" + targetTable + ", 系统将直接新建该表，如果字段类型及长度有误，请及时人工修正。", e);
            if (0 == 0) {
                Iterator<Map.Entry<String, Object>> it = sourceMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, Object> entry = it.next();
                    String columsName = new StringBuilder(String.valueOf(entry.getKey())).toString();
                    if (!sourceDbType.equals("Oracle") || !columsName.equals("ROWNUM")) {
                        if (entry.getValue() instanceof java.sql.Date) {
                            str = " date ,";
                        } else if ((entry.getValue() instanceof Time) || (entry.getValue() instanceof Timestamp)) {
                            str = " timestamp(6) ,";
                        } else if ((entry.getValue() instanceof Integer) || (entry.getValue() instanceof BigInteger)) {
                            str = " int ,";
                        } else if (entry.getValue() instanceof BigDecimal) {
                            str = " decimal(10,2) , ";
                        } else if (entry.getValue() instanceof Float) {
                            str = " float ,";
                        } else if ((entry.getValue() instanceof Long) || (entry.getValue() instanceof Double)) {
                            str = " number ,";
                        } else if (entry.getValue() instanceof Boolean) {
                            str = " tinyint ,";
                        } else if (entry.getValue() instanceof Byte) {
                            str = " Blob ,";
                        } else {
                            str = " varchar2(255) ,";
                        }
                        String columsType = str;
                        createSQL = String.valueOf(createSQL) + columsName + columsType;
                    }
                }
                String dataSynchronizeId = new StringBuilder().append(jobMessageMap.get("id")).toString();
                targetDB.setupdateData(String.valueOf(createSQL.substring(0, createSQL.length() - 1)) + " )");
                this.dataSynchronizeDao.dataSynchronizeLogSave("1", "数据同步时，目标库不存在表" + targetTable + ", 系统已自动新建该表，字段类型、数值精度可能与实际不符，请及时人工修正。", dataSynchronizeId, "", "");
                return true;
            }
            return true;
        }
    }

    public boolean insertByDataListForOracle(List<Map<String, Object>> dataList, String databaseName, String tableName, String databaseConfigId) throws Exception {
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
                } else if (entry.getValue() instanceof java.sql.Date) {
                    values = String.valueOf(values) + "to_date( '" + entry.getValue() + "','YYYY-MM-DD'),";
                } else if (this.permissionDao.isDateTime(new StringBuilder().append(entry.getValue()).toString())) {
                    values = String.valueOf(values) + "to_date( '" + entry.getValue() + "','YYYY-MM-DD HH24:MI:SS'),";
                } else if (entry.getValue() instanceof Boolean) {
                    if (((Boolean) entry.getValue()).booleanValue()) {
                        values = String.valueOf(values) + "1,";
                    } else {
                        values = String.valueOf(values) + "0,";
                    }
                } else if ((entry.getValue() instanceof Integer) || (entry.getValue() instanceof Float) || (entry.getValue() instanceof Long) || (entry.getValue() instanceof BigInteger) || (entry.getValue() instanceof Double) || (entry.getValue() instanceof BigDecimal) || (entry.getValue() instanceof Short)) {
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
                    values = String.valueOf(values) + "'" + tempValues.replace("'", "''").replace("\r\n", "\\r\\n").replace("\n\r", "\\n\\r").replace("\r", "\\r").replace("\n", "\\n") + "',";
                }
            }
            sqlList.add(String.valueOf(insertSQL) + " ( " + colums.substring(0, colums.length() - 1) + ") VALUES (" + values.substring(0, values.length() - 1) + " ) ");
        }
        db.updateExecuteBatch(sqlList);
        return true;
    }

    public boolean insertOrUpdateByDataListForOracle(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
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
                insertByDataListForOracle(insertDataList, databaseName, table, databaseConfigId);
            }
            if (updateDataList.size() > 0) {
                updateByDataListForOracle(updateDataList, databaseName, table, databaseConfigId, qualification);
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

    public boolean insertOnlyByDataListForOracle(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
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
                        sql = String.valueOf(sql) + " and " + ss + "='" + value.replaceAll("'", "\\\\'") + "' ";
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
                insertByDataListForOracle(insertDataList, databaseName, table, databaseConfigId);
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

    public String createTableSQLForOracle(String tableName, String databaseName, String databaseConfigId) throws Exception {
        new DBUtil2(databaseName, databaseConfigId);
        StringBuffer sb2 = new StringBuffer();
        sb2.append("CREATE TABLE " + tableName + " ( \r\n");
        String primary_key_list = "";
        List<Map<String, Object>> listTableColumn = getTableColumns3ForOracle(databaseName, tableName, databaseConfigId);
        Iterator<Map<String, Object>> it = listTableColumn.iterator();
        while (it.hasNext()) {
            Map<String, Object> map3 = it.next();
            String data_type_temp = new StringBuilder().append(map3.get("DATA_TYPE")).toString();
            new StringBuilder().append(map3.get("COLUMN_TYPE")).toString();
            String numeric_scale = new StringBuilder().append(map3.get("NUMERIC_SCALE")).toString();
            if (StringUtils.isEmpty(numeric_scale)) {
                numeric_scale = "0";
            }
            if (data_type_temp.equals("NUMBER") || data_type_temp.equals("LONG")) {
                if (StringUtils.isEmpty(map3.get("CHARACTER_MAXIMUM_LENGTH"))) {
                    sb2.append(" " + map3.get("COLUMN_NAME") + " " + data_type_temp);
                } else {
                    sb2.append(" " + map3.get("COLUMN_NAME") + " " + map3.get("DATA_TYPE") + "(" + map3.get("CHARACTER_MAXIMUM_LENGTH") + "," + numeric_scale + ")");
                }
            } else {
                sb2.append(" " + map3.get("COLUMN_NAME") + " " + map3.get("COLUMN_TYPE"));
            }
            if (map3.get("COLUMN_KEY") != null && map3.get("COLUMN_KEY").equals("PRI")) {
                primary_key_list = String.valueOf(primary_key_list) + map3.get("COLUMN_NAME") + ",";
            }
            if (map3.get("IS_NULLABLE") != null) {
                sb2.append(" NOT NULL ");
            }
            sb2.append(",\r\n");
        }
        if ("".equals(primary_key_list)) {
            sb2.delete(sb2.length() - 3, sb2.length() - 1);
            sb2.append("  \r\n");
        } else {
            sb2.append(" PRIMARY KEY (" + primary_key_list.substring(0, primary_key_list.length() - 1) + " )  \r\n");
        }
        sb2.append("); \r\n ");
        return sb2.toString();
    }

    public boolean deleteByDataListForOracle(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
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

    public boolean updateByDataListForOracle(List<Map<String, Object>> dataList, String databaseName, String tableName, String databaseConfigId, String qualification) throws Exception {
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
                    } else if (entry.getValue() instanceof java.sql.Date) {
                        updateSQL = String.valueOf(updateSQL2) + "to_date( '" + entry.getValue() + "','YYYY-MM-DD'),";
                    } else if (this.permissionDao.isDateTime(new StringBuilder().append(entry.getValue()).toString())) {
                        updateSQL = String.valueOf(updateSQL2) + "to_date( '" + entry.getValue() + "','YYYY-MM-DD HH24:MI:SS'),";
                    } else if (entry.getValue() instanceof Boolean) {
                        if (((Boolean) entry.getValue()).booleanValue()) {
                            updateSQL = String.valueOf(updateSQL2) + "1,";
                        } else {
                            updateSQL = String.valueOf(updateSQL2) + "0,";
                        }
                    } else if ((entry.getValue() instanceof Integer) || (entry.getValue() instanceof Float) || (entry.getValue() instanceof Long) || (entry.getValue() instanceof BigInteger) || (entry.getValue() instanceof Double) || (entry.getValue() instanceof BigDecimal) || (entry.getValue() instanceof Short)) {
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

    public List<Map<String, Object>> monitorItemValueForOracle(String databaseName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list2 = new ArrayList<>();
        List<Map<String, Object>> list = db.queryForListCommonMethod("  select NAME \"Variable_name\", VALUE \"Value\"  from v$sysstat ");
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> tempMap = list.get(i);
            new StringBuilder().append(tempMap.get("Variable_name")).toString();
            new StringBuilder().append(tempMap.get("Value")).toString();
            list2.add(tempMap);
        }
        return list2;
    }

    public String queryDatabaseStatusForOracleHitRatio(String databaseName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod("select floor(( 1 - sum(decode(name, 'physical reads', value, 0)) /(sum(decode(name, 'db block gets', value, 0)) + sum(decode(name, 'consistent gets', value, 0)))) *100)  HIT_RATIO from v$sysstat t where name in ('physical reads', 'db block gets', 'consistent gets')");
        String value = "0";
        if (list.size() > 0) {
            Map<String, Object> temp = list.get(0);
            value = new StringBuilder().append(temp.get("HIT_RATIO")).toString();
        }
        return value;
    }

    public Integer queryDatabaseStatusForOracleSession(String databaseName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod("select count(*) as SESSIONS from  v$session");
        String value = "0";
        if (list.size() > 0) {
            Map<String, Object> temp = list.get(0);
            value = new StringBuilder().append(temp.get("SESSIONS")).toString();
        }
        return Integer.valueOf(Integer.parseInt(value));
    }

    public Map<String, Object> queryDatabaseStatusForOracle(String databaseName, String databaseConfigId) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("SESSIONS", queryDatabaseStatusForOracleSession(databaseName, databaseConfigId));
        map.put("HIT_RATIO", queryDatabaseStatusForOracleHitRatio(databaseName, databaseConfigId));
        map.put("HIT_RADIO", queryDatabaseStatusForOracleHitRadio(databaseName, databaseConfigId));
        map.put("LOG_BUFFER", queryDatabaseStatusForOracleLogBuffer(databaseName, databaseConfigId));
        map.put("LOCK", queryDatabaseStatusForOracleLock(databaseName, databaseConfigId));
        map.put("PHYRDS", queryDatabaseStatusForOraclePhyrds(databaseName, databaseConfigId));
        map.put("PHYWRTS", queryDatabaseStatusForOraclePhywrts(databaseName, databaseConfigId));
        return map;
    }

    public List<Map<String, Object>> queryExplainSQLForOracle(DmsDto dto) throws Exception {
        String sql;
        DBUtil2 db = new DBUtil2(dto.getDatabaseName(), dto.getDatabaseConfigId());
        String sql2 = dto.getSql();
        String sql22 = "";
        if (sql2.toLowerCase().indexOf("explain") == 0) {
            sql = dto.getSql();
        } else {
            sql = " explain plan for " + dto.getSql();
            sql22 = "select * from table(dbms_xplan.display()) ";
        }
        db.setupdateData(sql);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql22);
        return list;
    }

    public Page<Map<String, Object>> selectTableIndexsForOracle(Page<Map<String, Object>> page, DmsDto dto) throws Exception {
        DBUtil2 db1 = new DBUtil2(dto.getDatabaseName(), dto.getDatabaseConfigId());
        String sql1 = " SELECT * FROM  USER_INDEXES  WHERE TABLE_NAME = '" + dto.getTableName() + "'";
        List<Map<String, Object>> list = db1.queryForListCommonMethod(sql1);
        Iterator<Map<String, Object>> it = list.iterator();
        while (it.hasNext()) {
            Map<String, Object> map = it.next();
            String tableName = new StringBuilder().append(map.get("TABLE_NAME")).toString();
            String indexName = new StringBuilder().append(map.get("INDEX_NAME")).toString();
            String sql2 = " SELECT COLUMN_NAME FROM  USER_IND_COLUMNS  WHERE TABLE_NAME = '" + tableName + "' AND INDEX_NAME='" + indexName + "'";
            List<Map<String, Object>> columnList = db1.queryForListCommonMethod(sql2);
            String columns = "";
            Iterator<Map<String, Object>> it2 = columnList.iterator();
            while (it2.hasNext()) {
                Map<String, Object> mapIndex = it2.next();
                columns = String.valueOf(columns) + mapIndex.get("COLUMN_NAME") + ",";
            }
            map.put("columnName", columns.substring(0, columns.length() - 1));
            map.put("indexName", indexName);
        }
        page.setTotalCount(list.size());
        page.setResult(list);
        return page;
    }

    public boolean indexSaveForOracle(HttpServletRequest request, IdsDto dto, String username) throws Exception {
        DBUtil2 db1 = new DBUtil2(dto.getDatabaseName(), dto.getDatabaseConfigId());
        String ip = NetworkUtil.getIpAddress(request);
        String sql = " create index  " + dto.getIndexName() + " on " + dto.getTableName() + "(" + dto.getColumn_name() + ")";
        db1.setupdateData(sql);
        this.logDao.saveLog("保存索引 " + sql, username, ip, dto.getDatabaseName(), dto.getDatabaseConfigId());
        return true;
    }

    public boolean indexDeleteForOracle(HttpServletRequest request, IdsDto dto, String username) throws Exception {
        DBUtil2 db1 = new DBUtil2(dto.getDatabaseName(), dto.getDatabaseConfigId());
        String ip = NetworkUtil.getIpAddress(request);
        String sql = " drop index  " + dto.getIndexName();
        db1.setupdateData(sql);
        this.logDao.saveLog("删除索引 " + sql, username, ip, dto.getDatabaseName(), dto.getDatabaseConfigId());
        return true;
    }

    public Page<Map<String, Object>> selectTableTriggersForOracle(Page<Map<String, Object>> page, DmsDto dto) throws Exception {
        DBUtil2 db1 = new DBUtil2(dto.getDatabaseName(), dto.getDatabaseConfigId());
        String sql1 = " select * from user_triggers where table_name ='" + dto.getTableName() + "' ";
        List<Map<String, Object>> list = db1.queryForListCommonMethod(sql1);
        page.setTotalCount(list.size());
        page.setResult(list);
        return page;
    }

    public boolean triggerDeleteForOracle(HttpServletRequest request, IdsDto dto, String username) throws Exception {
        DBUtil2 db1 = new DBUtil2(dto.getDatabaseName(), dto.getDatabaseConfigId());
        String ip = NetworkUtil.getIpAddress(request);
        String sql = " drop trigger " + dto.getTriggerName();
        db1.setupdateData(sql);
        this.logDao.saveLog("删除触发器 " + sql, username, ip, dto.getDatabaseName(), dto.getDatabaseConfigId());
        return true;
    }
}
