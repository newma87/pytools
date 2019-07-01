package {{Package}}.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.text.SimpleDateFormat;

/**
 * Author: huangpeilin
 * Create at: 2018-11-20 15:46:24
 * Description:
 *
 * @author huangpeilin
 */
public class XXTEA {

    static final Logger log = LoggerFactory.getLogger(XXTEA.class);

    public static int[] encryptKeys = {
            0x00543210, 0x00543211,
            0x00543212, 0x00543213
    };


    //===================== XXTEA加/解密 来自于厂家提供的代码 ===================
    public static int[] encrypt(int[] src, int[] keys, int srcLength) {
        return XXTEA.encrypt(src, keys, srcLength, 6);
    }

    /**
     * 加密算法，门禁二维码用，以及图片加密用。
     * @param src
     * @param keys  加密密钥：{ 0x00543210, 0x00543210, 0x00543210, 0x00543210 };
     * @param srcLength
     * @param base_round　加密图片时为了效率可以设置为1，默认为6
     * @return
     */
    public static int[] encrypt(int[] src, int[] keys, int srcLength, int base_round) {
        int y;
        int p;
        int rounds = base_round + 52/srcLength;
        int sum = 0;
        int z = src[srcLength-1];
        int delta = 0x9E3779B9;
        do {
            sum += delta;
            int e = (sum >>> 2) & 3;
            for (p=0; p<srcLength-1; p++) {
                y = src[p+1];
                z = src[p] += (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (keys[p & 3 ^ e] ^ z);
            }
            y = src[0];
            z = src[srcLength-1] += (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (keys[p & 3 ^ e] ^ z);
        } while (--rounds > 0);

        return src;

    }

    public static int[] decrypt(int[] src, int[] keys, int srcLength) {
        return XXTEA.decrypt(src, keys, srcLength, 6);
    }

    public static int[] decrypt(int[] src, int[] keys, int srcLength, int base_round) {
        //int n = v.length;
        int z = src[srcLength - 1], y = src[0], delta = 0x9E3779B9, sum, e;
        int p;
        int rounds = base_round + 52/srcLength;
        sum = rounds*delta;
        y = src[0];
        do {
            e = (sum >>> 2) & 3;
            for (p=srcLength-1; p>0; p--) {
                z = src[p-1];
                y = src[p] -= (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (keys[p & 3 ^ e] ^ z);
            }
            z = src[srcLength-1];
            y = src[0] -= (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (keys[p & 3 ^ e] ^ z);
        } while ((sum -= delta) != 0);
        return src;
    }
}

