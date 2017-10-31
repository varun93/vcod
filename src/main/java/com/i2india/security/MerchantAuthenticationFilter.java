package com.i2india.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.GenericFilterBean;


public class MerchantAuthenticationFilter extends GenericFilterBean{

	@Autowired
	private AuthenticationManager authenticationManager;
	
	static Logger log = Logger.getLogger(MerchantAuthenticationFilter.class.getName());
	
	public void setAuthenticationManager(
			AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}


	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {


		//HttpServletRequest request2 = (HttpServletRequest) request;

		if(requiresAuthentication((HttpServletRequest)request))
		{
			doAuthenticate((HttpServletRequest)request,(HttpServletResponse)response);
		}

		chain.doFilter(request, response);

	}



	public void doAuthenticate(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		
		Authentication authResult = null; 

		String merchantID = request.getParameter("merchantid");
		String merchantInfo = request.getParameter("encParm");

		
		if(merchantID == null || merchantInfo == null)
			return;

		try {
			
			PreAuthenticatedAuthenticationToken authRequest = new PreAuthenticatedAuthenticationToken(merchantID, merchantInfo);
			authResult = authenticationManager.authenticate(authRequest);
			successfulAuthentication(request, response, authResult);

		} 
		catch (AuthenticationException failed) 
		{
			unsuccessfulAuthentication(request, response, failed);

		}

	}


	protected void successfulAuthentication(HttpServletRequest request,

			HttpServletResponse response, Authentication authResult) {
		//can do a lot more than this 
		SecurityContextHolder.getContext().setAuthentication(authResult);

	}

	protected void unsuccessfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException failed) throws IOException {

		SecurityContextHolder.clearContext();
		//handle the subsequent steps 
		log.debug("Authentication unsuccessful");


	}


	private boolean requiresAuthentication(HttpServletRequest request)
	{

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		return (authentication == null);

	}


}
