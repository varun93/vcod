package com.i2india.Domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name="dispute")
//Override the default Hibernation delete and set the deleted flag rather than deleting the record from the db.
@SQLDelete(sql="UPDATE dispute SET is_deleted = '1' WHERE dispute_id = ?")
//Filter added to retrieve only records that have not been soft deleted.
@Where(clause="is_deleted <> '1'")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler","transaction" })
public class Dispute {

	@Id
	@GeneratedValue(strategy=IDENTITY)
    @Column(name="dispute_id")
	private int dispute_id;
	
	@Column(name="type", columnDefinition="enum('MERCHANT_REFUND',  'PRODUCT_DISPUTE' )")
	private String type;
	
	
    @Column(name="activity_time")
	private Date activity_time;
    
    @Column(name="title")
	private String title;
    
    @Column(name="description")
	private String description;
    
    @Column(name="disputed_amount")
	private double disputed_amount;
    
    @Column(name="refund_policy_id")
	private int refund_policy_id;
    
    @Column(name="status", columnDefinition="enum('OPEN',  'CLOSED' )")
	private String status;
    
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="transaction_id")
    private Transactions 	transaction;
    
	public int getDispute_id() {
		return dispute_id;
	}
	public void setDispute_id(int dispute_id) {
		this.dispute_id = dispute_id;
	}

	public Date getActivity_time() {
		return activity_time;
	}
	public void setActivity_time(Date activity_time) {
		this.activity_time = activity_time;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getDisputed_amount() {
		return disputed_amount;
	}
	public void setDisputed_amount(double disputed_amount) {
		this.disputed_amount = disputed_amount;
	}
	public int getRefund_policy_id() {
		return refund_policy_id;
	}
	public void setRefund_policy_id(int refund_policy_id) {
		this.refund_policy_id = refund_policy_id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Transactions getTransaction() {
		return transaction;
	}
	public void setTransaction(Transactions transaction_id) {
		this.transaction = transaction_id;
	}
}
