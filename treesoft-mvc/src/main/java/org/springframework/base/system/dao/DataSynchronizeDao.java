package org.springframework.base.system.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.base.system.dbUtils.DBUtil;
import org.springframework.base.system.entity.DataSynchronize;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.utils.DateUtils;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.base.system.utils.StringUtil;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/dao/DataSynchronizeDao.class */
public class DataSynchronizeDao {
    public Page<Map<String, Object>> dataSynchronizeList(Page<Map<String, Object>> page) throws Exception {
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        DBUtil db1 = new DBUtil();
        int rowCount = db1.executeQueryForCount(" select t1.id, t1.state , t1.name, t1.create_time as createTime,t1.update_time as updateTime ,t1.source_config_id as sourceConfigId , t1.source_database as sourceDataBase,t1.source_table as sourceTable,t1.do_sql as doSql ,t1.target_config_id as targetConfigId, t1.target_database as targetDataBase,t1.target_table as targetTable ,  t1.cron, t1.operation, t1.comments,t1.status ,t1.toplimit ,t1.speed , t1.increment , t1.offset, t2.name||','||t2.ip||':'||t2.port as sourceConfig ,  t3.ip||':'||t3.port as targetConfig from  treesoft_data_synchronize t1 left join treesoft_config t2  on t1.source_config_id = t2.id LEFT JOIN treesoft_config t3 on t1.target_config_id = t3.id ");
        String sql2 = String.valueOf(" select t1.id, t1.state , t1.name, t1.create_time as createTime,t1.update_time as updateTime ,t1.source_config_id as sourceConfigId , t1.source_database as sourceDataBase,t1.source_table as sourceTable,t1.do_sql as doSql ,t1.target_config_id as targetConfigId, t1.target_database as targetDataBase,t1.target_table as targetTable ,  t1.cron, t1.operation, t1.comments,t1.status ,t1.toplimit ,t1.speed , t1.increment , t1.offset, t2.name||','||t2.ip||':'||t2.port as sourceConfig ,  t3.ip||':'||t3.port as targetConfig from  treesoft_data_synchronize t1 left join treesoft_config t2  on t1.source_config_id = t2.id LEFT JOIN treesoft_config t3 on t1.target_config_id = t3.id ") + "  limit " + limitFrom + "," + pageSize;
        List<Map<String, Object>> list = db1.executeQuery(sql2);
        page.setTotalCount(rowCount);
        page.setResult(list);
        return page;
    }

    public List<Map<String, Object>> getDataSynchronizeListById(String[] ids) throws Exception {
        DBUtil du = new DBUtil();
        List<Map<String, Object>> list = du.getDataSynchronizeListById(ids);
        return list;
    }

