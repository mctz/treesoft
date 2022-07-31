package org.springframework.base.system.dao;

import com.alibaba.druid.sql.parser.ParserException;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.base.system.dbUtils.DBUtil;
import org.springframework.base.system.dbUtils.DBUtil2;
import org.springframework.base.system.entity.Attach;
import org.springframework.base.system.entity.NewQueryDTO;
import org.springframework.base.system.entity.Order;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.service.SQLParserService;
import org.springframework.base.system.utils.DateUtils;
import org.springframework.base.system.utils.FileUtil;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/dao/OrderDao.class */
public class OrderDao {
    @Autowired
    private SQLParserService sqlParserService;
    @Autowired
    private LogDao logDao;

    public Page<Map<String, Object>> orderListData(Page<Map<String, Object>> page, String startTime, String endTime, String userId, String doSql) throws Exception {
        String sql2;
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        DBUtil db1 = new DBUtil();
        String sql22 = " select id ,create_time as createTime,update_time as updateTime ,order_name as orderName ,order_number as orderNumber,order_type as orderType, level, create_user_id, create_user_name as createUserName,do_sql as doSql ,status ,config_id ,config_name as configName, database_name as databaseName ,run_time as runTime,run_user_name as runUserName,run_status as runStatus, run_message as runMessage , audit_time as auditTime,audit_user_name as auditUserName,comments ,remark ,attach_name as attachName, attach_url as attachUrl  from  treesoft_order  where 1 = 1 and (create_user_id = '" + userId + "' or create_user_id = '' )";
        if (!StringUtils.isEmpty(startTime)) {
            sql22 = String.valueOf(sql22) + " and create_time >='" + startTime + "'";
        }
        if (!StringUtils.isEmpty(endTime)) {
            sql22 = String.valueOf(sql22) + " and create_time <='" + endTime + "'";
        }
        if (!StringUtils.isEmpty(doSql)) {
            sql22 = String.valueOf(sql22) + " and do_sql like '%" + doSql + "%'";
        }
        int rowCount = db1.executeQueryForCount(String.valueOf(sql22) + " order by create_time desc ");
        List<Map<String, Object>> list = db1.executeQuery(String.valueOf(sql2) + "  limit " + limitFrom + "," + pageSize);
        page.setTotalCount(rowCount);
        page.setResult(list);
        return page;
    }

    public Page<Map<String, Object>> orderAuditListData(Page<Map<String, Object>> page, String startTime, String endTime, String userName, String doSql) throws Exception {
        String sql2;
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        DBUtil db1 = new DBUtil();
        String sql22 = " select id ,create_time as createTime,update_time as updateTime ,order_name as orderName ,order_number as orderNumber,order_type as orderType, level, create_user_id, create_user_name as createUserName,do_sql as doSql ,status ,config_id as configId ,config_name as configName, database_name as databaseName ,run_time as runTime,run_user_name as runUserName,run_status as runStatus, run_message as runMessage , audit_time as auditTime,audit_user_name as auditUserName,comments ,remark, attach_name as attachName, attach_url as attachUrl from  treesoft_order where 1=1 ";
        if (!StringUtils.isEmpty(startTime)) {
            sql22 = String.valueOf(sql22) + " and create_time >='" + startTime + "'";
        }
        if (!StringUtils.isEmpty(endTime)) {
            sql22 = String.valueOf(sql22) + " and create_time <='" + endTime + "'";
        }
        if (!StringUtils.isEmpty(userName)) {
            sql22 = String.valueOf(sql22) + " and create_user_name like '%" + userName + "%'";
        }
        if (!StringUtils.isEmpty(doSql)) {
            sql22 = String.valueOf(sql22) + " and do_sql like '%" + doSql + "%'";
        }
        int rowCount = db1.executeQueryForCount(String.valueOf(sql22) + " order by status , create_time desc ");
        List<Map<String, Object>> list = db1.executeQuery(String.valueOf(sql2) + "  limit " + limitFrom + "," + pageSize);
        page.setTotalCount(rowCount);
        page.setResult(list);
        return page;
    }

