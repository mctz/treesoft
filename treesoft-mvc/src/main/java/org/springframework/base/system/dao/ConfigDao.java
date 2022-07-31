package org.springframework.base.system.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.base.system.dbUtils.DBUtil;
import org.springframework.base.system.dynamicDataSource.DynamicDataSourceDruid;
import org.springframework.base.system.entity.Config;
import org.springframework.base.system.entity.IdsDto;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.utils.Constants;
import org.springframework.base.system.utils.CryptoUtil;
import org.springframework.base.system.utils.DateUtils;
import org.springframework.base.system.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.util.HtmlUtils;

@Repository
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/dao/ConfigDao.class */
public class ConfigDao {
    @Autowired
    DynamicDataSourceDruid dynamicDataSourceDruid;

    public Map<String, Object> getConfigById(String id) {
        String password;
        Map<String, Object> config = Constants.configStaticMap.get(id);
        if (config != null) {
            return config;
        }
        DBUtil db = new DBUtil();
        String sql = " select id, name, database_type as databaseType, database_name as databaseName, user_name as userName,  password, port, ip ,url ,is_default as isDefault,is_default_view as isDefaultView, memo, export_limit as exportLimit, is_read as isRead, sort, driver from  treesoft_config where id='" + id + "'";
        List<Map<String, Object>> list = db.executeQuery2(sql);
        new HashMap();
        if (list.size() > 0) {
            Map<String, Object> map = list.get(0);
            String password2 = CryptoUtil.decode(new StringBuilder().append(map.get("password")).toString());
            if (password2.split("`").length > 1) {
                password = password2.split("`")[1];
            } else {
                password = "";
            }
            map.put("password", password);
            Constants.configStaticMap.put(id, map);
            return map;
        }
        return new HashMap();
    }

    public boolean exist(String tenantKey) {
        return true;
    }

    public Map<String, Object> getConfigBySort(String sort) {
        DBUtil db = new DBUtil();
        String sql = " select id, name,database_type as databaseType ,database_name as databaseName, user_name as userName, port, ip ,url ,is_default as isDefault ,is_default_view as isDefaultView, memo, export_limit as exportLimit, is_read as isRead, sort from  treesoft_config where sort ='" + sort + "'";
        List<Map<String, Object>> list = db.executeQuery2(sql);
        new HashMap();
        if (list.size() > 0) {
            Map<String, Object> map = list.get(0);
            return map;
        }
        return new HashMap();
    }

    public boolean checkSortIsExists(Config config) {
        String sql;
        DBUtil db = new DBUtil();
        if (StringUtils.isEmpty(config.getId())) {
            sql = " select id, name  from  treesoft_config where sort ='" + config.getSort() + "'";
        } else {
            sql = " select id, name  from  treesoft_config where sort ='" + config.getSort() + "' and id<>'" + config.getId() + "'";
        }
        List<Map<String, Object>> list = db.executeQuery2(sql);
        if (list.size() >= 1) {
            return true;
        }
        return false;
    }

