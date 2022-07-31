package org.springframework.base.system.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/utils/StringUtil.class */
public class StringUtil extends StringUtils {
    public static String lowerFirst(String str) {
        if (isBlank(str)) {
            return "";
        }
        return String.valueOf(str.substring(0, 1).toLowerCase()) + str.substring(1);
    }

    public static String upperFirst(String str) {
        if (isBlank(str)) {
            return "";
        }
        return String.valueOf(str.substring(0, 1).toUpperCase()) + str.substring(1);
    }

    public static String replaceHtml(String html) {
        if (isBlank(html)) {
            return "";
        }
        Pattern p = Pattern.compile("<.+?>");
        Matcher m = p.matcher(html);
        String s = m.replaceAll("");
        return s;
    }

    public static String abbr(String str, int length) {
        if (str == null) {
            return "";
        }
        try {
            StringBuilder sb = new StringBuilder();
            int currentLength = 0;
            char[] charArray = replaceHtml(StringEscapeUtils.unescapeHtml4(str)).toCharArray();
            int length2 = charArray.length;
            int i = 0;
            while (true) {
                if (i < length2) {
                    char c = charArray[i];
                    currentLength += String.valueOf(c).getBytes("GBK").length;
                    if (currentLength <= length - 3) {
                        sb.append(c);
                        i++;
                    } else {
                        sb.append("...");
                        break;
                    }
                } else {
                    break;
                }
            }
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String rabbr(String str, int length) {
        return abbr(replaceHtml(str), length);
    }

    public static Double toDouble(Object val) {
        if (val == null) {
            return Double.valueOf(0.0d);
        }
        try {
            return Double.valueOf(trim(val.toString()));
        } catch (Exception e) {
            return Double.valueOf(0.0d);
        }
    }

    public static Float toFloat(Object val) {
        return Float.valueOf(toDouble(val).floatValue());
    }

    public static Long toLong(Object val) {
        return Long.valueOf(toDouble(val).longValue());
    }

    public static Integer toInteger(Object val) {
        return Integer.valueOf(toLong(val).intValue());
    }

    public static final String MD5(String s) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] strTemp = s.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (byte b : md) {
                int i = k;
                int k2 = k + 1;
                str[i] = hexDigits[(b >> 4) & 15];
                k = k2 + 1;
                str[k2] = hexDigits[b & 15];
            }
            return new String(str);
        } catch (Exception e) {
            return "";
        }
    }

    public static String getUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll(RuleBasedTransactionAttribute.PREFIX_ROLLBACK_RULE, "");
    }
}
