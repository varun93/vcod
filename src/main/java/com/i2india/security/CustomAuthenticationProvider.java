package com.i2india.security;

import java.util.Collection;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;


import com.i2india.Domain.User;
import com.i2india.SecurityUtils.KeyEncryption;
import com.i2india.Service.UserService;
import com.i2india.ServiceUtils.AccountStatus;



public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserService userService;

	static Logger log = Logger.getLogger(CustomAuthenticationProvider.class.getName());

	public void setUserService(UserService userService) {
		this.userService = userService;
	}



	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {


		String username = authentication.getName();
		String password = (String) authentication.getCredentials();

		
    	String suppliedPasswordHash = KeyEncryption.hashPassword(password);

		log.debug("In authentication provider before services");

		log.debug(username+password);
		User user=null;
		
		if(!StringUtils.isEmpty(username) || !StringUtils.isEmpty(password))
			user = (User) userService.loadUserByUsername(username);

		log.debug("In authentication provider " + user.getUsername());
		
			
		if (user!= null && password!=null && !suppliedPasswordHash.equals(user.getPassword())) {

			throw new BadCredentialsException("Username or password not correct");
		}
		
		if(!user.getStatus().equals(AccountStatus.ACTIVE))
		{
			throw new BadCredentialsException("Account not activated !");			
		}
		
		user.setLast_login(new Date()); //Updating log in time
		userService.updateUser(user);
		
		Collection<? extends GrantedAuthority> authorities = user.getAuthorities();


		return new UsernamePasswordAuthenticationToken(user,null, authorities);


	}



	@Override
	public boolean supports(Class<?> authentication) {

		return UsernamePasswordAuthenticationToken.class.equals(authentication);

	}

}
