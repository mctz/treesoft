package org.springframework.base.system.dbUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.base.system.utils.Constants;
import org.springframework.base.system.utils.ExcelUtil;
import org.springframework.base.system.utils.LogUtil;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/dbUtils/DBUtilJDBC.class */
public class DBUtilJDBC {
    private String driver;
    private String url;
    private String ip;
    private String port;
    private String userName;
    private String password;
    private String databaseType;
    private String databaseName;

    public DBUtilJDBC(String dbName, String databaseConfigId) {
        this.driver = "";
        this.url = "";
        this.ip = "";
        this.port = "";
        this.userName = "";
        this.password = "";
        this.databaseType = "";
        this.databaseName = "";
        Map<String, Object> map12 = Constants.configStaticMap.get(databaseConfigId);
        this.ip = new StringBuilder().append(map12.get("ip")).toString();
        this.url = new StringBuilder().append(map12.get("url")).toString();
        this.port = new StringBuilder().append(map12.get("port")).toString();
        this.driver = new StringBuilder().append(map12.get("driver")).toString();
        this.databaseType = new StringBuilder().append(map12.get("databaseType")).toString();
        this.userName = new StringBuilder().append(map12.get("userName")).toString();
        this.password = new StringBuilder().append(map12.get("password")).toString();
        this.databaseName = new StringBuilder().append(map12.get("databaseName")).toString();
        if (this.databaseType.equals("ES")) {
            this.url = "jdbc:es://http://" + this.ip + ":" + this.port;
        }
        if (this.databaseType.equals("Impala")) {
            this.url = "jdbc:impala://" + this.ip + ":" + this.port + "/" + dbName;
        }
    }

    public final synchronized Connection getConnection() {
        try {
            Class.forName(this.driver);
            Connection conn = DriverManager.getConnection(this.url, this.userName, this.password);
            return conn;
        } catch (Exception e) {
            LogUtil.e("数据库连接出错! URL=" + this.url, e);
            return null;
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
            LogUtil.e(" 按SQL查询出结果集部出错，", e);
            throw new Exception(e.getMessage());
        }
    }

    public List<Map<String, Object>> queryForListForSelect(String sql) throws Exception {
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

    public List<Map<String, Object>> queryForList(String sql) throws Exception {
        List<String> times = new ArrayList<>();
        List<Map<String, Object>> rows = new ArrayList<>();
        Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
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
            LogUtil.e("取数据出错，", e);
            throw new Exception("取数据出错，" + e.getMessage());
        }
    }

    public List<Map<String, Object>> queryForListForShenTong(String sql, int limitFrom, int pageSize) throws Exception {
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
                    LogUtil.e("新增和更新的操作出错，", e);
                    throw new Exception(e.getMessage());
                }
            } catch (Exception e2) {
                LogUtil.e("新增和更新的操作出错，", e2);
                throw new Exception(e2.getMessage());
            }
        } catch (Throwable th) {
            try {
                preStatement.close();
                conn.close();
                throw th;
            } catch (SQLException e3) {
                LogUtil.e("新增和更新的操作出错，", e3);
                throw new Exception(e3.getMessage());
            }
        }
    }

    public int executeQueryForCount(String sql) throws Exception {
        Connection conn = getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            try {
                stmt = conn.createStatement();
                rs = stmt.executeQuery("select count(*) as TOTALS from (" + sql + ") tab ");
                rs.next();
                int rowCount = rs.getInt("TOTALS");
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
            LogUtil.e("取数据出错，", e);
            throw new Exception("取数据出错，" + e.getMessage());
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
                LogUtil.e("取表总记录数出错，", e2);
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
            } catch (Throwable th) {
                try {
                    rs.close();
                    pstmt.close();
                    conn.close();
                } catch (SQLException e2) {
                    LogUtil.e(e2.getMessage());
                }
                throw th;
            }
        } catch (Exception e3) {
            LogUtil.e("queryForColumnOnly ,", e3);
            try {
                rs.close();
                pstmt.close();
                conn.close();
            } catch (SQLException e4) {
                LogUtil.e(e4.getMessage());
            }
        }
        return rows2;
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

    public Connection getConnectionForQuartzJob() throws Exception {
        try {
            Connection connQuartzJob = getConnection();
            return connQuartzJob;
        } catch (Exception e) {
            LogUtil.e("数据同步 , 数据库连接出错 , ", e);
            throw new Exception(String.valueOf(this.databaseName) + " 数据库连接出错，请检查连接配置信息。");
        }
    }

    public List<Map<String, Object>> queryForListForCache(String sql, int limitFrom, int pageSize) throws Exception {
        List<String> times = new ArrayList<>();
        List<String> binary = new ArrayList<>();
        List<String> object = new ArrayList<>();
        List<String> stringColumn = new ArrayList<>();
        List<Map<String, Object>> rows = new ArrayList<>();
        Connection conn = getConnection();
        String sql2 = "select top " + pageSize + " * from (" + sql + ") tab ";
        PreparedStatement pstmt = conn.prepareStatement(sql2);
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
            if ("java.lang.String".equals(rsmd.getColumnClassName(i + 1))) {
                stringColumn.add(fields[i]);
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
                    if (stringColumn.contains(fields[i2]) && value != null) {
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

    public static String byteToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder("");
        for (byte b : bytes) {
            String strHex = Integer.toHexString(b & 255);
            sb.append(strHex.length() == 1 ? "0" + strHex : strHex);
        }
        return "0x" + sb.toString().toUpperCase().trim();
    }
}
