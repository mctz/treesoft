package org.springframework.base.system.web;

import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.base.system.entity.Config;
import org.springframework.base.system.entity.IdsDto;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.service.ConfigService;
import org.springframework.base.system.service.PermissionService;
import org.springframework.base.system.utils.Constants;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping({"system/config"})
@Controller
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/web/ConfigController.class */
public class ConfigController extends BaseController {
    @Autowired
    private ConfigService configService;
    @Autowired
    private PermissionService permissionService;

    @RequestMapping(value = {"configList"}, method = {RequestMethod.GET})
    public String configList(Model model) {
        return "system/configList";
    }

    @RequestMapping(value = {"configListData/{random}"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> configList(String name, String ip, String userName, HttpServletRequest request) {
        Page<Map<String, Object>> page = getPage(request);
        HttpSession session = request.getSession(true);
        String permission = (String) session.getAttribute("LOGIN_USER_PERMISSION");
        if (permission.indexOf("config") == -1) {
            Map<String, Object> map2 = new HashMap<>();
            map2.put("mess", "没有操作权限！");
            map2.put("status", "fail");
            return map2;
        }
        try {
            page = this.configService.configList(page, name, ip, userName);
            return getEasyUIData(page);
        } catch (Exception e) {
            LogUtil.e("执行出错，", e);
            return getEasyUIData(page);
        }
    }

    @RequestMapping(value = {"configListForMonitor/{random}"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> configListForMonitor(String name, String ip, String userName, HttpServletRequest request) {
        Page<Map<String, Object>> page = getPage(request);
        try {
            page = this.configService.configListForMonitor(page, name, ip, userName);
            return getEasyUIData(page);
        } catch (Exception e) {
            LogUtil.e("执行出错，", e);
            return getEasyUIData(page);
        }
    }

    @RequestMapping(value = {"addConfigForm"}, method = {RequestMethod.GET})
    public String addConfigForm(Model model) {
        return "system/configForm";
    }

    @RequestMapping(value = {"configFormFirst"}, method = {RequestMethod.GET})
    public String configFormFirst(Model model) {
        return "system/configFormFirst";
    }

    @RequestMapping(value = {"editConfigForm/{id}"}, method = {RequestMethod.GET})
    public String editConfigForm(@PathVariable("id") String id, Model model) {
        Map<String, Object> map = new HashMap<>();
        try {
            map = this.configService.getConfigById(id);
        } catch (Exception e) {
            LogUtil.e("执行出错，", e);
        }
        model.addAttribute("config", map);
        return "system/configForm";
    }

    @RequestMapping(value = {"configUpdate"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> configUpdate(@RequestBody Config config, Model model, HttpServletRequest request) {
        String status;
        String mess;
        Map<String, Object> map = new HashMap<>();
        String databaseType = config.getDatabaseType();
        String ip = config.getIp();
        String port = config.getPort();
        String dbName = config.getDatabaseName();
        String url = "";
        String driver = "";
        HttpSession session = request.getSession(true);
        String permission = (String) session.getAttribute("LOGIN_USER_PERMISSION");
        if (permission.indexOf("config") == -1) {
            Map<String, Object> map2 = new HashMap<>();
            map2.put("mess", "没有操作权限！");
            map2.put("status", "fail");
            return map2;
        } else if (StringUtils.isEmpty(config.getName())) {
            map.put("mess", "配置名称 必填！");
            map.put("status", "fail");
            return map;
        } else if (StringUtils.isEmpty(config.getDatabaseType())) {
            map.put("mess", "数据库类型 必填！");
            map.put("status", "fail");
            return map;
        } else if (StringUtils.isEmpty(config.getDatabaseName())) {
            map.put("mess", "默认数据库 必填！");
            map.put("status", "fail");
            return map;
        } else if (StringUtils.isEmpty(config.getIp())) {
            map.put("mess", "IP地址 必填！");
            map.put("status", "fail");
            return map;
        } else if (StringUtils.isEmpty(config.getPort())) {
            map.put("mess", "端口 必填！");
            map.put("status", "fail");
            return map;
        } else if (StringUtils.isEmpty(config.getIsDefault())) {
            map.put("mess", "默认值 必填！");
            map.put("status", "fail");
            return map;
        } else {
            int num = this.configService.selectConfigNumber();
            if (num > 2 && versionType.equals("01")) {
                map.put("mess", "您好，试用版本限制配置数量，请购买商业授权！");
                map.put("status", "fail");
                return map;
            }
            boolean isvalidate = this.permissionService.identifying();
            if (num >= 3 && versionType.equals("02") && !isvalidate) {
                map.put("mess", "您好，试用版本限制配置数量，请购买商业授权！");
                map.put("status", "fail");
                return map;
            } else if (!StringUtils.isEmpty(config.getSort()) && this.configService.checkSortIsExists(config)) {
                map.put("mess", "排序号已存在，请正确配置！");
                map.put("status", "fail");
                return map;
            } else if (databaseType.equals("Informix") && !StringUtils.isEmpty(config.getDatabaseName()) && config.getDatabaseName().indexOf("@") < 0) {
                map.put("mess", "Informix默认数据库填写格式： 数据库名@实例名");
                map.put("status", "fail");
                return map;
            } else {
                if (databaseType.equals("MySQL") || databaseType.equals("TiDB")) {
                    driver = "com.mysql.jdbc.Driver";
                    url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName + "?characterEncoding=utf8&tinyInt1isBit=false&useSSL=false&serverTimezone=GMT%2B8";
                }
                if (databaseType.equals("MariaDB")) {
                    driver = "com.mysql.jdbc.Driver";
                    url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName + "?characterEncoding=utf8&tinyInt1isBit=false&useSSL=false";
                }
                if (databaseType.equals("MySql8.0")) {
                    driver = "com.mysql.cj.jdbc.Driver";
                    url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName + "?useUnicode=true&characterEncoding=utf-8&tinyInt1isBit=false&zeroDateTimeBehavior=CONVERT_TO_NULL&useSSL=false&serverTimezone=GMT%2B8";
                }
                if (databaseType.equals("Oracle")) {
                    driver = "oracle.jdbc.driver.OracleDriver";
                    url = "jdbc:oracle:thin:@//" + ip + ":" + port + "/" + dbName;
                }
                if (databaseType.equals("HANA2")) {
                    driver = "com.sap.db.jdbc.Driver";
                    url = "jdbc:sap://" + ip + ":" + port + "?reconnect=true";
                }
                if (databaseType.equals("PostgreSQL")) {
                    driver = "org.postgresql.Driver";
                    url = "jdbc:postgresql://" + ip + ":" + port + "/" + dbName;
                }
                if (databaseType.equals("SQL Server")) {
                    driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
                    url = "jdbc:sqlserver://" + ip + ":" + port + ";database=" + dbName;
                }
                if (databaseType.equals("Hive2")) {
                    driver = "org.apache.hive.jdbc.HiveDriver";
                    url = "jdbc:hive2://" + ip + ":" + port + "/" + dbName;
                }
                if (databaseType.equals("Impala")) {
                    driver = "com.cloudera.impala.jdbc41.Driver";
                    url = "jdbc:impala://" + ip + ":" + port + "/" + dbName;
                }
                if (databaseType.equals("Cache")) {
                    driver = "com.intersys.jdbc.CacheDriver";
                    url = "jdbc:Cache://" + ip + ":" + port + "/" + dbName;
                }
                if (databaseType.equals("DB2")) {
                    driver = "com.ibm.db2.jcc.DB2Driver";
                    url = "jdbc:db2://" + ip + ":" + port + "/" + dbName;
                }
                if (databaseType.equals("DM7")) {
                    driver = "dm.jdbc.driver.DmDriver";
                    url = "jdbc:dm://" + ip + ":" + port + "/" + dbName;
                }
                if (databaseType.equals("ShenTong")) {
                    driver = "com.oscar.Driver";
                    url = "jdbc:oscar://" + ip + ":" + port + "/" + dbName;
                }
                if (databaseType.equals("Sybase")) {
                    driver = "com.sybase.jdbc4.jdbc.SybDriver";
                    url = "jdbc:sybase:Tds:" + ip + ":" + port + "/" + dbName;
                }
                if (databaseType.equals("Kingbase")) {
                    driver = "com.kingbase.Driver";
                    url = "jdbc:kingbase://" + ip + ":" + port + "/" + dbName;
                }
                if (databaseType.equals("Informix")) {
                    String[] str = config.getDatabaseName().split("@");
                    String informixDbName = str[0];
                    String informixserver = str[1];
                    driver = "com.informix.jdbc.IfxDriver";
                    url = "jdbc:informix-sqli://" + ip + ":" + port + "/" + informixDbName + ":informixserver=" + informixserver + ";NEWCODESET=utf8,8859-1,819;CLIENT_LOCALE=en_US.utf8;DB_LOCALE=en_US.8859-1;IFX_USE_STRENC=true;";
                }
                if (databaseType.equals("ClickHouse")) {
                    driver = "ru.yandex.clickhouse.ClickHouseDriver";
                    url = "jdbc:clickhouse://" + ip + ":" + port + "/" + dbName;
                }
                if (databaseType.equals("Redshift")) {
                    driver = "com.amazon.redshift.jdbc41.Driver";
                    url = "jdbc:redshift://" + ip + ":" + port + "/" + dbName;
                }
                if (databaseType.equals("ES")) {
                    driver = "org.elasticsearch.xpack.sql.jdbc.EsDriver";
                    url = "jdbc:es://http://" + ip + ":" + port;
                }
                config.setDriver(driver);
                config.setUrl(url);
                try {
                    this.configService.configUpdate(config);
                    mess = "保存成功";
                    status = "success";
                    Constants.IS_FIRST_TIME_LOGIN = false;
                } catch (Exception e) {
                    LogUtil.e("保存 数据库配置出错，", e);
                    mess = "保存数据库配置出错";
                    status = "fail";
                }
                map.put("mess", mess);
                map.put("status", status);
                return map;
            }
        }
    }

    @RequestMapping(value = {"deleteConfig"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> deleteConfig(@RequestBody IdsDto tem, HttpServletRequest request) {
        String status;
        String mess;
        boolean bl;
        Map<String, Object> map = new HashMap<>();
        String[] ids = tem.getIds();
        HttpSession session = request.getSession(true);
        String permission = (String) session.getAttribute("LOGIN_USER_PERMISSION");
        if (permission.indexOf("config") == -1) {
            Map<String, Object> map2 = new HashMap<>();
            map2.put("mess", "没有操作权限！");
            map2.put("status", "fail");
            return map2;
        } else if (StringUtils.isEmpty(tem.getIds())) {
            map.put("mess", "ids 必填！");
            map.put("status", "fail");
            return map;
        } else {
            try {
                bl = this.permissionService.isTheConfigUsed(ids);
            } catch (Exception e) {
                LogUtil.e("删除 参数配置出错，", e);
                mess = "删除 参数配置出错";
                status = "fail";
            }
            if (bl) {
                LogUtil.e("数据库配置项 数据同步中被引用,不允许删除!");
                map.put("mess", "数据库配置被引用,不允许删除!");
                map.put("status", "fail");
                return map;
            }
            this.configService.deleteConfig(ids);
            mess = "删除成功";
            status = "success";
            map.put("mess", mess);
            map.put("status", status);
            return map;
        }
    }

    @RequestMapping({"dataBaseTypeCount"})
    @ResponseBody
    public String dataBaseTypeCount(HttpServletRequest request) {
        JSONObject json = new JSONObject();
        try {
            new ArrayList();
            List<Map<String, Object>> list = this.configService.dataBaseTypeCount();
            json.put("data", list);
            json.put("mess", "查询成功");
            json.put("status", "success");
            return json.toString();
        } catch (Exception e) {
            LogUtil.e("查询数据库类型及数量出错，", e);
            json.put("mess", "查询数据库类型及数量出错");
            json.put("status", "fail");
            return json.toString();
        }
    }
}
