package org.springframework.base.system.web;

import com.alibaba.fastjson.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.base.system.entity.IdsDto;
import org.springframework.base.system.entity.Person;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.service.ConfigService;
import org.springframework.base.system.service.LogService;
import org.springframework.base.system.service.PermissionService;
import org.springframework.base.system.service.PersonService;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.base.system.utils.MacAddress;
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

@RequestMapping({"system/person"})
@Controller
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/web/PersonController.class */
public class PersonController extends BaseController {
    @Autowired
    private PersonService personService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private LogService logService;

    @RequestMapping(value = {"i/person"}, method = {RequestMethod.GET})
    public String person(Model model) {
        return "system/personList";
    }

    @RequestMapping(value = {"i/personList"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> personList(String username, String realname, HttpServletRequest request) throws Exception {
        Page<Map<String, Object>> page = getPage(request);
        Map<String, Object> map2 = new HashMap<>();
        HttpSession session = request.getSession(true);
        String permission = (String) session.getAttribute("LOGIN_USER_PERMISSION");
        if (permission.indexOf("person") == -1) {
            map2.put("mess", "没有操作权限！");
            map2.put("status", "fail");
            map2.put("rows", "");
            map2.put("total", "");
            return map2;
        }
        try {
            Page<Map<String, Object>> page2 = this.personService.personList(page, username, realname);
            map2.put("rows", page2.getResult());
            map2.put("total", Long.valueOf(page2.getTotalCount()));
            map2.put("columns", page2.getColumns());
            map2.put("primaryKey", page2.getPrimaryKey());
        } catch (Exception e) {
            LogUtil.e(new StringBuilder().append(e).toString());
            String mess = e.getMessage();
            map2.put("mess", mess);
            map2.put("status", "fail");
            map2.put("rows", "");
            map2.put("total", "");
        }
        return map2;
    }

    @RequestMapping(value = {"i/addPersonForm"}, method = {RequestMethod.GET})
    public String addPersonForm(Model model) throws Exception {
        List<Map<String, Object>> configList = this.configService.getAllConfigList();
        model.addAttribute("configList", configList);
        return "system/personForm";
    }

    @RequestMapping(value = {"i/editPersonForm/{id}"}, method = {RequestMethod.GET})
    public String editPersonForm(@PathVariable("id") String id, Model model) throws Exception {
        Map<String, Object> map = new HashMap<>();
        try {
            map = this.personService.getPerson(id);
        } catch (Exception e) {
        }
        List<Map<String, Object>> configList = this.configService.getAllConfigList();
        model.addAttribute("configList", configList);
        model.addAttribute("person", map);
        return "system/personForm";
    }

    @RequestMapping(value = {"i/personUpdate"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> personUpdate(@RequestBody Person person, Model model, HttpServletRequest request) {
        String status;
        String mess;
        Page<Map<String, Object>> pageResult;
        Map<String, Object> map = new HashMap<>();
        HttpSession session = request.getSession(true);
        String permission = (String) session.getAttribute("LOGIN_USER_PERMISSION");
        if (permission.indexOf("person") == -1) {
            map.put("mess", "没有操作权限！");
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
        } else if (StringUtils.isEmpty(person.getStatus())) {
            map.put("mess", "用户状态 必填！");
            map.put("status", "fail");
            return map;
        } else {
            boolean isvalidate = this.permissionService.identifying();
            try {
                Page<Map<String, Object>> page = getPage(request);
                pageResult = this.personService.personList(page, "", "");
            } catch (Exception e) {
                LogUtil.e("新增 修改用户出错，" + e);
                mess = "新增或修改用户出错";
                status = "fail";
            }
            if (!isvalidate && pageResult.getTotalCount() >= 5) {
                map.put("mess", "您好，试用版本限制配置数量，请购买商业授权！");
                map.put("status", "fail");
                return map;
            }
            boolean isAdd = StringUtils.isEmpty(person.getId());
            boolean bl = this.personService.userNameIsExists(person.getUsername().toLowerCase());
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

    @RequestMapping(value = {"i/deletePerson"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> deletePerson(@RequestBody IdsDto tem, HttpServletRequest request) {
        String status;
        String mess;
        Map<String, Object> map = new HashMap<>();
        String[] ids = tem.getIds();
        HttpSession session = request.getSession(true);
        String permission = (String) session.getAttribute("LOGIN_USER_PERMISSION");
        if (permission.indexOf("person") == -1) {
            map.put("mess", "没有操作权限！");
            map.put("status", "fail");
            return map;
        }
        try {
        } catch (Exception e) {
            LogUtil.e("删除用户出错，" + e);
            mess = "删除用户出错";
            status = "fail";
        }
        if (StringUtils.isEmpty(tem.getIds())) {
            map.put("mess", "ids 必填！");
            map.put("status", "fail");
            return map;
        }
        List<Map<String, Object>> list = this.personService.selectPersonByIds(ids);
        this.personService.deletePerson(ids);
        mess = "删除成功";
        status = "success";
        String username = (String) session.getAttribute("LOGIN_USER_NAME");
        String ip = NetworkUtil.getIpAddress(request);
        String userName = "";
        for (int i = 0; i < list.size(); i++) {
            userName = String.valueOf(userName) + list.get(i).get("username") + ",";
        }
        this.logService.saveLog("删除用户:" + userName, username, ip, "", "");
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    @RequestMapping(value = {"i/register"}, method = {RequestMethod.GET})
    public String register(HttpServletRequest request) {
        String personNumber = MacAddress.getLocalMac();
        Map<String, Object> map = this.permissionService.getRegisterMess();
        request.setAttribute("personNumber", personNumber);
        request.setAttribute("company", map.get("company"));
        request.setAttribute("token", map.get("token"));
        request.setAttribute("mess", map.get("mess"));
        return "system/register";
    }

    @RequestMapping(value = {"i/resetPersonPass"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> resetPersonPass(@RequestBody IdsDto tem, HttpServletRequest request) {
        String status;
        String mess;
        String[] ids = tem.getIds();
        try {
            this.personService.resetPersonPass(ids);
            mess = "操作成功，新密码为：123321";
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

    @RequestMapping(value = {"configListSelect/{id}"}, method = {RequestMethod.GET})
    public String configListSelect(@PathVariable("id") String id, Model model) {
        new HashMap();
        try {
            Map<String, Object> map = this.personService.getPerson(id);
            String datascope = (String) map.get("datascope");
            model.addAttribute("personId", id);
            model.addAttribute("datascope", datascope);
            return "system/configListSelect";
        } catch (Exception e) {
            LogUtil.e("打开 数据库配置页面 出错，" + e);
            return "";
        }
    }

    @RequestMapping(value = {"saveSelectDatascope"}, method = {RequestMethod.POST})
    @ResponseBody
    public String saveSelectDatascope(@RequestBody IdsDto tem, Model model) {
        String[] ids;
        JSONObject json = new JSONObject();
        try {
            Person person = new Person();
            person.setId(tem.getId());
            String datasource = "";
            if (tem.getIds().length > 0) {
                for (String idTemp : tem.getIds()) {
                    datasource = String.valueOf(datasource) + idTemp + ",";
                }
                datasource = datasource.substring(0, datasource.length() - 1);
            }
            person.setDatascope(datasource);
            this.personService.personUpdateDatascope(person);
            json.put("mess", "操作成功！");
            json.put("status", "success");
        } catch (Exception e) {
            LogUtil.e("保存用户的数据库配置 出错，" + e);
            json.put("mess", "操作出错！" + e.getMessage());
            json.put("status", "fail");
        }
        return json.toString();
    }
}
