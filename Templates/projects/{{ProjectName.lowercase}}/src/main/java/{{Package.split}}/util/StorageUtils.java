package {{Package}}.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

public class StorageUtils {
    /**
     * 获取文件流的MD5值
     * @param inputStream 文件流
     * @return
     */
    public static String getMD5String(InputStream inputStream) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = inputStream.read(buffer, 0, 1024)) > 0) {
                messageDigest.update(buffer, 0, length);
            }
            inputStream.close();
            return new BigInteger(1, messageDigest.digest()).toString(16);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 下载文件时，针对不同浏览器，进行附件名的编码
     *
     * @param filename 下载文件名
     * @param agent    客户端浏览器
     * @return 编码后的下载附件名
     * @throws IOException
     */
    public static String encodeDownloadFilename(String filename, String agent)
            throws IOException {
        if (agent.contains("Firefox")) { // 火狐浏览器
            filename = "=?UTF-8?B?"
                    + new BASE64Encoder().encode(filename.getBytes("utf-8"))
                    + "?=";
            filename = filename.replaceAll("\r\n", "");
        } else { // IE及其他浏览器
            filename = URLEncoder.encode(filename, "utf-8");
            filename = filename.replace("+", " ");
        }
        return filename;
    }

    /**
     * 格式化UUID，去掉-
     *
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 获取URL地址中的KEY
     * @param url
     * @return
     */
    public static String httpUrlToKey(String url) {
        if(url.contains("url=")){
            url = url.substring(url.indexOf("url=")+4);
        }
        return url;
    }

    /**
     * 获取下载接口中的参数
     * @param downloadUrl
     * @return
     */
    public static String getDownloadParameters(String downloadUrl){
        if(downloadUrl != null && downloadUrl.contains("download")){
            downloadUrl = downloadUrl.substring(downloadUrl.indexOf("download") + 8);
        }
        return downloadUrl;
    }
}
