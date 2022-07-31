package org.springframework.base.system.dbUtils;

import java.io.File;
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
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.base.system.utils.Constants;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.util.ResourceUtils;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/dbUtils/DBUtil.class */
public class DBUtil {
    public static Connection sqliteConnStatic = null;

    public Connection getSqliteConn() throws Exception {
        String dbPath;
        try {
            if (StringUtils.isEmpty(Constants.DATABASEPATH)) {
                File file = ResourceUtils.getFile("classpath:servlet-context.xml");
                Constants.DATABASEPATH = file.getAbsolutePath();
                dbPath = file.getAbsolutePath();
            } else {
                dbPath = Constants.DATABASEPATH;
            }
            Class.forName("org.sqlite.JDBC");
            if (sqliteConnStatic == null) {
                sqliteConnStatic = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            }
            return sqliteConnStatic;
        } catch (Exception e) {
            LogUtil.e("系统启动时，取路径出错，程序布署路径不能有空隔！", e);
            return null;
        }
    }

    public boolean do_update(String sql) throws Exception {
        Connection sqliteConn = getSqliteConn();
        Statement stat = sqliteConn.createStatement();
        stat.executeUpdate(sql);
        return true;
    }

    public boolean do_update2(String sql) throws Exception {
        Connection sqliteConn = getSqliteConn();
        Statement stat = sqliteConn.createStatement();
        stat.executeUpdate(sql);
        return true;
    }

