package com.i2india.security;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import com.i2india.Domain.User;
import com.i2india.SecurityUtils.MerchantAuthentication;
import com.i2india.Service.UserService;

@Component
public class MerchantAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserService userService;

	static Logger log = Logger.getLogger(MerchantAuthenticationProvider.class.getName());
	
	
	public void setUserService(UserService userService)
	{
		this.userService = userService;
	}
	
	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {

		if (authentication.isAuthenticated())
			return authentication;

		log.debug("here in provider authentication");

		String merchantId = authentication.getPrincipal().toString();
		String merchantInfo = authentication.getCredentials().toString();

		Integer id;
		
		try
		{
			id = Integer.parseInt(merchantId);
		}
		catch(NumberFormatException exception)
		{
			throw new BadCredentialsException("credentials incorrect or some other erroer?");
		}
		
		User user = null;

		
		log.debug("print error !!!!!!!!" + userService==null);
		
		MerchantAuthentication merchantAuthentication = new MerchantAuthentication(userService);
		boolean truthValue = merchantAuthentication.validateMerchant(id,merchantInfo);
		
		
		log.debug("here is the validation result!!" + truthValue);
		
		
		if(merchantAuthentication.validateMerchant(id, merchantInfo))
			user = userService.loadUserByUserID(id);

		
		if (user != null) {

			authentication = new UsernamePasswordAuthenticationToken(merchantId,null,user.getAuthorities());

		} 
		
		else
		{
			throw new BadCredentialsException("credentials incorrect");
		}

		return authentication;
	}

	@Override
	public boolean supports(Class<?> authentication) {

		return  PreAuthenticatedAuthenticationToken.class.equals(authentication);
	}

	


}
