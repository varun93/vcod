package com.i2india.ServiceUtils;

import java.util.Map;


public class Message {
	private String message = null;
	private Map additionalData=null;
	Message (String msg)
	{
		message = msg;
	}
	public Message()
	{
		
	}
	public String getMessage()
	{
		return this.message;
	}
	public void setMessage(String var1)
	{
		message = var1;
	}

	public void setAdditionalData(Map data)
	{
		this.additionalData=data;
	}
	
	public Map getAdditionalData()
	{
		return this.additionalData;
	}
	
}
