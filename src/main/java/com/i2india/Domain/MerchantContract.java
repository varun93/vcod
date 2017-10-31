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
@Table(name="merchant_contract")
//Override the default Hibernation delete and set the deleted flag rather than deleting the record from the db.
@SQLDelete(sql="UPDATE merchant_contract SET is_deleted = '1' WHERE contract_id = ?")
//Filter added to retrieve only records that have not been soft deleted.
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Where(clause="is_deleted <> '1'")
public class MerchantContract {

	@Id
	@GeneratedValue(strategy=IDENTITY)
    @Column(name="contract_id")	
	private int contract_id;
	
    
    @Column(name="paymode")	
	private String paymode;
    
    @Column(name="rate")	
	private double rate;
    
    @Column(name="contract_date")	
	private Date contract_date;
    
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="merchant_id")
    private Merchant merchant;
    

	public int getContract_id() {
		return contract_id;
	}
	public void setContract_id(int contract_id) {
		this.contract_id = contract_id;
	}
	public String getPaymode() {
		return paymode;
	}
	public void setPaymode(String paymode) {
		this.paymode = paymode;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
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
	public void setMerchant(Merchant merchant) {
		this.merchant = merchant;
	}

}
