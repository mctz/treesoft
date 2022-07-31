package org.springframework.base.system.utils;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/utils/RSAEncrypt.class */
public class RSAEncrypt {
    public static final String ALGORITHM = "DSA";
    private static final int KEY_SIZE = 1024;
    private static final String DEFAULT_SEED = "1234567890";
    private static final String PUBLIC_KEY = "DSAPublicKey";
    private static final String PRIVATE_KEY = "DSAPrivateKey";
    private static final String publicKey = "MIIBtzCCASwGByqGSM44BAEwggEfAoGBAP1/U4EddRIpUt9KnC7s5Of2EbdSPO9EAMMeP4C2USZpRV1AIlH7WT2NWPq/xfW6MPbLm1Vs14E7gB00b/JmYLdrmVClpJ+f6AR7ECLCT7up1/63xhv4O1fnxqimFQ8E+4P208UewwI1VBNaFpEy9nXzrith1yrv8iIDGZ3RSAHHAhUAl2BQjxUjC8yykrmCouuEC/BYHPUCgYEA9+GghdabPd7LvKtcNrhXuXmUr7v6OuqC+VdMCz0HgmdRWVeOutRZT+ZxBxCBgLRJFnEj6EwoFhO3zwkyjMim4TwWeotUfI0o4KOuHiuzpnWRbqN/C/ohNWLx+2J6ASQ7zKTxvqhRkImog9/hWuWfBpKLZl6Ae1UlZAFMO/7PSSoDgYQAAoGAQRzvaSxmJQlzadWlABvxr0xGr2t6MEFLQhG5N0fZvNWPJkqYSe1yN9XWwu/pvCu7D/nesCRBF+a38Q5QEZS2jFpEcoCTHDDb660406KLt6eBTBJ8l2O3XmkeCYtY6F5hhT51RX/QyTa8UJOdA7LAxfaw7S9HLK6myzXOePP0T48=";

    public static String sign(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = decryptBASE64(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(keyFactory.getAlgorithm());
        signature.initSign(priKey);
        signature.update(data);
        return encryptBASE64(signature.sign());
    }

    public static boolean verify(byte[] data, String publicKey22, String sign) throws Exception {
        byte[] keyBytes = decryptBASE64(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PublicKey pubKey = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(keyFactory.getAlgorithm());
        signature.initVerify(pubKey);
        signature.update(data);
        boolean bl = signature.verify(decryptBASE64(sign));
        return bl;
    }

    public static Map<String, Object> initKey(String seed) throws Exception {
        KeyPairGenerator keygen = KeyPairGenerator.getInstance(ALGORITHM);
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.setSeed(seed.getBytes());
        keygen.initialize(KEY_SIZE, secureRandom);
        KeyPair keys = keygen.genKeyPair();
        Object publicKey2 = (DSAPublicKey) keys.getPublic();
        Object privateKey = (DSAPrivateKey) keys.getPrivate();
        Map<String, Object> map = new HashMap<>(2);
        map.put(PUBLIC_KEY, publicKey2);
        map.put(PRIVATE_KEY, privateKey);
        return map;
    }

    public static Map<String, Object> initKey() throws Exception {
        return initKey(DEFAULT_SEED);
    }

    public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return encryptBASE64(key.getEncoded());
    }

    public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return encryptBASE64(key.getEncoded());
    }

    public static byte[] decryptBASE64(String key) throws Exception {
        return Base64.decodeBase64(key);
    }

    public static String encryptBASE64(byte[] key) throws Exception {
        return Base64.encodeBase64String(key);
    }
}
