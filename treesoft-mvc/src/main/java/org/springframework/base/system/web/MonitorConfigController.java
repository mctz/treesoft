package org.springframework.base.system.web;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.base.system.entity.MonitorConfig;
import org.springframework.base.system.service.MonitorConfigService;
import org.springframework.base.system.utils.DateUtils;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.base.system.utils.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping({"monitorConfig"})
@Controller
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/web/MonitorConfigController.class */
public class MonitorConfigController {
    @Autowired
    private MonitorConfigService monitorConfigService;

    @RequestMapping(value = {"monitorConfigForm"}, method = {RequestMethod.GET})
    public String monitorConfigForm(HttpServletRequest request, Model model) {
        String id = request.getParameter("id");
        Map<String, Object> map = new HashMap<>();
        try {
            map = this.monitorConfigService.selectMonitorConfigById(id);
        } catch (Exception e) {
            LogUtil.e("打开数据同步预警设置  页面 出错，" + e);
        }
        model.addAttribute("monitorConfig", map);
        return "system/monitorConfigForm";
    }

    @RequestMapping(value = {"monitorConfigTaskForm"}, method = {RequestMethod.GET})
    public String monitorConfigTaskForm(HttpServletRequest request, Model model) {
        String id = request.getParameter("id");
        Map<String, Object> map = new HashMap<>();
        try {
            map = this.monitorConfigService.selectMonitorConfigById(id);
        } catch (Exception e) {
            LogUtil.e("打开定时任务预警设置  页面 出错，" + e);
        }
        model.addAttribute("monitorConfig", map);
        return "system/monitorConfigTaskForm";
    }

    @RequestMapping(value = {"monitorConfigSave"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> monitorConfigSave(@ModelAttribute @RequestBody MonitorConfig monitorConfig) {
        String status;
        String mess;
        Map<String, Object> map = new HashMap<>();
        try {
            this.monitorConfigService.monitorConfigSave(monitorConfig);
            mess = "保存成功";
            status = "success";
        } catch (Exception e) {
            LogUtil.e("新增,修改 预警设置出错，" + e);
            mess = e.getMessage();
            status = "fail";
        }
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    @RequestMapping(value = {"mailConfigTest"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> mailConfigTest(@RequestBody MonitorConfig monitorConfig) {
        Map<String, Object> map = new HashMap<>();
        String content = monitorConfig.getContent();
        monitorConfig.setContent(content.replace("{time}", DateUtils.getDateTime()).replace("{type}", "邮件测试").replace("{level}", "正常").replace("{message}", "测试邮件发送!"));
        Map<String, Object> res = MailUtil.mailSend(monitorConfig);
        map.put("mess", res.get("mess"));
        map.put("status", res.get("status"));
        return map;
    }

    @RequestMapping(value = {"shellConfigTest"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> shellConfigTest(@RequestBody MonitorConfig monitorConfig) {
        String status;
        String mess;
        Map<String, Object> map = new HashMap<>();
        try {
            this.monitorConfigService.shellConfigRun(monitorConfig);
            mess = "运行成功！";
            status = "success";
        } catch (Exception e) {
            mess = "运行测试失败！";
            status = "fail";
            LogUtil.e("运行shell测试失败，" + e);
        }
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    @RequestMapping(value = {"smsConfigTest"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> smsConfigTest(@RequestBody MonitorConfig monitorConfig) {
        Map<String, Object> map = new HashMap<>();
        try {
            this.monitorConfigService.smsConfigRun(monitorConfig);
        } catch (Exception e) {
            LogUtil.e("短信发送失败，" + e);
        }
        map.put("mess", "短信发送未实现！");
        map.put("status", "fail");
        return map;
    }

    @RequestMapping(value = {"dingdingConfigTest"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> dingdingConfigTest(@RequestBody MonitorConfig monitorConfig) {
        String status;
        String mess;
        Map<String, Object> map = new HashMap<>();
        try {
        } catch (Exception e) {
            mess = "钉钉测试失败！";
            status = "fail";
            LogUtil.e("钉钉 测试失败，" + e);
        }
        if (StringUtils.isEmpty(monitorConfig.getHookUrl())) {
            map.put("mess", "请填写hook地址");
            map.put("status", "fail");
            return map;
        }
        String content = monitorConfig.getContent();
        monitorConfig.setContent(content.replace("{time}", DateUtils.getDateTime()).replace("{type}", "钉钉测试").replace("{level}", "正常").replace("{message}", "钉钉测试消息!"));
        this.monitorConfigService.dingdingConfigRun(monitorConfig);
        mess = "发送成功！";
        status = "success";
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    @RequestMapping(value = {"wechatConfigTest"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> wechatConfigTest(@RequestBody MonitorConfig monitorConfig) {
        return null;
    }
}
