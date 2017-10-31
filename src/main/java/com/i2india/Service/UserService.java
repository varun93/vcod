package com.i2india.Service;

import java.util.ArrayList;

import com.i2india.Domain.Bank;
import com.i2india.Domain.Consumer;
import com.i2india.Domain.Merchant;
import com.i2india.Domain.MerchantAccount;
import com.i2india.Domain.MerchantContract;
import com.i2india.Domain.User;
import com.i2india.Domain.UserRole;

public interface UserService {

	
	public Consumer loadConsumerbyname(String username);
	
	public User loadUserByUsername(String username);
	
	public ArrayList<UserRole> getRolesByusername(String username);
	
	public ArrayList<UserRole> getRolesByuserid(int userID);
	
	public Merchant loadMerchantById(int merchantID);
	
	public User getUserByToken(String token);
	
	public Merchant loadMerchantByKey(String key);
	
	public Consumer getConsumerByName(String username,String firstName,String phoneNumber);
	
	public int addUser(User user);
	
	public Bank getBankbyId(int id);
	
	public Bank getBank(Integer id);
	
	public Merchant getMerchant(Integer id);
	
	public ArrayList<User> getAllUsers();

	public User loadUserByUserID(int id);
	
	public String getMerchantKey(Integer merchantID);

	void addMerchantAccount(MerchantAccount merchantaccount, Merchant merchant);

	int addMerchant(Merchant merchant);

	void updateMerchantWizard(Merchant merchant);

	void activateUser(String data);

	public String generateActivateTokenUser(int userId);

	public Merchant getMerchantWizardInfo(int userId);

	public void updateUser(User user);

	void addMerchantContract(MerchantContract merchantContract);

	void changePassword(User user, String oldPasswod,
			String newPassword);

	ArrayList<MerchantAccount> getAllMerchantAccounts(Merchant merchant);

	void updateMerchantAccount(MerchantAccount merchantAccount);
	
	public Merchant getMerchantFromMerchantAccount(int merchantAccountId);

	Consumer getConsumer(int consumerId);
	
	
}