package {{Package}}.exception;

import {{Package}}.common.ErrorConstants;
import {{Package}}.common.ErrorMessages;

/**
 * Filename      ServerErrorException
 *
 * @author hdy
 * @date 2018/1/25 11:18
 * Description:     服务未知错误异常，用于抛出给外部模块接收的
 */
public class ServerErrorException extends Exception {
    private int errorCode;

    public ServerErrorException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ServerErrorException(String message) {
        super(message);
        this.errorCode = ErrorConstants.ERR_UNKNOWN;
    }

    public ServerErrorException(int errorCode) {
        super(ErrorMessages.getErrorMessage(errorCode));
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
