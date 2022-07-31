package org.springframework.base.system.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.base.system.dbUtils.DBUtil;
import org.springframework.base.system.entity.SqlDistribute;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.utils.DateUtils;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.base.system.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/dao/SqlDistributeDao.class */
public class SqlDistributeDao {
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private ConfigDao configDao;
    public static Map<String, Object> configAllDataBase;

    public Page<Map<String, Object>> sqlDistributeList(Page<Map<String, Object>> page) throws Exception {
        String sql2;
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        String orderBy = page.getOrderBy();
        String order = page.getOrder();
        DBUtil db1 = new DBUtil();
        if (configAllDataBase == null) {
            configAllDataBase = this.configDao.getConfigAllDataBaseMap();
        }
        if (!StringUtils.isEmpty(orderBy)) {
            sql2 = String.valueOf(" select id,name ,run_time as runTime ,database_number as databaseNumber,status ,state from  treesoft_sql_distribute  ") + " order by  " + orderBy + " " + order;
        } else {
            sql2 = String.valueOf(" select id,name ,run_time as runTime ,database_number as databaseNumber,status ,state from  treesoft_sql_distribute  ") + " order by create_time desc ";
        }
        int rowCount = db1.executeQueryForCount(sql2);
        List<Map<String, Object>> list = db1.executeQuery(String.valueOf(sql2) + "  limit " + limitFrom + "," + pageSize);
        page.setTotalCount(rowCount);
        page.setResult(list);
        return page;
    }

