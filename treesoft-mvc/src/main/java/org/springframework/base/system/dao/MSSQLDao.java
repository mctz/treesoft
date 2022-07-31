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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/dao/MSSQLDao.class */
public class MSSQLDao {
    @Autowired
    private ConfigDao configDao;
    @Autowired
    private DataSynchronizeDao dataSynchronizeDao;
    @Autowired
    private LogDao logDao;
    @Autowired
    private SQLParserService sqlParserService;

    public List<Map<String, Object>> getAllDataBaseForMSSQL(String databaseConfigId) throws Exception {
        Map<String, Object> map12 = this.configDao.getConfigById(databaseConfigId);
        String databaseName = new StringBuilder().append(map12.get("databaseName")).toString();
        String isDefaultView = (String) map12.get("isDefaultView");
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
            list = db.queryForListCommonMethod(" SELECT name as SCHEMA_NAME FROM sys.databases where state='0' and name not in('master','msdb','model','tempdb') ORDER BY name  ");
        } catch (Exception e) {
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

    public List<Map<String, Object>> getAllTablesForMSSQL(String dbName, String databaseConfigId) {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
            String sql = " SELECT Name as TABLE_NAME FROM " + dbName + "..SysObjects Where XType='U' ORDER BY Name ";
            list = db.queryForListCommonMethod(sql);
            return list;
        } catch (Exception e) {
            return list;
        }
    }

    public List<Map<String, Object>> getTableColumns3ForMSSQL(String dbName, String tableName, String databaseConfigId) throws Exception {
        String sql = " select b.name   TREESOFTPRIMARYKEY, b.name COLUMN_NAME,  ISNULL( c.name +'('+  cast(b.length as varchar(10)) +')' , c.name ) as  COLUMN_TYPE, c.name DATA_TYPE,  case c.name    when 'date' then 0    when 'datetime' then 0    when 'datetime2' then 0    when 'int'  then 0    when 'tinyint'  then 0    when 'smallint'  then 0    when 'bigint'  then 0    when 'float' then b.xprec    when 'decimal' then b.xprec   when 'numeric' then b.xprec   else b.length  end as CHARACTER_MAXIMUM_LENGTH, b.xscale as NUMERIC_SCALE , case when b.isnullable=1  then 'YES' else 'NO' end as IS_NULLABLE ,  (SELECT 'PRI' FROM sysobjects where xtype='PK' and  parent_obj=b.id and name in (   SELECT name  FROM sysindexes   WHERE indid in(   SELECT indid FROM sysindexkeys WHERE id = b.id AND colid=b.colid  ))) as COLUMN_KEY ,  (SELECT cast( value as varchar(200)) FROM  sys.extended_properties WHERE major_id = a.id  and minor_id = b.colid ) as COLUMN_COMMENT  from sysobjects a,syscolumns b,systypes c  where a.id=b.id  and a.name='" + tableName + "' and a.xtype='U'  and b.xtype=c.xtype and c.name<>'sysname' order by colid";
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListForMSSQL(sql, 0, 999999999);
        List<Map<String, Object>> tempList = new ArrayList<>();
        Iterator<Map<String, Object>> it = list.iterator();
        while (it.hasNext()) {
            Map<String, Object> mmap = it.next();
            String data_type = (String) mmap.get("DATA_TYPE");
            if (data_type.equals("nvarchar")) {
                int leng = Integer.parseInt(mmap.get("CHARACTER_MAXIMUM_LENGTH").toString());
                mmap.put("CHARACTER_MAXIMUM_LENGTH", Integer.valueOf(leng / 2));
            }
            tempList.add(mmap);
        }
        return tempList;
    }

