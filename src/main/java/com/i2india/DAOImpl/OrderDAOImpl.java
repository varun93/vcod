package com.i2india.DAOImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.i2india.DAO.OrderDAO;
import com.i2india.Domain.Bank;
import com.i2india.Domain.BankContract;
import com.i2india.Domain.Consumer;
import com.i2india.Domain.Dispute;
import com.i2india.Domain.Merchant;
import com.i2india.Domain.MerchantContract;
import com.i2india.Domain.Settlement;
import com.i2india.Domain.Transactions;
import com.i2india.ServiceUtils.DisputeStates;
import com.i2india.ServiceUtils.ResultSetSize;

@Repository
@SuppressWarnings("rawtypes") 
public class OrderDAOImpl implements OrderDAO {

	@Autowired
    private SessionFactory sessionFactory;
	
	static Logger log = Logger.getLogger(OrderDAOImpl.class.getName());
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	//Add  methods
	@Override
	public void addTransactions(Transactions transaction) {
		this.sessionFactory.getCurrentSession().save(transaction);
	}

	@Override
	public void addDispute(Dispute dispute) {
		this.sessionFactory.getCurrentSession().save(dispute);
		
	}
	
	//List methods
	
	@SuppressWarnings("unchecked")
	public ArrayList<Transactions> getAllTransactions() {
		return (ArrayList<Transactions>) this.sessionFactory.getCurrentSession().createQuery("from Transactions").list();
	}
	
	//Get Methods

	public Transactions getTransaction(Integer id) {
		return (Transactions) this.sessionFactory.getCurrentSession().get(Transactions.class,id);
	}
	
	public Bank getBank(Integer id) {
		return (Bank) this.sessionFactory.getCurrentSession().get(Bank.class,id);
	}
	
	@Override
	public Dispute getDispute(Integer id) {
		return (Dispute) this.sessionFactory.getCurrentSession().get(Dispute.class,id);
	}
	
	@Override
	public Settlement getSettlement(Integer id) {
		return (Settlement) this.sessionFactory.getCurrentSession().get(Settlement.class,id);
	}
	
	
	public Transactions getTransactionByMerchant(Merchant merchant,int transaction_id)
	{
		String hql = "FROM Transactions O WHERE O.merchant = :merchant and O.transaction_id = :transaction_id";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("merchant", merchant);
		query.setParameter("transaction_id",transaction_id);
		@SuppressWarnings("unchecked")
		ArrayList<Transactions> results  = (ArrayList<Transactions>) query.list();
		log.debug("results size : "+results.size());
		if(results.size()==0)
		return null;
	     if(results.size()==1)
		{
				return (Transactions)results.get(0);
		}
		else
		return null;
	}

	//Get merchantContract by merchant and paymode
	public BankContract getBankContract(Bank bank,String paymode)
	{
		String hql = "FROM BankContract B WHERE B.bank = :bank1 and B.paymode = :paymode1";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("bank1", bank);
		query.setParameter("paymode1",paymode);
		@SuppressWarnings("unchecked")
		ArrayList<BankContract> results  = (ArrayList<BankContract>) query.list();
		log.debug("results size : "+results.size());
		if(results.size()==0)
		return null;
	     if(results.size()==1)
		{
			if(results.get(0).getClass().equals(BankContract.class))
			{
				return (BankContract) results.get(0);
			}else
			{
				return null;
			}			
		}
		else
		return null;
	}
	
	
	
	//Get merchantContract by merchant and paymode
	public MerchantContract getMerchantContract(Merchant merchant,String paymode)
	{
		String hql = "FROM MerchantContract M WHERE M.merchant = :merchant1 and M.paymode = :paymode1";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("merchant1", merchant);
		query.setParameter("paymode1",paymode);
		@SuppressWarnings("unchecked")
		ArrayList<MerchantContract> results  = (ArrayList<MerchantContract>) query.list();
		
		log.debug("results size : "+results.size());
		if(results.size()==0)
			return null;
	     if(results.size()==1)
		{
			if(results.get(0).getClass().equals(MerchantContract.class))
			{
				return (MerchantContract) results.get(0);
			}else
			{
				return null;
			}			
		}
		else
		return null;
	}
	
	
	//Update Methods
	
