package com.i2india.SecurityUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;
import com.i2india.Domain.User;
import com.i2india.Service.UserService;



public class AuthToken {

	
	private UserService userService;
	private static final String SYMMETRIC_KEY = "abcdefghijklmnop";
	private static final int EXPIRY_TIME_IN_MINUTES = 60;
	private static final String DELIMITER = ":";
	
	static Logger log = Logger.getLogger(AuthToken.class.getName());
	
	public AuthToken(UserService userService) {
		this.userService = userService;
	}


	public AuthToken()
	{
		
	}
	
	public String getAuthToken(Integer userid)
	{
		try {
			return userid==null?null:KeyEncryption.getEncryptedToken(userid.toString() + DELIMITER + new Long(System.currentTimeMillis()).toString(),SYMMETRIC_KEY);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
 	
    //tested for all possible inputs
	public String getAuthToken(String username)
	{

		try {
			return username==null?null:KeyEncryption.getEncryptedToken(username + DELIMITER + new Long(System.currentTimeMillis()).toString(),SYMMETRIC_KEY);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	//tested
	public String getUsername(String authToken) throws Exception
	{

		return authToken==null?null:KeyEncryption.getDecryptedToken(authToken,SYMMETRIC_KEY).split(DELIMITER)[0];
   }

	public User validateToken(String authToken) 
	{
		log.debug("Here before token validation");
    
		try {
			
			authToken = KeyEncryption.getDecryptedToken(authToken,SYMMETRIC_KEY);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		log.debug("Here after token validation");
		
		String userid =  authToken.split(DELIMITER)[0];
		String creationTime = authToken.split(DELIMITER)[1]; 
	
		log.debug("Userid is " + userid);
		
		User user = null;
		user = this.userService.loadUserByUserID(Integer.parseInt(userid));
		
		log.debug("Username is !!!!" + user.getUsername());
		
		boolean tokenExpired = hasTokenExpired(creationTime);
		
		
		if(user!=null && !tokenExpired)
			return user;
		else 
			return null;
		
	}
	
	public static int getUserIdFromToken(String authToken) 
	{
    
		try {
			authToken = KeyEncryption.getDecryptedToken(authToken,AuthToken.SYMMETRIC_KEY);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		
		String userid =  authToken.split(DELIMITER)[0];
		
		return Integer.parseInt(userid);

	}
	

	private boolean hasTokenExpired(String tokenCreationTime)
	{
		Calendar expirytime = new GregorianCalendar();
		expirytime.setTimeInMillis(Long.parseLong(tokenCreationTime));
		expirytime.add(Calendar.MINUTE,EXPIRY_TIME_IN_MINUTES);
		return (new Date(System.currentTimeMillis()).compareTo(expirytime.getTime())>0);
	}


	public static void main(String args[]) throws Exception
	{
		AuthToken auth = new AuthToken();
		String token = auth.getAuthToken(127);
		//log.debug(auth.isTokenValid(token));
		log.debug(token);
	}




}
