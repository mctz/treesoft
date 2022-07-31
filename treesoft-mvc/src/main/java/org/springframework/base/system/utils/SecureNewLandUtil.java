package org.springframework.base.system.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/utils/SecureNewLandUtil.class */
public class SecureNewLandUtil {
    public static String userAuthForNewLand(String authUrl, String token) {
        try {
            StringBuffer buffer = new StringBuffer();
            URL url = new URL(String.valueOf(authUrl) + "?token=" + token);
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
            httpUrlConn.setRequestMethod("GET");
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
            if (StringUtils.isEmpty(resultStr)) {
                return "";
            }
            JSONObject obj = JSONObject.fromObject(resultStr);
            String code = obj.getString("code");
            if (!StringUtils.isEmpty(code) && code.equals("0")) {
                return "success";
            }
            LogUtil.e("NewLand 登录认证 失败, " + resultStr);
            return "NewLand 登录认证 失败";
        } catch (Exception e) {
            LogUtil.e("NewLand 登录认证 失败。" + e);
            return "NewLand 登录认证 失败";
        }
    }
}
