package org.springframework.base.system.web;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping({"sso"})
@Controller
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/web/SsoController.class */
public class SsoController {
    @RequestMapping(value = {"treeSoftLogin"}, method = {RequestMethod.GET})
    public String treeSoftLogin(HttpServletRequest request) {
        try {
            String treeSoftSsoURL = String.valueOf("http://127.0.0.1:8080/treesoft") + "/treesoft/ssoAuthorize";
            HttpPost httpPost = new HttpPost(treeSoftSsoURL);
            httpPost.setHeader("Cookie", request.getHeader("Cookie"));
            httpPost.setHeader("SessionId", request.getRequestedSessionId());
            DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
            List<NameValuePair> parameters = new ArrayList<>();
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("userId", "7111114e3333fddfd32f49");
            jsonObj.put("userName", "tom");
            jsonObj.put("realName", "汤姆");
            jsonObj.put("permission", "synchronize,monitor,json,task,log");
            jsonObj.put("client_id", "ef3c7ea735ec76f1209cadacbcc73438");
            jsonObj.put("logout_url", "http://www.treesoft.cn/logout");
            JSONObject jsonObjDB = new JSONObject();
            jsonObjDB.put("id", "47eb6048a474463aaccf22b93b2");
            jsonObjDB.put("name", "我的数据库Hana");
            jsonObjDB.put("databaseType", "MySQL");
            jsonObjDB.put("version", "5.6");
            jsonObjDB.put("port", "3306");
            jsonObjDB.put("ip", "127.0.0.1");
            jsonObjDB.put("userName", "root");
            jsonObjDB.put("password", "123456");
            jsonObjDB.put("databaseName", "hive");
            jsonObjDB.put("exportLimit", "10000");
            jsonObjDB.put("isDefault", "1");
            jsonObjDB.put("isDefaultView", "1");
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(jsonObjDB);
            jsonObj.put("databaseList", jsonArray);
            parameters.add(new BasicNameValuePair("token", jsonObj.toString()));
            httpPost.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));
            HttpResponse response2 = defaultHttpClient.execute(httpPost);
            HttpEntity entity = response2.getEntity();
            String result = EntityUtils.toString(entity, "utf-8");
            JSONObject jsonObjResult = JSONObject.fromObject(result);
            String status = jsonObjResult.getString("status");
            jsonObjResult.getString("mess");
            String ssoClientSessionId = jsonObjResult.getString("ssoClientSessionId");
            if (status.equals("success")) {
                return "redirect:http://127.0.0.1:8080/treesoft/treesoft/ssoIndex?ssoClientSessionId=" + ssoClientSessionId + "&userName=tom&permission=synchronize,monitor,json,task,log";
            }
            return "error.jsp";
        } catch (Exception e) {
            return "";
        }
    }
}
