package {{Package}}.common;

/**
 * Created by huangpeilin
 * @author lin
 */
public class RestRespDTO {

	private Integer errorCode = ErrorConstants.SUCCESS;

	private String errorMessage;

	private Object data;

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
