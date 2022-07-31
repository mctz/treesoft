package org.springframework.base.system.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.base.system.dao.RedisDao;
import org.springframework.base.system.entity.NotSqlEntity;
import org.springframework.base.system.persistence.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Tuple;

@Service
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/service/RedisService.class */
public class RedisService {
    @Autowired
    private RedisDao redisDao;

    public List<Map<String, Object>> getAllDataBaseForRedis(String databaseConfigId) throws Exception {
        return this.redisDao.getAllDataBaseForRedis(databaseConfigId);
    }

    public List<Map<String, Object>> getAllShardForRedis(String dbName, String databaseConfigId) throws Exception {
        return this.redisDao.getAllShardForRedis(databaseConfigId);
    }

    public Page<Map<String, Object>> getNoSQLDBForRedis(Page<Map<String, Object>> page, String NoSQLDbName, String databaseConfigId, String selectKey, String selectValue) throws Exception {
        return this.redisDao.getNoSQLDBForRedis(page, NoSQLDbName, databaseConfigId, selectKey, selectValue);
    }

    public NotSqlEntity selectNotSqlDataForRedis(String key, String NoSQLDbName, String databaseConfigId) throws Exception {
        NotSqlEntity notSqlEntity = new NotSqlEntity();
        new HashMap();
        Map<String, Object> map = this.redisDao.selectNotSqlDataForRedis(key, NoSQLDbName, databaseConfigId);
        notSqlEntity.setKey(key);
        String type = (String) map.get("type");
        notSqlEntity.setType((String) map.get("type"));
        notSqlEntity.setExTime((String) map.get("exTime"));
        if (type.equals("string")) {
            notSqlEntity.setValue((String) map.get("value"));
        }
        if (type.equals("list")) {
            notSqlEntity.setList((List) map.get("value"));
        }
        if (type.equals("set")) {
            String temp = (String) map.get("value");
            String[] arr = temp.substring(1, temp.length() - 1).split(",");
            notSqlEntity.setList(Arrays.asList(arr));
        }
        if (type.equals("zset")) {
            Set<Tuple> set = (Set) map.get("value");
            Iterator<Tuple> itt = set.iterator();
            List<Map<String, Object>> listMap = new ArrayList<>();
            while (itt.hasNext()) {
                Tuple str = itt.next();
                Map<String, Object> mm = new HashMap<>();
                mm.put("valuek", Double.valueOf(str.getScore()));
                mm.put("valuev", str.getElement());
                listMap.add(mm);
            }
            notSqlEntity.setListMap(listMap);
        }
        if (type.equals("hash")) {
            Map<String, String> map5 = (Map) map.get("value");
            List<Map<String, Object>> listMap2 = new ArrayList<>();
            Iterator<Map.Entry<String, String>> it = map5.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                Map<String, Object> mm2 = new HashMap<>();
                mm2.put("valuek", entry.getKey());
                mm2.put("valuev", entry.getValue());
                listMap2.add(mm2);
            }
            notSqlEntity.setListMap(listMap2);
        }
        return notSqlEntity;
    }

    public boolean saveNotSqlDataForRedis(NotSqlEntity notSqlEntity, String databaseConfigId, String NoSQLDbName) throws Exception {
        return this.redisDao.saveNotSqlDataForRedis(notSqlEntity, databaseConfigId, NoSQLDbName);
    }

    public int deleteNoSQLKeyForRedis(String databaseConfigId, String NoSQLDbName, String[] ids) throws Exception {
        return this.redisDao.deleteNoSQLKeyForRedis(databaseConfigId, NoSQLDbName, ids);
    }

    public Page<Map<String, Object>> selectNoSQLDBStatusForRedis(Page<Map<String, Object>> page, String databaseConfigId) throws Exception {
        return this.redisDao.selectNoSQLDBStatusForRedis(page, databaseConfigId);
    }

    public Map<String, Object> queryInfoItemForRedis(String databaseConfigId) throws Exception {
        return this.redisDao.queryInfoItemForRedis(databaseConfigId);
    }
}