    public boolean configUpdate(Config config) throws Exception {
        String sql;
        DBUtil db = new DBUtil();
        String tempId = StringUtil.getUUID();
        String id = config.getId();
        boolean existsConfig = false;
        String isDefault = config.getIsDefault();
        if (isDefault == null || isDefault.equals("")) {
            isDefault = "0";
        }
        if (!StringUtils.isEmpty(config.getId())) {
            Map<String, Object> map = getConfigById(config.getId());
            if (map.size() > 1) {
                existsConfig = true;
            }
        }
        String pwd = config.getPassword();
        config.setPassword(HtmlUtils.htmlUnescape(pwd));
        if (!StringUtils.isEmpty(config.getId()) && existsConfig) {
            config.getId();
            sql = " update treesoft_config  set database_type='" + config.getDatabaseType() + "' ,database_name='" + config.getDatabaseName() + "' ,user_name='" + config.getUserName() + "', password='" + CryptoUtil.encode(String.valueOf(config.getUserName()) + "`" + config.getPassword()) + "', is_default='" + isDefault + "', is_default_view='" + config.getIsDefaultView() + "', memo='" + config.getMemo() + "', export_limit='" + config.getExportLimit() + "', name ='" + config.getName() + "', port='" + config.getPort() + "', ip='" + config.getIp() + "', driver='" + config.getDriver() + "', is_read='" + config.getIsRead() + "', sort=" + config.getSort() + ", status='" + config.getStatus() + "', url='" + config.getUrl() + "'  where id='" + id + "'";
            Constants.configStaticMap.remove(id);
        } else {
            config.setId(tempId);
            sql = " insert into treesoft_config (id, name,create_time, database_type ,database_name, user_name ,password , port, ip ,driver, is_default , is_default_view , memo , export_limit ,is_read,sort,status, url  ) values ( '" + tempId + "','" + config.getName() + "','" + DateUtils.getDateTime() + "','" + config.getDatabaseType() + "','" + config.getDatabaseName() + "','" + config.getUserName() + "','" + CryptoUtil.encode(String.valueOf(config.getUserName()) + "`" + config.getPassword()) + "','" + config.getPort() + "','" + config.getIp() + "','" + config.getDriver() + "','" + isDefault + "','" + config.getIsDefaultView() + "','" + config.getMemo() + "','" + config.getExportLimit() + "','" + config.getIsRead() + "'," + config.getSort() + ",'" + config.getStatus() + "','" + config.getUrl() + "' ) ";
        }
        boolean bl = db.do_update(sql);
        List<Map<String, Object>> list3 = db.executeQuery(" select id, name, database_type as databaseType ,database_name as databaseName, port, ip , is_read as isRead from  treesoft_config order by is_default desc ");
        String ids = "";
        Iterator<Map<String, Object>> it = list3.iterator();
        while (it.hasNext()) {
            Map<String, Object> map2 = it.next();
            ids = String.valueOf(ids) + map2.get("id") + ",";
        }
        String updateSQL = " update treesoft_users set datascope ='" + ids.substring(0, ids.length() - 1) + "'  where username in ('admin','treesoft')";
        db.do_update2(updateSQL);
        Map<String, Object> map1 = config.config2Map(config);
        this.dynamicDataSourceDruid.insertOrUpdateCustomDbDataSources(map1);
        return bl;
    }

    public Page<Map<String, Object>> configList(Page<Map<String, Object>> page, String name, String ip, String userName) throws Exception {
        String sql;
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        String orderBy = page.getOrderBy();
        String order = page.getOrder();
        DBUtil db = new DBUtil();
        String sql2 = "select id, name, database_type as databaseType , database_name as databaseName, user_name as userName,  password, port, ip ,url , status ,is_read as isRead, is_default as isDefault from treesoft_config where 1=1 ";
        if (!StringUtils.isEmpty(name)) {
            sql2 = String.valueOf(sql2) + " and name like '%" + name + "%' ";
        }
        if (!StringUtils.isEmpty(ip)) {
            sql2 = String.valueOf(sql2) + " and ip like '%" + ip + "%' ";
        }
        if (!StringUtils.isEmpty(userName)) {
            sql2 = String.valueOf(sql2) + " and userName like '%" + userName + "%' ";
        }
        if (StringUtils.isEmpty(orderBy)) {
            sql = String.valueOf(sql2) + " order by sort ";
        } else {
            sql = String.valueOf(sql2) + " order by  " + orderBy + " " + order;
        }
        int rowCount = db.executeQueryForCount(sql);
        List<Map<String, Object>> list = db.executeQuery(String.valueOf(sql) + "  limit " + limitFrom + "," + pageSize);
        page.setTotalCount(rowCount);
        page.setResult(list);
        return page;
    }

