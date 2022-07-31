package org.springframework.base.system.web;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping({"tools"})
@Controller
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/web/JsonFormatController.class */
public class JsonFormatController extends BaseController {
    @RequestMapping({"jsonFormat"})
    public String jsonFormat(HttpServletRequest request) {
        return "system/jsonFormat";
    }
}
