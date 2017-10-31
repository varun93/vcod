package com.i2india.DAO;

import java.util.ArrayList;
import java.util.Map;

import com.i2india.Domain.Bank;
import com.i2india.Domain.BankContract;
import com.i2india.Domain.Consumer;
import com.i2india.Domain.Dispute;
import com.i2india.Domain.Merchant;
import com.i2india.Domain.MerchantContract;
import com.i2india.Domain.Settlement;
import com.i2india.Domain.Transactions;
import com.i2india.ServiceUtils.ResultSetSize;

public interface OrderDAO {

	public void addTransactions(Transactions transaction);

	@SuppressWarnings("unchecked")
	public ArrayList<Transactions> getAllTransactions();

	public Transactions getTransactionByMerchant(Merchant merchant,int transaction_id);
	
	public Transactions checkTransactionExsist(Merchant merchant,Transactions order) ;
	
	public BankContract getBankContract(Bank bank,String paymode);
	
	public MerchantContract getMerchantContract(Merchant merchant,String paymode);
	
	public Bank getBank(Integer id) ;

	public Transactions getTransaction(Integer id);

	public void updateTransactions(Transactions transaction);

	public void deleteTransaction(Integer transactionId);

	public ArrayList<Transactions> getTransactions(Merchant merchant,Consumer consumer,Map Params,int start,int size,ResultSetSize resultSetSize);

	void addDispute(Dispute dispute);

	public ArrayList<Dispute> getDisputes(Merchant merchant,Consumer consumer, Map Params, int start,int size,ResultSetSize resultSetSize);

	public Dispute getDispute(Integer id);
	
	public Settlement getSettlement(Integer id);

	public ArrayList<Settlement> getSettlements(Merchant merchant, Map Params, int start,int size,ResultSetSize resultSetSize);

	ArrayList<Transactions> getDeliveredTransactions();

	public Merchant getMerchantForAuthorization(Object object,int id);

	Consumer getConsumerForAuthorization(Object object, int id);
}