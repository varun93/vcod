package com.i2india.ErrorUtils;

import java.util.HashMap;

public class FormValidationException extends RuntimeException{
	
	
	private static final long serialVersionUID = -2959890813460072909L;
	private int errorCode;
	private String message;
	private HashMap<String, String> errorsMapping = new HashMap<String, String>();
	
	public FormValidationException(int errorCode, String message,
			HashMap<String, String> errorsMapping) {
		this.errorCode = errorCode;
		this.message = message;
		this.errorsMapping = errorsMapping;
	}
	
	
	public FormValidationException(int errorCode,
			HashMap<String, String> errorsMapping) {
		this.errorCode = errorCode;
		this.errorsMapping = errorsMapping;
	}
	
	
	public int getErrorCode() {
		return errorCode;
	}
	public String getMessage() {
		return message;
	}
	public HashMap<String, String> getErrorsMapping() {
		return errorsMapping;
	}
	
	

}
