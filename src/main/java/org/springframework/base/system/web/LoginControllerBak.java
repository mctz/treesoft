package org.springframework.base.system.web;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.base.system.entity.Person;
import org.springframework.base.system.service.ConfigService;
import org.springframework.base.system.service.LogService;
import org.springframework.base.system.service.OrderService;
import org.springframework.base.system.service.PermissionService;
import org.springframework.base.system.service.PersonService;
import org.springframework.base.system.utils.Constants;
import org.springframework.base.system.utils.DateUtils;
import org.springframework.base.system.utils.ExcelUtil;
import org.springframework.base.system.utils.LogUtil;
import org.springframework.base.system.utils.MD5Utils;
import org.springframework.base.system.utils.NetworkUtil;
import org.springframework.base.system.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

//@RequestMapping({"treesoft"})
@Controller
public class LoginControllerBak extends BaseController {
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private PersonService personService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private LogService logService;
    public static Map<String, String> loginUserMap = new HashMap();
    public static Map<String, String> ssoClientSessionMap = new HashMap();
    public static Map<String, Integer> loginFailUserIpMap = new HashMap();
    public static String ssoLoginUrl = "";

    @RequestMapping({"/login"})
    public String login(HttpServletRequest request) {
        try {
            Properties props = PropertiesLoaderUtils.loadProperties(new ClassPathResource("application.properties"));
            String ssoLoginUrl2 = (String) props.get("sso.server");
            if (StringUtils.isEmpty(ssoLoginUrl2)) {
                return "system/login";
            }
            return "redirect:" + ssoLoginUrl2;
        } catch (Exception e) {
            LogUtil.m11e("登录失败。" + e);
            request.setAttribute("message", "登录失败。");
            return "system/login";
        }
    }

    @RequestMapping({"/index"})
    public String index(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        if (session == null) {
            return "system/login";
        }
        String realname = (String) session.getAttribute("LOGIN_USER_REAL_NAME");
        String username = (String) session.getAttribute("LOGIN_USER_NAME");
        String permission = (String) session.getAttribute("LOGIN_USER_PERMISSION");
        request.setAttribute("realname", realname);
        request.setAttribute("username", username);
        request.setAttribute("permission", permission);
        request.setAttribute("versionNumber", versionNumber);
        request.setAttribute("isFirstTimeLogin", Boolean.valueOf(Constants.IS_FIRST_TIME_LOGIN));
        try {
            int auditOrderNumber = this.orderService.getAuditOrderNumber();
            request.setAttribute("auditOrderNumber", Integer.valueOf(auditOrderNumber));
            Properties props = PropertiesLoaderUtils.loadProperties(new ClassPathResource("application.properties"));
            String firstIndexPage = (String) props.get("firstIndexPage");
            request.setAttribute("firstIndexPage", firstIndexPage);
        } catch (Exception e) {
            LogUtil.m10e("取得待审核的工单数量 出错，", e);
        }
        String deadline = this.permissionService.selectDeadline();
        if (versionType.equals("01")) {
            request.setAttribute("register", "欢迎试用！有效期至：" + deadline.substring(0, 10));
            return "system/index";
        }
        boolean isvalidate = this.permissionService.identifying();
        if (isvalidate) {
            request.setAttribute("register", "欢迎登录！");
            return "system/index";
        }
        request.setAttribute("register", "请及时注册，试用有效期至：" + deadline.substring(0, 10));
        return "system/index";
    }

