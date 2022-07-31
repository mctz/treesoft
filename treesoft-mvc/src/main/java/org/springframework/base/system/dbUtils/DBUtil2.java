package org.springframework.base.system.dbUtils;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import oracle.sql.BLOB;
import oracle.sql.CLOB;
import org.apache.commons.lang3.StringUtils;
import org.springframework.base.system.dynamicDataSource.DynamicDataSourceDruid;
import org.springframework.base.system.utils.Constants;
import org.springframework.base.system.utils.DataUtil;
import org.springframework.base.system.utils.ExcelUtil;
import org.springframework.base.system.utils.HttpUtil;
import org.springframework.base.system.utils.LogUtil;
import redis.clients.jedis.Jedis;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/dbUtils/DBUtil2.class */
public class DBUtil2 {
    private String databaseConfigId;
    private String databaseName;

    public DBUtil2(String dbName, String databaseConfigId) {
        this.databaseConfigId = databaseConfigId;
        this.databaseName = dbName;
    }

    public Connection getConnection() throws Exception {
        try {
            String poolName = String.valueOf(this.databaseConfigId) + Constants.POOL_NAME_SPLIT + this.databaseName;
            Connection conn = DynamicDataSourceDruid.getDataSource(poolName).getConnection();
            return conn;
        } catch (Exception e) {
            Map<String, Object> map = Constants.configStaticMap.get(this.databaseConfigId);
            String jdbc_url = map.get("url").toString();
            LogUtil.e("数据库连接出错 , url=" + jdbc_url, e);
            throw new Exception("数据库连接出错,请及时检查 ");
        }
    }

    public Connection getConnectionForQuartzJob() throws Exception {
        try {
            String poolName = String.valueOf(this.databaseConfigId) + Constants.POOL_NAME_SPLIT + this.databaseName;
            Connection connQuartzJob = DynamicDataSourceDruid.getDataSource(poolName).getConnection();
            return connQuartzJob;
        } catch (Exception e) {
            Map<String, Object> map = Constants.configStaticMap.get(this.databaseConfigId);
            String jdbc_url = map.get("url").toString();
            LogUtil.e("数据同步 , 数据库连接出错 , url=" + jdbc_url, e);
            throw new Exception(String.valueOf(this.databaseName) + " 数据库连接出错，请检查连接配置信息。");
        }
    }

