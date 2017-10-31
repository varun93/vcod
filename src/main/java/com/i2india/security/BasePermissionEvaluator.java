package com.i2india.security;

import java.io.Serializable;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.i2india.Domain.Merchant;
import com.i2india.Domain.Transactions;
import com.i2india.Service.OrderService;

@Component
public class BasePermissionEvaluator implements PermissionEvaluator{

	@Autowired
	private OrderService orderService;
	

	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
		boolean hasPermission = false;
		if ( authentication != null &&  permission instanceof String){
			hasPermission = true;

		} else {
			hasPermission =false; 
		}
		return hasPermission;
	}

	@Override
	public boolean hasPermission(Authentication authentication,
			Serializable targetId, String targetType, Object permission) {
		System.out.println("Second method !");
		throw new RuntimeException("Id and Class permissions are not supperted by this application");
		
		
	}
}