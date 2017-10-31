package com.i2india.serviceImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.i2india.DAO.OrderDAO;
import com.i2india.DAO.UserDAO;
import com.i2india.Domain.Bank;
import com.i2india.Domain.BankContract;
import com.i2india.Domain.Consumer;
import com.i2india.Domain.Dispute;
import com.i2india.Domain.Merchant;
import com.i2india.Domain.MerchantContract;
import com.i2india.Domain.Settlement;
import com.i2india.Domain.Transactions;
import com.i2india.ErrorUtils.CustomApplicationException;
import com.i2india.ErrorUtils.CustomDataException;
import com.i2india.ErrorUtils.CustomValidationException;
import com.i2india.ErrorUtils.ErrorConstants;
import com.i2india.Service.OrderService;
import com.i2india.ServiceUtils.DisputeStates;
import com.i2india.ServiceUtils.Library;
import com.i2india.ServiceUtils.OrderState;
import com.i2india.ServiceUtils.ResultSetSize;
import com.i2india.ServiceUtils.URLConstants;

@Service
@SuppressWarnings("rawtypes") 
public class OrderServiceImpl implements OrderService {
	private static final int WAITING_PERIOD_IN_HOURS = 24;

	@Autowired
	private OrderDAO orderDao;

	@Autowired
	private UserDAO userDao;

	private int index=0;


	static Logger log = Logger.getLogger(OrderServiceImpl.class.getName());

	public void setUserDao(UserDAO userDao)
	{
		this.userDao = userDao;
	}

	public void setOrderDao(OrderDAO orderDao)
	{
		this.orderDao = orderDao;
	}

	/*
	If paymode is specified by merchant then its passed on Else 
	Its optional
	The transaction is created
	The transactions created should be closed automatcally within some time limit
	 */
	@Transactional("transactionManager")
	public ArrayList<String> checkIn(String parm[], Consumer consumer, Merchant merchant,Bank bank) {

		ArrayList<String> encData = new ArrayList<String>();
		Transactions transaction = new Transactions();
		transaction.setMerchant(merchant);
		transaction.setConsumer(consumer);

		//Setting parameters
		transaction.setMerchant_order_id(parm[1]);
		//country

		//Store bank detailsS if received from merchant
		Transactions temp = orderDao.checkTransactionExsist(merchant, transaction); //If transactions exsist then throw error
		if(temp!=null)
			throw new CustomDataException(ErrorConstants.MERCHANT_INVALID_ORDER_ID,ErrorConstants.STR_MERCHANT_INVALID_ORDER_ID);

		transaction.setCustomer_phonenumber(Long.parseLong(parm[7]));
		transaction.setConsumer(consumer);
		transaction.setMerchant_country(parm[3]);
		transaction.setMerchant_currency(parm[4]);
		transaction.setMerchant_activity_time(new Date());
		transaction.setMerchant_amount(Double.parseDouble(parm[8]));
		transaction.setMerchant_success_url(parm[9]);
		transaction.setMerchant_fail_url(parm[10]);
		transaction.setMerchant_paymode(parm[12]);
		transaction.setMerchant_details(parm[13]);

		transaction.setStatus(OrderState.INITIATED);
		orderDao.addTransactions(transaction);

		log.info("Transaction id : "+transaction.getTransaction_id());
		log.debug("Transaction id : "+transaction.getTransaction_id());
		log.info("Merchant id : "+transaction.getMerchant().getUser_id());
		log.debug("Merchant id : "+transaction.getMerchant().getUser_id());
		log.info("Customer id : "+transaction.getConsumer().getUser_id());
		log.debug("Customer id : "+transaction.getConsumer().getUser_id());
		log.info("Merchant amount : "+transaction.getMerchant_amount());
		log.debug("Merchant amount : "+transaction.getMerchant_amount());
		log.info("Time : "+transaction.getMerchant_activity_time());
		log.debug("Time : "+transaction.getMerchant_activity_time());

		//Populating parameter for bank have to change the hard coded values
		encData.add(bank.getBank_merchant_id()); 
		encData.add("DOM"); //Have to change latter
		encData.add(transaction.getMerchant_country());
		encData.add(transaction.getMerchant_currency());
		encData.add(Double.toString(transaction.getMerchant_amount()));
		encData.add(transaction.getMerchant_details());
		encData.add(URLConstants.SBI_SUCCESS_URL); //change latter
		encData.add(URLConstants.SBI_FAIL_URL);
		encData.add("SBIEPAY");
		encData.add(Integer.toString(transaction.getTransaction_id()));
		encData.add(Integer.toString((transaction.getConsumer().getUser_id())));
		encData.add(transaction.getMerchant_paymode());
		encData.add("ONLINE");
		encData.add("ONLINE");

		return encData;
	}

