package com.i2india.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.i2india.Domain.Bank;


public class BankAuthenticationFilter extends TokenAuthenticationFilter{

	@Override
	protected void successfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, Authentication authResult) {
		
		if(authResult.getPrincipal() instanceof Bank)
			SecurityContextHolder.getContext().setAuthentication(authResult);
		else
			SecurityContextHolder.clearContext();

		
	}


	
}
