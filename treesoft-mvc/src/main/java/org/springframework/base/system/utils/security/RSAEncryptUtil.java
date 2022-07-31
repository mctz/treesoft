package org.springframework.base.system.utils.security;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/utils/security/RSAEncryptUtil.class */
public class RSAEncryptUtil {
    private static Map<Integer, String> keyMap = new HashMap();

    public static void main(String[] args) throws Exception {
        genKeyPair();
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
        String message = jsonObj.toString();
        System.out.println("随机生成的公钥为:" + keyMap.get(0));
        System.out.println("随机生成的私钥为:" + keyMap.get(1));
        String messageEn = encrypt(message, keyMap.get(0));
        System.out.println(String.valueOf(message) + "\t加密后的字符串为:" + messageEn);
        String messageDe = decrypt(messageEn, keyMap.get(1));
        System.out.println("还原后的字符串为:" + messageDe);
    }

    public static void genKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(RSAUtils.KEY_ALGORITHM);
        keyPairGen.initialize(1024, new SecureRandom());
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
        String privateKeyString = new String(Base64.encodeBase64(privateKey.getEncoded()));
        keyMap.put(0, publicKeyString);
        keyMap.put(1, privateKeyString);
    }

    public static String encrypt(String str, String publicKey) throws Exception {
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance(RSAUtils.KEY_ALGORITHM).generatePublic(new X509EncodedKeySpec(decoded));
        Cipher cipher = Cipher.getInstance(RSAUtils.KEY_ALGORITHM);
        cipher.init(1, pubKey);
        String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
        return outStr;
    }

    public static String decrypt(String str, String privateKey) throws Exception {
        byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance(RSAUtils.KEY_ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(decoded));
        Cipher cipher = Cipher.getInstance(RSAUtils.KEY_ALGORITHM);
        cipher.init(2, priKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }
}
