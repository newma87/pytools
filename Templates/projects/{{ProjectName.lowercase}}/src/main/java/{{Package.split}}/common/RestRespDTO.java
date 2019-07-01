package {{Package}}.common;

import {{Package}}.exception.ServerErrorException;

import java.io.Serializable;

/**
 * Created by huangpeilin
 * @author lin
 */
public class RestRespDTO implements Serializable {

	private Integer errorCode = ErrorConstants.SUCCESS;

	private String errorMessage;

	private Object data;

	public static RestRespDTO ok() {
		return RestRespDTO.ok(null);
	}

	public static RestRespDTO ok(Object data) {
		RestRespDTO result = new RestRespDTO();
		result.setErrorCode(ErrorConstants.SUCCESS);
		result.setErrorMessage("success");
		result.setData(data);
		return result;
	}

	public static RestRespDTO error(int errorCode, String errorMessage) {
		RestRespDTO result = new RestRespDTO();
		result.setErrorCode(errorCode);
		result.setErrorMessage(errorMessage);
		return result;
	}

	public static RestRespDTO error(int errorCode) {
		return RestRespDTO.error(errorCode, ErrorMessages.getErrorMessage(errorCode));
	}

	public static RestRespDTO error(ServerErrorException exception) {
		return RestRespDTO.error(exception.getErrorCode(), exception.getMessage());
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "{" +
				"errorCode=" + errorCode +
				", errorMessage='" + errorMessage + '\'' +
				", data=" + data +
				'}';
	}
}
