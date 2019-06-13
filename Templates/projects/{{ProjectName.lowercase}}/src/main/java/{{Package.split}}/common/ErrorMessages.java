package {{Package}}.common;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: newma<newma@live.cn>
 * Create: 2017/12/16 10:57
 * Description: 错误描述
 * @author lin
 */

public class ErrorMessages {

    // 默认错误描述
    public static final String DEFAULT_ERROR_MESSAGE = "unknown error";

    private static final Map<Integer, String> MESSAGES;

    static {
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(ErrorConstants.ERR_BAD_PARAM, "bad request param");
        map.put(ErrorConstants.ERR_INTERNAL_EXCEPTION, "internal server exception");
        map.put(ErrorConstants.ERR_UNKNOWN, DEFAULT_ERROR_MESSAGE);
        
        // TODO: 在此处添加更多的错误描述
        map.put(ErrorConstants.ERR_FORM_PARAMETER_ERROR, "form parameter error");

        // 用户模块
        map.put(ErrorConstants.USER_IS_NOT_EXISTS, "User is not exists.");
        map.put(ErrorConstants.USER_ID_IS_NULL,"User id is null.");
        map.put(ErrorConstants.USER_LOGIN_NAME_IS_NULL, "User loginName is null.");
        map.put(ErrorConstants.USER_USERNAME_IS_NULL, "User username is null.");
        map.put(ErrorConstants.USER_JOB_NUMBER_IS_NULL, "User jobNumber is null.");
        map.put(ErrorConstants.USER_MOBILE_PHONE_IS_NULL, "User mobilePhone is null.");
        map.put(ErrorConstants.USER_EMAIL_IS_NULL, "User email is null.");
        map.put(ErrorConstants.USER_LOGIN_NAME_IS_ALREADY_EXISTS, "User loginName is already exists.");
        map.put(ErrorConstants.USER_USERNAME_IS_ALREADY_EXISTS, "User username is already exists.");
        map.put(ErrorConstants.USER_JOB_NUMBER_IS_ALREADY_EXISTS, "User jobNumber is already exists.");
        map.put(ErrorConstants.USER_MOBILE_PHONE_IS_ALREADY_EXISTS, "User mobilePhone is already exists.");
        map.put(ErrorConstants.USER_MOBILE_PHONE_IS_ERROR_FORMAT, "User mobilePhone is error format.");
        map.put(ErrorConstants.USER_EMAIL_IS_ALREADY_EXISTS, "User email is already exists.");
        map.put(ErrorConstants.USER_EMAIL_IS_ERROR_FORMAT, "User email is error format.");
        map.put(ErrorConstants.NO_MATCH_USER,"No match user.");
        map.put(ErrorConstants.USER_PASSWORD_IS_NULL, "User password is null.");
        map.put(ErrorConstants.LOGIN_NAME_IS_ERROR, "LoginName is error.");
        map.put(ErrorConstants.USER_NEW_PASSWORD_IS_NULL, "User new password is null.");
        map.put(ErrorConstants.FILE_IS_NOT_EXCEL, "File is not excel.");
        map.put(ErrorConstants.PASSWORD_IS_ERROR, "Password is error.");
        map.put(ErrorConstants.USER_AUTHORITY_IS_NULL,"User authority is null.");
        map.put(ErrorConstants.FILE_OPERATING_EXCEPTION,"File operating execption.");
        map.put(ErrorConstants.IMPORT_USER_IS_FAIL,"Import user is fail.");
        map.put(ErrorConstants.USER_AUTHORITY_IS_ERROR,"User authority is error.");
        map.put(ErrorConstants.USER_AUTHORITY_IS_LOW, "User authority is low.");
        map.put(ErrorConstants.USER_PASSWORD_LENGTH_IS_ERROR, "User password length is error.");
        map.put(ErrorConstants.USER_ACCOUNT_NOT_ACTIVATED, "User account not activated.");
        map.put(ErrorConstants.USER_LOGIN_NAME_LENGTH_IS_ERROR, "User loginName length is error.");
        map.put(ErrorConstants.USER_TELEPHONE_IS_ERROR_FORMAT, "User telephone is error format.");
        map.put(ErrorConstants.USER_TELEPHONE_IS_ALREADY_EXISTS, "User telephone is already exists.");
        map.put(ErrorConstants.USER_ACCOUNT_HAVE_BEEN_LOGGED_IN_ELSEWHERE, "Accounts have been logged in elsewhere.");
        map.put(ErrorConstants.USER_IS_NOT_A_ADMIN, "User is not a admin or web admin or meeting admin.");
        map.put(ErrorConstants.USER_LOGIN_NAME_IS_ERROR, "User loginName contains chinese.");
        map.put(ErrorConstants.USER_PASSWORD_IS_ERROR, "User password contains chinese.");
        map.put(ErrorConstants.USERNAME_LENGTH_IS_ERROR, "Username length is error.");
        map.put(ErrorConstants.USER_IS_NOT_A_DEVICE_USER, "User is not a device user.");
        map.put(ErrorConstants.USER_CONNECTION_NUMBER_IS_ERROR, "Pad user login connection number is exceeded the limit.");

        MESSAGES = Collections.unmodifiableMap(map);
    };

    /**
     * 通过error code取得错误描述
     *
     * @param errCode : int 类型的错误代码
     * @return String : 返回错误描述
     */
    public static String getErrorMessage(int errCode) {
        return MESSAGES.get(errCode);
    }
}

