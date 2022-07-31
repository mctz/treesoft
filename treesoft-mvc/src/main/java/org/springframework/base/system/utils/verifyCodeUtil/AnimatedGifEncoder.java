package org.springframework.base.system.utils.verifyCodeUtil;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/utils/verifyCodeUtil/AnimatedGifEncoder.class */
public class AnimatedGifEncoder {
    protected int width;
    protected int height;
    protected int transIndex;
    protected OutputStream out;
    protected BufferedImage image;
    protected byte[] pixels;
    protected byte[] indexedPixels;
    protected int colorDepth;
    protected byte[] colorTab;
    protected Color transparent = null;
    protected Color background = null;
    protected int repeat = -1;
    protected int delay = 0;
    protected boolean started = false;
    protected boolean[] usedEntry = new boolean[256];
    protected int palSize = 7;
    protected int dispose = -1;
    protected boolean closeStream = false;
    protected boolean firstFrame = true;
    protected boolean sizeSet = false;
    protected int sample = 10;

    public void setDelay(int ms) {
        this.delay = Math.round(ms / 10.0f);
    }

    public void setDispose(int code) {
        if (code >= 0) {
            this.dispose = code;
        }
    }

    public void setRepeat(int iter) {
        if (iter >= 0) {
            this.repeat = iter;
        }
    }

    public void setTransparent(Color c) {
        this.transparent = c;
    }

    public void setBackground(Color c) {
        this.background = c;
    }

    public boolean addFrame(BufferedImage im) {
        if (im == null || !this.started) {
            return false;
        }
        boolean ok = true;
        try {
            if (!this.sizeSet) {
                setSize(im.getWidth(), im.getHeight());
            }
            this.image = im;
            getImagePixels();
            analyzePixels();
            if (this.firstFrame) {
                writeLSD();
                writePalette();
                if (this.repeat >= 0) {
                    writeNetscapeExt();
                }
            }
            writeGraphicCtrlExt();
            writeImageDesc();
            if (!this.firstFrame) {
                writePalette();
            }
            writePixels();
            this.firstFrame = false;
        } catch (IOException e) {
            ok = false;
        }
        return ok;
    }

    public boolean finish() {
        if (!this.started) {
            return false;
        }
        boolean ok = true;
        this.started = false;
        try {
            this.out.write(59);
            this.out.flush();
            if (this.closeStream) {
                this.out.close();
            }
        } catch (IOException e) {
            ok = false;
        }
        this.transIndex = 0;
        this.out = null;
        this.image = null;
        this.pixels = null;
        this.indexedPixels = null;
        this.colorTab = null;
        this.closeStream = false;
        this.firstFrame = true;
        return ok;
    }

    public void setFrameRate(float fps) {
        if (fps != 0.0f) {
            this.delay = Math.round(100.0f / fps);
        }
    }

    public void setQuality(int quality) {
        if (quality < 1) {
            quality = 1;
        }
        this.sample = quality;
    }

    public void setSize(int w, int h) {
        if (!this.started || this.firstFrame) {
            this.width = w;
            this.height = h;
            if (this.width < 1) {
                this.width = 320;
            }
            if (this.height < 1) {
                this.height = 240;
            }
            this.sizeSet = true;
        }
    }

    public boolean start(OutputStream os) {
        if (os == null) {
            return false;
        }
        boolean ok = true;
        this.closeStream = false;
        this.out = os;
        try {
            writeString("GIF89a");
        } catch (IOException e) {
            ok = false;
        }
        boolean z = ok;
        this.started = z;
        return z;
    }

    public boolean start(String file) {
        boolean ok;
        try {
            this.out = new BufferedOutputStream(new FileOutputStream(file));
            ok = start(this.out);
            this.closeStream = true;
        } catch (IOException e) {
            ok = false;
        }
        boolean z = ok;
        this.started = z;
        return z;
    }

    public boolean isStarted() {
        return this.started;
    }