	/*
		This module might is bank specific
	 */
	@Transactional("transactionManager")
	public  String checkOut(Bank bank,Transactions transaction,String parm[]) {



		//Time check
		String paymode;

		double amount;

		if(transaction==null)
			throw new CustomDataException(ErrorConstants.TRANSACTION_NOT_FOUND,ErrorConstants.STR_TRANSACTION_NOT_FOUND);

		if(!transaction.getStatus().equals(OrderState.INITIATED))
			throw new CustomDataException(ErrorConstants.TRANSACTION_ALREADY_PROCESSED,ErrorConstants.STR_TRANSACTION_ALREADY_PROCESSED);

		ArrayList<String> output = new ArrayList<String>();
		transaction.setBank_activity_time(new Date());
		transaction.setReference_id(parm[1]);
		transaction.setTransaction_status(parm[2]);
		amount = Double.parseDouble(parm[3]);
		transaction.setTransaction_amount(Double.parseDouble(parm[3]));
		paymode = parm[4];
		transaction.setTransaction_currency(parm[4]);
		transaction.setTransaction_paymode(parm[5]);
		transaction.setTransaction_other_details(parm[6]);
		transaction.setTransaction_status_description(parm[7]);

		transaction.setMerchant_reconcilation_status("Underprocess");
		//Reconcilation status


		//Calculation of Bank amount vcod amount merchant amount etc .. Demo
		Merchant merchant = transaction.getMerchant();
		MerchantContract merchantContract = orderDao.getMerchantContract(merchant, transaction.getTransaction_paymode());
		BankContract bankContract = orderDao.getBankContract(bank,  transaction.getTransaction_paymode());
		if(merchantContract==null||bankContract==null) //Change the logic Exceptions !
			throw new CustomDataException(ErrorConstants.TRANSACTION_NO_CONTRACT_FOUND,ErrorConstants.STR_TRANSACTION_NO_CONTRACT_FOUND);
		transaction.setBank_amount((amount*(bankContract.getRate()))/100);
		transaction.setVcod_amount((amount*(merchantContract.getRate()))/100);
		transaction.setMerchantcontract(merchantContract);
		transaction.setBankcontract(bankContract);
		//Computed on merchant reconiclaition
		transaction.setMerchant_reconcilation_amount(transaction.getTransaction_amount()-transaction.getBank_amount()-transaction.getVcod_amount());

		//Completion of order etc ie:update
		if(transaction.getTransaction_status().equals(OrderState.SUCCESS)||transaction.getTransaction_status().equals(OrderState.FAIL))
		{
			if(transaction.getTransaction_status().equals(OrderState.SUCCESS))
			{
				transaction.setStatus(OrderState.SUCCESS);
			}
			else 
				transaction.setStatus(OrderState.FAIL);
		}

		orderDao.updateTransactions(transaction);

		log.info("Transaction id : "+transaction.getTransaction_id());
		log.debug("Transaction id : "+transaction.getTransaction_id());
		log.info("Transaction amount : "+transaction.getTransaction_amount());
		log.debug("Transaction amount : "+transaction.getTransaction_amount());
		log.info("Bank activity Time : "+transaction.getBank_activity_time());
		log.debug("Bank activity Time: "+transaction.getBank_activity_time());
		log.info("Status : "+transaction.getTransaction_status());
		log.debug("Status "+transaction.getTransaction_status());

		//In addition to this we may give some parameters from orders table

		output.add(transaction.getMerchant_order_id()); //merchant order id
		output.add(Integer.toString(transaction.getTransaction_id())); //out transaction id
		output.add(transaction.getStatus()); //status
		output.add(Double.toString(transaction.getTransaction_amount())); //Amount
		output.add(transaction.getTransaction_currency()); // currency
		output.add(transaction.getTransaction_paymode()); // Paymode
		output.add(transaction.getTransaction_other_details()); // Other Details
		output.add(transaction.getTransaction_status_description()); // Reason

		ArrayList<String> outputParamList =  output;
		//Encryption 
		try {
			return Library.getParamterInString(outputParamList, merchant.getSecret_key());
		} catch (Exception e) {
			throw new CustomApplicationException(ErrorConstants.INTERNAL_ERROR,ErrorConstants.STR_INTERNAL_ERROR);
		}
	}


