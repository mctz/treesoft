package org.springframework.base.system.web;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping({"/"})
@Controller
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/web/LoginTempController.class */
public class LoginTempController extends BaseController {
    @RequestMapping(value = {""}, method = {RequestMethod.GET})
    public String login(HttpServletRequest request) {
        return "system/login";
    }

    @RequestMapping(value = {"login"}, method = {RequestMethod.GET})
    public String defaultLogin(HttpServletRequest request) {
        return "system/login";
    }
}