    protected void analyzePixels() {
        int len = this.pixels.length;
        int nPix = len / 3;
        this.indexedPixels = new byte[nPix];
        NeuQuant nq = new NeuQuant(this.pixels, len, this.sample);
        this.colorTab = nq.process();
        for (int i = 0; i < this.colorTab.length; i += 3) {
            byte temp = this.colorTab[i];
            this.colorTab[i] = this.colorTab[i + 2];
            this.colorTab[i + 2] = temp;
            this.usedEntry[i / 3] = false;
        }
        int k = 0;
        for (int i2 = 0; i2 < nPix; i2++) {
            int i3 = k;
            int k2 = k + 1;
            int k3 = k2 + 1;
            k = k3 + 1;
            int index = nq.map(this.pixels[i3] & 255, this.pixels[k2] & 255, this.pixels[k3] & 255);
            this.usedEntry[index] = true;
            this.indexedPixels[i2] = (byte) index;
        }
        this.pixels = null;
        this.colorDepth = 8;
        this.palSize = 7;
        if (this.transparent != null) {
            this.transIndex = findClosest(this.transparent);
        }
    }

    protected int findClosest(Color c) {
        if (this.colorTab == null) {
            return -1;
        }
        int r = c.getRed();
        int g = c.getGreen();
        int b = c.getBlue();
        int minpos = 0;
        int dmin = 16777216;
        int len = this.colorTab.length;
        int i = 0;
        while (i < len) {
            int i2 = i;
            int i3 = i + 1;
            int dr = r - (this.colorTab[i2] & 255);
            int i4 = i3 + 1;
            int dg = g - (this.colorTab[i3] & 255);
            int db = b - (this.colorTab[i4] & 255);
            int d = (dr * dr) + (dg * dg) + (db * db);
            int index = i4 / 3;
            if (this.usedEntry[index] && d < dmin) {
                dmin = d;
                minpos = index;
            }
            i = i4 + 1;
        }
        return minpos;
    }

    protected void getImagePixels() {
        int w = this.image.getWidth();
        int h = this.image.getHeight();
        int type = this.image.getType();
        if (w != this.width || h != this.height || type != 5) {
            BufferedImage temp = new BufferedImage(this.width, this.height, 5);
            Graphics2D g = temp.createGraphics();
            g.setColor(this.background);
            g.fillRect(0, 0, this.width, this.height);
            g.drawImage(this.image, 0, 0, (ImageObserver) null);
            this.image = temp;
        }
        this.pixels = this.image.getRaster().getDataBuffer().getData();
    }

    protected void writeGraphicCtrlExt() throws IOException {
        int disp;
        int transp;
        this.out.write(33);
        this.out.write(249);
        this.out.write(4);
        if (this.transparent == null) {
            transp = 0;
            disp = 0;
        } else {
            transp = 1;
            disp = 2;
        }
        if (this.dispose >= 0) {
            disp = this.dispose & 7;
        }
        this.out.write((disp << 2) | transp);
        writeShort(this.delay);
        this.out.write(this.transIndex);
        this.out.write(0);
    }

    protected void writeImageDesc() throws IOException {
        this.out.write(44);
        writeShort(0);
        writeShort(0);
        writeShort(this.width);
        writeShort(this.height);
        if (this.firstFrame) {
            this.out.write(0);
        } else {
            this.out.write(128 | this.palSize);
        }
    }

    protected void writeLSD() throws IOException {
        writeShort(this.width);
        writeShort(this.height);
        this.out.write(240 | this.palSize);
        this.out.write(0);
        this.out.write(0);
    }

    protected void writeNetscapeExt() throws IOException {
        this.out.write(33);
        this.out.write(255);
        this.out.write(11);
        writeString("NETSCAPE2.0");
        this.out.write(3);
        this.out.write(1);
        writeShort(this.repeat);
        this.out.write(0);
    }

    protected void writePalette() throws IOException {
        this.out.write(this.colorTab, 0, this.colorTab.length);
        int n = 768 - this.colorTab.length;
        for (int i = 0; i < n; i++) {
            this.out.write(0);
        }
    }

    protected void writePixels() throws IOException {
        LZWEncoder encoder = new LZWEncoder(this.width, this.height, this.indexedPixels, this.colorDepth);
        encoder.encode(this.out);
    }

    protected void writeShort(int value) throws IOException {
        this.out.write(value & 255);
        this.out.write((value >> 8) & 255);
    }

    protected void writeString(String s) throws IOException {
        for (int i = 0; i < s.length(); i++) {
            this.out.write((byte) s.charAt(i));
        }
    }
}
