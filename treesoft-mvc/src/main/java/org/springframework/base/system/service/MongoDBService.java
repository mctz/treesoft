package org.springframework.base.system.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.base.system.dao.MongoDBDao;
import org.springframework.base.system.entity.DmsDto;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/service/MongoDBService.class */
public class MongoDBService {
    @Autowired
    private MongoDBDao mongoDBDao;

    public List<Map<String, Object>> getAllDataBaseForMongoDB(String databaseConfigId) throws Exception {
        return this.mongoDBDao.getAllDataBaseForMongoDB(databaseConfigId);
    }

    public List<Map<String, Object>> getAllTablesForMongoDB(String databaseName, String databaseConfigId) throws Exception {
        return this.mongoDBDao.getAllTablesForMongoDB(databaseName, databaseConfigId);
    }

    public Page<Map<String, Object>> getDataForMongoDB(Page<Map<String, Object>> page, String tableName, String databaseName, String databaseConfigId) throws Exception {
        return this.mongoDBDao.getDataForMongoDB(page, tableName, databaseName, databaseConfigId);
    }

    public Page<Map<String, Object>> getDataForMongoJson(Page<Map<String, Object>> page, String tableName, String databaseName, String databaseConfigId) throws Exception {
        return this.mongoDBDao.getDataForMongoJson(page, tableName, databaseName, databaseConfigId);
    }

    public int deleteRowsNewForMongoDB(String databaseName, String tableName, String primary_key, List<String> condition, String databaseConfigId, String username, String ip) throws Exception {
        return this.mongoDBDao.deleteRowsNewForMongoDB(databaseName, tableName, primary_key, condition, databaseConfigId, username, ip);
    }

    public int saveRowsForMongoDB(Map map, String databaseName, String tableName, String databaseConfigId, String username, String ip) throws Exception {
        return this.mongoDBDao.saveRowsForMongoDB(map, databaseName, tableName, databaseConfigId, username, ip);
    }

    public int updateRowsNewForMongoDB(String databaseName, String tableName, List<String> strList, String databaseConfigId) throws Exception {
        this.mongoDBDao.updateRowsNewForMongoDB(databaseName, tableName, strList, databaseConfigId);
        return 0;
    }

    public boolean dropTableForMongoDB(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.mongoDBDao.dropTableForMongoDB(databaseName, tableName, databaseConfigId);
    }

    public boolean dropDatabaseForMongoDB(String databaseName, String databaseConfigId) throws Exception {
        return this.mongoDBDao.dropDatabaseForMongoDB(databaseName, databaseConfigId);
    }

    public boolean clearTableForMongoDB(String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.mongoDBDao.clearTableForMongoDB(databaseName, tableName, databaseConfigId);
    }

    public List<Map<String, Object>> viewTableMessForMongoDB(String databaseName, String tableName, String databaseConfigId) throws Exception {
        List<Map<String, Object>> listAllColumn = this.mongoDBDao.viewTableMessForMongoDB(databaseName, tableName, databaseConfigId);
        return listAllColumn;
    }

    public int saveNewTableForMongoDB(JSONArray insertArray, String databaseName, String tableName, String databaseConfigId) throws Exception {
        return this.mongoDBDao.saveNewTableForMongoDB(insertArray, databaseName, tableName, databaseConfigId);
    }

    public Map<String, Object> queryDatabaseStatusForMongoDB(String databaseName, String databaseConfigId) throws Exception {
        return this.mongoDBDao.queryDatabaseStatusForMongoDB(databaseName, databaseConfigId);
    }

    public List<Map<String, Object>> selectAllDataFromSQLForMongoDB(String databaseName, String databaseConfigId, String sql, int limitFrom, int pageSize) throws Exception {
        return this.mongoDBDao.selectAllDataFromSQLForMongoDB(databaseName, databaseConfigId, sql, limitFrom, pageSize);
    }

    public List<Map<String, Object>> selectAllDataFromSQLForMongoDB_DS(String databaseName, String databaseConfigId, String sql, int limitFrom, int pageSize) throws Exception {
        return this.mongoDBDao.selectAllDataFromSQLForMongoDB_DS(databaseName, databaseConfigId, sql, limitFrom, pageSize);
    }

    public List<Map<String, Object>> selectAllDataFromSQLForMongoDBForExport(String databaseName, String databaseConfigId, String sql) throws Exception {
        return this.mongoDBDao.selectAllDataFromSQLForMongoDBForExport(databaseName, databaseConfigId, sql);
    }

