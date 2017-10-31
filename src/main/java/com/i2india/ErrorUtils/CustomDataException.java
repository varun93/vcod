package com.i2india.ErrorUtils;

public class CustomDataException extends CustomException {
	public CustomDataException(int errorCode,String errorMessage)
	{
		super(errorCode,errorMessage);
	}
}
