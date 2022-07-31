package org.springframework.base.system.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.base.system.entity.IdsDto;
import org.springframework.base.system.entity.SqlDistribute;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.service.ConfigService;
import org.springframework.base.system.service.PermissionService;
import org.springframework.base.system.service.SqlDistributeService;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping({"system/sqlDistribute"})
@Controller
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/web/SqlDistributeController.class */
public class SqlDistributeController extends BaseController {
    @Autowired
    private SqlDistributeService sqlDistributeService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private ConfigService configService;

    @RequestMapping(value = {"listPage"}, method = {RequestMethod.GET})
    public String listPage(Model model) {
        return "system/sqlDistributeList";
    }

    @RequestMapping(value = {"sqlDistributeList"}, method = {RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> taskList(HttpServletRequest request) throws Exception {
        Page<Map<String, Object>> page = getPage(request);
        try {
            page = this.sqlDistributeService.sqlDistributeList(page);
            return getEasyUIData(page);
        } catch (Exception e) {
            return getEasyUIData(page);
        }
    }

    @RequestMapping(value = {"addSqlDistributeForm"}, method = {RequestMethod.GET})
    public String addSqlDistributeForm(Model model) throws Exception {
        List<Map<String, Object>> configList = this.configService.getAllConfigList();
        model.addAttribute("configList", configList);
        return "system/sqlDistributeForm";
    }

    @RequestMapping(value = {"editSqlDistributeForm/{id}"}, method = {RequestMethod.GET})
    public String editSqlDistributeForm(@PathVariable("id") String id, Model model) throws Exception {
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> configList = this.configService.getAllConfigList();
        try {
            map = this.sqlDistributeService.getSqlDistribute(id);
            List<Map<String, Object>> list3 = this.sqlDistributeService.getSqlDistributeDatabaseConfigList(id);
            map.put("databaseConfigList", list3);
        } catch (Exception e) {
        }
        model.addAttribute("configList", configList);
        model.addAttribute("task", map);
        return "system/sqlDistributeForm";
    }

    @RequestMapping(value = {"sqlDistributeUpdate"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> sqlDistributeUpdate(@ModelAttribute @RequestBody SqlDistribute sqlDistribute) {
        String status;
        String mess;
        Map<String, Object> map = new HashMap<>();
        try {
            this.sqlDistributeService.sqlDistributeUpdate(sqlDistribute);
            mess = "操作成功";
            status = "success";
        } catch (Exception e) {
            LogUtil.e(" SQL分发 操作失败。" + e);
            mess = "error:" + e.getMessage();
            status = "fail";
        }
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    @RequestMapping(value = {"sqlDistributeDelete"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> sqlDistributeDelete(@RequestBody IdsDto tem, HttpServletRequest request) {
        String status;
        String mess;
        String[] ids = tem.getIds();
        try {
            this.sqlDistributeService.sqlDistributeDelete(ids);
            for (String sqlDistributeId : ids) {
                this.sqlDistributeService.deleteTaskLogByDS(sqlDistributeId);
            }
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

    @RequestMapping(value = {"startSqlDistribute"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> startSqlDistribute(@RequestBody IdsDto tem, HttpServletRequest request) {
        String status;
        String comments;
        String status2;
        String[] ids = tem.getIds();
        String mess = "";
        String statusFinal = "2";
        String sqlDistributeId = "";
        try {
            List<Map<String, Object>> list = this.sqlDistributeService.getSqlDistributeByIds(ids);
            this.sqlDistributeService.sqlDistributeUpdateStatus(sqlDistributeId, "1");
            Iterator<Map<String, Object>> it = list.iterator();
            while (it.hasNext()) {
                Map<String, Object> mapTemp = it.next();
                sqlDistributeId = new StringBuilder().append(mapTemp.get("id")).toString();
                String doSql = new StringBuilder().append(mapTemp.get("doSql")).toString();
                List<Map<String, Object>> databaseConfigList = this.sqlDistributeService.getSqlDistributeDatabaseConfigList(sqlDistributeId);
                Iterator<Map<String, Object>> it2 = databaseConfigList.iterator();
                while (it2.hasNext()) {
                    Map<String, Object> mapConfigTemp = it2.next();
                    String databaseConfigId = (String) mapConfigTemp.get("databaseConfigId");
                    String databaseName = (String) mapConfigTemp.get("databaseName");
                    try {
                        Map<String, Object> map = this.permissionService.executeSqlNotRes(doSql, databaseName, databaseConfigId);
                        Thread.sleep(2000L);
                        status2 = "2";
                        comments = "分发SQL=" + doSql + "，运行成功， 影响" + map.get("totalCount").toString() + "行。";
                        LogUtil.i("数据库=" + databaseName + "," + comments);
                    } catch (Exception e) {
                        status2 = "3";
                        statusFinal = "3";
                        comments = "SQL=" + doSql + "，运行异常 ，" + e;
                    }
                    this.sqlDistributeService.saveSqlDistributeLog(databaseConfigId, databaseName, status2, comments, sqlDistributeId);
                }
            }
            this.sqlDistributeService.sqlDistributeUpdateStatus(sqlDistributeId, statusFinal);
            mess = "任务已完成!";
            status = "success";
        } catch (Exception e2) {
            LogUtil.e("运行 SQL分发任务  出错!" + e2);
            this.sqlDistributeService.sqlDistributeUpdateStatus(sqlDistributeId, "3");
            status = "fail";
        }
        Map<String, Object> map2 = new HashMap<>();
        map2.put("mess", mess);
        map2.put("status", status);
        return map2;
    }

    @RequestMapping(value = {"stopSqlDistribute"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> stopSqlDistribute(@RequestBody IdsDto tem, HttpServletRequest request) {
        String status;
        String mess;
        String[] ids = tem.getIds();
        String sqlDistributeId = "";
        try {
            List<Map<String, Object>> list = this.sqlDistributeService.getSqlDistributeByIds(ids);
            Iterator<Map<String, Object>> it = list.iterator();
            while (it.hasNext()) {
                Map<String, Object> job = it.next();
                sqlDistributeId = new StringBuilder().append(job.get("id")).toString();
                this.sqlDistributeService.sqlDistributeUpdateStatus(sqlDistributeId, "0");
            }
            mess = "操作成功!";
            status = "success";
        } catch (Exception e) {
            LogUtil.e("操作任务 出错!" + e);
            this.sqlDistributeService.sqlDistributeUpdateStatus(sqlDistributeId, "0");
            mess = e.getMessage();
            status = "fail";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    @RequestMapping(value = {"sqlDistributeLogForm/{taskId}"}, method = {RequestMethod.GET})
    public String sqlDistributeLogForm(@PathVariable("taskId") String taskId, HttpServletRequest request) throws Exception {
        request.setAttribute("taskId", taskId);
        return "system/sqlDistributeLogForm";
    }

    @RequestMapping(value = {"sqlDistributeLogList/{taskId}"}, method = {RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> sqlDistributeLogList(@PathVariable("taskId") String taskId, HttpServletRequest request) throws Exception {
        Page<Map<String, Object>> page = getPage(request);
        try {
            page = this.sqlDistributeService.sqlDistributeLogList(page, taskId);
            return getEasyUIData(page);
        } catch (Exception e) {
            LogUtil.e(" sqlDistributeLog日志查询 出错!" + e);
            return getEasyUIData(page);
        }
    }

    @RequestMapping(value = {"deleteSqlDistributeLog"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> deleteSqlDistributeLog(@RequestBody IdsDto tem, HttpServletRequest request) {
        String status;
        String mess;
        String[] ids = tem.getIds();
        try {
            this.sqlDistributeService.deleteTaskLog(ids);
            mess = "删除成功";
            status = "success";
        } catch (Exception e) {
            LogUtil.e("  出错!" + e);
            mess = e.getMessage();
            status = "fail";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    @RequestMapping(value = {"copySqlDistribute"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> copySqlDistribute(@RequestBody IdsDto tem, HttpServletRequest request) {
        String status;
        String mess;
        String[] ids = tem.getIds();
        try {
            this.sqlDistributeService.copySqlDistribute(ids);
            mess = "复制成功";
            status = "success";
        } catch (Exception e) {
            LogUtil.e("复制 SqlDistribute 出错!" + e);
            mess = e.getMessage();
            status = "fail";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    @RequestMapping(value = {"getAllConfigList"}, method = {RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> getAllConfigList(HttpServletRequest request) throws Exception {
        String status;
        String mess;
        List<Map<String, Object>> configList = new ArrayList<>();
        try {
            configList = this.configService.getAllConfigList();
            mess = "查询成功";
            status = "success";
        } catch (Exception e) {
            LogUtil.e("数据库配置查询出错!" + e);
            mess = "数据库配置查询出错!" + e.getMessage();
            status = "fail";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("mess", mess);
        map.put("status", status);
        map.put("totals", Integer.valueOf(configList.size()));
        map.put("rows", configList);
        return map;
    }
}
