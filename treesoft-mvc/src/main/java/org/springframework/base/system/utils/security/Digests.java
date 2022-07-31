package org.springframework.base.system.utils.security;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import org.apache.commons.lang3.Validate;
import org.springframework.base.system.utils.Exceptions;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/utils/security/Digests.class */
public class Digests {
    private static final String SHA1 = "SHA-1";
    private static final String MD5 = "MD5";
    private static SecureRandom random = new SecureRandom();

    public static byte[] sha1(byte[] input) {
        return digest(input, SHA1, null, 1);
    }

    public static byte[] sha1(byte[] input, byte[] salt) {
        return digest(input, SHA1, salt, 1);
    }

    public static byte[] sha1(byte[] input, byte[] salt, int iterations) {
        return digest(input, SHA1, salt, iterations);
    }

    private static byte[] digest(byte[] input, String algorithm, byte[] salt, int iterations) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            if (salt != null) {
                digest.update(salt);
            }
            byte[] result = digest.digest(input);
            for (int i = 1; i < iterations; i++) {
                digest.reset();
                result = digest.digest(result);
            }
            return result;
        } catch (GeneralSecurityException e) {
            throw Exceptions.unchecked(e);
        }
    }

    public static byte[] generateSalt(int numBytes) {
        Validate.isTrue(numBytes > 0, "numBytes argument must be a positive integer (1 or larger)", numBytes);
        byte[] bytes = new byte[numBytes];
        random.nextBytes(bytes);
        return bytes;
    }

    public static byte[] md5(InputStream input) throws IOException {
        return digest(input, MD5);
    }

    public static byte[] sha1(InputStream input) throws IOException {
        return digest(input, SHA1);
    }

    private static byte[] digest(InputStream input, String algorithm) throws IOException {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            byte[] buffer = new byte[8192];
            int read = input.read(buffer, 0, 8192);
            while (read > -1) {
                messageDigest.update(buffer, 0, read);
                read = input.read(buffer, 0, 8192);
            }
            return messageDigest.digest();
        } catch (GeneralSecurityException e) {
            throw Exceptions.unchecked(e);
        }
    }
}
