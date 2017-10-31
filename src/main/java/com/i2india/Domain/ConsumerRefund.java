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

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name="consumer_refund")
//Override the default Hibernation delete and set the deleted flag rather than deleting the record from the db.
@SQLDelete(sql="UPDATE consumer_refund SET is_deleted = '1' WHERE refund_id = ?")
//Filter added to retrieve only records that have not been soft deleted.
@Where(clause="is_deleted <> '1'")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class ConsumerRefund {	
	
	@Id
	@Column(name = "refund_id")
	@GeneratedValue(strategy=IDENTITY)
	private int refund_id;
	
	@Column(name = "bank_amount")
	private int bank_amount;
	
	@Column(name = "reference_id")
	private int reference_id;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "amount")
	private double amount;
	
	@Column(name = "activity_time")
	private Date activity_time;
	
	@Column(name = "currency")
	private String currency;
	
	@Column(name = "paymode")
	private String paymode;
	
	@Column(name = "other_details")
	private String other_details;
	
	@Column(name = "reason")
	private String reason;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="dispite_id")
    private Dispute dispute;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="bank_contract_id")
    private BankContract bankcontract;
	
	public int getRefund_id() {
		return refund_id;
	}
	public void setRefund_id(int refund_id) {
		this.refund_id = refund_id;
	}

	public int getBank_amount() {
		return bank_amount;
	}
	public void setBank_amount(int bank_amount) {
		this.bank_amount = bank_amount;
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
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public Date getActivity_time() {
		return activity_time;
	}
	public void setActivity_time(Date activity_time) {
		this.activity_time = activity_time;
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
	public Dispute getDispute() {
		return dispute;
	}
	public void setDispute(Dispute dispute) {
		this.dispute = dispute;
	}
	public BankContract getBankcontract() {
		return bankcontract;
	}
	public void setBankcontract(BankContract bankcontract) {
		this.bankcontract = bankcontract;
	}
}
