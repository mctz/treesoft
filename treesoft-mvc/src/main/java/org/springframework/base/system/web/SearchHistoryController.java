package org.springframework.base.system.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.base.system.entity.TempDto;
import org.springframework.base.system.service.PermissionService;
import org.springframework.base.system.service.SearchHistoryService;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping({"searchHistory"})
@Controller
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/web/SearchHistoryController.class */
public class SearchHistoryController {
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private SearchHistoryService searchHistoryService;

    @RequestMapping({"form"})
    public String searchHistory(Model model) {
        return "system/searchHistory";
    }

    @RequestMapping({"selectSearchHistory"})
    @ResponseBody
    public List<Map<String, Object>> selectSearchHistory() {
        List<Map<String, Object>> list = this.searchHistoryService.selectSearchHistory();
        List<Map<String, Object>> list2 = new ArrayList<>();
        Iterator<Map<String, Object>> it = list.iterator();
        while (it.hasNext()) {
            Map<String, Object> map = it.next();
            String tempName = (String) map.get("name");
            map.put("name", tempName);
            map.put("pid", "0");
            map.put("icon", "icon-hamburg-zoom");
            list2.add(map);
        }
        return list2;
    }

    @RequestMapping({"saveSearchHistory"})
    @ResponseBody
    public Map<String, Object> saveSearchHistory(@RequestBody TempDto tem, HttpServletRequest request) {
        boolean bool;
        String status;
        String mess;
        HttpSession session = request.getSession(true);
        String userId = (String) session.getAttribute("LOGIN_USER_ID");
        String id = tem.getId();
        String sql3 = tem.getSql();
        String dbName = tem.getDbName();
        String name = tem.getName();
        String configId = tem.getConfigId();
        String sql32 = sql3.replaceAll("'", "''");
        if (id == null || "".equals(id)) {
            bool = this.searchHistoryService.saveSearchHistory(name, sql32, dbName, userId, configId);
        } else {
            bool = this.searchHistoryService.updateSearchHistory(id, name, sql32, dbName, configId);
        }
        if (bool) {
            mess = "操作成功，请在右侧工具栏查看.";
            status = "success";
        } else {
            LogUtil.e("SQL保存失败");
            mess = "保存失败";
            status = "fail";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    @RequestMapping({"deleteSearchHistory"})
    @ResponseBody
    public Map<String, Object> deleteSearchHistory(@RequestBody TempDto tem, HttpServletRequest request) {
        String status;
        String mess;
        String id = tem.getId();
        HttpSession session = request.getSession(true);
        Map<String, Object> map = new HashMap<>();
        String userName = (String) session.getAttribute("LOGIN_USER_NAME");
        List<Map<String, Object>> list = this.permissionService.selectUserByName(userName);
        String userId = "";
        String role = "";
        if (list.size() > 0) {
            Map<String, Object> map2 = list.get(0);
            userId = new StringBuilder().append(map2.get("id")).toString();
            role = new StringBuilder().append(map2.get("role")).toString();
        }
        if (role.equals("0")) {
            Map<String, Object> map3 = this.searchHistoryService.selectSearchHistoryById(id);
            String userIdTemp = (String) map3.get("user_id");
            if (!userIdTemp.equals(userId)) {
                map.put("mess", "不允许删除其他用户的SQL");
                map.put("status", "fail");
                return map;
            }
        }
        boolean bool = true;
        if (id != null || !"".equals(id)) {
            LogUtil.i("删除我的SQL " + id);
            bool = this.searchHistoryService.deleteSearchHistory(id);
        }
        if (bool) {
            mess = "操作成功";
            status = "success";
        } else {
            mess = "操作失败";
            status = "fail";
        }
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }
}
