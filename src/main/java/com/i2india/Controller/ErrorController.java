package com.i2india.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.i2india.ErrorUtils.CustomException;
import com.i2india.ErrorUtils.CustomSecurityException;
import com.i2india.ErrorUtils.ErrorResource;
import com.i2india.ErrorUtils.FormValidationException;
import com.i2india.ErrorUtils.StandardResponse;



@ControllerAdvice
public class ErrorController {
	
	static Logger log = Logger.getLogger(ErrorController.class.getName());
	
	@ExceptionHandler(value=CustomSecurityException.class)
	@ResponseStatus(value=HttpStatus.UNAUTHORIZED)
	public @ResponseBody StandardResponse prepareSecurityErrorMessages(CustomException ex)
	{
	    ErrorResource resource = new ErrorResource();
	    resource.setHttpStatusCode(200);
	    resource.setCode(ex.getErrorCode());
	    resource.setMessage(ex.getErrorMessage());    
		return new StandardResponse("failure", resource);
	}
	
	@ExceptionHandler(value=CustomException.class)
	@ResponseStatus(value=HttpStatus.OK)
	public @ResponseBody StandardResponse prepareErrorMessages(CustomException ex)
	{
	    ErrorResource resource = new ErrorResource();
	    resource.setHttpStatusCode(200);
	    resource.setCode(ex.getErrorCode());
	    resource.setMessage(ex.getErrorMessage());
	    
	    resource.setDeveloperMessage("this is just a default message for now");
	    
		return new StandardResponse("failure", resource);
	}
	
	@ExceptionHandler(value=FormValidationException.class)
	public @ResponseBody StandardResponse prepareFormErrorMessages(FormValidationException ex)
	{
		ErrorResource resource = new ErrorResource();
		Map map = new HashMap<String, String>();
		resource.setCode(ex.getErrorCode());
		resource.setMessage(ex.getErrorsMapping());
		
		return new StandardResponse("failure", resource);
	}
	
	@ExceptionHandler(value=MethodArgumentNotValidException.class)
	public @ResponseBody ArrayList<FieldError> prepareFormFieldErrors(MethodArgumentNotValidException ex)
	{
		BindingResult result = ex.getBindingResult();
		ArrayList<FieldError> fieldErrors = (ArrayList<FieldError>) result.getFieldErrors();
		
		return fieldErrors;
	}
	
//	@ExceptionHandler(value=Exception.class)
//	@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
//	public @ResponseBody StandardResponse prepareDefaultCouldNotService(Exception ex)
//	{
//		ErrorResource resource = new ErrorResource();
//		resource.setHttpStatusCode(500);
//		resource.setCode(ErrorConstants.UKNOWN_ERROR);
//		resource.setMessage("Sorry for the inconvinice please bear with us");
//		
//		return new StandardResponse("failure", resource);
//	}

}
