package org.springframework.base.system.utils;

import java.security.MessageDigest;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/utils/MD5Utils.class */
public class MD5Utils {
    private static final String[] hexDigIts = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public static String MD5Encode(String origin) {
        String resultString = null;
        try {
            String resultString2 = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if ("utf8" == 0 || "".equals("utf8")) {
                resultString = byteArrayToHexString(md.digest(resultString2.getBytes()));
            } else {
                resultString = byteArrayToHexString(md.digest(resultString2.getBytes("utf8")));
            }
        } catch (Exception e) {
        }
        return resultString;
    }

    public static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (byte b2 : b) {
            resultSb.append(byteToHexString(b2));
        }
        return resultSb.toString();
    }

    public static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return String.valueOf(hexDigIts[d1]) + hexDigIts[d2];
    }

    public static void main(String[] args) throws Exception {
        String validToken = MD5Encode(String.valueOf("e96281f222072f8bf6e730d41b42cb30") + "福州青格软件有限公司treesoft");
        System.out.println("用户名=福州青格软件有限公司");
        System.out.println("CODE=" + validToken);
    }
}
