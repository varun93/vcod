package com.i2india.ErrorUtils;


public class ErrorResource {

	private int code;
	private int HttpStatusCode;
	private Object message;
	private String developerMessage;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public int getHttpStatusCode() {
		return HttpStatusCode;
	}
	public void setHttpStatusCode(int httpStatusCode) {
		HttpStatusCode = httpStatusCode;
	}
	
	public Object getMessage() {
		return message;
	}
	
	public void setMessage(Object message) {
		this.message = message;
	}
	public String getDeveloperMessage() {
		return developerMessage;
	}
	public void setDeveloperMessage(String developerMessage) {
		this.developerMessage = developerMessage;
	}
	
	




}
