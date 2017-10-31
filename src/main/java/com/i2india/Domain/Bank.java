package com.i2india.Domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@SuppressWarnings("serial")
@Entity
@Table(name="bank")
//Override the default Hibernation delete and set the deleted flag rather than deleting the record from the db.
@SQLDelete(sql="UPDATE bank SET is_deleted = '1' WHERE bank_id = ?")
//Filter added to retrieve only records that have not been soft deleted.
@Where(clause="is_deleted <> '1'")
@PrimaryKeyJoinColumn(name="bank_id")
public class Bank extends User implements Serializable {

	@Column(name = "bank_name")	
	private String bank_name;
	
	@Column(name = "account_number")	
	private Integer account_number;
	
	@Column(name = "ifsc_code")	
	private Integer ifsc_code;
	
	@Column(name = "account_name")	
	private String account_name;
	
	@Column(name = "branch")	
	private String branch;
	
	@Column(name = "address_bank")	
	private String address_bank ;
	
	@Column(name = "transaction_url")	
	private String transaction_url;
	
	@Column(name = "bank_merchant_id")	
	private String bank_merchant_id;
	
	@Column(name = "secret_key")
	private String secret_key;
	


	public int getAccount_number() {
		return account_number;
	}
	public void setAccount_number(Integer account_number) {
		this.account_number = account_number;
	}
	public String getBank_name() {
		return bank_name;
	}
	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}
	public int getIfsc_code() {
		return ifsc_code;
	}
	public void setIfsc_code(Integer ifsc_code) {
		this.ifsc_code = ifsc_code;
	}
	public String getAccount_name() {
		return account_name;
	}
	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public String getAddress_bank() {
		return address_bank;
	}
	public void setAddress_bank(String address_bank) {
		this.address_bank = address_bank;
	}
	public String getTransaction_url() {
		return transaction_url;
	}
	public void setTransaction_url(String transaction_url) {
		this.transaction_url = transaction_url;
	}
	public String getBank_merchant_id() {
		return bank_merchant_id;
	}
	public void setBank_merchant_id(String bank_merchant_url) {
		this.bank_merchant_id = bank_merchant_url;
	}
	public String getSecret_key() {
		return secret_key;
	}
	public void setSecret_key(String secret_key) {
		this.secret_key = secret_key;
	}

	
}
