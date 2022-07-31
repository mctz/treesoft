package org.springframework.base.system.dao;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.springframework.base.system.dbUtils.RedisAPI;
import org.springframework.base.system.entity.NotSqlEntity;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.utils.Constants;
import org.springframework.stereotype.Repository;

@Repository
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/dao/RedisDao.class */
public class RedisDao {
    public List<Map<String, Object>> getAllDataBaseForRedis(String databaseConfigId) throws Exception {
        List<Map<String, Object>> listAll = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> mapConfig = Constants.configStaticMap.get(databaseConfigId);
        String ip = (String) mapConfig.get("ip");
        String port = (String) mapConfig.get("port");
        map.put("SCHEMA_NAME", String.valueOf(ip) + ":" + port);
        listAll.add(map);
        return listAll;
    }

    public List<Map<String, Object>> getAllShardForRedis(String databaseConfigId) throws Exception {
        List<Map<String, Object>> listAll = new ArrayList<>();
        int dbAmount = RedisAPI.getDbAmountForRedis(databaseConfigId);
        for (int i = 0; i < dbAmount; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("TABLE_NAME", "DB" + i);
            listAll.add(map);
        }
        return listAll;
    }

    public Page<Map<String, Object>> getNoSQLDBForRedis(Page<Map<String, Object>> page, String NoSQLDbName, String databaseConfigId, String selectKey, String selectValue) throws Exception {
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        Map<String, Object> tempMap = RedisAPI.getNoSQLDBForRedis(pageSize, limitFrom, NoSQLDbName, selectKey, selectValue, databaseConfigId);
        page.setTotalCount(((Integer) tempMap.get("rowCount")).intValue());
        page.setResult((List) tempMap.get("dataList"));
        return page;
    }

    public Map<String, Object> selectNotSqlDataForRedis(String key, String NoSQLDbName, String databaseConfigId) {
        return RedisAPI.get2(key, NoSQLDbName, databaseConfigId);
    }

    public boolean saveNotSqlDataForRedis(NotSqlEntity notSqlEntity, String databaseConfigId, String NoSQLDbName) throws Exception {
        boolean bl = RedisAPI.set(notSqlEntity, databaseConfigId, NoSQLDbName);
        return bl;
    }

    public int deleteNoSQLKeyForRedis(String databaseConfigId, String NoSQLDbName, String[] ids) throws Exception {
        RedisAPI.deleteKeys(databaseConfigId, NoSQLDbName, ids);
        return 0;
    }

