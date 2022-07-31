package org.springframework.base.system.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.base.system.dao.ESDao;
import org.springframework.base.system.entity.NewQueryDTO;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.utils.DateUtils;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/service/ESService.class */
public class ESService {
    @Autowired
    private ESDao esDao;

    public List<Map<String, Object>> selectAllDataFromSQLForES(String databaseName, String databaseConfigId, String sql, int limitFrom, int pageSize) throws Exception {
        return this.esDao.selectAllDataFromSQLForES(databaseName, databaseConfigId, sql, limitFrom, pageSize);
    }

    public List<Map<String, Object>> getAllDataBaseForES(String databaseConfigId) throws Exception {
        return this.esDao.getAllDataBaseForES(databaseConfigId);
    }

    public Page<Map<String, Object>> getDataForES(Page<Map<String, Object>> page, String tableName, String dbName, String databaseConfigId) throws Exception {
        return this.esDao.getDataForES(page, tableName, dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getAllIndexsForES(String databaseName, String databaseConfigId) throws Exception {
        return this.esDao.getAllIndexsForES4HTTP(databaseName, databaseConfigId);
    }

    public List<Map<String, Object>> viewTableMessForES(String databaseName, String tableName, String databaseConfigId) throws Exception {
        JSONArray array = this.esDao.viewTableMessForES(databaseName, tableName, databaseConfigId);
        List<Map<String, Object>> listAllColumn2 = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            JSONObject map = array.getJSONObject(i);
            Map<String, Object> tempMap1 = new HashMap<>();
            tempMap1.put("propName", "health");
            tempMap1.put("propValue", String.valueOf(map.getString("health")) + "<font color='" + map.getString("health") + "'> ██ </font>");
            listAllColumn2.add(tempMap1);
            Map<String, Object> tempMap2 = new HashMap<>();
            tempMap2.put("propName", "status");
            tempMap2.put("propValue", map.getString("status"));
            listAllColumn2.add(tempMap2);
            Map<String, Object> tempMap3 = new HashMap<>();
            tempMap3.put("propName", "index");
            tempMap3.put("propValue", map.getString("index"));
            listAllColumn2.add(tempMap3);
            Map<String, Object> tempMap4 = new HashMap<>();
            tempMap4.put("propName", "uuid");
            tempMap4.put("propValue", map.getString("uuid"));
            listAllColumn2.add(tempMap4);
            Map<String, Object> tempMap5 = new HashMap<>();
            tempMap5.put("propName", "pri");
            tempMap5.put("propValue", map.getString("pri"));
            listAllColumn2.add(tempMap5);
            Map<String, Object> tempMap6 = new HashMap<>();
            tempMap6.put("propName", "rep");
            tempMap6.put("propValue", map.getString("rep"));
            listAllColumn2.add(tempMap6);
            Map<String, Object> tempMap7 = new HashMap<>();
            tempMap7.put("propName", "docs.count");
            tempMap7.put("propValue", map.getString("docs.count"));
            listAllColumn2.add(tempMap7);
            Map<String, Object> tempMap8 = new HashMap<>();
            tempMap8.put("propName", "docs.deleted");
            tempMap8.put("propValue", map.getString("docs.deleted"));
            listAllColumn2.add(tempMap8);
            Map<String, Object> tempMap9 = new HashMap<>();
            tempMap9.put("propName", "store.size");
            tempMap9.put("propValue", map.getString("store.size"));
            listAllColumn2.add(tempMap9);
            Map<String, Object> tempMap10 = new HashMap<>();
            tempMap10.put("propName", "pri.store.size");
            tempMap10.put("propValue", map.getString("pri.store.size"));
            listAllColumn2.add(tempMap10);
        }
        return listAllColumn2;
    }

    public Map<String, Object> executeSqlHaveResForES(String sql, String dbName, String databaseConfigId, Page<Map<String, Object>> page) {
        String status;
        String mess;
        Map<String, Object> map = new HashMap<>();
        Date b1 = new Date();
        try {
            page = this.esDao.executeSqlHaveResultForES(page, sql, dbName, databaseConfigId);
            mess = "执行成功！";
            status = "success";
        } catch (Exception e) {
            LogUtil.e("SQL执行出错,", e);
            mess = e.getMessage();
            status = "fail";
        }
        Date b2 = new Date();
        long y = b2.getTime() - b1.getTime();
        map.put("rows", page.getResult());
        map.put("total", Long.valueOf(page.getTotalCount()));
        map.put("columns", page.getColumns());
        map.put("tableName", "");
        map.put("totalCount", Long.valueOf(page.getTotalCount()));
        map.put("time", Long.valueOf(y));
        map.put("mess", mess);
        map.put("status", status);
        map.put("operator", page.getOperator());
        return map;
    }

    public Map<String, Object> executeSqlNotResForES(String sql, String dbName, String databaseConfigId) throws Exception {
        String status;
        String mess;
        Date b1 = new Date();
        int i = 0;
        try {
            i = this.esDao.executeSqlNotResForES(sql, dbName, databaseConfigId);
            mess = "执行成功！";
            LogUtil.i(String.valueOf(DateUtils.getDateTime()) + ",SQL执行 ,dbName=" + dbName + ",SQL=" + sql);
            status = "success";
        } catch (Exception e) {
            LogUtil.e("SQL执行出错, ", e);
            mess = "执行失败, " + e.getMessage();
            status = "fail";
        }
        Date b2 = new Date();
        long y = b2.getTime() - b1.getTime();
        Map<String, Object> map = new HashMap<>();
        map.put("totalCount", Integer.valueOf(i));
        map.put("time", Long.valueOf(y));
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    public boolean dropTableForES(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.esDao.dropTableForES(databaseName, tableName, databaseConfigId);
    }

    public Map<String, Object> queryDatabaseStatusForES(String databaseName, String databaseConfigId) throws Exception {
        return this.esDao.queryDatabaseStatusForES(databaseName, databaseConfigId);
    }

    public List<Map<String, Object>> monitorItemValueForES(String databaseName, String databaseConfigId) throws Exception {
        return this.esDao.monitorItemValueForES(databaseName, databaseConfigId);
    }

    public Map<String, Object> queryElasticSearch(NewQueryDTO dto) throws Exception {
        Date b1 = new Date();
        Map<String, Object> resultMap = this.esDao.queryElasticSearch(dto);
        Date b2 = new Date();
        long y = b2.getTime() - b1.getTime();
        resultMap.put("time", Long.valueOf(y));
        resultMap.put("mess", "");
        resultMap.put("status", "");
        return resultMap;
    }
}