    public List executeQuery(String sql) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        List rslist = new ArrayList();
        StringBuffer sqlPage = new StringBuffer(String.valueOf(sql) + " ");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            try {
                Connection sqliteConn = getSqliteConn();
                pstmt = sqliteConn.prepareStatement(sqlPage.toString());
                rs = pstmt.executeQuery();
                ResultSetMetaData rsmd = rs.getMetaData();
                int numberOfColumns = rsmd.getColumnCount();
                while (rs.next()) {
                    HashMap hashMap = new HashMap(numberOfColumns);
                    for (int i = 1; i <= numberOfColumns; i++) {
                        Object o = rs.getObject(i);
                        if ("Date".equalsIgnoreCase(rsmd.getColumnTypeName(i)) && o != null) {
                            hashMap.put(rsmd.getColumnName(i), formatter.format(o));
                        } else {
                            hashMap.put(rsmd.getColumnName(i), o == null ? "" : o);
                        }
                    }
                    rslist.add(hashMap);
                }
                try {
                    rs.close();
                    pstmt.close();
                } catch (SQLException e) {
                }
            } catch (Exception e2) {
                try {
                    rs.close();
                    pstmt.close();
                } catch (SQLException e3) {
                }
                try {
                    rs.close();
                    pstmt.close();
                } catch (SQLException e4) {
                }
            }
            return rslist;
        } catch (Throwable th) {
            try {
                rs.close();
                pstmt.close();
            } catch (SQLException e5) {
            }
            throw th;
        }
    }

    public List executeQuery2(String sql) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        List rslist = new ArrayList();
        StringBuffer sqlPage = new StringBuffer(String.valueOf(sql) + " ");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            try {
                Connection sqliteConn = getSqliteConn();
                pstmt = sqliteConn.prepareStatement(sqlPage.toString());
                rs = pstmt.executeQuery();
                ResultSetMetaData rsmd = rs.getMetaData();
                int numberOfColumns = rsmd.getColumnCount();
                while (rs.next()) {
                    HashMap hashMap = new HashMap(numberOfColumns);
                    for (int i = 1; i <= numberOfColumns; i++) {
                        Object o = rs.getObject(i);
                        if ("Date".equalsIgnoreCase(rsmd.getColumnTypeName(i)) && o != null) {
                            hashMap.put(rsmd.getColumnName(i), formatter.format(o));
                        } else {
                            hashMap.put(rsmd.getColumnName(i), o == null ? "" : o);
                        }
                    }
                    rslist.add(hashMap);
                }
                try {
                    rs.close();
                    pstmt.close();
                } catch (SQLException e) {
                }
            } catch (Exception e2) {
                LogUtil.e("executeQuery2 查询出错，", e2);
                try {
                    rs.close();
                    pstmt.close();
                } catch (SQLException e3) {
                }
                try {
                    rs.close();
                    pstmt.close();
                } catch (SQLException e4) {
                }
            }
            return rslist;
        } catch (Throwable th) {
            try {
                rs.close();
                pstmt.close();
            } catch (SQLException e5) {
            }
            throw th;
        }
    }

    public List selectPersonByUserName(String username) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        List rslist = new ArrayList();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            try {
                Connection sqliteConn = getSqliteConn();
                pstmt = sqliteConn.prepareStatement(" select * from treesoft_users where  LOWER(username)= ? ");
                pstmt.setString(1, username);
                rs = pstmt.executeQuery();
                ResultSetMetaData rsmd = rs.getMetaData();
                int numberOfColumns = rsmd.getColumnCount();
                while (rs.next()) {
                    HashMap hashMap = new HashMap(numberOfColumns);
                    for (int i = 1; i <= numberOfColumns; i++) {
                        Object o = rs.getObject(i);
                        if ("Date".equalsIgnoreCase(rsmd.getColumnTypeName(i)) && o != null) {
                            hashMap.put(rsmd.getColumnName(i), formatter.format(o));
                        } else {
                            hashMap.put(rsmd.getColumnName(i), o == null ? "" : o);
                        }
                    }
                    rslist.add(hashMap);
                }
                try {
                    rs.close();
                    pstmt.close();
                } catch (SQLException e) {
                }
            } catch (Exception e2) {
                try {
                    rs.close();
                    pstmt.close();
                } catch (SQLException e3) {
                }
                try {
                    rs.close();
                    pstmt.close();
                } catch (SQLException e4) {
                }
            }
            return rslist;
        } catch (Throwable th) {
            try {
                rs.close();
                pstmt.close();
            } catch (SQLException e5) {
            }
            throw th;
        }
    }

    public int executeQueryForCount(String sql) {
        ResultSet rs = null;
        int rowCount = 0;
        try {
            try {
                Connection sqliteConn = getSqliteConn();
                PreparedStatement pstmt = sqliteConn.prepareStatement(sql);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    rowCount++;
                }
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            } catch (Throwable th) {
                try {
                    rs.close();
                } catch (SQLException e2) {
                }
                throw th;
            }
        } catch (Exception e3) {
            try {
                rs.close();
            } catch (SQLException e4) {
            }
            try {
                rs.close();
            } catch (SQLException e5) {
            }
        }
        return rowCount;
    }

    public int executeQueryForCount2(String sql) {
        List<Map<String, Object>> list = executeQuery(sql);
        Map<String, Object> map = list.get(0);
        int num = Integer.parseInt(new StringBuilder().append(map.get("num")).toString());
        return num;
    }

    public List<Map<String, Object>> getConfigList() {
        List<Map<String, Object>> list = executeQuery(" select id, name,database_type as databaseType , database_name as databaseName, user_name as userName , password, port, ip ,url ,is_default as isDefault from  treesoft_config ");
        return list;
    }

    public List<Map<String, Object>> getDataSynchronizeList2(String state) {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            String sql = " select t1.id, t1.state , t1.name, t1.create_time as createTime ,t1.update_time as updateTime ,t1.source_config_id as sourceConfigId, t1.source_database as sourceDataBase,  t1.source_table as sourceTable ,t1.do_sql as doSql ,t1.target_config_id as targetConfigId, t1.target_database as targetDataBase,t1.target_table as targetTable , t1.cron, t1.operation,  t1.comments, t1.status ,t1.qualification,t1.toplimit, t1.increment, t1.offset,  t2.ip||':'||t2.port as sourceConfig , t3.ip||':'||t3.port as targetConfig  from  treesoft_data_synchronize t1 left join treesoft_config t2 on t1.source_config_id = t2.id  LEFT JOIN treesoft_config t3 on t1.target_config_id = t3.id where 1=1 ";
            if (!StringUtils.isEmpty(state)) {
                sql = String.valueOf(sql) + " and t1.state='" + state + "'";
            }
            list = executeQuery2(sql);
        } catch (Exception e) {
            LogUtil.e("取得 数据交换 列表出错," + e);
            e.printStackTrace();
        }
        return list;
    }

    public List<Map<String, Object>> getDataSynchronizeListById(String[] ids) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ids.length; i++) {
            sb = sb.append("'" + ids[i] + "',");
        }
        String newStr = sb.toString();
        String str3 = newStr.substring(0, newStr.length() - 1);
        List<Map<String, Object>> list = executeQuery(" select id, name, source_config_id as sourceConfigId,source_database as sourceDataBase ,source_table as sourceTable ,do_sql as doSql,target_config_id as targetConfigId, target_database as targetDataBase,target_table as targetTable , cron,operation,comments,status,state, qualification, toplimit,increment,offset, speed  from  treesoft_data_synchronize where id in (" + str3 + ") ");
        return list;
    }

    public List<Map<String, Object>> getTaskListById(String[] ids) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ids.length; i++) {
            sb = sb.append("'" + ids[i] + "',");
        }
        String newStr = sb.toString();
        String str3 = newStr.substring(0, newStr.length() - 1);
        List<Map<String, Object>> list = executeQuery(" select id, name, source_config_id as sourceConfigId, source_database as sourceDataBase, do_sql as doSql ,target_config_id as targetConfigId,target_database as targetDataBase,target_table as targetTable, cron,operation,comments,status,state, qualification from  treesoft_task  where id in (" + str3 + ") ");
        return list;
    }

    public List<Map<String, Object>> getTaskList2(String state) {
        String sql;
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            if (state.equals("")) {
                sql = " select t1.id, t1.state , t1.name, t1.create_time as createTime ,t1.update_time as updateTime ,t1.source_config_id as sourceConfigId,t1.source_database as sourceDataBase, t1.do_sql as doSql ,t1.target_config_id as targetConfigId, t1.target_database as targetDataBase,t1.target_table as targetTable, t1.cron, t1.operation, t1.comments, t1.status , t2.ip||':'||t2.port as sourceConfig , t3.ip||':'||t3.port as targetConfig from  treesoft_task t1 left join treesoft_config t2 on t1.source_config_id = t2.id LEFT JOIN treesoft_config t3 on t1.target_config_id = t3.id  ";
            } else {
                sql = " select t1.id, t1.state , t1.name, t1.create_time as createTime ,t1.update_time as updateTime ,t1.source_config_id as sourceConfigId,t1.source_database as sourceDataBase, t1.do_sql as doSql ,t1.target_config_id as targetConfigId, t1.target_database as targetDataBase,t1.target_table as targetTable, t1.cron, t1.operation, t1.comments, t1.status , t2.ip||':'||t2.port as sourceConfig , t3.ip||':'||t3.port as targetConfig from  treesoft_task t1 left join treesoft_config t2 on t1.source_config_id = t2.id LEFT JOIN treesoft_config t3 on t1.target_config_id = t3.id where t1.state='" + state + "'";
            }
            list = executeQuery2(sql);
        } catch (Exception e) {
            LogUtil.e("取得 定时任务  列表出错,", e);
        }
        return list;
    }

    public List<Map<String, Object>> getPersonList() {
        List<Map<String, Object>> list = executeQuery(" select * from  treesoft_users ");
        return list;
    }
}
