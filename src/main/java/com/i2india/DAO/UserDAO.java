package com.i2india.DAO;

import java.util.ArrayList;
import java.util.List;

import com.i2india.Domain.Bank;
import com.i2india.Domain.BankContract;
import com.i2india.Domain.Consumer;
import com.i2india.Domain.ConsumerRefund;
import com.i2india.Domain.Dispute;
import com.i2india.Domain.DisputeComment;
import com.i2india.Domain.Merchant;
import com.i2india.Domain.MerchantAccount;
import com.i2india.Domain.MerchantContract;
import com.i2india.Domain.MerchantRefundPolicy;
import com.i2india.Domain.Settlement;
import com.i2india.Domain.Transactions;
import com.i2india.Domain.User;
import com.i2india.Domain.UserRight;
import com.i2india.Domain.UserRole;

public interface UserDAO 
{

	public void addConsumer(Consumer consumser);
	public void addBank(Bank bank);
	public void addMerchant(Merchant merchant);
	public void addUser(User user);
	public void addUserRole(UserRole userRole);
	public void addUserRight(UserRight userRight);
	public void addMerchantAccount(MerchantAccount account);
	public void addMerchantContract(MerchantContract merchantContract);


	public ArrayList<Bank> getAllBanks();
	public ArrayList<Consumer> getAllConsumers();
	public ArrayList<Merchant> getAllMerchants();
	public ArrayList<User> getAllUsers();
	public ArrayList<UserRole> getAllUserRoles();
	public ArrayList<UserRight> getAllUserRights();
	public  List<UserRole> getRolesByUsernames(String username);
	public  List<UserRole> getRolesByUserID(int id);

	public Bank getBank(Integer id);
	public Consumer getConsumer(Integer id);
	public Merchant getMerchant(Integer id);
	public User getUser(Integer id);
	public UserRole getUserRole(Integer id);
	public UserRight getUserRight(Integer id);
	public Consumer getConsumerByUsername(String username);
	public User getUserbyUsername(String username);


	public void updateBank(Bank bank);
	public void updateConsumer(Consumer cons1);
	public void updateMerchant(Merchant merchant1);
	public void updateUser(User user1);
	public void updateUserRole(UserRole userRole);
	public void updateUserRight(UserRight userRight);


	public BankContract getBankContract(Integer id);
	public ConsumerRefund getConsumerRefund(Integer id);
	public MerchantRefundPolicy getMerchantRefundPolicy(Integer id);




	public ArrayList<String> getUserRolesByName(String username);
	public MerchantContract getMerchantContract(Integer id);
	public MerchantAccount getMerchantAccount(Integer id);
	public void deleteUser(Integer id);
	//DAO Level specific queries
	public String getMerchantKey(Integer merchantID);
	public ArrayList<MerchantAccount> getAllMerchantAccounts(Merchant merchant);
	public void updateMerchantAccount(MerchantAccount merchantaccount);
	public void updateResetIsPrimaryMerchantAccounts(Merchant merchant);
	public Merchant getMerchantByOrganizationName(String organizationName);

}