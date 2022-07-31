package org.springframework.base.system.web;

import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import org.springframework.base.system.entity.Attach;
import org.springframework.base.system.entity.IdsDto;
import org.springframework.base.system.entity.Order;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.service.ConfigService;
import org.springframework.base.system.service.FileUploadService;
import org.springframework.base.system.service.OrderService;
import org.springframework.base.system.service.PermissionService;
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

@RequestMapping({"order"})
@Controller
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/web/OrderController.class */
public class OrderController extends BaseController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private FileUploadService fileUploadService;
    @Autowired
    private ConfigService configService;

    @RequestMapping(value = {"orderList"}, method = {RequestMethod.GET})
    public String orderList(Model model) {
        return "system/orderList";
    }

    @RequestMapping(value = {"orderAuditList"}, method = {RequestMethod.GET})
    public String orderAuditList(Model model) {
        return "system/orderAuditList";
    }

    @RequestMapping(value = {"orderListData/{version}"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> orderListData(HttpServletRequest request, String startTime, String endTime, String doSql) {
        Page<Map<String, Object>> page = getPage(request);
        HttpSession session = request.getSession(true);
        String userId = (String) session.getAttribute("LOGIN_USER_ID");
        String permission = (String) session.getAttribute("LOGIN_USER_PERMISSION");
        if (permission.indexOf("ordersApply") == -1) {
            Map<String, Object> map2 = new HashMap<>();
            map2.put("mess", "没有操作权限！");
            map2.put("status", "fail");
            map2.put("rows", "");
            map2.put("total", "");
            return map2;
        }
        try {
            page = this.orderService.orderListData(page, startTime, endTime, userId, doSql);
            return getEasyUIData(page);
        } catch (Exception e) {
            LogUtil.e("SQL工单列表查询出错， " + e);
            return getEasyUIData(page);
        }
    }

    @RequestMapping(value = {"orderAuditListData/{version}"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> orderAuditListData(HttpServletRequest request, String startTime, String endTime, String userName, String doSql) {
        Page<Map<String, Object>> page = getPage(request);
        HttpSession session = request.getSession(true);
        String permission = (String) session.getAttribute("LOGIN_USER_PERMISSION");
        if (permission.indexOf("ordersAudit") == -1) {
            Map<String, Object> map2 = new HashMap<>();
            map2.put("mess", "没有操作权限！");
            map2.put("status", "fail");
            map2.put("rows", "");
            map2.put("total", "");
            return map2;
        }
        try {
            page = this.orderService.orderAuditListData(page, startTime, endTime, userName, doSql);
            return getEasyUIData(page);
        } catch (Exception e) {
            LogUtil.e("SQL工单列表查询出错， " + e);
            return getEasyUIData(page);
        }
    }

    @RequestMapping(value = {"addOrderForm"}, method = {RequestMethod.GET})
    public String addTaskForm(Model model) {
        List<Map<String, Object>> configList = new ArrayList<>();
        try {
            configList = this.configService.getAllConfigList();
        } catch (Exception e) {
            LogUtil.e("打开 某个 SQL工单 配置页面出错， " + e);
        }
        model.addAttribute("configList", configList);
        return "system/orderForm";
    }

    @RequestMapping(value = {"editOrderForm/{id}"}, method = {RequestMethod.GET})
    public String editTaskForm(@PathVariable("id") String id, Model model) {
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> configList = new ArrayList<>();
        try {
            configList = this.configService.getAllConfigList();
            map = this.orderService.getOrderById(id);
            List<Attach> attachList = this.fileUploadService.selectAttachsByBid(id);
            map.put("attachList", attachList);
        } catch (Exception e) {
            LogUtil.e("打开 某个 SQL工单 配置页面出错， " + e);
        }
        model.addAttribute("configList", configList);
        model.addAttribute("order", map);
        return "system/orderForm";
    }

    @RequestMapping(value = {"orderAuditForm/{id}"}, method = {RequestMethod.GET})
    public String orderAuditForm(@PathVariable("id") String id, Model model) {
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> configList = new ArrayList<>();
        try {
            configList = this.configService.getAllConfigList();
            map = this.orderService.getOrderById(id);
            List<Attach> attachList = this.fileUploadService.selectAttachsByBid(id);
            map.put("attachList", attachList);
        } catch (Exception e) {
            LogUtil.e("打开 某个 SQL工单 配置页面出错， " + e);
        }
        model.addAttribute("configList", configList);
        model.addAttribute("order", map);
        return "system/orderAuditForm";
    }

    @RequestMapping(value = {"orderUpdate"}, method = {RequestMethod.POST}, produces = {"text/html;charset=UTF-8"})
    @ResponseBody
    public String orderUpdate(HttpServletRequest request, HttpServletResponse response, @ModelAttribute Order order) {
        String status;
        String mess;
        JSONObject json = new JSONObject();
        try {
            if (StringUtils.isEmpty(order.getId())) {
                String tempId = StringUtil.getUUID();
                order.setId(tempId);
                HttpSession session = request.getSession(true);
                String createUserId = (String) session.getAttribute("LOGIN_USER_ID");
                String createUserName = (String) session.getAttribute("LOGIN_USER_REAL_NAME");
                order.setCreateUserId(createUserId);
                order.setCreateUserName(createUserName);
                order.setAuditUserName("");
                order.setAuditTime("");
                order.setStatus("0");
                order.setLevel("0");
                order.setOrderType("0");
                if (order.getOrderType().equals("0")) {
                    order.setOrderName("SQL工单");
                }
                if (order.getOrderType().equals("1")) {
                    order.setOrderName("事项工单");
                }
                if (order.getOrderType().equals("2")) {
                    order.setOrderName("运维工单 ");
                }
                this.orderService.orderSave(order);
            } else {
                order.setStatus("0");
                this.orderService.orderUpdate(order);
            }
            if (order.getAttachmentIds() != null) {
                this.fileUploadService.updateBid(order.getAttachmentIds(), order.getId());
            }
            mess = "保存成功，并提交审批";
            status = "success";
        } catch (Exception e) {
            LogUtil.e("新增,修改 SQL工单  出错， " + e);
            mess = "新增,修改 SQL工单  出错";
            status = "fail";
        }
        json.put("mess", mess);
        json.put("status", status);
        return json.toString();
    }

    @RequestMapping(value = {"orderRun/{id}"}, method = {RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> orderRun(@PathVariable("id") String id, HttpServletRequest request, Model model) {
        String status;
        String mess;
        String runMess = "";
        String ip = NetworkUtil.getIpAddress(request);
        try {
            HttpSession session = request.getSession(true);
            String runUserId = (String) session.getAttribute("LOGIN_USER_ID");
            String runUserName = (String) session.getAttribute("LOGIN_USER_REAL_NAME");
            List<Attach> attachList = this.fileUploadService.selectAttachsByBid(id);
            runMess = this.orderService.orderRun(id, runUserId, runUserName, ip, attachList);
            mess = "执行完成";
            status = "success";
        } catch (Exception e) {
            LogUtil.e("执行工单失败， " + e);
            mess = "执行工单失败 " + e;
            status = "fail";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("mess", mess);
        map.put("status", status);
        map.put("runMess", runMess);
        return map;
    }

    /* JADX WARN: Removed duplicated region for block: B:4:0x002d A[Catch: Exception -> 0x00b0, TryCatch #0 {Exception -> 0x00b0, blocks: (B:3:0x0016, B:4:0x002d, B:6:0x0052, B:8:0x005d, B:10:0x0068, B:12:0x008f, B:14:0x0099), top: B:19:0x0016 }] */
    @RequestMapping(value = {"deleteOrder"}, method = {RequestMethod.POST})
    @ResponseBody
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public Map<String, Object> deleteOrder(@RequestBody IdsDto r5, HttpServletRequest r6) {
        /*
            r4 = this;
            java.util.HashMap r0 = new java.util.HashMap
            r1 = r0
            r1.<init>()
            r7 = r0
            r0 = r5
            java.lang.String[] r0 = r0.getIds()
            r8 = r0
            java.lang.String r0 = ""
            r9 = r0
            java.lang.String r0 = ""
            r10 = r0
            r0 = r4
            org.springframework.base.system.service.OrderService r0 = r0.orderService     // Catch: java.lang.Exception -> Lb0
            r1 = r8
            java.util.List r0 = r0.selectOrderByIds(r1)     // Catch: java.lang.Exception -> Lb0
            r11 = r0
            r0 = r11
            java.util.Iterator r0 = r0.iterator()     // Catch: java.lang.Exception -> Lb0
            r13 = r0
            goto L8f
        L2d:
            r0 = r13
            java.lang.Object r0 = r0.next()     // Catch: java.lang.Exception -> Lb0
            java.util.Map r0 = (java.util.Map) r0     // Catch: java.lang.Exception -> Lb0
            r12 = r0
            r0 = r12
            java.lang.String r1 = "status"
            java.lang.Object r0 = r0.get(r1)     // Catch: java.lang.Exception -> Lb0
            java.lang.String r0 = (java.lang.String) r0     // Catch: java.lang.Exception -> Lb0
            r14 = r0
            java.lang.String r0 = "1"
            r1 = r14
            boolean r0 = r0.equals(r1)     // Catch: java.lang.Exception -> Lb0
            if (r0 != 0) goto L68
            java.lang.String r0 = "2"
            r1 = r14
            boolean r0 = r0.equals(r1)     // Catch: java.lang.Exception -> Lb0
            if (r0 != 0) goto L68
            java.lang.String r0 = "4"
            r1 = r14
            boolean r0 = r0.equals(r1)     // Catch: java.lang.Exception -> Lb0
            if (r0 == 0) goto L8f
        L68:
            java.lang.String r0 = "已审核的工单不允许删除！， "
            org.springframework.base.system.utils.LogUtil.e(r0)     // Catch: java.lang.Exception -> Lb0
            java.lang.String r0 = "已审核的工单不允许删除！"
            r9 = r0
            java.lang.String r0 = "fail"
            r10 = r0
            r0 = r7
            java.lang.String r1 = "mess"
            r2 = r9
            java.lang.Object r0 = r0.put(r1, r2)     // Catch: java.lang.Exception -> Lb0
            r0 = r7
            java.lang.String r1 = "status"
            r2 = r10
            java.lang.Object r0 = r0.put(r1, r2)     // Catch: java.lang.Exception -> Lb0
            r0 = r7
            return r0
        L8f:
            r0 = r13
            boolean r0 = r0.hasNext()     // Catch: java.lang.Exception -> Lb0
            if (r0 != 0) goto L2d
            r0 = r4
            org.springframework.base.system.service.OrderService r0 = r0.orderService     // Catch: java.lang.Exception -> Lb0
            r1 = r8
            boolean r0 = r0.deleteOrder(r1)     // Catch: java.lang.Exception -> Lb0
            java.lang.String r0 = "删除成功"
            r9 = r0
            java.lang.String r0 = "success"
            r10 = r0
            goto Ld0
        Lb0:
            r11 = move-exception
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r1 = r0
            java.lang.String r2 = "删除SQL工单  出错， "
            r1.<init>(r2)
            r1 = r11
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r0 = r0.toString()
            org.springframework.base.system.utils.LogUtil.e(r0)
            java.lang.String r0 = "删除SQL工单 出错"
            r9 = r0
            java.lang.String r0 = "fail"
            r10 = r0
        Ld0:
            r0 = r7
            java.lang.String r1 = "mess"
            r2 = r9
            java.lang.Object r0 = r0.put(r1, r2)
            r0 = r7
            java.lang.String r1 = "status"
            r2 = r10
            java.lang.Object r0 = r0.put(r1, r2)
            r0 = r7
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.springframework.base.system.web.OrderController.deleteOrder(org.springframework.base.system.entity.IdsDto, javax.servlet.http.HttpServletRequest):java.util.Map");
    }

    @RequestMapping(value = {"auditOrderPass"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> auditOrderPass(@RequestBody IdsDto tem, HttpServletRequest request) {
        String status;
        String mess;
        String[] ids = tem.getIds();
        String remark = tem.getRemark();
        if (StringUtils.isEmpty(remark)) {
            remark = "";
        }
        Map<String, Object> map = new HashMap<>();
        try {
            List<Map<String, Object>> list = this.orderService.selectOrderByIds(ids);
            Iterator<Map<String, Object>> it = list.iterator();
            while (it.hasNext()) {
                Map<String, Object> mapTemp = it.next();
                String orderStatus = (String) mapTemp.get("status");
                if ("4".equals(orderStatus)) {
                    LogUtil.e("已执行的工单不用审核！， ");
                    map.put("mess", "已执行的工单不用审核！");
                    map.put("status", "fail");
                    return map;
                }
            }
            HttpSession session = request.getSession(true);
            String auditUserId = (String) session.getAttribute("LOGIN_USER_ID");
            String auditUserName = (String) session.getAttribute("LOGIN_USER_REAL_NAME");
            this.orderService.updateOrderStatusByIds(ids, "1", remark, auditUserId, auditUserName);
            mess = "操作成功";
            status = "success";
        } catch (Exception e) {
            LogUtil.e("审核工单  出错， " + e);
            mess = "审核工单出错";
            status = "fail";
        }
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    @RequestMapping(value = {"auditOrderNoPass"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> auditOrderNoPass(@RequestBody IdsDto tem, HttpServletRequest request) {
        String status;
        String mess;
        String[] ids = tem.getIds();
        String remark = tem.getRemark();
        Map<String, Object> map = new HashMap<>();
        try {
            List<Map<String, Object>> list = this.orderService.selectOrderByIds(ids);
            Iterator<Map<String, Object>> it = list.iterator();
            while (it.hasNext()) {
                Map<String, Object> mapTemp = it.next();
                String orderStatus = (String) mapTemp.get("status");
                if ("4".equals(orderStatus)) {
                    LogUtil.e("已执行的工单不用审核！， ");
                    map.put("mess", "已执行的工单不用审核！");
                    map.put("status", "fail");
                    return map;
                }
            }
            HttpSession session = request.getSession(true);
            String auditUserId = (String) session.getAttribute("LOGIN_USER_ID");
            String auditUserName = (String) session.getAttribute("LOGIN_USER_REAL_NAME");
            this.orderService.updateOrderStatusByIds(ids, "2", remark, auditUserId, auditUserName);
            mess = "操作成功";
            status = "success";
        } catch (Exception e) {
            LogUtil.e("审核不通过 工单  出错， " + e);
            mess = "审核不通过  工单出错";
            status = "fail";
        }
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    @RequestMapping(value = {"auditOrderRebut"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> auditOrderRebut(@RequestBody IdsDto tem, HttpServletRequest request) {
        String status;
        String mess;
        String[] ids = tem.getIds();
        String remark = tem.getRemark();
        try {
            HttpSession session = request.getSession(true);
            String auditUserId = (String) session.getAttribute("LOGIN_USER_ID");
            String auditUserName = (String) session.getAttribute("LOGIN_USER_REAL_NAME");
            this.orderService.updateOrderStatusByIds(ids, "3", remark, auditUserId, auditUserName);
            mess = "操作成功";
            status = "success";
        } catch (Exception e) {
            LogUtil.e("审核驳回工单  出错， " + e);
            mess = "审核驳回工单出错";
            status = "fail";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }

    @RequestMapping(value = {"getAuditOrderNumber"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getAuditOrderNumber(HttpServletRequest request, Model model) {
        String status;
        String mess;
        int auditOrderNumber = 0;
        try {
            auditOrderNumber = this.orderService.getAuditOrderNumber();
            mess = "查询成功";
            status = "success";
        } catch (Exception e) {
            LogUtil.e("查询待审批的工单数量出错， " + e);
            mess = "查询出错" + e;
            status = "fail";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("mess", mess);
        map.put("status", status);
        map.put("auditOrderNumber", Integer.valueOf(auditOrderNumber));
        return map;
    }
}
