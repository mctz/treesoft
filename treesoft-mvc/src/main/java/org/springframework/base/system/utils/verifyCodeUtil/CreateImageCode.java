package org.springframework.base.system.utils.verifyCodeUtil;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/utils/verifyCodeUtil/CreateImageCode.class */
public class CreateImageCode extends HttpServlet {
    private static final long serialVersionUID = 8484577524665768986L;
    private int width;
    private int height;
    private int codeCount;
    private int lineCount;
    private String code;
    private BufferedImage buffImg;
    Random random;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("image/jpeg");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0L);
        CreateImageCode vCode = new CreateImageCode(100, 30, 5, 10);
        request.getSession().setAttribute("KAPTCHA_SESSION_KEY", vCode.getCode().toLowerCase());
        vCode.write(response.getOutputStream());
    }

    public CreateImageCode() {
        this.width = 160;
        this.height = 40;
        this.codeCount = 1;
        this.lineCount = 20;
        this.code = null;
        this.buffImg = null;
        this.random = new Random();
        creatImage();
    }

    public CreateImageCode(int width, int height) {
        this.width = 160;
        this.height = 40;
        this.codeCount = 1;
        this.lineCount = 20;
        this.code = null;
        this.buffImg = null;
        this.random = new Random();
        this.width = width;
        this.height = height;
        creatImage();
    }

    public CreateImageCode(int width, int height, int codeCount) {
        this.width = 160;
        this.height = 40;
        this.codeCount = 1;
        this.lineCount = 20;
        this.code = null;
        this.buffImg = null;
        this.random = new Random();
        this.width = width;
        this.height = height;
        this.codeCount = codeCount;
        creatImage();
    }

    public CreateImageCode(int width, int height, int codeCount, int lineCount) {
        this.width = 160;
        this.height = 40;
        this.codeCount = 1;
        this.lineCount = 20;
        this.code = null;
        this.buffImg = null;
        this.random = new Random();
        this.width = width;
        this.height = height;
        this.codeCount = codeCount;
        this.lineCount = lineCount;
        creatImage();
    }

    private void creatImage() {
        int fontWidth = this.width / this.codeCount;
        int fontHeight = this.height - 5;
        int codeY = this.height - 8;
        this.buffImg = new BufferedImage(this.width, this.height, 1);
        Graphics g = this.buffImg.getGraphics();
        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, this.width, this.height);
        Font font = new Font("Fixedsys", 1, fontHeight);
        g.setFont(font);
        for (int i = 0; i < this.lineCount; i++) {
            int xs = this.random.nextInt(this.width);
            int ys = this.random.nextInt(this.height);
            int xe = xs + this.random.nextInt(this.width);
            int ye = ys + this.random.nextInt(this.height);
            g.setColor(getRandColor(1, 255));
            g.drawLine(xs, ys, xe, ye);
        }
        int area = (int) (0.01f * this.width * this.height);
        for (int i2 = 0; i2 < area; i2++) {
            int x = this.random.nextInt(this.width);
            int y = this.random.nextInt(this.height);
            this.buffImg.setRGB(x, y, this.random.nextInt(255));
        }
        String str1 = randomStr(this.codeCount);
        this.code = str1;
        for (int i3 = 0; i3 < this.codeCount; i3++) {
            String strRand = str1.substring(i3, i3 + 1);
            g.setColor(getRandColor(1, 255));
            g.drawString(strRand, (i3 * fontWidth) + 3, codeY);
        }
    }

    private String randomStr(int n) {
        String str2 = "";
        int len = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890".length() - 1;
        for (int i = 0; i < n; i++) {
            double r = Math.random() * len;
            str2 = String.valueOf(str2) + "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890".charAt((int) r);
        }
        return str2;
    }

    private Color getRandColor(int fc, int bc) {
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + this.random.nextInt(bc - fc);
        int g = fc + this.random.nextInt(bc - fc);
        int b = fc + this.random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    private Font getFont(int size) {
        Random random = new Random();
        Font[] font = {new Font("Ravie", 0, size), new Font("Antique Olive Compact", 0, size), new Font("Fixedsys", 0, size), new Font("Wide Latin", 0, size), new Font("Gill Sans Ultra Bold", 0, size)};
        return font[random.nextInt(5)];
    }

    private void shear(Graphics g, int w1, int h1, Color color) {
        shearX(g, w1, h1, color);
        shearY(g, w1, h1, color);
    }

    private void shearX(Graphics g, int w1, int h1, Color color) {
        int period = this.random.nextInt(2);
        int phase = this.random.nextInt(2);
        for (int i = 0; i < h1; i++) {
            double d = (period >> 1) * Math.sin((i / period) + ((6.283185307179586d * phase) / 1));
            g.copyArea(0, i, w1, 1, (int) d, 0);
            if (1 != 0) {
                g.setColor(color);
                g.drawLine((int) d, i, 0, i);
                g.drawLine(((int) d) + w1, i, w1, i);
            }
        }
    }

    private void shearY(Graphics g, int w1, int h1, Color color) {
        int period = this.random.nextInt(40) + 10;
        for (int i = 0; i < w1; i++) {
            double d = (period >> 1) * Math.sin((i / period) + ((6.283185307179586d * 7) / 20));
            g.copyArea(i, 0, 1, h1, 0, (int) d);
            if (1 != 0) {
                g.setColor(color);
                g.drawLine(i, (int) d, i, 0);
                g.drawLine(i, ((int) d) + h1, i, h1);
            }
        }
    }

    public void write(OutputStream sos) throws IOException {
        ImageIO.write(this.buffImg, "png", sos);
        sos.close();
    }

    public BufferedImage getBuffImg() {
        return this.buffImg;
    }

    public String getCode() {
        return this.code.toLowerCase();
    }

    public void getCode3(HttpServletRequest req, HttpServletResponse response, HttpSession session) throws IOException {
        response.setContentType("image/jpeg");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0L);
        CreateImageCode vCode = new CreateImageCode(100, 30, 5, 10);
        session.setAttribute("KAPTCHA_SESSION_KEY", vCode.getCode().toLowerCase());
        vCode.write(response.getOutputStream());
    }
}
