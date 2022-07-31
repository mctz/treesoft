package org.springframework.base.system.web;

import com.alibaba.druid.util.StringUtils;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.base.system.entity.NewQueryDTO;
import org.springframework.base.system.service.ConfigService;
import org.springframework.base.system.service.ESService;
import org.springframework.base.system.service.LogService;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.base.system.utils.NetworkUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping({"newQuery"})
@Controller
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/web/NewQueryController.class */
public class NewQueryController {
    @Autowired
    private ConfigService configService;
    @Autowired
    private ESService esService;
    @Autowired
    private LogService logService;

    @RequestMapping({"index/{databaseName}/{databaseConfigId}"})
    public String index(@PathVariable("databaseName") String databaseName, @PathVariable("databaseConfigId") String databaseConfigId, HttpServletRequest request, Model model) {
        model.addAttribute("databaseName", databaseName);
        model.addAttribute("databaseConfigId", databaseConfigId);
        Map<String, Object> map0 = this.configService.getConfigById(databaseConfigId);
        String exportLimit = (String) map0.get("exportLimit");
        String isRead = (String) map0.get("isRead");
        model.addAttribute("databaseConfigName", (String) map0.get("name"));
        model.addAttribute("databaseType", (String) map0.get("databaseType"));
        model.addAttribute("exportLimit", exportLimit);
        model.addAttribute("isRead", isRead);
        String databaseType = (String) map0.get("databaseType");
        if (databaseType.equals("ES")) {
            return "system/elasticSearchQueryForm";
        }
        return "system/newQueryForm";
    }

    @RequestMapping(value = {"queryElasticSearch"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> queryElasticSearch(@RequestBody NewQueryDTO dto, HttpServletRequest request) {
        String status;
        String mess;
        Map<String, Object> map = new HashMap<>();
        HttpSession session = request.getSession(true);
        String username = (String) session.getAttribute("LOGIN_USER_NAME");
        String ip = NetworkUtil.getIpAddress(request);
        if (StringUtils.isEmpty(dto.getRequestURL())) {
            map.put("mess", "URL地址必填！");
            map.put("status", "fail");
            return map;
        }
        try {
            map = this.esService.queryElasticSearch(dto);
            this.logService.saveLog(String.valueOf(dto.getRequestURL()) + " " + dto.getRequestBody(), username, ip, dto.getDatabaseName(), dto.getDatabaseConfigId());
            mess = "操作成功";
            status = "success";
        } catch (Exception e) {
            LogUtil.e("ElasticSearch DSL 操作出错!", e);
            mess = "操作出错" + e.getMessage();
            status = "fail";
        }
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }
}
