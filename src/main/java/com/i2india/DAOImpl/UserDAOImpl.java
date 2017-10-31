package com.i2india.DAOImpl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.i2india.DAO.UserDAO;
import com.i2india.Domain.Bank;
import com.i2india.Domain.BankContract;
import com.i2india.Domain.Consumer;
import com.i2india.Domain.ConsumerRefund;
import com.i2india.Domain.Merchant;
import com.i2india.Domain.MerchantAccount;
import com.i2india.Domain.MerchantContract;
import com.i2india.Domain.MerchantRefundPolicy;
import com.i2india.Domain.User;
import com.i2india.Domain.UserRight;
import com.i2india.Domain.UserRole;

@Repository
public class UserDAOImpl implements UserDAO{

	@Autowired
	private SessionFactory sessionFactory;
	static Logger log = Logger.getLogger(UserDAOImpl.class.getName());
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	//Add methods
	public void addMerchantContract(MerchantContract merchantContract)
	{
		this.sessionFactory.getCurrentSession().save(merchantContract);
	}
	

	public void addConsumer(Consumer consumser)
	{
		this.sessionFactory.getCurrentSession().save(consumser);
	}


	public void addMerchant(Merchant merchant)
	{
		this.sessionFactory.getCurrentSession().save(merchant);
	}

	public void addBank(Bank bank) {
		this.sessionFactory.getCurrentSession().save(bank);
	}
	public void addUser(User user) {
		this.sessionFactory.getCurrentSession().save(user);
	}
	public void addUserRole(UserRole userRole) {
		this.sessionFactory.getCurrentSession().save(userRole);
	}
	public void addUserRight(UserRight userRight) {
		this.sessionFactory.getCurrentSession().save(userRight);
	}



	//Get List of entites

	@SuppressWarnings("unchecked")
	public ArrayList<Bank> getAllBanks() {
		return (ArrayList<Bank>) this.sessionFactory.getCurrentSession().createQuery("from Bank").list();
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Consumer> getAllConsumers() {
		return (ArrayList<Consumer>) this.sessionFactory.getCurrentSession().createQuery("from Consumer").list();
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Merchant> getAllMerchants() {
		return (ArrayList<Merchant>) this.sessionFactory.getCurrentSession().createQuery("from Merchant").list();
	}
	@SuppressWarnings("unchecked")
	public ArrayList<User> getAllUsers() {
		return (ArrayList<User>) this.sessionFactory.getCurrentSession().createQuery("from User").list();
	}
	@SuppressWarnings("unchecked")
	public ArrayList<UserRole> getAllUserRoles() {
		return (ArrayList<UserRole>) this.sessionFactory.getCurrentSession().createQuery("from UserRole").list();
	}
	@SuppressWarnings("unchecked")
	public ArrayList<UserRight> getAllUserRights() {
		return (ArrayList<UserRight>) this.sessionFactory.getCurrentSession().createQuery("from UserRight").list();
	}

	//Get methods

	public Bank getBank(Integer id) {
		return (Bank) this.sessionFactory.getCurrentSession().get(Bank.class,id);
	}
	public Consumer getConsumer(Integer id) {
		return (Consumer) this.sessionFactory.getCurrentSession().get(Consumer.class,id);
	}

	public Merchant getMerchant(Integer id) {
		return (Merchant) this.sessionFactory.getCurrentSession().get(Merchant.class,id);
	}
	public User getUser(Integer id) {
		return (User) this.sessionFactory.getCurrentSession().get(User.class,id);
	}
	public UserRole getUserRole(Integer id) {
		return (UserRole) this.sessionFactory.getCurrentSession().get(UserRole.class,id);
	}	
	public UserRight getUserRight(Integer id) {
		return (UserRight) this.sessionFactory.getCurrentSession().get(UserRight.class,id);
	}


	//Update methods

	public void updateBank(Bank bank) {
		this.sessionFactory.getCurrentSession().update(bank);
	}
	public void updateConsumer(Consumer cons1)
	{
		this.sessionFactory.getCurrentSession().update(cons1);
	}
	public void updateMerchant(Merchant merchant1) {
		this.sessionFactory.getCurrentSession().update(merchant1);
	}
	public void updateUser(User user1) {
		this.sessionFactory.getCurrentSession().update(user1);
	}

	public void updateUserRole(UserRole userRole) {
		this.sessionFactory.getCurrentSession().update(userRole);
	}

	public void updateUserRight(UserRight userRight) {
		this.sessionFactory.getCurrentSession().update(userRight);
	}


	//This is for loading merchant by ID
	public Merchant loadMerchantById(String merchantID)
	{

		Merchant merchant = new Merchant();
		String hql = "FROM Merchant M WHERE M.merchant_id = : merchantID";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("merchantID",merchantID);
		@SuppressWarnings("unchecked")
		ArrayList<User> results  = (ArrayList<User>) query.list();
		if(results.size()==0)
			return null;
		else if(results.size()==1)
		{
			if(results.get(0).getClass().equals(Consumer.class))
			{
				return (Merchant) results.get(0);
			}else
			{
				return null;
			}			
		}
		else
			return null;
	}

	//This is for getting consumer by username
	@Override
	public Consumer getConsumerByUsername(String username) {
		String hql = "FROM User U WHERE U.username = :username";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("username", username);
		@SuppressWarnings("unchecked")
		ArrayList<User> results  = (ArrayList<User>) query.list();
		if(results.size()==0)
			return null;
		else if(results.size()==1)
		{
			if(results.get(0).getClass().equals(Consumer.class))
			{
				return (Consumer) results.get(0);
			}else
			{
				return null;
			}			
		}
		else
			return null;

	}
	
	@Override
	public Merchant getMerchantByOrganizationName(String organizationName) {
		String hql = "FROM Merchant M WHERE M.organization_name = :organizationName";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("organizationName", organizationName);
		@SuppressWarnings("unchecked")
		ArrayList<Merchant> results  = (ArrayList<Merchant>) query.list();
		log.debug("Results size "+results.size());
		if(results.size()==0)
			return null;
		else 
		{
			if(results.get(0).getClass().equals(Merchant.class))
			{
				return (Merchant) results.get(0);
			}else
			{
				return null;
			}			
		}


	}

	//Security
	@Override
	public  List<UserRole> getRolesByUserID(int id) {


		String hql = "FROM User U where U.user_id = :id";

		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);

		query.setParameter("id", id);



		@SuppressWarnings("unchecked")
		ArrayList<User> results  = (ArrayList<User>) query.list();
		log.debug("results size : "+results.size());

		if(results.size()==0)

			return null;


		else
		{
			log.debug(results.get(0).getRoles().size());
			return (List<UserRole>) results.get(0).getRoles();

		}
	}



	//Security 
	@Override
	public  List<UserRole> getRolesByUsernames(String username) {


		String hql = "FROM User U where U.username = :username";

		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);

		query.setParameter("username", username);



		@SuppressWarnings("unchecked")

		ArrayList<User> results  = (ArrayList<User>) query.list();
		log.debug("results size : "+results.size());

		if(results.size()==0)

			return null;


		else
		{
			log.debug(results.get(0).getRoles().size());
			return (List<UserRole>) results.get(0).getRoles();

		}
	}

