package org.springframework.base.system.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.base.system.dbUtils.DBUtil;
import org.springframework.base.system.entity.Person;
import org.springframework.base.system.entity.TempDto;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.utils.DateUtils;
import org.springframework.base.system.utils.MD5Utils;
import org.springframework.base.system.utils.StringUtil;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/dao/PersonDao.class */
public class PersonDao {
    public boolean deletePerson(String[] ids) throws Exception {
        DBUtil db = new DBUtil();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ids.length; i++) {
            sb = sb.append("'" + ids[i] + "',");
        }
        String newStr = sb.toString();
        String str3 = newStr.substring(0, newStr.length() - 1);
        String sql = "  delete  from  treesoft_users where id in (" + str3 + ")";
        boolean bl = db.do_update(sql);
        return bl;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public Map<String, Object> getPerson(String id) {
        DBUtil db = new DBUtil();
        String sql = " select id, username,  realname ,role, status, note ,expiration , permission,datascope  from  treesoft_users where id='" + id + "'";
        List<Map<String, Object>> list = db.executeQuery2(sql);
        Map<String, Object> map = new HashMap<>();
        if (list.size() > 0) {
            map = list.get(0);
        }
        return map;
    }

    public Page<Map<String, Object>> personList(Page<Map<String, Object>> page, String username, String realname) throws Exception {
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        DBUtil db = new DBUtil();
        String sql = " select * from  treesoft_users where 1=1 ";
        if (!StringUtils.isEmpty(username)) {
            sql = String.valueOf(sql) + " and username like '%" + username + "%'";
        }
        if (!StringUtils.isEmpty(realname)) {
            sql = String.valueOf(sql) + " and realname like '%" + realname + "%'";
        }
        int rowCount = db.executeQueryForCount(sql);
        List<Map<String, Object>> list = db.executeQuery(String.valueOf(sql) + "  limit " + limitFrom + "," + pageSize);
        page.setTotalCount(rowCount);
        page.setResult(list);
        return page;
    }

    public boolean personInsert(Person person) throws Exception {
        DBUtil db = new DBUtil();
        String id = person.getId();
        String username = person.getUsername();
        String password = MD5Utils.MD5Encode(String.valueOf(person.getPassword()) + "treesoft" + username.toLowerCase());
        String sql = " insert into treesoft_users (id, create_time ,username, password,realname , status,token,role,note,expiration,datascope,permission ) values ( '" + id + "','" + DateUtils.getDateTime() + "','" + person.getUsername() + "','" + password + "','" + person.getRealname() + "','0','" + MD5Utils.MD5Encode(String.valueOf(password) + "39") + "','0','','','" + person.getDatascope() + "','" + person.getPermission() + "' ) ";
        boolean bl = db.do_update(sql);
        return bl;
    }

    public boolean personUpdate(Person person) throws Exception {
        String sql;
        String sql2;
        DBUtil db = new DBUtil();
        String id = person.getId();
        String username = person.getUsername();
        String password = person.getPassword();
        boolean existsConfig = false;
        if (!StringUtils.isEmpty(person.getId())) {
            Map<String, Object> map = getPerson(person.getId());
            if (map.size() > 1) {
                existsConfig = true;
            }
        }
        if (person.getExpiration() == null) {
            person.setExpiration("");
        }
        if (!StringUtils.isEmpty(password)) {
            password = MD5Utils.MD5Encode(String.valueOf(password) + "treesoft" + username.toLowerCase());
        }
        if (!StringUtils.isEmpty(id) && existsConfig) {
            String sql3 = " update treesoft_users  set ";
            if (!StringUtils.isEmpty(person.getUsername())) {
                sql3 = String.valueOf(sql3) + "username='" + person.getUsername() + "' ,";
            }
            if (!StringUtils.isEmpty(person.getRealname())) {
                sql3 = String.valueOf(sql3) + "realname='" + person.getRealname() + "' ,";
            }
            if (!StringUtils.isEmpty(person.getExpiration())) {
                sql3 = String.valueOf(sql3) + "expiration='" + person.getExpiration() + "' ,";
            }
            if (person.getPermission() == null) {
                sql2 = String.valueOf(sql3) + "permission='' ,";
            } else {
                sql2 = String.valueOf(sql3) + "permission='" + person.getPermission() + "' ,";
            }
            if (!StringUtils.isEmpty(person.getDatascope())) {
                sql2 = String.valueOf(sql2) + "datascope='" + person.getDatascope() + "' ,";
            }
            if (!StringUtils.isEmpty(person.getStatus())) {
                sql2 = String.valueOf(sql2) + "status='" + person.getStatus() + "' ,";
            }
            if (!StringUtils.isEmpty(person.getRole())) {
                sql2 = String.valueOf(sql2) + "role='" + person.getRole() + "' ,";
            }
            if (!StringUtils.isEmpty(person.getNote())) {
                sql2 = String.valueOf(sql2) + "note='" + person.getNote() + "' ";
            }
            if (StringUtils.isEmpty(person.getNote())) {
                sql2 = String.valueOf(sql2) + "note='' ";
            }
            sql = String.valueOf(sql2) + "  where id='" + id + "'";
        } else {
            String tempId = StringUtil.getUUID();
            sql = " insert into treesoft_users (id, create_time ,username,password,realname , status,token,role,note,expiration,datascope,permission ) values ( '" + tempId + "','" + DateUtils.getDateTime() + "','" + person.getUsername() + "','" + password + "','" + person.getRealname() + "','" + person.getStatus() + "','" + MD5Utils.MD5Encode(String.valueOf(password) + "39") + "','" + person.getRole() + "','" + person.getNote() + "','" + person.getExpiration() + "','" + person.getDatascope() + "','" + person.getPermission() + "' ) ";
        }
        boolean bl = db.do_update(sql);
        return bl;
    }

