package com.i2india.ServiceUtils;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.i2india.ErrorUtils.CustomException;
import com.i2india.ErrorUtils.ErrorConstants;

public final class ValidationHelper {
	
	public final static String EMAIL_PATTERN = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
	public static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
	
	public static void  validateForStringEmptyAndNull(String ... strings)
	{
		int i=0;
		try
		{
	      for (i = 0; i < strings.length; i++)
	      {
	    	  if(strings[i].equals(null))
	    			  throw new CustomException(ErrorConstants.STRING_NULL,ErrorConstants.STR_STRING_NULL);
	    	  if(strings[i].isEmpty())
	    		  throw new CustomException(ErrorConstants.STRING_EMPTY,ErrorConstants.STR_STRING_EMPTY);
	      }
		}catch(NullPointerException ex)
	     {
	    	 throw new CustomException(ErrorConstants.STRING_NULL,ErrorConstants.STR_STRING_NULL);
	     }
	}
	
	public static void validateForDateNull(Date ... dates)
	{
	      for (int i = 0; i < dates.length; i++)
	      {
	    	  if(dates[i].equals(null))
	    			  throw new CustomException(ErrorConstants.DATE_NULL,ErrorConstants.STR_DATE_NULL);
	      }
	}

	
	public static void validateForLength(String string,int size)
	{
		if(string.equals(null))
			throw new CustomException(ErrorConstants.STRING_NULL,ErrorConstants.STR_STRING_NULL);
		if(string.length()<size)
			throw new CustomException(ErrorConstants.RANGE_ERROR,ErrorConstants.STR_RANGE_ERROR);
	}
	
	public static void validateForLength(String string,int low,int high)
	{
		if(string.equals(null))
			throw new CustomException(ErrorConstants.STRING_NULL,ErrorConstants.STR_STRING_NULL);
		if(string.length()<high && string.length() >low)
			throw new CustomException(ErrorConstants.RANGE_ERROR,ErrorConstants.STR_RANGE_ERROR);
	}
	
	
	public static void validateForRange(int num,int low,int high)
	{
		if(num>low && num<high)
			throw new CustomException(ErrorConstants.RANGE_ERROR,ErrorConstants.STR_RANGE_ERROR);
			
	}
	
	public static void validateForRange(long num,long low,long high)
	{
		if(num>low && num<high)
			throw new CustomException(ErrorConstants.RANGE_ERROR,ErrorConstants.STR_RANGE_ERROR);
	}
	
	
	public static void validateForRange(double num,double low,double high)
	{
		if(num>low && num<high)
			throw new CustomException(ErrorConstants.RANGE_ERROR,ErrorConstants.STR_RANGE_ERROR);
	}
	
	public static Boolean validateWithRegex(String data,String Regex)
	{
		try
		{
		    Pattern pattern;
		    Matcher matcher;
		    matcher=  Pattern.compile(Regex).matcher(data);
			return matcher.find();
		}catch(NullPointerException ex)
		{
			throw new CustomException(ErrorConstants.INVALID_DATA,ErrorConstants.STR_INVALID_DATA);
		}
	}
	
/*	public static void validateForZero(int ... num)
	{
	      for (int i = 0; i < num.length; i++)
	      {
	    	  if(num[i]==0)
	    	  {
	    		  throw new CustomException(ErrorConstants.NUM_ZERO,ErrorConstants.STR_NUM_ZERO);
	    	  }
	      }
	}
	*/
	
	public static void validateForZero(long ... num)
	{
	      for (int i = 0; i < num.length; i++)
	      {
	    	  if(num[i]==0)
	    	  {
	    		  throw new CustomException(ErrorConstants.NUM_ZERO,ErrorConstants.STR_NUM_ZERO);
	    	  }
	      }
	}
	
	
/*	public static void validateForZero(double ... num)
	{
	      for (int i = 0; i < num.length; i++)
	      {
	    	  if(num[i]==(double)0)
	    	  {
	    		  throw new CustomException(ErrorConstants.NUM_ZERO,ErrorConstants.STR_NUM_ZERO);
	    	  }
	      }
	}*/
	
	
	
	public static void validateEmails(String ... emails)
	{
		 for (int i = 0; i < emails.length; i++)
	      {
	    	 if(ValidationHelper.validateWithRegex(emails[i], EMAIL_PATTERN) == false )
			throw new CustomException(ErrorConstants.USER_INVALID_USERNAME, ErrorConstants.STR_USER_INVALID_USERNAME);
	      }
	}
	
	
	public static void validatePassword(String password)
	{
		if(ValidationHelper.validateWithRegex(password, PASSWORD_PATTERN) == false )
			throw new CustomException(ErrorConstants.USER_INVALID_PASSWORD,ErrorConstants.STR_USER_INVALID_PASSWORD);
	}
	
	
}
