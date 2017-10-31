package com.i2india.ErrorUtils;

public class CustomSecurityException extends CustomException{
	public CustomSecurityException(int errorCode,String errorMessage)
	{
		super(errorCode,errorMessage);
	}
}
