package com.i2india.Domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@Entity
@Table(name="transactions")
//Override the default Hibernation delete and set the deleted flag rather than deleting the record from the db.
@SQLDelete(sql="UPDATE transactions SET is_deleted = '1' WHERE transaction_id = ?")
//Filter added to retrieve only records that have not been soft deleted.
@Where(clause="is_deleted <> '1'")
@JsonIgnoreProperties({"reference_id","transaction_amount","transaction_other_details","transaction_status_description","vcod_amount","transaction_country","bank_amount","reconciliation_id","transaction_details","transaction_status","consumer","merchant", "bankcontract" , "merchantcontract" , "order" , "hibernateLazyInitializer", "handler" })
//@Cache(usage=CacheConcurrencyStrategy.READ_ONLY, region="transaction")
public class Transactions implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="transaction_id")
	private int transaction_id;
	
	@Column(name="reference_id")
	private String reference_id;
	
	@Column(name="status", columnDefinition="enum('INITIATED','SUCCESS','FAIL','REFUND','CLOSED','DELIVERED','DISPUTE')")
	private String status;
	
	@Column(name="merchant_activity_time")
	private Date merchant_activity_time;
	
	@Column(name="refund_amount")
	private double refund_amount;
	
	@Column(name="transaction_amount")
	private double transaction_amount;
	
	@Column(name="customer_phonenumber")
	private long customer_phonenumber;
	
	
	@Column(name="delivery_time")
	private Date delivery_time;
	
	@Column(name="merchant_currency")
	private String merchant_currency;
	
	@Column(name="transaction_paymode")
	private String transaction_paymode;
	
	@Column(name="transaction_other_details")
	private String transaction_other_details;
	
	@Column(name="transaction_status_description")
	private String transaction_status_description;
	
	
	@Column(name="merchant_reconciliation_amount")
	private double merchant_reconcilation_amount;
	
	@Column(name="vcod_amount")
	private double vcod_amount;
	
	@Column(name="transaction_country")
	private String transaction_country;
	
	@Column(name="bank_amount")
	private double bank_amount;
	
	@Column(name="merchant_reconciliation_Status")
	private String merchant_reconcilation_status;
	
	@Column(name="reconciliation_id")
	private int reconciliation_id;
	
	@Column(name="merchant_order_id")
	private String merchant_order_id;	
	
	@Column(name="bank_activity_time")
	private Date bank_activity_time;	
	
	@Column(name="merchant_success_url")
	private String merchant_success_url;	

	@Column(name="transaction_details")
	private String transaction_details;	
	
	
	@Column(name="merchant_fail_url")
	private String merchant_fail_url;	

	@Column(name="merchant_paymode")
	private String merchant_paymode;	
	
	@Column(name="transaction_currency")
	private String transaction_currency;	
	
	
	@Column(name="merchant_country")
	private String merchant_country;	
	
	@Column(name="transaction_status")
	private String transaction_status;	
	
	@Column(name="merchant_details")
	private String merchant_details;	
	@Column(name="merchant_amount")
	private double merchant_amount;


	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="consumer_id")
    private Consumer consumer;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="merchant_id")
    private Merchant merchant;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="bank_contract_id")
    private BankContract bankcontract;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="merchant_contract_id")
    private MerchantContract merchantcontract;
	
	public int getTransaction_id() {
		return transaction_id;
	}
	public void setTransaction_id(int transaction_id) {
		this.transaction_id = transaction_id;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public double getVcod_amount() {
		return vcod_amount;
	}
	public void setVcod_amount(double vcod_amount) {
		this.vcod_amount = vcod_amount;
	}
	public double getBank_amount() {
		return bank_amount;
	}
	public void setBank_amount(double bank_amount) {
		this.bank_amount = bank_amount;
	}
	public int getReconciliation_id() {
		return reconciliation_id;
	}
	public void setReconciliation_id(int reconciliation_id) {
		this.reconciliation_id = reconciliation_id;
	}
	public BankContract getBankcontract() {
		return bankcontract;
	}
	public void setBankcontract(BankContract bankcontract) {
		this.bankcontract = bankcontract;
	}
	public MerchantContract getMerchantcontract() {
		return merchantcontract;
	}
	public void setMerchantcontract(MerchantContract merchantcontract) {
		this.merchantcontract = merchantcontract;
	}
	public String getReference_id() {
		return reference_id;
	}
	public void setReference_id(String reference_id) {
		this.reference_id = reference_id;
	}
	public Date getMerchant_activity_time() {
		return merchant_activity_time;
	}
	public void setMerchant_activity_time(Date merchant_activity_time) {
		this.merchant_activity_time = merchant_activity_time;
	}
	public double getTransaction_amount() {
		return transaction_amount;
	}
	public void setTransaction_amount(double transaction_amount) {
		this.transaction_amount = transaction_amount;
	}
	public String getTransaction_paymode() {
		return transaction_paymode;
	}
	public void setTransaction_paymode(String transaction_paymode) {
		this.transaction_paymode = transaction_paymode;
	}
	public String getTransaction_other_details() {
		return transaction_other_details;
	}
	public void setTransaction_other_details(String transaction_other_details) {
		this.transaction_other_details = transaction_other_details;
	}
	public String getMerchant_order_id() {
		return merchant_order_id;
	}
	public void setMerchant_order_id(String merchant_order_id) {
		this.merchant_order_id = merchant_order_id;
	}
	public Date getBank_activity_time() {
		return bank_activity_time;
	}
	public void setBank_activity_time(Date bank_activity_time) {
		this.bank_activity_time = bank_activity_time;
	}
	public String getMerchant_success_url() {
		return merchant_success_url;
	}
	public void setMerchant_success_url(String merchant_success_url) {
		this.merchant_success_url = merchant_success_url;
	}
	public String getMerchant_country() {
		return merchant_country;
	}
	public void setMerchant_country(String merchant_country) {
		this.merchant_country = merchant_country;
	}
	public String getMerchant_details() {
		return merchant_details;
	}
	public void setMerchant_details(String merchant_details) {
		this.merchant_details = merchant_details;
	}
	public double getMerchant_amount() {
		return merchant_amount;
	}
	public void setMerchant_amount(double merchant_amount) {
		this.merchant_amount = merchant_amount;
	}
	public Consumer getConsumer() {
		return consumer;
	}
	public void setConsumer(Consumer consumer) {
		this.consumer = consumer;
	}
	public Merchant getMerchant() {
		return merchant;
	}
	public void setMerchant(Merchant merchant) {
		this.merchant = merchant;
	}
	public String getMerchant_currency() {
		return merchant_currency;
	}
	public void setMerchant_currency(String merchant_currency) {
		this.merchant_currency = merchant_currency;
	}
	public String getMerchant_fail_url() {
		return merchant_fail_url;
	}
	public void setMerchant_fail_url(String merchant_fail_url) {
		this.merchant_fail_url = merchant_fail_url;
	}
	public String getMerchant_paymode() {
		return merchant_paymode;
	}
	public void setMerchant_paymode(String merchant_paymode) {
		this.merchant_paymode = merchant_paymode;
	}
	public String getTransaction_currency() {
		return transaction_currency;
	}
	public void setTransaction_currency(String transaction_currency) {
		this.transaction_currency = transaction_currency;
	}
	public String getTransaction_details() {
		return transaction_details;
	}
	public void setTransaction_details(String transaction_details) {
		this.transaction_details = transaction_details;
	}
	public String getTransaction_status_description() {
		return transaction_status_description;
	}
	public void setTransaction_status_description(
			String transaction_status_description) {
		this.transaction_status_description = transaction_status_description;
	}
	public String getTransaction_country() {
		return transaction_country;
	}
	public void setTransaction_country(String transaction_country) {
		this.transaction_country = transaction_country;
	}
	public String getTransaction_status() {
		return transaction_status;
	}
	public void setTransaction_status(String transaction_status) {
		this.transaction_status = transaction_status;
	}
	public long getCustomer_phonenumber() {
		return customer_phonenumber;
	}
	public void setCustomer_phonenumber(long customer_phonenumber) {
		this.customer_phonenumber = customer_phonenumber;
	}
	public double getRefund_amount() {
		return refund_amount;
	}
	public void setRefund_amount(double refund_amount) {
		this.refund_amount = refund_amount;
	}
	public Date getDelivery_time() {
		return delivery_time;
	}
	public void setDelivery_time(Date delivery_time) {
		this.delivery_time = delivery_time;
	}
	public double getMerchant_reconcilation_amount() {
		return merchant_reconcilation_amount;
	}
	public void setMerchant_reconcilation_amount(
			double merchant_reconcilation_amount) {
		this.merchant_reconcilation_amount = merchant_reconcilation_amount;
	}
	public String getMerchant_reconcilation_status() {
		return merchant_reconcilation_status;
	}
	public void setMerchant_reconcilation_status(
			String merchant_reconcilation_status) {
		this.merchant_reconcilation_status = merchant_reconcilation_status;
	}
	
}
