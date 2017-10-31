package com.i2india.Service;

import java.util.ArrayList;
import java.util.Map;

import com.i2india.Domain.Bank;
import com.i2india.Domain.Consumer;
import com.i2india.Domain.Dispute;
import com.i2india.Domain.Merchant;
import com.i2india.Domain.Settlement;
import com.i2india.Domain.Transactions;

public interface OrderService {

	public ArrayList<String> checkIn(String parm[], Consumer consumer, Merchant merchant,Bank bank);
	public String checkOut(Bank bank,Transactions transaction,String parm[]);
	public Transactions getTransactions(int id);
	public int cancelOrder(int transaction_id,int merchant_id,int amount,String Reason);
	public Boolean completeCancelOrder(int order_id);
	@SuppressWarnings("rawtypes")
	public Map getAllTransactionsForMerchant(Merchant merchant, String status,
			int start, int size, String fromDateParm, String toDateParm,
			String onDateParm,int transactionid,double startAmount,double endAmount,String merchantOrderId,String customerEmail,String sort,String order);
	@SuppressWarnings("rawtypes")
	public Map getAllDisputesForMerchant(Merchant merchant, String status, int start, int size,
			String fromDateParm, String toDateParm, String onDateParm,
			int disputeId, double startAmount, double endAmount,
			String merchantOrderId, String customerEmail,int transactionId,String type,String sort,String order);
	@SuppressWarnings("rawtypes")
	public Map getAllSettlementsForMerchant(Merchant merchant, String status, int start, int size,
			String fromDateParm, String toDateParm, String onDateParm,
			int settlementId, double startAmount, double endAmount,String sort,String order);
	public ArrayList<Transactions> getClosedTransactions();
	public void updateTransaction(Transactions transaction);
	public Transactions getTransaction();
	public Transactions getTransactionById(int id);
	public Settlement getSettlementById(int id);
	public Dispute getDisputeById(int id);
	public Merchant getMerchantForAuthorization(Object object,int id);
	public Map getAllTransactionsForConsumer(Consumer consumer, String status,int start, int size, String fromDateParm,
			String toDateParm,String onDateParm, int transactionId, double startAmount,	double endAmount,
			String merchantOrderId, String organizationName,String sort, String order);
	public Map getAllDisputesForConsumer(Consumer consumer, String status, int start,int size, String fromDateParm, 
			String toDateParm,	String onDateParm, int disputeId, double startAmount,double endAmount, 
			String merchantOrderId, String organizationName,	int transactionId, String type, String sort, String order);
	public void addConsumerDispute(Dispute dispute, int transactionId);
	Consumer getConsumerForAuthorization(Object object, int id);
}