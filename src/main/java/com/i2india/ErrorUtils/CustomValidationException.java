package com.i2india.ErrorUtils;

public class CustomValidationException extends CustomException{
	public CustomValidationException(int errorCode,String errorMessage)
	{
		super(errorCode,errorMessage);
	}
}
