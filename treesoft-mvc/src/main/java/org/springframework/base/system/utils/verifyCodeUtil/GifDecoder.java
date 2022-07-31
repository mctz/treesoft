package org.springframework.base.system.utils.verifyCodeUtil;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/utils/verifyCodeUtil/GifDecoder.class */
public class GifDecoder {
    public static final int STATUS_OK = 0;
    public static final int STATUS_FORMAT_ERROR = 1;
    public static final int STATUS_OPEN_ERROR = 2;
    protected BufferedInputStream in;
    protected int status;
    protected int width;
    protected int height;
    protected boolean gctFlag;
    protected int gctSize;
    protected int[] gct;
    protected int[] lct;
    protected int[] act;
    protected int bgIndex;
    protected int bgColor;
    protected int lastBgColor;
    protected int pixelAspect;
    protected boolean lctFlag;
    protected boolean interlace;
    protected int lctSize;
    protected int ix;
    protected int iy;
    protected int iw;
    protected int ih;
    protected Rectangle lastRect;
    protected BufferedImage image;
    protected BufferedImage lastImage;
    protected int transIndex;
    protected static final int MaxStackSize = 4096;
    protected short[] prefix;
    protected byte[] suffix;
    protected byte[] pixelStack;
    protected byte[] pixels;
    protected ArrayList<GifFrame> frames;
    protected int frameCount;
    protected int loopCount = 1;
    protected byte[] block = new byte[256];
    protected int blockSize = 0;
    protected int dispose = 0;
    protected int lastDispose = 0;
    protected boolean transparency = false;
    protected int delay = 0;

    /* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/utils/verifyCodeUtil/GifDecoder$GifFrame.class */
    public static class GifFrame {
        public BufferedImage image;
        public int delay;

        public GifFrame(BufferedImage im, int del) {
            this.image = im;
            this.delay = del;
        }
    }

    public int getDelay(int n) {
        this.delay = -1;
        if (n >= 0 && n < this.frameCount) {
            this.delay = this.frames.get(n).delay;
        }
        return this.delay;
    }

    public int getFrameCount() {
        return this.frameCount;
    }

    public BufferedImage getImage() {
        return getFrame(0);
    }

    public int getLoopCount() {
        return this.loopCount;
    }

    protected void setPixels() {
        Color c;
        int[] dest = this.image.getRaster().getDataBuffer().getData();
        if (this.lastDispose > 0) {
            if (this.lastDispose == 3) {
                int n = this.frameCount - 2;
                if (n > 0) {
                    this.lastImage = getFrame(n - 1);
                } else {
                    this.lastImage = null;
                }
            }
            if (this.lastImage != null) {
                int[] prev = this.lastImage.getRaster().getDataBuffer().getData();
                System.arraycopy(prev, 0, dest, 0, this.width * this.height);
                if (this.lastDispose == 2) {
                    Graphics2D g = this.image.createGraphics();
                    if (this.transparency) {
                        c = new Color(0, 0, 0, 0);
                    } else {
                        c = new Color(this.lastBgColor);
                    }
                    g.setColor(c);
                    g.setComposite(AlphaComposite.Src);
                    g.fill(this.lastRect);
                    g.dispose();
                }
            }
        }
        int pass = 1;
        int inc = 8;
        int iline = 0;
        for (int i = 0; i < this.ih; i++) {
            int line = i;
            if (this.interlace) {
                if (iline >= this.ih) {
                    pass++;
                    switch (pass) {
                        case 2:
                            iline = 4;
                            break;
                        case 3:
                            iline = 2;
                            inc = 4;
                            break;
                        case 4:
                            iline = 1;
                            inc = 2;
                            break;
                    }
                }
                line = iline;
                iline += inc;
            }
            int line2 = line + this.iy;
            if (line2 < this.height) {
                int k = line2 * this.width;
                int dx = k + this.ix;
                int dlim = dx + this.iw;
                if (k + this.width < dlim) {
                    dlim = k + this.width;
                }
                int sx = i * this.iw;
                while (dx < dlim) {
                    int i2 = sx;
                    sx++;
                    int index = this.pixels[i2] & 255;
                    int c2 = this.act[index];
                    if (c2 != 0) {
                        dest[dx] = c2;
                    }
                    dx++;
                }
            }
        }
    }

    public BufferedImage getFrame(int n) {
        BufferedImage im = null;
        if (n >= 0 && n < this.frameCount) {
            im = this.frames.get(n).image;
        }
        return im;
    }

    public Dimension getFrameSize() {
        return new Dimension(this.width, this.height);
    }

    public int read(BufferedInputStream is) {
        init();
        if (is != null) {
            this.in = is;
            readHeader();
            if (!err()) {
                readContents();
                if (this.frameCount < 0) {
                    this.status = 1;
                }
            }
        } else {
            this.status = 2;
        }
        try {
            is.close();
        } catch (IOException e) {
        }
        return this.status;
    }

