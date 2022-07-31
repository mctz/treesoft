package org.springframework.base.system.web;

import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.base.system.persistence.Page;
import org.springframework.base.system.utils.DateUtils;
import org.springframework.base.system.utils.StringUtil;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/web/BaseController.class */
public class BaseController {
    public static String versionType = "01";
    public static String versionNumber = "试用版";

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new PropertyEditorSupport() { // from class: org.springframework.base.system.web.BaseController.1
            public void setAsText(String text) {
                setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
            }

            public String getAsText() {
                Object value = getValue();
                return value != null ? value.toString() : "";
            }
        });
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() { // from class: org.springframework.base.system.web.BaseController.2
            public void setAsText(String text) {
                setValue(DateUtils.parseDate(text));
            }
        });
        binder.registerCustomEditor(Timestamp.class, new PropertyEditorSupport() { // from class: org.springframework.base.system.web.BaseController.3
            public void setAsText(String text) {
                Date date = DateUtils.parseDate(text);
                setValue(date == null ? null : new Timestamp(date.getTime()));
            }
        });
    }

    public <T> Page<T> getPage(HttpServletRequest request) {
        int pageNo = 1;
        int pageSize = 30;
        String orderBy = "";
        String order = Page.ASC;
        if (StringUtil.isNotEmpty(request.getParameter("page"))) {
            pageNo = Integer.valueOf(request.getParameter("page")).intValue();
        }
        if (StringUtil.isNotEmpty(request.getParameter("rows"))) {
            pageSize = Integer.valueOf(request.getParameter("rows")).intValue();
        }
        if (StringUtil.isNotEmpty(request.getParameter("sort"))) {
            orderBy = request.getParameter("sort").toString();
        }
        if (StringUtil.isNotEmpty(request.getParameter("order"))) {
            order = request.getParameter("order").toString();
        }
        return new Page<>(pageNo, pageSize, orderBy, order);
    }

    public <T> Map<String, Object> getEasyUIData(Page<T> page) {
        Map<String, Object> map = new HashMap<>();
        map.put("rows", page.getResult());
        map.put("total", Long.valueOf(page.getTotalCount()));
        map.put("columns", page.getColumns());
        map.put("primaryKey", page.getPrimaryKey());
        return map;
    }
}
