package {{Package}}.util;

import org.springframework.util.Assert;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.List;

/**
 * 字符串处理帮助类
 */
public class StringUtil {
	/**
	 * 字符串为null时转换为空字符串
	 */
	public static String blankIfNull(String str) {
		return str != null ? str : "";
	}
	
	/**
	 * 将字符串编码强制转换为UTF8
	 */
	public static String convertToUTF8(String str) {
		String result = null;
		try {
			result = new String(str.getBytes(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 针对表示数字的字符串，补完到需要的字符串长度
	 * @param str	  表示数字的字符串（进制不限）， 如： "00001234","0A0CD200"
	 * @param length  指定要生成的字符串长度
	 */
	public static String fixStringLength(String str, int length) {
    	Assert.hasLength(str, "message argument must not be null");
    	
        for (; length - str.length() > 0; ) {
            str = "0" + str;
        }

        if (str.length() - length > 0) {
            str = str.substring(str.length() - length);
        }

        return str;
    }

	/**
	 * 将数值转成固定长度的字符串，注意此函数会产生截短而出现数值损失
	 */
    public static String decimalToHexString(long dec, int fixedLength) {
        String result = Long.toHexString(dec);
        result = fixStringLength(result, fixedLength);
        return result.toUpperCase();
	}
	
	/**
	 * 十六进制的数字字符串转换为数字, 注意：字符串长度最好不要大于8;超过时，只取字符串的前面8位, 如 "00AB134D0010" 实际转的是 "00AB134D"
	 * @param hex  十六进制的数字字符串，如： "00AB134D"
	 * @return 	   返回相应的int值
	 */
	public static int hexStringToInt(String hex) {
    	Assert.notNull(hex, "hex argument must not be null");
        if (hex.length() < 8) {
            hex = StringUtil.fixStringLength(hex, 8);
        }
        String hVal = hex.substring(0, 4);
        String lVal = hex.substring(4, 8);
        int hInt = Integer.valueOf(hVal, 16);
        int lInt = Integer.valueOf(lVal, 16);

        return (hInt << 16) | lInt;
    }

	/**
	 * 将一串十六进制的数字字符串转换成一组int数组, 字符串按每8位转换成int。最后一个不满8位时在前面补0然后再转成int. 如 "12ADC" 将转成 "00012ADC"
	 */
    public static int[] hexStringToIntArray(String hex, int count) {
    	Assert.notNull(hex, "hex argument must not be null!");
        Assert.isTrue(((count - 1) * 8) < hex.length(), "Bad argument of count");

        int[] result = new int[count];
        for (int i = 0; i < count; i++) {
            result[i] = hexStringToInt(hex.substring(i * 8));
        }
        return result;
    }

    public static int[] toIntArray(byte buf[])
    {
        return toIntArray(buf, buf.length);
    }

    public static int[] toIntArray(byte buf[], int length)
    {
        IntBuffer intBuf = ByteBuffer.wrap(buf, 0, length)
                                    .order(ByteOrder.BIG_ENDIAN)
                                    .asIntBuffer();
        int[] ret = new int[intBuf.remaining()];
        intBuf.get(ret);
        return ret;
    }

    public static byte[] toByteArray(int[] ints)
    {
        ByteBuffer buf = ByteBuffer.allocate(ints.length * 4)
            .order(ByteOrder.BIG_ENDIAN);
        buf.asIntBuffer().put(ints);
        return buf.array();
    }

    /**
     * 将List转为String
     * @param mList
     * @return
     */
    public static String listToString(List<String> mList) {
        final String SEPARATOR = ",";
        StringBuilder sb = new StringBuilder();
        String convertedListStr = "";
        if (null != mList && mList.size() > 0) {
            for (String item : mList) {
                sb.append(item);
                sb.append(SEPARATOR);
            }
            convertedListStr = sb.toString();
            convertedListStr = convertedListStr.substring(0, convertedListStr.length()
                    - SEPARATOR.length());
            return convertedListStr;
        } else {
            return null;
        }
    }
    
}
