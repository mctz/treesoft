package org.springframework.base.system.web;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.base.system.entity.DataSynchronize;
import org.springframework.base.system.entity.IdsDto;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.scheduler.QuartzJobFactory;
import org.springframework.base.system.scheduler.QuartzManager;
import org.springframework.base.system.service.ConfigService;
import org.springframework.base.system.service.DataSynchronizeService;
import org.springframework.base.system.service.MSSQLService;
import org.springframework.base.system.service.MysqlService;
import org.springframework.base.system.service.OracleService;
import org.springframework.base.system.service.PermissionService;
import org.springframework.base.system.service.SQLParserService;
import org.springframework.base.system.utils.DateUtils;
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

@RequestMapping({"dataSynchronize"})
@Controller
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/web/DataSynchronizeController.class */
public class DataSynchronizeController extends BaseController {
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private DataSynchronizeService dataSynchronizeService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private OracleService oracleService;
    @Autowired
    private MSSQLService mssqlService;
    @Autowired
    private MysqlService mysqlService;
    @Autowired
    private QuartzManager quartzManager;
    @Autowired
    private SQLParserService sqlParserService;

    @RequestMapping({"index"})
    public String dataSynchronize(Model model) {
        return "system/dataSynchronizeList";
    }

    @RequestMapping({"dataSynchronizeList"})
    @ResponseBody
    public Map<String, Object> dataSynchronizeList(HttpServletRequest request) throws Exception {
        Page<Map<String, Object>> page = getPage(request);
        HttpSession session = request.getSession(true);
        String permission = (String) session.getAttribute("LOGIN_USER_PERMISSION");
        if (permission.indexOf("synchronize") == -1) {
            Map<String, Object> map2 = new HashMap<>();
            map2.put("mess", "没有操作权限！");
            map2.put("status", "fail");
            map2.put("rows", "");
            map2.put("total", "");
            return map2;
        }
        try {
            page = this.dataSynchronizeService.dataSynchronizeList(page);
            return getEasyUIData(page);
        } catch (Exception e) {
            return getEasyUIData(page);
        }
    }

    @RequestMapping({"addDataSynchronizeForm"})
    public String addDataSynchronizeForm(Model model) throws Exception {
        List<Map<String, Object>> configList = this.configService.getAllConfigList();
        model.addAttribute("configList", configList);
        return "system/dataSynchronizeForm";
    }

    @RequestMapping({"editDataSynchronizeForm/{id}"})
    public String editDataSynchronizeForm(@PathVariable("id") String id, Model model) {
        new HashMap();
        try {
            List<Map<String, Object>> configList = this.configService.getAllConfigList();
            Map<String, Object> map = this.dataSynchronizeService.getDataSynchronizeById(id);
            model.addAttribute("configList", configList);
            model.addAttribute("dataSynchronize", map);
            return "system/dataSynchronizeForm";
        } catch (Exception e) {
            return "error/error";
        }
    }

