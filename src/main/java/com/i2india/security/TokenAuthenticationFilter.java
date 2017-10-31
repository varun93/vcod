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
import org.springframework.web.filter.GenericFilterBean;

import com.i2india.Domain.User;

public class TokenAuthenticationFilter extends GenericFilterBean{


	@Autowired
	private AuthenticationManager authenticationManager;

	static Logger log = Logger.getLogger(TokenAuthenticationFilter.class.getName());
	
	public void setAuthenticationManager(
			AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}



	protected Object getToken(HttpServletRequest request) {


	//	return "lSlF0N2UozJL28B6+JfbsKWzjm743GuDcKmRhd9yYnY=";
		log.debug("method "+request.getMethod());
		return request.getParameter("authToken");
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


	public void doAuthenticate(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException
	{
		Authentication authResult = null; 

		String token = (String) this.getToken(request);
		
		log.debug("auth token is !!!!"+token);

		if(token == null)
			return;


		try {
			Authentication authRequest  = new TokenAuthentication(token,null); 
			authResult = authenticationManager.authenticate(authRequest);
			User user = (User) authResult.getPrincipal();
			request.setAttribute("userId",new Integer(user.getUser_id()).toString());
			successfulAuthentication(request, response, authResult);
		}

		catch (AuthenticationException failed) {

			unsuccessfulAuthentication(request, response, failed);
		}

	}


	protected void unsuccessfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException failed) {

		SecurityContextHolder.clearContext();


	}


	protected void successfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, Authentication authResult) {

		System.out.print("here in successful authentication");
		User user = (User) authResult.getPrincipal();
		request.setAttribute("userId",new Integer(user.getUser_id()).toString());
		SecurityContextHolder.getContext().setAuthentication(authResult);

	}



	private boolean requiresAuthentication(HttpServletRequest request)
	{

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		return (authentication == null);

	}


}
