package org.springframework.base.system.utils.verifyCodeUtil;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.Random;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/utils/verifyCodeUtil/VerifyCodeUtil.class */
public class VerifyCodeUtil {
    private static String randomTextCode = null;
    private static char[] codeSequence = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    private static int charNum = codeSequence.length;

    public static String getRandomCode() {
        return randomTextCode;
    }

    private static void setRandomCode(String randomTextCode2) {
        randomTextCode = randomTextCode2;
    }

    private static String textCode() {
        Random random = new Random();
        StringBuffer verifyCode = new StringBuffer();
        for (int i = 0; i < 4; i++) {
            String str = String.valueOf(codeSequence[random.nextInt(charNum)]);
            verifyCode.append(str);
        }
        return verifyCode.toString();
    }

    public static BufferedImage createImageCode() {
        String textCode = textCode();
        setRandomCode(textCode);
        return createImageCode(textCode);
    }

    public static BufferedImage[] createImageCodes() {
        String textCode = textCode();
        BufferedImage[] bImg = new BufferedImage[10];
        for (int i = 0; i < 10; i++) {
            bImg[i] = createImageCode(textCode);
        }
        setRandomCode(textCode);
        return bImg;
    }

    private static BufferedImage createImageCode(String textCode) {
        BufferedImage bufferedImage = new BufferedImage(100, 36, 1);
        Graphics2D createGraphics = bufferedImage.createGraphics();
        createGraphics.setColor(Color.LIGHT_GRAY);
        createGraphics.fillRect(0, 0, 100, 36);
        createGraphics.setColor(Color.BLACK);
        createGraphics.drawRect(0, 0, 100 - 1, 36 - 1);
        createGraphics.setColor(Color.gray);
        Random random = new Random();
        for (int i = 0; i < 16; i++) {
            int x1 = random.nextInt(100);
            int y1 = random.nextInt(36);
            int x2 = random.nextInt(12);
            int y2 = random.nextInt(12);
            createGraphics.drawLine(x1, y1, x1 + x2, y1 + y2);
        }
        int fontSize = (int) (36 * 0.8d);
        int fontX = (100 - 3) / (4 + 1);
        int fontY = 36 - 7;
        Font font = new Font("Fixedsys", 0, fontSize);
        createGraphics.setFont(font);
        for (int i2 = 0; i2 < textCode.length(); i2++) {
            createGraphics.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            createGraphics.drawString(String.valueOf(textCode.charAt(i2)), (i2 + 1) * fontX, fontY);
        }
        return bufferedImage;
    }

    public static FileInputStream createTempGifFile() {
        FileInputStream fileIn = null;
        try {
            File file = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".gif");
            AnimatedGifEncoder e = new AnimatedGifEncoder();
            e.setRepeat(0);
            e.start(file.getCanonicalPath());
            BufferedImage[] src = createImageCodes();
            for (BufferedImage bufferedImage : src) {
                e.setDelay(200);
                e.addFrame(bufferedImage);
            }
            e.finish();
            fileIn = new FileInputStream(file);
            file.delete();
        } catch (Exception e2) {
            System.out.println("JPG to GIF failed:" + e2.getMessage());
            e2.printStackTrace();
        }
        return fileIn;
    }
}
