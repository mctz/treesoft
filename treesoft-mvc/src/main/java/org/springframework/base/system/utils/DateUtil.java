package org.springframework.base.system.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/utils/DateUtil.class */
public class DateUtil {
    private static DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");

    public static Date getTodayByTreeSoft() {
        try {
            StringBuffer buffer = new StringBuffer();
            URL url = new URL("http://dms.treesoft.cn/treesoft/openApi/getDateTimeString");
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
            if (!StringUtils.isEmpty(resultStr)) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = simpleDateFormat.parse(resultStr);
                return date;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static Date getTodayBySuning() {
        try {
            StringBuffer buffer = new StringBuffer();
            URL url = new URL("http://quan.suning.com/getSysTime.do");
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
            httpUrlConn.setRequestMethod("GET");
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
                    JSONObject obj = JSONObject.fromObject(resultStr);
                    return formatDate.parse(obj.getString("sysTime2"));
                }
            }
        } catch (Exception e) {
            return null;
        }
    }
}
