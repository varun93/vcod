package com.i2india.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class TokenAuthentication implements Authentication {

	private static final long serialVersionUID = 1L;
	private Object token;
	private Object details;


	public TokenAuthentication(Object token,Object details) {
		this.token = token;
		this.details = details;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return new ArrayList<GrantedAuthority>(0);
	}
	@Override
	public Object getCredentials() {
		return token;
	}
	@Override
	public Object getDetails() {
		return null;
	}
	@Override
	public Object getPrincipal() {
		return null;
	}

	@Override
	public boolean isAuthenticated() {
		return false;
	}
	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
	}
	@Override
	public String getName() {
		return null;
	}
}

