package org.springframework.base.system.dao;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.base.system.dbUtils.DBUtilJDBC;
import org.springframework.base.system.dbUtils.ElasticSearchUtil;
import org.springframework.base.system.entity.NewQueryDTO;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.utils.Constants;
import org.springframework.base.system.utils.DateUtils;
import org.springframework.base.system.utils.HttpUtil;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.base.system.utils.ServletUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/dao/ESDao.class */
public class ESDao {
    @Autowired
    private ConfigDao configDao;

    public List<Map<String, Object>> getAllDataBaseForES(String databaseConfigId) throws Exception {
        Map<String, Object> map12 = this.configDao.getConfigById(databaseConfigId);
        Object databaseName = new StringBuilder().append(map12.get("databaseName")).toString();
        List<Map<String, Object>> list2 = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("SCHEMA_NAME", databaseName);
        list2.add(map);
        return list2;
    }

    public Page<Map<String, Object>> getDataForES(Page<Map<String, Object>> page, String tableName, String dbName, String databaseConfigId) throws Exception {
        String sql2;
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int i = (pageNo - 1) * pageSize;
        String orderBy = page.getOrderBy();
        String order = page.getOrder();
        DBUtilJDBC db = new DBUtilJDBC(dbName, databaseConfigId);
        String sql = "select * from  " + tableName;
        if (orderBy == null || orderBy.equals("")) {
            sql2 = "select * from  " + tableName + " limit " + pageSize;
        } else {
            sql2 = "select * from  " + tableName + " limit " + pageSize + " order by " + orderBy + " " + order;
        }
        List<Map<String, Object>> list = db.queryForListCommonMethod(sql2);
        int rowCount = db.executeQueryForCount(sql);
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
        page.setTotalCount(rowCount);
        page.setResult(list);
        page.setColumns(jsonfromList);
        page.setOperator("read");
        return page;
    }

