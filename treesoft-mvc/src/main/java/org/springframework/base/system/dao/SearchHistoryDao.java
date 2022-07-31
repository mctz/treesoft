package org.springframework.base.system.dao;

import java.util.List;
import java.util.Map;
import org.springframework.base.system.dbUtils.DBUtil;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.stereotype.Repository;

@Repository
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/dao/SearchHistoryDao.class */
public class SearchHistoryDao {
    public boolean saveSearchHistory(String name, String sql, String dbName, String userId, String configId) {
        DBUtil db = new DBUtil();
        String insertSQL = "insert into  treesoft_search_history ( create_time, sqls, name, database,user_id , config_id) values (  datetime('now') ,'" + sql + "','" + name + "','" + dbName + "','" + userId + "','" + configId + "')";
        try {
            db.do_update(insertSQL);
            return true;
        } catch (Exception e) {
            LogUtil.e("保存 查询 条件出错，", e);
            return false;
        }
    }

    public boolean updateSearchHistory(String id, String name, String sql, String dbName, String configId) {
        DBUtil db = new DBUtil();
        String insertSQL = "update  treesoft_search_history set create_time= datetime('now') , sqls='" + sql + "', name = '" + name + "', database='" + dbName + "',config_id='" + configId + "' where id='" + id + "' ";
        try {
            db.do_update(insertSQL);
            return true;
        } catch (Exception e) {
            LogUtil.e("更新 查询 条件出错，", e);
            return false;
        }
    }

    public boolean deleteSearchHistory(String id) {
        DBUtil db = new DBUtil();
        String insertSQL = "delete  from  treesoft_search_history  where id='" + id + "' ";
        try {
            db.do_update(insertSQL);
            return true;
        } catch (Exception e) {
            LogUtil.e("删除  SQL 查询条件出错，", e);
            return false;
        }
    }

    public List<Map<String, Object>> selectSearchHistory() {
        DBUtil db = new DBUtil();
        List<Map<String, Object>> list = db.executeQuery(" select * from  treesoft_search_history ");
        return list;
    }

    public Map<String, Object> selectSearchHistoryById(String id) {
        DBUtil db = new DBUtil();
        String sql = " select * from  treesoft_search_history where id='" + id + "'";
        List<Map<String, Object>> list = db.executeQuery(sql);
        Map<String, Object> map = list.get(0);
        return map;
    }
}
