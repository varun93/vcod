package com.i2india.ServiceImpl;

import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import com.i2india.AbstractTests.AbstractTransactionalTest;

@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml"})
@WebAppConfiguration
public abstract class UserServiceTest extends AbstractTransactionalTest {

	@Override
	public void doInit() {
		
		String principal = "varun";
		String credentials = "varun123";
        Authentication auth = new UsernamePasswordAuthenticationToken(principal,credentials);
        SecurityContextHolder.getContext().setAuthentication(auth);
		
	}
	
	@Test
	public void loadUserByUsername()
	{
		
		
	}
	
	


	
}
