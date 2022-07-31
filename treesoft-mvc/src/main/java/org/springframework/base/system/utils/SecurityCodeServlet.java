package org.springframework.base.system.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.base.system.utils.verifyCodeUtil.AnimatedGifEncoder;
import org.springframework.base.system.utils.verifyCodeUtil.VerifyCodeUtil;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/utils/SecurityCodeServlet.class */
public class SecurityCodeServlet extends HttpServlet {
    private Logger log;

    public SecurityCodeServlet() {
        this.log = null;
        this.log = Logger.getRootLogger();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("image/gif");
        createImage(request, response);
    }

    private void createImage(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0L);
        FileInputStream fileIn = null;
        InputStream is = null;
        File file = null;
        try {
            file = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".gif");
            AnimatedGifEncoder e = new AnimatedGifEncoder();
            e.setRepeat(0);
            e.start(file.getCanonicalPath());
            BufferedImage[] src = VerifyCodeUtil.createImageCodes();
            request.getSession().setAttribute("KAPTCHA_SESSION_KEY", VerifyCodeUtil.getRandomCode().toLowerCase());
            for (BufferedImage bufferedImage : src) {
                e.setDelay(200);
                e.addFrame(bufferedImage);
            }
            e.finish();
            fileIn = new FileInputStream(file);
            is = fileIn;
        } catch (Exception e2) {
            this.log.error("SecurityCodeSevlet.createImage() : Failed : " + e2.getMessage());
        }
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] buffer = new byte[1024];
            for (int b = is.read(buffer); b != -1; b = is.read(buffer)) {
                outputStream.write(buffer, 0, b);
            }
            is.close();
            outputStream.close();
            fileIn.close();
            file.delete();
        } catch (IOException e3) {
            this.log.error("SecurityCodeSevlet.createImage() : Failed : " + e3.getMessage());
        }
    }
}