	//Complete refund
	@Transactional("transactionManager")
	public Boolean completeCancelOrder(int transaction_id)
	{
		Transactions transaction = orderDao.getTransaction(transaction_id);

		if(transaction==null)
		{
			throw new CustomDataException(ErrorConstants.REFUND_INVALID_TRANSACTION_ID,ErrorConstants.STR_REFUND_INVALID_TRANSACTION_ID);

		}else if(transaction.getStatus().equals(OrderState.REFUND))
		{
			transaction.setMerchant_activity_time(new Date());
			//Refund calculation

			transaction.setMerchant_amount(transaction.getMerchant_amount());
			transaction.setStatus(OrderState.CLOSED);
			orderDao.addTransactions(transaction);

			return true; //Refund Intiated
		}else

		{
			return false;
		}
	}

	//Cancel order
	@Transactional("transactionManager")
	public int cancelOrder(int transaction_id,int merchant_id,int amount,String reason)
	{
		Merchant merchant = userDao.getMerchant(merchant_id);
		Transactions transaction = orderDao.getTransactionByMerchant(merchant, transaction_id);
		if(transaction==null)
		{
			throw new CustomDataException(ErrorConstants.TRANSACTION_NOT_FOUND,ErrorConstants.STR_TRANSACTION_NOT_FOUND); //The order doesn't exsist
		}else if(transaction.getStatus().equals(OrderState.CLOSED))
		{
			throw new CustomDataException(ErrorConstants.TRANSACTION_ALREADY_PROCESSED, ErrorConstants.STR_TRANSACTION_ALREADY_PROCESSED); ///The order is processed already
		}else if(transaction.getStatus().equals(OrderState.REFUND))
		{
			throw new CustomDataException(ErrorConstants.TRANSACTION_REFUND_INITIATED_ALREADY, ErrorConstants.STR_TRANSACTION_REFUND_INITIATED_ALREADY); //Refund Intiated
		}else if(transaction.getStatus().equals(OrderState.SUCCESS)) //Then refund can be processed
		{
			if(amount>transaction.getMerchant_amount()||amount<=0)
				throw new CustomDataException(ErrorConstants.TRANSACTION_REFUND_INVALID_AMOUNT,ErrorConstants.STR_TRANSACTION_REFUND_INVALID_AMOUNT); //Refund Intiated //Refund Amount is larger.

			transaction.setRefund_amount(amount);
			transaction.setStatus(OrderState.REFUND);
			orderDao.updateTransactions(transaction);
			Dispute dispute = new Dispute();
			dispute.setTransaction(transaction);
			dispute.setActivity_time(new Date());
			dispute.setTitle("Merchant Refund");
			dispute.setDescription(reason);
			dispute.setDisputed_amount(transaction.getMerchant_amount());
			dispute.setRefund_policy_id(0);
			dispute.setStatus("INITIATED");
			dispute.setType("MERCHANT_REFUND");
			orderDao.addDispute(dispute);
			return 0;
		}
		return 1;
	}
	
	/*
	 * Authorization should be built
	 */
	@Override
	@Transactional("transactionManager")
	public void addConsumerDispute(Dispute dispute,int transactionId)
	{
		dispute.setDispute_id(0);
		Transactions transaction = orderDao.getTransaction(transactionId);
		if(transaction==null)
			throw new CustomApplicationException(ErrorConstants.INVALID_TRANSACTION_ID,ErrorConstants.STR_INVALID_TRANSACTION_ID);
		if(!transaction.getStatus().equals(OrderState.DELIVERED))
			throw new CustomApplicationException(ErrorConstants.INVALID_TRANSACTION_ID,ErrorConstants.STR_INVALID_TRANSACTION_ID);
		//Description , dispute amount
		if(dispute.getDisputed_amount() < 1 || dispute.getDisputed_amount() > transaction.getTransaction_amount())
			throw new CustomDataException(ErrorConstants.INVALID_DISPUTE_AMOUNT,ErrorConstants.STR_INVALID_DISPUTE_AMOUNT);
		dispute.setStatus(DisputeStates.OPEN);
		dispute.setType(DisputeStates.PRODUCT_DISPUTE);
		dispute.setRefund_policy_id(0);
		dispute.setActivity_time(new Date());
		transaction.setStatus(OrderState.DISPUTE);
		
		dispute.setTransaction(transaction);
		orderDao.addDispute(dispute);
		orderDao.updateTransactions(transaction);
	}
	
	

	/*
	 * (non-Javadoc)
	 * @see com.i2india.Service.OrderService#getTransactions(int)
	 * Loading transactions 
	 */
	@Override
	@Transactional("transactionManager")
	public Transactions getTransactions(int id) {
		Transactions transaction=  (Transactions)orderDao.getTransaction(id);
		Hibernate.initialize(transaction.getMerchant()); //This used to load objects which are marked lazy
		return transaction;
	}

