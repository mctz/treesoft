package org.springframework.base.system.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/utils/HttpUtil.class */
public class HttpUtil {
    public static String getHttpMethod(String urlString, String method) throws Exception {
        StringBuffer buffer = new StringBuffer();
        URL url = new URL(urlString);
        HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
        httpUrlConn.setRequestMethod(method);
        httpUrlConn.setConnectTimeout(3000);
        httpUrlConn.connect();
        InputStream inputStream = httpUrlConn.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        while (true) {
            String str = bufferedReader.readLine();
            if (str != null) {
                buffer.append(str);
            } else {
                bufferedReader.close();
                inputStreamReader.close();
                inputStream.close();
                httpUrlConn.disconnect();
                String resultStr = buffer.toString();
                return resultStr;
            }
        }
    }

    public static String getHttpMethodAuth(String urlString, String method, String user, String password) throws Exception {
        StringBuffer buffer = new StringBuffer();
        URL url = new URL(urlString);
        HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
        httpUrlConn.setRequestMethod(method);
        httpUrlConn.setConnectTimeout(3000);
        if (!StringUtils.isEmpty(user) && !StringUtils.isEmpty(password)) {
            String auth = String.valueOf(user) + ":" + password;
            byte[] rel = Base64.encodeBase64(auth.getBytes());
            String res = new String(rel);
            httpUrlConn.setRequestProperty(ServletUtils.AUTHENTICATION_HEADER, "Basic " + res);
        }
        httpUrlConn.connect();
        InputStream inputStream = httpUrlConn.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        while (true) {
            String str = bufferedReader.readLine();
            if (str != null) {
                buffer.append(str);
            } else {
                bufferedReader.close();
                inputStreamReader.close();
                inputStream.close();
                httpUrlConn.disconnect();
                String resultStr = buffer.toString();
                return resultStr;
            }
        }
    }

    public static String getHttpMethodNew(String urlString, String method, String contentType, String bodyString, String user, String password) throws Exception {
        StringBuffer sbuffer = new StringBuffer("");
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setRequestProperty("Accept-Charset", "utf-8");
        connection.setRequestProperty("Connection", "keep-alive");
        connection.setRequestProperty("Transfer-Encoding", "chunked");
        if (!StringUtils.isEmpty(user) && !StringUtils.isEmpty(password)) {
            String auth = String.valueOf(user) + ":" + password;
            byte[] rel = Base64.encodeBase64(auth.getBytes());
            String res = new String(rel);
            connection.setRequestProperty(ServletUtils.AUTHENTICATION_HEADER, "Basic " + res);
        }
        connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
        connection.setRequestProperty("Content-Length", new StringBuilder(String.valueOf(bodyString.toString().getBytes().length)).toString());
        connection.setReadTimeout(10000);
        connection.setConnectTimeout(10000);
        connection.connect();
        OutputStream out = connection.getOutputStream();
        out.write(bodyString.getBytes("utf-8"));
        out.flush();
        out.close();
        if (connection.getResponseCode() == 200 || connection.getResponseCode() == 201) {
            InputStreamReader inputStream = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(inputStream);
            while (true) {
                String lines = reader.readLine();
                if (lines != null) {
                    sbuffer.append(new String(lines.getBytes(), "utf-8"));
                } else {
                    reader.close();
                    connection.disconnect();
                    return sbuffer.toString();
                }
            }
        } else {
            LogUtil.e("发送 http 请求出错 ResponseCode=" + connection.getResponseCode() + ", ResponseMessage=" + connection.getResponseMessage());
            throw new Exception(connection.getResponseMessage());
        }
    }
}
