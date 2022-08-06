package org.springframework.base.system.web;

import org.springframework.base.system.persistence.Page;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class BaseController {
    public static String versionType = "01";
    public static String versionNumber = "试用版";

    @InitBinder
    public void initBinder(WebDataBinder binder) {
    }

    public <T> Page<T> getPage(HttpServletRequest request) {
        return null;
    }

    public <T> Map<String, Object> getEasyUIData(Page<T> page) {
        return null;
    }
}