	/*
	 * (non-Javadoc)
	 * @see com.i2india.Service.OrderService#getAllTransactions(com.i2india.Domain.Merchant, java.lang.String, int, int, java.lang.String, java.lang.String, java.lang.String, int, double, double, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 * Transaction filter.
	 * Adds parameter to map based on parameter values passed
	 * Returns the transactions in the form of array of map
	 */
	@Override
	@Transactional("transactionManager")
	public Map getAllTransactionsForMerchant(Merchant merchant,String status,int start,int size,String fromDateParm,String toDateParm,String onDateParm,int transactionId,double startAmount,double endAmount,String merchantOrderId,String customerEmail,String sort,String order)
	{

		Map<String,Object> Response = new HashMap<String,Object>();
		Map<String,Object> parm = new HashMap<String,Object>();
		Consumer consumer =null;
		Date fromDate=null;
		Date toDate=null;
		Date onDate=null;
		
		if(transactionId!=-1)
			parm.put("transactionId", transactionId);	
		else if(!merchantOrderId.equals(""))
		{
			parm.put("merchantOrderId", merchantOrderId);
			start=0;
			size=1;
		}else
		{
			if(!status.equals("")) // Check only valid success states
				parm.put("status", status);
			if(!sort.equals(""))
				parm.put("sort", sort);
			if(!order.equals(""))
				parm.put("order", order);
			try
			{
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				if(!fromDateParm.equals(""))
					fromDate = df.parse(fromDateParm);

				if(!toDateParm.equals(""))
					toDate = df.parse(toDateParm);

				if(!onDateParm.equals(""))
					onDate = df.parse(onDateParm);

			}catch(IllegalArgumentException ex)
			{
				throw new CustomValidationException(ErrorConstants.INVALID_DATE, ErrorConstants.STR_INVALID_DATE); 
			}catch(ParseException ex)
			{
				throw new CustomValidationException(ErrorConstants.INVALID_DATE, ErrorConstants.STR_INVALID_DATE); 
			}

			if(onDate!=null)
			{
				parm.put("onDate", onDate);
			}else
			{
				if(fromDate!=null)
					parm.put("fromDate", fromDate);
				if(toDate!=null)
					parm.put("toDate", toDate);
			}

			if(startAmount != -1)
				parm.put("startAmount", startAmount);
			if(endAmount != -1)
				parm.put("endAmount", endAmount);
			if(!customerEmail.equals(""))
			{
				consumer = userDao.getConsumerByUsername(customerEmail);
				if(consumer==null)
				{
					Map<String,Object> temp = new HashMap<String,Object>();
					temp.put("Message", "No results found ! ");
					temp.put("Transactions", null);
					return temp;
				}
			}

		}

		ResultSetSize resultSetSize = new ResultSetSize();
		Map<String,Object> array[]=null;
		ArrayList<Transactions> transactions=this.orderDao.getTransactions(merchant,consumer, parm,start,size,resultSetSize);

		if(transactions.size()==0)
		{
			Map<String,Object> temp = new HashMap<String,Object>();
			temp.put("Message", "No results found ! ");
			temp.put("Transactions", null);
			return temp;
		}
		array = new Map[transactions.size()];
		int i;
		for(i=0;i<transactions.size();i++)
		{
			Map<String,Object> temp = new HashMap<String,Object>();
			Transactions trans= transactions.get(i);
			temp.put("transaction_id", trans.getTransaction_id());
			temp.put("merchant_order_id", trans.getMerchant_order_id());
			temp.put("status", trans.getStatus());
			temp.put("amount", trans.getTransaction_amount());
			temp.put("date",trans.getMerchant_activity_time().toString());
			array[i]=temp;
		}

		if(resultSetSize.getRowCount()!=0){
			Response.put("resultSetSize", resultSetSize);
		}
		
		Response.put("Transactions", array);
		
		return Response;
	}

