package org.springframework.base.system.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.base.system.entity.IdsDto;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.service.ConfigService;
import org.springframework.base.system.service.MonitorGlobalService;
import org.springframework.base.system.utils.Constants;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping({"monitorGlobal"})
@Controller
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/web/MonitorGlobalController.class */
public class MonitorGlobalController extends BaseController {
    @Autowired
    private ConfigService configService;
    @Autowired
    private MonitorGlobalService monitorGlobalService;
    private static IdsDto idsDto;

    @RequestMapping({"monitorGlobalPage"})
    public String task(Model model) {
        return "system/monitorGlobalPage";
    }

    @RequestMapping({"selectDBAlarm"})
    @ResponseBody
    public Map<String, Object> selectDataBaseAlarm(Model model, HttpServletRequest request) {
        Page<Map<String, Object>> page = getPage(request);
        try {
            Page<Map<String, Object>> pageList = this.monitorGlobalService.selectDataBaseAlarm(page);
            return getEasyUIData(pageList);
        } catch (Exception e) {
            LogUtil.e("查询数据库安全运维告警出错，", e);
            page.setTotalCount(0L);
            page.setResult(null);
            return getEasyUIData(page);
        }
    }

    @RequestMapping({"selectTop5Amount"})
    @ResponseBody
    public Map<String, Object> selectTopAmount(Model model, HttpServletRequest request) {
        Page<Map<String, Object>> page = getPage(request);
        try {
            List<Map<String, Object>> list = this.configService.getDBTotals();
            page.setTotalCount(list.size());
            page.setResult(list);
            return getEasyUIData(page);
        } catch (Exception e) {
            LogUtil.e("查询数据库 数据量出错，", e);
            page.setTotalCount(0L);
            page.setResult(null);
            return getEasyUIData(page);
        }
    }

    /* JADX WARN: Type inference failed for: r0v15, types: [org.springframework.base.system.web.MonitorGlobalController$1] */
    @RequestMapping({"countDBDataTotals"})
    @ResponseBody
    public Map<String, Object> countDBDataTotals(@RequestBody IdsDto dto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        HttpSession session = request.getSession(true);
        String userRole = (String) session.getAttribute("LOGIN_USER_ROLE");
        if (StringUtils.isNotEmpty(userRole) && !userRole.equals(Constants.userRole.ADMINISTRATOR.type)) {
            map.put("mess", "仅限管理员操作此功能！");
            map.put("status", "fail");
            return map;
        }
        idsDto = dto;
        try {
            new Thread() { // from class: org.springframework.base.system.web.MonitorGlobalController.1
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    try {
                        MonitorGlobalController.this.configService.countDBDataTotals(MonitorGlobalController.idsDto);
                    } catch (Exception e) {
                        LogUtil.e("进行数据库数据量的计算(异步执行)出错，", e);
                    }
                }
            }.start();
            map.put("mess", "统计任务已提交，耗时较长，请稍后刷新页面查看！");
            map.put("status", "success");
            return map;
        } catch (Exception e) {
            LogUtil.e("进行数据库数据量的计算(异步执行)出错，", e);
            map.put("mess", "操作失败");
            map.put("status", "fail");
            return map;
        }
    }

    /* JADX WARN: Type inference failed for: r0v7, types: [org.springframework.base.system.web.MonitorGlobalController$2] */
    @RequestMapping({"monitortDataBase"})
    @ResponseBody
    public Map<String, Object> monitortDataBase(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        try {
            new Thread() { // from class: org.springframework.base.system.web.MonitorGlobalController.2
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    try {
                        MonitorGlobalController.this.monitorGlobalService.monitortDataBase();
                    } catch (Exception e) {
                        LogUtil.e("进行 数据库 状态监测出错，", e);
                    }
                }
            }.start();
            map.put("mess", "数据库状态监测！");
            map.put("status", "fail");
            return map;
        } catch (Exception e) {
            LogUtil.e("进行 数据库 状态监测出错，", e);
            map.put("mess", "操作失败");
            map.put("status", "fail");
            return map;
        }
    }
}
