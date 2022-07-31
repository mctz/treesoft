package org.springframework.base.system.dao;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.UUID;
import net.sf.json.JSONObject;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.base.system.dbUtils.MongoDBUtil;
import org.springframework.base.system.entity.DmsDto;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.utils.DateUtils;
import org.springframework.base.system.utils.ExcelUtil;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;

@Repository
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/dao/MongoDBDao.class */
public class MongoDBDao {
    @Autowired
    private ConfigDao configDao;
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private LogDao logDao;

    public List<Map<String, Object>> exportDataToSQLForMongoDB(String databaseName, String tableName, List<String> condition, String databaseConfigId) throws Exception {
        MongoDBUtil db = new MongoDBUtil(databaseConfigId);
        SimpleDateFormat sdf2 = new SimpleDateFormat(ExcelUtil.DATE_FORMAT);
        MongoCollection<Document> dbCollection = db.getCollection(databaseName, tableName);
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < condition.size(); i++) {
            String id = condition.get(i);
            Document doc = db.findById(dbCollection, id);
            Map<String, Object> map3 = new HashMap<>();
            Iterator it = doc.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry) it.next();
                String key = entry.getKey();
                Object param = entry.getValue();
                if (param instanceof String) {
                    map3.put(key, param);
                } else if (param instanceof Date) {
                    map3.put(key, sdf2.format(param));
                } else if (param instanceof ObjectId) {
                    map3.put(key, "ObjectId(\"" + param.toString() + "\")");
                } else if (param instanceof Document) {
                    map3.put(key, (Document) param);
                } else if (param instanceof Object) {
                    map3.put(key, param);
                } else {
                    map3.put(key, param);
                }
            }
            list.add(map3);
        }
        db.close();
        return list;
    }

    public List<Map<String, Object>> getAllDataBaseForMongoDB(String databaseConfigId) throws Exception {
        Map<String, Object> map12 = this.configDao.getConfigById(databaseConfigId);
        String databaseName = new StringBuilder().append(map12.get("databaseName")).toString();
        MongoDBUtil db = new MongoDBUtil(databaseConfigId);
        List<String> list = new ArrayList<>();
        try {
            list = db.getAllDBNames();
        } catch (Exception e) {
            list.add(databaseName);
        }
        List<Map<String, Object>> list2 = new ArrayList<>();
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            String str = it.next();
            Map<String, Object> map = new HashMap<>();
            map.put("SCHEMA_NAME", str);
            list2.add(map);
        }
        db.close();
        return list2;
    }

    public List<Map<String, Object>> getAllTablesForMongoDB(String databaseName, String databaseConfigId) throws Exception {
        MongoDBUtil db = new MongoDBUtil(databaseConfigId);
        List<String> list = db.getAllCollections(databaseName);
        List<Map<String, Object>> list2 = new ArrayList<>();
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            String tableName = it.next();
            Map<String, Object> map = new HashMap<>();
            map.put("TABLE_NAME", tableName);
            list2.add(map);
        }
        db.close();
        return list2;
    }

    public Integer getTableRowsForMongoDB(String dbName, String tableName, String databaseConfigId) throws Exception {
        MongoDBUtil db = new MongoDBUtil(databaseConfigId);
        MongoCollection<Document> dbCollection = db.getCollection(dbName, tableName);
        int rowCount = (int) dbCollection.countDocuments();
        return Integer.valueOf(rowCount);
    }

    public Page<Map<String, Object>> getDataForMongoDB(Page<Map<String, Object>> page, String tableName, String dbName, String databaseConfigId) throws Exception {
        String data_type;
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        MongoDBUtil db = new MongoDBUtil(databaseConfigId);
        SimpleDateFormat sdf2 = new SimpleDateFormat(ExcelUtil.DATE_FORMAT);
        MongoCollection<Document> dbCollection = db.getCollection(dbName, tableName);
        MongoCursor<Document> mongoCursor = db.findByPageForMongoDB(dbCollection, new BasicDBObject(), limitFrom, pageSize);
        int rowCount = (int) dbCollection.count();
        List<Map<String, Object>> list = new ArrayList<>();
        List<Map<String, Object>> tempList = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<>();
        map1.put("field", "treeSoftPrimaryKey");
        map1.put("checkbox", true);
        tempList.add(map1);
        TreeSet<String> ts = new TreeSet<>();
        while (mongoCursor.hasNext()) {
            Document doc = (Document) mongoCursor.next();
            Map<String, Object> map3 = new HashMap<>();
            Iterator it = doc.keySet().iterator();
            while (it.hasNext()) {
                String columnName = (String) it.next();
                if (!ts.contains(columnName)) {
                    ts.add(columnName);
                    Map<String, Object> map = new HashMap<>();
                    map.put("column_name", columnName);
                    Object param = doc.get(columnName);
                    if (columnName.equals("_id")) {
                        data_type = "String";
                    } else if (param instanceof String) {
                        data_type = "String";
                        map.put("editor", "text");
                    } else if (param instanceof Date) {
                        data_type = "Date";
                        map.put("editor", "datetimebox");
                    } else if (param instanceof Double) {
                        data_type = "Double";
                        map.put("editor", "numberbox");
                    } else if (param instanceof Boolean) {
                        data_type = "Boolean";
                        map.put("editor", "text");
                    } else if (param instanceof Integer) {
                        data_type = "Integer";
                        map.put("editor", "numberbox");
                    } else if (param instanceof Float) {
                        data_type = "Float";
                        map.put("editor", "numberbox");
                    } else if (param instanceof Long) {
                        data_type = "Long";
                        map.put("editor", "numberbox");
                    } else if (param instanceof Document) {
                        data_type = "Document";
                    } else if (param instanceof ObjectId) {
                        data_type = "ObjectId";
                    } else {
                        data_type = "Object";
                    }
                    map.put("data_type", data_type);
                    map.put("field", columnName);
                    map.put("title", columnName);
                    map.put("sortable", true);
                    tempList.add(map);
                }
            }
            Iterator it2 = doc.entrySet().iterator();
            while (it2.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry) it2.next();
                String key = entry.getKey();
                Object param2 = entry.getValue();
                if (param2 instanceof String) {
                    map3.put(key, param2);
                } else if (param2 instanceof Date) {
                    map3.put(key, sdf2.format(param2));
                } else if (param2 instanceof ObjectId) {
                    map3.put(key, "ObjectId(\"" + param2.toString() + "\")");
                } else if (param2 instanceof Document) {
                    map3.put(key, "{ " + ((Document) param2).size() + " field }");
                } else if (param2 instanceof Object) {
                    map3.put(key, param2);
                } else {
                    map3.put(key, param2);
                }
            }
            list.add(map3);
        }
        if (list.size() > 0) {
            new HashMap();
            Map<String, Object> map5 = list.get(0);
            Iterator<String> it3 = ts.iterator();
            while (it3.hasNext()) {
                String str = it3.next();
                if (map5.get(str) == null) {
                    map5.put(str, "");
                }
            }
            list.set(0, map5);
        }
        ObjectMapper mapper = new ObjectMapper();
        String jsonfromList = "[" + mapper.writeValueAsString(tempList) + "]";
        page.setTotalCount(rowCount);
        page.setResult(list);
        page.setColumns(jsonfromList);
        page.setPrimaryKey("_id");
        page.setTableName(tableName);
        page.setOperator("read");
        db.close();
        return page;
    }

    public boolean clearTableForMongoDB(String databaseName, String tableName, String databaseConfigId) throws Exception {
        MongoDBUtil db = new MongoDBUtil(databaseConfigId);
        db.deleteCollection(databaseName, tableName);
        LogUtil.i(String.valueOf(DateUtils.getDateTime()) + ",清空MongoDB表, tableName =" + tableName);
        db.close();
        return true;
    }

    public boolean dropTableForMongoDB(String databaseName, String tableName, String databaseConfigId) throws Exception {
        MongoDBUtil db = new MongoDBUtil(databaseConfigId);
        db.dropCollection(databaseName, tableName);
        LogUtil.i(String.valueOf(DateUtils.getDateTime()) + ",删除表,tableName =" + tableName);
        db.close();
        return true;
    }

    public boolean dropDatabaseForMongoDB(String databaseName, String databaseConfigId) throws Exception {
        MongoDBUtil db = new MongoDBUtil(databaseConfigId);
        db.dropDB(databaseName);
        LogUtil.i(String.valueOf(DateUtils.getDateTime()) + ",删除数据库,databaseName =" + databaseName);
        db.close();
        return true;
    }

    public Page<Map<String, Object>> getDataForMongoJson(Page<Map<String, Object>> page, String tableName, String dbName, String databaseConfigId) throws Exception {
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        if (limitFrom > 0) {
            int i = limitFrom - 1;
        }
        page.getOrderBy();
        page.getOrder();
        MongoDBUtil db = new MongoDBUtil(databaseConfigId);
        SimpleDateFormat sdf2 = new SimpleDateFormat(ExcelUtil.DATE_FORMAT);
        MongoCollection<Document> dbCollection = db.getCollection(dbName, tableName);
        MongoCursor<Document> mongoCursor = db.findByPageForMongoDB(dbCollection, new BasicDBObject(), pageNo, pageSize);
        int rowCount = (int) dbCollection.count();
        List<Map<String, Object>> list = new ArrayList<>();
        while (mongoCursor.hasNext()) {
            Document doc = (Document) mongoCursor.next();
            Map<String, Object> map3 = new HashMap<>();
            Iterator it = doc.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry) it.next();
                String key = entry.getKey();
                Object param = entry.getValue();
                if (param instanceof String) {
                    map3.put(key, param);
                } else if (param instanceof Date) {
                    map3.put(key, sdf2.format(param));
                } else if (param instanceof ObjectId) {
                    map3.put(key, "ObjectId(\"" + param.toString() + "\")");
                } else if (param instanceof Document) {
                    map3.put(key, (Document) param);
                } else if (param instanceof Object) {
                    map3.put(key, param);
                } else {
                    map3.put(key, param);
                }
            }
            list.add(map3);
        }
        page.setTotalCount(rowCount);
        page.setResult(list);
        page.setTableName(tableName);
        db.close();
        return page;
    }

    public Page<Map<String, Object>> executeSqlHaveResForMongoDB(Page<Map<String, Object>> page, String sql, String dbName, String databaseConfigId) throws Exception {
        String data_type;
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        String[] str2 = sql.split("\\.");
        String tableName = str2[1];
        String queryStr = str2[2].replace("find(", "");
        String queryStr2 = queryStr.substring(0, queryStr.lastIndexOf(")")).replaceAll(" ", "");
        if (queryStr2.equals("")) {
            queryStr2 = "{}";
        }
        JSONObject obj = JSONObject.fromObject(queryStr2);
        MongoDBUtil db = new MongoDBUtil(databaseConfigId);
        MongoCollection<Document> dbCollection = db.getCollection(dbName, tableName);
        SimpleDateFormat sdf2 = new SimpleDateFormat(ExcelUtil.DATE_FORMAT);
        Document filter = new Document(obj);
        MongoCursor<Document> mongoCursor = db.findByPageForMongoDB(dbCollection, filter, pageNo, pageSize);
        int rowCount = (int) dbCollection.count();
        List<Map<String, Object>> list = new ArrayList<>();
        List<Map<String, Object>> tempList = new ArrayList<>();
        while (mongoCursor.hasNext()) {
            Document doc = (Document) mongoCursor.next();
            Map<String, Object> map3 = new HashMap<>();
            if (tempList.size() == 0) {
                Map<String, Object> map1 = new HashMap<>();
                map1.put("field", "treeSoftPrimaryKey");
                map1.put("checkbox", true);
                tempList.add(map1);
                Iterator it = doc.keySet().iterator();
                while (it.hasNext()) {
                    String columnName = (String) it.next();
                    Map<String, Object> map = new HashMap<>();
                    map.put("column_name", columnName);
                    Object param = doc.get(columnName);
                    if (columnName.equals("_id")) {
                        data_type = "String";
                    } else if (param instanceof String) {
                        data_type = "String";
                        map.put("editor", "text");
                    } else if (param instanceof Date) {
                        data_type = "Date";
                        map.put("editor", "datetimebox");
                    } else if (param instanceof Double) {
                        data_type = "Double";
                        map.put("editor", "numberbox");
                    } else if (param instanceof Boolean) {
                        data_type = "Boolean";
                        map.put("editor", "text");
                    } else if (param instanceof Integer) {
                        data_type = "Integer";
                        map.put("editor", "numberbox");
                    } else if (param instanceof Float) {
                        data_type = "Float";
                        map.put("editor", "numberbox");
                    } else if (param instanceof Long) {
                        data_type = "Long";
                        map.put("editor", "numberbox");
                    } else if (param instanceof Document) {
                        data_type = "Document";
                    } else if (param instanceof ObjectId) {
                        data_type = "ObjectId";
                    } else {
                        data_type = "Object";
                    }
                    map.put("data_type", data_type);
                    map.put("field", columnName);
                    map.put("title", columnName);
                    map.put("sortable", true);
                    tempList.add(map);
                }
            }
            Iterator it2 = doc.entrySet().iterator();
            while (it2.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry) it2.next();
                String key = entry.getKey();
                Object param2 = entry.getValue();
                if (param2 instanceof String) {
                    map3.put(key, param2);
                } else if (param2 instanceof Date) {
                    map3.put(key, sdf2.format(param2));
                } else if (param2 instanceof ObjectId) {
                    map3.put(key, "ObjectId(\"" + param2.toString() + "\")");
                } else if (param2 instanceof Document) {
                    map3.put(key, "{ " + ((Document) param2).size() + " field }");
                } else if (param2 instanceof Object) {
                    map3.put(key, param2);
                } else {
                    map3.put(key, param2);
                }
            }
            list.add(map3);
        }
        ObjectMapper mapper = new ObjectMapper();
        String jsonfromList = "[" + mapper.writeValueAsString(tempList) + "]";
        page.setTotalCount(rowCount);
        page.setResult(list);
        page.setColumns(jsonfromList);
        page.setPrimaryKey("_id");
        page.setTableName(tableName);
        page.setOperator("read");
        db.close();
        return page;
    }

    public int deleteRowsNewForMongoDB(String databaseName, String tableName, String primary_key, List<String> condition, String databaseConfigId, String username, String ip) throws Exception {
        MongoDBUtil db = new MongoDBUtil(databaseConfigId);
        MongoCollection<Document> dbCollection = db.getCollection(databaseName, tableName);
        int y = 0;
        for (int i = 0; i < condition.size(); i++) {
            String id = condition.get(i).substring(12, condition.get(i).length() - 2).trim();
            this.logDao.saveLog("MongoDB删除数据行, _id=" + id, username, ip, databaseName, databaseConfigId);
            LogUtil.i(String.valueOf(DateUtils.getDateTime()) + ",MongoDB删除数据行, _id=" + id + "," + username + "," + ip + "," + databaseName);
            y += db.deleteById(dbCollection, id);
        }
        db.close();
        return y;
    }

    public int saveNewTableForMongoDB(JSONArray insertArray, String databaseName, String tableName, String databaseConfigId) throws Exception {
        MongoDBUtil db = new MongoDBUtil(databaseConfigId);
        db.createCollection(databaseName, tableName);
        db.close();
        return 0;
    }

    public List<Map<String, Object>> viewTableMessForMongoDB(String databaseName, String tableName, String databaseConfigId) throws Exception {
        MongoDBUtil db = new MongoDBUtil(databaseConfigId);
        MongoCollection<Document> document = db.getCollection(databaseName, tableName);
        List<Map<String, Object>> listAllColumn = new ArrayList<>();
        Map<String, Object> tempMap = new HashMap<>();
        tempMap.put("propName", "表名");
        tempMap.put("propValue", tableName);
        listAllColumn.add(tempMap);
        Map<String, Object> tempMap4 = new HashMap<>();
        tempMap4.put("propName", "总记录数");
        tempMap4.put("propValue", Long.valueOf(document.count()));
        listAllColumn.add(tempMap4);
        db.close();
        return listAllColumn;
    }

    public List<Map<String, Object>> selectAllDataFromSQLForMongoDB(String databaseName, String databaseConfigId, String sql, int limitFrom, int pageSize) throws Exception {
        String[] str2 = sql.split("\\.");
        String tableName = str2[1];
        String queryStr = str2[2].replace("find(", "");
        String queryStr2 = queryStr.substring(0, queryStr.lastIndexOf(")"));
        if (queryStr2.equals("")) {
            queryStr2 = "{}";
        }
        JSONObject obj = JSONObject.fromObject(queryStr2);
        MongoDBUtil mdb = new MongoDBUtil(databaseConfigId);
        MongoCollection<Document> dbCollection = mdb.getCollection(databaseName, tableName);
        SimpleDateFormat sdf2 = new SimpleDateFormat(ExcelUtil.DATE_FORMAT);
        Document filter = new Document(obj);
        MongoCursor<Document> mongoCursor = mdb.findByPageForMongoDB(dbCollection, filter, limitFrom, pageSize);
        List<Map<String, Object>> list = new ArrayList<>();
        while (mongoCursor.hasNext()) {
            Document doc = (Document) mongoCursor.next();
            Map<String, Object> map3 = new HashMap<>();
            Iterator it = doc.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry) it.next();
                String key = entry.getKey();
                Object param = entry.getValue();
                if (param instanceof String) {
                    map3.put(key, param);
                } else if (param instanceof Date) {
                    map3.put(key, sdf2.format(param));
                } else if (param instanceof ObjectId) {
                    map3.put(key, "ObjectId(\"" + param.toString() + "\")");
                } else if (param instanceof Document) {
                    map3.put(key, "{ " + ((Document) param).size() + " field }");
                } else if (param instanceof Object) {
                    map3.put(key, param);
                } else {
                    map3.put(key, param);
                }
            }
            list.add(map3);
        }
        mdb.close();
        return list;
    }

    public List<Map<String, Object>> selectAllDataFromSQLForMongoDB_DS(String databaseName, String databaseConfigId, String sql, int limitFrom, int pageSize) throws Exception {
        String[] str2 = sql.split("\\.");
        String tableName = str2[1];
        String queryStr = str2[2].replace("find(", "");
        String queryStr2 = queryStr.substring(0, queryStr.lastIndexOf(")"));
        if (queryStr2.equals("")) {
            queryStr2 = "{}";
        }
        JSONObject obj = JSONObject.fromObject(queryStr2);
        MongoDBUtil mdb = new MongoDBUtil(databaseConfigId);
        MongoCollection<Document> dbCollection = mdb.getCollection(databaseName, tableName);
        SimpleDateFormat sdf2 = new SimpleDateFormat(ExcelUtil.DATE_FORMAT);
        Document filter = new Document(obj);
        MongoCursor<Document> mongoCursor = mdb.findByPageForMongoDB(dbCollection, filter, limitFrom, pageSize);
        List<Map<String, Object>> list = new ArrayList<>();
        while (mongoCursor.hasNext()) {
            Document doc = (Document) mongoCursor.next();
            Map<String, Object> map3 = new HashMap<>();
            Iterator it = doc.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry) it.next();
                String key = entry.getKey();
                Object param = entry.getValue();
                if (!key.equals("_id")) {
                    if (param instanceof String) {
                        map3.put(key, param);
                    } else if (param instanceof Date) {
                        map3.put(key, sdf2.format(param));
                    } else if (param instanceof ObjectId) {
                        map3.put(key, "ObjectId(\"" + param.toString() + "\")");
                    } else if (param instanceof Document) {
                        map3.put(key, "{ " + ((Document) param).size() + " field }");
                    } else if (param instanceof Object) {
                        map3.put(key, param);
                    } else {
                        map3.put(key, param);
                    }
                }
            }
            list.add(map3);
        }
        mdb.close();
        return list;
    }

    public int saveRowsForMongoDB(Map<String, String> map, String databaseName, String tableName, String databaseConfigId, String username, String ip) throws Exception {
        boolean isUUID;
        MongoDBUtil db = new MongoDBUtil(databaseConfigId);
        MongoCollection<Document> collection = db.getCollection(databaseName, tableName);
        Document myDoc = (Document) collection.find().first();
        if (myDoc == null) {
            myDoc = new Document();
        }
        Object param = myDoc.get("_id");
        if (param instanceof String) {
            isUUID = true;
        } else {
            isUUID = false;
        }
        if (isUUID) {
            String uuid = UUID.randomUUID().toString().replaceAll(RuleBasedTransactionAttribute.PREFIX_ROLLBACK_RULE, "");
            map.put("_id", uuid);
        }
        JSONObject obj = JSONObject.fromObject(map);
        Document document = new Document(obj);
        collection.insertOne(document);
        this.logDao.saveLog(" insertOne " + map.toString(), username, ip, databaseName, databaseConfigId);
        db.close();
        return 0;
    }

    public int updateRowsNewForMongoDB(String databaseName, String tableName, List<String> strList, String databaseConfigId) throws Exception {
        MongoDBUtil db = new MongoDBUtil(databaseConfigId);
        MongoCollection<Document> collection = db.getCollection(databaseName, tableName);
        Iterator<String> it = strList.iterator();
        while (it.hasNext()) {
            String str = it.next();
            String[] sss = str.split("#@@#");
            JSONObject obj = JSONObject.fromObject(sss[0]);
            Document newdoc = new Document(obj);
            String id = sss[1];
            db.updateById(collection, id, newdoc);
        }
        db.close();
        return 0;
    }

    public int executeSqlNotResForMongoDB(String sql, String databaseName, String databaseConfigId) throws Exception {
        MongoDBUtil db = new MongoDBUtil(databaseConfigId);
        int i = 1;
        String[] str2 = sql.split("\\.");
        String tableName = str2[1];
        MongoCollection<Document> dbCollection = db.getCollection(databaseName, tableName);
        if ("".equals("")) {
        }
        if (sql.indexOf(".insert(") > 0) {
            String insertStr = sql.replace("db." + tableName + ".insert(", "");
            JSONObject obj = JSONObject.fromObject(insertStr.substring(0, insertStr.lastIndexOf(")")));
            Document doc = new Document(obj);
            dbCollection.insertOne(doc);
        }
        if (sql.indexOf(".update(") > 0) {
            String updateStr = sql.replace("db." + tableName + ".update(", "");
            String[] str = updateStr.substring(0, updateStr.lastIndexOf(")")).split(",");
            if (str.length != 2) {
                throw new Exception("输入的更新语句格式有误。");
            }
            BsonDocument arg0 = BsonDocument.parse(str[0]);
            BsonDocument arg1 = BsonDocument.parse(str[1]);
            dbCollection.updateOne(arg0, arg1);
        }
        if (sql.indexOf(".remove(") > 0) {
            String removeStr = sql.replace("db." + tableName + ".remove(", "");
            JSONObject obj2 = JSONObject.fromObject(removeStr.substring(0, removeStr.lastIndexOf(")")));
            Document doc2 = new Document(obj2);
            DeleteResult ss = dbCollection.deleteMany(doc2);
            i = (int) ss.getDeletedCount();
        }
        db.close();
        return i;
    }

    public Integer queryDatabaseStatusForMongoDBConnection(String databaseName, String databaseConfigId) throws Exception {
        MongoDBUtil db = new MongoDBUtil(databaseConfigId);
        CommandResult stats2 = db.getMongoStatus2(databaseName);
        if (stats2 == null) {
            LogUtil.e("取得MongoDB数据库状态为空1。");
            throw new Exception("需要root角色才能查询状态信息。");
        }
        Object tempConnections = stats2.get("connections");
        JSONObject jb = JSONObject.fromObject(tempConnections);
        return Integer.valueOf(Integer.parseInt(jb.get("current").toString()));
    }

    public Map<String, Object> queryDatabaseStatusForMongoDB(String databaseName, String databaseConfigId) throws Exception {
        MongoDBUtil db = new MongoDBUtil(databaseConfigId);
        CommandResult stats = db.getMongoStatus(databaseName);
        CommandResult stats2 = db.getMongoStatus2(databaseName);
        db.close();
        if (stats == null) {
            LogUtil.e("取得MongoDB数据库状态为空1。");
            throw new Exception("需要root角色才能查询状态信息。");
        } else if (stats2 == null) {
            LogUtil.e("取得MongoDB数据库状态为空, 需要为MongoDB用户赋予root角色才能执行db.serverStatus()命令。");
            throw new Exception("需要root角色才能查询状态信息。");
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("collections", stats.get("collections"));
            map.put("dataSize", stats.get("dataSize"));
            map.put("indexes", stats.get("indexes"));
            Object tempConnections = stats2.get("connections");
            JSONObject jb = JSONObject.fromObject(tempConnections);
            map.put("connections", jb.get("current"));
            map.put("uptime", stats2.get("uptime"));
            Object tempOpcounters2 = stats2.get("opcounters");
            JSONObject jb2 = JSONObject.fromObject(tempOpcounters2);
            map.put("insert", jb2.get("insert"));
            map.put("query", jb2.get("query"));
            map.put("update", jb2.get("update"));
            map.put("delete", jb2.get("delete"));
            Object tempOpcounters3 = stats2.get("network");
            JSONObject jb3 = JSONObject.fromObject(tempOpcounters3);
            map.put("bytesIn", jb3.get("bytesIn"));
            map.put("bytesOut", jb3.get("bytesOut"));
            Object tempOpcounters4 = stats2.get("mem");
            JSONObject jb4 = JSONObject.fromObject(tempOpcounters4);
            map.put("resident", jb4.get("resident"));
            map.put("virtual", jb4.get("virtual"));
            Object tempOpcounters5 = stats2.get("globalLock");
            JSONObject jb5 = JSONObject.fromObject(tempOpcounters5);
            JSONObject jb6 = jb5.getJSONObject("currentQueue");
            map.put("globalLock_total", jb6.get("total"));
            map.put("globalLock_readers", jb6.get("readers"));
            map.put("globalLock_writers", jb6.get("writers"));
            return map;
        }
    }

    public int executeSqlNotResForMongoDBInsert(List<Document> list, String databaseName, String tableName, String databaseConfigId) throws Exception {
        MongoDBUtil db = new MongoDBUtil(databaseConfigId);
        MongoCollection<Document> dbCollection = db.getCollection(databaseName, tableName);
        dbCollection.insertMany(list);
        db.close();
        return 1;
    }

    public List<Map<String, Object>> selectAllDataFromSQLForMongoDBForExport(String databaseName, String databaseConfigId, String sql) throws Exception {
        String[] str2 = sql.split("\\.");
        String tableName = str2[1];
        String queryStr = str2[2].replace("find(", "");
        String queryStr2 = queryStr.substring(0, queryStr.lastIndexOf(")"));
        if (queryStr2.equals("")) {
            queryStr2 = "{}";
        }
        JSONObject obj = JSONObject.fromObject(queryStr2);
        MongoDBUtil db = new MongoDBUtil(databaseConfigId);
        MongoCollection<Document> dbCollection = db.getCollection(databaseName, tableName);
        SimpleDateFormat sdf2 = new SimpleDateFormat(ExcelUtil.DATE_FORMAT);
        Document filter = new Document(obj);
        MongoCursor<Document> mongoCursor = db.find(dbCollection, filter);
        List<Map<String, Object>> list = new ArrayList<>();
        while (mongoCursor.hasNext()) {
            Document doc = (Document) mongoCursor.next();
            Map<String, Object> map3 = new HashMap<>();
            Iterator it = doc.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry) it.next();
                String key = entry.getKey();
                Object param = entry.getValue();
                if (param instanceof String) {
                    map3.put(key, param);
                } else if (param instanceof Date) {
                    map3.put(key, sdf2.format(param));
                } else if (param instanceof ObjectId) {
                    map3.put(key, "ObjectId(\"" + param.toString() + "\")");
                } else if (param instanceof Document) {
                    map3.put(key, ((Document) param).toJson());
                } else if (param instanceof Object) {
                    map3.put(key, param);
                } else {
                    map3.put(key, param);
                }
            }
            list.add(map3);
        }
        db.close();
        return list;
    }

    public boolean insertByDataListForMongoDB(List<Map<String, Object>> dataList, String databaseName, String tableName, String databaseConfigId) throws Exception {
        List<Document> list = new ArrayList<>();
        int num = 0;
        for (int i = 0; i < dataList.size(); i++) {
            Map<String, Object> map4 = dataList.get(i);
            JSONObject obj = JSONObject.fromObject(JSONArray.toJSONString(map4));
            Document doc = new Document(obj);
            list.add(doc);
            num++;
            if (num > 2000) {
                executeSqlNotResForMongoDBInsert(list, databaseName, tableName, databaseConfigId);
                list.clear();
                num = 0;
            }
        }
        if (list.size() > 0) {
            executeSqlNotResForMongoDBInsert(list, databaseName, tableName, databaseConfigId);
            list.clear();
            return true;
        }
        return true;
    }

    public boolean updateByDataListForMongoDB(List<Map<String, Object>> dataList, String databaseName, String tableName, String databaseConfigId, String qualification) throws Exception {
        MongoDBUtil db = new MongoDBUtil(databaseConfigId);
        MongoCollection<Document> collection = db.getCollection(databaseName, tableName);
        String[] whereColumn = qualification.split(",");
        if (whereColumn.length == -1) {
            return false;
        }
        Iterator<Map<String, Object>> it = dataList.iterator();
        while (it.hasNext()) {
            Map<String, Object> map4 = it.next();
            String id = new StringBuilder().append(map4.get(whereColumn[0])).toString();
            map4.remove(whereColumn[0]);
            JSONObject obj = JSONObject.fromObject(map4);
            Document newdoc = new Document(obj);
            db.updateById(collection, id, newdoc);
        }
        db.close();
        return true;
    }

    public boolean insertOrUpdateByDataListForMongoDB(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        MongoDBUtil db = new MongoDBUtil(databaseConfigId);
        MongoCollection<Document> collection = db.getCollection(databaseName, table);
        List<Map<String, Object>> insertDataList = new ArrayList<>();
        List<Map<String, Object>> updateDataList = new ArrayList<>();
        String[] whereColumn = qualification.split(",");
        Iterator<Map<String, Object>> it = dataList.iterator();
        while (it.hasNext()) {
            Map<String, Object> map4 = it.next();
            String _id = whereColumn[0];
            Document doc = db.findById(collection, _id);
            if (doc.isEmpty()) {
                insertDataList.add(map4);
            } else {
                updateDataList.add(map4);
            }
        }
        if (insertDataList.size() > 0) {
            insertByDataListForMongoDB(insertDataList, databaseName, table, databaseConfigId);
        }
        if (updateDataList.size() > 0) {
            updateByDataListForMongoDB(updateDataList, databaseName, table, databaseConfigId, qualification);
            return true;
        }
        return true;
    }

    public boolean insertOnlyByDataListForMongoDB(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        MongoDBUtil db = new MongoDBUtil(databaseConfigId);
        MongoCollection<Document> collection = db.getCollection(databaseName, table);
        List<Map<String, Object>> insertDataList = new ArrayList<>();
        String[] whereColumn = qualification.split(",");
        Iterator<Map<String, Object>> it = dataList.iterator();
        while (it.hasNext()) {
            Map<String, Object> map4 = it.next();
            String _id = whereColumn[0];
            Document doc = db.findById(collection, _id);
            if (doc.isEmpty()) {
                insertDataList.add(map4);
            }
        }
        if (insertDataList.size() > 0) {
            insertByDataListForMongoDB(insertDataList, databaseName, table, databaseConfigId);
            return true;
        }
        return true;
    }

    public boolean deleteByDataListForMongoDB(List<Map<String, Object>> dataList, String databaseName, String table, String databaseConfigId, String qualification) throws Exception {
        MongoDBUtil db = new MongoDBUtil(databaseConfigId);
        MongoCollection<Document> collection = db.getCollection(databaseName, table);
        String[] whereColumn = qualification.split(",");
        Iterator<Map<String, Object>> it = dataList.iterator();
        while (it.hasNext()) {
            it.next();
            String _id = whereColumn[0];
            db.findById(collection, _id);
            db.deleteById(collection, _id);
        }
        return true;
    }

    public List<Map<String, Object>> queryExplainSQLForMongoDB(DmsDto dto) throws Exception {
        String[] str2 = dto.getSql().split("\\.");
        String tableName = str2[1];
        String queryStr = str2[2].replace("find(", "");
        String queryStr2 = queryStr.substring(0, queryStr.lastIndexOf(")")).replaceAll(" ", "");
        if (queryStr2.equals("")) {
            queryStr2 = "{}";
        }
        JSONObject obj = JSONObject.fromObject(queryStr2);
        MongoDBUtil db = new MongoDBUtil(dto.getDatabaseConfigId());
        MongoCollection<Document> dbCollection = db.getCollection(dto.getDatabaseName(), tableName);
        Document filter = new Document(obj);
        db.findByPageForMongoDB(dbCollection, filter, 1, 30);
        return null;
    }
}
