package {{Package}}.util;

/**
 * @author admin
 * @ClassName: MothedUtil
 * @Description: @Description
 * @date 2018/3/19 11:32
 */
public class MethodUtil {

    /**
     *  获取调用这个方法的方法名
     * @return 方法名 如"getMethodName :"
     */
    public static String getCurrentMethodName() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        return stackTrace[2].getMethodName() + ": ";
    }
}