	/*
	 * (non-Javadoc)
	 * @see com.i2india.Service.OrderService#getAllTransactions(com.i2india.Domain.Merchant, java.lang.String, int, int, java.lang.String, java.lang.String, java.lang.String, int, double, double, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 * Dispute filter.
	 * Adds parameter to map based on parameter values passed
	 * Returns the dispute/refunds in the form of array of map
	 * Identifies based on type whether its request for dispute or refund
	 */
	@Override
	@Transactional("transactionManager")
	public Map getAllDisputesForMerchant(Merchant merchant,String status,int start,int size,String fromDateParm,String toDateParm,String onDateParm,int disputeId,double startAmount,double endAmount,String merchantOrderId,String customerEmail,int transactionId,String type,String sort,String order)
	{
		Map<String,Object> parm = new HashMap<String,Object>();
		Map<String,Object> Response = new HashMap<String,Object>();
		Date fromDate=null;
		Date toDate=null;
		Date onDate=null;
		Consumer consumer = null;
		if(disputeId!=-1)
			parm.put("disputeId", disputeId);
		else if(transactionId>0)
		{
			parm.put("transactionId", transactionId);
			start=0;
			size=1;
		}
		else if(!merchantOrderId.equals(""))
		{
			parm.put("merchantOrderId", merchantOrderId);
			start=0;
			size=1;
		}else
		{
			if(!type.equals(""))
			{
				log.debug("type : "+type);
				parm.put("type", type);
			}
			if(!sort.equals(""))
				parm.put("sort", sort);
			if(!order.equals(""))
				parm.put("order", order);
			if(!status.equals("")) // Check only valid success states
				parm.put("status", status);
			try
			{
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				if(!fromDateParm.equals(""))
					fromDate = df.parse(fromDateParm);

				if(!toDateParm.equals(""))
					toDate = df.parse(toDateParm);

				if(!onDateParm.equals(""))
					onDate = df.parse(onDateParm);

			}catch(IllegalArgumentException ex)
			{
				//new exception
				throw new CustomValidationException(ErrorConstants.INVALID_DATE, ErrorConstants.STR_INVALID_DATE); 
			}catch(ParseException ex)
			{
				throw new CustomValidationException(ErrorConstants.INVALID_DATE, ErrorConstants.STR_INVALID_DATE); 
			}

			if(onDate!=null)
			{
				parm.put("onDate", onDate);
			}else
			{
				if(fromDate!=null)
					parm.put("fromDate", fromDate);
				if(toDate!=null)
					parm.put("toDate", toDate);
			}

			if(startAmount != -1)
				parm.put("startAmount", startAmount);
			if(endAmount != -1)
				parm.put("endAmount", endAmount);
			if(!customerEmail.equals(""))
			{
			    consumer = userDao.getConsumerByUsername(customerEmail);
				if(consumer==null)
				{
					Map<String,Object> temp = new HashMap<String,Object>();
					temp.put("Message", "No results found ! ");
					temp.put("Transactions", null);
					return temp;
				}
			}

		}

		//This object is passed to api to get the total number of rows when start=0
		ResultSetSize resultSetSize = new ResultSetSize();	

		Map array[]=null;
		ArrayList<Dispute> disputes=this.orderDao.getDisputes(merchant,consumer,parm,start,size,resultSetSize);

		if(disputes.size()==0)
		{
			Map<String,Object> temp = new HashMap<String,Object>();
			temp.put("Message", "No results found ! ");
			temp.put("Disputes", null);
			return temp;
		}
		array = new Map[disputes.size()];
		int i;
		for(i=0;i<disputes.size();i++)
		{
			Map<String,Object> temp = new HashMap<String,Object>();
			Dispute dispute= disputes.get(i);
			temp.put("dispute_id", dispute.getDispute_id());
			temp.put("transaction_id", dispute.getTransaction().getTransaction_id());
			temp.put("status", dispute.getStatus());
			temp.put("title", dispute.getTitle());
			temp.put("amount", dispute.getTransaction().getTransaction_amount());
			temp.put("type", dispute.getType());
			if(type.equals(DisputeStates.MERCHANT_REFUND))
				temp.put("refund_amount", dispute.getTransaction().getRefund_amount());
			else if(type.equals(DisputeStates.PRODUCT_DISPUTE))
				temp.put("dispute_amount", dispute.getDisputed_amount());
			temp.put("date",dispute.getActivity_time().toString());
			array[i]=temp;
		}

		Response.put("Disputes", array);
		if(resultSetSize.getRowCount()!=0)
		Response.put("resultSetSize", resultSetSize);

		return Response;
	}