	public void updateTransactions(Transactions transaction) {
		this.sessionFactory.getCurrentSession().update(transaction);
	}	
	
	//Delete Methods
	
	
	public void deleteTransaction(Integer transactionId) {
		
		Transactions transaction = (Transactions) sessionFactory.getCurrentSession().load(
				Transactions.class, transactionId);
        if (null != transaction) {
        	this.sessionFactory.getCurrentSession().delete(transaction);
        }
	}
	
	
	//Check whether transaction exist or not
	public Transactions checkTransactionExsist(Merchant merchant,Transactions transaction) {
		String hql = "FROM Transactions T WHERE T.merchant = :merchant1 and T.merchant_order_id = :order_id";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("merchant1", merchant);
		query.setParameter("order_id", transaction.getMerchant_order_id());
		@SuppressWarnings("unchecked")
		ArrayList<Transactions> results  = (ArrayList<Transactions>) query.list();
		if(results.size()==0)
		return null;
		log.debug("results size : "+results.size());
	     if(results.size()==1)
		{
			if(results.get(0).getClass().equals(Transactions.class))
			{
				return (Transactions) results.get(0);
			}else
			{
				return null;
			}			
		}
		else
		return null;
	}
	
	
	//DAO implementation of transactions filter. The map contains all the parameters passed to transactions filter
	@Override
	public ArrayList<Transactions> getTransactions(Merchant merchant,Consumer consumer,Map Params,int start,int size,ResultSetSize resultSetSize) {
		
		Criteria search = this.sessionFactory.getCurrentSession().createCriteria(Transactions.class);
		search.setFirstResult(start);
		if(size>20)
			size=20;
		search.setMaxResults(size);
		/*		This if for ecache for caching
		search.setCacheable(true);
		search.setCacheRegion("Query.Transactions");
		*/
		if(merchant!=null)
			search.add(Restrictions.eq("merchant", merchant));
		if(consumer!=null)
			search.add(Restrictions.eq("consumer",consumer));
		
		if(Params.get("transactionId")!=null)
			search.add(Restrictions.eq("transaction_id", Params.get("transactionId")));
		else
		{
			if(Params.get("status")!=null)
				search.add(Restrictions.eq("status", Params.get("status")));
			if(Params.get("merchantOrderId")!=null)
				search.add(Restrictions.eq("merchant_order_id", Params.get("merchantOrderId")));
			
			if(Params.get("onDate")!=null)
			{
				Date startDate=(Date)Params.get("onDate");
				Calendar c = Calendar.getInstance();
				c.setTime(startDate);
				c.add(Calendar.DATE, 1);
				Date endDate=c.getTime();
				search.add(Restrictions.ge("merchant_activity_time", startDate));
				search.add(Restrictions.le("merchant_activity_time", endDate));
				log.debug(startDate+" - "+endDate);
			}
			if(Params.get("fromDate")!=null)
				search.add(Restrictions.ge("merchant_activity_time", (Date)Params.get("fromDate")));
			if(Params.get("toDate")!=null)
				search.add(Restrictions.le("merchant_activity_time", (Date)Params.get("toDate")));
			if(Params.get("startAmount")!=null)
				search.add(Restrictions.ge("transaction_amount", Params.get("startAmount")));
			if(Params.get("endAmount")!=null)
				search.add(Restrictions.le("transaction_amount", Params.get("endAmount")));
			if(Params.get("consumer_flag")!=null)
				search.add(Restrictions.eq("consumer", Params.get("consumer")));
			
			if(Params.get("sort")!=null)
			{
				String sort=(String)Params.get("sort");
				Boolean order=true;
				if(Params.get("order")!=null)
				{
					if(Params.get("order").equals("desc")||Params.get("order").equals("DESC"))
						order=false;
				}
				if(sort.equals("date"))
				{
					if(order)
					{
						search.addOrder(Order.asc("merchant_activity_time"));
					}else
					{
						search.addOrder(Order.desc("merchant_activity_time"));	
					}
				}else if(sort.equals("amount")){
					if(order)
					{
						search.addOrder(Order.asc("transaction_amount"));
					}else
					{
						search.addOrder(Order.desc("transaction_amount"));	
					}
				}else  if(sort.equals("status")){
					if(order)
					{
						search.addOrder(Order.asc("status"));
					}else
					{
						search.addOrder(Order.desc("status"));	
					}
				}else  if(sort.equals("consumer")){
					if(order)
					{
						search.addOrder(Order.asc("consumer"));
					}else
					{
						search.addOrder(Order.desc("consumer"));	
					}
				}
			}
		}
		
		log.debug(Params);
		@SuppressWarnings("unchecked")
		ArrayList<Transactions> results  = (ArrayList<Transactions>) search.list();	
		
		//Query to count number of rows
		if(start==0&&Params.get("merchantOrderId")==null){
			search.setProjection(Projections.rowCount());
	    	search.setResultTransformer(Criteria.PROJECTION);
	    	try
	    	{
	    		resultSetSize.setRowCount((Long)search.uniqueResult());
	    	}catch(NullPointerException ex)
	    	{
	    		//Do nothing
	    	}
		}
		
		log.debug("results size : "+results.size());
		this.sessionFactory.getStatistics().logSummary();
		return results;
	

	}
	
	
	
