package org.springframework.base.system.utils.security;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base32;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/utils/security/EncodeUtil.class */
public class EncodeUtil {
    public String encode(String str) {
        Base32 b32 = new Base32();
        String result = b32.encodeAsString(str.getBytes());
        return result;
    }

    public String decode(String str) {
        Base32 b32 = new Base32();
        byte[] temp = b32.decode(str);
        try {
            String res = new String(temp, "utf-8");
            return res;
        } catch (Exception e) {
            return "";
        }
    }

    public static void main(String[] grts) {
        EncodeUtil obj = new EncodeUtil();
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("userId", "ww2xxx11aaa");
        jsonObj.put("userLoginName", "yu");
        jsonObj.put("realName", "俞心玲");
        JSONObject databaseJson = new JSONObject();
        databaseJson.put("id", "22222aaa");
        databaseJson.put("name", "测试数据库");
        databaseJson.put("databaseType", "MySQL");
        databaseJson.put("port", "3306");
        databaseJson.put("ip", "127.0.0.1");
        databaseJson.put("userName", "root");
        databaseJson.put("password", "123456");
        databaseJson.put("databaseName", "mysql");
        JSONArray databaseList = new JSONArray();
        databaseList.add(databaseJson);
        jsonObj.put("databaseList", databaseList);
        String s1 = jsonObj.toString();
        System.out.println("加密后的字符串=" + obj.encode(s1));
    }
}
