package org.springframework.base.system.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.base.system.dao.ImpalaDao;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.utils.DateUtils;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/service/ImpalaService.class */
public class ImpalaService {
    @Autowired
    private ImpalaDao impalaDao;

    public List<Map<String, Object>> selectAllDataFromSQLForImpala(String databaseName, String databaseConfigId, String sql, int limitFrom, int pageSize) throws Exception {
        return this.impalaDao.selectAllDataFromSQLForImpala(databaseName, databaseConfigId, sql, limitFrom, pageSize);
    }

    public List<Map<String, Object>> getAllDataBaseForImpala(String databaseConfigId) throws Exception {
        return this.impalaDao.getAllDataBaseForImpala(databaseConfigId);
    }

    public List<Map<String, Object>> getAllViewsForImpala(String dbName, String databaseConfigId) throws Exception {
        return this.impalaDao.getAllViewsForImpala(dbName, databaseConfigId);
    }

    public Page<Map<String, Object>> getDataForImpala(Page<Map<String, Object>> page, String tableName, String dbName, String databaseConfigId) throws Exception {
        return this.impalaDao.getDataForImpala(page, tableName, dbName, databaseConfigId);
    }

    public List<Map<String, Object>> getAllTablesForImpala(String databaseName, String databaseConfigId) throws Exception {
        return this.impalaDao.getAllTablesForImpala(databaseName, databaseConfigId);
    }

    public List<Map<String, Object>> viewTableMessForImpala(String databaseName, String tableName, String databaseConfigId) throws Exception {
        JSONArray array = this.impalaDao.viewTableMessForImpala(databaseName, tableName, databaseConfigId);
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

    public Map<String, Object> executeSqlHaveResForImpala(String sql, String dbName, String databaseConfigId, Page<Map<String, Object>> page) {
        String status;
        String mess;
        Map<String, Object> map = new HashMap<>();
        Date b1 = new Date();
        try {
            page = this.impalaDao.executeSqlHaveResultForImpala(page, sql, dbName, databaseConfigId);
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
        map.put("operator", page.getOperator());
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    public Map<String, Object> executeSqlNotResForImpala(String sql, String dbName, String databaseConfigId) throws Exception {
        String status;
        String mess;
        Date b1 = new Date();
        int i = 0;
        try {
            i = this.impalaDao.executeSqlNotResForImpala(sql, dbName, databaseConfigId);
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

    public boolean dropTableForImpala(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.impalaDao.dropTableForImpala(databaseName, tableName, databaseConfigId);
    }

    public Map<String, Object> queryDatabaseStatusForImpala(String databaseName, String databaseConfigId) throws Exception {
        return this.impalaDao.queryDatabaseStatusForImpala(databaseName, databaseConfigId);
    }

    public List<Map<String, Object>> monitorItemValueForImpala(String databaseName, String databaseConfigId) throws Exception {
        return this.impalaDao.monitorItemValueForImpala(databaseName, databaseConfigId);
    }
}