	//DAO implementation of disputes filter. The map contains all the parameters passed to disputes filter query
	@Override
	public ArrayList<Dispute> getDisputes(Merchant merchant,Consumer consumer,Map Params,int start,int size, ResultSetSize resultSetSize) {
		
		Criteria search = this.sessionFactory.getCurrentSession().createCriteria(Dispute.class);
		search.setFirstResult(start);
		if(size>20)
			size=20;
		search.setMaxResults(size);
		search.createAlias("transaction", "t");
		
		if(merchant!=null)
			search.add(Restrictions.eq("t.merchant", merchant));
		if(consumer!=null)
			search.add(Restrictions.eq("t.consumer",consumer));
		
		Boolean amountType=true;
		if(Params.get("disputeId")!=null)
			search.add(Restrictions.eq("dispute_id", Params.get("disputeId")));
		else if(Params.get("transactionId")!=null)
		{
			search.add(Restrictions.eq("t.transaction_id", Params.get("transactionId")));
		}else if(Params.get("merchantOrderId")!=null)
			search.add(Restrictions.eq("t.merchant_order_id", Params.get("merchantOrderId")));
		else
		{
			if(Params.get("onDate")!=null)
			{
				Date startDate=(Date)Params.get("onDate");
				Calendar c = Calendar.getInstance();
				c.setTime(startDate);
				c.add(Calendar.DATE, 1);
				Date endDate=c.getTime();
				search.add(Restrictions.ge("activity_time", startDate));
				search.add(Restrictions.le("activity_time", endDate));
				log.debug(startDate+" - "+endDate);
			}
			if(Params.get("fromDate")!=null)
				search.add(Restrictions.ge("activity_time", (Date)Params.get("fromDate")));
			if(Params.get("toDate")!=null)
				search.add(Restrictions.le("activity_time", (Date)Params.get("toDate")));
			if(Params.get("status")!=null)
				search.add(Restrictions.eq("status", Params.get("status")));
			if(Params.get("type")!=null)
			{
				search.add(Restrictions.eq("type", Params.get("type")));
				if(Params.get("type").equals(DisputeStates.MERCHANT_REFUND))
				{
					if(Params.get("startAmount")!=null)
						search.add(Restrictions.ge("t.refund_amount", Params.get("startAmount")));
					if(Params.get("endAmount")!=null)
						search.add(Restrictions.le("t.refund_amount", Params.get("endAmount")));
				}
				if(Params.get("type").equals(DisputeStates.PRODUCT_DISPUTE))
				{
					amountType =false;
					if(Params.get("startAmount")!=null)
						search.add(Restrictions.ge("disputed_amount", Params.get("startAmount")));
					if(Params.get("endAmount")!=null)
						search.add(Restrictions.le("disputed_amount", Params.get("endAmount")));
				}
			}
			
			if(Params.get("sort")!=null)
			{
				String sort=(String)Params.get("sort");
				Boolean order=true;
				if(Params.get("order")!=null)
				{
					if(Params.get("order").equals("desc")||Params.get("order").equals("DESC"))
						order=false;
				}
				if(sort.equals("date"))
				{
					if(order)
					{
						search.addOrder(Order.asc("activity_time"));
					}else
					{
						search.addOrder(Order.desc("activity_time"));	
					}
				}else if(sort.equals("amount")){
					if(order&&amountType)
					{
						search.addOrder(Order.asc("t.refund_amount"));
					}else if(amountType)
					{
						search.addOrder(Order.desc("t.refund_amount"));	
					}else
					{
						if(order)
						{
							search.addOrder(Order.asc("t.disputed_amount"));
						}else if(amountType)
						{
							search.addOrder(Order.desc("disputed_amount"));	
						}
					}
					
					
				}else  if(sort.equals("status")){
					if(order)
					{
						search.addOrder(Order.asc("status"));
					}else
					{
						search.addOrder(Order.desc("status"));	
					}
				}else  if(sort.equals("consumer")){
					if(order)
					{
						search.addOrder(Order.asc("t.consumer"));
					}else
					{
						search.addOrder(Order.desc("t.consumer"));	
					}
				}
			}
		}
		log.debug(Params);
		//search.add(Restrictions.eq("status","SUCCESS"));
		@SuppressWarnings("unchecked")
		ArrayList<Dispute> results  = (ArrayList<Dispute>) search.list();
		
		//Query to count number of rows
		if(start==0&&Params.get("transactionId")==null&&Params.get("merchantOrderId")==null){
			search.setProjection(Projections.rowCount());
	    	search.setResultTransformer(Criteria.PROJECTION);
	    	try
	    	{
	    		resultSetSize.setRowCount((Long)search.uniqueResult());
	    	}catch(NullPointerException ex)
	    	{
	    		//Do nothing
	    	}
		}
		
		log.debug("results size : "+results.size());
		return results;

	}
	
