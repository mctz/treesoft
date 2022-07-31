package org.springframework.base.system.utils.verifyCodeUtil;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.imageio.ImageIO;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/utils/verifyCodeUtil/VerifyCodeUtilTest.class */
public class VerifyCodeUtilTest {
    public void verifyCodeUtilTest2() {
        verifyCodeJpgToGif("E:/image/dynamic.gif");
        System.err.println("固定验证码生成类随机生成的验证码字符串(动态的)：" + VerifyCodeUtil.getRandomCode());
        try {
            File file = File.createTempFile(new StringBuilder(String.valueOf(System.currentTimeMillis())).toString(), ".gif");
            AnimatedGifEncoder e = new AnimatedGifEncoder();
            e.setRepeat(0);
            e.start(file.getCanonicalPath());
            BufferedImage[] src = VerifyCodeUtil.createImageCodes();
            for (BufferedImage bufferedImage : src) {
                e.setDelay(200);
                e.addFrame(bufferedImage);
            }
            e.finish();
            FileInputStream fileIn = new FileInputStream(file);
            fileIn.close();
            file.delete();
        } catch (Exception e2) {
            System.out.println("JPG to GIF faileE:" + e2.getMessage());
            e2.printStackTrace();
        }
    }

    public void createTempGifFile() {
        try {
            InputStream is = VerifyCodeUtil.createTempGifFile();
            OutputStream out = new FileOutputStream(new File("E:\\image\\dynamicTest.gif"), true);
            byte[] buffer = new byte[1024];
            for (int b = is.read(buffer); b != -1; b = is.read(buffer)) {
                out.write(buffer, 0, b);
            }
            is.close();
            out.close();
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void verifyCodeUtil(String path) {
        BufferedImage bufferedImage = VerifyCodeUtil.createImageCode();
        try {
            ImageIO.write(bufferedImage, "jpg", new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void verifyCodeSUtil(String path) {
        BufferedImage bufferedImage = VerifyCodeSUtil.createImageCode(2, 4, "1ILl0o", 100, 36, 5, true, false, null, new Color(238, 242, 237), new Color(0, 0, 0), null);
        try {
            ImageIO.write(bufferedImage, "jpg", new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void verifyCodeJpgToGif(String path) {
        try {
            AnimatedGifEncoder e = new AnimatedGifEncoder();
            e.setRepeat(0);
            e.start(path);
            BufferedImage[] src = VerifyCodeUtil.createImageCodes();
            for (BufferedImage bufferedImage : src) {
                e.setDelay(200);
                e.addFrame(bufferedImage);
            }
            e.finish();
        } catch (Exception e2) {
            System.out.println("JPG to GIF faileE:" + e2.getMessage());
            e2.printStackTrace();
        }
    }

    public void verifyCodeJpgToGifs(String path) {
        try {
            AnimatedGifEncoder e = new AnimatedGifEncoder();
            e.setRepeat(0);
            e.start(path);
            BufferedImage[] src = VerifyCodeSUtil.createImageCodes(2, 4, "1ILl0o", 100, 36, 5, true, false, null, new Color(238, 242, 237), new Color(0, 0, 0), null);
            for (BufferedImage bufferedImage : src) {
                e.setDelay(200);
                e.addFrame(bufferedImage);
            }
            e.finish();
        } catch (Exception e2) {
            System.out.println("JPG to GIF faileE:" + e2.getMessage());
            e2.printStackTrace();
        }
    }

    public void verifyCodeGifToJpgs(String imagePath, String jpgPath) {
        GifDecoder decoder = new GifDecoder();
        int status = decoder.read(imagePath);
        try {
            if (status != 0) {
                throw new IOException("read image " + imagePath + " error!");
            }
            int frameCount = decoder.getFrameCount();
            for (int i = 0; i < frameCount; i++) {
                BufferedImage bufferedImage = decoder.getFrame(i);
                ImageIO.write(bufferedImage, "jpg", new File(String.valueOf(jpgPath) + "\\" + i + ".jpg"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createTempGifFiles() {
        try {
            InputStream is = VerifyCodeSUtil.createTempGifFile(2, 4, "1ILl0o", 100, 36, 5, true, false, null, new Color(238, 242, 237), new Color(0, 0, 0), null);
            OutputStream out = new FileOutputStream(new File("E:\\image\\dynamicTests.gif"), true);
            byte[] buffer = new byte[1024];
            for (int b = is.read(buffer); b != -1; b = is.read(buffer)) {
                out.write(buffer, 0, b);
            }
            is.close();
            out.close();
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