    public int read(InputStream is) {
        init();
        if (is != null) {
            if (!(is instanceof BufferedInputStream)) {
                is = new BufferedInputStream(is);
            }
            this.in = (BufferedInputStream) is;
            readHeader();
            if (!err()) {
                readContents();
                if (this.frameCount < 0) {
                    this.status = 1;
                }
            }
        } else {
            this.status = 2;
        }
        try {
            is.close();
        } catch (IOException e) {
        }
        return this.status;
    }

    public int read(String name) {
        this.status = 0;
        try {
            String name2 = name.trim().toLowerCase();
            if (name2.indexOf("file:") >= 0 || name2.indexOf(":/") > 0) {
                URL url = new URL(name2);
                this.in = new BufferedInputStream(url.openStream());
            } else {
                this.in = new BufferedInputStream(new FileInputStream(name2));
            }
            this.status = read(this.in);
        } catch (IOException e) {
            this.status = 2;
        }
        return this.status;
    }

    protected void decodeImageData() {
        int npix = this.iw * this.ih;
        if (this.pixels == null || this.pixels.length < npix) {
            this.pixels = new byte[npix];
        }
        if (this.prefix == null) {
            this.prefix = new short[MaxStackSize];
        }
        if (this.suffix == null) {
            this.suffix = new byte[MaxStackSize];
        }
        if (this.pixelStack == null) {
            this.pixelStack = new byte[4097];
        }
        int data_size = read();
        int clear = 1 << data_size;
        int end_of_information = clear + 1;
        int available = clear + 2;
        int old_code = -1;
        int code_size = data_size + 1;
        int code_mask = (1 << code_size) - 1;
        for (int code = 0; code < clear; code++) {
            this.prefix[code] = 0;
            this.suffix[code] = (byte) code;
        }
        int bi = 0;
        int pi = 0;
        int top = 0;
        int first = 0;
        int count = 0;
        int bits = 0;
        int datum = 0;
        int i = 0;
        while (i < npix) {
            if (top == 0) {
                if (bits < code_size) {
                    if (count == 0) {
                        count = readBlock();
                        if (count <= 0) {
                            break;
                        }
                        bi = 0;
                    }
                    datum += (this.block[bi] & 255) << bits;
                    bits += 8;
                    bi++;
                    count--;
                } else {
                    int code2 = datum & code_mask;
                    datum >>= code_size;
                    bits -= code_size;
                    if (code2 > available || code2 == end_of_information) {
                        break;
                    } else if (code2 != clear) {
                        if (old_code == -1) {
                            int i2 = top;
                            top++;
                            this.pixelStack[i2] = this.suffix[code2];
                            old_code = code2;
                            first = code2;
                        } else {
                            if (code2 == available) {
                                int i3 = top;
                                top++;
                                this.pixelStack[i3] = (byte) first;
                                code2 = old_code;
                            }
                            while (code2 > clear) {
                                int i4 = top;
                                top++;
                                this.pixelStack[i4] = this.suffix[code2];
                                code2 = this.prefix[code2];
                            }
                            first = this.suffix[code2] & 255;
                            if (available >= MaxStackSize) {
                                int i5 = top;
                                top++;
                                this.pixelStack[i5] = (byte) first;
                            } else {
                                int i6 = top;
                                top++;
                                this.pixelStack[i6] = (byte) first;
                                this.prefix[available] = (short) old_code;
                                this.suffix[available] = (byte) first;
                                available++;
                                if ((available & code_mask) == 0 && available < MaxStackSize) {
                                    code_size++;
                                    code_mask += available;
                                }
                                old_code = code2;
                            }
                        }
                    } else {
                        code_size = data_size + 1;
                        code_mask = (1 << code_size) - 1;
                        available = clear + 2;
                        old_code = -1;
                    }
                }
            }
            top--;
            int i7 = pi;
            pi++;
            this.pixels[i7] = this.pixelStack[top];
            i++;
        }
        for (int i8 = pi; i8 < npix; i8++) {
            this.pixels[i8] = 0;
        }
    }

    protected boolean err() {
        return this.status != 0;
    }

    protected void init() {
        this.status = 0;
        this.frameCount = 0;
        this.frames = new ArrayList<>();
        this.gct = null;
        this.lct = null;
    }

    protected int read() {
        int curByte = 0;
        try {
            curByte = this.in.read();
        } catch (IOException e) {
            this.status = 1;
        }
        return curByte;
    }

    protected int readBlock() {
        this.blockSize = read();
        int n = 0;
        if (this.blockSize > 0) {
            while (n < this.blockSize) {
                try {
                    int count = this.in.read(this.block, n, this.blockSize - n);
                    if (count == -1) {
                        break;
                    }
                    n += count;
                } catch (IOException e) {
                }
            }
            if (n < this.blockSize) {
                this.status = 1;
            }
        }
        return n;
    }