    public List<Map<String, Object>> getDataSynchronizeList2(String state) {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            DBUtil du = new DBUtil();
            list = du.getDataSynchronizeList2(state);
            return list;
        } catch (Exception e) {
            LogUtil.e("取得全部的数据交换列表数据出错, ", e);
            return list;
        }
    }

    public boolean deleteDataSynchronize(String[] ids) throws Exception {
        DBUtil db = new DBUtil();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ids.length; i++) {
            sb = sb.append("'" + ids[i] + "',");
        }
        String newStr = sb.toString();
        String str3 = newStr.substring(0, newStr.length() - 1);
        String sql = "  delete  from  treesoft_data_synchronize  where id in (" + str3 + ")";
        boolean bl = db.do_update(sql);
        return bl;
    }

    public boolean copyDataSynchronize(String[] ids) throws Exception {
        DBUtil db = new DBUtil();
        boolean bl = true;
        for (int i = 0; i < ids.length; i++) {
            String tempId = StringUtil.getUUID();
            String sql = "  insert into treesoft_data_synchronize ( id, name, create_time ,update_time ,source_config_id,source_database, do_sql ,target_config_id , target_database,target_table, cron,operation,comments,status ,qualification ,state,toplimit,speed, offset,increment  ) select '" + tempId + "',  name,   datetime('now') , datetime('now')  ,source_config_id,source_database, do_sql ,target_config_id , target_database,target_table, cron,operation,comments,'0' ,qualification ,state ,toplimit,speed,'0', increment from treesoft_data_synchronize where id in ('" + ids[i] + "')";
            bl = db.do_update(sql);
        }
        return bl;
    }

    public boolean dataSynchronizeUpdate(DataSynchronize dataSynchronize) throws Exception {
        String sql;
        DBUtil db = new DBUtil();
        String id = dataSynchronize.getId();
        String status = dataSynchronize.getStatus();
        if (StringUtils.isEmpty(dataSynchronize.getStatus())) {
            status = "0";
        }
        if (StringUtils.isEmpty(dataSynchronize.getOffset())) {
            dataSynchronize.setOffset("0");
        }
        if (!StringUtils.isEmpty(dataSynchronize.getId()) && dataSynchronize.getIncrement().equals("0")) {
            dataSynchronize.setOffset("0");
        }
        String doSql = dataSynchronize.getDoSql();
        dataSynchronize.setDoSql(StringEscapeUtils.unescapeHtml4(doSql.replaceAll("'", "''")).replaceAll(";", ""));
        if (!id.equals("")) {
            sql = " update treesoft_data_synchronize  set name='" + dataSynchronize.getName() + "' ,source_config_id='" + dataSynchronize.getSourceConfigId() + "' ,source_database='" + dataSynchronize.getSourceDataBase() + "', source_table ='" + dataSynchronize.getSourceTable() + "', do_sql='" + dataSynchronize.getDoSql() + "', target_config_id='" + dataSynchronize.getTargetConfigId() + "', target_database ='" + dataSynchronize.getTargetDataBase() + "', target_table='" + dataSynchronize.getTargetTable() + "', cron='" + dataSynchronize.getCron() + "', status='" + status + "', state='" + dataSynchronize.getState() + "', qualification='" + dataSynchronize.getQualification() + "', comments='" + dataSynchronize.getComments() + "', toplimit='" + dataSynchronize.getToplimit() + "', speed='" + dataSynchronize.getSpeed() + "', increment='" + dataSynchronize.getIncrement() + "', offset='" + dataSynchronize.getOffset() + "', operation='" + dataSynchronize.getOperation() + "', update_time='" + dataSynchronize.getUpdateTime() + "'  where id='" + id + "'";
        } else {
            String tempId = StringUtil.getUUID();
            sql = " insert into treesoft_data_synchronize (id , name, create_time ,update_time ,source_config_id,source_database,source_table, do_sql ,target_config_id ,target_database,target_table, cron,operation,comments,status ,qualification ,state ,increment,toplimit,offset, speed ) values ( '" + tempId + "','" + dataSynchronize.getName() + "','" + DateUtils.getDateTime() + "','" + DateUtils.getDateTime() + "','" + dataSynchronize.getSourceConfigId() + "','" + dataSynchronize.getSourceDataBase() + "','" + dataSynchronize.getSourceTable() + "','" + dataSynchronize.getDoSql() + "','" + dataSynchronize.getTargetConfigId() + "','" + dataSynchronize.getTargetDataBase() + "','" + dataSynchronize.getTargetTable() + "','" + dataSynchronize.getCron() + "','" + dataSynchronize.getOperation() + "','" + dataSynchronize.getComments() + "','" + status + "','" + dataSynchronize.getQualification() + "','" + dataSynchronize.getState() + "','" + dataSynchronize.getIncrement() + "','" + dataSynchronize.getToplimit() + "','0' ,'' ) ";
        }
        boolean bl = db.do_update(sql);
        return bl;
    }

    public boolean dataSynchronizeUpdateStatus(String dataSynchronizeId, String status) {
        DBUtil db = new DBUtil();
        boolean bl = true;
        try {
            String sql = "update treesoft_data_synchronize set status='" + status + "'  where id='" + dataSynchronizeId + "'";
            bl = db.do_update2(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bl;
    }

    public boolean dataSynchronizeUpdateOffset(String dataSynchronizeId, int maxnum) {
        DBUtil db = new DBUtil();
        boolean bl = true;
        try {
            String sql = "update treesoft_data_synchronize set offset='" + maxnum + "'  where id='" + dataSynchronizeId + "'";
            bl = db.do_update2(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bl;
    }

    public Map<String, Object> getDataSynchronizeById(String id) {
        DBUtil db = new DBUtil();
        String sql = " select id, name, source_config_id as sourceConfigId ,source_database as sourceDataBase,source_table as sourceTable ,do_sql as doSql,target_config_id as targetConfigId,  target_database as targetDataBase, target_table as targetTable,cron, operation,comments ,status ,state,qualification,toplimit,increment,offset ,speed  from  treesoft_data_synchronize where id='" + id + "'";
        List<Map<String, Object>> list = db.executeQuery(sql);
        Map<String, Object> map = list.get(0);
        return map;
    }

    public boolean dataSynchronizeLogSave(String status, String comments, String dataSynchronizeId, String times, String totals) {
        DBUtil db = new DBUtil();
        boolean bl = true;
        String comments2 = comments.replaceAll("'", "''");
        try {
            String sql = " insert into treesoft_data_synchronize_log ( create_time, status ,comments,times,totals, data_synchronize_id ) values ( '" + DateUtils.getDateTime() + "','" + status + "','" + comments2 + "','" + times + "','" + totals + "','" + dataSynchronizeId + "')";
            bl = db.do_update2(sql);
        } catch (Exception e) {
            LogUtil.e("保存任务运行日志出错", e);
        }
        return bl;
    }

    public Page<Map<String, Object>> dataSynchronizeLogList(Page<Map<String, Object>> page, String dataSynchronizeId) throws Exception {
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        DBUtil db1 = new DBUtil();
        String sql = " select id, create_time as createTime, status ,comments,times ,totals  from  treesoft_data_synchronize_log where data_synchronize_id ='" + dataSynchronizeId + "' order by create_time desc ";
        int rowCount = db1.executeQueryForCount(sql);
        List<Map<String, Object>> list = db1.executeQuery(String.valueOf(sql) + "  limit " + limitFrom + "," + pageSize);
        page.setTotalCount(rowCount);
        page.setResult(list);
        return page;
    }

    public boolean deleteDataSynchronizeLog(String[] ids) throws Exception {
        DBUtil db = new DBUtil();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ids.length; i++) {
            sb = sb.append("'" + ids[i] + "',");
        }
        String newStr = sb.toString();
        String str3 = newStr.substring(0, newStr.length() - 1);
        String sql = "  delete  from  treesoft_data_synchronize_log  where id in (" + str3 + ")";
        boolean bl = db.do_update(sql);
        return bl;
    }

    public boolean deleteDataSynchronizeLogByDS(String id) throws Exception {
        DBUtil db = new DBUtil();
        String sql = "  delete  from  treesoft_data_synchronize_log  where data_synchronize_id ='" + id + "'";
        boolean bl = db.do_update(sql);
        return bl;
    }

    public int selectDataSynchronizeNumber() {
        DBUtil db = new DBUtil();
        List<Map<String, Object>> list = db.executeQuery2(" select count(*) as num  from  treesoft_data_synchronize ");
        Map<String, Object> map = list.get(0);
        int num = Integer.parseInt(new StringBuilder().append(map.get("num")).toString());
        return num;
    }

    public boolean cleanSynchronizeLog(String dataSynchronizeId) throws Exception {
        DBUtil db = new DBUtil();
        String sql = "  delete  from  treesoft_data_synchronize_log  where data_synchronize_id='" + dataSynchronizeId + "'";
        boolean bl = db.do_update(sql);
        return bl;
    }

    public boolean batchStartSynchronize(String[] ids) throws Exception {
        DBUtil db = new DBUtil();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ids.length; i++) {
            sb = sb.append("'" + ids[i] + "',");
        }
        String newStr = sb.toString();
        String str3 = newStr.substring(0, newStr.length() - 1);
        String sql = " update  treesoft_data_synchronize set state ='0'  where id in (" + str3 + ")";
        boolean bl = db.do_update(sql);
        return bl;
    }

    public boolean batchBlockSynchronize(String[] ids) throws Exception {
        DBUtil db = new DBUtil();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ids.length; i++) {
            sb = sb.append("'" + ids[i] + "',");
        }
        String newStr = sb.toString();
        String str3 = newStr.substring(0, newStr.length() - 1);
        String sql = " update  treesoft_data_synchronize set state ='1'  where id in (" + str3 + ")";
        boolean bl = db.do_update(sql);
        return bl;
    }
}
