package CRUD;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


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


public class CrudDAOImpl{

    private SessionFactory sessionFactory;
	
	
	public void addBank(Bank bank) {
		this.sessionFactory.getCurrentSession().save(bank);
	}
	
	public void addBankContract(BankContract bankcontract) {
		this.sessionFactory.getCurrentSession().save(bankcontract);
	}

	public void addConsumer(Consumer cons1)
	{
		this.sessionFactory.getCurrentSession().save(cons1);
	}
	

	public void addConsumerRefund(ConsumerRefund consumerrefund)
	{
		this.sessionFactory.getCurrentSession().save(consumerrefund);
	}
	

	public void addDispute(Dispute dispute)
	{
		this.sessionFactory.getCurrentSession().save(dispute);
	}
	

	public void addDispute(DisputeComment disputecomment)
	{
		this.sessionFactory.getCurrentSession().save(disputecomment);
	}

	public void addMerchant(Merchant merchant1) {
		this.sessionFactory.getCurrentSession().save(merchant1);
	}
	

	public void addMerchantAccount(MerchantAccount account) {
		this.sessionFactory.getCurrentSession().save(account);
	}
	

	public void addMerchantContract(MerchantContract merchantcontract) {
		this.sessionFactory.getCurrentSession().save(merchantcontract);
	}
	

	public void addMerchantRefundPolicy(MerchantRefundPolicy merchantrefundpolicy) {
		this.sessionFactory.getCurrentSession().save(merchantrefundpolicy);
	}

	public void addSettlement(Settlement settlement) {
		this.sessionFactory.getCurrentSession().save(settlement);
	}
	

	public void addTransactions(Transactions transaction) {
		this.sessionFactory.getCurrentSession().save(transaction);
	}
	
	public void addUser(User user1) {
		this.sessionFactory.getCurrentSession().save(user1);
	}
	public void addUserRole(UserRole userRole) {
		this.sessionFactory.getCurrentSession().save(userRole);
	}
	public void addUserRight(UserRight userRight) {
		this.sessionFactory.getCurrentSession().save(userRight);
	}
	
	
	
	
	// Get All 
	
	@SuppressWarnings("unchecked")
	public ArrayList<Bank> getAllBanks() {
		return (ArrayList<Bank>) this.sessionFactory.getCurrentSession().createQuery("from Bank").list();
	}


	@SuppressWarnings("unchecked")
	public ArrayList<BankContract> getAllBankContracts() {
		return (ArrayList<BankContract>) this.sessionFactory.getCurrentSession().createQuery("from BankContract").list();
	}
	

	@SuppressWarnings("unchecked")
	public ArrayList<Consumer> getAllConsumers() {
		return (ArrayList<Consumer>) this.sessionFactory.getCurrentSession().createQuery("from Consumer").list();
	}
	

	@SuppressWarnings("unchecked")
	public ArrayList<ConsumerRefund> getAllConsumerRefunds() {
		return (ArrayList<ConsumerRefund>) this.sessionFactory.getCurrentSession().createQuery("from ConsumerRefund").list();
	}
	

	@SuppressWarnings("unchecked")
	public ArrayList<Dispute> getAllDispute() {
		return (ArrayList<Dispute>) this.sessionFactory.getCurrentSession().createQuery("from Dispute").list();
	}
	

	@SuppressWarnings("unchecked")
	public ArrayList<DisputeComment> getAllDisputeComments() {
		return (ArrayList<DisputeComment>) this.sessionFactory.getCurrentSession().createQuery("from DisputeComment").list();
	}
	

	@SuppressWarnings("unchecked")
	public ArrayList<Merchant> getAllMerchants() {
		return (ArrayList<Merchant>) this.sessionFactory.getCurrentSession().createQuery("from Merchant").list();
	}
	

	@SuppressWarnings("unchecked")
	public ArrayList<MerchantAccount> getAllMerchantAccounts() {
		return (ArrayList<MerchantAccount>) this.sessionFactory.getCurrentSession().createQuery("from MerchantAccount").list();
	}
	
	
	@SuppressWarnings("unchecked")
	public ArrayList<MerchantContract> getAllMerchantContracts() {
		return (ArrayList<MerchantContract>) this.sessionFactory.getCurrentSession().createQuery("from MerchantContract").list();
	}
	

