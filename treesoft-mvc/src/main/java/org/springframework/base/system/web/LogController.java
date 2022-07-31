package org.springframework.base.system.web;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.base.system.entity.IdsDto;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.service.LogService;
import org.springframework.base.system.service.PermissionService;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.base.system.utils.NetworkUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping({"system/log"})
@Controller
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/web/LogController.class */
public class LogController extends BaseController {
    @Autowired
    private LogService logService;
    @Autowired
    private PermissionService permissionService;

    @RequestMapping(value = {"log"}, method = {RequestMethod.GET})
    public String task(Model model) {
        return "system/logList";
    }

    @RequestMapping(value = {"logList/{version}"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> logList(String startTime, String endTime, String userName, String log, HttpServletRequest request) throws Exception {
        Page<Map<String, Object>> page = getPage(request);
        HttpSession session = request.getSession(true);
        Map<String, Object> map2 = new HashMap<>();
        String permission = (String) session.getAttribute("LOGIN_USER_PERMISSION");
        if (permission.indexOf("log") == -1) {
            map2.put("mess", "没有操作权限！");
            map2.put("status", "fail");
            map2.put("rows", "");
            map2.put("total", "");
            return map2;
        }
        try {
            Page<Map<String, Object>> page2 = this.logService.logList(page, startTime, endTime, userName, log);
            map2.put("rows", page2.getResult());
            map2.put("total", Long.valueOf(page2.getTotalCount()));
            map2.put("columns", page2.getColumns());
            map2.put("primaryKey", page2.getPrimaryKey());
        } catch (Exception e) {
            String mess = e.getMessage();
            map2.put("mess", mess);
            map2.put("status", "fail");
            map2.put("rows", "");
            map2.put("total", "");
        }
        return map2;
    }

    @RequestMapping(value = {"deleteLog"}, method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> deleteLog(@RequestBody IdsDto tem, HttpServletRequest request) {
        String status;
        String mess;
        String[] ids = tem.getIds();
        try {
            this.logService.deleteLog(ids);
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

    @RequestMapping(value = {"exportLogDataToExcelFromSearch"}, method = {RequestMethod.POST})
    @ResponseBody
    public void exportLogDataToExcelFromSearch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String userName = request.getParameter("userName");
        String log = request.getParameter("log");
        new ArrayList();
        List<Map<String, Object>> dataList = this.logService.allLogList(startTime, endTime, userName, log);
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(true);
        Sheet sheet = workbook.createSheet();
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("时间");
        row.createCell(1).setCellValue("用户");
        row.createCell(2).setCellValue(" IP ");
        row.createCell(3).setCellValue("数据库");
        row.createCell(4).setCellValue("日志内容");
        for (int i = 0; i < dataList.size(); i++) {
            int rowIndex = 1 + i;
            Row row2 = sheet.createRow(rowIndex);
            row2.createCell(0).setCellValue(new StringBuilder().append(dataList.get(i).get("createTime")).toString());
            row2.createCell(1).setCellValue(new StringBuilder().append(dataList.get(i).get("username")).toString());
            row2.createCell(2).setCellValue(new StringBuilder().append(dataList.get(i).get("ip")).toString());
            row2.createCell(3).setCellValue(new StringBuilder().append(dataList.get(i).get("databaseName")).toString());
            row2.createCell(4).setCellValue(new StringBuilder().append(dataList.get(i).get("log")).toString());
        }
        response.setContentType("application/octet-stream;charset=UTF-8");
        String excelFileName = URLEncoder.encode("操作日志.xlsx", "UTF8");
        try {
            response.addHeader("Content-Disposition", "attachment;filename=" + excelFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
            if (workbook != null) {
                workbook.dispose();
            }
        } catch (Exception e2) {
            LogUtil.e("日志导出EXCEL数据失败 " + e2);
        }
    }

    @RequestMapping({"deleteAllLog"})
    @ResponseBody
    public Map<String, Object> deleteAllLog(HttpServletRequest request) {
        String status;
        String mess;
        Map<String, Object> map = new HashMap<>();
        HttpSession session = request.getSession(true);
        String ip = NetworkUtil.getIpAddress(request);
        String permission = (String) session.getAttribute("LOGIN_USER_PERMISSION");
        if (permission.indexOf("log") == -1) {
            map.put("mess", "没有操作权限！");
            map.put("status", "fail");
            return map;
        }
        try {
            this.logService.deleteAllLog();
            mess = "删除成功";
            status = "success";
            LogUtil.i("清空操作日志，" + ip);
        } catch (Exception e) {
            mess = e.getMessage();
            status = "fail";
        }
        map.put("mess", mess);
        map.put("status", status);
        return map;
    }
}
