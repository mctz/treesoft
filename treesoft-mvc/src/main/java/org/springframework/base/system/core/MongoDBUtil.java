package org.springframework.base.system.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.base.system.utils.CryptoUtil;

import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;

public class MongoDBUtil
{
    private static MongoClient mongoClient;
    
    public MongoDBUtil(String databaseConfigId)
    {
        MongoClientURI uri = null;
        String sql = " select databaseName, userName,  password, port, ip ,url from  treesoft_config where id=?";
        Map<String, Object> map0 = new SysDataBaseUtil().queryForMap(sql, databaseConfigId);
        String ip = (String)map0.get("ip");
        String port = (String)map0.get("port");
        String userName = (String)map0.get("userName");
        String databaseName = (String)map0.get("databaseName");
        String password = CryptoUtil.decode(map0.get("password").toString());
        if (password.indexOf("`") > 0)
        {
            password = password.split("`")[1];
        }
        if (userName.equals(""))
        {
            uri = new MongoClientURI("mongodb://" + ip + ":" + port);
        }
        else
        {
            uri = new MongoClientURI("mongodb://" + userName + ":" + password + "@" + ip + ":" + port + "/?authSource=" + databaseName + "&ssl=false");
        }
        mongoClient = new MongoClient(uri);
    }
    
    public MongoDatabase getDB(String dbName)
    {
        if ((dbName != null) && (!"".equals(dbName)))
        {
            MongoDatabase database = mongoClient.getDatabase(dbName);
            return database;
        }
        return null;
    }
    
    public MongoCollection<Document> getCollection(String dbName, String collName)
    {
        if ((collName == null) || ("".equals(collName)))
        {
            return null;
        }
        if ((dbName == null) || ("".equals(dbName)))
        {
            return null;
        }
        MongoCollection<Document> collection = mongoClient.getDatabase(dbName).getCollection(collName);
        return collection;
    }
    
    public List<String> getAllCollections(String dbName)
    {
        MongoIterable<String> colls = getDB(dbName).listCollectionNames();
        List<String> list = new ArrayList<>();
        for (Iterator<String> iterator = colls.iterator(); iterator.hasNext();)
        {
            String name = iterator.next();
            list.add(name);
        }
        return list;
    }
    
    public CommandResult getMongoStatus(String dbName)
    {
        CommandResult resultSet = mongoClient.getDB(dbName).getStats();
        return resultSet;
    }
    
    public CommandResult getMongoStatus2(String dbName)
    {
        CommandResult resultSet2 = mongoClient.getDB(dbName).command("serverStatus");
        return resultSet2;
    }
    
    public List<String> getAllDBNames()
    {
        MongoIterable<String> colls = mongoClient.listDatabaseNames();
        List<String> list = new ArrayList<>();
        for (Iterator<String> iterator = colls.iterator(); iterator.hasNext();)
        {
            String name = iterator.next();
            list.add(name);
        }
        return list;
    }
    
    public void dropDB(String dbName)
    {
        getDB(dbName).drop();
    }
    
    public Document runCommand(String dbName, String sql)
    {
        Bson arg0 = new BasicDBObject();
        Document doc = getDB(dbName).runCommand(arg0);
        return doc;
    }
    
    public Document findById(MongoCollection<Document> coll, String id)
    {
        Document myDoc = new Document();
        if (id.indexOf("ObjectId") >= 0)
        {
            id = id.replace("ObjectId(\"", "").replace("\")", "");
            ObjectId object = new ObjectId(id);
            Document filter = new Document();
            filter.append("_id", object);
            myDoc = coll.find(filter).first();
        }
        else
        {
            Document filter = new Document();
            filter.append("_id", id);
            myDoc = coll.find(filter).first();
        }
        return myDoc;
    }
    
    public int getCount(MongoCollection<Document> coll)
    {
        int count = (int)coll.count();
        return count;
    }
    
    public int getCountForFilter(MongoCollection<Document> coll, Bson filter)
    {
        MongoCursor<Document> cursor = coll.find(filter).iterator();
        int count = 0;
        while (cursor.hasNext())
        {
            count++;
        }
        return count;
    }
    
    public MongoCursor<Document> find(MongoCollection<Document> coll, Bson filter)
    {
        return coll.find(filter).iterator();
    }
    
    public MongoCursor<Document> findByPageForMongoDB(MongoCollection<Document> coll, Bson filter, int pageNo, int pageSize)
    {
        return coll.find(filter).skip((pageNo - 1) * pageSize).limit(pageSize).iterator();
    }
    
    public int deleteById(MongoCollection<Document> coll, String id)
    {
        int count = 0;
        if (id.indexOf("ObjectId") >= 0)
        {
            id = id.replace("ObjectId(\"", "").replace("\")", "");
            ObjectId object = new ObjectId(id);
            Document filter = new Document();
            filter.append("_id", object);
            coll.deleteOne(filter);
        }
        else
        {
            Document filter = new Document();
            filter.append("_id", id);
            coll.deleteOne(filter);
        }
        return count;
    }
    
    public Document updateById(MongoCollection<Document> coll, String id, Document newdoc)
    {
        if (id.indexOf("ObjectId") >= 0)
        {
            id = id.replace("ObjectId(\"", "").replace("\")", "");
            ObjectId idobj = new ObjectId(id);
            Bson filter = Filters.eq("_id", idobj);
            coll.updateOne(filter, new Document("$set", newdoc));
        }
        else
        {
            Bson filter = Filters.eq("_id", id);
            coll.updateOne(filter, new Document("$set", newdoc));
        }
        return newdoc;
    }
    
    public void dropCollection(String databaseName, String tableName)
    {
        getDB(databaseName).getCollection(tableName).drop();
    }
    
    public void createCollection(String databaseName, String tableName)
    {
        getDB(databaseName).createCollection(tableName);
    }
    
    public void deleteCollection(String databaseName, String tableName)
    {
        getDB(databaseName).getCollection(tableName).drop();
    }
    
    public void close()
    {
        if (mongoClient != null)
        {
            mongoClient.close();
            mongoClient = null;
        }
    }
    
    public static void main(String[] args)
    {
        String dbName = "GC_MAP_DISPLAY_DB";
        String collName = "COMMUNITY_BJ";
    }
    
    public static boolean testConnection(String databaseType2, String databaseName2, String ip2, String port2, String user2, String pass2)
    {
        MongoClientURI uri = null;
        if (user2.equals(""))
        {
            uri = new MongoClientURI("mongodb://" + ip2 + ":" + port2);
        }
        else
        {
            uri = new MongoClientURI("mongodb://" + user2 + ":" + pass2 + "@" + ip2 + ":" + port2);
        }
        MongoClient mongoClient = new MongoClient(uri);
        MongoDatabase database = mongoClient.getDatabase(databaseName2);
        if (database != null)
        {
            return true;
        }
        return false;
    }
}
