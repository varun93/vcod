package com.i2india.security;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.i2india.Domain.User;
import com.i2india.Service.UserService;


// checks for a valid token 
//returns a true TokenAuthentication object upon successful authentication 

@Component
public class TokenAuthenticationProvider implements AuthenticationProvider {

	@Autowired 
	private UserService userService;

	static Logger log = Logger.getLogger(TokenAuthenticationProvider.class.getName());
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}


	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		if (authentication.isAuthenticated())
			return authentication;


		String token = authentication.getCredentials().toString();

		User user = null;

		log.debug("here is the token in authentication provider"+token);
		user = userService.getUserByToken(token);
  
		//check for account status 
		if (user != null) {

			authentication = new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
		} 
		else
		{
			throw new BadCredentialsException("credentials not correct");
		}


		return authentication;
	}



	@Override
	public boolean supports(Class<?> authentication) {

		return TokenAuthentication.class.equals(authentication);
	}
}