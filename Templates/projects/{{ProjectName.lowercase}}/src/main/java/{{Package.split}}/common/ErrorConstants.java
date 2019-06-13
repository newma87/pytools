package {{Package}}.common;

/**
 * Author: newma<newma@live.cn>
 * Create: 2017/12/16 10:55
 * Description: 错误代码类
 * @author lin
 */

public class ErrorConstants {

    //========================= 全局错误码 =============================

    public static final int SUCCESS = 0x00000000;

    public static final int ERR_BAD_PARAM = 0x00000001;

    public static final int ERR_FORM_PARAMETER_ERROR = 0x00000002;

    public static final int ERR_INTERNAL_EXCEPTION = 0xfffffffE;

    public static final int ERR_UNKNOWN = 0xffffffff;


    //========================= 模块错误码 ===============================
    //                   模块定义从0x00010000开始

    /**
     * 用户模块
     */
    private static final int USER_BASE_ERROR_SEGMENT = 0x00010000;

    //========================= 用户模块 =================================

    /**
     * 用户不存在
     */
    public static final int USER_IS_NOT_EXISTS = USER_BASE_ERROR_SEGMENT | 1;

    /**
     * 用户登录名为空
     */
    public static final int USER_LOGIN_NAME_IS_NULL = USER_BASE_ERROR_SEGMENT | 2;

    /**
     * 用户名为空
     */
    public static final int USER_USERNAME_IS_NULL = USER_BASE_ERROR_SEGMENT | 3;

    /**
     * 用户工号为空
     */
    public static final int USER_JOB_NUMBER_IS_NULL = USER_BASE_ERROR_SEGMENT | 4;

    /**
     * 用户登录名已存在
     */
    public static final int USER_LOGIN_NAME_IS_ALREADY_EXISTS = USER_BASE_ERROR_SEGMENT | 5;

    /**
     * 用户名已存在
     */
    public static final int USER_USERNAME_IS_ALREADY_EXISTS = USER_BASE_ERROR_SEGMENT | 6;

    /**
     * 用户工号已存在
     */
    public static final int USER_JOB_NUMBER_IS_ALREADY_EXISTS = USER_BASE_ERROR_SEGMENT | 7;

    /**
     *用户手机号码已存在
     */
    public static final int USER_MOBILE_PHONE_IS_ALREADY_EXISTS = USER_BASE_ERROR_SEGMENT | 8;

    /**
     * 用户手机号码格式错误
     */
    public static final int USER_MOBILE_PHONE_IS_ERROR_FORMAT = USER_BASE_ERROR_SEGMENT | 9;

    /**
     * 用户邮箱已存在
     */
    public static final int USER_EMAIL_IS_ALREADY_EXISTS = USER_BASE_ERROR_SEGMENT | 10;

    /**
     * 用户邮箱格式错误
     */
    public static final int USER_EMAIL_IS_ERROR_FORMAT = USER_BASE_ERROR_SEGMENT | 11;

    /**
     * 用户名密码为空
     */
    public static final int USER_PASSWORD_IS_NULL = USER_BASE_ERROR_SEGMENT | 12;

    /**
     * 用户名或密码错误
     */
    public static final int LOGIN_NAME_IS_ERROR = USER_BASE_ERROR_SEGMENT | 13;

    /**
     * 用户新密码为空
     */
    public static final int USER_NEW_PASSWORD_IS_NULL = USER_BASE_ERROR_SEGMENT | 14;

    /**
     * 用户手机号码为空
     */
    public static final int USER_MOBILE_PHONE_IS_NULL = USER_BASE_ERROR_SEGMENT | 15;

    /**
     * 用户邮箱为空
     */
    public static final int USER_EMAIL_IS_NULL = USER_BASE_ERROR_SEGMENT | 16;

    /**
     * 用户导入的文件不是Excel文件
     */
    public static final int FILE_IS_NOT_EXCEL = USER_BASE_ERROR_SEGMENT | 17;

    /**
     * 用户ID为空
     */
    public static final int USER_ID_IS_NULL = USER_BASE_ERROR_SEGMENT | 18;

    /**
     * 找不到匹配的用户
     */
    public static final int NO_MATCH_USER = USER_BASE_ERROR_SEGMENT | 19;

    /**
     * 密码错误
     */
    public static final int PASSWORD_IS_ERROR = USER_BASE_ERROR_SEGMENT | 20;

    /**
     * 用户权限为空
     */
    public static final int USER_AUTHORITY_IS_NULL = USER_BASE_ERROR_SEGMENT | 21;

    /**
     *  文件操作异常
     */
    public static final int FILE_OPERATING_EXCEPTION = USER_BASE_ERROR_SEGMENT | 22;

    /**
     * 用户导入数据失败
     */
    public static final int IMPORT_USER_IS_FAIL = USER_BASE_ERROR_SEGMENT | 23;

    /**
     * 用户密码长度错误
     */
    public static final int USER_PASSWORD_LENGTH_IS_ERROR = USER_BASE_ERROR_SEGMENT | 24;

    /**
     * 用户电话号码格式错误
     */
    public static final int USER_TELEPHONE_IS_ERROR_FORMAT = USER_BASE_ERROR_SEGMENT | 25;

    /**
     * 用户电话号码已存在
     */
    public static final int USER_TELEPHONE_IS_ALREADY_EXISTS = USER_BASE_ERROR_SEGMENT | 26;

    /**
     * 用户权限不足
     */
    public static final int USER_AUTHORITY_IS_LOW = USER_BASE_ERROR_SEGMENT | 27;

    /**
     * 用户权限错误
     */
    public static final int USER_AUTHORITY_IS_ERROR = USER_BASE_ERROR_SEGMENT | 28;

    /**
     * 用户账号没有被激活
     */
    public static final int USER_ACCOUNT_NOT_ACTIVATED = USER_BASE_ERROR_SEGMENT | 29;

    /**
     * 登录名长度错误
     */
    public static final int USER_LOGIN_NAME_LENGTH_IS_ERROR = USER_BASE_ERROR_SEGMENT | 30;

    /**
     * 用户账号在其他地方登陆
     */
    public static final int USER_ACCOUNT_HAVE_BEEN_LOGGED_IN_ELSEWHERE = USER_BASE_ERROR_SEGMENT | 31;

    /**
     * 用户不是一个管理员（平台管理员，后台管理员，会议管理员）
     */
    public static final int USER_IS_NOT_A_ADMIN = USER_BASE_ERROR_SEGMENT | 32;

    /**
     * 用户登录名包含中文
     */
    public static final int USER_LOGIN_NAME_IS_ERROR = USER_BASE_ERROR_SEGMENT | 33;

    /**
     * 用户密码包含中文
     */
    public static final int USER_PASSWORD_IS_ERROR = USER_BASE_ERROR_SEGMENT | 34;

    /**
     * 用户名长度错误
     */
    public static final int USERNAME_LENGTH_IS_ERROR = USER_BASE_ERROR_SEGMENT | 35;

    /**
     * 用户不是一个设备用户
     */
    public static final int USER_IS_NOT_A_DEVICE_USER = USER_BASE_ERROR_SEGMENT | 36;

    /**
     * pad端用户登录人数超过限定
     */
    public static final int USER_CONNECTION_NUMBER_IS_ERROR = USER_BASE_ERROR_SEGMENT | 37;

}