	@SuppressWarnings("unchecked")
	public ArrayList<MerchantRefundPolicy> getAllMerchantRefundPolicys() {
		return (ArrayList<MerchantRefundPolicy>) this.sessionFactory.getCurrentSession().createQuery("from MerchantRefundPolicy").list();
	}
	
	
	@SuppressWarnings("unchecked")
	public ArrayList<Settlement> getAllSettlements() {
		return (ArrayList<Settlement>) this.sessionFactory.getCurrentSession().createQuery("from Settlement").list();
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Transactions> getAllTransactions() {
		return (ArrayList<Transactions>) this.sessionFactory.getCurrentSession().createQuery("from Transactions").list();
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
	
	
	
	
	
	// Read by Id

	public Bank getBank(Integer id) {
		return (Bank) this.sessionFactory.getCurrentSession().get(Bank.class,id);
	}

	public BankContract getBankContract(Integer id) {
		return (BankContract) this.sessionFactory.getCurrentSession().get(BankContract.class,id);
	}
	
	public Consumer getConsumer(Integer id) {
		return (Consumer) this.sessionFactory.getCurrentSession().get(Consumer.class,id);
	}
	
	public ConsumerRefund getConsumerRefund(Integer id) {
		return (ConsumerRefund) this.sessionFactory.getCurrentSession().get(ConsumerRefund.class,id);
	}
	
	public Dispute getDispute(Integer id) {
		return (Dispute) this.sessionFactory.getCurrentSession().get(Dispute.class,id);
	}
	
	public DisputeComment getDisputeComment(Integer id) {
		return (DisputeComment) this.sessionFactory.getCurrentSession().get(DisputeComment.class,id);
	}
	
	public Merchant getMerchant(Integer id) {
		return (Merchant) this.sessionFactory.getCurrentSession().get(Merchant.class,id);
	}
	
	public MerchantAccount getMerchantAccount(Integer id) {
		return (MerchantAccount) this.sessionFactory.getCurrentSession().get(MerchantAccount.class,id);
	}
	
	public MerchantContract getMerchantContract(Integer id) {
		return (MerchantContract) this.sessionFactory.getCurrentSession().get(MerchantContract.class,id);
	}
	
	public MerchantRefundPolicy getMerchantRefundPolicy(Integer id) {
		return (MerchantRefundPolicy) this.sessionFactory.getCurrentSession().get(MerchantRefundPolicy.class,id);
	}
	
	public Settlement getSettlement(Integer id) {
		return (Settlement) this.sessionFactory.getCurrentSession().get(Settlement.class,id); 
	}
	
	public Transactions getTransaction(Integer id) {
		return (Transactions) this.sessionFactory.getCurrentSession().get(Transactions.class,id);
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
	
	
	
	
	//Update
	
	public void updateBank(Bank bank) {
		this.sessionFactory.getCurrentSession().update(bank);
	}
	
	public void updateBankContract(BankContract bankcontract) {
		this.sessionFactory.getCurrentSession().update(bankcontract);
	}
	
	public void updateConsumer(Consumer cons1)
	{
		this.sessionFactory.getCurrentSession().update(cons1);
	}
	
	public void updateConsumerRefund(ConsumerRefund consumerrefund)
	{
		this.sessionFactory.getCurrentSession().update(consumerrefund);
	}
	
	public void updateDispute(Dispute dispute)
	{
		this.sessionFactory.getCurrentSession().update(dispute);
	}
	
	public void updateDispute(DisputeComment disputecomment)
	{
		this.sessionFactory.getCurrentSession().update(disputecomment);
	}
	
	public void updateMerchant(Merchant merchant1) {
		this.sessionFactory.getCurrentSession().update(merchant1);
	}
	
	public void updateMerchantAccount(MerchantAccount account) {
		this.sessionFactory.getCurrentSession().update(account);
	}
	
	public void updateMerchantContract(MerchantContract merchantcontract) {
		this.sessionFactory.getCurrentSession().update(merchantcontract);
	}
	
	public void updateMerchantRefundPolicy(MerchantRefundPolicy merchantrefundpolicy) {
		this.sessionFactory.getCurrentSession().update(merchantrefundpolicy);
	}
	
	public void updateSettlement(Settlement settlement) {
		this.sessionFactory.getCurrentSession().update(settlement);
	}
	

	public void updateTransactions(Transactions transaction) {
		this.sessionFactory.getCurrentSession().update(transaction);
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
	
	//All Delete operations
	
	public void deleteBank(Integer bankId) {
		Bank bank = (Bank) sessionFactory.getCurrentSession().load(
				Bank.class, bankId);
        if (null != bank) {
        	this.sessionFactory.getCurrentSession().delete(bank);
        }
	}
	
	public void deleteBankContract(Integer bankContractId) {
		BankContract bankcontract = (BankContract) sessionFactory.getCurrentSession().load(
				BankContract.class, bankContractId);
        if (null != bankcontract) {
        	this.sessionFactory.getCurrentSession().delete(bankcontract);
        }
	}

	public void deleteConsumer(Integer consumerId) {
		Consumer consumer = (Consumer) sessionFactory.getCurrentSession().load(
				Consumer.class, consumerId);
        if (null != consumer) {
        	this.sessionFactory.getCurrentSession().delete(consumer);
        }
	}
	
	
	public void deleteConsumerRefund(Integer consumerRefundId) {
		ConsumerRefund consumerRefund = (ConsumerRefund) sessionFactory.getCurrentSession().load(
				ConsumerRefund.class, consumerRefundId);
        if (null != consumerRefund) {
        	this.sessionFactory.getCurrentSession().delete(consumerRefund);
        }
	}
	
	
	public void deleteDispute(Integer disputeId) {
		Dispute dispute = (Dispute) sessionFactory.getCurrentSession().load(
				Dispute.class, disputeId);
        if (null != dispute) {
        	this.sessionFactory.getCurrentSession().delete(dispute);
        }
	}
	
	public void deleteDisputeComment(Integer disputeCommentId) {
		DisputeComment disputeComment = (DisputeComment) sessionFactory.getCurrentSession().load(
				DisputeComment.class, disputeCommentId);
        if (null != disputeComment) {
        	this.sessionFactory.getCurrentSession().delete(disputeComment);
        }
	}
	
	public void deleteMerchant(Integer MerchantId) {
		Merchant merchant = (Merchant) sessionFactory.getCurrentSession().load(
				Merchant.class, MerchantId);
        if (null != merchant) {
        	this.sessionFactory.getCurrentSession().delete(merchant);
        }
	}
	
	public void deleteMerchantAccount(Integer MerchantAccountId) {
		MerchantAccount merchantAccount = (MerchantAccount) sessionFactory.getCurrentSession().load(
				Merchant.class, MerchantAccountId);
        if (null != merchantAccount) {
        	this.sessionFactory.getCurrentSession().delete(merchantAccount);
        }
	}

	public void deleteMerchantContract(Integer MerchantContractId) {
		MerchantContract merchantContract = (MerchantContract) sessionFactory.getCurrentSession().load(
				MerchantContract.class, MerchantContractId);
        if (null != merchantContract) {
        	this.sessionFactory.getCurrentSession().delete(merchantContract);
        }
	}

	public void deleteMerchantRefundPolicy(Integer MerchantRefundPolicyId) {
		MerchantRefundPolicy merchantRefundPolicy = (MerchantRefundPolicy) sessionFactory.getCurrentSession().load(
				MerchantRefundPolicy.class, MerchantRefundPolicyId);
        if (null != merchantRefundPolicy) {
        	this.sessionFactory.getCurrentSession().delete(merchantRefundPolicy);
        }
	}
	
	public void deleteSettlement(Integer settlementId) {
		Settlement settlement = (Settlement) sessionFactory.getCurrentSession().load(
				Settlement.class, settlementId);
        if (null != settlement) {
        	this.sessionFactory.getCurrentSession().delete(settlement);
        }
	}
	
	public void deleteTransaction(Integer transactionId) {
		Transactions transaction = (Transactions) sessionFactory.getCurrentSession().load(
				Transactions.class, transactionId);
        if (null != transaction) {
        	this.sessionFactory.getCurrentSession().delete(transaction);
        }
	}
	
	public void deleteUser(Integer userId) {
		User user = (User) sessionFactory.getCurrentSession().load(
				User.class, userId);
        if (null != user) {
        	this.sessionFactory.getCurrentSession().delete(user);
        }
	}
	
	public void deleteUserRight(Integer userRightId) {
		UserRight userRight = (UserRight) sessionFactory.getCurrentSession().load(
				UserRight.class, userRightId);
        if (null != userRight) {
        	this.sessionFactory.getCurrentSession().delete(userRight);
        }
	}
	
	public void deleteUserRole(Integer userRoleId) {
		UserRole userRole = (UserRole) sessionFactory.getCurrentSession().load(
				UserRole.class, userRoleId);
        if (null != userRole) {
        	this.sessionFactory.getCurrentSession().delete(userRole);
        }
	}
	
	
	
	
//	@Override
//
//	public  List<UserRole> getRolesByUsernames(String username) {
//
//
//	    String hql = "FROM User U where U.username = :username";
//
//	    Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
//
//	    query.setParameter("username", username);
//	   
//	   
//
//	    @SuppressWarnings("unchecked")
//
//	ArrayList<User> results  = (ArrayList<User>) query.list();
//	    System.out.println(results.size());
//
//	if(results.size()==0)
//
//	return null;
//
//
//	else
//	{
//		System.out.println(results.get(0).getRoles().size());
//		return (List<UserRole>) results.get(0).getRoles();
//		
//	}
//	}
	
	
}