    protected int[] readColorTable(int ncolors) {
        int nbytes = 3 * ncolors;
        int[] tab = null;
        byte[] c = new byte[nbytes];
        int n = 0;
        try {
            n = this.in.read(c);
        } catch (IOException e) {
        }
        if (n < nbytes) {
            this.status = 1;
        } else {
            tab = new int[256];
            int i = 0;
            int j = 0;
            while (i < ncolors) {
                int i2 = j;
                int j2 = j + 1;
                int r = c[i2] & 255;
                int j3 = j2 + 1;
                int g = c[j2] & 255;
                j = j3 + 1;
                int b = c[j3] & 255;
                int i3 = i;
                i++;
                tab[i3] = (-16777216) | (r << 16) | (g << 8) | b;
            }
        }
        return tab;
    }

    protected void readContents() {
        boolean done = false;
        while (!done && !err()) {
            int code = read();
            switch (code) {
                case 0:
                    break;
                case 33:
                    int code2 = read();
                    switch (code2) {
                        case 249:
                            readGraphicControlExt();
                            continue;
                        case 255:
                            readBlock();
                            String app = "";
                            for (int i = 0; i < 11; i++) {
                                app = String.valueOf(app) + ((char) this.block[i]);
                            }
                            if (app.equals("NETSCAPE2.0")) {
                                readNetscapeExt();
                                continue;
                            } else {
                                skip();
                                break;
                            }
                        default:
                            skip();
                            continue;
                    }
                case 44:
                    readImage();
                    break;
                case 59:
                    done = true;
                    break;
                default:
                    this.status = 1;
                    break;
            }
        }
    }

    protected void readGraphicControlExt() {
        read();
        int packed = read();
        this.dispose = (packed & 28) >> 2;
        if (this.dispose == 0) {
            this.dispose = 1;
        }
        this.transparency = (packed & 1) != 0;
        this.delay = readShort() * 10;
        this.transIndex = read();
        read();
    }

    protected void readHeader() {
        String id = "";
        for (int i = 0; i < 6; i++) {
            id = String.valueOf(id) + ((char) read());
        }
        if (!id.startsWith("GIF")) {
            this.status = 1;
            return;
        }
        readLSD();
        if (this.gctFlag && !err()) {
            this.gct = readColorTable(this.gctSize);
            this.bgColor = this.gct[this.bgIndex];
        }
    }

    protected void readImage() {
        this.ix = readShort();
        this.iy = readShort();
        this.iw = readShort();
        this.ih = readShort();
        int packed = read();
        this.lctFlag = (packed & 128) != 0;
        this.interlace = (packed & 64) != 0;
        this.lctSize = 2 << (packed & 7);
        if (this.lctFlag) {
            this.lct = readColorTable(this.lctSize);
            this.act = this.lct;
        } else {
            this.act = this.gct;
            if (this.bgIndex == this.transIndex) {
                this.bgColor = 0;
            }
        }
        int save = 0;
        if (this.transparency) {
            save = this.act[this.transIndex];
            this.act[this.transIndex] = 0;
        }
        if (this.act == null) {
            this.status = 1;
        }
        if (err()) {
            return;
        }
        decodeImageData();
        skip();
        if (err()) {
            return;
        }
        this.frameCount++;
        this.image = new BufferedImage(this.width, this.height, 3);
        setPixels();
        this.frames.add(new GifFrame(this.image, this.delay));
        if (this.transparency) {
            this.act[this.transIndex] = save;
        }
        resetFrame();
    }

    protected void readLSD() {
        this.width = readShort();
        this.height = readShort();
        int packed = read();
        this.gctFlag = (packed & 128) != 0;
        this.gctSize = 2 << (packed & 7);
        this.bgIndex = read();
        this.pixelAspect = read();
    }

    protected void readNetscapeExt() {
        do {
            readBlock();
            if (this.block[0] == 1) {
                int b1 = this.block[1] & 255;
                int b2 = this.block[2] & 255;
                this.loopCount = (b2 << 8) | b1;
            }
            if (this.blockSize <= 0) {
                return;
            }
        } while (!err());
    }

    protected int readShort() {
        return read() | (read() << 8);
    }

    protected void resetFrame() {
        this.lastDispose = this.dispose;
        this.lastRect = new Rectangle(this.ix, this.iy, this.iw, this.ih);
        this.lastImage = this.image;
        this.lastBgColor = this.bgColor;
        this.lct = null;
    }

    protected void skip() {
        do {
            readBlock();
            if (this.blockSize <= 0) {
                return;
            }
        } while (!err());
    }
}