	/*
	 * (non-Javadoc)
	 * @see com.i2india.Service.OrderService#getAllTransactions(com.i2india.Domain.Merchant, java.lang.String, int, int, java.lang.String, java.lang.String, java.lang.String, int, double, double, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 * Settlements filter.
	 * Adds parameter to map based on parameter values passed
	 * Returns the settlements in the form of array of map
	 */
	@Override
	@Transactional("transactionManager")
	public Map getAllSettlementsForMerchant(Merchant merchant,String status,int start,int size,String fromDateParm,String toDateParm,String onDateParm,int settlementId,double startAmount,double endAmount,String sort,String order)
	{
		Map<String,Object> parm = new HashMap<String,Object>();
		Map<String,Object> Response = new HashMap<String,Object>();
		Map array[]=null;
		Date fromDate=null;
		Date toDate=null;
		Date onDate=null;
		log.debug("settelement_id : "+settlementId);
		if(settlementId!=-1)
			parm.put("settlementId", settlementId);
		else
		{
			if(!status.equals("")) // Check only valid success states
				parm.put("status", status);
			if(!sort.equals(""))
				parm.put("sort", sort);
			if(!order.equals(""))
				parm.put("order", order);
			try
			{
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				if(!fromDateParm.equals(""))
					fromDate = df.parse(fromDateParm);
	
				if(!toDateParm.equals(""))
					toDate =df.parse(toDateParm);
	
				if(!onDateParm.equals(""))
					onDate = df.parse(onDateParm);
	
			}catch(IllegalArgumentException ex)
			{
				throw new CustomValidationException(ErrorConstants.INVALID_DATE, ErrorConstants.STR_INVALID_DATE); 
			}catch(ParseException ex)
			{
				throw new CustomValidationException(ErrorConstants.INVALID_DATE, ErrorConstants.STR_INVALID_DATE); 
			}
	
			if(onDate!=null)
			{
				parm.put("onDate", onDate);
			}else
			{
				if(fromDate!=null)
					parm.put("fromDate", fromDate);
				if(toDate!=null)
					parm.put("toDate", toDate);
			}
	
			if(startAmount != -1)
				parm.put("startAmount", startAmount);
			if(endAmount != -1)
				parm.put("endAmount", endAmount);
		}


		//This object is passed to api to get the total number of rows when start=0
		ResultSetSize resultSetSize = new ResultSetSize();	
		ArrayList<Settlement> settlements=this.orderDao.getSettlements(merchant, parm,start,size,resultSetSize);

		if(settlements.size()==0)
		{
			Map<String,Object> temp = new HashMap<String,Object>();
			temp.put("Message", "No results found ! ");
			temp.put("Settlements", null);
			return temp;
		}
		array = new Map[settlements.size()];
		int i;
		for(i=0;i<settlements.size();i++)
		{
			Map<String,Object> temp = new HashMap<String,Object>();
			Settlement settlement= settlements.get(i);
			temp.put("settlement_id", settlement.getSettlement_id());
			temp.put("status", settlement.getStatus());
			temp.put("amount", settlement.getAmount());
			temp.put("date",settlement.getActivity_time().toString());
			temp.put("transactions",settlement.getTransactions());
			array[i]=temp;
		}

		Response.put("Settlements", array);
		
		if(resultSetSize.getRowCount()!=0){
			Response.put("resultSetSize", resultSetSize);
		}

		return Response;
	}

	/*
	 * 
	 * (non-Javadoc)
	 * Customer Portal filters Transactions and disputes
	 */
	
	@Override
	@Transactional("transactionManager")
	public Map getAllTransactionsForConsumer(Consumer consumer,String status,int start,int size,String fromDateParm,String toDateParm,String onDateParm,int transactionId,double startAmount,double endAmount,String merchantOrderId,String organizationName,String sort,String order)
	{

		Map<String,Object> Response = new HashMap<String,Object>();
		Map<String,Object> parm = new HashMap<String,Object>();
		Merchant merchant =null;
		Date fromDate=null;
		Date toDate=null;
		Date onDate=null;
		
		if(transactionId!=-1)
			parm.put("transactionId", transactionId);	
		else if(!merchantOrderId.equals(""))
		{
			parm.put("merchantOrderId", merchantOrderId);
			start=0;
			size=1;
		}else
		{
			if(!status.equals("")) // Check only valid success states
				parm.put("status", status);
			if(!sort.equals(""))
				parm.put("sort", sort);
			if(!order.equals(""))
				parm.put("order", order);
			try
			{
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				if(!fromDateParm.equals(""))
					fromDate = df.parse(fromDateParm);

				if(!toDateParm.equals(""))
					toDate = df.parse(toDateParm);

				if(!onDateParm.equals(""))
					onDate = df.parse(onDateParm);

			}catch(IllegalArgumentException ex)
			{
				throw new CustomValidationException(ErrorConstants.INVALID_DATE, ErrorConstants.STR_INVALID_DATE); 
			}catch(ParseException ex)
			{
				throw new CustomValidationException(ErrorConstants.INVALID_DATE, ErrorConstants.STR_INVALID_DATE); 
			}

			if(onDate!=null)
			{
				parm.put("onDate", onDate);
			}else
			{
				if(fromDate!=null)
					parm.put("fromDate", fromDate);
				if(toDate!=null)
					parm.put("toDate", toDate);
			}

			if(startAmount != -1)
				parm.put("startAmount", startAmount);
			if(endAmount != -1)
				parm.put("endAmount", endAmount);
			if(!organizationName.equals(""))
			{
				merchant = userDao.getMerchantByOrganizationName(organizationName);
				if(merchant==null)
				{
					Map<String,Object> temp = new HashMap<String,Object>();
					temp.put("Message", "No results found ! ");
					temp.put("Transactions", null);
					return temp;
				}
			}

		}

		ResultSetSize resultSetSize = new ResultSetSize();
		Map<String,Object> array[]=null;
		ArrayList<Transactions> transactions=this.orderDao.getTransactions(merchant,consumer, parm,start,size,resultSetSize);

		if(transactions.size()==0)
		{
			Map<String,Object> temp = new HashMap<String,Object>();
			temp.put("Message", "No results found ! ");
			temp.put("Transactions", null);
			return temp;
		}
		array = new Map[transactions.size()];
		int i;
		for(i=0;i<transactions.size();i++)
		{
			Map<String,Object> temp = new HashMap<String,Object>();
			Transactions trans= transactions.get(i);
			temp.put("transaction_id", trans.getTransaction_id());
			temp.put("merchant_order_id", trans.getMerchant_order_id());
			temp.put("status", trans.getStatus());
			temp.put("amount", trans.getTransaction_amount());
			temp.put("date",trans.getMerchant_activity_time().toString());
			array[i]=temp;
		}

		if(resultSetSize.getRowCount()!=0){
			Response.put("resultSetSize", resultSetSize);
		}
		
		Response.put("Transactions", array);
		
		return Response;
	}