    public Page<Map<String, Object>> executeSqlHaveResultForES(Page<Map<String, Object>> page, String sql, String dbName, String databaseConfigId) throws Exception {
        String sql2;
        int rowCount;
        String orderBy = page.getOrderBy();
        String order = page.getOrder();
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int i = (pageNo - 1) * pageSize;
        String sql3 = sql.trim();
        if (StringUtils.isEmpty(orderBy)) {
            sql2 = " select * from ( " + sql3 + " ) tab  LIMIT " + pageSize;
        } else {
            sql2 = " select * from ( " + sql3 + " ) tab  order by " + orderBy + " " + order + " LIMIT " + pageSize;
        }
        if (sql3.toLowerCase().indexOf("show") == 0 || sql3.toLowerCase().indexOf("explain") == 0) {
            sql2 = sql3;
        }
        DBUtilJDBC db = new DBUtilJDBC(dbName, databaseConfigId);
        Date b1 = new Date();
        List<Map<String, Object>> list = db.queryForList(sql2);
        Date b2 = new Date();
        long executeTime = b2.getTime() - b1.getTime();
        if (sql3.indexOf("show") == 0 || sql3.indexOf("SHOW") == 0 || sql3.indexOf("explain") == 0 || sql3.indexOf("EXPLAIN") == 0) {
            rowCount = list.size();
        } else if (list.size() < 10 && pageNo == 1) {
            rowCount = list.size();
        } else {
            rowCount = db.executeQueryForCount(sql3);
        }
        List<Map<String, Object>> tempList = new ArrayList<>();
        if (list.size() > 0) {
            Map<String, Object> mapTemp = list.get(0);
            Iterator<Map.Entry<String, Object>> it = mapTemp.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = it.next();
                Map<String, Object> mapTemp2 = new HashMap<>();
                String columnName = entry.getKey();
                mapTemp2.put("field", columnName);
                mapTemp2.put("title", columnName);
                mapTemp2.put("editor", null);
                mapTemp2.put("sortable", true);
                tempList.add(mapTemp2);
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        String jsonfromList = "[" + mapper.writeValueAsString(tempList) + "]";
        page.setTotalCount(rowCount);
        page.setResult(list);
        page.setColumns(jsonfromList);
        page.setPrimaryKey("");
        page.setTableName("");
        page.setExecuteTime(new StringBuilder(String.valueOf(executeTime)).toString());
        page.setOperator("read");
        return page;
    }

    public int executeSqlNotResForES(String sql, String dbName, String databaseConfigId) throws Exception {
        new HashMap();
        DBUtilJDBC db = new DBUtilJDBC(dbName, databaseConfigId);
        int i = db.setupdateData(sql);
        return i;
    }

    public List<Map<String, Object>> getAllIndexsForES4HTTP(String databaseName, String databaseConfigId) throws Exception {
        Map<String, Object> map12 = Constants.configStaticMap.get(databaseConfigId);
        String ip = new StringBuilder().append(map12.get("ip")).toString();
        String port = new StringBuilder().append(map12.get("port")).toString();
        String userName = new StringBuilder().append(map12.get("userName")).toString();
        String password = new StringBuilder().append(map12.get("password")).toString();
        if (StringUtils.isEmpty(ElasticSearchUtil.VALID_HTTP_URL)) {
            ElasticSearchUtil.returnVaildURL(ip, port, userName, password);
        }
        String httpUrl = String.valueOf(ElasticSearchUtil.VALID_HTTP_URL) + "/_cat/indices/?format=json";
        try {
            String resultStr = HttpUtil.getHttpMethodAuth(httpUrl, "GET", userName, password);
            JSONArray array = JSONArray.parseArray(resultStr);
            List<Map<String, Object>> list2 = new ArrayList<>();
            for (int i = 0; i < array.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                JSONObject map2 = array.getJSONObject(i);
                map.put("TABLE_NAME", map2.get("index"));
                if (!map2.get("index").equals(".security") && !map2.get("index").equals(".security-7")) {
                    list2.add(map);
                }
            }
            return list2;
        } catch (Exception e) {
            ElasticSearchUtil.VALID_HTTP_URL = "";
            throw new Exception(e);
        }
    }

    public JSONArray viewTableMessForES(String dbName, String tableName, String databaseConfigId) throws Exception {
        Map<String, Object> map12 = Constants.configStaticMap.get(databaseConfigId);
        String ip = new StringBuilder().append(map12.get("ip")).toString();
        String port = new StringBuilder().append(map12.get("port")).toString();
        String userName = new StringBuilder().append(map12.get("userName")).toString();
        String password = new StringBuilder().append(map12.get("password")).toString();
        if (StringUtils.isEmpty(ElasticSearchUtil.VALID_HTTP_URL)) {
            ElasticSearchUtil.returnVaildURL(ip, port, userName, password);
        }
        String httpUrl = String.valueOf(ElasticSearchUtil.VALID_HTTP_URL) + "/_cat/indices/" + tableName + "?format=json";
        String resultStr = HttpUtil.getHttpMethodAuth(httpUrl, "GET", userName, password);
        JSONArray array = JSONArray.parseArray(resultStr);
        return array;
    }

    public List<Map<String, Object>> selectAllDataFromSQLForES(String databaseName, String databaseConfigId, String sql, int limitFrom, int pageSize) throws Exception {
        DBUtilJDBC db = new DBUtilJDBC(databaseName, databaseConfigId);
        String sql2 = " select * from ( " + sql.trim() + " ) tab  LIMIT " + pageSize;
        List<Map<String, Object>> list = db.queryForList(sql2);
        return list;
    }

    public boolean dropTableForES(String databaseName, String tableName, String databaseConfigId) throws Exception {
        Map<String, Object> map12 = Constants.configStaticMap.get(databaseConfigId);
        String ip = new StringBuilder().append(map12.get("ip")).toString();
        String port = new StringBuilder().append(map12.get("port")).toString();
        String userName = new StringBuilder().append(map12.get("userName")).toString();
        String password = new StringBuilder().append(map12.get("password")).toString();
        if (StringUtils.isEmpty(ElasticSearchUtil.VALID_HTTP_URL)) {
            ElasticSearchUtil.returnVaildURL(ip, port, userName, password);
        }
        String httpUrl = String.valueOf(ElasticSearchUtil.VALID_HTTP_URL) + "/" + tableName;
        HttpUtil.getHttpMethodAuth(httpUrl, "DELETE", userName, password);
        LogUtil.i(String.valueOf(DateUtils.getDateTime()) + ",删除ElasticSearch索引,URL =" + httpUrl);
        return true;
    }

    public Map<String, Object> queryDatabaseStatusForES(String databaseName, String databaseConfigId) throws Exception {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> map12 = Constants.configStaticMap.get(databaseConfigId);
        String ip = new StringBuilder().append(map12.get("ip")).toString();
        String port = new StringBuilder().append(map12.get("port")).toString();
        String userName = new StringBuilder().append(map12.get("userName")).toString();
        String password = new StringBuilder().append(map12.get("password")).toString();
        if (StringUtils.isEmpty(ElasticSearchUtil.VALID_HTTP_URL)) {
            ElasticSearchUtil.returnVaildURL(ip, port, userName, password);
        }
        String httpUrl = String.valueOf(ElasticSearchUtil.VALID_HTTP_URL) + "/_cat/health?format=json";
        String resultStr = HttpUtil.getHttpMethodAuth(httpUrl, "GET", userName, password);
        JSONArray array = JSONArray.parseArray(resultStr);
        JSONObject jsobj = array.getJSONObject(0);
        map.put("cluster_status", jsobj.getString("status"));
        map.put("node_total", jsobj.getString("node.total"));
        map.put("node_data", jsobj.getString("node.data"));
        map.put("shards", jsobj.getString("shards"));
        map.put("active_shards_percent", jsobj.getString("active_shards_percent"));
        return map;
    }

    public List<Map<String, Object>> monitorItemValueForES(String databaseName, String databaseConfigId) throws Exception {
        List<Map<String, Object>> list2 = new ArrayList<>();
        Map<String, Object> map12 = Constants.configStaticMap.get(databaseConfigId);
        String ip = new StringBuilder().append(map12.get("ip")).toString();
        String port = new StringBuilder().append(map12.get("port")).toString();
        String userName = new StringBuilder().append(map12.get("userName")).toString();
        String password = new StringBuilder().append(map12.get("password")).toString();
        if (StringUtils.isEmpty(ElasticSearchUtil.VALID_HTTP_URL)) {
            ElasticSearchUtil.returnVaildURL(ip, port, userName, password);
        }
        String httpUrl = String.valueOf(ElasticSearchUtil.VALID_HTTP_URL) + "/_cat/health?format=json";
        String resultStr = HttpUtil.getHttpMethodAuth(httpUrl, "GET", userName, password);
        JSONArray array = JSONArray.parseArray(resultStr);
        JSONObject jsonObj = array.getJSONObject(0);
        Iterator it = jsonObj.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            Map<String, Object> tempMap = new HashMap<>();
            tempMap.put("Variable_name", key);
            tempMap.put("Value", jsonObj.getString(key));
            tempMap.put("descript", "");
            list2.add(tempMap);
        }
        return list2;
    }