    public List<Map<String, Object>> getAllViewsForMSSQL(String dbName, String databaseConfigId) throws Exception {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
            list = db.queryForListCommonMethod(" SELECT  NAME AS TABLE_NAME FROM  sysobjects where XTYPE ='V'  order by NAME ");
        } catch (Exception e) {
        }
        return list;
    }

    public List<Map<String, Object>> getAllProcedureForMSSQL(String dbName, String databaseConfigId) throws Exception {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
            list = db.queryForListCommonMethod("  select  NAME AS PROC_NAME FROM sys.sysobjects where XTYPE ='P' order by NAME ");
        } catch (Exception e) {
        }
        return list;
    }

    public String getProcedureSqlForMSSQL(String databaseName, String tableName, String databaseConfigId) throws Exception {
        String sql = " SELECT name, definition as TEXT  FROM sys.sql_modules AS m   INNER JOIN sys.all_objects AS o ON m.object_id = o.object_id  WHERE o.type = 'P' and name ='" + tableName + "'";
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        String defineSQL = " ";
        for (int i = 0; i < list.size(); i++) {
            defineSQL = String.valueOf(defineSQL) + list.get(i).get("TEXT").toString() + "; \r\n";
        }
        return defineSQL;
    }

    public List<Map<String, Object>> getAllFuntionForMSSQL(String dbName, String databaseConfigId) throws Exception {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
            list = db.queryForListCommonMethod(" SELECT  NAME AS ROUTINE_NAME FROM  sysobjects where XTYPE ='FN'  order by NAME ");
        } catch (Exception e) {
        }
        return list;
    }

    public Integer getTableRowsForMSSQL(String dbName, String tableName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        String sql = " select count(*) as totals from  " + tableName;
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        Map<String, Object> map = list.get(0);
        Integer rowCount = Integer.valueOf(Integer.parseInt(map.get("totals").toString()));
        return rowCount;
    }

    public Page<Map<String, Object>> getDataForMSSQL(Page<Map<String, Object>> page, String tableName, String dbName, String databaseConfigId) throws Exception {
        Iterator<Map<String, Object>> it;
        String sql2;
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        if (limitFrom > 0) {
            int i = limitFrom - 1;
        }
        String orderBy = page.getOrderBy();
        String order = page.getOrder();
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        List<Map<String, Object>> list3 = getPrimaryKeyssForMSSQL(dbName, tableName, databaseConfigId);
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
            sql2 = "select * from  " + tableName;
        } else {
            sql2 = "select * from  " + tableName + " order by " + orderBy + " " + order;
        }
        List<Map<String, Object>> list = db.queryForListPageForMSSQL(sql2, pageNo * pageSize, (pageNo - 1) * pageSize);
        int rowCount = getTableRowsForMSSQL(dbName, tableName, databaseConfigId).intValue();
        List<Map<String, Object>> columns = getTableColumnsForMSSQL(dbName, tableName, databaseConfigId);
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
            String data_type = new StringBuilder().append(map.get("data_type")).toString().toUpperCase();
            if (data_type.equals("DATETIME") || data_type.equals("DATE")) {
                map2.put("editor", "datetimebox");
            } else if (data_type.equals("INT") || data_type.equals("SMALLINT") || data_type.equals("TINYINT")) {
                map2.put("editor", "numberbox");
            } else if (data_type.equals("DOUBLE")) {
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

    public int deleteRowsNewForMSSQL(String databaseName, String tableName, String primary_key, List<String> condition, String databaseConfigId, String username, String ip) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        int y = 0;
        for (int i = 0; i < condition.size(); i++) {
            String whereStr = condition.get(i);
            String sql = " delete top (1)  from  " + tableName + " where  1=1 " + whereStr;
            LogUtil.i(String.valueOf(DateUtils.getDateTime()) + ",删除数据SQL =" + sql);
            this.logDao.saveLog(sql, username, ip, databaseName, databaseConfigId);
            y += db.setupdateData(sql);
        }
        return y;
    }

    public String getViewSqlForMSSQL(String databaseName, String tableName, String databaseConfigId) throws Exception {
        String sql = " select  view_definition  from  information_schema.views  where  table_name='" + tableName + "' and table_catalog='" + databaseName + "'  ";
        String str = " ";
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        if (list.size() == 1) {
            Map<String, Object> map = list.get(0);
            str = (String) map.get("view_definition");
        }
        return str;
    }

    public Page<Map<String, Object>> executeSqlHaveResForMSSQL(Page<Map<String, Object>> page, String sql, String dbName, String databaseConfigId) throws Exception {
        String sql2;
        int rowCount;
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        if (limitFrom > 0) {
            int i = limitFrom - 1;
        }
        String tableName = this.sqlParserService.parserTableNames(sql, "SQL Server");
        String tempStr = sql.replace(" ", "").toLowerCase();
        if (tempStr.indexOf("select") >= 0 && tempStr.indexOf("orderby") > 0 && tempStr.indexOf("selecttop") < 0) {
            sql = String.valueOf(sql.substring(0, sql.indexOf(" "))) + " top 9999999 " + sql.substring(sql.indexOf(" "));
        }
        if (tempStr.indexOf("selectcount(*)from") >= 0) {
            sql = String.valueOf(sql.substring(0, sql.indexOf("(*) ") + 4)) + " as totals " + sql.substring(sql.indexOf("(*)") + 4);
        }
        String sql22 = " select  * from (" + sql + ")  t1 ";
        String orderBy = page.getOrderBy();
        String order = page.getOrder();
        if (StringUtils.isEmpty(orderBy)) {
            sql2 = sql22;
        } else {
            sql2 = String.valueOf(sql22) + "  order by " + orderBy + " " + order;
        }
        if (sql.indexOf("with") >= 0 || sql.indexOf("WITH") >= 0) {
            sql2 = sql;
        }
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListPageForMSSQL(sql2, pageNo * pageSize, (pageNo - 1) * pageSize);
        if (sql.indexOf("with") >= 0 || sql.indexOf("WITH") >= 0) {
            rowCount = db.executeQueryForCountForMSSqlWith(sql);
        } else if (list.size() < 10 && pageNo == 1) {
            rowCount = list.size();
        } else {
            rowCount = db.executeQueryForCountForPostgreSQL(sql);
        }
        List<Map<String, Object>> columns = executeSqlForColumnsForMSSQL(sql, dbName, databaseConfigId);
        List<Map<String, Object>> tempList = new ArrayList<>();
        Iterator<Map<String, Object>> it = columns.iterator();
        while (it.hasNext()) {
            Map<String, Object> map = it.next();
            Map<String, Object> map2 = new HashMap<>();
            map2.put("field", map.get("column_name"));
            map2.put("title", map.get("column_name"));
            map2.put("sortable", true);
            if (map.get("data_type").equals("date") || map.get("data_type").equals("datetime") || map.get("data_type").equals("timestamp") || map.get("data_type").equals("datetime2")) {
                map2.put("editor", "datetimebox");
            } else if (map.get("data_type").equals("int") || map.get("data_type").equals("smallint") || map.get("data_type").equals("tinyint") || map.get("data_type").equals("integer") || map.get("data_type").equals("bit") || map.get("data_type").equals("real") || map.get("data_type").equals("bigint") || map.get("data_type").equals("long") || map.get("data_type").equals("float") || map.get("data_type").equals("decimal") || map.get("data_type").equals("numeric") || map.get("data_type").equals("mediumint")) {
                map2.put("editor", "numberbox");
            } else if (map.get("data_type").equals("binary") || map.get("data_type").equals("varbinary") || map.get("data_type").equals("blob") || map.get("data_type").equals("tinyblob") || map.get("data_type").equals("mediumblob") || map.get("data_type").equals("longblob") || map.get("data_type").equals("image") || map.get("data_type").equals("money") || map.get("data_type").equals("text") || map.get("data_type").equals("xml")) {
                map2.put("editor", null);
            } else {
                map2.put("editor", "text");
            }
            tempList.add(map2);
        }
        String primaryKey = "";
        List<Map<String, Object>> list3 = selectTablePrimaryKeyForMSSQL(dbName, tableName, databaseConfigId);
        String tem = "";
        Iterator<Map<String, Object>> it2 = list3.iterator();
        while (it2.hasNext()) {
            Map<String, Object> map3 = it2.next();
            tem = String.valueOf(tem) + map3.get("COLUMN_NAME") + ",";
        }
        if (!StringUtils.isEmpty(tem)) {
            primaryKey = tem.substring(0, tem.length() - 1);
        }
        ObjectMapper mapper = new ObjectMapper();
        String jsonfromList = "[" + mapper.writeValueAsString(tempList) + "]";
        page.setTotalCount(rowCount);
        page.setResult(list);
        page.setColumns(jsonfromList);
        page.setPrimaryKey(primaryKey);
        page.setTableName(tableName);
        page.setOperator("read");
        return page;
    }

    public Page<Map<String, Object>> executeSqlProcedureForMSSQL(Page<Map<String, Object>> page, String sql, String dbName, String databaseConfigId) throws Exception {
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        if (limitFrom > 0) {
            int i = limitFrom - 1;
        }
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.executeSqlProcedureForMSSQL(sql, pageNo * pageSize, (pageNo - 1) * pageSize);
        if (list.size() == 0) {
            return null;
        }
        int rowCount = db.executeQueryForCountProcedureForMSSQL(sql);
        List<Map<String, Object>> columns = db.executeSqlProcedureForColumnsForMSSQL(sql);
        List<Map<String, Object>> tempList = new ArrayList<>();
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
        page.setTableName("");
        page.setOperator("read");
        return page;
    }

    public int deleteTableColumnForMSSQL(String databaseName, String tableName, String[] ids, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        int y = 0;
        for (int i = 0; i < ids.length; i++) {
            String sql = " alter table   " + tableName + " drop column  " + ids[i];
            y += db.setupdateData(sql);
        }
        return y;
    }

    public int updateTableNullAbleForMSSQL(String databaseName, String tableName, String column_name, String is_nullable, String databaseConfigId) throws Exception {
        String sql4;
        if (column_name != null && !column_name.equals("")) {
            DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
            String column_type = selectOneColumnTypeForMSSQL(databaseName, tableName, column_name, databaseConfigId);
            if (!StringUtils.isEmpty(column_type) && column_type.indexOf("int") == 0) {
                column_type = "int";
            }
            if (!StringUtils.isEmpty(column_type) && column_type.indexOf("tinyint") == 0) {
                column_type = "tinyint";
            }
            if (is_nullable.equals("true")) {
                sql4 = " alter table  " + tableName + " alter column   " + column_name + " " + column_type + "   null";
            } else {
                sql4 = " alter table  " + tableName + " alter column   " + column_name + " " + column_type + "   not null";
            }
            db.setupdateData(sql4);
            return 0;
        }
        return 0;
    }

    public int savePrimaryKeyForMSSQL(String databaseName, String tableName, String column_name, String isSetting, String databaseConfigId) throws Exception {
        if (column_name != null && !column_name.equals("")) {
            DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
            List<Map<String, Object>> list2 = selectTablePrimaryKeyForMSSQL(databaseName, tableName, databaseConfigId);
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

    public List<Map<String, Object>> selectTablePrimaryKeyForMSSQL(String databaseName, String tableName, String databaseConfigId) throws Exception {
        String sql = "  select  b.CONSTRAINT_NAME, b.COLUMN_NAME  from information_schema.table_constraints a  inner join information_schema.constraint_column_usage b  on a.constraint_name = b.constraint_name  where a.constraint_type = 'PRIMARY KEY' and a.table_name = '" + tableName + "'";
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        return list;
    }

    public List<Map<String, Object>> getPrimaryKeyssForMSSQL(String databaseName, String tableName, String databaseConfigId) throws Exception {
        String sql = " select  c.name as COLUMN_NAME from sysindexes i   join sysindexkeys k on i.id = k.id and i.indid = k.indid    join sysobjects o on i.id = o.id    join syscolumns c on i.id=c.id and k.colid = c.colid    where o.xtype = 'U'   and exists(select 1 from sysobjects where  xtype = 'PK'  and name = i.name)     and o.name='" + tableName + "' ";
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        return list;
    }

    public List<String> getIsIdentityForMSSQL(String databaseName, String tableName, String databaseConfigId) throws Exception {
        String sql = " select column_name  from  INFORMATION_SCHEMA.columns   where  table_catalog ='" + databaseName + "' and  TABLE_NAME='" + tableName + "'  AND  COLUMNPROPERTY(OBJECT_ID('" + tableName + "'),COLUMN_NAME,'IsIdentity')=1 ";
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        List<String> resList = new ArrayList<>();
        Iterator<Map<String, Object>> it = list.iterator();
        while (it.hasNext()) {
            Map<String, Object> map = it.next();
            resList.add(map.get("column_name").toString());
        }
        return resList;
    }

    public List<Map<String, Object>> getTableColumnsForMSSQL(String dbName, String tableName, String databaseConfigId) throws Exception {
        String sql = "select top 1 * from   " + tableName;
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForColumnOnly(sql);
        return list;
    }

    public List<Map<String, Object>> executeSqlForColumnsForMSSQL(String sql, String dbName, String databaseConfigId) throws Exception {
        String sql2 = " select top 1 * from (" + sql + ") t  ";
        if (sql.indexOf("with") >= 0 || sql.indexOf("WITH") >= 0) {
            sql2 = sql;
        }
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.executeSqlForColumns(sql2);
        return list;
    }

    public String selectOneColumnTypeForMSSQL(String databaseName, String tableName, String column_name, String databaseConfigId) throws Exception {
        String sql = " select  ISNULL( c.name +'('+  cast(b.length as varchar(10)) +')' , c.name ) as  column_type  from sysobjects a,syscolumns b,systypes c  where a.id=b.id  and a.name='" + tableName + "'  and  b.name='" + column_name + "' and a.xtype='U'  and b.xtype=c.xtype ";
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        return (String) list.get(0).get("column_type");
    }

    public boolean backupDatabaseExecuteForMSSQL(String databaseName, String tableName, String path, String databaseConfigId) throws Exception {
        BackDbForMSSQL bdo = new BackDbForMSSQL();
        bdo.readDataToFile(databaseName, tableName, path, databaseConfigId);
        return true;
    }

    public boolean copyTableForMSSQL(String databaseName, String tableName, String databaseConfigId) throws Exception {
        String sql4 = "select * into " + tableName + "_" + DateUtils.getDateTimeString(new Date()) + " from " + tableName;
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        db.setupdateData(sql4);
        return true;
    }

    public List<Map<String, Object>> exportDataToSQLForMSSQL(String databaseName, String tableName, List<String> condition, String databaseConfigId) throws Exception {
        String sql = " select * from  " + tableName + " where   1=1  ";
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < condition.size(); i++) {
            List<Map<String, Object>> list2 = db.queryForListForMSSQL(String.valueOf(sql) + condition.get(i), 0, 999999999);
            list.add(list2.get(0));
        }
        return list;
    }

    public boolean dropTableForMSSQL(String databaseName, String tableName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        String sql = " drop  table " + tableName;
        db.setupdateData(sql);
        LogUtil.i(String.valueOf(DateUtils.getDateTime()) + ",删除MSSQL表,SQL =" + sql);
        return true;
    }

    public List<Map<String, Object>> viewTableMessForMSSQL(String dbName, String tableName, String databaseConfigId) throws Exception {
        String sql = "  select top 1 t1.name as table_name, t1.crdate ,t1.refdate , t2.*  from   sysobjects t1 left join  sysindexes t2  on   t1.id=t2.id  where  t1.name ='" + tableName + "'  ";
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        return list;
    }

    public int saveNewTableForMSSQL(JSONArray insertArray, String databaseName, String tableName, String databaseConfigId) throws Exception {
        String sql;
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        String sql2 = " create table " + tableName + " (  ";
        String PRIMARY_KEY = "";
        new ArrayList();
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

    public boolean judgeTableExistsForMSSQL(Map<String, Object> jobMessageMap, String sourceDbType, String targetDbType) throws Exception {
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
        if (sourceDbType.equals("MySQL") || sourceDbType.equals("MariaDB") || sourceDbType.equals("MySQL8.0") || sourceDbType.equals("TiDB")) {
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

    public List<Map<String, Object>> selectAllDataFromSQLForMSSQL(String databaseName, String databaseConfigId, String sql, int limitFrom, int pageSize) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListForMSSQL(sql, limitFrom, pageSize);
        return list;
    }

    public boolean insertByDataListForMSSQL(List<Map<String, Object>> dataList, String databaseName, String tableName, String databaseConfigId) throws Exception {
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
                } else if (entry.getValue().toString().indexOf("0x") == 0) {
                    values = String.valueOf(values) + entry.getValue().toString() + ",";
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

    public boolean updateByDataListForMSSQL(List<Map<String, Object>> dataList, String databaseName, String tableName, String databaseConfigId, String qualification) throws Exception {
        String ss;
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
                        byte[] ss2 = (byte[]) entry.getValue();
                        if (ss2.length == 0) {
                            updateSQL = String.valueOf(updateSQL2) + "null,";
                        } else {
                            updateSQL = String.valueOf(updateSQL2) + "0x" + DataUtil.bytesToHexString(ss2) + ",";
                        }
                    } else if (entry.getValue() instanceof ArrayList) {
                        updateSQL = String.valueOf(updateSQL2) + "'" + entry.getValue().toString() + "',";
                    } else if (entry.getValue().toString().indexOf("0x") == 0) {
                        updateSQL = String.valueOf(updateSQL2) + entry.getValue().toString() + ",";
                    } else {
                        String tempValues = (String) entry.getValue();
                        updateSQL = String.valueOf(updateSQL2) + "'" + tempValues.replace("\\", "\\\\").replace("'", "\\'").replace("\r\n", "\\r\\n").replace("\n\r", "\\n\\r").replace("\r", "\\r").replace("\n", "\\n") + "',";
                    }
                }
            }
            String updateSQL3 = updateSQL.substring(0, updateSQL.length() - 1);
            String whereStr = " where 1=1 ";
            for (String ss3 : whereColumn) {
                whereStr = String.valueOf(whereStr) + " and " + ss3.trim() + "='" + map4.get(ss) + "' ";
            }
            sqlList.add(String.valueOf(updateSQL3) + whereStr);
        }
        db.updateExecuteBatch(sqlList);
        return true;
    }

    public boolean insertOrUpdateByDataListForMSSQL(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
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
                insertByDataListForMSSQL(insertDataList, databaseName, table, databaseConfigId);
            }
            if (updateDataList.size() > 0) {
                updateByDataListForMSSQL(updateDataList, databaseName, table, databaseConfigId, qualification);
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

    public boolean insertOnlyByDataListForMSSQL(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
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
            insertByDataListForMSSQL(insertDataList, databaseName, table, databaseConfigId);
            return true;
        }
        return true;
    }

    public boolean deleteByDataListForMSSQL(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
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

    public String createTableSQLForMSSQL(String tableName, String databaseName, String databaseConfigId) throws Exception {
        new DBUtil2(databaseName, databaseConfigId);
        StringBuffer sb2 = new StringBuffer();
        sb2.append("CREATE TABLE " + tableName + " ( \r\n");
        String primary_key_list = "";
        List<Map<String, Object>> listTableColumn = getTableColumns3ForMSSQL(databaseName, tableName, databaseConfigId);
        Iterator<Map<String, Object>> it = listTableColumn.iterator();
        while (it.hasNext()) {
            Map<String, Object> map3 = it.next();
            String data_type_temp = new StringBuilder().append(map3.get("DATA_TYPE")).toString();
            new StringBuilder().append(map3.get("COLUMN_TYPE")).toString();
            String numeric_scale = new StringBuilder().append(map3.get("NUMERIC_SCALE")).toString();
            if (StringUtils.isEmpty(numeric_scale)) {
                numeric_scale = "0";
            }
            if (data_type_temp.equals("date") || data_type_temp.equals("int") || data_type_temp.equals("smallint") || data_type_temp.equals("bigint")) {
                sb2.append(" " + map3.get("COLUMN_NAME") + " " + map3.get("DATA_TYPE"));
            } else if (data_type_temp.equals("float")) {
                sb2.append(" " + map3.get("COLUMN_NAME") + " " + map3.get("DATA_TYPE") + "(" + map3.get("CHARACTER_MAXIMUM_LENGTH") + ")");
            } else if (data_type_temp.equals("decimal") || data_type_temp.equals("numeric")) {
                sb2.append(" " + map3.get("COLUMN_NAME") + " " + map3.get("DATA_TYPE") + "(" + map3.get("CHARACTER_MAXIMUM_LENGTH") + "," + numeric_scale + ")");
            } else {
                sb2.append(" " + map3.get("COLUMN_NAME") + " " + map3.get("COLUMN_TYPE"));
            }
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

    public int updateTableColumnForMSSQL(Map<String, Object> map, String databaseName, String tableName, String columnName, String idValues, String databaseConfigId) throws Exception {
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
        String numeric_scale = new StringBuilder().append(map.get("NUMERIC_SCALE")).toString();
        if (StringUtils.isEmpty(numeric_scale)) {
            numeric_scale = "0";
        }
        if (!old_field_name.endsWith(column_name)) {
            String sql = " exec sp_rename '" + tableName + "." + old_field_name + "','" + column_name + "','COLUMN'";
            db.setupdateData(sql);
        }
        String sql2 = " alter table  " + tableName + " alter column " + column_name + " " + data_type;
        if (!data_type.equals("int") && !data_type.equals("date") && !data_type.equals("datetime") && !data_type.equals("timestamp") && character_maximum_length != null && !character_maximum_length.equals("")) {
            sql2 = (data_type.equals("float") || data_type.equals("decimal") || data_type.equals("numeric")) ? String.valueOf(sql2) + " (" + character_maximum_length + "," + numeric_scale + ") " : String.valueOf(sql2) + " (" + character_maximum_length + ")";
        }
        if (column_comment != null && !column_comment.equals("")) {
            try {
                String sql5 = "select t2.name , cast( t1.value as varchar(200)) as value  from sys.extended_properties t1 left join syscolumns t2   on  t1.minor_id = t2.colid and t1.major_id = t2.id  where  1=1   and t1.major_id in (select id from sys.sysobjects sysobj where sysobj.name='" + tableName + "')  and t2.name = '" + column_name + "' ";
                new ArrayList();
                List<Map<String, Object>> list = db.queryForListCommonMethod(sql5);
                if (list.size() > 0) {
                    String sql4 = "exec sp_updateextendedproperty 'MS_Description','" + column_comment + "','user','dbo','TABLE','" + tableName + "','column','" + column_name + "' ";
                    db.setupdateData(sql4);
                } else {
                    String sql42 = "exec sp_addextendedproperty  'MS_Description','" + column_comment + "','user','dbo','TABLE','" + tableName + "','column','" + column_name + "' ";
                    db.setupdateData(sql42);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        int y = db.setupdateData(sql2);
        return y;
    }

    public int saveDesginColumnForMSSQL(Map<String, String> map, String databaseName, String tableName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        String data_type = map.get("DATA_TYPE");
        String character_maximum_length = new StringBuilder(String.valueOf(map.get("CHARACTER_MAXIMUM_LENGTH"))).toString();
        String numeric_scale = new StringBuilder(String.valueOf(map.get("NUMERIC_SCALE"))).toString();
        if (StringUtils.isEmpty(numeric_scale)) {
            numeric_scale = "0";
        }
        String sql = String.valueOf(String.valueOf(" alter table " + tableName + " add  ") + map.get("COLUMN_NAME") + "  ") + data_type;
        if (!data_type.equals("int") && !data_type.equals("date") && map.get("CHARACTER_MAXIMUM_LENGTH") != null && !map.get("CHARACTER_MAXIMUM_LENGTH").equals("")) {
            sql = (data_type.equals("float") || data_type.equals("decimal") || data_type.equals("numeric")) ? String.valueOf(sql) + " (" + character_maximum_length + "," + numeric_scale + ") " : String.valueOf(sql) + " (" + character_maximum_length + ") ";
        }
        int y = db.setupdateData(sql);
        if (map.get("COLUMN_COMMENT") != null && !map.get("COLUMN_COMMENT").equals("")) {
            String column_comment = map.get("COLUMN_COMMENT");
            String sql4 = "exec sp_addextendedproperty 'MS_Description','" + column_comment + "','user','dbo','TABLE','" + tableName + "','column','" + map.get("COLUMN_NAME") + "' ";
            db.setupdateData(sql4);
        }
        return y;
    }

    public int saveRowsForMSSQL(Map<String, String> map, String databaseName, String tableName, String databaseConfigId, String username, String ip) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        String sql = " insert into " + tableName;
        String colums = " ";
        String values = " ";
        List<String> identityColumnList = getIsIdentityForMSSQL(databaseName, tableName, databaseConfigId);
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            String valueTemp = entry.getValue();
            String columnTemp = entry.getKey();
            if (!identityColumnList.contains(columnTemp) || !valueTemp.equals("")) {
                colums = String.valueOf(colums) + columnTemp + ",";
                String columnType = selectOneColumnTypeForMSSQL(databaseName, tableName, entry.getKey(), databaseConfigId);
                if (valueTemp.equals("")) {
                    values = String.valueOf(values) + " null ,";
                } else if (columnType.equals("DATE")) {
                    values = String.valueOf(values) + " to_date('" + valueTemp + "' ,'yyyy-mm-dd hh24:mi:ss') ,";
                } else {
                    values = String.valueOf(values) + "'" + valueTemp + "',";
                }
            }
        }
        String sql2 = String.valueOf(sql) + " (" + colums.substring(0, colums.length() - 1) + ") values (" + values.substring(0, values.length() - 1) + ")";
        this.logDao.saveLog(sql2, username, ip, databaseName, databaseConfigId);
        int y = db.setupdateData(sql2);
        return y;
    }

    public Page<Map<String, Object>> selectTableIndexsForMSSQL(Page<Map<String, Object>> page, DmsDto dto) throws Exception {
        DBUtil2 db1 = new DBUtil2(dto.getDatabaseName(), dto.getDatabaseConfigId());
        String sql1 = " SELECT  a.name as indexName , c.name as tableName ,   d.name as columnName    FROM   sysindexes   a  JOIN   sysindexkeys   b   ON   a.id=b.id   AND   a.indid=b.indid  JOIN   sysobjects   c   ON   b.id=c.id  JOIN   syscolumns   d   ON   b.id=d.id   AND   b.colid=d.colid  WHERE   a.indid   NOT IN(0,255)  AND   c.name='" + dto.getTableName() + "' ORDER BY  c.name,a.name,d.name";
        List<Map<String, Object>> list = db1.queryForListCommonMethod(sql1);
        page.setTotalCount(list.size());
        page.setResult(list);
        return page;
    }

    public boolean indexSaveForMSSQL(HttpServletRequest request, IdsDto dto, String username) throws Exception {
        DBUtil2 db1 = new DBUtil2(dto.getDatabaseName(), dto.getDatabaseConfigId());
        String ip = NetworkUtil.getIpAddress(request);
        String sql = " create index  " + dto.getIndexName() + " on " + dto.getTableName() + "(" + dto.getColumn_name() + ")";
        db1.setupdateData(sql);
        this.logDao.saveLog("保存索引 " + sql, username, ip, dto.getDatabaseName(), dto.getDatabaseConfigId());
        return true;
    }

    public boolean indexDeleteForMSSQL(HttpServletRequest request, IdsDto dto, String username) throws Exception {
        DBUtil2 db1 = new DBUtil2(dto.getDatabaseName(), dto.getDatabaseConfigId());
        String ip = NetworkUtil.getIpAddress(request);
        String sql = " drop index  " + dto.getIndexName() + " on " + dto.getTableName();
        db1.setupdateData(sql);
        this.logDao.saveLog("删除索引 " + sql, username, ip, dto.getDatabaseName(), dto.getDatabaseConfigId());
        return true;
    }

    public Page<Map<String, Object>> selectTableTriggersForMSSQL(Page<Map<String, Object>> page, DmsDto dto) throws Exception {
        DBUtil2 db1 = new DBUtil2(dto.getDatabaseName(), dto.getDatabaseConfigId());
        String sql1 = " SELECT  t1.name as trigger_name ,t2.name as table_name    from  sysobjects  t1 left join  sysobjects t2   on   t1.parent_obj  =t2.id  where t1.xtype='TR'  and  t2.name ='" + dto.getTableName() + "'";
        List<Map<String, Object>> list = db1.queryForListCommonMethod(sql1);
        page.setTotalCount(list.size());
        page.setResult(list);
        return page;
    }

    public boolean triggerDeleteForMSSQL(HttpServletRequest request, IdsDto dto, String username) throws Exception {
        DBUtil2 db1 = new DBUtil2(dto.getDatabaseName(), dto.getDatabaseConfigId());
        String ip = NetworkUtil.getIpAddress(request);
        String sql = " drop trigger " + dto.getTriggerName();
        db1.setupdateData(sql);
        this.logDao.saveLog("删除触发器 " + sql, username, ip, dto.getDatabaseName(), dto.getDatabaseConfigId());
        return true;
    }

    public Integer queryDatabaseStatusForMSSQLSession(String databaseName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        String sql = "SELECT COUNT(*) as session FROM [Master].[dbo].[SYSPROCESSES]  WHERE [DBID] IN ( SELECT [DBID] FROM [Master].[dbo].[SYSDATABASES] WHERE NAME='" + databaseName + "' )";
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        String value = "0";
        if (list.size() > 0) {
            Map<String, Object> temp = list.get(0);
            value = new StringBuilder().append(temp.get("session")).toString();
        }
        return Integer.valueOf(Integer.parseInt(value));
    }
}
