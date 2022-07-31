package org.springframework.base.system.utils.verifyCodeUtil;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.Random;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.TransactionDefinition;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/utils/verifyCodeUtil/VerifyCodeSUtil.class */
public class VerifyCodeSUtil {
    private static String randomTextCode = null;

    public static String getRandomTextCode() {
        return randomTextCode;
    }

    private static void setRandomTextCode(String randomTextCode2) {
        randomTextCode = randomTextCode2;
    }

    private static Color getRandomColor() {
        Random random = new Random();
        return new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    }

    public static BufferedImage createImageCode(int type, int length, String excludeString, int width, int height, int interLine, boolean randomPosition, boolean hasBoder, Color boderColor, Color backColor, Color fontColor, Color lineColor) {
        randomTextCode = createTextCode(type, length, excludeString);
        setRandomTextCode(randomTextCode);
        return createImageCode(randomTextCode, width, height, interLine, randomPosition, hasBoder, boderColor, backColor, fontColor, lineColor);
    }

    public static BufferedImage[] createImageCodes(int type, int length, String excludeString, int width, int height, int interLine, boolean randomPosition, boolean hasBoder, Color boderColor, Color backColor, Color fontColor, Color lineColor) {
        randomTextCode = createTextCode(type, length, excludeString);
        BufferedImage[] bImg = new BufferedImage[10];
        for (int i = 0; i < 10; i++) {
            bImg[i] = createImageCode(randomTextCode, width, height, interLine, randomPosition, hasBoder, boderColor, backColor, fontColor, lineColor);
        }
        setRandomTextCode(randomTextCode);
        return bImg;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private static String createTextCode(int type, int length, String excludeString) {
        if (length <= 0) {
            length = 4;
        }
        StringBuffer verifyCode = new StringBuffer();
        int i = 0;
        Random random = new Random();
        switch (type) {
            case 0:
                while (i < length) {
                    int t = random.nextInt(10);
                    if (StringUtils.isEmpty(excludeString) || excludeString.indexOf(String.valueOf(t)) < 0) {
                        verifyCode.append(t);
                        i++;
                    }
                }
                break;
            case 1:
                while (i < length) {
                    int t2 = random.nextInt(123);
                    if (t2 >= 97 || (t2 >= 65 && t2 <= 90)) {
                        if (StringUtils.isEmpty(excludeString) || excludeString.indexOf((char) t2) < 0) {
                            verifyCode.append((char) t2);
                            i++;
                        }
                    }
                }
                break;
            case 2:
                while (i < length) {
                    int t3 = random.nextInt(123);
                    if (t3 >= 97 || ((t3 >= 65 && t3 <= 90) || (t3 >= 48 && t3 <= 57))) {
                        if (StringUtils.isEmpty(excludeString) || excludeString.indexOf((char) t3) < 0) {
                            verifyCode.append((char) t3);
                            i++;
                        }
                    }
                }
                break;
            case 3:
                while (i < length) {
                    int t4 = random.nextInt(91);
                    if (t4 >= 65 || (t4 >= 48 && t4 <= 57)) {
                        if (StringUtils.isEmpty(excludeString) || excludeString.indexOf((char) t4) < 0) {
                            verifyCode.append((char) t4);
                            i++;
                        }
                    }
                }
                break;
            case 4:
                while (i < length) {
                    int t5 = random.nextInt(123);
                    if (t5 >= 97 || (t5 >= 48 && t5 <= 57)) {
                        if (StringUtils.isEmpty(excludeString) || excludeString.indexOf((char) t5) < 0) {
                            verifyCode.append((char) t5);
                            i++;
                        }
                    }
                }
                break;
            case TransactionDefinition.PROPAGATION_NEVER /* 5 */:
                while (i < length) {
                    int t6 = random.nextInt(91);
                    if (t6 >= 65 && (StringUtils.isEmpty(excludeString) || excludeString.indexOf((char) t6) < 0)) {
                        verifyCode.append((char) t6);
                        i++;
                    }
                }
                break;
            case TransactionDefinition.PROPAGATION_NESTED /* 6 */:
                while (i < length) {
                    int t7 = random.nextInt(123);
                    if (t7 >= 97 && (StringUtils.isEmpty(excludeString) || excludeString.indexOf((char) t7) < 0)) {
                        verifyCode.append((char) t7);
                        i++;
                    }
                }
                break;
        }
        return verifyCode.toString();
    }

    private static BufferedImage createImageCode(String textCode, int width, int height, int interLine, boolean randomPosition, boolean hasBoder, Color boderColor, Color backColor, Color fontColor, Color lineColor) {
        BufferedImage bufferedImage = new BufferedImage(width, height, 1);
        Graphics graphics = bufferedImage.getGraphics();
        graphics.setColor(backColor == null ? getRandomColor() : backColor);
        graphics.fillRect(0, 0, width, height);
        if (hasBoder) {
            graphics.setColor(boderColor == null ? getRandomColor() : boderColor);
            graphics.drawRect(0, 0, width - 1, height - 1);
        }
        Random random = new Random();
        if (interLine > 0) {
            for (int i = 0; i < interLine; i++) {
                graphics.setColor(lineColor == null ? getRandomColor() : lineColor);
                int y1 = random.nextInt(height);
                int y2 = random.nextInt(height);
                graphics.drawLine(0, y1, width, y2);
            }
        }
        int fontSize = (int) (height * 0.8d);
        int fontX = height - fontSize;
        int fontY = fontSize;
        graphics.setFont(new Font("Default", 0, fontSize));
        for (int i2 = 0; i2 < textCode.length(); i2++) {
            fontY = randomPosition ? (int) (((Math.random() * 0.3d) + 0.6d) * height) : fontY;
            graphics.setColor(fontColor == null ? getRandomColor() : fontColor);
            graphics.drawString(String.valueOf(textCode.charAt(i2)), fontX, fontY);
            fontX = (int) (fontX + (fontSize * 0.9d));
        }
        graphics.dispose();
        return bufferedImage;
    }

    public static FileInputStream createTempGifFile(int type, int length, String excludeString, int width, int height, int interLine, boolean randomPosition, boolean hasBoder, Color boderColor, Color backColor, Color fontColor, Color lineColor) {
        FileInputStream fileIn = null;
        try {
            File file = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".gif");
            AnimatedGifEncoder e = new AnimatedGifEncoder();
            e.setRepeat(0);
            e.start(file.getCanonicalPath());
            BufferedImage[] src = createImageCodes(type, length, excludeString, width, height, interLine, randomPosition, hasBoder, boderColor, backColor, fontColor, lineColor);
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
