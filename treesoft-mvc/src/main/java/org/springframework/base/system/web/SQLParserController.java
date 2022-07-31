package org.springframework.base.system.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.base.system.entity.NewQueryDTO;
import org.springframework.base.system.service.ConfigService;
import org.springframework.base.system.service.SQLParserService;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping({"system/sqlParser"})
@Controller
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/web/SQLParserController.class */
public class SQLParserController {
    @Autowired
    private SQLParserService sqlParserService;
    @Autowired
    private ConfigService configService;

    @RequestMapping({"operationSQL"})
    @ResponseBody
    public Map<String, Object> operationSQL(HttpServletRequest request, NewQueryDTO dto) {
        String status;
        String mess;
        Map<String, Object> mapResult = new HashMap<>();
        List<Map<String, String>> sqlList = new ArrayList<>();
        Map<String, Object> map = this.configService.getConfigById(dto.getDatabaseConfigId());
        String databaseType = (String) map.get("databaseType");
        if (databaseType.equals("Redis")) {
            mapResult.put("mess", "该功能不支持Redis数据库!");
            mapResult.put("status", "fail");
            return mapResult;
        } else if (databaseType.equals("该功能不支持Memcache数据库!")) {
            mapResult.put("mess", "Memcache");
            mapResult.put("status", "fail");
            return mapResult;
        } else if (StringUtils.isEmpty(dto.getSql())) {
            mapResult.put("mess", "请提交SQL语句");
            mapResult.put("status", "fail");
            return mapResult;
        } else {
            try {
                sqlList = this.sqlParserService.operationSQL(dto);
                mess = "操作成功";
                status = "success";
            } catch (Exception e) {
                LogUtil.e("SQL解析出错，\n " + dto.getSql(), e);
                mess = "SQL解析失败或不支持当前SQL，请检查SQL命令.";
                status = "fail";
            }
            mapResult.put("mess", mess);
            mapResult.put("status", status);
            mapResult.put("rows", sqlList);
            return mapResult;
        }
    }
}
