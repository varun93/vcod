package com.i2india.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.i2india.DAO.UserDAO;
import com.i2india.Domain.Bank;
import com.i2india.Domain.Consumer;
import com.i2india.Domain.Merchant;
import com.i2india.Domain.MerchantAccount;
import com.i2india.Domain.MerchantContract;
import com.i2india.Domain.User;
import com.i2india.Domain.UserRole;
import com.i2india.ErrorUtils.CustomApplicationException;
import com.i2india.ErrorUtils.CustomDataException;
import com.i2india.ErrorUtils.CustomValidationException;
import com.i2india.ErrorUtils.ErrorConstants;
import com.i2india.SecurityUtils.AuthToken;
import com.i2india.SecurityUtils.KeyEncryption;
import com.i2india.Service.UserService;
import com.i2india.ServiceUtils.AccountStatus;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserDAO userDao;

	//The salt added to activation code generation should be large and random
	private static String activationSalt = "zxc12345";
	private static String activationEncDecKey = "abcdef1234567890";
	static Logger log = Logger.getLogger(UserServiceImpl.class.getName());


	public void setUserDao(UserDAO userDao)
	{
		this.userDao = userDao;
	}

	@Override
	@Transactional("transactionManager")
	public void updateUser(User user) {
		userDao.updateUser(user);
	}

	//Loading user by Username
	@Transactional("transactionManager")
	public User loadUserByUsername( String username) {
		return userDao.getUserbyUsername(username);
	}

	//Get user roles by username
	@Transactional("transactionManager")
	public ArrayList<UserRole> getRolesByusername(String username) {

		return (ArrayList<UserRole>) userDao.getRolesByUsernames(username);
	}


	//Get roles of user by user_id
	@Transactional("transactionManager")
	public ArrayList<UserRole> getRolesByuserid(int userID) {

		return (ArrayList<UserRole>) userDao.getRolesByUserID(userID);
	}
	
	//Load merchant by by id
	@Transactional("transactionManager")
	public Merchant loadMerchantById(int merchantID) {

		return userDao.getMerchant(merchantID);
	}

	//To get merchant account from merchant
	@Transactional("transactionManager")
	public Merchant getMerchantFromMerchantAccount(int merchantAccountId) {

		MerchantAccount merchantAccount= userDao.getMerchantAccount(merchantAccountId);
		Hibernate.initialize(merchantAccount.getMerchant());
		return merchantAccount.getMerchant();
	}


	//security
	@Transactional("transactionManager")
	//	@ReadThroughSingleCache(namespace = "userName", expiration = 3600) @ParameterValueKeyProvider
	public User getUserByToken( String token) {

		User user = null;
		AuthToken auth = new AuthToken(this);
     	user = auth.validateToken(token);
        
		return user;
	}


	@Transactional("transactionManager")
	public Merchant loadMerchantByKey(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional("transactionManager")
	public Bank getBank(Integer id) {
		return userDao.getBank(id);
	}

	@Transactional("transactionManager")
	public Merchant getMerchant(Integer id) {
		return userDao.getMerchant(id);
	}

    //Load consumer by username
	@Transactional("transactionManager")
	@Cacheable(value = "defaultCache#300")
	public Consumer loadConsumerbyname(String username) {

		log.debug("here I am testing for caching!!!");
		Consumer consumer= userDao.getConsumerByUsername(username);

		return consumer;
	}

	/*
	 * (non-Javadoc)
	 * @see com.i2india.Service.UserService#getConsumerByName(java.lang.String, java.lang.String, java.lang.String)
	 * A consumer is created if consumer doesn't exsist
	 * An email should be sent on successful creation with activation code
	 */
	@Transactional("transactionManager")
	public Consumer getConsumerByName(String username,String firstName,String phoneNumber) {

		Consumer consumer= userDao.getConsumerByUsername(username);

		if(consumer != null)
			return consumer;

		else
		{
			consumer = new Consumer();
			consumer.setAddress(null);
			consumer.setUsername(username);
			long phno = Long.parseLong(phoneNumber);
			log.debug(phoneNumber+" size "+phoneNumber.length());
			consumer.setMobile(phno); 
			consumer.setStatus(AccountStatus.PENDING);
			consumer.setLast_login(null);
			consumer.setName(firstName);
			consumer.setPassword("asidnsaindzNx"); // A Random password generater is required
			consumer.setRegistered_date(new Date());
			consumer.setRegistration(AccountStatus.PARTIAL);
			userDao.addConsumer(consumer);
			return consumer;
		}


	}


	@Transactional("transactionManager")
	public int addUser(User user) {
		return 0;
	}

	//Get bank by ID
	@Transactional("transactionManager")
	public Bank getBankbyId(int id) {
		return (Bank) userDao.getBank(id);
	}


	@Transactional("transactionManager")
	public ArrayList<User> getAllUsers() {

		return userDao.getAllUsers();
	}



   @Transactional("transactionManager")
	public User loadUserByUserID(int id) {

		log.debug("here I am testing for caching!!!");
		return userDao.getUser(id);

	}



	@Override
	@Transactional("transactionManager")
	public String getMerchantKey(Integer merchantID) {

		return userDao.getMerchantKey(merchantID);

	}

	/*
	 * (non-Javadoc)
	 * @see com.i2india.Service.UserService#addMerchant(com.i2india.Domain.Merchant)
	 * Adding a new Merchant
	 */
	@Override
	@Transactional("transactionManager")
	public int addMerchant(Merchant merchant)
	{

		//The form may contain unwanted values . To avoid that we can copy only username and password
	
		Merchant finalMerchant = new Merchant();
		finalMerchant.setUsername(merchant.getUsername());
		finalMerchant.setPassword(KeyEncryption.hashPassword(merchant.getPassword()));
		finalMerchant.setMobile(merchant.getMobile());
		finalMerchant.setName(merchant.getName());
		finalMerchant.setStatus(AccountStatus.PENDING);
		finalMerchant.setRegistration(AccountStatus.PARTIAL);
		finalMerchant.setRegistered_date(new Date());	
		this.userDao.addMerchant(finalMerchant);
		return 0;
	}
	/*
	 * (non-Javadoc)
	 * @see com.i2india.Service.UserService#getAllMerchantAccounts(com.i2india.Domain.Merchant)
	 * List of merchant accounts
	 */
	@Override
	@Transactional("transactionManager")
	public ArrayList<MerchantAccount> getAllMerchantAccounts(Merchant merchant)
	{
		return userDao.getAllMerchantAccounts(merchant);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.i2india.Service.UserService#addMerchantAccount(com.i2india.Domain.MerchantAccount, com.i2india.Domain.Merchant)
	 * Adding a new merchant account
	 */
	@Override
	@Transactional("transactionManager")
	public void addMerchantAccount(MerchantAccount merchantAccount,Merchant merchant)
	{
		merchantAccount.setMerchant(merchant);
		merchantAccount.setIsPrimary(1);
		ArrayList<MerchantAccount> merchantAccounts=userDao.getAllMerchantAccounts(merchant);
		if(merchantAccounts==null)
		{
			merchantAccount.setIsPrimary(1);
		}else 
			throw new CustomValidationException(ErrorConstants.MERCHANT_ONE_BANK_ACCOUNT,ErrorConstants.STR_MERCHANT_ONE_BANK_ACCOUNT);
		userDao.addMerchantAccount(merchantAccount);
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.i2india.Service.UserService#updateMerchantAccount(com.i2india.Domain.MerchantAccount)
	 * Updating merchant account application
	 */
	@Override
	@Transactional("transactionManager")
	public void updateMerchantAccount(MerchantAccount merchantAccount)
	{
		userDao.updateMerchantAccount(merchantAccount);	
	}

	/*
	 * 	@Override(non-Javadoc)
	 * @see com.i2india.Service.UserService#updateMerchantWizard(com.i2india.Domain.Merchant)
	 * Updating merchant account information
	 */
	@Transactional("transactionManager")
	public void updateMerchantWizard(Merchant merchant) {
		
		Merchant temp= userDao.getMerchant(merchant.getUser_id());

		temp.setOrganization_name(merchant.getOrganization_name());
		temp.setBusiness_category(merchant.getBusiness_category());
		temp.setBusiness_type(merchant.getBusiness_type());
		
		temp.setLandline(merchant.getLandline());
		temp.setOfficial_email(merchant.getOfficial_email());
		temp.setOfficial_landline1(merchant.getOfficial_landline1());
		temp.setOfficial_landline2(merchant.getOfficial_landline2());
		temp.setOfficial_landline3(merchant.getOfficial_landline3());
		temp.setOfficial_mobile(merchant.getOfficial_mobile());
		
		temp.setOperating_address(merchant.getOperating_address());
		temp.setOperating_city(merchant.getOperating_city());
		temp.setOperating_state(merchant.getOperating_state());
		temp.setOperating_country(merchant.getOperating_country());
		temp.setOperating_pincode(merchant.getOperating_pincode());
		
		temp.setPancard_est_date(merchant.getPancard_est_date());
		temp.setPancard_name(merchant.getPancard_name());
		temp.setPancard_no(merchant.getPancard_no());
		
		temp.setRegistered_address(merchant.getRegistered_address());
		temp.setRegistered_city(merchant.getRegistered_city());
		temp.setRegistered_country(merchant.getRegistered_country());
		temp.setRegistered_pincode(merchant.getRegistered_pincode());
		temp.setRegistered_state(merchant.getRegistered_state());
		
		/*temp.setRegistration(AccountStatus.COMPLETE);*/
		userDao.updateMerchant(temp);
		
	}

	
	@Override
	@Transactional("transactionManager")
	public void addMerchantContract(MerchantContract merchantContract)
	{
		//Basic Merchant Contracts
		userDao.addMerchantContract(merchantContract);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.i2india.Service.UserService#changePassword(com.i2india.Domain.User, java.lang.String, java.lang.String)
	 * Changing merchant password
	 */
	@Override
	@Transactional("transactionManager")
	public void changePassword(User user,String oldPassword,String newPassword)
	{
		if(user instanceof Merchant)
		{
			Merchant merchant = (Merchant)user;
			if(merchant.getPassword().equals(KeyEncryption.hashPassword(oldPassword)))
			{
				merchant.setPassword(KeyEncryption.hashPassword(newPassword));
				userDao.updateMerchant(merchant);
			}else
			{
				throw new CustomValidationException(ErrorConstants.USER_INVALID_OLD_PASSWORD,ErrorConstants.STR_USER_INVALID_OLD_PASSWORD);
			}
		}else if(user instanceof Consumer)
		{
			Consumer consumer =(Consumer)user;
			if(consumer.getPassword().equals(KeyEncryption.hashPassword(oldPassword)))
			{
				consumer.setPassword(KeyEncryption.hashPassword(newPassword));
				userDao.updateConsumer(consumer);
			}else
			{
				throw new CustomValidationException(ErrorConstants.USER_INVALID_OLD_PASSWORD,ErrorConstants.STR_USER_INVALID_OLD_PASSWORD);
			}
		}else if(user instanceof Bank)
		{
			Bank bank = (Bank)user;
			if(bank.getPassword().equals(KeyEncryption.hashPassword(oldPassword)))
			{
				bank.setPassword(KeyEncryption.hashPassword(newPassword));
				//Update bank object here
			}else
			{
				throw new CustomValidationException(ErrorConstants.USER_INVALID_OLD_PASSWORD,ErrorConstants.STR_USER_INVALID_OLD_PASSWORD);
			}
		}else
			throw new CustomApplicationException(ErrorConstants.INTERNAL_ERROR,ErrorConstants.STR_INTERNAL_ERROR);
	}

	/*
	 * (non-Javadoc)
	 * @see com.i2india.Service.UserService#activateUser(java.lang.String)
	 * Activating user, User a key of multiple of 8 very long and random for encrypting and decrypt
	 * Roles are set here for user
	 */
	@Override
	@Transactional("transactionManager")
	public void activateUser(String data) {
		try
		{
			String decrypt=com.i2india.SecurityUtils.KeyEncryption.getDecryptedToken(data, this.activationEncDecKey);
			decrypt = decrypt.substring(0, decrypt.indexOf(activationSalt));
			int userId = Integer.parseInt(decrypt);
			User user = userDao.getUser(userId);
			if(user.getStatus().equals(AccountStatus.ACTIVE))
				throw new CustomApplicationException(ErrorConstants.USER_ACTIVATE_ERROR,ErrorConstants.STR_USER_ACTIVATE_ERROR);
			user.setStatus(AccountStatus.ACTIVE);
			
			//Setting permissions
			if(user instanceof Merchant)
			{
				Set<UserRole> userRoles =  new HashSet<UserRole>();
				UserRole userRole = userDao.getUserRole(1);
				userRoles.add(userRole);
				user.setRoles(userRoles);
				
			}
			log.debug(user.getUsername());
			userDao.updateUser(user);
		}catch(Exception ex)
		{
			ex.printStackTrace();
			throw new CustomApplicationException(ErrorConstants.USER_ACTIVATE_ERROR,ErrorConstants.STR_USER_ACTIVATE_ERROR);
		}
		
	}


	/*
	 * (non-Javadoc)
	 * @see com.i2india.Service.UserService#generateActivateTokenUser(int)
	 * Generation of activation token. This code should be in email generation service. 
	 * The account's which are not in Active state are eligible for this operation
	 */
	@Override
	@Transactional("transactionManager")
	public String generateActivateTokenUser(int userId) {
		try
		{
			User user = userDao.getUser(userId);
			if(user.getStatus().equals(AccountStatus.ACTIVE))
				throw new CustomDataException(ErrorConstants.USER_INVALID_ID,ErrorConstants.STR_USER_INVALID_ID);
			String token=com.i2india.SecurityUtils.KeyEncryption.getEncryptedToken(Integer.toString(userId)+activationSalt,this.activationEncDecKey);
			log.debug(token);
			return token;
		}catch(Exception ex)
		{
			throw new CustomDataException(ErrorConstants.USER_INVALID_ID,ErrorConstants.STR_USER_INVALID_ID);
		}
	}


	/*
	 * (non-Javadoc)
	 * @see com.i2india.Service.UserService#getMerchantWizardInfo(int)
	 * Merchant application info.
	 * The information hiding is done at domain class level using annotations
	 */
	@Override
	@Transactional("transactionManager")
	public Merchant getMerchantWizardInfo(int userId) {
		Merchant merchant = userDao.getMerchant(userId);
		return merchant;
	}

	@Override
	@Transactional
	public Consumer getConsumer(int consumerId)
	{
		return userDao.getConsumer(consumerId);
	}

}