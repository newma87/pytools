package {{Package}}.util;

import {{Package}}.common.ErrorConstants;
import {{Package}}.common.ErrorMessages;
import {{Package}}.common.RestRespDTO;
import {{Package}}.exception.ServerErrorException;

import static {{Package}}.common.ErrorConstants.SUCCESS;

public final class RestRespUtil {

    private RestRespUtil() {
    }

    public static RestRespDTO getSuccessRsp() {
        return getSuccessRsp(null);
    }

    /**
     * 成功提示
     *
     * @param data
     * @return
     */
    public static RestRespDTO getSuccessRsp(Object data) {
        RestRespDTO restRespDTO = new RestRespDTO();
        restRespDTO.setErrorCode(SUCCESS);
        restRespDTO.setErrorMessage("Success");
        restRespDTO.setData(data);
        return restRespDTO;
    }

    public static RestRespDTO getErrorRsp(Exception e) {
        RestRespDTO restRespDTO = new RestRespDTO();
        restRespDTO.setErrorCode(ErrorConstants.ERR_UNKNOWN);
        restRespDTO.setErrorMessage(e.getMessage().isEmpty() ? e.getMessage() : "unknown exception");
        return restRespDTO;
    }

    /**
     *  自定义的异常处理的返回调用的函数
     * @param e ServerErrorException
     * @return RestRespDTO
     */
    public static RestRespDTO getErrorRsp(ServerErrorException e) {
        RestRespDTO restRespDTO = new RestRespDTO();
        restRespDTO.setErrorCode(e.getErrorCode());
        restRespDTO.setErrorMessage(e.getMessage());
        return restRespDTO;
    }

    public static RestRespDTO getErrorRsp(int errorCode, String msg) {
        RestRespDTO restRespDTO = new RestRespDTO();
        restRespDTO.setErrorCode(errorCode);
        restRespDTO.setErrorMessage(msg);
        return restRespDTO;
    }

    public static RestRespDTO getErrorRsp(int errorCode, String msg, Object data) {
        RestRespDTO restRespDTO = new RestRespDTO();
        restRespDTO.setErrorCode(errorCode);
        restRespDTO.setErrorMessage(msg);
        restRespDTO.setData(data);
        return restRespDTO;
    }

    public static RestRespDTO getErrorRsp(int errorCode) {
        String errorMsg = ErrorMessages.getErrorMessage(errorCode);
        return getErrorRsp(errorCode, errorMsg);
    }

    public static RestRespDTO getOtherErrorRsp(String msg) {
        return getErrorRsp(ErrorConstants.ERR_UNKNOWN, msg);
    }
}
