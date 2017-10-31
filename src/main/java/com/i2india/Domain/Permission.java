package com.i2india.Domain;



import java.io.Serializable;

import org.springframework.security.core.GrantedAuthority;



public class Permission implements GrantedAuthority,Serializable {



/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private String authority;


public Permission(String authority)

{

this.authority = authority;

}



@Override

public String getAuthority() {

// TODO Auto-generated method stub

return this.authority;

}



}