package org.springframework.base.system.utils.security;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.springframework.base.system.utils.CryptoUtil;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/utils/security/DESEncrypt.class */
public class DESEncrypt {
    static AlgorithmParameterSpec iv = null;
    private static SecretKey key = null;
    private final byte[] DESkey = "xiy@li&u".getBytes();
    private final byte[] DESIV = "18367201".getBytes();

    public DESEncrypt() throws Exception {
        DESKeySpec keySpec = new DESKeySpec(this.DESkey);
        iv = new IvParameterSpec(this.DESIV);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(CryptoUtil.DES);
        key = keyFactory.generateSecret(keySpec);
    }

    public String encode(String data) throws Exception {
        Cipher enCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        enCipher.init(1, key, iv);
        byte[] pasByte = enCipher.doFinal(data.getBytes("utf-8"));
        String str = Base64.encodeBase64String(pasByte);
        return URLEncoder.encode(str, "UTF-8");
    }

    public String decode(String data) throws Exception {
        Cipher deCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        deCipher.init(2, key, iv);
        byte[] pasByte = deCipher.doFinal(Base64.decodeBase64(data));
        String str = new String(pasByte, "UTF-8");
        return URLDecoder.decode(str, "UTF-8");
    }

    public static void main(String[] args) throws Exception {
        DESEncrypt tools = new DESEncrypt();
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("userId", "ww2xxx11aaa");
        jsonObj.put("userLoginName", "lixm");
        jsonObj.put("realName", "李小明");
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
        String data = jsonObj.toString();
        System.out.println("明文输出:" + data);
        System.out.println("密文输出:" + tools.encode(data));
    }
}