    @RequestMapping({"/loginVaildate"})
    @ResponseBody
    public String loginVaildate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String identifyCodeTemp = null;
        JSONObject json = new JSONObject();
        String ip = NetworkUtil.getIpAddress(request);
        HttpSession session = request.getSession(true);
        String username = request.getParameter("username1");
        String password = request.getParameter("password1");
        String captcha = request.getParameter("captcha").toLowerCase();
        if (username == "" || username == null) {
            json.put("message", "请输入用户名！");
            json.put("status", "fail");
            return json.toString();
        }
        try {
            Base64 base64 = new Base64();
            String username2 = new String(base64.decode(username), "UTF-8").toLowerCase();
            password = new String(base64.decode(password), "UTF-8");
            username = StringEscapeUtils.escapeHtml4(username2.trim());
            identifyCodeTemp = (String) session.getAttribute("KAPTCHA_SESSION_KEY");
        } catch (Exception e) {
            LogUtil.m10e("登录验证出错， ", e);
        }
        if (username.length() > 32) {
            json.put("message", "请输入有效的用户名！");
            json.put("status", "fail");
            return json.toString();
        } else if (username.indexOf("'") > 1 || username.indexOf(";") > 1) {
            json.put("message", "请输入有效的用户名！");
            json.put("status", "fail");
            return json.toString();
        } else {
            Properties props = PropertiesLoaderUtils.loadProperties(new ClassPathResource("application.properties"));
            String identifyingCode = (String) props.get("identifyingCode");
            if (identifyingCode.equals("1") && !captcha.equals(identifyCodeTemp)) {
                this.logService.saveLog(String.valueOf(username) + " 输入的验证码有误 ,登录失败！login fail ！ ", username, ip, "", "");
                json.put("message", "验证码错误！");
                json.put("status", "fail");
                return json.toString();
            }
            if (loginFailUserIpMap.get(String.valueOf(username) + "$$" + ip) != null) {
                int loginFailTime = loginFailUserIpMap.get(String.valueOf(username) + "$$" + ip).intValue();
                if (loginFailTime >= 10) {
                    LogUtil.m5i("登录失败超过10次限制 ,IP及用户已锁定，需重启才能解锁， userName=" + username + ", IP=" + ip + ", " + DateUtils.getDateTime());
                    this.logService.saveLog(String.valueOf(username) + "登录失败超过10次限制 ,IP及用户已锁定， 需重启才能解锁 ！ ", username, ip, "", "");
                    json.put("message", "登录失败超过限制 ,IP及用户已锁定！");
                    json.put("status", "fail");
                    return json.toString();
                }
            }
            List<Map<String, Object>> list = new ArrayList<>();
            int userNumber = 0;
            try {
                if (!StringUtils.isEmpty(username) && username.equals("admin")) {
                    Constants.IS_FIRST_TIME_LOGIN = this.permissionService.isFirstLogin();
                }
                if (!StringUtils.isEmpty(username) && username.equals("treesoft")) {
                    Constants.IS_FIRST_TIME_LOGIN = this.permissionService.isFirstLogin();
                }
                list = this.personService.selectPersonByUserName(username);
                userNumber = this.personService.selectPersonNum(new Person());
            } catch (Exception e2) {
                LogUtil.m10e("登录验证出错， ", e2);
            }
            boolean isvalidate = this.permissionService.identifying();
            boolean isDeadLine = this.permissionService.isDeadLine();
            isvalidate = true;
            isDeadLine = true;
            userNumber = 1;
            if (userNumber > 2 && versionType.equals("01")) {
                json.put("message", "涉嫌篡改系统，限制登录！");
                json.put("status", "fail");
                return json.toString();
            } else if (userNumber > 5 && versionType.equals("02") && !isvalidate) {
                json.put("message", "您好，未注册版本限制用户登录！");
                json.put("status", "fail");
                return json.toString();
            } else if (!isDeadLine && !isvalidate) {
                json.put("message", "您好，系统已过期，请购买授权！<a href='" + request.getContextPath() + "/system/permission/i/registerTemp'> 马上注册 </a>");
                json.put("status", "fail");
                return json.toString();
            } else if (list.size() <= 0) {
                request.getSession().setAttribute("KAPTCHA_SESSION_KEY", StringUtil.getUUID());
                if (loginFailUserIpMap.get(username + "$$" + ip) == null) {
                    loginFailUserIpMap.put(username + "$$" + ip, 1);
                } else {
                    int loginFailTime2 = loginFailUserIpMap.get(String.valueOf(username) + "$$" + ip);
                    loginFailUserIpMap.put(username + "$$" + ip, Integer.valueOf(loginFailTime2 + 1));
                }
                this.logService.saveLog(username + " 输入的用户名或密码有误 ,登录失败！login fail！", username, ip, "", "");
                json.put("message", "您输入的用户名或密码有误！");
                json.put("status", "fail");
                return json.toString();
            } else {
                String userId = (String) list.get(0).get("id");
                String pas = (String) list.get(0).get("password");
                String realname = (String) list.get(0).get("realname");
                String role = (String) list.get(0).get("role");
                String userNameInDB = (String) list.get(0).get("username");
                String userStatus = (String) list.get(0).get("status");
                String expiration = (String) list.get(0).get("expiration");
                String permission = (String) list.get(0).get("permission");
                if ("1".equals(userStatus)) {
                    json.put("message", "当前用户已停用！");
                    json.put("status", "fail");
                    return json.toString();
                }
                if (!StringUtils.isEmpty(expiration)) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat(ExcelUtil.DATE_FORMAT);
                        Date bt = sdf.parse(expiration);
                        Date nowDate = new Date();
                        if (bt.before(nowDate)) {
                            json.put("message", "当前用户已过期！");
                            json.put("status", "fail");
                            return json.toString();
                        }
                    } catch (Exception e3) {
                        LogUtil.m10e("登录过期验证出错， ", e3);
                    }
                }
                if (!pas.equals(MD5Utils.MD5Encode(String.valueOf(password) + "treesoft" + username))) {
                    if (loginFailUserIpMap.get(String.valueOf(username) + "$$" + ip) == null) {
                        loginFailUserIpMap.put(String.valueOf(username) + "$$" + ip, 1);
                    } else {
                        int loginFailTime3 = loginFailUserIpMap.get(String.valueOf(username) + "$$" + ip).intValue();
                        loginFailUserIpMap.put(String.valueOf(username) + "$$" + ip, Integer.valueOf(loginFailTime3 + 1));
                    }
                    this.logService.saveLog(String.valueOf(username) + " 输入的用户名或密码有误 ,登录失败！login fail！", username, ip, "", "");
                    json.put("message", "您输入的用户名或密码有误！");
                    json.put("status", "fail");
                    return json.toString();
                }
                session.setAttribute("LOGIN_USER_REAL_NAME", realname);
                session.setAttribute("LOGIN_USER_NAME", userNameInDB);
                session.setAttribute("LOGIN_USER_PERMISSION", permission);
                session.setAttribute("LOGIN_USER_ROLE", role);
                session.setAttribute("LOGIN_USER_ID", userId);
                loginUserMap.put(userNameInDB, session.getId());
                SimpleDateFormat sdf2 = new SimpleDateFormat(ExcelUtil.DATE_FORMAT);
                try {
                    Person person = new Person();
                    person.setId(userId);
                    person.setLastLoginIp(ip);
                    person.setLastLoginTime(sdf2.format(new Date()));
                    this.personService.personUpdateLastLoginTime(person);
                    loginFailUserIpMap.remove(String.valueOf(username) + "$$" + ip);
                } catch (Exception e4) {
                    LogUtil.m10e("更新用户最后登录时间出错， ", e4);
                }
                this.logService.saveLog(String.valueOf(userNameInDB) + "登录成功！login success！ ", userNameInDB, ip, "", "");
                json.put("message", "登录成功！");
                json.put("status", "success");
                return json.toString();
            }
        }
    }

    @RequestMapping({"/logout"})
    public String logout(HttpServletRequest request) {
        Enumeration<String> em = request.getSession().getAttributeNames();
        while (em.hasMoreElements()) {
            request.getSession().removeAttribute(em.nextElement().toString());
        }
        request.getSession().invalidate();
        request.setAttribute("versionNumber", versionNumber);
        if (StringUtils.isEmpty(ssoLoginUrl)) {
            return "system/login";
        }
        return "redirect:" + ssoLoginUrl;
    }
}