    public Page<Map<String, Object>> configListForMonitor(Page<Map<String, Object>> page, String name, String ip, String userName) throws Exception {
        String sql;
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        String orderBy = page.getOrderBy();
        String order = page.getOrder();
        DBUtil db = new DBUtil();
        String sql2 = "select id, name, database_type as databaseType , database_name as databaseName, port, ip, status ,is_read as isRead, is_default as isDefault from treesoft_config where 1=1 ";
        if (!StringUtils.isEmpty(name)) {
            sql2 = String.valueOf(sql2) + " and name like '%" + name + "%' ";
        }
        if (!StringUtils.isEmpty(ip)) {
            sql2 = String.valueOf(sql2) + " and ip like '%" + ip + "%' ";
        }
        if (!StringUtils.isEmpty(userName)) {
            sql2 = String.valueOf(sql2) + " and userName like '%" + userName + "%' ";
        }
        if (StringUtils.isEmpty(orderBy)) {
            sql = String.valueOf(sql2) + " order by sort ";
        } else {
            sql = String.valueOf(sql2) + " order by  " + orderBy + " " + order;
        }
        int rowCount = db.executeQueryForCount(sql);
        List<Map<String, Object>> list = db.executeQuery(String.valueOf(sql) + "  limit " + limitFrom + "," + pageSize);
        page.setTotalCount(rowCount);
        page.setResult(list);
        return page;
    }

    public List<Map<String, Object>> getAllConfigList() {
        DBUtil dbUtil = new DBUtil();
        List<Map<String, Object>> list = dbUtil.getConfigList();
        return list;
    }

    public List<Map<String, Object>> getAllVaildConfigList() {
        DBUtil dbUtil = new DBUtil();
        List<Map<String, Object>> list = dbUtil.executeQuery(" select id, name, database_type as databaseType, database_name as databaseName, user_name as userName , port, ip  from  treesoft_config where status in ('1','2') ");
        return list;
    }

    public List<Map<String, Object>> getAllVaildConfigListById(IdsDto dto) {
        DBUtil dbUtil = new DBUtil();
        String sql = " select id, name, database_type as databaseType, database_name as databaseName, user_name as userName, port, ip  from  treesoft_config where status in ('1','2') ";
        if (dto.getIds().length > 0) {
            String newStr = "";
            for (int i = 0; i < dto.getIds().length; i++) {
                newStr = String.valueOf(newStr) + "'" + dto.getIds()[i] + "',";
            }
            sql = String.valueOf(sql) + " and id in (" + newStr.substring(0, newStr.length() - 1) + ")";
        }
        List<Map<String, Object>> list = dbUtil.executeQuery(sql);
        return list;
    }

    public int selectConfigNumber() {
        DBUtil db = new DBUtil();
        List<Map<String, Object>> list = db.executeQuery2(" select count(*) as num  from  treesoft_config ");
        Map<String, Object> map = list.get(0);
        int num = Integer.parseInt(new StringBuilder().append(map.get("num")).toString());
        return num;
    }

    public boolean isConfigExistsById(String configId) {
        String sql = " select id from treesoft_config where  id='" + configId + "'";
        new ArrayList();
        DBUtil db = new DBUtil();
        List<Map<String, Object>> list = db.executeQuery(sql);
        if (list.size() > 0) {
            return true;
        }
        return false;
    }

    public List<Map<String, Object>> getConfigAllDataBase() throws Exception {
        DBUtil db = new DBUtil();
        List<Map<String, Object>> list = db.executeQuery(" select id, name, database_type as databaseType ,database_name as databaseName, port, ip, export_limit as exportLimit, is_read as isRead ,status  from  treesoft_config order by is_default desc ");
        return list;
    }

    public Map<String, Object> getConfigAllDataBaseMap() throws Exception {
        DBUtil db = new DBUtil();
        List<Map<String, Object>> list = db.executeQuery(" select id, name, database_type as databaseType ,database_name as databaseName, port, ip, export_limit as exportLimit,is_read as isRead, status  from  treesoft_config order by is_default desc ");
        Map<String, Object> resMap = new HashMap<>();
        Iterator<Map<String, Object>> it = list.iterator();
        while (it.hasNext()) {
            Map<String, Object> map = it.next();
            resMap.put((String) map.get("id"), map);
        }
        return resMap;
    }

