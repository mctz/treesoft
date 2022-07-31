package org.springframework.base.system.web;

import com.alibaba.fastjson.JSONObject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.base.system.entity.IdsDto;
import org.springframework.base.system.entity.NotSqlEntity;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.service.ConfigService;
import org.springframework.base.system.service.LogService;
import org.springframework.base.system.service.RedisService;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.base.system.utils.NetworkUtil;
import org.springframework.base.system.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping({"redis"})
@Controller
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/web/RedisController.class */
public class RedisController extends BaseController {
    @Autowired
    private ConfigService configService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private LogService logService;

    @RequestMapping({"showNoSQLDBData/{NoSQLDbName}/{databaseConfigId}"})
    public String showNoSQLDBData(@PathVariable("NoSQLDbName") String NoSQLDbName, @PathVariable("databaseConfigId") String databaseConfigId, HttpServletRequest request) {
        request.setAttribute("databaseConfigId", databaseConfigId);
        request.setAttribute("NoSQLDbName", NoSQLDbName);
        return "system/showNoSQLDBData";
    }

    @RequestMapping(value = {"showNoSQLDBValue/{NoSQLDbName}/{databaseConfigId}/{selectKey}/{selectValue}"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> showNoSQLDBValue(@PathVariable("NoSQLDbName") String NoSQLDbName, @PathVariable("databaseConfigId") String databaseConfigId, @PathVariable("selectKey") String selectKey, @PathVariable("selectValue") String selectValue, HttpServletRequest request) {
        Page<Map<String, Object>> page = getPage(request);
        Map<String, Object> map = this.configService.getConfigById(databaseConfigId);
        String databaseType = (String) map.get("databaseType");
        try {
            if (databaseType.equals("Redis")) {
                page = this.redisService.getNoSQLDBForRedis(page, NoSQLDbName, databaseConfigId, selectKey, selectValue);
            }
            return getEasyUIData(page);
        } catch (Exception e) {
            LogUtil.e(e);
            return getEasyUIData(page);
        }
    }

    @RequestMapping(value = {"addOrEditNotSql/{NoSQLDbName}/{databaseConfigId}"}, method = {RequestMethod.GET})
    public String addOrEditNotSql(@PathVariable("NoSQLDbName") String NoSQLDbName, @PathVariable("databaseConfigId") String databaseConfigId, HttpServletRequest request) {
        request.setAttribute("NoSQLDbName", NoSQLDbName);
        request.setAttribute("databaseConfigId", databaseConfigId);
        return "system/addNoSQLDBData";
    }

    @RequestMapping(value = {"deleteNoSQLKeys"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> deleteNoSQLKeys(@RequestBody IdsDto tem, HttpServletRequest request) {
        String status;
        String mess;
        Map<String, Object> map = new HashMap<>();
        tem.getDatabaseName();
        String NoSQLDbName = tem.getNoSQLDbName();
        String databaseConfigId = tem.getDatabaseConfigId();
        HttpSession session = request.getSession(true);
        String username = (String) session.getAttribute("LOGIN_USER_NAME");
        String ip = NetworkUtil.getIpAddress(request);
        String[] ids = tem.getIds();
        Map<String, Object> mapConfig = this.configService.getConfigById(databaseConfigId);
        String databaseType = (String) mapConfig.get("databaseType");
        String isRead = (String) mapConfig.get("isRead");
        if (StringUtil.isNotEmpty(isRead) && isRead.equals("1")) {
            map.put("mess", "当前数据库只读，限制该操作.");
            map.put("status", "fail");
            return map;
        }
        try {
            if (databaseType.equals("Redis")) {
                this.redisService.deleteNoSQLKeyForRedis(databaseConfigId, NoSQLDbName, ids);
            }
            mess = "删除成功";
            status = "success";
            this.logService.saveLog("delete " + NoSQLDbName + ",keys: " + Arrays.toString(ids), username, ip, NoSQLDbName, databaseConfigId);
        } catch (Exception e) {
            LogUtil.e("删除失败," + e);
            mess = e.getMessage();
            status = "fail";
        }
        map.put("totalCount", 0);
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    @RequestMapping(value = {"editNotSqlData"}, method = {RequestMethod.GET})
    public String editNotSqlData(@ModelAttribute @RequestBody NotSqlEntity notSqlEntity2, Model model, HttpServletRequest request) {
        String NoSQLDbName = notSqlEntity2.getNoSQLDbName();
        String databaseConfigId = notSqlEntity2.getDatabaseConfigId();
        String key = notSqlEntity2.getKey();
        Map<String, Object> mapConfig = this.configService.getConfigById(databaseConfigId);
        String databaseType = (String) mapConfig.get("databaseType");
        String key2 = key.replaceAll("&amp;quot;", "\"").replaceAll("&amp;amp;", "&");
        NotSqlEntity notSqlEntity = new NotSqlEntity();
        try {
            if (databaseType.equals("Redis")) {
                notSqlEntity = this.redisService.selectNotSqlDataForRedis(key2, NoSQLDbName, databaseConfigId);
            }
        } catch (Exception e) {
            LogUtil.e("失败," + e.getMessage());
        }
        model.addAttribute("notSqlEntity", notSqlEntity);
        request.setAttribute("NoSQLDbName", NoSQLDbName);
        request.setAttribute("databaseConfigId", databaseConfigId);
        return "system/addNoSQLDBData";
    }

    @RequestMapping(value = {"saveNotSqlData/{NoSQLDbName}/{databaseConfigId}"}, method = {RequestMethod.POST})
    @ResponseBody
    public String saveNotSqlData(@ModelAttribute @RequestBody NotSqlEntity notSqlEntity, Model model, @PathVariable("NoSQLDbName") String NoSQLDbName, @PathVariable("databaseConfigId") String databaseConfigId, HttpServletRequest request) {
        String status;
        String mess;
        new HashMap();
        JSONObject obj = new JSONObject();
        HttpSession session = request.getSession(true);
        String username = (String) session.getAttribute("LOGIN_USER_NAME");
        String ip = NetworkUtil.getIpAddress(request);
        Map<String, Object> mapConfig = this.configService.getConfigById(databaseConfigId);
        String databaseType = (String) mapConfig.get("databaseType");
        String isRead = (String) mapConfig.get("isRead");
        if (StringUtil.isNotEmpty(isRead) && isRead.equals("1")) {
            obj.put("mess", "当前数据库只读，限制该操作.");
            obj.put("status", "fail");
            return obj.toJSONString();
        }
        try {
            boolean bl = true;
            if ("".equals(notSqlEntity.getExTime())) {
                notSqlEntity.setExTime("0");
            }
            if (databaseType.equals("Redis")) {
                bl = this.redisService.saveNotSqlDataForRedis(notSqlEntity, databaseConfigId, NoSQLDbName);
            }
            if (bl) {
                mess = "保存成功";
                status = "success";
                this.logService.saveLog("save " + notSqlEntity.toString(), username, ip, NoSQLDbName, databaseConfigId);
            } else {
                mess = "保存失败";
                status = "fail";
            }
        } catch (Exception e) {
            LogUtil.e("保存失败," + e);
            mess = e.getMessage();
            status = "fail";
        }
        obj.put("mess", mess);
        obj.put("status", status);
        return obj.toJSONString();
    }

    @RequestMapping({"monitorRedisItem/{databaseName}/{databaseConfigId}"})
    public String monitorItem(@PathVariable("databaseName") String databaseName, @PathVariable("databaseConfigId") String databaseConfigId, HttpServletRequest request) throws Exception {
        request.setAttribute("databaseName", databaseName);
        request.setAttribute("databaseConfigId", databaseConfigId);
        return "system/monitorRedisItem";
    }

    @RequestMapping(value = {"selectNoSQLDBStatus/{databaseConfigId}"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> selectNoSQLDBStatus(@PathVariable("databaseConfigId") String databaseConfigId, HttpServletRequest request) {
        String status;
        String mess;
        Page<Map<String, Object>> page = getPage(request);
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> mapConfig = this.configService.getConfigById(databaseConfigId);
        String databaseType = (String) mapConfig.get("databaseType");
        try {
            if (databaseType.equals("Redis")) {
                page = this.redisService.selectNoSQLDBStatusForRedis(page, databaseConfigId);
            }
            map.put("rows", page.getResult());
            map.put("total", Long.valueOf(page.getTotalCount()));
            map.put("columns", page.getColumns());
            map.put("primaryKey", page.getPrimaryKey());
            mess = "查询成功";
            status = "success";
        } catch (Exception e) {
            LogUtil.e("系统当前状态参数查询失败,可能参数配置有误！" + e);
            mess = e.getMessage();
            status = "fail";
        }
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    @RequestMapping(value = {"showSQLMess"}, method = {RequestMethod.GET})
    public String showSQLMess(HttpServletRequest request) {
        return "system/showSQLMess";
    }

    @RequestMapping(value = {"queryInfoItem/{databaseConfigId}"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> queryInfoItem(@PathVariable("databaseConfigId") String databaseConfigId) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> mapConfig = this.configService.getConfigById(databaseConfigId);
        String databaseType = (String) mapConfig.get("databaseType");
        try {
            if (databaseType.equals("Redis")) {
                map = this.redisService.queryInfoItemForRedis(databaseConfigId);
            }
            map.put("mess", "查询成功");
            map.put("status", "success");
            return map;
        } catch (Exception e) {
            String str = "实时监控查询出错，" + e.getMessage();
            LogUtil.e("实时监控查询出错，， " + e);
            return null;
        }
    }
}
