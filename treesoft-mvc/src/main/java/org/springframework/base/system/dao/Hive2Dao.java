package org.springframework.base.system.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.base.system.dbUtils.DBUtil2;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.utils.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/dao/Hive2Dao.class */
public class Hive2Dao {
    @Autowired
    private ConfigDao configDao;
    @Autowired
    private PermissionDao permissionDao;

    public List<Map<String, Object>> getAllDataBaseForHive2(String databaseConfigId) throws Exception {
        Map<String, Object> map12 = this.configDao.getConfigById(databaseConfigId);
        String databaseName = new StringBuilder().append(map12.get("databaseName")).toString();
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
            list = db.queryForListCommonMethod(" show databases ");
        } catch (Exception e) {
        }
        List<Map<String, Object>> list2 = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("SCHEMA_NAME", databaseName);
        list2.add(map);
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map2 = list.get(i);
            String schema_name = (String) map2.get("database_name");
            if (!schema_name.equals(databaseName)) {
                map2.put("SCHEMA_NAME", schema_name);
                list2.add(map2);
            }
        }
        return list2;
    }

    public List<Map<String, Object>> getAllViewsForHive2(String databaseName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        String sql = " use " + databaseName;
        db.setupdateData(sql);
        List<Map<String, Object>> list = db.queryForListCommonMethod("show views ");
        List<Map<String, Object>> list2 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            Map<String, Object> map2 = list.get(i);
            map.put("TABLE_NAME", map2.get("tab_name"));
            list2.add(map);
        }
        return list2;
    }

    public Page<Map<String, Object>> getDataForHive2(Page<Map<String, Object>> page, String tableName, String dbName, String databaseConfigId) throws Exception {
        String sql2;
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int i = (pageNo - 1) * pageSize;
        String orderBy = page.getOrderBy();
        page.getOrder();
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        String sql_use = " use  " + dbName;
        db.setupdateData(sql_use);
        if (orderBy == null || orderBy.equals("")) {
            sql2 = "select * from   " + tableName + " limit 20 ";
        } else {
            sql2 = "select * from   " + tableName + " limit 20 ";
        }
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql2);
        List<Map<String, Object>> tempList = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<>();
        map1.put("field", "treeSoftPrimaryKey");
        map1.put("checkbox", true);
        tempList.add(map1);
        if (list.size() > 0) {
            Map<String, Object> map2 = list.get(0);
            Iterator<Map.Entry<String, Object>> it = map2.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = it.next();
                Map<String, Object> map3 = new HashMap<>();
                map3.put("field", entry.getKey());
                map3.put("title", entry.getKey());
                map3.put("sortable", true);
                tempList.add(map3);
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        String jsonfromList = "[" + mapper.writeValueAsString(tempList) + "]";
        page.setTotalCount(20);
        page.setResult(list);
        page.setColumns(jsonfromList);
        page.setOperator("read");
        return page;
    }

    public Page<Map<String, Object>> executeSqlHaveResForHive2(Page<Map<String, Object>> page, String sql, String dbName, String databaseConfigId) throws Exception {
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        if (limitFrom > 0) {
            int i = limitFrom - 1;
        }
        String sql2 = " select  * from (" + sql + ")  t1  ";
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListPageForHive2(sql2, pageNo * pageSize, (pageNo - 1) * pageSize);
        List<Map<String, Object>> tempList = new ArrayList<>();
        if (list.size() > 0) {
            Map<String, Object> map2 = list.get(0);
            Iterator<Map.Entry<String, Object>> it = map2.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = it.next();
                Map<String, Object> map3 = new HashMap<>();
                map3.put("field", entry.getKey());
                map3.put("title", entry.getKey());
                map3.put("editor", null);
                map3.put("sortable", true);
                tempList.add(map3);
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        String jsonfromList = "[" + mapper.writeValueAsString(tempList) + "]";
        page.setTotalCount(20);
        page.setResult(list);
        page.setColumns(jsonfromList);
        page.setOperator("read");
        return page;
    }

    public boolean renameTableForHive2(String databaseName, String tableName, String databaseConfigId, String newTableName) throws Exception {
        String sql4 = " alter table " + ("\"" + tableName + "\"") + " rename to  " + ("\"" + newTableName + "\"");
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        db.setupdateData(sql4);
        return true;
    }

    public List<Map<String, Object>> getAllTablesForHive2(String databaseName, String databaseConfigId) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        String sql = " use " + databaseName;
        db.setupdateData(sql);
        new ArrayList();
        List<Map<String, Object>> list = db.queryForListCommonMethod("show tables ");
        List<Map<String, Object>> list2 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            Map<String, Object> map2 = list.get(i);
            map.put("TABLE_NAME", map2.get("tab_name"));
            list2.add(map);
        }
        return list2;
    }

    public List<Map<String, Object>> viewTableMessForHive2(String dbName, String tableName, String databaseConfigId) throws Exception {
        String sql = "desc formatted " + tableName + " ";
        DBUtil2 db = new DBUtil2(dbName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql);
        return list;
    }

    public List<Map<String, Object>> selectAllDataFromSQLForHive2(String databaseName, String databaseConfigId, String sql, int limitFrom, int pageSize) throws Exception {
        DBUtil2 db = new DBUtil2(databaseName, databaseConfigId);
        List<Map<String, Object>> list = db.queryForListForHive2(sql, limitFrom, pageSize);
        return list;
    }

    public boolean insertByDataListForHive2(List<Map<String, Object>> dataList, String databaseName, String tableName, String databaseConfigId) throws Exception {
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
                } else if ((entry.getValue() instanceof Date) || (entry.getValue() instanceof Time) || (entry.getValue() instanceof Timestamp)) {
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
}
