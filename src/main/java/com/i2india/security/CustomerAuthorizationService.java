package com.i2india.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.i2india.Domain.Consumer;
import com.i2india.Domain.Dispute;
import com.i2india.Domain.Transactions;
import com.i2india.ErrorUtils.CustomSecurityException;
import com.i2india.ErrorUtils.ErrorConstants;
import com.i2india.Service.OrderService;
import com.i2india.Service.UserService;

/*
 * Merchant authorization framework
 */

@Service("CustomerAuthorizationService")
public class CustomerAuthorizationService {

	//Could not autowire DAO methods
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private UserService userService;
	
	
	/*
	 * Checks whether the customer is the owner of the transaction
	 * Caching can be used to optimize and reduce fetching from twice to once
	 */
	public Boolean hasTransactionAccessPermission(int transactionId)
	{
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
		Consumer consumer = (Consumer)authentication.getPrincipal();
		try
		{
				Consumer consumerFromTransactions =orderService.getConsumerForAuthorization(new Transactions(), transactionId);
				if(consumerFromTransactions.getUser_id()==consumer.getUser_id())
					return true;
				else
					throw new CustomSecurityException(ErrorConstants.UNAUTHORIZED_ACCESS,ErrorConstants.STR_UNAUTHORIZED_ACCESS);
		}
		catch(NullPointerException ex)
		{
			//Do nothing
			return true;
		}

	}

	/*
	 * Checks whether the customer is the owner of the dispute
	 * Caching can be used to optimize and reduce fetching from twice to once
	 */
	public Boolean hasDisputeAccessPermission(int disputeId)
	{
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
		Consumer consumer = (Consumer)authentication.getPrincipal();
		try
		{
				Consumer consumerFromDispute=orderService.getConsumerForAuthorization(new Dispute(), disputeId);
				if(consumerFromDispute.getUser_id()==consumer.getUser_id())
					return true;
				else
					return false;
		}
		catch(NullPointerException ex)
		{
			//Do nothing
			return true;
		}

	}
	
	
}
