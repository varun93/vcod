package com.i2india.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.i2india.Domain.Dispute;
import com.i2india.Domain.Merchant;
import com.i2india.Domain.MerchantAccount;
import com.i2india.Domain.Settlement;
import com.i2india.Domain.Transactions;
import com.i2india.ErrorUtils.CustomSecurityException;
import com.i2india.ErrorUtils.ErrorConstants;
import com.i2india.Service.OrderService;
import com.i2india.Service.UserService;
import com.i2india.ServiceUtils.AccountStatus;

/*
 * Merchant authorization framework
 */

@Service("MerchantAuthorizationService")
public class MerchantAuthorizationService {

	//Could not autowire DAO methods
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private UserService userService;
	
	
	/*
	 * Checks whether the merchant is the owner of the transaction
	 * Caching can be used to optimize and reduce fetching from twice to once
	 */
	public Boolean hasTransactionAccessPermission(int transactionId)
	{
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
		Merchant merchant = (Merchant)authentication.getPrincipal();
		try
		{
				Merchant merchantFromTransactions =orderService.getMerchantForAuthorization(new Transactions(), transactionId);
				if(merchantFromTransactions.getUser_id()==merchant.getUser_id())
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
	 * Checks whether the merchant is the owner of the dispute
	 * Caching can be used to optimize and reduce fetching from twice to once
	 */
	public Boolean hasDisputeAccessPermission(int disputeId)
	{
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
		Merchant merchant = (Merchant)authentication.getPrincipal();
		try
		{
				Merchant merchantFromDispute =orderService.getMerchantForAuthorization(new Dispute(), disputeId);
				if(merchantFromDispute.getUser_id()==merchant.getUser_id())
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
	
	/*
	 * Checks whether the merchant is the owner of the settlement
	 * Caching can be used to optimize and reduce fetching from twice to once
	 */
	public Boolean hasSettlementAccessPermission(int settlementId)
	{
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
		Merchant merchant = (Merchant)authentication.getPrincipal();
		try
		{
				Merchant merchantFromSettlement =orderService.getMerchantForAuthorization(new Settlement(), settlementId);
				if(merchantFromSettlement.getUser_id()==merchant.getUser_id())
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
	
	public Boolean hasMerchantAccountUpdatePermission(MerchantAccount merchantAccount)
	{
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
		Merchant merchant = (Merchant)authentication.getPrincipal();
		try
		{
				Merchant merchantFromMerchantAccount = userService.getMerchantFromMerchantAccount(merchantAccount.getAccount_id());
				if(merchantFromMerchantAccount.getUser_id()==merchant.getUser_id())
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
	
	/*
	 * Checks whether the has completed registration and granted access to use of payment gatewat
	 */
	public Boolean hasGatewayAccessPermission()
	{
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
		Merchant merchant = (Merchant)authentication.getPrincipal();
		try
		{
				System.out.println(merchant.getRegistration());
				if(merchant.getRegistration().equals(AccountStatus.COMPLETE))
					return true;
				else
					return false;
		}
		catch(NullPointerException ex)
		{
			//Do nothing
			return false;
		}

	}
	
	
}