	//DAO implementation of settlements filter. The map contains all the parameters passed to settlements
	@Override
	public ArrayList<Settlement> getSettlements(Merchant merchant,Map Params,int start,int size,ResultSetSize resultSetSize) {
		
		Criteria search = this.sessionFactory.getCurrentSession().createCriteria(Settlement.class);
		search.setFirstResult(start);
		if(size>20)
			size=20;
		search.setMaxResults(size);
		search.createAlias("merchantaccount", "m");
		if(merchant!=null)
		search.add(Restrictions.eq("m.merchant", merchant));
			
		if(Params.get("settlementId")!=null)
			search.add(Restrictions.eq("settlement_id", Params.get("settlementId")));
		if(Params.get("onDate")!=null)
		{
			Date startDate=(Date)Params.get("onDate");
			Calendar c = Calendar.getInstance();
			c.setTime(startDate);
			c.add(Calendar.DATE, 1);
			Date endDate=c.getTime();
			search.add(Restrictions.ge("activity_time", startDate));
			search.add(Restrictions.le("activity_time", endDate));
			log.debug(startDate+" - "+endDate);
		}
		if(Params.get("fromDate")!=null)
			search.add(Restrictions.ge("activity_time", (Date)Params.get("fromDate")));
		if(Params.get("toDate")!=null)
			search.add(Restrictions.le("activity_time", (Date)Params.get("toDate")));
		if(Params.get("status")!=null)
			search.add(Restrictions.eq("status", Params.get("status")));
		if(Params.get("startAmount")!=null)
			search.add(Restrictions.ge("amount", Params.get("startAmount")));
		if(Params.get("endAmount")!=null)
			search.add(Restrictions.le("amount", Params.get("endAmount")));

		if(Params.get("sort")!=null)
		{
			String sort=(String)Params.get("sort");
			Boolean order=true;
			if(Params.get("order")!=null)
			{
				if(Params.get("order").equals("desc")||Params.get("order").equals("DESC"))
					order=false;
			}
			if(sort.equals("date"))
			{
				if(order)
				{
					search.addOrder(Order.asc("activity_time"));
				}else
				{
					search.addOrder(Order.desc("activity_time"));	
				}
			}else if(sort.equals("amount")){
				if(order)
				{
					search.addOrder(Order.asc("amount"));
				}else
				{
					search.addOrder(Order.desc("amount"));	
				}
			}else  if(sort.equals("status")){
				if(order)
				{
					search.addOrder(Order.asc("status"));
				}else
				{
					search.addOrder(Order.desc("status"));	
				}
			}
		}
			
		log.debug(Params);
		//search.add(Restrictions.eq("status","SUCCESS"));
		@SuppressWarnings("unchecked")
		ArrayList<Settlement> results  = (ArrayList<Settlement>) search.list();
		//Query to count number of rows
		if(start==0){
			search.setProjection(Projections.rowCount());
	    	search.setResultTransformer(Criteria.PROJECTION);
	    	try
	    	{
	    		resultSetSize.setRowCount((Long)search.uniqueResult());
	    	}catch(NullPointerException ex)
	    	{
	    		//Do nothing
	    	}
		}
		
		log.debug("results size : "+results.size());
		return results;

	}
	
