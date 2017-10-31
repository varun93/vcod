package com.i2india.Domain;

import static javax.persistence.GenerationType.IDENTITY;

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
@Table(name="merchant_account")
//Override the default Hibernation delete and set the deleted flag rather than deleting the record from the db.
@SQLDelete(sql="UPDATE merchant_account SET is_deleted = '1' WHERE account_id = ?")
//Filter added to retrieve only records that have not been soft deleted.
@Where(clause="is_deleted <> '1'")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler","merchant" })
public class MerchantAccount {

	@Id
	@GeneratedValue(strategy=IDENTITY)
    @Column(name="account_id")
	private int account_id;
    
    @Column(name="isPrimary")
	private int isPrimary;
    
    @Column(name="account_name")
	private String account_name;
    
    @Column(name="account_number")
	private long account_number;
    
    @Column(name="ifsc_code")
	private String ifsc_code;
    
    @Column(name="bank_name")
	private String bank_name;
    
    @Column(name="branch")
	private String branch;
    
    @Column(name="bank_address")
	private String bank_address;
    
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="merchant_id")
    private Merchant merchant;
    
    
	public int getAccount_id() {
		return account_id;
	}
	
	
	public void setAccount_id(int account_id) {
		this.account_id = account_id;
	}
	public int getIsPrimary() {
		return isPrimary;
	}
	public void setIsPrimary(int isPrimary) {
		this.isPrimary = isPrimary;
	}
	public String getAccount_name() {
		return account_name;
	}
	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}
	public long getAccount_number() {
		return account_number;
	}
	public void setAccount_number(long account_number) {
		this.account_number = account_number;
	}
	public String getIfsc_code() {
		return ifsc_code;
	}
	public void setIfsc_code(String ifsc_code) {
		this.ifsc_code = ifsc_code;
	}
	public String getBank_name() {
		return bank_name;
	}
	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public String getBank_address() {
		return bank_address;
	}
	public void setBank_address(String bank_address) {
		this.bank_address = bank_address;
	}
	public void setMerchant(Merchant merchant) {
		this.merchant = merchant;
	}



	public Merchant getMerchant() {
		return merchant;
	}
	
}
