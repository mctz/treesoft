package org.springframework.base.system.dbUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.base.system.entity.NotSqlEntity;
import org.springframework.base.system.utils.Constants;
import org.springframework.base.system.utils.LogUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Tuple;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/dbUtils/RedisAPI.class */
public class RedisAPI {
    public static JedisPool pool = null;
    private static JedisPool pool2 = null;
    private static int TIMEOUT = 10000;
    private static List nodekeys;
    private static Jedis jedis;

    public static JedisPool getPool(String ip, String port, String password) {
        if (pool == null) {
            new JedisPoolConfig();
        }
        return pool;
    }

    public static void returnResource(JedisPool pool3, Jedis redis) {
        if (redis != null) {
            pool3.returnResource(redis);
        }
    }

    public static Jedis getJedis(String databaseConfigId) {
        if (jedis != null) {
            return jedis;
        }
        Map<String, Object> map = Constants.configStaticMap.get(databaseConfigId);
        String ip = (String) map.get("ip");
        String port = (String) map.get("port");
        String password = (String) map.get("password");
        jedis = new Jedis(ip, Integer.parseInt(port), 10000);
        if (StringUtils.isNotEmpty(password)) {
            jedis.auth(password);
        }
        return jedis;
    }

    public static int getDbAmountForRedis(String databaseConfigId) {
        int dbAmount;
        jedis = getJedis(databaseConfigId);
        try {
            List<String> dbs = jedis.configGet("databases");
            if (dbs.size() > 0) {
                dbAmount = Integer.parseInt(dbs.get(1));
            } else {
                dbAmount = 15;
            }
            return dbAmount;
        } catch (Exception e) {
            jedis.disconnect();
            jedis = null;
            e.printStackTrace();
            return 1;
        }
    }

    public static String getInfo(String databaseConfigId) {
        String value = null;
        jedis = getJedis(databaseConfigId);
        try {
            value = jedis.info();
        } catch (Exception e) {
        }
        return value;
    }

    public String getInfo2(String databaseConfigId) {
        jedis = getJedis(databaseConfigId);
        String value = "";
        try {
            value = jedis.info();
        } catch (Exception e) {
        }
        return value;
    }

