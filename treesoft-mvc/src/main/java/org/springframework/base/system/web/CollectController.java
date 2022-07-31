package org.springframework.base.system.web;

import com.alibaba.fastjson.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.base.system.entity.Collect;
import org.springframework.base.system.entity.IdsDto;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.service.CollectService;
import org.springframework.base.system.service.ConfigService;
import org.springframework.base.system.service.LogService;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.base.system.utils.NetworkUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping({"collect"})
@Controller
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/web/CollectController.class */
public class CollectController extends BaseController {
    @Autowired
    private ConfigService configService;
    @Autowired
    private CollectService collectService;
    @Autowired
    private LogService logService;

    @RequestMapping({"collectListPage"})
    public String collectListPage(Model model) {
        return "system/collectListPage";
    }

    @RequestMapping(value = {"collectForm"}, method = {RequestMethod.GET})
    public String collectForm(Model model) throws Exception {
        List<Map<String, Object>> configList = this.configService.getAllConfigList();
        model.addAttribute("configList", configList);
        return "system/collectForm";
    }

    @RequestMapping({"collectListSearch"})
    @ResponseBody
    public Map<String, Object> collectListSearch(String name, String sourceDatabase, String status, HttpServletRequest request) throws Exception {
        Page<Map<String, Object>> page = getPage(request);
        Map<String, Object> map2 = new HashMap<>();
        try {
            Page<Map<String, Object>> page2 = this.collectService.collectListSearch(page, name, sourceDatabase, status);
            map2.put("rows", page2.getResult());
            map2.put("total", Long.valueOf(page2.getTotalCount()));
            map2.put("columns", page2.getColumns());
            map2.put("primaryKey", page2.getPrimaryKey());
            map2.put("mess", "执行完成！");
            map2.put("status", "success");
        } catch (Exception e) {
            LogUtil.e("执行出错，", e);
            map2.put("mess", "执行出错");
            map2.put("status", "fail");
            map2.put("rows", "");
            map2.put("total", "");
        }
        return map2;
    }

    @RequestMapping({"collectSelectById/{id}"})
    public String collectSelectById(@PathVariable("id") String id, Model model) throws Exception {
        try {
            Collect temp1 = new Collect();
            temp1.setId(id);
            Collect temp2 = this.collectService.collectSelectById(temp1);
            List<Map<String, Object>> configList = this.configService.getAllConfigList();
            model.addAttribute("configList", configList);
            model.addAttribute("collect", temp2);
            return "system/collectForm";
        } catch (Exception e) {
            LogUtil.e("", e);
            return "error/error";
        }
    }

    @RequestMapping({"collectSave"})
    @ResponseBody
    public String collectSave(@RequestBody Collect collect, Model model) {
        String status;
        String mess;
        boolean isExists;
        JSONObject json = new JSONObject();
        try {
            isExists = this.collectService.checkAliasExists(collect);
        } catch (Exception e) {
            LogUtil.e("新增,修改 数据集  出错， ", e);
            mess = "新增,修改 数据集  出错";
            status = "fail";
        }
        if (isExists) {
            json.put("mess", "别名已存在，请重新输入！");
            json.put("status", "fail");
            return json.toString();
        }
        this.collectService.collectSave(collect);
        mess = "操作成功";
        status = "success";
        json.put("mess", mess);
        json.put("status", status);
        return json.toString();
    }

    @RequestMapping({"collectDelete"})
    @ResponseBody
    public String collectDelete(@RequestBody IdsDto tem, HttpServletRequest request) {
        String status;
        String mess;
        String[] ids = tem.getIds();
        JSONObject json = new JSONObject();
        try {
        } catch (Exception e) {
            LogUtil.e("删除数据集合出错，", e);
            mess = "删除数据集合出错";
            status = "fail";
        }
        if (StringUtils.isEmpty(tem.getIds())) {
            json.put("mess", "ids 必填！");
            json.put("status", "fail");
            return json.toString();
        }
        this.collectService.collectDelete(ids);
        mess = "删除成功";
        status = "success";
        HttpSession session = request.getSession(true);
        String username = (String) session.getAttribute("LOGIN_USER_NAME");
        String ip = NetworkUtil.getIpAddress(request);
        this.logService.saveLog("删除数据集合:" + ids.toString(), username, ip, "", "");
        json.put("mess", mess);
        json.put("status", status);
        return json.toString();
    }
}