	/*
	 * (non-Javadoc)
	 * @see com.i2india.Service.OrderService#getAllTransactions(com.i2india.Domain.Merchant, java.lang.String, int, int, java.lang.String, java.lang.String, java.lang.String, int, double, double, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 * Dispute filter.
	 * Adds parameter to map based on parameter values passed
	 * Returns the dispute/refunds in the form of array of map
	 * Identifies based on type whether its request for dispute or refund
	 */
	@Override
	@Transactional("transactionManager")
	public Map getAllDisputesForConsumer(Consumer consumer,String status,int start,int size,String fromDateParm,String toDateParm,String onDateParm,int disputeId,double startAmount,double endAmount,String merchantOrderId,String organizationName,int transactionId,String type,String sort,String order)
	{
		Map<String,Object> parm = new HashMap<String,Object>();
		Map<String,Object> Response = new HashMap<String,Object>();
		Date fromDate=null;
		Date toDate=null;
		Date onDate=null;
		Merchant merchant = null;
		if(disputeId!=-1)
			parm.put("disputeId", disputeId);
		else if(transactionId>0)
		{
			parm.put("transactionId", transactionId);
			start=0;
			size=1;
		}
		else if(!merchantOrderId.equals(""))
		{
			parm.put("merchantOrderId", merchantOrderId);
			start=0;
			size=1;
		}else
		{
			if(!type.equals(""))
			{
				log.debug("type : "+type);
				parm.put("type", type);
			}
			if(!sort.equals(""))
				parm.put("sort", sort);
			if(!order.equals(""))
				parm.put("order", order);
			if(!status.equals("")) // Check only valid success states
				parm.put("status", status);
			try
			{
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				if(!fromDateParm.equals(""))
					fromDate = df.parse(fromDateParm);

				if(!toDateParm.equals(""))
					toDate = df.parse(toDateParm);

				if(!onDateParm.equals(""))
					onDate = df.parse(onDateParm);

			}catch(IllegalArgumentException ex)
			{
				//new exception
				throw new CustomValidationException(ErrorConstants.INVALID_DATE, ErrorConstants.STR_INVALID_DATE); 
			}catch(ParseException ex)
			{
				throw new CustomValidationException(ErrorConstants.INVALID_DATE, ErrorConstants.STR_INVALID_DATE); 
			}

			if(onDate!=null)
			{
				parm.put("onDate", onDate);
			}else
			{
				if(fromDate!=null)
					parm.put("fromDate", fromDate);
				if(toDate!=null)
					parm.put("toDate", toDate);
			}

			if(startAmount != -1)
				parm.put("startAmount", startAmount);
			if(endAmount != -1)
				parm.put("endAmount", endAmount);
			if(!organizationName.equals(""))
			{	
				merchant = userDao.getMerchantByOrganizationName(organizationName);
				if(merchant==null)
				{
					Map<String,Object> temp = new HashMap<String,Object>();
					temp.put("Message", "No results found ! ");
					temp.put("Disputes", null);
					return temp;
				}
			}
			
		}

		//This object is passed to api to get the total number of rows when start=0
		ResultSetSize resultSetSize = new ResultSetSize();	

		Map array[]=null;
		ArrayList<Dispute> disputes=this.orderDao.getDisputes(merchant,consumer,parm,start,size,resultSetSize);

		if(disputes.size()==0)
		{
			Map<String,Object> temp = new HashMap<String,Object>();
			temp.put("Message", "No results found ! ");
			temp.put("Disputes", null);
			return temp;
		}
		array = new Map[disputes.size()];
		int i;
		for(i=0;i<disputes.size();i++)
		{
			Map<String,Object> temp = new HashMap<String,Object>();
			Dispute dispute= disputes.get(i);
			temp.put("dispute_id", dispute.getDispute_id());
			temp.put("transaction_id", dispute.getTransaction().getTransaction_id());
			temp.put("status", dispute.getStatus());
			temp.put("title", dispute.getTitle());
			temp.put("amount", dispute.getTransaction().getTransaction_amount());
			temp.put("type", dispute.getType());
			if(type.equals(DisputeStates.MERCHANT_REFUND))
				temp.put("refund_amount", dispute.getTransaction().getRefund_amount());
			else if(type.equals(DisputeStates.PRODUCT_DISPUTE))
				temp.put("dispute_amount", dispute.getDisputed_amount());
			temp.put("date",dispute.getActivity_time().toString());
			array[i]=temp;
		}

		Response.put("Disputes", array);
		if(resultSetSize.getRowCount()!=0)
		Response.put("resultSetSize", resultSetSize);

		return Response;
	}
	
