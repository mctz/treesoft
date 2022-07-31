package org.springframework.base.system.utils.verifyCodeUtil;

import java.io.IOException;
import java.io.OutputStream;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/utils/verifyCodeUtil/LZWEncoder.class */
public class LZWEncoder {
    private static final int EOF = -1;
    private int imgW;
    private int imgH;
    private byte[] pixAry;
    private int initCodeSize;
    private int remaining;
    private int curPixel;
    static final int BITS = 12;
    static final int HSIZE = 5003;
    int n_bits;
    int maxcode;
    int g_init_bits;
    int ClearCode;
    int EOFCode;
    int a_count;
    int maxbits = BITS;
    int maxmaxcode = 4096;
    int[] htab = new int[HSIZE];
    int[] codetab = new int[HSIZE];
    int hsize = HSIZE;
    int free_ent = 0;
    boolean clear_flg = false;
    int cur_accum = 0;
    int cur_bits = 0;
    int[] masks = {0, 1, 3, 7, 15, 31, 63, 127, 255, 511, 1023, 2047, 4095, 8191, 16383, 32767, 65535};
    byte[] accum = new byte[256];

    public LZWEncoder(int width, int height, byte[] pixels, int color_depth) {
        this.imgW = width;
        this.imgH = height;
        this.pixAry = pixels;
        this.initCodeSize = Math.max(2, color_depth);
    }

    void char_out(byte c, OutputStream outs) throws IOException {
        byte[] bArr = this.accum;
        int i = this.a_count;
        this.a_count = i + 1;
        bArr[i] = c;
        if (this.a_count >= 254) {
            flush_char(outs);
        }
    }

    void cl_block(OutputStream outs) throws IOException {
        cl_hash(this.hsize);
        this.free_ent = this.ClearCode + 2;
        this.clear_flg = true;
        output(this.ClearCode, outs);
    }

    void cl_hash(int hsize) {
        for (int i = 0; i < hsize; i++) {
            this.htab[i] = -1;
        }
    }

    void compress(int init_bits, OutputStream outs) throws IOException {
        this.g_init_bits = init_bits;
        this.clear_flg = false;
        this.n_bits = this.g_init_bits;
        this.maxcode = MAXCODE(this.n_bits);
        this.ClearCode = 1 << (init_bits - 1);
        this.EOFCode = this.ClearCode + 1;
        this.free_ent = this.ClearCode + 2;
        this.a_count = 0;
        int ent = nextPixel();
        int hshift = 0;
        int i = this.hsize;
        while (true) {
            int fcode = i;
            if (fcode >= 65536) {
                break;
            }
            hshift++;
            i = fcode * 2;
        }
        int hshift2 = 8 - hshift;
        int hsize_reg = this.hsize;
        cl_hash(hsize_reg);
        output(this.ClearCode, outs);
        while (true) {
            int c = nextPixel();
            if (c != -1) {
                int fcode2 = (c << this.maxbits) + ent;
                int i2 = (c << hshift2) ^ ent;
                if (this.htab[i2] == fcode2) {
                    ent = this.codetab[i2];
                } else {
                    if (this.htab[i2] >= 0) {
                        int disp = hsize_reg - i2;
                        if (i2 == 0) {
                            disp = 1;
                        }
                        do {
                            int i3 = i2 - disp;
                            i2 = i3;
                            if (i3 < 0) {
                                i2 += hsize_reg;
                            }
                            if (this.htab[i2] == fcode2) {
                                ent = this.codetab[i2];
                                break;
                            }
                        } while (this.htab[i2] >= 0);
                    }
                    output(ent, outs);
                    ent = c;
                    if (this.free_ent < this.maxmaxcode) {
                        int i4 = this.free_ent;
                        this.free_ent = i4 + 1;
                        this.codetab[i2] = i4;
                        this.htab[i2] = fcode2;
                    } else {
                        cl_block(outs);
                    }
                }
            } else {
                output(ent, outs);
                output(this.EOFCode, outs);
                return;
            }
        }
    }

    public void encode(OutputStream os) throws IOException {
        os.write(this.initCodeSize);
        this.remaining = this.imgW * this.imgH;
        this.curPixel = 0;
        compress(this.initCodeSize + 1, os);
        os.write(0);
    }

    void flush_char(OutputStream outs) throws IOException {
        if (this.a_count > 0) {
            outs.write(this.a_count);
            outs.write(this.accum, 0, this.a_count);
            this.a_count = 0;
        }
    }

    final int MAXCODE(int n_bits) {
        return (1 << n_bits) - 1;
    }

    private int nextPixel() {
        if (this.remaining == 0) {
            return -1;
        }
        this.remaining--;
        byte[] bArr = this.pixAry;
        int i = this.curPixel;
        this.curPixel = i + 1;
        byte pix = bArr[i];
        return pix & 255;
    }

    void output(int code, OutputStream outs) throws IOException {
        this.cur_accum &= this.masks[this.cur_bits];
        if (this.cur_bits > 0) {
            this.cur_accum |= code << this.cur_bits;
        } else {
            this.cur_accum = code;
        }
        this.cur_bits += this.n_bits;
        while (this.cur_bits >= 8) {
            char_out((byte) (this.cur_accum & 255), outs);
            this.cur_accum >>= 8;
            this.cur_bits -= 8;
        }
        if (this.free_ent > this.maxcode || this.clear_flg) {
            if (this.clear_flg) {
                int i = this.g_init_bits;
                this.n_bits = i;
                this.maxcode = MAXCODE(i);
                this.clear_flg = false;
            } else {
                this.n_bits++;
                if (this.n_bits == this.maxbits) {
                    this.maxcode = this.maxmaxcode;
                } else {
                    this.maxcode = MAXCODE(this.n_bits);
                }
            }
        }
        if (code == this.EOFCode) {
            while (this.cur_bits > 0) {
                char_out((byte) (this.cur_accum & 255), outs);
                this.cur_accum >>= 8;
                this.cur_bits -= 8;
            }
            flush_char(outs);
        }
    }
}
