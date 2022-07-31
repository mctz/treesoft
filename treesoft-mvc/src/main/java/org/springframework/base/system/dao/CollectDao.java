package org.springframework.base.system.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.base.system.dbUtils.DBUtil;
import org.springframework.base.system.dbUtils.DBUtil2;
import org.springframework.base.system.dbUtils.DBUtilJDBC;
import org.springframework.base.system.entity.Collect;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.utils.DateUtils;
import org.springframework.base.system.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/dao/CollectDao.class */
public class CollectDao {
    @Autowired
    private ConfigDao configDao;

    public Page<Map<String, Object>> collectListSearch(Page<Map<String, Object>> page, String name, String sourceDatabase, String status) throws Exception {
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        DBUtil db1 = new DBUtil();
        String sql2 = " select t1.id, t1.name , t1.alias ,t1.source_config_id as sourceConfigId,  t1.source_database as sourceDatabase, t1.do_sql as doSql, t1.model, t1.operation, t1.plan, t1.status, t1.toplimit, t1.app_key as appKey,t1.request_number as requestNumber, t2.name as sourceConfigName  from treesoft_collect t1 left join treesoft_config t2 on t1.source_config_id = t2.id where 1=1 ";
        if (!StringUtils.isEmpty(name)) {
            sql2 = String.valueOf(sql2) + " and t1.name like '%" + name + "%'";
        }
        if (!StringUtils.isEmpty(sourceDatabase)) {
            sql2 = String.valueOf(sql2) + " and t1.source_database like '%" + sourceDatabase + "%'";
        }
        int rowCount = db1.executeQueryForCount(sql2);
        List<Map<String, Object>> list = db1.executeQuery(String.valueOf(sql2) + "  limit " + limitFrom + "," + pageSize);
        page.setTotalCount(rowCount);
        page.setResult(list);
        return page;
    }