    public Map<String, Object> queryElasticSearch(NewQueryDTO dto) throws Exception {
        String resultStr;
        String httpUrl;
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> map12 = Constants.configStaticMap.get(dto.getDatabaseConfigId());
        String ip = new StringBuilder().append(map12.get("ip")).toString();
        String port = new StringBuilder().append(map12.get("port")).toString();
        String userName = new StringBuilder().append(map12.get("userName")).toString();
        String password = new StringBuilder().append(map12.get("password")).toString();
        if (StringUtils.isEmpty(ElasticSearchUtil.VALID_HTTP_URL)) {
            ElasticSearchUtil.returnVaildURL(ip, port, userName, password);
        }
        String httpUrl2 = String.valueOf(ElasticSearchUtil.VALID_HTTP_URL) + dto.getRequestURL();
        if (dto.getRequestURL().indexOf("/") != 0) {
            httpUrl2 = String.valueOf(ElasticSearchUtil.VALID_HTTP_URL) + "/" + dto.getRequestURL();
        }
        if ("GET".equals(dto.getRequestMethod())) {
            if (dto.getRequestURL().indexOf("?") > 0) {
                httpUrl = String.valueOf(httpUrl2) + "&format=json";
            } else {
                httpUrl = String.valueOf(httpUrl2) + "?format=json";
            }
            resultStr = HttpUtil.getHttpMethodAuth(httpUrl, "GET", userName, password);
        } else if ("DELETE".equals(dto.getRequestMethod())) {
            resultStr = HttpUtil.getHttpMethodAuth(httpUrl2, dto.getRequestMethod(), userName, password);
        } else {
            resultStr = HttpUtil.getHttpMethodNew(httpUrl2, dto.getRequestMethod(), ServletUtils.JSON_TYPE, dto.getRequestBody().trim(), userName, password);
        }
        resultMap.put("totalCount", 0);
        resultMap.put("result", resultStr);
        return resultMap;
    }
}
