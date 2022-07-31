package org.springframework.base.system.dbUtils;

import org.springframework.base.system.utils.HttpUtil;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/dbUtils/ElasticSearchUtil.class */
public class ElasticSearchUtil {
    public static String VALID_HTTP_URL = "";

    public static String returnVaildURL(String ip, String port, String userName, String password) {
        String resUrl = "";
        String ip2 = ip.replace("；", ";");
        if (ip2.indexOf(";") > 0) {
            String[] ips = ip2.split(";");
            for (String ipOne : ips) {
                if (ipOne.indexOf(":") > 0) {
                    resUrl = ipOne;
                } else {
                    resUrl = String.valueOf(ipOne) + ":" + port;
                }
                try {
                    String httpUrl = "http://" + resUrl;
                    HttpUtil.getHttpMethodAuth(String.valueOf(httpUrl) + "/_cat/indices/?format=json", "GET", userName, password);
                    VALID_HTTP_URL = httpUrl;
                    return "";
                } catch (Exception e) {
                }
            }
        } else {
            resUrl = "http://" + ip2 + ":" + port;
            VALID_HTTP_URL = resUrl;
        }
        return resUrl;
    }
}