    public boolean deleteOrder(String[] ids) throws Exception {
        DBUtil db = new DBUtil();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ids.length; i++) {
            sb = sb.append("'" + ids[i] + "',");
        }
        String newStr = sb.toString();
        String str3 = newStr.substring(0, newStr.length() - 1);
        String sql = "  delete  from  treesoft_order  where id in (" + str3 + ")";
        boolean bl = db.do_update(sql);
        return bl;
    }

    public String orderRun(String id, String runUserId, String runUserName, String ip, List<Attach> attachList) throws Exception {
        Map<String, Object> order = getOrderById(id);
        String doSQL = (String) order.get("doSql");
        String databaseConfigId = (String) order.get("configId");
        String databaseName = (String) order.get("databaseName");
        String orderNumber = (String) order.get("orderNumber");
        String runMess = order.get("runMessage") == null ? "" : order.get("runMessage").toString();
        if (StringUtils.isEmpty(runMess)) {
            runMess = "";
        }
        DBUtil db = new DBUtil();
        if (!StringUtils.isEmpty(doSQL)) {
            runMess = String.valueOf(runMess) + runSQL(databaseName, databaseConfigId, doSQL, orderNumber, id, runUserId, runUserName);
        }
        for (int i = 0; i < attachList.size(); i++) {
            String filePath = attachList.get(i).getFilePath();
            File sqlFile = new File(filePath);
            if (sqlFile.exists()) {
                String sqlStr = FileUtil.readFileContent(sqlFile);
                doSQL = String.valueOf(doSQL) + sqlStr;
                runMess = String.valueOf(runMess) + runSQL(databaseName, databaseConfigId, sqlStr, orderNumber, id, runUserId, runUserName);
            }
        }
        String runMess2 = runMess.replaceAll("'", "''");
        String sql = "update treesoft_order set run_status = '2', status = '4', run_user_id = '" + runUserId + "', run_user_name = '" + runUserName + "', run_time = '" + DateUtils.getDateTime() + "', run_message = '" + runMess2 + "'  where id = '" + id + "'";
        db.do_update2(sql);
        this.logDao.saveLog("执行工单 ：" + orderNumber + "，SQL：" + doSQL, runUserName, ip, databaseName, databaseConfigId);
        return runMess2;
    }

    public boolean orderSave(Order order) throws Exception {
        String orderNumber;
        DBUtil db = new DBUtil();
        String status = order.getStatus();
        if (status == null || status.equals("")) {
        }
        String doSql = order.getDoSql();
        order.setDoSql(StringEscapeUtils.unescapeHtml4(doSql.replaceAll("'", "''")));
        Map<String, Object> mapTemp = selectOrderByNumber(DateUtils.getDateTimeStringNotTime(new Date()));
        if (mapTemp != null) {
            String orderNumber2 = (String) mapTemp.get("orderNumber");
            int i = Integer.parseInt(orderNumber2.substring(8));
            String tempNum = new StringBuilder(String.valueOf(i + 1)).toString();
            for (int y = 0; y < 6 - tempNum.length(); y++) {
                tempNum = "0" + tempNum;
            }
            orderNumber = String.valueOf(DateUtils.getDateTimeStringNotTime(new Date())) + tempNum;
        } else {
            orderNumber = String.valueOf(DateUtils.getDateTimeStringNotTime(new Date())) + "0001";
        }
        if (StringUtils.isEmpty(order.getAttachName())) {
            order.setAttachName("");
        }
        if (StringUtils.isEmpty(order.getAttachUrl())) {
            order.setAttachUrl("");
        }
        String sql = " insert into treesoft_order ( id ,create_time , update_time ,order_name,order_number,order_type, level, create_user_id,create_user_name,do_sql,status, config_id , config_name, database_name,run_status, audit_user_name, audit_time, comments ) values ( '" + order.getId() + "','" + DateUtils.getDateTime() + "','" + DateUtils.getDateTime() + "','" + order.getOrderName() + "','" + orderNumber + "','" + order.getOrderType() + "','" + order.getLevel() + "','" + order.getCreateUserId() + "','" + order.getCreateUserName() + "','" + order.getDoSql() + "','" + order.getStatus() + "','" + order.getConfigId() + "','" + order.getConfigName() + "','" + order.getDatabaseName() + "','0','" + order.getAuditUserName() + "','" + order.getAuditTime() + "','" + order.getComments() + "')";
        boolean bl = db.do_update(sql);
        return bl;
    }

    public boolean orderUpdate(Order order) throws Exception {
        DBUtil db = new DBUtil();
        String doSql = order.getDoSql();
        order.setDoSql(StringEscapeUtils.unescapeHtml4(doSql.replaceAll("'", "''")));
        String sql = " update treesoft_order  set update_time='" + DateUtils.getDateTime() + "' ,do_sql='" + order.getDoSql() + "', config_id ='" + order.getConfigId() + "', status ='" + order.getStatus() + "', config_name='" + order.getConfigName() + "', database_name='" + order.getDatabaseName() + "', ";
        boolean bl = db.do_update(String.valueOf(String.valueOf(sql) + "comments='" + order.getComments() + "'") + " where id='" + order.getId() + "'");
        return bl;
    }

    public boolean updateOrderStatus(String orderId, String status) {
        DBUtil db = new DBUtil();
        boolean bl = true;
        try {
            String sql = "update treesoft_order set status='" + status + "' where id='" + orderId + "'";
            bl = db.do_update2(sql);
        } catch (Exception e) {
            LogUtil.e("updateOrderStatus出错,", e);
        }
        return bl;
    }

    public boolean updateOrderStatusByIds(String[] ids, String status, String remark, String auditUserId, String auditUserName) {
        DBUtil db = new DBUtil();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ids.length; i++) {
            sb = sb.append("'" + ids[i] + "',");
        }
        String newStr = sb.toString();
        String str3 = newStr.substring(0, newStr.length() - 1);
        boolean bl = true;
        try {
            String sql = "update treesoft_order set status='" + status + "', remark='" + remark + "', audit_user_id='" + auditUserId + "', audit_user_name='" + auditUserName + "', audit_time='" + DateUtils.getDateTime() + "'  where id in (" + str3 + ")";
            bl = db.do_update2(sql);
        } catch (Exception e) {
            LogUtil.e("updateOrderStatusByIds 更新工单状态 出错,", e);
            e.printStackTrace();
        }
        return bl;
    }

    public Map<String, Object> getOrderById(String id) {
        DBUtil db = new DBUtil();
        String sql = " select id ,create_time as createTime,update_time as updateTime ,order_name as orderName ,order_number as orderNumber, order_type as orderType, level, create_user_id, create_user_name as createUserName,do_sql as doSql ,status ,config_id as configId,config_name as configName, database_name as databaseName ,run_time as runTime,run_user_name as runUserName, run_message as runMessage , audit_time as auditTime,audit_user_name as auditUserName,comments ,remark , attach_name as attachName, attach_url as attachUrl  from  treesoft_order   where id='" + id + "'";
        List<Map<String, Object>> list = db.executeQuery(sql);
        Map<String, Object> map = list.get(0);
        return map;
    }

    public Map<String, Object> selectOrderByNumber(String orderNumber) {
        DBUtil db = new DBUtil();
        String sql = " select id ,create_time as createTime,update_time as updateTime ,order_name as orderName ,order_number as orderNumber, order_type as orderType, level, create_user_id, create_user_name as createUserName,do_sql as doSql ,status ,config_id as configId,config_name as configName, database_name as databaseName ,run_time as runTime,run_user_name as runUserName, run_message as runMessage , audit_time as auditTime,audit_user_name as auditUserName,comments ,remark, attach_name as attachName, attach_url as attachUrl  from  treesoft_order   where order_number like '" + orderNumber + "%' order by order_number desc ";
        List<Map<String, Object>> list = db.executeQuery(sql);
        if (list.size() == 0) {
            return null;
        }
        Map<String, Object> map = list.get(0);
        return map;
    }

    public List<Map<String, Object>> selectOrderByIds(String[] ids) throws Exception {
        DBUtil db = new DBUtil();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ids.length; i++) {
            sb = sb.append("'" + ids[i] + "',");
        }
        String newStr = sb.toString();
        String str3 = newStr.substring(0, newStr.length() - 1);
        String sqlTemp = " select id ,status  from  treesoft_order  where id in (" + str3 + ")";
        List<Map<String, Object>> list = db.executeQuery(sqlTemp);
        return list;
    }

    public int getAuditOrderNumber() throws Exception {
        DBUtil db = new DBUtil();
        List<Map<String, Object>> list = db.executeQuery(" select count(*) as number  from  treesoft_order  where status='0' ");
        Map<String, Object> map = list.get(0);
        int num = ((Integer) map.get("number")).intValue();
        return num;
    }

    public String runSQL(String databaseName, String databaseConfigId, String doSQL, String orderNumber, String id, String runUserId, String runUserName) throws Exception {
        DBUtil db = new DBUtil();
        DBUtil2 targerDb = new DBUtil2(databaseName, databaseConfigId);
        String sqlTemp = "";
        String runMess = "";
        try {
            NewQueryDTO queryDTO = new NewQueryDTO();
            queryDTO.setDatabaseConfigId(databaseConfigId);
            queryDTO.setSql(doSQL);
            List<Map<String, String>> sqlList = this.sqlParserService.operationSQL(queryDTO);
            for (int y = 0; y < sqlList.size(); y++) {
                Date b1 = new Date();
                sqlTemp = sqlList.get(y).get("sql");
                int totals = targerDb.setupdateData(sqlTemp);
                Date b2 = new Date();
                long time = b2.getTime() - b1.getTime();
                runMess = String.valueOf(runMess) + "【SQL" + (y + 1) + "】" + sqlTemp + "; \r\n 执行成功，影响 " + totals + " 行，耗时 " + time + " 毫秒，时间：" + DateUtils.getDateTime() + " \r\n ";
            }
            return runMess;
        } catch (ParserException e) {
            LogUtil.e("工单任务执行失败：" + orderNumber + "，SQL：" + doSQL + " 解析失败，请检查SQL语句是否正确 .", e);
            String sql = "update treesoft_order set run_status='3', status='4', run_user_id='" + runUserId + "', run_user_name='" + runUserName + "', run_time='" + DateUtils.getDateTime() + "', run_message='" + (String.valueOf(runMess) + "【SQL】" + sqlTemp + "; 执行失败，   \r\n" + e + " \r\n").replaceAll("'", "''") + "'  where id ='" + id + "'";
            db.do_update2(sql);
            throw new Exception((Throwable) e);
        } catch (Exception e2) {
            LogUtil.e("工单任务执行失败：" + orderNumber + "，SQL：" + doSQL + "，", e2);
            String sql2 = "update treesoft_order set run_status='3', status='4', run_user_id='" + runUserId + "', run_user_name='" + runUserName + "', run_time='" + DateUtils.getDateTime() + "', run_message='" + (String.valueOf(runMess) + "【SQL】" + sqlTemp + "; 执行失败，   \r\n" + e2 + " \r\n").replaceAll("'", "''") + "'  where id ='" + id + "'";
            db.do_update2(sql2);
            throw new Exception(e2);
        }
    }
}
