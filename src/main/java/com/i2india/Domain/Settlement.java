package com.i2india.Domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name="settlement")
//Override the default Hibernation delete and set the deleted flag rather than deleting the record from the db.
@SQLDelete(sql="UPDATE settlement SET is_deleted = '1' WHERE settlement_id = ?")
//Filter added to retrieve only records that have not been soft deleted.
@Where(clause="is_deleted <> '1'")
@JsonIgnoreProperties({ "merchantaccount" , "bankcontract", "hibernateLazyInitializer", "handler" })

public class Settlement {
	
	@Id
	@GeneratedValue(strategy=IDENTITY)
    @Column(name="settlement_id")
	private int settlement_id;
	
    @Column(name="reference_id")
	private int reference_id;
    
    @Column(name="status")
	private String status;
    
    @Column(name="activity_time")
	private Date activity_time;
    
    @Column(name="bank_contract_amount")
	private double bank_contract_amount;
    
    @Column(name="amount")
	private double amount;
    
    @Column(name="currency")
	private String currency;
    
    @Column(name="paymode")
	private String paymode;
    
    @Column(name="other_details")
	private String other_details;
    
    @Column(name="reason")
	private String reason;
    
    @Column(name="transactions")
	private String transactions;
    
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="account_id")
    private MerchantAccount merchantaccount;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="bank_contract_id")
    private BankContract bankcontract;
    
	public int getSettlement_id() {
		return settlement_id;
	}
	public void setSettlement_id(int settlement_id) {
		this.settlement_id = settlement_id;
	}

	public int getReference_id() {
		return reference_id;
	}
	public void setReference_id(int reference_id) {
		this.reference_id = reference_id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getActivity_time() {
		return activity_time;
	}
	public void setActivity_time(Date activity_time) {
		this.activity_time = activity_time;
	}
	public double getBank_contract_amount() {
		return bank_contract_amount;
	}
	public void setBank_contract_amount(double bank_contract_amount) {
		this.bank_contract_amount = bank_contract_amount;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getPaymode() {
		return paymode;
	}
	public void setPaymode(String paymode) {
		this.paymode = paymode;
	}
	public String getOther_details() {
		return other_details;
	}
	public void setOther_details(String other_details) {
		this.other_details = other_details;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getTransactions() {
		return transactions;
	}
	public void setTransactions(String transactions) {
		this.transactions = transactions;
	}
	public MerchantAccount getMerchantaccount() {
		return merchantaccount;
	}
	public void setMerchantaccount(MerchantAccount merchantaccount) {
		this.merchantaccount = merchantaccount;
	}
	public BankContract getBankcontract() {
		return bankcontract;
	}
	public void setBankcontract(BankContract bankcontract) {
		this.bankcontract = bankcontract;
	}
}
