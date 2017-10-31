package com.i2india.AbstractTests;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager="transactionManager",defaultRollback=true)
@Transactional
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
	TransactionalTestExecutionListener.class})
public abstract class AbstractTransactionalTest 
{

	
	
	@Before
	public void setup()
	{
		doInit();
	}
	
	
	@After
	public void teardown()
	{
		SecurityContextHolder.clearContext();
	}
	
	public abstract void doInit();
	
	
}