    @RequestMapping(value = {"dataSynchronizeUpdate"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> dataSynchronizeUpdate(@RequestBody DataSynchronize dataSynchronize, Model model, HttpServletRequest request) {
        String status;
        String mess;
        String dataSynchronizeId;
        String status2;
        String cron;
        String state;
        String doSQL;
        String sourceTableName;
        Map<String, Object> map = new HashMap<>();
        int num = this.dataSynchronizeService.selectDataSynchronizeNumber();
        if (num > 2 && versionType.equals("01")) {
            map.put("mess", "您好，试用版本限制配置数量！");
            map.put("status", "fail");
            return map;
        }
        boolean isvalidate = this.permissionService.identifying();
        if (num > 2 && versionType.equals("02") && !isvalidate) {
            map.put("mess", "您好，未注册版本限制配置数量！");
            map.put("status", "fail");
            return map;
        }
        try {
            dataSynchronizeId = dataSynchronize.getId();
            status2 = dataSynchronize.getStatus();
            cron = dataSynchronize.getCron();
            state = dataSynchronize.getState();
            if (state.equals("1")) {
                dataSynchronize.setStatus("0");
            }
            if (StringUtils.isEmpty(dataSynchronize.getToplimit())) {
                dataSynchronize.setToplimit("20000");
            }
            Map<String, Object> configMap = this.configService.getConfigById(dataSynchronize.getSourceConfigId());
            String sourceDatabaseType = (String) configMap.get("databaseType");
            doSQL = dataSynchronize.getDoSql();
            sourceTableName = this.sqlParserService.parserTableNames(doSQL, sourceDatabaseType);
            dataSynchronize.setSourceTable(sourceTableName);
        } catch (Exception e) {
            LogUtil.e("保存 数据同步出错，", e);
            mess = "保存 数据同步出错";
            status = "fail";
        }
        if (StringUtils.isEmpty(sourceTableName) && dataSynchronize.getIncrement().equals("1")) {
            LogUtil.e("查询SQL不是单张表，此时设置为自增主键是有问题的，不允许这样设置。sql=" + doSQL);
            map.put("mess", "自增主键设置有误，请检查");
            map.put("status", "fail");
            return map;
        } else if (!StringUtils.isEmpty(dataSynchronize.getQualification()) && dataSynchronize.getIncrement().equals("1") && dataSynchronize.getQualification().split(",").length > 1) {
            LogUtil.e("设置为自增主键时，只允许填写一个主键字段。");
            map.put("mess", "仅允许设置一个自增主键，请检查");
            map.put("status", "fail");
            return map;
        } else {
            dataSynchronize.setUpdateTime(DateUtils.getDateTime());
            this.dataSynchronizeService.dataSynchronizeUpdate(dataSynchronize);
            if (!dataSynchronizeId.equals("")) {
                if (status2.equals("1")) {
                    Map<String, Object> job = this.dataSynchronizeService.getDataSynchronizeById(dataSynchronizeId);
                    QuartzManager.removeJob(dataSynchronizeId);
                    this.quartzManager.addJob(dataSynchronizeId, QuartzJobFactory.class, cron, job);
                }
                if (state.equals("1")) {
                    QuartzManager.removeJob(dataSynchronizeId);
                }
            }
            mess = "保存成功";
            status = "success";
            map.put("mess", mess);
            map.put("status", status);
            return map;
        }
    }

    @RequestMapping(value = {"deleteDataSynchronize"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> deleteDataSynchronize(@RequestBody IdsDto tem, HttpServletRequest request) {
        String status;
        String mess;
        String[] ids = tem.getIds();
        try {
            this.dataSynchronizeService.deleteDataSynchronize(ids);
            for (String dataSynchronizeId : ids) {
                QuartzManager.removeJob(dataSynchronizeId);
                this.dataSynchronizeService.deleteDataSynchronizeLogByDS(dataSynchronizeId);
            }
            mess = "删除成功";
            status = "success";
        } catch (Exception e) {
            LogUtil.e("删除 数据交换出错，", e);
            mess = "删除 数据交换出错";
            status = "fail";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    @RequestMapping(value = {"copyDataSynchronize"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> copyDataSynchronize(@RequestBody IdsDto tem, HttpServletRequest request) {
        String status;
        String mess;
        String[] ids = tem.getIds();
        try {
            this.dataSynchronizeService.copyDataSynchronize(ids);
            mess = "复制成功";
            status = "success";
        } catch (Exception e) {
            mess = e.getMessage();
            status = "fail";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    @RequestMapping(value = {"startDataTask"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> startDataTask(@RequestBody IdsDto tem, HttpServletRequest request) {
        String status;
        String mess;
        String[] ids = tem.getIds();
        String dataSynchronizeId = "";
        try {
            List<Map<String, Object>> list = this.dataSynchronizeService.getDataSynchronizeListById(ids);
            Iterator<Map<String, Object>> it = list.iterator();
            while (it.hasNext()) {
                Map<String, Object> jobMessageMap = it.next();
                dataSynchronizeId = new StringBuilder().append(jobMessageMap.get("id")).toString();
                String state = new StringBuilder().append(jobMessageMap.get("state")).toString();
                if (state.equals("1")) {
                    throw new Exception("启用状态的任务才能运行！");
                }
                QuartzManager.removeJob(dataSynchronizeId);
                this.quartzManager.addJob(dataSynchronizeId, QuartzJobFactory.class, new StringBuilder().append(jobMessageMap.get("cron")).toString(), jobMessageMap);
                this.dataSynchronizeService.dataSynchronizeUpdateStatus(dataSynchronizeId, "1");
            }
            mess = "任务已提交!";
            status = "success";
        } catch (Exception e) {
            LogUtil.e("启动 数据交换任务  出错!", e);
            this.dataSynchronizeService.dataSynchronizeUpdateStatus(dataSynchronizeId, "0");
            if (e.getMessage().indexOf("one already exists") > 0) {
                mess = "任务正运行中！";
            } else {
                mess = e.getMessage();
            }
            status = "fail";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    @RequestMapping(value = {"startDataTaskOne"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> startDataTaskOne(@RequestBody IdsDto tem, HttpServletRequest request) {
        String status;
        String mess;
        String[] ids = tem.getIds();
        String dataSynchronizeId = "";
        try {
            List<Map<String, Object>> list = this.dataSynchronizeService.getDataSynchronizeListById(ids);
            Iterator<Map<String, Object>> it = list.iterator();
            while (it.hasNext()) {
                Map<String, Object> jobMessageMap = it.next();
                dataSynchronizeId = new StringBuilder().append(jobMessageMap.get("id")).toString();
                String sourceConfigId = new StringBuilder().append(jobMessageMap.get("sourceConfigId")).toString();
                String targetConfigId = new StringBuilder().append(jobMessageMap.get("targetConfigId")).toString();
                Map<String, Object> sourceMap = this.configService.getConfigById(sourceConfigId);
                Map<String, Object> targetMap = this.configService.getConfigById(targetConfigId);
                String sourceDbType = (String) sourceMap.get("databaseType");
                String targetDbType = (String) targetMap.get("databaseType");
                try {
                    if (targetDbType.equals("MySQL") || targetDbType.equals("TiDB")) {
                        this.mysqlService.judgeTableExistsForMySQL(jobMessageMap, sourceDbType, targetDbType);
                    } else if (targetDbType.equals("MariaDB")) {
                        this.mysqlService.judgeTableExistsForMySQL(jobMessageMap, sourceDbType, targetDbType);
                    } else if (targetDbType.equals("MySql8.0")) {
                        this.mysqlService.judgeTableExistsForMySQL(jobMessageMap, sourceDbType, targetDbType);
                    } else if (targetDbType.equals("Oracle")) {
                        this.oracleService.judgeTableExistsForOracle(jobMessageMap, sourceDbType, targetDbType);
                    } else if (!targetDbType.equals("HANA2") && !targetDbType.equals("PostgreSQL")) {
                        if (targetDbType.equals("SQL Server")) {
                            this.mssqlService.judgeTableExistsForMSSQL(jobMessageMap, sourceDbType, targetDbType);
                        } else if (!targetDbType.equals("MongoDB") && !targetDbType.equals("Cache") && !targetDbType.equals("DB2") && !targetDbType.equals("DM7") && !targetDbType.equals("ShenTong") && !targetDbType.equals("Sybase") && !targetDbType.equals("Kingbase") && !targetDbType.equals("Informix")) {
                            if (targetDbType.equals("ClickHouse")) {
                                this.mysqlService.judgeTableExistsForMySQL(jobMessageMap, sourceDbType, targetDbType);
                            } else {
                                targetDbType.equals("Redshift");
                            }
                        }
                    }
                } catch (Exception e) {
                    LogUtil.e("启动 运行一次 数据交换任务  判断目标表是否存在出错! sourceDbType=" + sourceDbType + "，", e);
                }
                Thread.sleep(500L);
                this.quartzManager.addJobOne(dataSynchronizeId, QuartzJobFactory.class, jobMessageMap);
                this.dataSynchronizeService.dataSynchronizeUpdateStatus(dataSynchronizeId, "1");
            }
            mess = "任务已提交，请稍后刷新查看!";
            status = "success";
        } catch (Exception e2) {
            this.dataSynchronizeService.dataSynchronizeUpdateStatus(dataSynchronizeId, "0");
            LogUtil.e("启动 运行一次 数据交换任务  出错!", e2);
            if (e2.getMessage().indexOf("one already exists") > 0) {
                mess = "任务正运行中！";
            } else {
                mess = e2.getMessage();
            }
            status = "fail";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    @RequestMapping(value = {"stopDataTask"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> stopDataTask(@RequestBody IdsDto tem, HttpServletRequest request) {
        String status;
        String mess;
        String[] ids = tem.getIds();
        String dataSynchronizeId = "";
        try {
            List<Map<String, Object>> list = this.dataSynchronizeService.getDataSynchronizeListById(ids);
            Iterator<Map<String, Object>> it = list.iterator();
            while (it.hasNext()) {
                Map<String, Object> job = it.next();
                dataSynchronizeId = new StringBuilder().append(job.get("id")).toString();
                QuartzManager.removeJob(dataSynchronizeId);
                this.dataSynchronizeService.dataSynchronizeUpdateStatus(dataSynchronizeId, "0");
            }
            mess = "操作成功!";
            status = "success";
        } catch (Exception e) {
            LogUtil.e("停止 数据交换任务  出错!", e);
            this.dataSynchronizeService.dataSynchronizeUpdateStatus(dataSynchronizeId, "0");
            mess = e.getMessage();
            status = "fail";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    @RequestMapping({"dataSynchronizeLogForm/{dataSynchronizeId}"})
    public String dataSynchronizeLogForm(@PathVariable("dataSynchronizeId") String dataSynchronizeId, HttpServletRequest request) {
        request.setAttribute("dataSynchronizeId", dataSynchronizeId);
        return "system/dataSynchronizeLogForm";
    }

    @RequestMapping({"dataSynchronizeLogList/{dataSynchronizeId}"})
    @ResponseBody
    public Map<String, Object> dataSynchronizeLogList(@PathVariable("dataSynchronizeId") String dataSynchronizeId, HttpServletRequest request) {
        Page<Map<String, Object>> page = getPage(request);
        try {
            page = this.dataSynchronizeService.dataSynchronizeLogList(page, dataSynchronizeId);
            return getEasyUIData(page);
        } catch (Exception e) {
            return getEasyUIData(page);
        }
    }

    @RequestMapping(value = {"deleteDataSynchronizeLog"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> deleteDataSynchronizeLog(@RequestBody IdsDto tem, HttpServletRequest request) {
        String status;
        String mess;
        String[] ids = tem.getIds();
        try {
            this.dataSynchronizeService.deleteDataSynchronizeLog(ids);
            mess = "删除成功";
            status = "success";
        } catch (Exception e) {
            mess = e.getMessage();
            status = "fail";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    @RequestMapping({"cleanSynchronizeLog"})
    @ResponseBody
    public Map<String, Object> cleanSynchronizeLog(@RequestBody DataSynchronize dataSynchronize, HttpServletRequest request) {
        String status;
        String mess;
        Map<String, Object> map = new HashMap<>();
        try {
            this.dataSynchronizeService.cleanSynchronizeLog(dataSynchronize.getId());
            mess = "清空成功";
            status = "success";
        } catch (Exception e) {
            LogUtil.e("数据同步日志清空失败", e);
            mess = "执行清空 失败！";
            status = "fail";
        }
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    @RequestMapping({"batchStartSynchronize"})
    @ResponseBody
    public Map<String, Object> batchStartSynchronize(@RequestBody IdsDto tem, HttpServletRequest request) {
        String status;
        String mess;
        Map<String, Object> map = new HashMap<>();
        try {
            String[] ids = tem.getIds();
            this.dataSynchronizeService.batchStartSynchronize(ids);
            mess = "执行成功";
            status = "success";
        } catch (Exception e) {
            LogUtil.e("批量启用 数据同步失败", e);
            mess = "执行失败！";
            status = "fail";
        }
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    @RequestMapping({"batchBlockSynchronize"})
    @ResponseBody
    public Map<String, Object> batchBlockSynchronize(@RequestBody IdsDto tem, HttpServletRequest request) {
        String status;
        String mess;
        Map<String, Object> map = new HashMap<>();
        try {
            String[] ids = tem.getIds();
            this.dataSynchronizeService.batchBlockSynchronize(ids);
            mess = "执行成功";
            status = "success";
        } catch (Exception e) {
            LogUtil.e("批量停用 数据同步失败", e);
            mess = "执行失败！";
            status = "fail";
        }
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }
}
