package org.springframework.base.system.dynamicDataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.base.system.utils.Constants;
import org.springframework.stereotype.Component;

@Component
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/dynamicDataSource/DynamicDataSourceDruid.class */
public class DynamicDataSourceDruid {
    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceDruid.class);
    public static Map<String, DruidDataSource> customDataSources = new ConcurrentHashMap();

    public void insertOrUpdateCustomDbDataSources(Map<String, Object> map) throws Exception {
        String id = map.get("id").toString();
        String poolName = map.get("id") + Constants.POOL_NAME_SPLIT + map.get("databaseName");
        logger.info("update dataSources , poolName=" + poolName);
        Properties properties = new Properties();
        properties.setProperty("url", map.get("jdbc_url").toString());
        properties.setProperty("driver", map.get("driver_class_name").toString());
        properties.setProperty("username", map.get("username").toString());
        properties.setProperty("password", map.get("password").toString());
        properties.setProperty("initialSize", Constants.MINIMUM_IDLE.toString());
        properties.setProperty("minIdle", Constants.MINIMUM_IDLE.toString());
        properties.setProperty("maxActive", Constants.MAXIMUM_POOL_SIZE.toString());
        properties.setProperty("removeAbandoned", "true");
        properties.setProperty("removeAbandonedTimeout", "180");
        properties.setProperty("maxWait", "60000");
        DruidDataSource druidDataSource = DruidDataSourceFactory.createDataSource(properties);
        druidDataSource.setTimeBetweenConnectErrorMillis(300000L);
        druidDataSource.setTestWhileIdle(true);
        Iterator<Map.Entry<String, DruidDataSource>> it = customDataSources.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, DruidDataSource> entry = it.next();
            if (entry.getKey().indexOf(id) >= 0) {
                DruidDataSource dataSourceOld = entry.getValue();
                dataSourceOld.close();
                customDataSources.remove(entry.getKey());
            }
        }
        customDataSources.put(poolName, druidDataSource);
    }

    public void deleteCustomDbDataSources(String poolName) {
        logger.info("drop dataSources ,poolName=" + poolName);
        DruidDataSource dataSource = customDataSources.get(poolName);
        if (dataSource != null) {
            dataSource.close();
        }
        customDataSources.remove(poolName);
    }

    public static DataSource getDataSource(String poolName) {
        String jdbc_url = "";
        try {
            String id = poolName.substring(0, poolName.indexOf(Constants.POOL_NAME_SPLIT));
            String databaseName = poolName.substring(poolName.indexOf(Constants.POOL_NAME_SPLIT) + 4);
            DruidDataSource dataSource = customDataSources.get(poolName);
            if (dataSource == null) {
                logger.info("create datasource， poolName=" + poolName);
                Map<String, Object> configMess = new HashMap<>();
                configMess.putAll(Constants.configStaticMap.get(id));
                jdbc_url = concatJdbcUrl(configMess, databaseName);
                Properties properties = new Properties();
                properties.setProperty("url", jdbc_url);
                properties.setProperty("driver", configMess.get("driver").toString());
                properties.setProperty("username", configMess.get("userName").toString());
                properties.setProperty("password", configMess.get("password").toString());
                properties.setProperty("initialSize", Constants.MINIMUM_IDLE.toString());
                properties.setProperty("minIdle", Constants.MINIMUM_IDLE.toString());
                properties.setProperty("maxActive", Constants.MAXIMUM_POOL_SIZE.toString());
                properties.setProperty("removeAbandoned", "true");
                properties.setProperty("removeAbandonedTimeout", "180");
                properties.setProperty("maxWait", "60000");
                properties.setProperty("logAbandoned", "true");
                DruidDataSource druidDataSource = DruidDataSourceFactory.createDataSource(properties);
                druidDataSource.setTimeBetweenConnectErrorMillis(300000L);
                druidDataSource.setTestWhileIdle(true);
                customDataSources.put(poolName, druidDataSource);
                return druidDataSource;
            }
            return dataSource;
        } catch (Exception e) {
            logger.error("数据源连接出错，" + jdbc_url, e);
            return null;
        }
    }

    public static String concatJdbcUrl(Map<String, Object> map0, String dbName) {
        String databaseType = new StringBuilder().append(map0.get("databaseType")).toString();
        String ip = new StringBuilder().append(map0.get("ip")).toString();
        String port = new StringBuilder().append(map0.get("port")).toString();
        String url = "";
        if (databaseType.equals("MySQL") || databaseType.equals("TiDB")) {
            url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName + "?characterEncoding=utf8&tinyInt1isBit=false&useSSL=false&serverTimezone=GMT%2B8";
        }
        if (databaseType.equals("MariaDB")) {
            url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName + "?characterEncoding=utf8&tinyInt1isBit=false&useSSL=false";
        }
        if (databaseType.equals("MySQL8.0")) {
            url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName + "?useUnicode=true&characterEncoding=utf-8&tinyInt1isBit=false&zeroDateTimeBehavior=CONVERT_TO_NULL&useSSL=false&serverTimezone=GMT%2B8";
        }
        if (databaseType.equals("Oracle")) {
            url = "jdbc:oracle:thin:@//" + ip + ":" + port + "/" + dbName;
        }
        if (databaseType.equals("HANA2")) {
            url = "jdbc:sap://" + ip + ":" + port + "?reconnect=true";
        }
        if (databaseType.equals("PostgreSQL")) {
            url = "jdbc:postgresql://" + ip + ":" + port + "/" + dbName;
        }
        if (databaseType.equals("SQL Server")) {
            url = "jdbc:sqlserver://" + ip + ":" + port + ";database=" + dbName;
        }
        if (databaseType.equals("Hive2")) {
            url = "jdbc:hive2://" + ip + ":" + port + "/" + dbName;
        }
        if (databaseType.equals("Impala")) {
            url = "jdbc:impala://" + ip + ":" + port;
        }
        if (databaseType.equals("Cache")) {
            url = "jdbc:Cache://" + ip + ":" + port + "/" + dbName;
        }
        if (databaseType.equals("DB2")) {
            url = "jdbc:db2://" + ip + ":" + port + "/" + dbName;
        }
        if (databaseType.equals("DM7")) {
            url = "jdbc:dm://" + ip + ":" + port + "/" + dbName;
        }
        if (databaseType.equals("ShenTong")) {
            String databaseName = new StringBuilder().append(map0.get("databaseName")).toString();
            url = "jdbc:oscar://" + ip + ":" + port + "/" + databaseName;
        }
        if (databaseType.equals("Sybase")) {
            url = "jdbc:sybase:Tds:" + ip + ":" + port + "/" + dbName;
        }
        if (databaseType.equals("Kingbase")) {
            url = "jdbc:kingbase://" + ip + ":" + port + "/" + dbName;
        }
        if (databaseType.equals("Informix")) {
            String[] str = dbName.split("@");
            String informixDbName = str[0];
            String informixserver = str[1];
            url = "jdbc:informix-sqli://" + ip + ":" + port + "/" + informixDbName + ":informixserver=" + informixserver + ";NEWCODESET=utf8,8859-1,819;CLIENT_LOCALE=en_US.utf8;DB_LOCALE=en_US.8859-1;IFX_USE_STRENC=true;";
        }
        if (databaseType.equals("ClickHouse")) {
            url = "jdbc:clickhouse://" + ip + ":" + port + "/" + dbName;
        }
        if (databaseType.equals("Redshift")) {
            url = "jdbc:redshift://" + ip + ":" + port + "/" + dbName;
        }
        if (databaseType.equals("ES")) {
            url = "jdbc:es://http://" + ip + ":" + port + "/";
        }
        return url;
    }
}
