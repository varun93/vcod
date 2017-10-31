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
@Table(name="merchant_refund_policy")
//Override the default Hibernation delete and set the deleted flag rather than deleting the record from the db.
@SQLDelete(sql="UPDATE merchant_refund_policy SET is_deleted = '1' WHERE refund_policy_id = ?")
//Filter added to retrieve only records that have not been soft deleted.
@Where(clause="is_deleted <> '1'")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class MerchantRefundPolicy {
	
	@Id
	@GeneratedValue(strategy=IDENTITY)
    @Column(name="refund_policy_id")	
	private int refund_policy_id;
    
    @Column(name="policy_name")	
	private String policy_name;
    
    @Column(name="description")	
	private String description;
    
    @Column(name="contract_date")	
	private Date contract_date;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="merchant_id")
    private Merchant merchant;
	
	public int getRefund_policy_id() {
		return refund_policy_id;
	}
	public void setRefund_policy_id(int refund_policy_id) {
		this.refund_policy_id = refund_policy_id;
	}

	public String getPolicy_name() {
		return policy_name;
	}
	public void setPolicy_name(String policy_name) {
		this.policy_name = policy_name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getContract_date() {
		return contract_date;
	}
	public void setContract_date(Date contract_date) {
		this.contract_date = contract_date;
	}
	public Merchant getMerchant() {
		return merchant;
	}
}