    public boolean sqlDistributeDelete(String[] ids) throws Exception {
        DBUtil db = new DBUtil();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ids.length; i++) {
            sb = sb.append("'" + ids[i] + "',");
        }
        String newStr = sb.toString();
        String str3 = newStr.substring(0, newStr.length() - 1);
        String sql = "  delete  from  treesoft_sql_distribute   where id in (" + str3 + ")";
        String sql2 = "  delete  from  treesoft_sql_distribute_database   where sql_distribute_id in (" + str3 + ")";
        boolean bl = db.do_update(sql);
        db.do_update(sql2);
        return bl;
    }

    public boolean sqlDistributeUpdate(SqlDistribute sqlDistribute) throws Exception {
        String sql;
        DBUtil db = new DBUtil();
        String id = sqlDistribute.getId();
        String status = sqlDistribute.getStatus();
        String databaseNumber = new StringBuilder(String.valueOf(sqlDistribute.getDatabaseConfigId().length)).toString();
        if (status == null || status.equals("")) {
            status = "0";
        }
        String doSql = sqlDistribute.getDoSql();
        sqlDistribute.setDoSql(StringEscapeUtils.unescapeHtml4(doSql.replaceAll("'", "''")));
        if (!id.equals("")) {
            sql = " update treesoft_sql_distribute   set name='" + sqlDistribute.getName() + "' ,do_sql='" + sqlDistribute.getDoSql() + "', status='" + status + "', state='" + sqlDistribute.getState() + "', update_time='" + DateUtils.getDateTime() + "', database_number='" + databaseNumber + "', comments='" + sqlDistribute.getComments() + "'  where id='" + id + "'";
            deleteDatabaseConfig(id);
            insertDatabaseConfig(id, sqlDistribute.getDatabaseConfigId(), sqlDistribute.getDatabaseName());
        } else {
            String tempId = StringUtil.getUUID();
            sql = " insert into treesoft_sql_distribute ( id,create_time ,name ,do_sql, database_number,status ,state, database_number, comments ) values ( '" + tempId + "','" + DateUtils.getDateTime() + "','" + sqlDistribute.getName() + "','" + sqlDistribute.getDoSql() + "','" + databaseNumber + "','" + sqlDistribute.getState() + "','" + status + "','" + databaseNumber + "','" + sqlDistribute.getComments() + "' ) ";
            insertDatabaseConfig(tempId, sqlDistribute.getDatabaseConfigId(), sqlDistribute.getDatabaseName());
        }
        boolean bl = db.do_update(sql);
        return bl;
    }

    public boolean sqlDistributeUpdateStatus(String sqlDistributeId, String status) {
        DBUtil db = new DBUtil();
        boolean bl = true;
        try {
            String sql = "update treesoft_sql_distribute set status='" + status + "', run_time='" + DateUtils.getDateTime() + "'  where id='" + sqlDistributeId + "'";
            bl = db.do_update2(sql);
        } catch (Exception e) {
            LogUtil.e("taskUpdateStatus出错," + e);
            e.printStackTrace();
        }
        return bl;
    }

    public Map<String, Object> getSqlDistribute(String id) {
        DBUtil db = new DBUtil();
        String sql = " select id,name ,do_sql as doSql,status ,state,comments from  treesoft_sql_distribute  where id='" + id + "'";
        List<Map<String, Object>> list = db.executeQuery(sql);
        Map<String, Object> map = list.get(0);
        return map;
    }

    public List<Map<String, Object>> getSqlDistributeByIds(String[] ids) {
        DBUtil db = new DBUtil();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ids.length; i++) {
            sb = sb.append("'" + ids[i] + "',");
        }
        String newStr = sb.toString();
        String str3 = newStr.substring(0, newStr.length() - 1);
        String sql = " select id,name ,do_sql as doSql,status ,state,comments from  treesoft_sql_distribute  where id  in (" + str3 + ")";
        List<Map<String, Object>> list = db.executeQuery(sql);
        return list;
    }

    public List<Map<String, Object>> getSqlDistributeDatabaseConfigList(String sql_distribute_id) {
        DBUtil db1 = new DBUtil();
        new ArrayList();
        String sql = " select id ,config_id as databaseConfigId, config_name as configName , database_name as databaseName ,order_number as orderNumber  from treesoft_sql_distribute_database where  sql_distribute_id='" + sql_distribute_id + "'  order by order_number ";
        List<Map<String, Object>> list = db1.executeQuery(sql);
        return list;
    }

    public Page<Map<String, Object>> sqlDistributeLogList(Page<Map<String, Object>> page, String taskId) throws Exception {
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        DBUtil db1 = new DBUtil();
        String sql = " select id, create_time as createTime ,config_id as configId, config_name as configName, database_name as databaseName,  status ,totals ,comments from  treesoft_sql_distribute_log where sql_distribute_id ='" + taskId + "' order by createTime desc ";
        int rowCount = db1.executeQueryForCount(sql);
        List<Map<String, Object>> list = db1.executeQuery(String.valueOf(sql) + "  limit " + limitFrom + "," + pageSize);
        page.setTotalCount(rowCount);
        page.setResult(list);
        return page;
    }

    public List<Map<String, Object>> getTaskList2(String state) {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            DBUtil du = new DBUtil();
            list = du.getTaskList2(state);
            return list;
        } catch (Exception e) {
            LogUtil.e("取得全部的任务 列表数据," + e);
            e.printStackTrace();
            return list;
        }
    }

    public boolean deleteDatabaseConfig(String sql_distribute_id) throws Exception {
        DBUtil db = new DBUtil();
        String sql = "  delete  from  treesoft_sql_distribute_database  where sql_distribute_id ='" + sql_distribute_id + "'";
        boolean bl = db.do_update(sql);
        return bl;
    }

    public boolean deleteTaskLog(String[] ids) throws Exception {
        DBUtil db = new DBUtil();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ids.length; i++) {
            sb = sb.append("'" + ids[i] + "',");
        }
        String newStr = sb.toString();
        String str3 = newStr.substring(0, newStr.length() - 1);
        String sql = "  delete  from  treesoft_sql_distribute_log  where id in (" + str3 + ")";
        boolean bl = db.do_update(sql);
        return bl;
    }

    public boolean deleteTaskLogByDS(String sqlDistributeId) throws Exception {
        DBUtil db = new DBUtil();
        String sql = "  delete  from  treesoft_sql_distribute_log  where sql_distribute_id ='" + sqlDistributeId + "'";
        boolean bl = db.do_update(sql);
        return bl;
    }

    public boolean saveSqlDistributeLog(String configId, String databaseName, String status, String comments, String sqlDistributeId) {
        DBUtil db = new DBUtil();
        boolean bl = true;
        String comments2 = comments.replaceAll("'", "''");
        try {
            Map<String, Object> map = (Map) configAllDataBase.get(configId);
            String configName = (String) map.get("name");
            String sql = " insert into treesoft_sql_distribute_log ( config_id ,config_name ,database_name , create_time, status ,comments, sql_distribute_id ) values ( '" + configId + "','" + configName + "','" + databaseName + "','" + DateUtils.getDateTime() + "','" + status + "','" + comments2 + "','" + sqlDistributeId + "')";
            bl = db.do_update2(sql);
        } catch (Exception e) {
            LogUtil.e("记录SQL分发任务日志出错," + e);
            e.printStackTrace();
        }
        return bl;
    }

    public boolean copySqlDistribute(String[] ids) throws Exception {
        DBUtil db = new DBUtil();
        boolean bl = true;
        for (int i = 0; i < ids.length; i++) {
            String tempId = StringUtil.getUUID();
            String sql = "insert into treesoft_sql_distribute ( id,create_time ,name ,do_sql, database_number,status ,state, database_number, comments ) select '" + tempId + "', datetime('now') ,name ,doSql, database_number,status ,state, database_number, comments from treesoft_sql_distribute where id in ('" + ids[i] + "')";
            db.do_update(sql);
            String sql2 = "insert into treesoft_sql_distribute_database ( id ,config_id , config_name , database_name ,order_number ,sql_distribute_id  ) select abs(random()), config_id , config_name , database_name ,order_number ,'" + tempId + "'  from treesoft_sql_distribute_database   where sql_distribute_id in ('" + ids[i] + "')";
            bl = db.do_update(sql2);
        }
        return bl;
    }

    public boolean insertDatabaseConfig(String sql_distribute_id, String[] databaseConfigId, String[] databaseName) throws Exception {
        DBUtil db = new DBUtil();
        boolean bl = true;
        for (int i = 0; i < databaseConfigId.length; i++) {
            String tempId = StringUtil.getUUID();
            Map<String, Object> map = (Map) configAllDataBase.get(databaseConfigId[i]);
            String configName = (String) map.get("name");
            String sql = " insert into treesoft_sql_distribute_database( id ,config_id , config_name , database_name ,order_number ,sql_distribute_id ) values ('" + tempId + "','" + databaseConfigId[i] + "','" + configName + "','" + databaseName[i] + "'," + i + ",'" + sql_distribute_id + "')";
            bl = db.do_update(sql);
        }
        return bl;
    }
}