	@Override
	public void addMerchantAccount(MerchantAccount account) {
		this.sessionFactory.getCurrentSession().save(account);

	}


	@Override
	public BankContract getBankContract(Integer id) {
		return (BankContract) this.sessionFactory.getCurrentSession().get(BankContract.class,id);
	}

	@Override
	public ConsumerRefund getConsumerRefund(Integer id) {
		return (ConsumerRefund) this.sessionFactory.getCurrentSession().get(ConsumerRefund.class,id);
	}

	@Override
	public MerchantRefundPolicy getMerchantRefundPolicy(Integer id) {
		return (MerchantRefundPolicy) this.sessionFactory.getCurrentSession().get(MerchantRefundPolicy.class,id);
	}



	@Override
	public void deleteUser(Integer id) {
		// TODO Auto-generated method stub

	}




	@Override
	public ArrayList<String> getUserRolesByName(String username) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public MerchantContract getMerchantContract(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MerchantAccount getMerchantAccount(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	//Security
	@Override
	public User getUserbyUsername(String username) {

		String hql = "FROM User U WHERE U.username = :username";

		log.debug(this.sessionFactory);
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);


		query.setParameter("username", username);
		@SuppressWarnings("unchecked")
		ArrayList<User> results  = (ArrayList<User>) query.list();
		log.debug("Result size is" + results.size());
		if(results.size()==0)
			return null;
		else if(results.size()==1)
		{
				return results.get(0);	
		}
		else
			return null;

	}
	//For fetching merchant key
	@Override
	public String getMerchantKey(Integer merchantID) {

		String hql = "FROM Merchant M where M.user_id = :merchantID";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("merchantID", merchantID);
		@SuppressWarnings("unchecked")
		ArrayList<Merchant> results  = (ArrayList<Merchant>) query.list();
		log.debug("Result size is" + results.size());
		if(results.size()==0)
			return null;
		else if(results.size()==1)
		{
			if(results.get(0).getClass().equals(Merchant.class))
			{
				return results.get(0).getSecret_key();
			}else
			{
				return null;
			}			
		}
		else
			return null;
		
       
	}
	//To get all merchant accounts
	@Override
	@SuppressWarnings("unchecked")
	public ArrayList<MerchantAccount> getAllMerchantAccounts(Merchant merchant) {
		String hql = "FROM MerchantAccount MA where MA.merchant = :merchant";
		  Query query= sessionFactory.getCurrentSession().createQuery(hql);
			query.setParameter("merchant", merchant);
			@SuppressWarnings("unchecked")
			ArrayList<MerchantAccount> results  = (ArrayList<MerchantAccount>) query.list();
			log.debug("Result size is" + results.size());
			if(results.size()==0)
				return null;
			else
				return results;
	}

	@Override
	public void updateMerchantAccount(MerchantAccount merchantaccount)
	{
		this.sessionFactory.getCurrentSession().update(merchantaccount);
	}
	

	//This method was used to allow multiple bank accounts. Now only one account is allowed
	@Override
	public void updateResetIsPrimaryMerchantAccounts(Merchant merchant)
	{
		String hql = "from MerchantAccount where merchant =:merchant and isPrimary=1";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("merchant", merchant);
		ArrayList<MerchantAccount> results  = (ArrayList<MerchantAccount>) query.list();
		log.debug("Result size is" + results.size());
		if(results.size()==1)
		{
			results.get(0).setIsPrimary(0);
			this.sessionFactory.getCurrentSession().update(results.get(0));
		}
	
	}

}