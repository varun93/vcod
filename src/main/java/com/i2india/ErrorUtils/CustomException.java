package com.i2india.ErrorUtils;

public class CustomException extends RuntimeException{

	private int errorCode;
	private String errorMessage;
	//this can be related to particular product or customer can be a data structure too
	private Object additionalDetails;



	public CustomException(int errorCode)
	{
		this.errorCode = errorCode;
	}
	
	public CustomException(int errorCode,Object additionalDetails)
	{
		
		this.errorCode = errorCode;
		this.additionalDetails = additionalDetails;
		
	}
	
	public CustomException(int errorCode, String errorMessage,
			Object additionalDetails) {

		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.additionalDetails = additionalDetails;
	}

	public CustomException(int errorCode, String errorMessage) {

		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public Object getAdditionalDetails() {
		return additionalDetails;
	}



}