    public static boolean testConnection(String databaseType2, String databaseName2, String ip2, String port2, String user2, String pass2) {
        MongoClientURI uri;
        try {
            String url2 = "";
            if (databaseType2.equals("MySQL") || databaseType2.equals("TiDB")) {
                Class.forName("com.mysql.jdbc.Driver");
                url2 = "jdbc:mysql://" + ip2 + ":" + port2 + "/" + databaseName2 + "?characterEncoding=utf8&tinyInt1isBit=false&useSSL=false&serverTimezone=GMT%2B8";
            }
            if (databaseType2.equals("MariaDB")) {
                Class.forName("com.mysql.jdbc.Driver");
                url2 = "jdbc:mysql://" + ip2 + ":" + port2 + "/" + databaseName2 + "?characterEncoding=utf8&tinyInt1isBit=false&useSSL=false&serverTimezone=GMT%2B8";
            }
            if (databaseType2.equals("MySQL8.0")) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                url2 = "jdbc:mysql://" + ip2 + ":" + port2 + "/" + databaseName2 + "?useUnicode=true&characterEncoding=utf8&tinyInt1isBit=false&yearIsDateType=false&useSSL=false&serverTimezone=GMT%2B8";
            }
            if (databaseType2.equals("Oracle")) {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                url2 = "jdbc:oracle:thin:@//" + ip2 + ":" + port2 + "/" + databaseName2;
            }
            if (databaseType2.equals("HANA2")) {
                Class.forName("com.sap.db.jdbc.Driver");
                url2 = "jdbc:sap://" + ip2 + ":" + port2 + "?reconnect=true";
            }
            if (databaseType2.equals("PostgreSQL")) {
                Class.forName("org.postgresql.Driver");
                url2 = "jdbc:postgresql://" + ip2 + ":" + port2 + "/" + databaseName2;
            }
            if (databaseType2.equals("SQL Server")) {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                url2 = "jdbc:sqlserver://" + ip2 + ":" + port2 + ";database=" + databaseName2;
            }
            if (databaseType2.equals("Hive2")) {
                Class.forName("org.apache.hive.jdbc.HiveDriver");
                url2 = "jdbc:hive2://" + ip2 + ":" + port2 + "/" + databaseName2;
            }
            if (databaseType2.equals("Impala")) {
                Class.forName("com.cloudera.impala.jdbc41.Driver");
                url2 = "jdbc:impala://" + ip2 + ":" + port2 + "/" + databaseName2;
            }
            if (databaseType2.equals("Cache")) {
                Class.forName("com.intersys.jdbc.CacheDriver");
                url2 = "jdbc:Cache://" + ip2 + ":" + port2 + "/" + databaseName2;
            }
            if (databaseType2.equals("ClickHouse")) {
                Class.forName("ru.yandex.clickhouse.ClickHouseDriver");
                url2 = "jdbc:clickhouse://" + ip2 + ":" + port2 + "/" + databaseName2;
            }
            if (databaseType2.equals("DB2")) {
                Class.forName("com.ibm.db2.jcc.DB2Driver");
                url2 = "jdbc:db2://" + ip2 + ":" + port2 + "/" + databaseName2;
            }
            if (databaseType2.equals("DM7")) {
                Class.forName("dm.jdbc.driver.DmDriver");
                url2 = "jdbc:dm://" + ip2 + ":" + port2 + "/" + databaseName2;
            }
            if (databaseType2.equals("ShenTong")) {
                Class.forName("com.oscar.Driver");
                url2 = "jdbc:oscar://" + ip2 + ":" + port2 + "/" + databaseName2;
            }
            if (databaseType2.equals("Sybase")) {
                Class.forName("com.sybase.jdbc4.jdbc.SybDriver");
                url2 = "jdbc:sybase:Tds:" + ip2 + ":" + port2 + "/" + databaseName2;
            }
            if (databaseType2.equals("Kingbase")) {
                Class.forName("com.kingbase.Driver");
                url2 = "jdbc:kingbase://" + ip2 + ":" + port2 + "/" + databaseName2;
            }
            if (databaseType2.equals("Informix")) {
                String[] str = databaseName2.split("@");
                String informixDbName = str[0];
                String informixserver = str[1];
                Class.forName("com.informix.jdbc.IfxDriver");
                url2 = "jdbc:informix-sqli://" + ip2 + ":" + port2 + "/" + informixDbName + ":informixserver=" + informixserver + ";";
            }
            if (databaseType2.equals("Redshift")) {
                Class.forName("com.amazon.redshift.jdbc41.Driver");
                url2 = "jdbc:redshift://" + ip2 + ":" + port2 + "/" + databaseName2;
            }
            if (databaseType2.equals("ES")) {
                String httpUrl = "http://" + ip2 + ":" + port2 + "/_cat/indices/?format=json";
                String resultStr = HttpUtil.getHttpMethodAuth(httpUrl, "GET", user2, pass2);
                if (StringUtils.isEmpty(resultStr)) {
                    return false;
                }
                return true;
            } else if (databaseType2.equals("Redis")) {
                Jedis jedis = new Jedis(ip2, Integer.parseInt(port2), 10000);
                if (StringUtils.isNotEmpty(pass2)) {
                    jedis.auth(pass2);
                }
                List<String> dbs = jedis.configGet("databases");
                if (dbs.size() >= 0) {
                    jedis.close();
                    return true;
                }
                return false;
            } else if (databaseType2.equals("MongoDB")) {
                if (user2.equals("")) {
                    uri = new MongoClientURI("mongodb://" + ip2 + ":" + port2);
                } else {
                    uri = new MongoClientURI("mongodb://" + user2 + ":" + pass2 + "@" + ip2 + ":" + port2 + "/" + databaseName2);
                }
                MongoClient mongoClient = new MongoClient(uri);
                MongoDatabase database = mongoClient.getDatabase(databaseName2);
                MongoIterable<String> colls = database.listCollectionNames();
                if (colls.iterator().hasNext()) {
                    if (mongoClient != null) {
                        mongoClient.close();
                        return true;
                    }
                    return true;
                } else if (mongoClient != null) {
                    mongoClient.close();
                    return false;
                } else {
                    return false;
                }
            } else {
                Connection conn = DriverManager.getConnection(url2, user2, pass2);
                if (conn != null) {
                    return true;
                }
                return false;
            }
        } catch (Exception e) {
            LogUtil.e("测试连接出错，", e);
            return false;
        }
    }

    public int setupdateData(String sql) throws Exception {
        Connection conn = getConnection();
        PreparedStatement preStatement = null;
        try {
            try {
                preStatement = conn.prepareStatement(sql);
                int executeUpdate = preStatement.executeUpdate();
                try {
                    preStatement.close();
                    conn.close();
                    return executeUpdate;
                } catch (SQLException e) {
                    LogUtil.e(e);
                    throw new Exception(e.getMessage());
                }
            } catch (Exception e2) {
                LogUtil.e(e2);
                throw new Exception(e2.getMessage());
            }
        } catch (Throwable th) {
            try {
                preStatement.close();
                conn.close();
                throw th;
            } catch (SQLException e3) {
                LogUtil.e(e3);
                throw new Exception(e3.getMessage());
            }
        }
    }

    public Map<String, Object> executeSqlForProcedure(String sql) throws Exception {
        Connection conn = getConnection();
        CallableStatement cs = null;
        Map<String, Object> map = new HashMap<>();
        try {
            try {
                cs = conn.prepareCall("{" + sql + "}");
                cs.executeQuery();
                try {
                    cs.close();
                    conn.close();
                    return map;
                } catch (SQLException e) {
                    LogUtil.e(e);
                    throw new Exception(e.getMessage());
                }
            } catch (Exception e2) {
                LogUtil.e(e2);
                throw new Exception(e2.getMessage());
            }
        } catch (Throwable th) {
            try {
                cs.close();
                conn.close();
                throw th;
            } catch (SQLException e3) {
                LogUtil.e(e3);
                throw new Exception(e3.getMessage());
            }
        }
    }

    public int updateExecuteBatch(List<String> sqlList) throws Exception {
        Connection conn = getConnectionForQuartzJob();
        Statement stmt = null;
        try {
            try {
                conn.setAutoCommit(false);
                stmt = conn.createStatement();
                for (int i = 0; i < sqlList.size(); i++) {
                    String sql = sqlList.get(i);
                    if (sql.endsWith(";")) {
                        sql = sql.substring(0, sql.length() - 1);
                    }
                    stmt.addBatch(sql);
                    if (i > 1 && (i + 1) % 1000 == 0) {
                        stmt.executeBatch();
                        conn.commit();
                        stmt.clearBatch();
                    }
                }
                stmt.executeBatch();
                conn.commit();
                int size = sqlList.size();
                try {
                    stmt.close();
                    conn.close();
                    return size;
                } catch (SQLException e) {
                    LogUtil.e(e.getMessage());
                    throw new Exception(e.getMessage());
                }
            } catch (Exception e2) {
                conn.rollback();
                LogUtil.e("批量  新增和更新的操作出错，", e2);
                throw new Exception(e2.getMessage());
            }
        } catch (Throwable th) {
            try {
                stmt.close();
                conn.close();
                throw th;
            } catch (SQLException e3) {
                LogUtil.e(e3.getMessage());
                throw new Exception(e3.getMessage());
            }
        }
    }

    public int updateExecuteBatchForCK(List<Map<String, Object>> dataList, String databaseName, String tableName) throws Exception {
        Iterator<Map.Entry<String, Object>> it;
        Connection conn = getConnectionForQuartzJob();
        PreparedStatement stmt = null;
        try {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf2 = new SimpleDateFormat(ExcelUtil.DATE_FORMAT);
                String colums = " ";
                String values = " ";
                String insertSQL = " insert into " + tableName;
                Map<String, Object> mapTemp = dataList.get(0);
                while (mapTemp.entrySet().iterator().hasNext()) {
                    colums = String.valueOf(colums) + it.next().getKey() + ",";
                    values = String.valueOf(values) + " ?,";
                }
                stmt = conn.prepareStatement(String.valueOf(insertSQL) + " ( " + colums.substring(0, colums.length() - 1) + ") VALUES (" + values.substring(0, values.length() - 1) + " ) ");
                for (int i = 0; i < dataList.size(); i++) {
                    Map<String, Object> map4 = dataList.get(i);
                    int z = 1;
                    Iterator<Map.Entry<String, Object>> it2 = map4.entrySet().iterator();
                    while (it2.hasNext()) {
                        Map.Entry<String, Object> entry = it2.next();
                        if (entry.getValue() == null) {
                            stmt.setString(z, null);
                        } else if (entry.getValue() instanceof Date) {
                            stmt.setString(z, sdf.format(entry.getValue()));
                        } else if ((entry.getValue() instanceof Time) || (entry.getValue() instanceof Timestamp)) {
                            stmt.setString(z, sdf2.format(entry.getValue()));
                        } else if (entry.getValue() instanceof Integer) {
                            stmt.setInt(z, Integer.parseInt(entry.getValue().toString()));
                        } else if ((entry.getValue() instanceof Float) || (entry.getValue() instanceof Long) || (entry.getValue() instanceof BigInteger) || (entry.getValue() instanceof Double) || (entry.getValue() instanceof BigDecimal)) {
                            stmt.setString(z, new StringBuilder().append(entry.getValue()).toString());
                        } else if (entry.getValue() instanceof Boolean) {
                            stmt.setString(z, new StringBuilder().append(entry.getValue()).toString());
                        } else if (entry.getValue() instanceof Byte) {
                            stmt.setString(z, new StringBuilder().append(entry.getValue()).toString());
                        } else if (entry.getValue() instanceof ArrayList) {
                            stmt.setString(z, entry.getValue().toString());
                        } else {
                            stmt.setString(z, entry.getValue().toString());
                        }
                        z++;
                    }
                    stmt.addBatch();
                    if (i > 1 && (i + 1) % 1000 == 0) {
                        stmt.executeBatch();
                        stmt.clearBatch();
                    }
                }
                stmt.executeBatch();
                try {
                    stmt.close();
                    conn.close();
                    return 0;
                } catch (SQLException e) {
                    LogUtil.e(" 出错了，" + e.getMessage());
                    throw new Exception(e.getMessage());
                }
            } catch (Exception e2) {
                LogUtil.e("批量  新增和更新的操作出错，", e2);
                throw new Exception(e2.getMessage());
            }
        } catch (Throwable th) {
            try {
                stmt.close();
                conn.close();
                throw th;
            } catch (SQLException e3) {
                LogUtil.e(" 出错了，" + e3.getMessage());
                throw new Exception(e3.getMessage());
            }
        }
    }

    public List<Map<String, Object>> queryForListForOracle(String sql, int limitFrom, int pageSize) throws Exception {
        List<String> times = new ArrayList<>();
        List<String> clob = new ArrayList<>();
        List<String> binary = new ArrayList<>();
        List<Map<String, Object>> rows = new ArrayList<>();
        Connection conn = getConnection();
        int endNum = limitFrom + pageSize;
        String sql2 = "SELECT * FROM (SELECT A.*, ROWNUM RN  FROM (  " + sql + " ) A  WHERE ROWNUM <= " + endNum + "  ) \tWHERE RN >= " + limitFrom;
        if (sql.toLowerCase().indexOf("show") == 0 || sql.toLowerCase().indexOf("explain") == 0) {
            sql2 = sql;
        }
        PreparedStatement pstmt = conn.prepareStatement(sql2);
        ResultSet rs = pstmt.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        int maxSize = rsmd.getColumnCount();
        String[] fields = new String[maxSize];
        for (int i = 0; i < maxSize; i++) {
            fields[i] = rsmd.getColumnLabel(i + 1);
            if ("java.sql.Timestamp".equals(rsmd.getColumnClassName(i + 1)) || "oracle.sql.TIMESTAMP".equals(rsmd.getColumnClassName(i + 1))) {
                times.add(fields[i]);
            }
            if ("oracle.jdbc.OracleClob".equals(rsmd.getColumnClassName(i + 1)) || "oracle.jdbc.OracleBlob".equals(rsmd.getColumnClassName(i + 1))) {
                clob.add(fields[i]);
            }
            if ("[B".equals(rsmd.getColumnClassName(i + 1))) {
                binary.add(fields[i]);
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat(ExcelUtil.DATE_FORMAT);
        while (rs.next()) {
            Map row = new LinkedHashMap();
            for (int i2 = 0; i2 < maxSize; i2++) {
                Object value = times.contains(fields[i2]) ? rs.getTimestamp(fields[i2]) : rs.getObject(fields[i2]);
                if (times.contains(fields[i2]) && value != null) {
                    value = sdf.format(value);
                }
                if (clob.contains(fields[i2]) && value != null) {
                    value = "(Blob)";
                }
                if (binary.contains(fields[i2]) && value != null) {
                    value = new String((byte[]) value);
                }
                row.put(fields[i2], value);
            }
            rows.add(row);
        }
        try {
            rs.close();
            pstmt.close();
            conn.close();
            return rows;
        } catch (SQLException e) {
            LogUtil.e("取Oracle数据出错，", e);
            throw new Exception("取Oracle数据出错，" + e.getMessage());
        }
    }

    public List<Map<String, Object>> queryForListForOracleForExport(String sql, int limitFrom, int pageSize) throws Exception {
        List<String> times = new ArrayList<>();
        List<String> clob = new ArrayList<>();
        List<String> blob = new ArrayList<>();
        List<String> binary = new ArrayList<>();
        List<Map<String, Object>> rows = new ArrayList<>();
        Connection conn = getConnection();
        int endNum = limitFrom + pageSize;
        String sql2 = "SELECT * FROM (SELECT A.*, ROWNUM RN  FROM ( " + sql + " ) A  WHERE ROWNUM <= " + endNum + "  ) \tWHERE RN >= " + limitFrom;
        PreparedStatement pstmt = conn.prepareStatement(sql2);
        ResultSet rs = pstmt.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        int maxSize = rsmd.getColumnCount();
        String[] fields = new String[maxSize];
        for (int i = 0; i < maxSize; i++) {
            fields[i] = rsmd.getColumnLabel(i + 1);
            if ("java.sql.Timestamp".equals(rsmd.getColumnClassName(i + 1)) || "oracle.sql.TIMESTAMP".equals(rsmd.getColumnClassName(i + 1))) {
                times.add(fields[i]);
            }
            if ("oracle.sql.CLOB".equals(rsmd.getColumnClassName(i + 1)) || "oracle.jdbc.OracleClob".equals(rsmd.getColumnClassName(i + 1))) {
                clob.add(fields[i]);
            }
            if ("oracle.sql.BLOB".equals(rsmd.getColumnClassName(i + 1)) || "oracle.jdbc.OracleBlob".equals(rsmd.getColumnClassName(i + 1))) {
                blob.add(fields[i]);
            }
            if ("[B".equals(rsmd.getColumnClassName(i + 1))) {
                binary.add(fields[i]);
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat(ExcelUtil.DATE_FORMAT);
        while (rs.next()) {
            Map row = new LinkedHashMap();
            for (int i2 = 0; i2 < maxSize; i2++) {
                if (!fields[i2].equals("RN")) {
                    Object value = times.contains(fields[i2]) ? rs.getTimestamp(fields[i2]) : rs.getObject(fields[i2]);
                    if (times.contains(fields[i2]) && value != null) {
                        value = sdf.format(value);
                    }
                    try {
                        if (blob.contains(fields[i2]) && value != null) {
                            BLOB blobTemp = (BLOB) value;
                            value = byteToHex(blobTemp.getBytes());
                        }
                        if (clob.contains(fields[i2]) && value != null) {
                            CLOB clobTemp = (CLOB) value;
                            Object str = clobTemp == null ? null : clobTemp.getSubString(1L, (int) clobTemp.length());
                            value = str;
                        }
                        if (binary.contains(fields[i2]) && value != null) {
                            value = byteToHex((byte[]) value);
                        }
                    } catch (Exception e) {
                        LogUtil.e("Oracle查询出结果集 ，BLOB大字段 转换 出错了，", e);
                        value = new String("");
                    }
                    row.put(fields[i2], value);
                }
            }
            rows.add(row);
        }
        try {
            rs.close();
            pstmt.close();
            conn.close();
            return rows;
        } catch (SQLException e2) {
            LogUtil.e("取数据出错，", e2);
            throw new Exception("取数据出错，" + e2.getMessage());
        }
    }

    public List<Map<String, Object>> queryForListForHive2(String sql, int limitFrom, int pageSize) throws Exception {
        List<String> times = new ArrayList<>();
        List<Map<String, Object>> rows = new ArrayList<>();
        Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        int maxRow = limitFrom + pageSize;
        pstmt.setMaxRows(maxRow);
        ResultSet rs = pstmt.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        int maxSize = rsmd.getColumnCount();
        String[] fields = new String[maxSize];
        for (int i = 0; i < maxSize; i++) {
            fields[i] = rsmd.getColumnLabel(i + 1);
            if ("java.sql.Timestamp".equals(rsmd.getColumnClassName(i + 1))) {
                times.add(fields[i]);
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat(ExcelUtil.DATE_FORMAT);
        while (rs.next()) {
            Map row = new LinkedHashMap();
            for (int i2 = 0; i2 < maxSize; i2++) {
                Object value = times.contains(fields[i2]) ? rs.getTimestamp(fields[i2]) : rs.getObject(fields[i2]);
                if (times.contains(fields[i2]) && value != null) {
                    value = sdf.format(value);
                }
                row.put(fields[i2], value);
            }
            rows.add(row);
        }
        try {
            rs.close();
            pstmt.close();
            conn.close();
            return rows;
        } catch (SQLException e) {
            LogUtil.e("按SQL查询出结果集出错了，", e);
            throw new Exception(e.getMessage());
        }
    }

    public List<Map<String, Object>> queryForListCommonMethod(String sql) throws Exception {
        List<Map<String, Object>> resultList = new ArrayList<>();
        try {
            Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> resultMap = new LinkedHashMap<>();
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                for (int i = 0; i < columnCount; i++) {
                    resultMap.put(metaData.getColumnLabel(i + 1), rs.getObject(i + 1));
                }
                resultList.add(resultMap);
            }
            rs.close();
            pstmt.close();
            conn.close();
            return resultList;
        } catch (SQLException e) {
            LogUtil.e("通用方法，按SQL查询出结果集出错，sql=" + sql, e);
            throw new Exception(e.getMessage());
        }
    }

    public List<Map<String, Object>> queryForListForMySql(String sql) throws Exception {
        List<String> times = new ArrayList<>();
        List<String> binary = new ArrayList<>();
        List<String> object = new ArrayList<>();
        List<String> bigInt = new ArrayList<>();
        List<String> longType = new ArrayList<>();
        List<String> stringType = new ArrayList<>();
        List<String> clobType = new ArrayList<>();
        List<Map<String, Object>> rows = new ArrayList<>();
        Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        int maxSize = rsmd.getColumnCount();
        String[] fields = new String[maxSize];
        for (int i = 0; i < maxSize; i++) {
            fields[i] = rsmd.getColumnLabel(i + 1);
            if ("java.sql.Timestamp".equals(rsmd.getColumnClassName(i + 1)) || "oracle.sql.TIMESTAMP".equals(rsmd.getColumnClassName(i + 1))) {
                times.add(fields[i]);
            }
            if ("java.lang.Object".equals(rsmd.getColumnClassName(i + 1))) {
                object.add(fields[i]);
            }
            if ("[B".equals(rsmd.getColumnClassName(i + 1))) {
                binary.add(fields[i]);
            }
            if ("java.sql.Blob".equals(rsmd.getColumnClassName(i + 1))) {
                binary.add(fields[i]);
            }
            if ("java.lang.Long".equals(rsmd.getColumnClassName(i + 1))) {
                longType.add(fields[i]);
            }
            if ("java.math.BigInteger".equals(rsmd.getColumnClassName(i + 1))) {
                bigInt.add(fields[i]);
            }
            if ("java.lang.String".equals(rsmd.getColumnClassName(i + 1))) {
                stringType.add(fields[i]);
            }
            if ("java.sql.Clob".equals(rsmd.getColumnClassName(i + 1)) || "oracle.sql.CLOB".equals(rsmd.getColumnClassName(i + 1)) || "oracle.jdbc.OracleClob".equals(rsmd.getColumnClassName(i + 1))) {
                clobType.add(fields[i]);
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat(ExcelUtil.DATE_FORMAT);
        while (rs.next()) {
            Map row = new LinkedHashMap();
            for (int i2 = 0; i2 < maxSize; i2++) {
                Object value = times.contains(fields[i2]) ? rs.getTimestamp(fields[i2]) : rs.getObject(fields[i2]);
                if (times.contains(fields[i2]) && value != null) {
                    value = sdf.format(value);
                }
                try {
                    if (binary.contains(fields[i2]) && value != null) {
                        value = "(BLOB)";
                    }
                    if (clobType.contains(fields[i2]) && value != null) {
                        value = "(长文本)";
                    }
                    if (object.contains(fields[i2]) && value != null) {
                        value = value.toString();
                    }
                    if (longType.contains(fields[i2]) && value != null) {
                        value = value.toString();
                    }
                    if (bigInt.contains(fields[i2]) && value != null) {
                        value = value.toString();
                    }
                    if (stringType.contains(fields[i2]) && value != null) {
                        value = value.toString();
                        if (value.toString().length() > 501) {
                            value = String.valueOf(value.toString().substring(0, 500)) + "......";
                        }
                    }
                } catch (Exception e) {
                    value = "<Object>";
                }
                row.put(fields[i2], value);
            }
            rows.add(row);
        }
        try {
            rs.close();
            pstmt.close();
            conn.close();
            return rows;
        } catch (SQLException e2) {
            LogUtil.e("取数据出错，", e2);
            throw new Exception("取数据出错，" + e2.getMessage());
        }
    }

    public List<Map<String, Object>> queryForListForMySqlForExport(String sql, int limitFrom, int pageSize) throws Exception {
        List<String> times = new ArrayList<>();
        List<String> binary = new ArrayList<>();
        List<String> object = new ArrayList<>();
        List<Map<String, Object>> rows = new ArrayList<>();
        Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement("select * from (" + sql + ") tab limit " + limitFrom + "," + pageSize);
        ResultSet rs = pstmt.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        int maxSize = rsmd.getColumnCount();
        String[] fields = new String[maxSize];
        for (int i = 0; i < maxSize; i++) {
            fields[i] = rsmd.getColumnLabel(i + 1);
            if ("java.sql.Timestamp".equals(rsmd.getColumnClassName(i + 1))) {
                times.add(fields[i]);
            }
            if ("java.lang.Object".equals(rsmd.getColumnClassName(i + 1))) {
                object.add(fields[i]);
            }
            if ("[B".equals(rsmd.getColumnClassName(i + 1))) {
                binary.add(fields[i]);
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat(ExcelUtil.DATE_FORMAT);
        while (rs.next()) {
            Map row = new LinkedHashMap();
            for (int i2 = 0; i2 < maxSize; i2++) {
                Object value = times.contains(fields[i2]) ? rs.getTimestamp(fields[i2]) : rs.getObject(fields[i2]);
                if (times.contains(fields[i2]) && value != null) {
                    value = sdf.format(value);
                }
                try {
                    if (binary.contains(fields[i2]) && value != null) {
                        value = byteToHex((byte[]) value);
                    }
                    if (object.contains(fields[i2]) && value != null) {
                        value = value.toString();
                    }
                } catch (Exception e) {
                    value = "(Object)";
                }
                row.put(fields[i2], value);
            }
            rows.add(row);
        }
        try {
            rs.close();
            pstmt.close();
            conn.close();
            return rows;
        } catch (SQLException e2) {
            LogUtil.e("取数据出错，" + e2);
            throw new Exception("取数据出错，" + e2);
        }
    }

    public List<Map<String, Object>> queryForListForDb2(String sql, int limitFrom, int pageSize) throws Exception {
        List<String> times = new ArrayList<>();
        List<String> binary = new ArrayList<>();
        List<String> object = new ArrayList<>();
        List<Map<String, Object>> rows = new ArrayList<>();
        Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(" SELECT * FROM ( SELECT B.*, ROWNUMBER() OVER() AS ROWNUMBER FROM (" + sql + ") AS B ) AS A WHERE A.ROWNUMBER BETWEEN " + (limitFrom + 1) + " AND " + (limitFrom + pageSize));
        ResultSet rs = pstmt.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        int maxSize = rsmd.getColumnCount();
        String[] fields = new String[maxSize];
        for (int i = 0; i < maxSize; i++) {
            fields[i] = rsmd.getColumnLabel(i + 1);
            if ("java.sql.Timestamp".equals(rsmd.getColumnClassName(i + 1))) {
                times.add(fields[i]);
            }
            if ("java.lang.Object".equals(rsmd.getColumnClassName(i + 1))) {
                object.add(fields[i]);
            }
            if ("[B".equals(rsmd.getColumnClassName(i + 1))) {
                binary.add(fields[i]);
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat(ExcelUtil.DATE_FORMAT);
        while (rs.next()) {
            Map row = new LinkedHashMap();
            for (int i2 = 0; i2 < maxSize; i2++) {
                if (!fields[i2].equals("ROWNUMBER")) {
                    Object value = times.contains(fields[i2]) ? rs.getTimestamp(fields[i2]) : rs.getObject(fields[i2]);
                    if (times.contains(fields[i2]) && value != null) {
                        value = sdf.format(value);
                    }
                    try {
                        if (binary.contains(fields[i2]) && value != null) {
                            value = "(BLOB)";
                        }
                        if (object.contains(fields[i2]) && value != null) {
                            value = value.toString();
                        }
                    } catch (Exception e) {
                        value = "(Object)";
                    }
                    row.put(fields[i2], value);
                }
            }
            rows.add(row);
        }
        try {
            rs.close();
            pstmt.close();
            conn.close();
            return rows;
        } catch (SQLException e2) {
            LogUtil.e("取数据出错，" + e2.getMessage());
            throw new Exception("取数据出错，" + e2.getMessage());
        }
    }

    public List<Map<String, Object>> queryForListForDm7(String sql, int limitFrom, int pageSize) throws Exception {
        List<String> times = new ArrayList<>();
        List<String> binary = new ArrayList<>();
        List<String> object = new ArrayList<>();
        List<Map<String, Object>> rows = new ArrayList<>();
        Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(" select * from ( " + sql + " ) tab  LIMIT " + limitFrom + "," + pageSize);
        ResultSet rs = pstmt.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        int maxSize = rsmd.getColumnCount();
        String[] fields = new String[maxSize];
        for (int i = 0; i < maxSize; i++) {
            fields[i] = rsmd.getColumnLabel(i + 1);
            if ("java.sql.Timestamp".equals(rsmd.getColumnClassName(i + 1))) {
                times.add(fields[i]);
            }
            if ("java.lang.Object".equals(rsmd.getColumnClassName(i + 1))) {
                object.add(fields[i]);
            }
            if ("[B".equals(rsmd.getColumnClassName(i + 1))) {
                binary.add(fields[i]);
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat(ExcelUtil.DATE_FORMAT);
        while (rs.next()) {
            Map row = new LinkedHashMap();
            for (int i2 = 0; i2 < maxSize; i2++) {
                Object value = times.contains(fields[i2]) ? rs.getTimestamp(fields[i2]) : rs.getObject(fields[i2]);
                if (times.contains(fields[i2]) && value != null) {
                    value = sdf.format(value);
                }
                try {
                    if (binary.contains(fields[i2]) && value != null) {
                        value = byteToHex((byte[]) value);
                    }
                    if (object.contains(fields[i2]) && value != null) {
                        value = value.toString();
                    }
                } catch (Exception e) {
                    value = "(Object)";
                }
                row.put(fields[i2], value);
            }
            rows.add(row);
        }
        try {
            rs.close();
            pstmt.close();
            conn.close();
            return rows;
        } catch (SQLException e2) {
            LogUtil.e("取数据出错，" + e2);
            throw new Exception("取数据出错，" + e2);
        }
    }

    public List<Map<String, Object>> queryForListForKingbase(String sql, int limitFrom, int pageSize) throws Exception {
        List<String> times = new ArrayList<>();
        List<String> binary = new ArrayList<>();
        List<String> object = new ArrayList<>();
        List<Map<String, Object>> rows = new ArrayList<>();
        Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(" select * from ( " + sql + " ) tab  LIMIT " + limitFrom + "," + pageSize);
        ResultSet rs = pstmt.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        int maxSize = rsmd.getColumnCount();
        String[] fields = new String[maxSize];
        for (int i = 0; i < maxSize; i++) {
            fields[i] = rsmd.getColumnLabel(i + 1);
            if ("java.sql.Timestamp".equals(rsmd.getColumnClassName(i + 1))) {
                times.add(fields[i]);
            }
            if ("java.lang.Object".equals(rsmd.getColumnClassName(i + 1))) {
                object.add(fields[i]);
            }
            if ("[B".equals(rsmd.getColumnClassName(i + 1))) {
                binary.add(fields[i]);
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat(ExcelUtil.DATE_FORMAT);
        while (rs.next()) {
            Map row = new LinkedHashMap();
            for (int i2 = 0; i2 < maxSize; i2++) {
                Object value = times.contains(fields[i2]) ? rs.getTimestamp(fields[i2]) : rs.getObject(fields[i2]);
                if (times.contains(fields[i2]) && value != null) {
                    value = sdf.format(value);
                }
                try {
                    if (binary.contains(fields[i2]) && value != null) {
                        value = byteToHex((byte[]) value);
                    }
                    if (object.contains(fields[i2]) && value != null) {
                        value = value.toString();
                    }
                } catch (Exception e) {
                    value = "(Object)";
                }
                row.put(fields[i2], value);
            }
            rows.add(row);
        }
        try {
            rs.close();
            pstmt.close();
            conn.close();
            return rows;
        } catch (SQLException e2) {
            LogUtil.e("取数据出错，" + e2);
            throw new Exception("取数据出错，" + e2);
        }
    }

    public List<Map<String, Object>> queryForListForPostgreSQL(String sql) throws Exception {
        List<String> times = new ArrayList<>();
        List<String> binary = new ArrayList<>();
        List<String> object = new ArrayList<>();
        List<Map<String, Object>> rows = new ArrayList<>();
        Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        int maxSize = rsmd.getColumnCount();
        String[] fields = new String[maxSize];
        for (int i = 0; i < maxSize; i++) {
            fields[i] = rsmd.getColumnLabel(i + 1);
            if ("java.sql.Timestamp".equals(rsmd.getColumnClassName(i + 1)) || "oracle.sql.TIMESTAMP".equals(rsmd.getColumnClassName(i + 1))) {
                times.add(fields[i]);
            }
            if ("java.lang.Object".equals(rsmd.getColumnClassName(i + 1))) {
                object.add(fields[i]);
            }
            if ("[B".equals(rsmd.getColumnClassName(i + 1))) {
                binary.add(fields[i]);
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat(ExcelUtil.DATE_FORMAT);
        while (rs.next()) {
            Map row = new LinkedHashMap();
            for (int i2 = 0; i2 < maxSize; i2++) {
                Object value = times.contains(fields[i2]) ? rs.getTimestamp(fields[i2]) : rs.getObject(fields[i2]);
                if (times.contains(fields[i2]) && value != null) {
                    value = sdf.format(value);
                }
                try {
                    if (binary.contains(fields[i2]) && value != null) {
                        value = new String((byte[]) value);
                    }
                    if (object.contains(fields[i2]) && value != null) {
                        value = value.toString();
                    }
                } catch (Exception e) {
                    value = "(Object)";
                }
                row.put(fields[i2], value);
            }
            rows.add(row);
        }
        try {
            rs.close();
            pstmt.close();
            conn.close();
            return rows;
        } catch (SQLException e2) {
            LogUtil.e("取数据出错，" + e2.getMessage());
            throw new Exception("取数据出错，" + e2.getMessage());
        }
    }

    public List<Map<String, Object>> queryForListForPostgreSQLForExport(String sql, int limitFrom, int pageSize) throws Exception {
        List<String> times = new ArrayList<>();
        List<String> binary = new ArrayList<>();
        List<String> object = new ArrayList<>();
        List<Map<String, Object>> rows = new ArrayList<>();
        Connection conn = getConnection();
        String sql2 = "select  *  from  (" + sql + ") t  limit " + pageSize + "   " + limitFrom;
        PreparedStatement pstmt = conn.prepareStatement(sql2);
        ResultSet rs = pstmt.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        int maxSize = rsmd.getColumnCount();
        String[] fields = new String[maxSize];
        for (int i = 0; i < maxSize; i++) {
            fields[i] = rsmd.getColumnLabel(i + 1);
            if ("java.sql.Timestamp".equals(rsmd.getColumnClassName(i + 1)) || "oracle.sql.TIMESTAMP".equals(rsmd.getColumnClassName(i + 1))) {
                times.add(fields[i]);
            }
            if ("java.lang.Object".equals(rsmd.getColumnClassName(i + 1))) {
                object.add(fields[i]);
            }
            if ("[B".equals(rsmd.getColumnClassName(i + 1))) {
                binary.add(fields[i]);
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat(ExcelUtil.DATE_FORMAT);
        while (rs.next()) {
            Map row = new LinkedHashMap();
            for (int i2 = 0; i2 < maxSize; i2++) {
                Object value = times.contains(fields[i2]) ? rs.getTimestamp(fields[i2]) : rs.getObject(fields[i2]);
                if (times.contains(fields[i2]) && value != null) {
                    value = sdf.format(value);
                }
                try {
                    if (binary.contains(fields[i2]) && value != null) {
                        value = new String((byte[]) value);
                    }
                    if (object.contains(fields[i2]) && value != null) {
                        value = value.toString();
                    }
                } catch (Exception e) {
                    value = "(Object)";
                }
                row.put(fields[i2], value);
            }
            rows.add(row);
        }
        try {
            rs.close();
            pstmt.close();
            conn.close();
            return rows;
        } catch (SQLException e2) {
            LogUtil.e("取数据出错，" + e2.getMessage());
            throw new Exception("取数据出错，" + e2.getMessage());
        }
    }

    public List<Map<String, Object>> queryForListForHana2(String sql, int limitFrom, int pageSize) throws Exception {
        List<Map<String, Object>> resultList = new ArrayList<>();
        try {
            Connection conn = getConnection();
            String sql2 = String.valueOf(sql) + "  LIMIT " + pageSize + " OFFSET  " + limitFrom;
            PreparedStatement pstmt = conn.prepareStatement(sql2);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> resultMap = new LinkedHashMap<>();
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                for (int i = 0; i < columnCount; i++) {
                    resultMap.put(metaData.getColumnLabel(i + 1), rs.getObject(i + 1));
                }
                resultList.add(resultMap);
            }
            rs.close();
            pstmt.close();
            conn.close();
            return resultList;
        } catch (SQLException e) {
            LogUtil.e("取数据出错，" + e.getMessage());
            throw new Exception("取数据出错，" + e.getMessage());
        }
    }

    public List<Map<String, Object>> queryForListForMSSQL(String sql, int limitFrom, int pageSize) throws Exception {
        List<String> times = new ArrayList<>();
        List<String> binary = new ArrayList<>();
        List<String> object = new ArrayList<>();
        List<Map<String, Object>> rows = new ArrayList<>();
        Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql, 1005, 1008);
        pstmt.setMaxRows(limitFrom + pageSize);
        ResultSet rs = pstmt.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        int maxSize = rsmd.getColumnCount();
        String[] fields = new String[maxSize];
        for (int i = 0; i < maxSize; i++) {
            fields[i] = rsmd.getColumnLabel(i + 1);
            if ("java.sql.Timestamp".equals(rsmd.getColumnClassName(i + 1))) {
                times.add(fields[i]);
            }
            if ("java.lang.Object".equals(rsmd.getColumnClassName(i + 1))) {
                object.add(fields[i]);
            }
            if ("[B".equals(rsmd.getColumnClassName(i + 1))) {
                binary.add(fields[i]);
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat(ExcelUtil.DATE_FORMAT);
        rs.absolute(limitFrom);
        while (rs.next()) {
            Map row = new LinkedHashMap();
            for (int i2 = 0; i2 < maxSize; i2++) {
                Object value = times.contains(fields[i2]) ? rs.getTimestamp(fields[i2]) : rs.getObject(fields[i2]);
                if (times.contains(fields[i2]) && value != null) {
                    value = sdf.format(value);
                }
                try {
                    if (binary.contains(fields[i2]) && value != null) {
                        value = "0x" + DataUtil.bytesToHexString((byte[]) value);
                    }
                    if (object.contains(fields[i2]) && value != null) {
                        value = value.toString();
                    }
                } catch (Exception e) {
                    value = "(Object)";
                }
                row.put(fields[i2], value);
            }
            rows.add(row);
        }
        try {
            rs.close();
            pstmt.close();
            conn.close();
            return rows;
        } catch (SQLException e2) {
            LogUtil.e("取数据出错，" + e2.getMessage());
            throw new Exception("取数据出错，" + e2.getMessage());
        }
    }

    public List<Map<String, Object>> queryForListPageForMSSQL(String sql, int maxRow, int beginIndex) throws Exception {
        List<String> times = new ArrayList<>();
        List<String> binary = new ArrayList<>();
        List<String> object = new ArrayList<>();
        List<Map<String, Object>> rows = new ArrayList<>();
        Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql, 1005, 1008);
        pstmt.setMaxRows(maxRow);
        ResultSet rs = pstmt.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        int maxSize = rsmd.getColumnCount();
        String[] fields = new String[maxSize];
        for (int i = 0; i < maxSize; i++) {
            fields[i] = rsmd.getColumnLabel(i + 1);
            if ("java.sql.Timestamp".equals(rsmd.getColumnClassName(i + 1))) {
                times.add(fields[i]);
            }
            if ("java.lang.Object".equals(rsmd.getColumnClassName(i + 1))) {
                object.add(fields[i]);
            }
            if ("[B".equals(rsmd.getColumnClassName(i + 1))) {
                binary.add(fields[i]);
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat(ExcelUtil.DATE_FORMAT);
        rs.absolute(beginIndex);
        while (rs.next()) {
            Map row = new HashMap();
            for (int i2 = 0; i2 < maxSize; i2++) {
                Object value = times.contains(fields[i2]) ? rs.getTimestamp(fields[i2]) : rs.getObject(fields[i2]);
                if (times.contains(fields[i2]) && value != null) {
                    value = sdf.format(value);
                }
                try {
                    if (binary.contains(fields[i2]) && value != null) {
                        value = "(Object)";
                    }
                    if (object.contains(fields[i2]) && value != null) {
                        value = "(Object)";
                    }
                } catch (Exception e) {
                    value = "(Object)";
                }
                row.put(fields[i2], value);
            }
            rows.add(row);
        }
        try {
            rs.close();
            pstmt.close();
            conn.close();
            return rows;
        } catch (SQLException e2) {
            LogUtil.e("MSSQL按SQL查询出结果集出错， " + e2);
            throw new Exception(e2.getMessage());
        }
    }

    public List<Map<String, Object>> executeSqlProcedureForMSSQL(String sql, int maxRow, int beginIndex) throws Exception {
        List<String> times = new ArrayList<>();
        List<Map<String, Object>> rows = new ArrayList<>();
        Connection conn = getConnection();
        CallableStatement callS = conn.prepareCall(sql, 1005, 1008);
        try {
            callS.setMaxRows(maxRow);
            ResultSet rs = callS.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int maxSize = rsmd.getColumnCount();
            String[] fields = new String[maxSize];
            for (int i = 0; i < maxSize; i++) {
                fields[i] = rsmd.getColumnLabel(i + 1);
                if ("java.sql.Timestamp".equals(rsmd.getColumnClassName(i + 1))) {
                    times.add(fields[i]);
                }
            }
            SimpleDateFormat sdf = new SimpleDateFormat(ExcelUtil.DATE_FORMAT);
            rs.absolute(beginIndex);
            while (rs.next()) {
                Map row = new HashMap();
                for (int i2 = 0; i2 < maxSize; i2++) {
                    Object value = times.contains(fields[i2]) ? rs.getTimestamp(fields[i2]) : rs.getObject(fields[i2]);
                    if (times.contains(fields[i2]) && value != null) {
                        value = sdf.format(value);
                    }
                    row.put(fields[i2], value);
                }
                rows.add(row);
            }
            rs.close();
        } catch (Exception e) {
            LogUtil.e("MSSQL存储过程执行出错，" + e);
            throw new Exception("存储过程执行出错，" + e.getMessage());
        } catch (SQLServerException e2) {
        }
        try {
            callS.close();
            conn.close();
            return rows;
        } catch (SQLException e3) {
            LogUtil.e(e3.getMessage());
            throw new Exception(e3.getMessage());
        }
    }

    public List<Map<String, Object>> executeSqlProcedureForColumnsForMSSQL(String sql) throws Exception {
        List<Map<String, Object>> columnRows = new ArrayList<>();
        Connection conn = getConnection();
        CallableStatement callS = conn.prepareCall(sql, 1005, 1008);
        callS.setMaxRows(1);
        ResultSet rs = callS.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        int maxSize = rsmd.getColumnCount();
        String[] fields = new String[maxSize];
        new ArrayList();
        for (int i = 0; i < maxSize; i++) {
            fields[i] = rsmd.getColumnLabel(i + 1);
            Map<String, Object> map = new HashMap<>();
            map.put("column_name", fields[i]);
            columnRows.add(map);
        }
        try {
            rs.close();
            callS.close();
            conn.close();
            return columnRows;
        } catch (SQLException e) {
            LogUtil.e(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    public int executeQueryForCountProcedureForMSSQL(String sql) throws Exception {
        Connection conn = getConnection();
        CallableStatement callS = conn.prepareCall(sql);
        ResultSet rs = callS.executeQuery();
        int countNum = 0;
        while (rs.next()) {
            countNum++;
        }
        try {
            rs.close();
            callS.close();
            conn.close();
            return countNum;
        } catch (SQLException e) {
            LogUtil.e(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    public List<Map<String, Object>> queryForListPageForHive2(String sql, int maxRow, int beginIndex) throws Exception {
        List<String> times = new ArrayList<>();
        List<Map<String, Object>> rows = new ArrayList<>();
        Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setMaxRows(maxRow);
        ResultSet rs = pstmt.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        int maxSize = rsmd.getColumnCount();
        String[] fields = new String[maxSize];
        for (int i = 0; i < maxSize; i++) {
            fields[i] = rsmd.getColumnLabel(i + 1);
            if ("java.sql.Timestamp".equals(rsmd.getColumnClassName(i + 1))) {
                times.add(fields[i]);
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat(ExcelUtil.DATE_FORMAT);
        while (rs.next()) {
            Map row = new LinkedHashMap();
            for (int i2 = 0; i2 < maxSize; i2++) {
                Object value = times.contains(fields[i2]) ? rs.getTimestamp(fields[i2]) : rs.getObject(fields[i2]);
                if (times.contains(fields[i2]) && value != null) {
                    value = sdf.format(value);
                }
                row.put(fields[i2], value);
            }
            rows.add(row);
        }
        try {
            rs.close();
            pstmt.close();
            conn.close();
            return rows;
        } catch (SQLException e) {
            LogUtil.e(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    public List<Map<String, Object>> queryForListWithType(String sql) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Map<String, Object>> rows2 = new ArrayList<>();
        try {
            try {
                conn = getConnection();
                pstmt = conn.prepareStatement(sql);
                rs = pstmt.executeQuery();
                ResultSetMetaData rsme = rs.getMetaData();
                int columnCount = rsme.getColumnCount();
                rs.next();
                for (int i = 1; i < columnCount + 1; i++) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("column_name", rsme.getColumnName(i));
                    map.put("column_value", rs.getObject(rsme.getColumnName(i)));
                    map.put("data_type", rsme.getColumnTypeName(i));
                    map.put("precision", Integer.valueOf(rsme.getPrecision(i)));
                    map.put("isAutoIncrement", Boolean.valueOf(rsme.isAutoIncrement(i)));
                    map.put("is_nullable", Integer.valueOf(rsme.isNullable(i)));
                    map.put("isReadOnly", Boolean.valueOf(rsme.isReadOnly(i)));
                    rows2.add(map);
                }
                try {
                    rs.close();
                    pstmt.close();
                    conn.close();
                } catch (SQLException e) {
                }
            } catch (Exception e2) {
                System.out.println("queryForListWithType  " + e2.getMessage());
                try {
                    rs.close();
                    pstmt.close();
                    conn.close();
                } catch (SQLException e3) {
                }
            }
            return rows2;
        } catch (Throwable th) {
            try {
                rs.close();
                pstmt.close();
                conn.close();
            } catch (SQLException e4) {
            }
            throw th;
        }
    }

    public List<Map<String, Object>> queryForColumnOnly(String sql) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Map<String, Object>> rows2 = new ArrayList<>();
        try {
            try {
                conn = getConnection();
                pstmt = conn.prepareStatement(sql);
                rs = pstmt.executeQuery();
                ResultSetMetaData rsme = rs.getMetaData();
                int columnCount = rsme.getColumnCount();
                for (int i = 1; i < columnCount + 1; i++) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("column_name", rsme.getColumnName(i));
                    map.put("data_type", rsme.getColumnTypeName(i));
                    map.put("precision", Integer.valueOf(rsme.getPrecision(i)));
                    map.put("isAutoIncrement", Boolean.valueOf(rsme.isAutoIncrement(i)));
                    map.put("is_nullable", Integer.valueOf(rsme.isNullable(i)));
                    map.put("isReadOnly", Boolean.valueOf(rsme.isReadOnly(i)));
                    rows2.add(map);
                }
                try {
                    rs.close();
                    pstmt.close();
                    conn.close();
                } catch (SQLException e) {
                    LogUtil.e(e.getMessage());
                }
            } catch (Exception e2) {
                LogUtil.e("queryForColumnOnly ," + e2);
                try {
                    rs.close();
                    pstmt.close();
                    conn.close();
                } catch (SQLException e3) {
                    LogUtil.e(e3.getMessage());
                }
            }
            return rows2;
        } catch (Throwable th) {
            try {
                rs.close();
                pstmt.close();
                conn.close();
            } catch (SQLException e4) {
                LogUtil.e(e4.getMessage());
            }
            throw th;
        }
    }

    public List<Map<String, Object>> executeSqlForColumns(String sql) throws Exception {
        List<Map<String, Object>> rows = new ArrayList<>();
        Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        int maxSize = rsmd.getColumnCount();
        for (int i = 0; i < maxSize; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("column_name", rsmd.getColumnLabel(i + 1));
            map.put("data_type", rsmd.getColumnTypeName(i + 1));
            rows.add(map);
        }
        rs.close();
        pstmt.close();
        conn.close();
        return rows;
    }

    public int executeQueryForCount(String sql) throws Exception {
        int rowCount = 0;
        Connection conn = getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            try {
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    Object count = rs.getObject("count(*)");
                    rowCount = Integer.parseInt(count.toString());
                }
                try {
                    rs.close();
                    stmt.close();
                    conn.close();
                } catch (SQLException e) {
                }
            } catch (Exception e2) {
                LogUtil.e(e2);
                try {
                    rs.close();
                    stmt.close();
                    conn.close();
                } catch (SQLException e3) {
                }
            }
            return rowCount;
        } catch (Throwable th) {
            try {
                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException e4) {
            }
            throw th;
        }
    }

    public int executeQueryForCountForPostgesSQL(String sql) throws Exception {
        int rowCount = 0;
        Connection conn = getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            try {
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    Object count = rs.getObject("count");
                    rowCount = Integer.parseInt(count.toString());
                }
                try {
                    rs.close();
                    stmt.close();
                    conn.close();
                } catch (SQLException e) {
                }
            } catch (Exception e2) {
                LogUtil.e("", e2);
                try {
                    rs.close();
                    stmt.close();
                    conn.close();
                } catch (SQLException e3) {
                }
            }
            return rowCount;
        } catch (Throwable th) {
            try {
                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException e4) {
            }
            throw th;
        }
    }

    public int executeQueryForCountForMySQL(String sql) throws Exception {
        Connection conn = getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            try {
                stmt = conn.createStatement();
                rs = stmt.executeQuery("select count(*) as totals from (" + sql + ") tab ");
                rs.next();
                int rowCount = rs.getInt("totals");
                try {
                    rs.close();
                    stmt.close();
                    conn.close();
                    return rowCount;
                } catch (SQLException e) {
                    throw new Exception("取得表行数出错，" + e.getMessage());
                }
            } catch (Exception e2) {
                LogUtil.e("取得记录的行数出错，", e2);
                throw new Exception("取得表行数出错，" + e2.getMessage());
            }
        } catch (Throwable th) {
            try {
                rs.close();
                stmt.close();
                conn.close();
                throw th;
            } catch (SQLException e3) {
                throw new Exception("取得表行数出错，" + e3.getMessage());
            }
        }
    }

    public boolean executeQuery(String sql) throws Exception {
        boolean bl = false;
        Connection conn = getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            try {
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    bl = true;
                }
                try {
                    rs.close();
                    stmt.close();
                    conn.close();
                } catch (SQLException e) {
                }
            } catch (Exception e2) {
                LogUtil.e("", e2);
                try {
                    rs.close();
                    stmt.close();
                    conn.close();
                } catch (SQLException e3) {
                }
            }
            return bl;
        } catch (Throwable th) {
            try {
                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException e4) {
            }
            throw th;
        }
    }

    public boolean executeQueryForQuartzJob(String sql) throws Exception {
        boolean bl = false;
        Connection conn = getConnectionForQuartzJob();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            try {
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    bl = true;
                }
                try {
                    rs.close();
                    stmt.close();
                    conn.close();
                } catch (SQLException e) {
                }
            } catch (Exception e2) {
                LogUtil.e("", e2);
                try {
                    rs.close();
                    stmt.close();
                    conn.close();
                } catch (SQLException e3) {
                }
            }
            return bl;
        } catch (Throwable th) {
            try {
                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException e4) {
            }
            throw th;
        }
    }

    public boolean testConn() {
        boolean bl = false;
        try {
            Connection conn = getConnection();
            if (conn != null) {
                bl = true;
            }
            conn.close();
        } catch (Exception e) {
            LogUtil.e("测试连接失败,", e);
        }
        return bl;
    }

    public String getPrimaryKeys(String databaseName, String tableName) {
        Connection conn = null;
        new ArrayList();
        try {
            try {
                conn = getConnection();
                DatabaseMetaData metadata = conn.getMetaData();
                ResultSet rs2 = metadata.getPrimaryKeys(databaseName, null, tableName);
                if (!rs2.next()) {
                    try {
                        conn.close();
                        return "";
                    } catch (SQLException e) {
                        return "";
                    }
                }
                System.out.println("主键名称: " + rs2.getString(4));
                String string = rs2.getString(4);
                try {
                    conn.close();
                } catch (SQLException e2) {
                }
                return string;
            } catch (Exception e3) {
                LogUtil.e("", e3);
                try {
                    conn.close();
                    return "";
                } catch (SQLException e4) {
                    return "";
                }
            }
        } catch (Throwable th) {
            try {
                conn.close();
            } catch (SQLException e5) {
            }
            throw th;
        }
    }

    public List<String> getPrimaryKeyss(String databaseName, String tableName) {
        Connection conn = null;
        List<String> rows2 = new ArrayList<>();
        try {
            try {
                conn = getConnection();
                DatabaseMetaData metadata = conn.getMetaData();
                ResultSet rs2 = metadata.getPrimaryKeys(databaseName, null, tableName);
                while (rs2.next()) {
                    rows2.add(rs2.getString(4));
                }
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            } catch (Exception e2) {
                LogUtil.e("", e2);
                try {
                    conn.close();
                } catch (SQLException e3) {
                }
            }
            return rows2;
        } catch (Throwable th) {
            try {
                conn.close();
            } catch (SQLException e4) {
            }
            throw th;
        }
    }

    public int executeQueryForCountForOracle(String sql) throws Exception {
        int rowCount = 0;
        Connection conn = getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        String sql3 = " select count(*) as count from  (" + sql + ")";
        try {
            try {
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql3);
                rs.next();
                rowCount = rs.getInt("count");
                try {
                    rs.close();
                    stmt.close();
                    conn.close();
                } catch (SQLException e) {
                }
            } catch (Exception e2) {
                LogUtil.e("", e2);
                try {
                    rs.close();
                    stmt.close();
                    conn.close();
                } catch (SQLException e3) {
                }
            }
            return rowCount;
        } catch (Throwable th) {
            try {
                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException e4) {
            }
            throw th;
        }
    }

    public int executeQueryForCountForPostgreSQL(String sql) throws Exception {
        int rowCount = 0;
        Connection conn = getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        String sql3 = " select count(*) as totals from  (" + sql + ") t ";
        try {
            try {
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql3);
                rs.next();
                rowCount = rs.getInt("totals");
                try {
                    rs.close();
                    stmt.close();
                    conn.close();
                } catch (SQLException e) {
                }
            } catch (Exception e2) {
                LogUtil.e("", e2);
                try {
                    rs.close();
                    stmt.close();
                    conn.close();
                } catch (SQLException e3) {
                }
            }
            return rowCount;
        } catch (Throwable th) {
            try {
                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException e4) {
            }
            throw th;
        }
    }

    public int executeQueryForCountForMSSqlWith(String sql) throws Exception {
        int rowCount = 0;
        Connection conn = getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            try {
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    rowCount++;
                }
                try {
                    rs.close();
                    stmt.close();
                    conn.close();
                } catch (SQLException e) {
                }
            } catch (Exception e2) {
                LogUtil.e("", e2);
                try {
                    rs.close();
                    stmt.close();
                    conn.close();
                } catch (SQLException e3) {
                }
            }
            return rowCount;
        } catch (Throwable th) {
            try {
                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException e4) {
            }
            throw th;
        }
    }

    public int executeQueryForCountForHana2(String sql) throws Exception {
        int rowCount = 0;
        Connection conn = getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        String sql3 = " select count(*) as COUNT from  (" + sql + ") t ";
        try {
            try {
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql3);
                rs.next();
                rowCount = rs.getInt("COUNT");
                try {
                    rs.close();
                    stmt.close();
                    conn.close();
                } catch (SQLException e) {
                }
            } catch (Exception e2) {
                LogUtil.e("", e2);
                try {
                    rs.close();
                    stmt.close();
                    conn.close();
                } catch (SQLException e3) {
                }
            }
            return rowCount;
        } catch (Throwable th) {
            try {
                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException e4) {
            }
            throw th;
        }
    }

    public static String byteToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder("");
        for (byte b : bytes) {
            String strHex = Integer.toHexString(b & 255);
            sb.append(strHex.length() == 1 ? "0" + strHex : strHex);
        }
        return "0x" + sb.toString().toUpperCase().trim();
    }
}