    public Page<Map<String, Object>> selectNoSQLDBStatusForRedis(Page<Map<String, Object>> page, String databaseConfigId) throws Exception {
        List<Map<String, Object>> list = new ArrayList<>();
        String info = RedisAPI.getInfo(databaseConfigId);
        Properties properties = new Properties();
        InputStream inStream = new ByteArrayInputStream(info.getBytes());
        properties.load(inStream);
        Iterator<Map.Entry<Object, Object>> it = properties.entrySet().iterator();
        while (it.hasNext()) {
            Map<String, Object> map = new HashMap<>();
            Map.Entry<Object, Object> entry = it.next();
            String parameter = (String) entry.getKey();
            String value = (String) entry.getValue();
            map.put("parameter", parameter);
            if (parameter.equals("redis_version")) {
                map.put("value", value);
                map.put("content", "redis版本 ");
            } else if (parameter.equals("aof_enabled")) {
                map.put("value", value);
                map.put("content", "Redis是否开启了aof");
            } else if (parameter.equals("used_memory_peak")) {
                map.put("value", value);
                map.put("content", "Redis所用内存的高峰值（以字节为单位）");
            } else if (parameter.equals("used_memory_peak_human")) {
                map.put("value", value);
                map.put("content", "Redis所用内存的高峰值");
            } else if (parameter.equals("used_memory_human")) {
                map.put("value", value);
                map.put("content", "Redis分配的内存总量");
            } else if (parameter.equals("connected_clients")) {
                map.put("value", value);
                map.put("content", "连接的客户端数量");
            } else if (parameter.equals("mem_fragmentation_ratio")) {
                map.put("value", value);
                map.put("content", "内存碎片比率");
            } else if (parameter.equals("db0")) {
                map.put("value", value);
                map.put("content", "分片的键数量及过期键数量");
            } else if (parameter.equals("used_memory")) {
                map.put("value", value);
                map.put("content", "由 Redis分配器分配的内存总量，以字节（byte）为单位");
            } else if (parameter.equals("total_connections_received")) {
                map.put("value", value);
                map.put("content", "运行以来连接过的客户端的总数量");
            } else if (parameter.equals("role")) {
                map.put("value", value);
                map.put("content", "当前实例的角色master还是slave");
            } else if (parameter.equals("keyspace_misses")) {
                map.put("value", value);
                map.put("content", "没命中key 的次数");
            } else if (parameter.equals("expired_keys")) {
                map.put("value", value);
                map.put("content", "运行以来过期的 key 的数量");
            } else if (parameter.equals("keyspace_hits")) {
                map.put("value", value);
                map.put("content", "命中key 的次数");
            } else if (parameter.equals("client_longest_output_list")) {
                map.put("value", value);
                map.put("content", "当前连接的客户端当中，最长的输出列表");
            } else if (parameter.equals("used_cpu_user_children")) {
                map.put("value", value);
                map.put("content", "后台进程耗费的用户 CPU");
            } else if (parameter.equals("uptime_in_seconds")) {
                map.put("value", value);
                map.put("content", "自 Redis 服务器启动以来，经过的秒数");
            } else if (parameter.equals("lru_clock")) {
                map.put("value", value);
                map.put("content", "以分钟为单位进行自增的时钟，用于 LRU 管理");
            } else if (parameter.equals("redis_git_sha1")) {
                map.put("value", value);
                map.put("content", "Git SHA1");
            } else if (parameter.equals("redis_git_dirty")) {
                map.put("value", value);
                map.put("content", "Git dirty flag");
            } else if (parameter.equals("latest_fork_usec")) {
                map.put("value", value);
                map.put("content", "最近一次 fork() 操作耗费的毫秒数");
            } else if (parameter.equals("used_cpu_sys")) {
                map.put("value", value);
                map.put("content", "Redis服务器耗费的系统 CPU");
            } else if (parameter.equals("used_cpu_user")) {
                map.put("value", value);
                map.put("content", "Redis服务器耗费的用户 CPU");
            } else if (parameter.equals("used_cpu_sys_children")) {
                map.put("value", value);
                map.put("content", "后台进程耗费的系统 CPU");
            } else if (parameter.equals("process_id")) {
                map.put("value", value);
                map.put("content", "服务器进程的 PID");
            } else if (parameter.equals("mem_allocator")) {
                map.put("value", value);
                map.put("content", "在编译时指定的， Redis 所使用的内存分配器。可以是 libc 、 jemalloc 或者 tcmalloc");
            } else if (parameter.equals("pubsub_channels")) {
                map.put("value", value);
                map.put("content", "目前被订阅的频道数量");
            } else if (parameter.equals("evicted_keys")) {
                map.put("value", value);
                map.put("content", "因为最大内存容量限制而被驱逐（evict）的键数量");
            } else if (parameter.equals("uptime_in_days")) {
                map.put("value", value);
                map.put("content", "自 Redis服务器启动以来，经过的天数");
            } else if (parameter.equals("blocked_clients")) {
                map.put("value", value);
                map.put("content", "正在等待阻塞命令（BLPOP、BRPOP、BRPOPLPUSH）的客户端的数量");
            } else if (parameter.equals("changes_since_last_save")) {
                map.put("value", value);
                map.put("content", "距离最近一次成功创建持久化文件之后，经过了多少秒");
            } else if (parameter.equals("multiplexing_api")) {
                map.put("value", value);
                map.put("content", "Redis所使用的事件处理机制");
            } else if (parameter.equals("bgsave_in_progress")) {
                map.put("value", value);
                map.put("content", "一个标志值，记录了服务器是否正在创建 RDB文件");
            } else if (parameter.equals("pubsub_patterns")) {
                map.put("value", value);
                map.put("content", "目前被订阅的模式数量");
            } else if (parameter.equals("total_commands_processed")) {
                map.put("value", value);
                map.put("content", "服务器已执行的命令数量");
            } else if (parameter.equals("used_memory_rss")) {
                map.put("value", value);
                map.put("content", "从操作系统的角度，返回 Redis 已分配的内存总量（俗称常驻集大小）");
            } else if (parameter.equals("loading")) {
                map.put("value", value);
                map.put("content", "一个标志值，记录了服务器是否正在载入持久化文件");
            } else if (parameter.equals("connected_slaves")) {
                map.put("value", value);
                map.put("content", "已连接的从服务器数量");
            } else if (parameter.equals("rdb_changes_since_last_save")) {
                map.put("value", value);
                map.put("content", "距离最近一次成功创建持久化文件之后，经过了多少秒");
            } else if (parameter.equals("rdb_last_save_time")) {
                map.put("value", value);
                map.put("content", "最近一次成功创建 RDB 文件的 UNIX 时间戳");
            } else if (parameter.equals("tcp_port")) {
                map.put("value", value);
                map.put("content", "TCP/IP 监听端口");
            } else if (parameter.equals("aof_last_rewrite_time_sec")) {
                map.put("value", value);
                map.put("content", "最近一次创建 AOF 文件耗费的时长");
            } else if (parameter.equals("run_id")) {
                map.put("value", value);
                map.put("content", "Redis 服务器的随机标识符（用于 Sentinel 和集群）");
            } else if (parameter.equals("aof_rewrite_scheduled")) {
                map.put("value", value);
                map.put("content", "一个标志值，记录了在 RDB 文件创建完毕之后，是否需要执行预约的 AOF 重写操作");
            } else if (parameter.equals("os")) {
                map.put("value", value);
                map.put("content", "Redis服务器的宿主操作系统");
            } else if (parameter.equals("rdb_last_bgsave_time_sec")) {
                map.put("value", value);
                map.put("content", "记录了最近一次创建 RDB 文件耗费的秒数");
            } else if (parameter.equals("aof_last_bgrewrite_status")) {
                map.put("value", value);
                map.put("content", "一个标志值，记录了最近一次创建 AOF 文件的结果是成功还是失败");
            } else if (parameter.equals("rdb_current_bgsave_time_sec")) {
                map.put("value", value);
                map.put("content", "如果服务器正在创建 RDB 文件，那么这个域记录的就是当前的创建操作已经耗费的秒数");
            } else if (parameter.equals("rdb_last_bgsave_status")) {
                map.put("value", value);
                map.put("content", "一个标志值，记录了最近一次创建 RDB 文件的结果是成功还是失败");
            } else if (parameter.equals("aof_rewrite_in_progress")) {
                map.put("value", value);
                map.put("content", "一个标志值，记录了服务器是否正在创建 AOF 文件");
            } else if (parameter.equals("aof_current_rewrite_time_sec")) {
                map.put("value", value);
                map.put("content", "如果服务器正在创建 AOF 文件，那么这个域记录的就是当前的创建操作已经耗费的秒数");
            } else if (parameter.equals("instantaneous_ops_per_sec")) {
                map.put("value", value);
                map.put("content", "服务器每秒钟执行的命令数量");
            } else if (parameter.equals("arch_bits")) {
                map.put("value", value);
                map.put("content", "架构（32 或 64 位）");
            } else if (parameter.equals("used_memory_lua")) {
                map.put("value", value);
                map.put("content", "Lua 引擎所使用的内存大小（以字节为单位）");
            } else if (parameter.equals("gcc_version")) {
                map.put("value", value);
                map.put("content", "编译 Redis 时所使用的 GCC 版本");
            } else if (parameter.equals("rdb_bgsave_in_progress")) {
                map.put("value", value);
                map.put("content", "一个标志值，记录了服务器是否正在创建 RDB 文件");
            } else if (parameter.equals("client_biggest_input_buf")) {
                map.put("value", value);
                map.put("content", " 最大输入缓冲区，单位字节，其实就是最大 qbuf");
            } else if (parameter.equals("redis_mode")) {
                map.put("value", value);
                map.put("content", "Redis运行模式，单机or集群");
            } else if (parameter.equals("rejected_connections")) {
                map.put("value", value);
                map.put("content", "因为最大客户端数量限制而被拒绝的连接请求数量");
            } else if (parameter.equals("redis_build_id")) {
                map.put("value", value);
                map.put("content", "ID");
            } else if (parameter.equals("repl_backlog_size")) {
                map.put("value", value);
                map.put("content", "一个环形缓存区,默认1024*1024大小, 即1Mb");
            } else if (parameter.equals("hz")) {
                map.put("value", value);
                map.put("content", "执行后台任务的频率,默认为10,此值越大表示redis对\"间歇性task\"的执行次数越频繁(次数/秒)");
            } else if (parameter.equals("last_save_time")) {
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.parseLong(String.valueOf(value) + "000"));
                map.put("value", formatter.format(calendar.getTime()));
                map.put("content", "上次保存RDB文件的时间");
            } else {
                map.put("value", value);
                map.put("content", "");
            }
            list.add(map);
        }
        Collections.sort(list, new Comparator<Map<String, Object>>() { // from class: org.springframework.base.system.dao.RedisDao.1
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                return o1.get("parameter").toString().substring(0, 1).compareTo(o2.get("parameter").toString().substring(0, 1));
            }
        });
        int rowCount = list.size();
        page.setTotalCount(rowCount);
        page.setResult(list);
        return page;
    }

    public Map<String, Object> queryInfoItemForRedis(String databaseConfigId) throws Exception {
        String info = RedisAPI.getInfo(databaseConfigId);
        Properties properties = new Properties();
        InputStream inStream = new ByteArrayInputStream(info.getBytes());
        properties.load(inStream);
        Map<String, Object> map = new HashMap<>();
        int totalKeys = 0;
        Iterator<Map.Entry<Object, Object>> it = properties.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Object, Object> entry = it.next();
            String parameter = (String) entry.getKey();
            String value = (String) entry.getValue();
            if (parameter.indexOf("db") == 0) {
                try {
                    String ssi = value.substring(5, value.indexOf(","));
                    totalKeys += Integer.parseInt(ssi);
                } catch (Exception e) {
                }
            } else {
                map.put(parameter, value);
            }
        }
        int keyspace_hits = Integer.parseInt(new StringBuilder().append(properties.get("keyspace_hits")).toString());
        int keyspace_misses = Integer.parseInt(new StringBuilder().append(properties.get("keyspace_misses")).toString());
        int keyspace_hits_scope = 0;
        if (keyspace_misses + keyspace_hits > 0) {
            keyspace_hits_scope = Math.round((keyspace_hits * 100) / (keyspace_misses + keyspace_hits));
            if (keyspace_hits_scope < 0) {
                keyspace_hits_scope = 0;
            }
        }
        map.put("keyspace_hits_scope", Integer.valueOf(keyspace_hits_scope));
        map.put("totalKeys", Integer.valueOf(totalKeys));
        return map;
    }
}