	/*
	 * (non-Javadoc)
	 * @see com.i2india.DAO.OrderDAO#getDeliveredTransactions()
	 * This for Batch Processing
	 */
	
	@Override
	public ArrayList<Transactions> getDeliveredTransactions()
	{
		String hql = "FROM Transactions T WHERE T.status = :delivered";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("delivered", "DELIVERED");
		@SuppressWarnings("unchecked")
		ArrayList<Transactions> results  = (ArrayList<Transactions>) query.list();
		log.debug(results.size());
		if(results.size()==0)
		return null;
		return results;
	}
	
	/*
	 * This is for security, Authorization framework
	 * Since its dealing with Order objects the code exsist here
	 * 	*/
	@Override
	public Merchant getMerchantForAuthorization(Object object,int id) {
		
		Merchant merchant=null;
		try
		{
			if(object.getClass().equals(Transactions.class))
			{
				Transactions transaction =this.getTransaction(id);
				Hibernate.initialize(transaction.getMerchant());
				merchant = transaction.getMerchant();
			}
			if(object.getClass().equals(Dispute.class))
			{
				Dispute dispute =this.getDispute(id);
				Hibernate.initialize(dispute.getTransaction());
				Hibernate.initialize(dispute.getTransaction().getMerchant());
				merchant =dispute.getTransaction().getMerchant();
			}
			if(object.getClass().equals(Settlement.class))
			{
				Settlement settlement =this.getSettlement(id);
				Hibernate.initialize(settlement.getMerchantaccount());
				Hibernate.initialize(settlement.getMerchantaccount().getMerchant());
				merchant=settlement.getMerchantaccount().getMerchant();
			}
			return merchant;
			
		}catch(NullPointerException ex)
		{
			return null;
		}
	}
	/*
	 * This is for security, Authorization framework
	 * Since its dealing with Order objects the code exsist here
	 * 	*/
	@Override
	public Consumer getConsumerForAuthorization(Object object,int id) {
		
		Consumer consumer=null;
		try
		{
			if(object.getClass().equals(Transactions.class))
			{
				Transactions transaction =this.getTransaction(id);
				Hibernate.initialize(transaction.getConsumer());
				consumer = transaction.getConsumer();
			}
			if(object.getClass().equals(Dispute.class))
			{
				Dispute dispute =this.getDispute(id);
				Hibernate.initialize(dispute.getTransaction());
				Hibernate.initialize(dispute.getTransaction().getConsumer());
				consumer =dispute.getTransaction().getConsumer();
			}
			return consumer;
			
		}catch(NullPointerException ex)
		{
			return null;
		}
	}
	
	
}