    public boolean configInsert(Config config) throws Exception {
        DBUtil db = new DBUtil();
        String id = config.getId();
        String isDefault = config.getIsDefault();
        if (isDefault == null || isDefault.equals("")) {
            isDefault = "0";
        }
        String pwd = config.getPassword();
        config.setPassword(HtmlUtils.htmlUnescape(pwd));
        String sql = " insert into treesoft_config (id, name,create_time, database_type ,database_name, user_name ,password , port, ip , is_default , is_default_view , memo , export_limit ,is_read , url  ) values ( '" + id + "','" + config.getName() + "','" + DateUtils.getDateTime() + "','" + config.getDatabaseType() + "','" + config.getDatabaseName() + "','" + config.getUserName() + "','" + CryptoUtil.encode(String.valueOf(config.getUserName()) + "`" + config.getPassword()) + "','" + config.getPort() + "','" + config.getIp() + "','" + isDefault + "','" + config.getIsDefaultView() + "','','" + config.getExportLimit() + "','" + config.getIsRead() + "','" + config.getUrl() + "' ) ";
        boolean bl = db.do_update(sql);
        return bl;
    }

    public boolean deleteConfig(String[] ids) throws Exception {
        DBUtil db = new DBUtil();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ids.length; i++) {
            sb = sb.append("'" + ids[i] + "',");
            Map<String, Object> configMess = Constants.configStaticMap.get(ids[i]);
            if (configMess != null) {
                this.dynamicDataSourceDruid.deleteCustomDbDataSources(String.valueOf(ids[i]) + Constants.POOL_NAME_SPLIT + configMess.get("databaseName"));
            }
        }
        String newStr = sb.toString();
        String str3 = newStr.substring(0, newStr.length() - 1);
        String sql = "  delete  from  treesoft_config where id in (" + str3 + ")";
        boolean bl = db.do_update(sql);
        return bl;
    }

    public List<Map<String, Object>> getAllDataBaseById(String datascope) throws Exception {
        DBUtil db = new DBUtil();
        String[] ids = datascope.split(",");
        String idTemp = "";
        for (int i = 0; i < ids.length; i++) {
            idTemp = String.valueOf(idTemp) + "'" + ids[i] + "',";
        }
        String sql = " select id, name, database_type as databaseType,database_name as databaseName, port, ip ,export_limit as exportLimit, is_read as isRead ,status   from  treesoft_config where id in (" + idTemp.substring(0, idTemp.length() - 1) + ") order by is_default desc, sort , create_time desc ";
        List<Map<String, Object>> list = db.executeQuery(sql);
        return list;
    }

    public List<Map<String, Object>> dataBaseTypeCount() throws Exception {
        DBUtil db = new DBUtil();
        List<Map<String, Object>> list = db.executeQuery(" select database_type as name, count(*) as value from  treesoft_config GROUP BY database_type ORDER BY value desc ");
        return list;
    }

    public List<Map<String, Object>> getDBTotals() throws Exception {
        DBUtil db = new DBUtil();
        List<Map<String, Object>> list = db.executeQuery(" select id, name , database_type as databaseType, totals from  treesoft_config  ORDER BY totals desc limit 5 ");
        int maxNum = 5;
        if (list.size() < 5) {
            maxNum = list.size();
        }
        for (int i = 0; i < maxNum; i++) {
            Map<String, Object> map = list.get(i);
            map.put("rank", new Integer(i + 1));
        }
        return list;
    }

    public boolean updateDBStatus(String databaseConfigId, String status) throws Exception {
        if (StringUtils.isNotEmpty(databaseConfigId) && StringUtils.isNotEmpty(status)) {
            DBUtil dbUtil = new DBUtil();
            String sql = " update  treesoft_config set status ='" + status + "'  where id = '" + databaseConfigId + "'";
            dbUtil.do_update(sql);
            return true;
        }
        return false;
    }

    public boolean updateDBDataTotals(String databaseConfigId, Integer dataTotals) throws Exception {
        DBUtil dbUtil = new DBUtil();
        String sql = " update  treesoft_config set totals ='" + dataTotals + "'  where id = '" + databaseConfigId + "'";
        dbUtil.do_update(sql);
        return true;
    }
}