    public boolean collectDelete(String[] ids) throws Exception {
        DBUtil db = new DBUtil();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ids.length; i++) {
            sb = sb.append("'" + ids[i] + "',");
        }
        String newStr = sb.toString();
        String str3 = newStr.substring(0, newStr.length() - 1);
        String sql = "  delete  from  treesoft_collect  where id in (" + str3 + ")";
        boolean bl = db.do_update(sql);
        return bl;
    }

    public boolean collectSave(Collect collect) throws Exception {
        String sql;
        String sql2;
        DBUtil db = new DBUtil();
        String id = collect.getId();
        if (!StringUtils.isEmpty(collect.getDoSql())) {
            collect.setDoSql(collect.getDoSql().replaceAll("'", "''"));
        }
        if (StringUtils.isEmpty(id)) {
            String tempId = StringUtil.getUUID();
            sql = " insert into treesoft_collect( id,create_time,update_time,name,alias,type, source_config_id , source_database, do_sql, model,operation, plan, status, state, send, toplimit, speed, app_key,request_number , comments) values ('" + tempId + "','" + DateUtils.getDateTime() + "','" + DateUtils.getDateTime() + "','" + collect.getName() + "','" + collect.getAlias() + "','json','" + collect.getSourceConfigId() + "','" + collect.getSourceDatabase() + "','" + collect.getDoSql() + "','" + collect.getModel() + "','" + collect.getOperation() + "','" + collect.getPlan() + "','" + collect.getStatus() + "','" + collect.getState() + "','" + collect.getSend() + "','" + collect.getToplimit() + "','" + collect.getSpeed() + "','" + collect.getAppKey() + "',0,'" + collect.getComments() + "' )";
        } else {
            String sql3 = " update  treesoft_collect set ";
            if (!StringUtils.isEmpty(collect.getName())) {
                sql3 = String.valueOf(sql3) + "name ='" + collect.getName() + "',";
            }
            if (!StringUtils.isEmpty(collect.getAlias())) {
                sql3 = String.valueOf(sql3) + "alias ='" + collect.getAlias() + "',";
            }
            if (!StringUtils.isEmpty(collect.getSourceConfigId())) {
                sql3 = String.valueOf(sql3) + "source_config_id='" + collect.getSourceConfigId() + "',";
            }
            if (!StringUtils.isEmpty(collect.getSourceDatabase())) {
                sql3 = String.valueOf(sql3) + "source_database='" + collect.getSourceDatabase() + "',";
            }
            if (!StringUtils.isEmpty(collect.getDoSql())) {
                sql3 = String.valueOf(sql3) + "do_sql='" + collect.getDoSql() + "',";
            }
            if (!StringUtils.isEmpty(collect.getModel())) {
                sql3 = String.valueOf(sql3) + "model='" + collect.getModel() + "',";
            }
            if (!StringUtils.isEmpty(collect.getOperation())) {
                sql3 = String.valueOf(sql3) + "operation='" + collect.getOperation() + "',";
            }
            if (!StringUtils.isEmpty(collect.getPlan())) {
                sql3 = String.valueOf(sql3) + "plan='" + collect.getPlan() + "',";
            }
            if (!StringUtils.isEmpty(collect.getStatus())) {
                sql3 = String.valueOf(sql3) + "status='" + collect.getStatus() + "',";
            }
            if (!StringUtils.isEmpty(collect.getState())) {
                sql3 = String.valueOf(sql3) + "state='" + collect.getState() + "',";
            }
            if (!StringUtils.isEmpty(collect.getSpeed())) {
                sql3 = String.valueOf(sql3) + "speed='" + collect.getSpeed() + "',";
            }
            if (!StringUtils.isEmpty(collect.getAppKey())) {
                sql3 = String.valueOf(sql3) + "app_key='" + collect.getAppKey() + "',";
            }
            if (!StringUtils.isEmpty(collect.getComments())) {
                sql2 = String.valueOf(sql3) + "comments='" + collect.getComments() + "'";
            } else {
                sql2 = String.valueOf(sql3) + "comments=''";
            }
            sql = String.valueOf(sql2) + "  where id='" + id + "'";
        }
        boolean bl = db.do_update(sql);
        return bl;
    }

    public Collect collectSelectById(Collect collect) throws Exception {
        DBUtil db = new DBUtil();
        String sql = " select id,name,alias , source_config_id as sourceConfigId, source_database as sourceDatabase, do_sql as doSql, model,operation, plan, status, toplimit, app_key as appKey, request_number as requestNumber  ,comments from  treesoft_collect where id='" + collect.getId() + "'";
        List<Map<String, Object>> list = db.executeQuery2(sql);
        new HashMap();
        if (list.size() > 0) {
            Map<String, Object> map = list.get(0);
            BeanUtils.populate(collect, map);
        }
        return collect;
    }

    public Collect collectSelectByAlias(String alias) throws Exception {
        Collect collectTemp = new Collect();
        DBUtil db = new DBUtil();
        String sql = " select id,name,alias , source_config_id as sourceConfigId, source_database as sourceDatabase, do_sql as doSql, model,operation, plan, status, toplimit, app_key as appKey, request_number as requestNumber  ,comments from  treesoft_collect where alias='" + alias + "'";
        List<Map<String, Object>> list = db.executeQuery2(sql);
        new HashMap();
        if (list.size() > 0) {
            Map<String, Object> map = list.get(0);
            BeanUtils.populate(collectTemp, map);
        }
        return collectTemp;
    }

    public boolean checkAliasExists(Collect collect) throws Exception {
        DBUtil db = new DBUtil();
        String sql = " select id,name,alias  from  treesoft_collect where alias='" + collect.getAlias() + "'";
        if (!StringUtils.isEmpty(collect.getId())) {
            sql = String.valueOf(sql) + " and id not in ('" + collect.getId() + "')";
        }
        List<Map<String, Object>> list = db.executeQuery2(sql);
        if (list.size() > 0) {
            return true;
        }
        return false;
    }

    public List<Map<String, Object>> executeSQLForData(Collect collectTemp, Map<String, String[]> paramMap) throws Exception {
        List<Map<String, Object>> list;
        String dbName = collectTemp.getSourceDatabase();
        String databaseConfigId = collectTemp.getSourceConfigId();
        Map<String, Object> configMap = this.configDao.getConfigById(databaseConfigId);
        String sql2 = collectTemp.getDoSql();
        String databaseType = (String) configMap.get("databaseType");
        if (paramMap != null) {
            Iterator<String> it = paramMap.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                String[] valueArray = paramMap.get(key);
                String value = valueArray.length > 0 ? valueArray[0] : "";
                sql2 = sql2.replace("#(" + key + ")", value);
            }
        }
        String sql22 = sql2.trim();
        if (sql22.endsWith(";")) {
            sql22 = sql22.substring(0, sql22.length() - 1);
        }
        new ArrayList();
        if (databaseType.equals("HANA2") || databaseType.equals("ShenTong") || databaseType.equals("Cache")) {
            DBUtilJDBC dbJDBC = new DBUtilJDBC(dbName, databaseConfigId);
            list = dbJDBC.queryForListCommonMethod(sql22);
        } else {
            DBUtil2 db2 = new DBUtil2(dbName, databaseConfigId);
            list = db2.queryForListForMySql(sql22);
        }
        return list;
    }

    public boolean addRequestNumber(String id) throws Exception {
        String sql3 = " update treesoft_collect set  request_number = request_number + 1 where id ='" + id + "'";
        DBUtil localDB = new DBUtil();
        localDB.do_update(sql3);
        return true;
    }
}
