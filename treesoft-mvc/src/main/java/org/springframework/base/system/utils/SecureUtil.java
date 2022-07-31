package org.springframework.base.system.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/utils/SecureUtil.class */
public class SecureUtil {
    public static String userAuthForOpen(String userName, String password) {
        String message = "";
        try {
            StringBuffer buffer = new StringBuffer();
            URL url = new URL("http://sso.open.com.cn/sso/ajaxlogin?username=" + userName + "&password=" + password + "&appId=10005");
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
            httpUrlConn.setRequestMethod("POST");
            httpUrlConn.setConnectTimeout(3000);
            httpUrlConn.connect();
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            while (true) {
                String str = bufferedReader.readLine();
                if (str == null) {
                    break;
                }
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            httpUrlConn.disconnect();
            String resultStr = buffer.toString();
            if (!StringUtils.isEmpty(resultStr)) {
                JSONObject obj = JSONObject.fromObject(resultStr);
                String code = obj.getString("code");
                if (!StringUtils.isEmpty(code) && code.equals("0")) {
                    return "登录成功";
                }
                message = obj.getString("message");
                LogUtil.e("奥鹏登录认证 失败,code=" + code + ", message=" + obj.getString("message"));
            }
            return message;
        } catch (Exception e) {
            LogUtil.e("奥鹏登录认证 失败。" + e);
            return "奥鹏登录认证 失败";
        }
    }
}
