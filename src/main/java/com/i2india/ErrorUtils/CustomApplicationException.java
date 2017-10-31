package com.i2india.ErrorUtils;

public class CustomApplicationException extends CustomException{
	public CustomApplicationException(int errorCode,String errorMessage)
	{
		super(errorCode,errorMessage);
	}
}