    public List<Map<String, Object>> exportDataToSQLForMongoDB(String databaseName, String tableName, List<String> condition, String databaseConfigId) throws Exception {
        return this.mongoDBDao.exportDataToSQLForMongoDB(databaseName, tableName, condition, databaseConfigId);
    }

    public boolean insertByDataListForMongoDB(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId) throws Exception {
        return this.mongoDBDao.insertByDataListForMongoDB(dataList, databaseName, table, databaseConfigId);
    }

    public boolean updateByDataListForMongoDB(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.mongoDBDao.updateByDataListForMongoDB(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public boolean insertOrUpdateByDataListForMongoDB(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.mongoDBDao.insertOrUpdateByDataListForMongoDB(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public boolean insertOnlyByDataListForMongoDB(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.mongoDBDao.insertOnlyByDataListForMongoDB(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public boolean deleteByDataListForMongoDB(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        return this.mongoDBDao.deleteByDataListForMongoDB(dataList, databaseName, table, databaseConfigId, qualification);
    }

    public Map<String, Object> queryExplainSQLForMongoDB(DmsDto dto) throws Exception {
        Date b1 = new Date();
        Map<String, Object> resultMap = new HashMap<>();
        List<Map<String, Object>> list = this.mongoDBDao.queryExplainSQLForMongoDB(dto);
        Date b2 = new Date();
        List<Map<String, Object>> columnList = new ArrayList<>();
        if (list.size() > 0) {
            Map<String, Object> oneRow = list.get(0);
            Iterator<Map.Entry<String, Object>> it = oneRow.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> m = it.next();
                Map<String, Object> map2 = new HashMap<>();
                map2.put("field", m.getKey());
                map2.put("title", m.getKey());
                map2.put("sortable", false);
                columnList.add(map2);
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        String jsonfromList = "[" + mapper.writeValueAsString(columnList) + "]";
        long executeTime = b2.getTime() - b1.getTime();
        resultMap.put("rows", list);
        resultMap.put("total", Integer.valueOf(list.size()));
        resultMap.put("columns", jsonfromList);
        resultMap.put("primaryKey", null);
        resultMap.put("tableName", null);
        resultMap.put("totalCount", Integer.valueOf(list.size()));
        resultMap.put("time", Long.valueOf(executeTime));
        resultMap.put("mess", "执行成功");
        resultMap.put("status", "success");
        return resultMap;
    }

    public Map<String, Object> executeSqlNotResForMongoDB(String sql, String dbName, String databaseConfigId) {
        String status;
        String mess;
        Date b1 = new Date();
        int i = 0;
        try {
            i = this.mongoDBDao.executeSqlNotResForMongoDB(sql, dbName, databaseConfigId);
            mess = "执行成功！";
            status = "success";
        } catch (Exception e) {
            mess = e.getMessage();
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

    public String dataListToStringForMongoDB(List<Map<String, Object>> dataList) {
        String str = "";
        try {
            JSONArray json = JSONArray.parseArray(JSON.toJSONString(dataList));
            str = json.toString();
        } catch (Exception e) {
            LogUtil.e(e);
        }
        return str;
    }

    public Map<String, Object> executeSqlHaveResForMongoDB(String sql, String dbName, String databaseConfigId, Page<Map<String, Object>> page) {
        String status;
        String mess;
        Map<String, Object> map = new HashMap<>();
        Date b1 = new Date();
        try {
            page = this.mongoDBDao.executeSqlHaveResForMongoDB(page, sql, dbName, databaseConfigId);
            mess = "执行成功！";
            status = "success";
        } catch (Exception e) {
            LogUtil.e("MongoDB不支持该shell命令或执行shell命令出错, ", e);
            mess = "MongoDB不支持该shell命令或执行shell命令出错," + e.getMessage();
            status = "fail";
        }
        Date b2 = new Date();
        long y = b2.getTime() - b1.getTime();
        map.put("rows", page.getResult());
        map.put("total", Long.valueOf(page.getTotalCount()));
        map.put("columns", page.getColumns());
        map.put("primaryKey", page.getPrimaryKey());
        map.put("tableName", page.getTableName());
        map.put("totalCount", Long.valueOf(page.getTotalCount()));
        map.put("time", Long.valueOf(y));
        map.put("operator", page.getOperator());
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }
}
