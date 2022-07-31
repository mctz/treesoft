package org.springframework.base.system.dao;

import java.util.List;
import java.util.Map;
import org.springframework.base.system.dbUtils.DBUtil;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.utils.DateUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/dao/LogDao.class */
public class LogDao {
    public Page<Map<String, Object>> logList(Page<Map<String, Object>> page, String startTime, String endTime, String userName, String log) throws Exception {
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        page.getOrderBy();
        page.getOrder();
        DBUtil db1 = new DBUtil();
        String whereSql = " where 1=1 ";
        if (!StringUtils.isEmpty(startTime)) {
            whereSql = String.valueOf(whereSql) + " and  t1.create_time>='" + startTime + "'";
        }
        if (!StringUtils.isEmpty(endTime)) {
            whereSql = String.valueOf(whereSql) + " and  t1.create_time <='" + endTime + "'";
        }
        if (!StringUtils.isEmpty(userName)) {
            whereSql = String.valueOf(whereSql) + " and LOWER( t1.username) like '%" + userName.toLowerCase() + "%'";
        }
        if (!StringUtils.isEmpty(log)) {
            whereSql = String.valueOf(whereSql) + " and t1.log like '%" + log + "%'";
        }
        int rowCount = db1.executeQueryForCount2(String.valueOf(" select count(*) num from treesoft_log t1 left join treesoft_config t2 on t1.config_id=t2.id  ") + whereSql);
        List<Map<String, Object>> list = db1.executeQuery(String.valueOf(" select t1.id , t1.create_time as createTime , t1.operator ,t1.username , t1.log ,t1.ip,t1.result,t1.module,t1.mess,t1.level,t1.database_name as databaseName ,t2.name as configName from treesoft_log t1 left join treesoft_config t2 on t1.config_id=t2.id  ") + whereSql + " order by t1.create_time desc   limit " + limitFrom + "," + pageSize);
        page.setTotalCount(rowCount);
        page.setResult(list);
        return page;
    }

    public boolean deleteLog(String[] ids) throws Exception {
        DBUtil db = new DBUtil();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ids.length; i++) {
            sb = sb.append("'" + ids[i] + "',");
        }
        String newStr = sb.toString();
        String str3 = newStr.substring(0, newStr.length() - 1);
        String sql = "  delete  from  treesoft_log  where id in (" + str3 + ")";
        boolean bl = db.do_update(sql);
        return bl;
    }

    public boolean deleteAllLog() throws Exception {
        DBUtil db = new DBUtil();
        boolean bl = db.do_update("  delete  from  treesoft_log  ");
        return bl;
    }

    public List<Map<String, Object>> allLogList(String startTime, String endTime, String userName, String log) throws Exception {
        DBUtil db1 = new DBUtil();
        String sql2 = " select create_time as createTime ,username ,ip, log,result,module,mess,level,database_name as databaseName from treesoft_log where 1=1 ";
        if (!StringUtils.isEmpty(startTime)) {
            sql2 = String.valueOf(sql2) + " and  create_time>='" + startTime + "'";
        }
        if (!StringUtils.isEmpty(endTime)) {
            sql2 = String.valueOf(sql2) + " and  create_time <='" + endTime + "'";
        }
        if (!StringUtils.isEmpty(userName)) {
            sql2 = String.valueOf(sql2) + " and username like '%" + userName + "%'";
        }
        if (!StringUtils.isEmpty(log)) {
            sql2 = String.valueOf(sql2) + " and log like '%" + log + "%'";
        }
        List<Map<String, Object>> list = db1.executeQuery(String.valueOf(sql2) + " order by create_time desc ");
        return list;
    }

    public boolean saveLog(String sql, String username, String ip, String databaseName, String databaseConfigId) throws Exception {
        DBUtil db = new DBUtil();
        String sqls = "insert into treesoft_log( create_time ,operator,username ,log, ip ,database_name, config_id  ) values ( '" + DateUtils.getDateTime() + "','operator','" + username + "','" + sql.replace("'", "''") + "','" + ip + "','" + databaseName + "','" + databaseConfigId + "' )";
        boolean bl = db.do_update(sqls);
        return bl;
    }
}