	//For spring batch processing
	@Override
	@Transactional
	public ArrayList<Transactions> getClosedTransactions() {

		ArrayList<Transactions> transactions = orderDao.getDeliveredTransactions();
		ArrayList<Transactions> closedTransactions = new ArrayList<Transactions>();
		
		if(transactions!=null)
		{
			for(Transactions transaction : transactions)
			{
				log.debug("Transaction delivery time is of id  = " + transaction.getTransaction_id() + "and time is " + transaction.getDelivery_time());
				if(isCustomerSatisfied(transaction.getDelivery_time()))
				{
					transaction.setStatus("CLOSED");
					closedTransactions.add(transaction);
				}
			}
		}
		
		return closedTransactions;
	}

	private boolean isCustomerSatisfied(Date date) 
	{
		String delivered  = date.toString();
		Calendar currentTime = Calendar.getInstance();
		currentTime.setTime(new Date());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date =  df.parse(delivered);
			log.debug("Date is transactionClose" + date);

		} catch (ParseException e) {
			
			e.printStackTrace();

			return true;

		}

		Calendar deliveredTime = Calendar.getInstance();

		deliveredTime.setTime(date);


		deliveredTime.add(Calendar.HOUR,WAITING_PERIOD_IN_HOURS);

		//time2 is lesser than time1


		return (currentTime.getTime().compareTo(deliveredTime.getTime())>0);


	}
	
	//For spring batch processing
	@Override
	@Transactional
	public Transactions getTransaction() {

		ArrayList<Transactions> closedTransactions=null;
		if(closedTransactions == null)
		{
		closedTransactions = new ArrayList<Transactions>();
		closedTransactions = this.getClosedTransactions();
		}


		if (index < closedTransactions.size()) {
			Transactions transaction  = closedTransactions.get(index++);
			return transaction;
		} else {
			return null;
		}
	}


	@Override
	@Transactional
	public void updateTransaction(Transactions transaction) {
		orderDao.updateTransactions(transaction);

	}
	
	//For authorization 
	@Override
	@Transactional
	public Merchant getMerchantForAuthorization(Object object,int id) {
		return orderDao.getMerchantForAuthorization(object, id);
	}
	
	@Override
	@Transactional
	public Consumer getConsumerForAuthorization(Object object,int id) {
		return orderDao.getConsumerForAuthorization(object, id);
	}
	
	//Fetching transactions,settlements and disputes
	
	@Override
	@Transactional
	public Dispute getDisputeById(int id) {
		return orderDao.getDispute(id);
	}
	
	@Override
	@Transactional
	public Settlement getSettlementById(int id) {
		return orderDao.getSettlement(id);
	}


	@Override
	@Transactional
	public Transactions getTransactionById(int id) {
		Transactions transaction = orderDao.getTransaction(id);
		Hibernate.initialize(transaction.getConsumer());
		return transaction;
	}



}
