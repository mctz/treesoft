package org.springframework.base.system.dao;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.base.system.dbUtils.DBUtil2;
import org.springframework.base.system.entity.DmsDto;
import org.springframework.base.system.entity.IdsDto;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.utils.NetworkUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/dao/DesignTableDao.class */
public class DesignTableDao {
    @Autowired
    private LogDao logDao;

    public Page<Map<String, Object>> selectTableIndexsForMySQL(Page<Map<String, Object>> page, DmsDto dto) throws Exception {
        DBUtil2 db1 = new DBUtil2(dto.getDatabaseName(), dto.getDatabaseConfigId());
        String sql2 = " show index from  " + dto.getDatabaseName() + "." + dto.getTableName() + " where key_name != 'PRIMARY'";
        List<Map<String, Object>> list = db1.queryForListCommonMethod(sql2);
        Iterator<Map<String, Object>> it = list.iterator();
        while (it.hasNext()) {
            Map<String, Object> map = it.next();
            map.put("columnName", new StringBuilder().append(map.get("Column_name")).toString());
            map.put("indexName", new StringBuilder().append(map.get("Key_name")).toString());
        }
        page.setTotalCount(list.size());
        page.setResult(list);
        return page;
    }

    public boolean indexSaveForMySQL(HttpServletRequest request, IdsDto dto, String username) throws Exception {
        DBUtil2 db1 = new DBUtil2(dto.getDatabaseName(), dto.getDatabaseConfigId());
        String ip = NetworkUtil.getIpAddress(request);
        String sql = " create index  " + dto.getIndexName() + " on " + dto.getDatabaseName() + "." + dto.getTableName() + "(" + dto.getColumn_name() + ")";
        db1.setupdateData(sql);
        this.logDao.saveLog("保存索引 " + sql, username, ip, dto.getDatabaseName(), dto.getDatabaseConfigId());
        return true;
    }

    public boolean indexDeleteForMySQL(HttpServletRequest request, IdsDto dto, String username) throws Exception {
        DBUtil2 db1 = new DBUtil2(dto.getDatabaseName(), dto.getDatabaseConfigId());
        String ip = NetworkUtil.getIpAddress(request);
        String sql = " drop index  " + dto.getIndexName() + " on " + dto.getDatabaseName() + "." + dto.getTableName();
        db1.setupdateData(sql);
        this.logDao.saveLog("删除索引 " + sql, username, ip, dto.getDatabaseName(), dto.getDatabaseConfigId());
        return true;
    }

    public Page<Map<String, Object>> selectTableTriggersForMySQL(Page<Map<String, Object>> page, DmsDto dto) throws Exception {
        DBUtil2 db1 = new DBUtil2(dto.getDatabaseName(), dto.getDatabaseConfigId());
        String sql2 = " select * from information_schema.triggers where event_object_table = '" + dto.getTableName() + "' and  event_object_schema='" + dto.getDatabaseName() + "'";
        List<Map<String, Object>> list = db1.queryForListCommonMethod(sql2);
        page.setTotalCount(list.size());
        page.setResult(list);
        return page;
    }

    public boolean triggerDeleteForMySQL(HttpServletRequest request, IdsDto dto, String username) throws Exception {
        DBUtil2 db1 = new DBUtil2(dto.getDatabaseName(), dto.getDatabaseConfigId());
        String ip = NetworkUtil.getIpAddress(request);
        String sql = " drop trigger " + dto.getDatabaseName() + "." + dto.getTriggerName();
        db1.setupdateData(sql);
        this.logDao.saveLog("删除触发器 " + sql, username, ip, dto.getDatabaseName(), dto.getDatabaseConfigId());
        return true;
    }

    public Page<Map<String, Object>> selectTableForeignKeyForMySQL(Page<Map<String, Object>> page, DmsDto dto) throws Exception {
        DBUtil2 db1 = new DBUtil2(dto.getDatabaseName(), dto.getDatabaseConfigId());
        String sql2 = " select table_schema, constraint_name, column_name ,  referenced_table_schema,referenced_table_name, referenced_column_name  from information_schema.key_column_usage  where  referenced_table_name is not null  and table_schema ='" + dto.getDatabaseName() + "' and  table_name = '" + dto.getTableName() + "' ";
        List<Map<String, Object>> list = db1.queryForListCommonMethod(sql2);
        page.setTotalCount(list.size());
        page.setResult(list);
        return page;
    }

    public boolean deleteForeignKeyForMySQL(HttpServletRequest request, IdsDto dto, String username) throws Exception {
        DBUtil2 db1 = new DBUtil2(dto.getDatabaseName(), dto.getDatabaseConfigId());
        String ip = NetworkUtil.getIpAddress(request);
        String sql = " ALTER TABLE " + dto.getDatabaseName() + "." + dto.getTableName() + " DROP FOREIGN KEY  " + dto.getForeignKeyName();
        db1.setupdateData(sql);
        this.logDao.saveLog("删除外键  " + sql, username, ip, dto.getDatabaseName(), dto.getDatabaseConfigId());
        return true;
    }
}