    public String getConfig2(String databaseConfigId, String configKey) {
        String value = "";
        jedis = getJedis(databaseConfigId);
        try {
            List<String> list = jedis.configGet(configKey);
            for (int i = 0; i < list.size(); i++) {
                value = list.get(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static Map<String, Object> get2(String key, String NoSQLDbName, String databaseConfigId) {
        jedis = getJedis(databaseConfigId);
        String currentDBindex = NoSQLDbName.substring(2, NoSQLDbName.length());
        Map<String, Object> map = new HashMap<>();
        try {
            jedis.select(Integer.parseInt(currentDBindex));
            String type = jedis.type(key);
            String exTime = new StringBuilder().append(jedis.ttl(key)).toString();
            map.put("key", key);
            map.put("type", type);
            map.put("exTime", exTime);
            if (type.equals("string")) {
                map.put("value", jedis.get(key));
            }
            if (type.equals("list")) {
                Long lon = jedis.llen(key);
                map.put("value", jedis.lrange(key, 0L, lon.longValue()));
            }
            if (type.equals("set")) {
                map.put("value", jedis.smembers(key).toString());
            }
            if (type.equals("zset")) {
                Set<Tuple> set = jedis.zrangeWithScores(key, 0L, -1L);
                map.put("value", set);
            }
            if (type.equals("hash")) {
                map.put("value", jedis.hgetAll(key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static boolean bgsave(String databaseConfigId) {
        try {
            jedis = getJedis(databaseConfigId);
            jedis.bgsave();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean set(NotSqlEntity notSqlEntity, String databaseConfigId, String NoSQLDbName) {
        int o1;
        try {
            jedis = getJedis(databaseConfigId);
            String currentDBindex = NoSQLDbName.substring(2, NoSQLDbName.length());
            jedis.select(Integer.parseInt(currentDBindex));
            String key = notSqlEntity.getKey();
            String value = notSqlEntity.getValue();
            String value2 = StringEscapeUtils.unescapeHtml4(value);
            String type = notSqlEntity.getType();
            if ("".equals(notSqlEntity.getExTime()) || "0".equals(notSqlEntity.getExTime())) {
                o1 = -1;
            } else {
                o1 = Integer.parseInt(notSqlEntity.getExTime());
            }
            if (type == null || type.equals("none")) {
                return false;
            }
            if (type.equals("string")) {
                jedis.set(key, value2);
            }
            if (type.equals("list")) {
                String[] valuek = notSqlEntity.getValuek();
                jedis.del(key);
                for (int i = valuek.length; i > 0; i--) {
                    if (i == valuek.length) {
                        jedis.lpush(key, new String[]{valuek[i - 1]});
                    } else {
                        jedis.lpushx(key, new String[]{valuek[i - 1]});
                    }
                }
            }
            if (type.equals("set")) {
                String[] valuek2 = notSqlEntity.getValuek();
                jedis.del(key);
                String members = "";
                for (int i2 = 0; i2 < valuek2.length; i2++) {
                    if (i2 == 0) {
                        members = String.valueOf(members) + valuek2[i2];
                    } else {
                        members = String.valueOf(members) + "," + valuek2[i2];
                    }
                }
                jedis.sadd(key, new String[]{members});
            }
            if (type.equals("zset")) {
                String[] valuek3 = notSqlEntity.getValuek();
                String[] valueV = notSqlEntity.getValuev();
                jedis.del(key);
                Map<String, Double> scoreMembers = new HashMap<>();
                for (int i3 = valuek3.length; i3 > 0; i3--) {
                    Double valuekkk = Double.valueOf(Double.parseDouble(valuek3[i3 - 1].trim()));
                    String valuevvv = valueV[i3 - 1].trim();
                    if (valuevvv == null) {
                        valuevvv = "";
                    }
                    scoreMembers.put(valuevvv, valuekkk);
                }
                jedis.zadd(key, scoreMembers);
            }
            if (type.equals("hash")) {
                String[] valuek4 = notSqlEntity.getValuek();
                String[] valueV2 = notSqlEntity.getValuev();
                jedis.del(key);
                Map<String, String> hashmm = new HashMap<>();
                for (int i4 = valuek4.length; i4 > 0; i4--) {
                    String valuekkk2 = valuek4[i4 - 1].trim();
                    String valuevvv2 = valueV2[i4 - 1].trim();
                    if (valuevvv2 == null) {
                        valuevvv2 = "";
                    }
                    hashmm.put(valuekkk2, valuevvv2);
                }
                jedis.hmset(key, hashmm);
            }
            if (type.equals("HashSet")) {
                System.out.println("HashSet 类型暂时不支持！");
            }
            if (type.equals("ArryList")) {
                System.out.println("ArryList 类型暂时不支持！");
            }
            if (o1 != -1) {
                jedis.expire(key, o1);
                return true;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteKeys(String databaseConfigId, String NoSQLDbName, String[] ids) {
        jedis = getJedis(databaseConfigId);
        String currentDBindex = NoSQLDbName.substring(2, NoSQLDbName.length());
        try {
            jedis.select(Integer.parseInt(currentDBindex));
            for (String str : ids) {
                jedis.del(str);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static Map<String, Object> getNoSQLDBForRedis(int pageSize, int limitFrom, String NoSQLDbName, String selectKey, String selectValue, String databaseConfigId) {
        String currentDBindex = NoSQLDbName.substring(2, NoSQLDbName.length());
        Map<String, Object> tempMap = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            Jedis jedis2 = getJedis(databaseConfigId);
            jedis2.select(Integer.parseInt(currentDBindex));
            Long dbSize = jedis2.dbSize();
            Set nodekeys2 = new HashSet();
            if (selectKey.equals("nokey")) {
                if (dbSize.longValue() > 1000) {
                    limitFrom = 0;
                    for (int z = 0; z < pageSize; z++) {
                        nodekeys2.add(jedis2.randomKey());
                    }
                } else {
                    nodekeys2 = jedis2.keys("*");
                }
            } else {
                nodekeys2 = jedis2.keys("*" + selectKey + "*");
            }
            Iterator it = nodekeys2.iterator();
            int i = 1;
            while (it.hasNext()) {
                if (i >= limitFrom && i <= limitFrom + pageSize) {
                    Map<String, Object> map = new HashMap<>();
                    String key = (String) it.next();
                    String type = jedis2.type(key);
                    map.put("key", key);
                    map.put("type", type);
                    if (type.equals("string")) {
                        String value = jedis2.get(key);
                        if (value.length() > 80) {
                            map.put("value", String.valueOf(value.substring(0, 79)) + "......");
                        } else {
                            map.put("value", value);
                        }
                    }
                    if (type.equals("list")) {
                        Long lon = jedis2.llen(key);
                        if (lon.longValue() > 20) {
                            lon = 20L;
                        }
                        map.put("value", jedis2.lrange(key, 0L, lon.longValue()));
                    }
                    if (type.equals("set")) {
                        map.put("value", jedis2.smembers(key).toString());
                    }
                    if (type.equals("zset")) {
                        Long lon2 = jedis2.zcard(key);
                        if (lon2.longValue() > 20) {
                            lon2 = 20L;
                        }
                        Set<Tuple> set = jedis2.zrangeWithScores(key, 0L, lon2.longValue());
                        Iterator<Tuple> itt = set.iterator();
                        String ss = "";
                        while (itt.hasNext()) {
                            Tuple str = itt.next();
                            ss = String.valueOf(ss) + "[" + str.getScore() + "," + str.getElement() + "],";
                        }
                        map.put("value", "[" + ss.substring(0, ss.length() - 1) + "]");
                    }
                    if (type.equals("hash")) {
                        map.put("value", jedis2.hgetAll(key).toString());
                    }
                    list.add(map);
                } else {
                    it.next();
                }
                i++;
            }
            if (selectKey.equals("nokey")) {
                tempMap.put("rowCount", Integer.valueOf(Integer.parseInt(dbSize.toString())));
            } else {
                tempMap.put("rowCount", Integer.valueOf(i));
            }
            tempMap.put("dataList", list);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("取得 NoSQL数据出错：" + e);
        }
        return tempMap;
    }

    public static boolean testConnForRedis(String databaseType, String databaseName, String ip, String port, String user, String pass) {
        try {
            Jedis jedis7 = new Jedis(ip, Integer.parseInt(port), 5000);
            if (!pass.equals("")) {
                jedis7.auth(pass);
            }
            if (jedis7.isConnected()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static boolean flushAllForRedis(Map<String, Object> map) throws Exception {
        String ip = (String) map.get("ip");
        String port = (String) map.get("port");
        String password = (String) map.get("password");
        Jedis jedis7 = new Jedis(ip, Integer.parseInt(port));
        password.equals("");
        jedis7.flushAll();
        return true;
    }
}
