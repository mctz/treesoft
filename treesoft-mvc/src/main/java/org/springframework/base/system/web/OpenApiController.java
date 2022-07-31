package org.springframework.base.system.web;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.base.system.entity.Attach;
import org.springframework.base.system.entity.Config;
import org.springframework.base.system.entity.IdsDto;
import org.springframework.base.system.entity.Order;
import org.springframework.base.system.entity.Person;
import org.springframework.base.system.entity.WeaverOA;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.service.ConfigService;
import org.springframework.base.system.service.FileUploadService;
import org.springframework.base.system.service.LogService;
import org.springframework.base.system.service.OrderService;
import org.springframework.base.system.service.PermissionService;
import org.springframework.base.system.service.PersonService;
import org.springframework.base.system.utils.DateUtils;
import org.springframework.base.system.utils.ExcelUtil;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.base.system.utils.NetworkUtil;
import org.springframework.base.system.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping({"openApi"})
@Controller
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/web/OpenApiController.class */
public class OpenApiController extends BaseController {
    @Autowired
    private PersonService personService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private LogService logService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private FileUploadService fileUploadService;
    @Autowired
    private ConfigService configService;
    static String clientId = "";

    @RequestMapping(value = {"/getDateTimeString"}, method = {RequestMethod.GET})
    @ResponseBody
    public String getDateTimeString(HttpServletRequest request) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(ExcelUtil.DATE_FORMAT);
            String dateString = formatter.format(new Date());
            return dateString;
        } catch (Exception e) {
            return "";
        }
    }

    @RequestMapping(value = {"/personUpdate"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> personUpdate(@RequestBody Person person, Model model, HttpServletRequest request) throws Exception {
        String status;
        String mess;
        boolean isAdd;
        boolean bl;
        String ip = NetworkUtil.getIpAddress(request);
        LogUtil.i("调用新增 修改 用户信息接口 personUpdate，" + ip);
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isEmpty(person.getClientId())) {
            map.put("mess", "clientId 必填！");
            map.put("status", "fail");
            return map;
        } else if (!getClientId().equals(person.getClientId())) {
            map.put("mess", "clientId错误！");
            map.put("status", "fail");
            return map;
        } else if (StringUtils.isEmpty(person.getUsername())) {
            map.put("mess", "用户名 必填！");
            map.put("status", "fail");
            return map;
        } else if (StringUtils.isEmpty(person.getRealname())) {
            map.put("mess", "姓名 必填！");
            map.put("status", "fail");
            return map;
        } else if (StringUtils.isEmpty(person.getRole())) {
            map.put("mess", "用户角色 必填！");
            map.put("status", "fail");
            return map;
        } else if (StringUtils.isEmpty(person.getDatascope())) {
            map.put("mess", "数据范围  必填！");
            map.put("status", "fail");
            return map;
        } else if (StringUtils.isEmpty(person.getPermission())) {
            map.put("mess", "功能权限 必填！");
            map.put("status", "fail");
            return map;
        } else if (StringUtils.isEmpty(person.getStatus())) {
            map.put("mess", "用户状态 必填！");
            map.put("status", "fail");
            return map;
        } else {
            try {
                isAdd = StringUtils.isEmpty(person.getId());
                bl = this.personService.userNameIsExists(person.getUsername().toLowerCase());
            } catch (Exception e) {
                LogUtil.e("API新增 修改用户出错，" + e);
                mess = "API新增或修改用户出错";
                status = "fail";
            }
            if (isAdd && bl) {
                map.put("mess", "该用户名已存在！");
                map.put("status", "fail");
                return map;
            }
            this.personService.personUpdate(person);
            mess = "操作成功";
            status = "success";
            map.put("mess", mess);
            map.put("status", status);
            return map;
        }
    }

    @RequestMapping(value = {"/deletePerson"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> deletePerson(@RequestBody IdsDto tem, HttpServletRequest request) {
        String status;
        String mess;
        String ip = NetworkUtil.getIpAddress(request);
        LogUtil.i("调用删除  用户信息接口 deletePerson，" + ip);
        Map<String, Object> map = new HashMap<>();
        String[] ids = tem.getIds();
        try {
        } catch (Exception e) {
            LogUtil.e("API删除用户出错，" + e);
            mess = "API删除用户出错";
            status = "fail";
        }
        if (StringUtils.isEmpty(tem.getIds())) {
            map.put("mess", "ids 必填！");
            map.put("status", "fail");
            return map;
        } else if (StringUtils.isEmpty(tem.getClientId())) {
            map.put("mess", "clientId 必填！");
            map.put("status", "fail");
            return map;
        } else if (!getClientId().equals(tem.getClientId())) {
            map.put("mess", "clientId错误！");
            map.put("status", "fail");
            return map;
        } else {
            List<Map<String, Object>> list = this.personService.selectPersonByIds(ids);
            this.personService.deletePerson(ids);
            mess = "删除成功";
            status = "success";
            HttpSession session = request.getSession(true);
            String username = (String) session.getAttribute("LOGIN_USER_NAME");
            String userName = "";
            for (int i = 0; i < list.size(); i++) {
                userName = String.valueOf(userName) + list.get(i).get("username") + ",";
            }
            this.logService.saveLog("API删除用户:" + userName, username, ip, "", "");
            map.put("mess", mess);
            map.put("status", status);
            return map;
        }
    }

    @RequestMapping(value = {"/configUpdate"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> configUpdate(@RequestBody Config config, Model model) {
        String status;
        String mess;
        Map<String, Object> map = new HashMap<>();
        String databaseType = config.getDatabaseType();
        String ip = config.getIp();
        String port = config.getPort();
        String dbName = config.getDatabaseName();
        String url = "";
        String driver = "";
        if (StringUtils.isEmpty(config.getClientId())) {
            map.put("mess", "clientId 必填！");
            map.put("status", "fail");
            return map;
        } else if (!getClientId().equals(config.getClientId())) {
            map.put("mess", "clientId错误！");
            map.put("status", "fail");
            return map;
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
        } else if (StringUtils.isEmpty(config.getUserName())) {
            map.put("mess", "用户名 必填！");
            map.put("status", "fail");
            return map;
        } else if (StringUtils.isEmpty(config.getIsDefault())) {
            map.put("mess", "默认值 必填！");
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
            if (databaseType.equals("MySQL8.0")) {
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
            databaseType.equals("ShenTong");
            if (databaseType.equals("Sybase")) {
                driver = "com.sybase.jdbc4.jdbc.SybDriver";
                url = "jdbc:sybase:Tds:" + ip + ":" + port + "/" + dbName;
            }
            if (databaseType.equals("Kingbase")) {
                driver = "com.kingbase.Driver";
                url = "jdbc:kingbase://" + ip + ":" + port + "/" + dbName;
            }
            if (databaseType.equals("Informix")) {
                driver = "com.informix.jdbc.IfxDriver";
                url = "jdbc:informix-sqli://" + ip + ":" + port + "/" + dbName + ":informixserver=demoServer;NEWCODESET=utf8,8859-1,819;CLIENT_LOCALE=en_US.utf8;DB_LOCALE=en_US.8859-1;IFX_USE_STRENC=true;";
            }
            if (databaseType.equals("ClickHouse")) {
                driver = "ru.yandex.clickhouse.ClickHouseDriver";
                url = "jdbc:clickhouse://" + ip + ":" + port + "/" + dbName;
            }
            if (databaseType.equals("Redshift")) {
                driver = "com.amazon.redshift.jdbc41.Driver";
                url = "jdbc:redshift://" + ip + ":" + port + "/" + dbName;
            }
            config.setUrl(url);
            config.setDriver(driver);
            try {
                this.configService.configUpdate(config);
                mess = "修改成功";
                status = "success";
            } catch (Exception e) {
                LogUtil.e("API修改 参数配置出错，" + e);
                mess = "API修改参数配置出错";
                status = "fail";
            }
            map.put("mess", mess);
            map.put("status", status);
            return map;
        }
    }

    @RequestMapping(value = {"/deleteConfig"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> deleteConfig(@RequestBody IdsDto tem, HttpServletRequest request) {
        String status;
        String mess;
        boolean bl;
        String ip = NetworkUtil.getIpAddress(request);
        LogUtil.i("调用删除  数据库配置接口 deleteConfig，" + ip);
        Map<String, Object> map = new HashMap<>();
        String[] ids = tem.getIds();
        if (StringUtils.isEmpty(tem.getIds())) {
            map.put("mess", "ids 必填！");
            map.put("status", "fail");
            return map;
        } else if (StringUtils.isEmpty(tem.getClientId())) {
            map.put("mess", "clientId 必填！");
            map.put("status", "fail");
            return map;
        } else if (!getClientId().equals(tem.getClientId())) {
            map.put("mess", "clientId错误！");
            map.put("status", "fail");
            return map;
        } else {
            try {
                bl = this.permissionService.isTheConfigUsed(ids);
            } catch (Exception e) {
                LogUtil.e("API删除 参数配置出错，" + e);
                mess = "API删除 参数配置出错";
                status = "fail";
            }
            if (bl) {
                throw new Exception("数据库配置被引用,不允许删除!");
            }
            this.configService.deleteConfig(ids);
            mess = "删除成功";
            status = "success";
            map.put("mess", mess);
            map.put("status", status);
            return map;
        }
    }

    @RequestMapping(value = {"/configList"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> configList(@RequestBody IdsDto tem, HttpServletRequest request) {
        String status;
        String mess;
        String ip = NetworkUtil.getIpAddress(request);
        LogUtil.i("调用 取数据库配置  列表接口 configList，" + ip);
        Map<String, Object> map = new HashMap<>();
        if (!getClientId().equals(tem.getClientId())) {
            map.put("mess", "clientId错误！");
            map.put("status", "fail");
            return map;
        }
        try {
            List<Map<String, Object>> configList = this.configService.getAllConfigList();
            map.put("rows", configList);
            map.put("total", Integer.valueOf(configList.size()));
            mess = "查询成功";
            status = "success";
        } catch (Exception e) {
            LogUtil.e("API查询数据库配置 列表出错，" + e);
            mess = "查询数据库配置 列表出错。";
            status = "fail";
        }
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    @RequestMapping(value = {"/userList"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> userList(@RequestBody IdsDto tem, HttpServletRequest request) {
        String status;
        String mess;
        String ip = NetworkUtil.getIpAddress(request);
        LogUtil.i("调用 取 用户信息  列表接口 userList，" + ip);
        Map<String, Object> map = new HashMap<>();
        if (!getClientId().equals(tem.getClientId())) {
            map.put("mess", "clientId错误！");
            map.put("status", "fail");
            return map;
        }
        try {
            List<Map<String, Object>> configList = this.permissionService.getAllUserList();
            map.put("rows", configList);
            map.put("total", Integer.valueOf(configList.size()));
            mess = "查询成功";
            status = "success";
        } catch (Exception e) {
            LogUtil.e("API查询用户信息 列表出错，" + e);
            mess = "查询用户信息 列表出错。";
            status = "fail";
        }
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    public String getClientId() {
        try {
            Properties props = PropertiesLoaderUtils.loadProperties(new ClassPathResource("application.properties"));
            clientId = (String) props.get("client_id");
            return clientId;
        } catch (Exception e) {
            return clientId;
        }
    }

    @RequestMapping(value = {"/selectLog"}, method = {RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> selectLog(String startTime, String endTime, String userName, String log, String clientId2, HttpServletRequest request) {
        String mess;
        String ip = NetworkUtil.getIpAddress(request);
        LogUtil.i("调用取操作日志接口selectLog，" + ip);
        Page<Map<String, Object>> page = getPage(request);
        Map<String, Object> map = new HashMap<>();
        String status = "";
        if (!getClientId().equals(clientId2)) {
            map.put("mess", "clientId错误！");
            map.put("status", "fail");
            return map;
        }
        try {
            Page<Map<String, Object>> page2 = this.logService.logList(page, startTime, endTime, userName, log);
            map.put("rows", page2.getResult());
            map.put("total", Long.valueOf(page2.getTotalCount()));
            mess = "执行完成！";
            status = "success";
        } catch (Exception e) {
            mess = e.getMessage();
            map.put("mess", mess);
            map.put("status", "fail");
        }
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    @RequestMapping(value = {"/weaverOAOrderSubmit"}, method = {RequestMethod.POST})
    @ResponseBody
    public String weaverOAOrderSubmit(@ModelAttribute WeaverOA weaverOA, HttpServletRequest request) {
        String ip = NetworkUtil.getIpAddress(request);
        LogUtil.i("接收泛微OA提交数据库工单 ，" + ip + " , 附件数量：" + weaverOA.getAttachments().size());
        Map<String, Object> configMap = this.configService.getConfigBySort(weaverOA.getITdatabase());
        if (configMap.size() <= 0) {
            LogUtil.e("接收泛微OA提交数据库工单 ， ITdatabase =" + weaverOA.getITdatabase() + " , TreeDMS查询不到对应的数据库，请确认数据库配置中的排序号是否正确设置");
            return "查询不到对应的数据库，请确认TreeDMS数据库配置中的排序号是否正确设置.";
        }
        try {
            Order order = new Order();
            String bidTemp = StringUtil.getUUID();
            order.setId(bidTemp);
            order.setCreateUserId("");
            order.setCreateUserName(weaverOA.getLastname());
            order.setDoSql("");
            order.setStatus("4");
            order.setLevel("0");
            order.setOrderType("0");
            order.setRemark("默认审批通过.");
            order.setAuditUserName("管理员");
            order.setAuditTime(DateUtils.getDateTime());
            order.setOrderName("SQL工单");
            order.setComments(String.valueOf(weaverOA.getITreason()) + "，单号:" + weaverOA.getRequestId());
            if (configMap.size() > 0) {
                order.setConfigId(configMap.get("id").toString());
                order.setConfigName(configMap.get("name").toString());
                order.setDatabaseName(configMap.get("databaseName").toString());
            }
            this.orderService.orderSave(order);
            String fileUploadPath = String.valueOf(request.getSession().getServletContext().getRealPath("/")) + "upload";
            Properties props = PropertiesLoaderUtils.loadProperties(new ClassPathResource("application.properties"));
            String pathStr = (String) props.get("uploadFilePath");
            if (!StringUtils.isEmpty(pathStr)) {
                fileUploadPath = pathStr;
            }
            File filePath = new File(fileUploadPath);
            if (!filePath.exists()) {
                filePath.mkdirs();
            }
            List<Attach> attachList = new ArrayList<>();
            if (weaverOA.getAttachments() != null) {
                for (int i = 0; i < weaverOA.getAttachments().size(); i++) {
                    MultipartFile multipartFile = weaverOA.getAttachments().get(i);
                    String fileNameOld = multipartFile.getOriginalFilename();
                    Random r = new Random();
                    String uuid = "attach_" + r.nextInt(100000000);
                    String fileNameNew = String.valueOf(uuid) + "_@@_" + fileNameOld;
                    File fileNew = new File(String.valueOf(fileUploadPath) + File.separator + fileNameNew);
                    multipartFile.transferTo(fileNew);
                    String tempId = StringUtil.getUUID();
                    Attach attach = new Attach();
                    attach.setId(tempId);
                    attach.setBid(bidTemp);
                    attach.setFileName(fileNameOld);
                    attach.setFileExt(fileNameOld.substring(fileNameOld.lastIndexOf(".")));
                    attach.setFilePath(String.valueOf(fileUploadPath) + File.separator + fileNameNew);
                    attach.setStatus(0);
                    attach.setSort(0);
                    attach.setFileSize(Long.valueOf(multipartFile.getSize()));
                    attach.setCreateUser(weaverOA.getLastname());
                    attach.setRemark("");
                    attach.setFileMd5("");
                    attach.setFileType("");
                    attach.setFileTag(fileNameNew);
                    this.fileUploadService.saveAttach(attach);
                    attachList.add(attach);
                }
            }
            this.orderService.orderRun(bidTemp, "", weaverOA.getLastname(), ip, attachList);
            return "执行成功";
        } catch (Exception e) {
            LogUtil.e("执行失败 ， 接收泛微OA提交数据库工单 ， 附件数量：" + weaverOA.getAttachments().size() + " ，" + e);
            String mess = "执行失败， " + e.getMessage();
            if (mess.length() > 150) {
                mess = String.valueOf(mess.substring(0, 150)) + "......";
            }
            return mess;
        }
    }
}
