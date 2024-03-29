package org.springframework.base.system.core;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
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

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.base.system.utils.CryptoUtil;

/**
 * 
 * 业务数据库操作工具
 * 
 * @author 00fly
 * @version [版本号, 2018年11月22日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class BusiDataBaseUtil
{
    private static final Logger logger = LoggerFactory.getLogger(BusiDataBaseUtil.class);
    
    private static String driver;
    
    private static String url;
    
    private static String userName;
    
    private static String passWord;
    
    private SysDataBaseUtil sysDataBaseUtil = new SysDataBaseUtil();
    
    public BusiDataBaseUtil(String dbName, String databaseConfigId)
    {
        String sql = " select databaseType, userName, password, port, ip, url from dms_config where id=?";
        Map<String, Object> map = sysDataBaseUtil.queryForMap(sql, databaseConfigId);
        String ip = (String)map.get("ip");
        String port = (String)map.get("port");
        String databaseType = (String)map.get("databaseType");
        switch (databaseType)
        {
            case "MySql":
            case "MariaDB":
                driver = "com.mysql.jdbc.Driver";
                url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName + "?characterEncoding=utf8&tinyInt1isBit=false&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true";
                break;
            case "Oracle":
                driver = "oracle.jdbc.driver.OracleDriver";
                url = "jdbc:oracle:thin:@" + ip + ":" + port + ":" + dbName;
                break;
            case "PostgreSQL":
                driver = "org.postgresql.Driver";
                url = "jdbc:postgresql://" + ip + ":" + port + "/" + dbName;
                break;
            case "MSSQL":
                driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
                url = "jdbc:sqlserver://" + ip + ":" + port + ";database=" + dbName;
                break;
            case "Hive2":
                driver = "org.apache.hive.jdbc.HiveDriver";
                url = "jdbc:hive2://" + ip + ":" + port + "/" + dbName;
                break;
            default:
                break;
        }
        userName = (String)map.get("userName");
        passWord = CryptoUtil.decode(map.get("password").toString());
        if (passWord.contains("`"))
        {
            passWord = StringUtils.substringAfter(passWord, "`");
        }
    }
    
    private synchronized Connection getConnection()
    {
        try
        {
            Class.forName(driver);
            return DriverManager.getConnection(url, userName, passWord);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return null;
        }
    }
    
    public static boolean testConnection(String databaseType, String databaseName, String ip, String port, String user, String password)
    {
        try
        {
            String url = "";
            if (databaseType.equals("MySql"))
            {
                Class.forName("com.mysql.jdbc.Driver");
                url = "jdbc:mysql://" + ip + ":" + port + "/" + databaseName + "?characterEncoding=utf8";
            }
            if (databaseType.equals("MariaDB"))
            {
                Class.forName("com.mysql.jdbc.Driver");
                url = "jdbc:mysql://" + ip + ":" + port + "/" + databaseName + "?characterEncoding=utf8";
            }
            if (databaseType.equals("Oracle"))
            {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                url = "jdbc:oracle:thin:@" + ip + ":" + port + ":" + databaseName;
            }
            if (databaseType.equals("PostgreSQL"))
            {
                Class.forName("org.postgresql.Driver");
                url = "jdbc:postgresql://" + ip + ":" + port + "/" + databaseName;
            }
            if (databaseType.equals("MSSQL"))
            {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                url = "jdbc:sqlserver://" + ip + ":" + port + ";database=" + databaseName;
            }
            if (databaseType.equals("Hive2"))
            {
                Class.forName("org.apache.hive.jdbc.HiveDriver");
                url = "jdbc:hive2://" + ip + ":" + port + "/" + databaseName;
            }
            Connection conn = DriverManager.getConnection(url, user, password);
            return (conn != null);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        return false;
    }
    
    public int setupdateData(String sql)
        throws Exception
    {
        Connection conn = getConnection();
        Statement stmt = null;
        try
        {
            stmt = conn.createStatement();
            return stmt.executeUpdate(sql);
        }
        catch (SQLException e)
        {
            logger.error(e.getMessage(), e);
            throw new Exception(e.getMessage());
        }
        finally
        {
            try
            {
                stmt.close();
                conn.close();
            }
            catch (SQLException e)
            {
                logger.error(e.getMessage(), e);
            }
        }
    }
    
    public int updateExecuteBatch(List<String> sqlList)
        throws Exception
    {
        Connection conn = getConnection();
        Statement stmt = null;
        try
        {
            stmt = conn.createStatement();
            conn.setAutoCommit(false);
            for (String sql : sqlList)
            {
                sql = sql.replaceAll(";", "");
                stmt.addBatch(sql);
            }
            int[] updateCounts = stmt.executeBatch();
            conn.commit();
            return updateCounts.length;
        }
        catch (Exception e)
        {
            conn.rollback();
            logger.error(e.getMessage(), e);
            throw new Exception(e.getMessage());
        }
        finally
        {
            try
            {
                stmt.close();
                conn.close();
            }
            catch (SQLException e)
            {
                logger.error(e.getMessage(), e);
            }
        }
    }
    
    public List<Map<String, Object>> queryForList(String sql)
        throws Exception
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        int maxSize = -1;
        String[] fields = null;
        List<String> times = new ArrayList<>();
        List<String> clob = new ArrayList<>();
        List<String> binary = new ArrayList<>();
        List<Map<String, Object>> rows = new ArrayList<>();
        conn = getConnection();
        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery();
        rsmd = rs.getMetaData();
        maxSize = rsmd.getColumnCount();
        fields = new String[maxSize];
        for (int i = 0; i < maxSize; i++)
        {
            fields[i] = rsmd.getColumnLabel(i + 1);
            if (("java.sql.Timestamp".equals(rsmd.getColumnClassName(i + 1))) || ("oracle.sql.TIMESTAMP".equals(rsmd.getColumnClassName(i + 1))))
            {
                times.add(fields[i]);
            }
            if (("oracle.jdbc.OracleClob".equals(rsmd.getColumnClassName(i + 1))) || ("oracle.jdbc.OracleBlob".equals(rsmd.getColumnClassName(i + 1))))
            {
                clob.add(fields[i]);
            }
            if ("[B".equals(rsmd.getColumnClassName(i + 1)))
            {
                binary.add(fields[i]);
            }
        }
        Map<String, Object> row = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        while (rs.next())
        {
            row = new LinkedHashMap<>();
            for (int i = 0; i < maxSize; i++)
            {
                Object value = times.contains(fields[i]) ? rs.getTimestamp(fields[i]) : rs.getObject(fields[i]);
                if ((times.contains(fields[i])) && (value != null))
                {
                    value = sdf.format(value);
                }
                if ((clob.contains(fields[i])) && (value != null))
                {
                    value = "(Blob)";
                }
                if ((binary.contains(fields[i])) && (value != null))
                {
                    value = new String((byte[])value);
                }
                row.put(fields[i], value);
            }
            rows.add(row);
        }
        try
        {
            rs.close();
            pstmt.close();
            conn.close();
        }
        catch (SQLException e)
        {
            logger.error(e.getMessage(), e);
        }
        return rows;
    }
    
    public List<Map<String, Object>> queryForListForPostgreSQL(String sql)
        throws Exception
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        int maxSize = -1;
        String[] fields;
        List<String> times = new ArrayList<>();
        List<String> binary = new ArrayList<>();
        List<String> object = new ArrayList<>();
        List<Map<String, Object>> rows = new ArrayList<>();
        conn = getConnection();
        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery();
        rsmd = rs.getMetaData();
        maxSize = rsmd.getColumnCount();
        fields = new String[maxSize];
        for (int i = 0; i < maxSize; i++)
        {
            fields[i] = rsmd.getColumnLabel(i + 1);
            if (("java.sql.Timestamp".equals(rsmd.getColumnClassName(i + 1))) || ("oracle.sql.TIMESTAMP".equals(rsmd.getColumnClassName(i + 1))))
            {
                times.add(fields[i]);
            }
            if ("java.lang.Object".equals(rsmd.getColumnClassName(i + 1)))
            {
                object.add(fields[i]);
            }
            if ("[B".equals(rsmd.getColumnClassName(i + 1)))
            {
                binary.add(fields[i]);
            }
        }
        Map<String, Object> row;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        while (rs.next())
        {
            row = new LinkedHashMap<>();
            for (int i = 0; i < maxSize; i++)
            {
                Object value = times.contains(fields[i]) ? rs.getTimestamp(fields[i]) : rs.getObject(fields[i]);
                if ((times.contains(fields[i])) && (value != null))
                {
                    value = sdf.format(value);
                }
                try
                {
                    if ((binary.contains(fields[i])) && (value != null))
                    {
                        value = new String((byte[])value);
                    }
                    if ((object.contains(fields[i])) && (value != null))
                    {
                        value = value.toString();
                    }
                }
                catch (Exception e)
                {
                    value = "(Object)";
                }
                row.put(fields[i], value);
            }
            rows.add(row);
        }
        try
        {
            rs.close();
            pstmt.close();
            conn.close();
        }
        catch (SQLException e)
        {
            logger.error(e.getMessage(), e);
        }
        return rows;
    }
    
    public List<Map<String, Object>> queryForList2(String sql)
        throws Exception
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        int maxSize = -1;
        conn = getConnection();
        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery();
        rsmd = rs.getMetaData();
        maxSize = rsmd.getColumnCount();
        List<Map<String, Object>> rows = new ArrayList<>();
        Map<String, Object> row;
        while (rs.next())
        {
            row = new HashMap<>();
            for (int i = 0; i < maxSize; i++)
            {
                row.put(rsmd.getColumnLabel(i + 1), rs.getObject(rsmd.getColumnLabel(i + 1)));
            }
            rows.add(row);
        }
        return rows;
    }
    
    public List<Map<String, Object>> queryForListPageForMSSQL(String sql, int maxRow, int beginIndex)
        throws Exception
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        int maxSize = -1;
        String[] fields;
        List<String> times = new ArrayList<>();
        conn = getConnection();
        pstmt = conn.prepareStatement(sql, 1005, 1008);
        pstmt.setMaxRows(maxRow);
        rs = pstmt.executeQuery();
        rsmd = rs.getMetaData();
        maxSize = rsmd.getColumnCount();
        fields = new String[maxSize];
        for (int i = 0; i < maxSize; i++)
        {
            fields[i] = rsmd.getColumnLabel(i + 1);
            if ("java.sql.Timestamp".equals(rsmd.getColumnClassName(i + 1)))
            {
                times.add(fields[i]);
            }
        }
        List<Map<String, Object>> rows = new ArrayList<>();
        Map<String, Object> row;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        rs.absolute(beginIndex);
        while (rs.next())
        {
            row = new HashMap<>();
            for (int i = 0; i < maxSize; i++)
            {
                Object value = times.contains(fields[i]) ? rs.getTimestamp(fields[i]) : rs.getObject(fields[i]);
                if ((times.contains(fields[i])) && (value != null))
                {
                    value = sdf.format(value);
                }
                row.put(fields[i], value);
            }
            rows.add(row);
        }
        try
        {
            rs.close();
            pstmt.close();
            conn.close();
        }
        catch (SQLException e)
        {
            logger.error(e.getMessage(), e);
        }
        return rows;
    }
    
    public List<Map<String, Object>> queryForListPageForHive2(String sql, int maxRow, int beginIndex)
        throws Exception
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        int maxSize = -1;
        String[] fields;
        List<String> times = new ArrayList<>();
        conn = getConnection();
        pstmt = conn.prepareStatement(sql);
        pstmt.setMaxRows(maxRow);
        rs = pstmt.executeQuery();
        rsmd = rs.getMetaData();
        maxSize = rsmd.getColumnCount();
        fields = new String[maxSize];
        for (int i = 0; i < maxSize; i++)
        {
            fields[i] = rsmd.getColumnLabel(i + 1);
            if ("java.sql.Timestamp".equals(rsmd.getColumnClassName(i + 1)))
            {
                times.add(fields[i]);
            }
        }
        List<Map<String, Object>> rows = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> row = null;
        while (rs.next())
        {
            row = new LinkedHashMap<>();
            for (int i = 0; i < maxSize; i++)
            {
                Object value = times.contains(fields[i]) ? rs.getTimestamp(fields[i]) : rs.getObject(fields[i]);
                if ((times.contains(fields[i])) && (value != null))
                {
                    value = sdf.format(value);
                }
                row.put(fields[i], value);
            }
            rows.add(row);
        }
        try
        {
            rs.close();
            pstmt.close();
            conn.close();
        }
        catch (SQLException e)
        {
            logger.error(e.getMessage(), e);
        }
        return rows;
    }
    
    public List<Map<String, Object>> queryForListWithType(String sql)
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Map<String, Object>> rows2 = new ArrayList<>();
        try
        {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            ResultSetMetaData rsme = rs.getMetaData();
            int columnCount = rsme.getColumnCount();
            rs.next();
            for (int i = 1; i < columnCount + 1; i++)
            {
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
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        finally
        {
            try
            {
                rs.close();
                pstmt.close();
                conn.close();
            }
            catch (SQLException localSQLException1)
            {
                logger.error(localSQLException1.getMessage(), localSQLException1);
            }
        }
        return rows2;
    }
    
    public List<Map<String, Object>> queryForColumnOnly(String sql)
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Map<String, Object>> rows2 = new ArrayList<>();
        try
        {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            ResultSetMetaData rsme = rs.getMetaData();
            int columnCount = rsme.getColumnCount();
            for (int i = 1; i < columnCount + 1; i++)
            {
                Map<String, Object> map = new HashMap<>();
                map.put("column_name", rsme.getColumnName(i));
                map.put("data_type", rsme.getColumnTypeName(i));
                map.put("precision", Integer.valueOf(rsme.getPrecision(i)));
                map.put("isAutoIncrement", Boolean.valueOf(rsme.isAutoIncrement(i)));
                map.put("is_nullable", Integer.valueOf(rsme.isNullable(i)));
                map.put("isReadOnly", Boolean.valueOf(rsme.isReadOnly(i)));
                rows2.add(map);
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        finally
        {
            try
            {
                rs.close();
                pstmt.close();
                conn.close();
            }
            catch (SQLException localSQLException1)
            {
                logger.error(localSQLException1.getMessage(), localSQLException1);
            }
        }
        return rows2;
    }
    
    public List<Map<String, Object>> executeSqlForColumns(String sql)
        throws Exception
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        int maxSize = -1;
        conn = getConnection();
        List<Map<String, Object>> rows = new ArrayList<>();
        try
        {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            rsmd = rs.getMetaData();
            maxSize = rsmd.getColumnCount();
            for (int i = 0; i < maxSize; i++)
            {
                Map<String, Object> map = new HashMap<>();
                map.put("column_name", rsmd.getColumnLabel(i + 1));
                map.put("data_type", rsmd.getColumnTypeName(i + 1));
                rows.add(map);
            }
        }
        finally
        {
            try
            {
                rs.close();
                pstmt.close();
                conn.close();
            }
            catch (Exception e)
            {
                logger.error(e.getMessage(), e);
            }
        }
        return rows;
    }
    
    public int executeQueryForCount(String sql)
    {
        int rowCount = 0;
        Connection conn = getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next())
            {
                Object count = rs.getObject("count(*)");
                rowCount = Integer.parseInt(count.toString());
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        finally
        {
            try
            {
                rs.close();
                stmt.close();
                conn.close();
            }
            catch (SQLException localSQLException1)
            {
                logger.error(localSQLException1.getMessage(), localSQLException1);
            }
        }
        return rowCount;
    }
    
    public int executeQueryForCountForPostgesSQL(String sql)
    {
        int rowCount = 0;
        Connection conn = getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next())
            {
                Object count = rs.getObject("count");
                rowCount = Integer.parseInt(count.toString());
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        finally
        {
            try
            {
                rs.close();
                stmt.close();
                conn.close();
            }
            catch (SQLException localSQLException1)
            {
                logger.error(localSQLException1.getMessage(), localSQLException1);
            }
        }
        return rowCount;
    }
    
    public int executeQueryForCount2(String sql)
    {
        int rowCount = 0;
        Connection conn = getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            rs.last();
            rowCount = rs.getRow();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        finally
        {
            try
            {
                rs.close();
                stmt.close();
                conn.close();
            }
            catch (SQLException localSQLException1)
            {
                logger.error(localSQLException1.getMessage(), localSQLException1);
            }
        }
        return rowCount;
    }
    
    public boolean executeQuery(String sql)
    {
        boolean bl = false;
        Connection conn = getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next())
            {
                bl = true;
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        finally
        {
            try
            {
                rs.close();
                stmt.close();
                conn.close();
            }
            catch (SQLException localSQLException1)
            {
                logger.error(localSQLException1.getMessage(), localSQLException1);
            }
        }
        return bl;
    }
    
    public String getPrimaryKeys(String databaseName, String tableName)
    {
        Connection conn = null;
        try
        {
            conn = getConnection();
            DatabaseMetaData metadata = conn.getMetaData();
            ResultSet rs2 = metadata.getPrimaryKeys(databaseName, null, tableName);
            if (rs2.next())
            {
                logger.info("主键名称: {}", rs2.getString(4));
                return rs2.getString(4);
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        finally
        {
            try
            {
                conn.close();
            }
            catch (SQLException localSQLException2)
            {
                logger.error(localSQLException2.getMessage(), localSQLException2);
            }
        }
        return "";
    }
    
    public List<String> getPrimaryKeyss(String databaseName, String tableName)
    {
        Connection conn = null;
        List<String> rows2 = new ArrayList<>();
        try
        {
            conn = getConnection();
            DatabaseMetaData metadata = conn.getMetaData();
            ResultSet rs2 = metadata.getPrimaryKeys(databaseName, null, tableName);
            while (rs2.next())
            {
                logger.info("主键名称2: {}", rs2.getString(4));
                rows2.add(rs2.getString(4));
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        finally
        {
            try
            {
                conn.close();
            }
            catch (SQLException localSQLException1)
            {
                logger.error(localSQLException1.getMessage(), localSQLException1);
            }
        }
        return rows2;
    }
    
    public int executeQueryForCountForOracle(String sql)
    {
        int rowCount = 0;
        Connection conn = getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        String sql3 = " select count(*) as count from  (" + sql + ")";
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql3);
            rs.next();
            rowCount = rs.getInt("count");
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        finally
        {
            try
            {
                rs.close();
                stmt.close();
                conn.close();
            }
            catch (SQLException localSQLException1)
            {
                logger.error(localSQLException1.getMessage(), localSQLException1);
            }
        }
        return rowCount;
    }
    
    public int executeQueryForCountForPostgreSQL(String sql)
    {
        int rowCount = 0;
        Connection conn = getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        String sql3 = " select count(*) as count from  (" + sql + ") t ";
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql3);
            rs.next();
            rowCount = rs.getInt("count");
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        finally
        {
            try
            {
                rs.close();
                stmt.close();
                conn.close();
            }
            catch (SQLException localSQLException1)
            {
                logger.error(localSQLException1.getMessage(), localSQLException1);
            }
        }
        return rowCount;
    }
}
