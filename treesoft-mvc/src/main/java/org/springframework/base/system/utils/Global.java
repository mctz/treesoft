package org.springframework.base.system.utils;

import com.google.common.collect.Maps;
import java.util.Map;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/utils/Global.class */
public class Global {
    private static Map<String, String> map = Maps.newHashMap();
    private static PropertiesLoader propertiesLoader = new PropertiesLoader("application.properties");

    public static String getConfig(String key) {
        String value = map.get(key);
        if (value == null) {
            value = propertiesLoader.getProperty(key);
            map.put(key, value);
        }
        return value;
    }

    public static String getAdminPath() {
        return getConfig("adminPath");
    }

    public static String getFrontPath() {
        return getConfig("frontPath");
    }

    public static String getUrlSuffix() {
        return getConfig("urlSuffix");
    }
}
