package org.springframework.base.system.web;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.base.system.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping({"dataApi"})
@Controller
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/web/DataApiController.class */
public class DataApiController {
    @Autowired
    private CollectService collectService;

    @RequestMapping({"select/{alias}"})
    @ResponseBody
    public String select(@PathVariable("alias") String alias, String appKey, HttpServletRequest request) {
        try {
            Map<String, String[]> paramMap = request.getParameterMap();
            String jsonString = this.collectService.selectDataByAlias(alias, appKey, paramMap);
            return jsonString;
        } catch (Exception e) {
            return "";
        }
    }
}