    public boolean resetPersonPass(String[] ids) throws Exception {
        DBUtil db = new DBUtil();
        for (int i = 0; i < ids.length; i++) {
            List<Map<String, Object>> list = selectUserById(ids[i]);
            String username = (String) list.get(0).get("username");
            String password = MD5Utils.MD5Encode("123321treesoft" + username.toLowerCase());
            String token = MD5Utils.MD5Encode(String.valueOf(password) + "39");
            String sql = "  update  treesoft_users set password='" + password + "' ,token ='" + token + "'  where id in ('" + ids[i] + "')";
            db.do_update(sql);
        }
        return true;
    }

    public List<Map<String, Object>> selectUserById(String userId) {
        DBUtil db = new DBUtil();
        String sql = " select * from  treesoft_users where id='" + userId + "' ";
        List<Map<String, Object>> list = db.executeQuery(sql);
        return list;
    }

    public boolean registerUpdate(TempDto tem) throws Exception {
        boolean bl;
        DBUtil db = new DBUtil();
        String personNumber = tem.getPersonNumber();
        String company = tem.getCompany();
        String token = tem.getToken();
        boolean isvalidate = tem.isIsvalidate();
        if (isvalidate) {
            String sql = "  update  treesoft_license set create_time = '" + DateUtils.getDateTime() + "' , person_number='" + personNumber + "' , company ='" + company + "' ,token ='" + token + "', mess ='registered' ";
            bl = db.do_update(sql);
        } else {
            String sql2 = "  update  treesoft_license set person_number='" + personNumber + "' , company ='' ,token ='', mess ='unregistered' ";
            db.do_update(sql2);
            bl = false;
        }
        return bl;
    }

    public List<Map<String, Object>> selectPersonByIds(String[] ids) {
        DBUtil db = new DBUtil();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ids.length; i++) {
            sb = sb.append("'" + ids[i] + "',");
        }
        String newStr = sb.toString();
        String str3 = newStr.substring(0, newStr.length() - 1);
        String sql = " select * from  treesoft_users where id in (" + str3 + ") ";
        List<Map<String, Object>> list = db.executeQuery(sql);
        return list;
    }

    public boolean userNameIsExists(String userName) {
        DBUtil db = new DBUtil();
        String sql = " select username from  treesoft_users where LOWER(username) in ('" + userName + "') ";
        List<Map<String, Object>> list = db.executeQuery(sql);
        if (list.size() > 0) {
            return true;
        }
        return false;
    }

    public boolean personUpdateDatascope(Person person) throws Exception {
        DBUtil db = new DBUtil();
        String sql = "  update  treesoft_users set datascope='" + person.getDatascope() + "'   where id ='" + person.getId() + "'";
        db.do_update(sql);
        return true;
    }

    public boolean personUpdateLastLoginTime(Person person) throws Exception {
        DBUtil db = new DBUtil();
        String sql = " update  treesoft_users set last_login_time='" + person.getLastLoginTime() + "', last_login_ip='" + person.getLastLoginIp() + "'  where id ='" + person.getId() + "'";
        db.do_update(sql);
        return true;
    }

    public int selectPersonNum(Person person) throws Exception {
        DBUtil db = new DBUtil();
        String sql = " select count(*) as num from  treesoft_users where 1=1 ";
        if (!StringUtils.isEmpty(person.getUsername())) {
            sql = String.valueOf(sql) + " and username like '%" + person.getUsername() + "%'";
        }
        if (!StringUtils.isEmpty(person.getRealname())) {
            sql = String.valueOf(sql) + " and realname like '%" + person.getRealname() + "%'";
        }
        List<Map<String, Object>> list = db.executeQuery(sql);
        int rowCount = Integer.parseInt(list.get(0).get("num").toString());
        return rowCount;
    }

    public List<Map<String, Object>> selectPersonByUserName(String username) throws Exception {
        DBUtil db = new DBUtil();
        List<Map<String, Object>> list = db.selectPersonByUserName(username);
        return list;
    }
}
