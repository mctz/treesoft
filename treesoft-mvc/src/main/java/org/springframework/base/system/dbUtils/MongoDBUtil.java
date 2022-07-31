package org.springframework.base.system.dbUtils;

import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.base.system.utils.CryptoUtil;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/dbUtils/MongoDBUtil.class */
public class MongoDBUtil {
    private static MongoClient mongoClient;

    public MongoDBUtil(String databaseConfigId) throws Exception {
        MongoClientURI uri;
        DBUtil db = new DBUtil();
        String sql = " select id, name,database_type as databaseType , database_name as databaseName, user_name as userName , password, port, ip ,url ,is_default as isDefault from  treesoft_config where id='" + databaseConfigId + "'";
        List<Map<String, Object>> list = db.executeQuery2(sql);
        Map<String, Object> map0 = list.get(0);
        String ip = new StringBuilder().append(map0.get("ip")).toString();
        String port = new StringBuilder().append(map0.get("port")).toString();
        String userName = new StringBuilder().append(map0.get("userName")).toString();
        String databaseName = new StringBuilder().append(map0.get("databaseName")).toString();
        String password = CryptoUtil.decode(new StringBuilder().append(map0.get("password")).toString());
        password = password.indexOf("`") > 0 ? password.split("`")[1] : password;
        new StringBuilder().append(map0.get("databaseType")).toString();
        if (userName.equals("")) {
            uri = new MongoClientURI("mongodb://" + ip + ":" + port);
        } else {
            uri = new MongoClientURI("mongodb://" + userName + ":" + password + "@" + ip + ":" + port + "/" + databaseName);
        }
        mongoClient = new MongoClient(uri);
    }

    public MongoDatabase getDB(String dbName) {
        if (dbName != null && !"".equals(dbName)) {
            MongoDatabase database = mongoClient.getDatabase(dbName);
            return database;
        }
        return null;
    }

    public MongoCollection<Document> getCollection(String dbName, String collName) {
        if (collName == null || "".equals(collName) || dbName == null || "".equals(dbName)) {
            return null;
        }
        MongoCollection<Document> collection = mongoClient.getDatabase(dbName).getCollection(collName);
        return collection;
    }

    public List<String> getAllCollections(String dbName) {
        MongoIterable<String> colls = getDB(dbName).listCollectionNames();
        List<String> _list = new ArrayList<>();
        MongoCursor it = colls.iterator();
        while (it.hasNext()) {
            String name = (String) it.next();
            _list.add(name);
        }
        return _list;
    }

    public CommandResult getMongoStatus(String dbName) throws Exception {
        try {
            CommandResult resultSet = mongoClient.getDB(dbName).getStats();
            return resultSet;
        } catch (Exception e) {
            throw new Exception("需要root角色才能查询状态信息。");
        }
    }

    public CommandResult getMongoStatus2(String dbName) {
        CommandResult resultSet2 = mongoClient.getDB(dbName).command("serverStatus");
        return resultSet2;
    }

    public List<String> getAllDBNames() {
        MongoIterable<String> colls = mongoClient.listDatabaseNames();
        List<String> _list = new ArrayList<>();
        MongoCursor it = colls.iterator();
        while (it.hasNext()) {
            String name = (String) it.next();
            _list.add(name);
        }
        return _list;
    }

    public void dropDB(String dbName) {
        getDB(dbName).drop();
    }

    public Document runCommand(String dbName, String sql) {
        Document doc = getDB(dbName).runCommand(new BasicDBObject());
        return doc;
    }

    public Document findById(MongoCollection<Document> coll, String id) {
        Document myDoc;
        new Document();
        if (id.indexOf("ObjectId") >= 0) {
            ObjectId object = new ObjectId(id.replace("ObjectId(\"", "").replace("\")", ""));
            Document filter = new Document();
            filter.append("_id", object);
            myDoc = (Document) coll.find(filter).first();
        } else {
            Document filter2 = new Document();
            filter2.append("_id", id);
            myDoc = (Document) coll.find(filter2).first();
        }
        return myDoc;
    }

    public int getCount(MongoCollection<Document> coll) {
        int count = (int) coll.count();
        return count;
    }

    public int getCountForFilter(MongoCollection<Document> coll, Bson filter) {
        MongoCursor<Document> cursor = coll.find(filter).iterator();
        int count = 0;
        while (cursor.hasNext()) {
            count++;
        }
        return count;
    }

    public MongoCursor<Document> find(MongoCollection<Document> coll, Bson filter) {
        return coll.find(filter).iterator();
    }

    public MongoCursor<Document> findByPageForMongoDB(MongoCollection<Document> coll, Bson filter, int limitFrom, int pageSize) {
        return coll.find(filter).skip(limitFrom).limit(pageSize).iterator();
    }

    public int deleteById(MongoCollection<Document> coll, String id) {
        if (id.indexOf("ObjectId") >= 0) {
            ObjectId object = new ObjectId(id.replace("ObjectId(\"", "").replace("\")", ""));
            Document filter = new Document();
            filter.append("_id", object);
            coll.deleteOne(filter);
        } else {
            Document filter2 = new Document();
            filter2.append("_id", id);
            coll.deleteOne(filter2);
        }
        return 0;
    }

    public Document updateById(MongoCollection<Document> coll, String id, Document newdoc) {
        if (id.indexOf("ObjectId") >= 0) {
            ObjectId _idobj = new ObjectId(id.replace("ObjectId(\"", "").replace("\")", ""));
            Bson filter = Filters.eq("_id", _idobj);
            coll.updateOne(filter, new Document("$set", newdoc));
        } else {
            Bson filter2 = Filters.eq("_id", id);
            coll.updateOne(filter2, new Document("$set", newdoc));
        }
        return newdoc;
    }

    public void dropCollection(String databaseName, String tableName) {
        getDB(databaseName).getCollection(tableName).drop();
    }

    public void createCollection(String databaseName, String tableName) {
        getDB(databaseName).createCollection(tableName);
    }

    public void deleteCollection(String databaseName, String tableName) {
        getDB(databaseName).getCollection(tableName).drop();
    }

    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
        }
    }

    public static void main(String[] args) {
    }

    public static boolean testConnection(String databaseType2, String databaseName2, String ip2, String port2, String user2, String pass2) {
        MongoClientURI uri;
        if (user2.equals("")) {
            uri = new MongoClientURI("mongodb://" + ip2 + ":" + port2);
        } else {
            uri = new MongoClientURI("mongodb://" + user2 + ":" + pass2 + "@" + ip2 + ":" + port2);
        }
        MongoClient mongoClient2 = new MongoClient(uri);
        MongoDatabase database = mongoClient2.getDatabase(databaseName2);
        if (database != null) {
            return true;
        }
        return false;
    }
}
