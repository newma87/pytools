package {{Package}}.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;

/**
 * DES加密 解密算法
 * @author huangpeilin
 *
 */
public class DesUtil {

    private final static String DES = "DES";
    private final static String ENCODE = "utf-8";
    private final static String defaultKey = "sailfish_project";

    /**
     * 使用 默认key 加密
     * @param data 待加密数据
     * @return
     * @throws Exception
     */
    public static String encrypt(String data) throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        byte[] bt = encrypt(data.getBytes(ENCODE), defaultKey.getBytes(ENCODE));
        String strs = Base64.getEncoder().encodeToString(bt);
        return strs;
    }

    /**
     * 使用 默认key 解密
     * @param data 待解密数据
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static String decrypt(String data) throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        if (data == null){
            return null;
        }
        byte[] buf = Base64.getDecoder().decode(data);
        byte[] bt = decrypt(buf, defaultKey.getBytes(ENCODE));
        return new String(bt, ENCODE);
    }

    /**
     * Description 根据键值进行加密
     * @param data 待加密数据
     * @param key 密钥
     * @return
     * @throws Exception
     */
    public static String encrypt(String data, String key) throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        byte[] bt = encrypt(data.getBytes(ENCODE), key.getBytes(ENCODE));
        String strs = Base64.getEncoder().encodeToString(bt);
        return strs;
    }

    /**
     * 根据键值进行解密
     * @param data 待解密数据
     * @param key    密钥
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static String decrypt(String data, String key) throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        if (data == null){
            return null;
        }
        byte[] buf = Base64.getDecoder().decode(data);
        byte[] bt = decrypt(buf, key.getBytes(ENCODE));
        return new String(bt, ENCODE);
    }
    /**
     * Description 根据键值进行加密
     *
     * @param data
     * @param key
     *            加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] encrypt(byte[] data, byte[] key) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey secureKey = keyFactory.generateSecret(dks);

        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(DES);

        // 用密钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, secureKey, sr);

        return cipher.doFinal(data);
    }

    /**
     * Description 根据键值进行解密
     *
     * @param data
     * @param key 加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] decrypt(byte[] data, byte[] key) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();

        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey secureKey = keyFactory.generateSecret(dks);

        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(DES);

        // 用密钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, secureKey, sr);

        return cipher.doFinal(data);
    }

}