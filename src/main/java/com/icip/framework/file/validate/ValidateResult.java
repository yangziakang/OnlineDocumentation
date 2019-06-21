package com.icip.framework.file.validate;

public class ValidateResult {

	private boolean isSuccess;
	private String errorMsg;
	
	public ValidateResult(boolean isSuccess, String errorMsg) {
		this.isSuccess = isSuccess;
		this.errorMsg = errorMsg;
	}
	
	public boolean isSuccess() {
		return isSuccess;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
