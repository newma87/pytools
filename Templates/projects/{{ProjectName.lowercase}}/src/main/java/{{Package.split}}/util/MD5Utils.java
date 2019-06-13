package {{Package}}.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Author: huangpeilin
 * Create at: 2018-08-28 10:52:41
 * Description:
 */
public class MD5Utils {

    /**
     * 获取文件流的MD5值
     * @param fis
     * @return
     */
    public static String getFileMD5String(MultipartFile file) {
        try {
            InputStream fis =  (FileInputStream) file.getInputStream();
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = fis.read(buffer, 0, 1024)) > 0) {
                messageDigest.update(buffer, 0, length);
            }
            fis.close();
            return new BigInteger(1, messageDigest.digest()).toString(16);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}
