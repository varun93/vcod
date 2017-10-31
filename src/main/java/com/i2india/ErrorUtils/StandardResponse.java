package com.i2india.ErrorUtils;

public class StandardResponse {


	private String status;
	private Object data;
	private ErrorResource errors;



	public StandardResponse(String status,Object data,ErrorResource errors)
	{
		this.status = status;
		this.data = data;
		this.errors = errors;
	}


	public StandardResponse(String status,Object data)
	{
		this.status = status;
		this.data = data;
	}

    public StandardResponse(String status,ErrorResource errors)
    {
    	this.status = status;
    	this.errors = errors;
    }


	public String getStatus() {
		return this.status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Object getData() {
		return this.data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public ErrorResource getErrors() {
		return this.errors;
	}
	public void setErrors(ErrorResource errors) {
		this.errors = errors;
	}




}
