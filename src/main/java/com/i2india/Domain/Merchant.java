package com.i2india.Domain;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@SuppressWarnings("serial")
@Entity
@Table(name="merchant")
//Override the default Hibernation delete and set the deleted flag rather than deleting the record from the db.
@SQLDelete(sql="UPDATE merchant SET is_deleted = '1' WHERE merchant_id = ?")
//Filter added to retrieve only records that have not been soft deleted.
@Where(clause="is_deleted <> '1'")
@JsonIgnoreProperties({"user_id","user_role","accountNonExpired", "last_login" ,"registered_date","mobile",
	"accountNonLocked","credentialsNonExpired","enabled","is_deleted",
	"status", "is_deleted",
	"roles" ,"authorities" ,"username", "password" , "secret_key" ,  "hibernateLazyInitializer", "handler" })
@PrimaryKeyJoinColumn(name="merchant_id")
public class Merchant extends User implements Serializable {
	
	@Column(name = "secret_key")
	private String secret_key;


	@Column(name = "organization_name")
	private String organization_name;
	
	@Column(name = "landline")
	private long landline;
	
	@Column(name = "business_type")
	private String business_type;
	
	@Column(name = "official_landline1")
	private long official_landline1;
	
	@Column(name = "official_landline2")
	private long official_landline2;
	
	@Column(name = "official_landline3")
	private long official_landline3;
	
	@Column(name = "official_mobile")
	private long official_mobile;
	
	@Column(name = "official_email")
	private String official_email;
	
	@Column(name = "registered_address")
	private String registered_address;
	
	@Column(name = "registered_city")
	private String registered_city;
	
	@Column(name = "registered_state")
	private String registered_state;
	
	@Column(name = "registered_country")
	private String registered_country;
	
	@Column(name = "registered_pincode")
	private int registered_pincode;
	
	@Column(name = "operating_address")
	private String operating_address;
	
	@Column(name = "operating_city")
	private String operating_city;
	
	@Column(name = "operating_state")
	private String operating_state;
	
	@Column(name = "operating_pincode")
	private int operating_pincode;
	
	@Column(name = "operating_country")
	private String operating_country;	
	
	@Column(name = "business_category")
	private String business_category;
	
	@Column(name = "pancard_no")
	private String pancard_no;
	
	@Column(name = "pancard_name")
	private String pancard_name;
	
	@Column(name = "pancard_est_date")
	private Date pancard_est_date;

	public String getSecret_key() {
		return secret_key;
	}
	public void setSecret_key(String secret_key) {
		this.secret_key = secret_key;
	}
	
	public long getLandline() {
		return landline;
	}
	public void setLandline(long landline) {
		this.landline = landline;
	}
	public String getBusiness_type() {
		return business_type;
	}
	public void setBusiness_type(String business_type) {
		this.business_type = business_type;
	}
	public long getOfficial_landline1() {
		return official_landline1;
	}
	public void setOfficial_landline1(long official_landline1) {
		this.official_landline1 = official_landline1;
	}
	public long getOfficial_landline2() {
		return official_landline2;
	}
	public void setOfficial_landline2(long official_landline2) {
		this.official_landline2 = official_landline2;
	}
	public long getOfficial_landline3() {
		return official_landline3;
	}
	public void setOfficial_landline3(long official_landline3) {
		this.official_landline3 = official_landline3;
	}
	public long getOfficial_mobile() {
		return official_mobile;
	}
	public void setOfficial_mobile(long official_mobile) {
		this.official_mobile = official_mobile;
	}
	public String getOfficial_email() {
		return official_email;
	}
	public void setOfficial_email(String official_email) {
		this.official_email = official_email;
	}
	public String getRegistered_address() {
		return registered_address;
	}
	public void setRegistered_address(String registered_address) {
		this.registered_address = registered_address;
	}
	public String getRegistered_city() {
		return registered_city;
	}
	public void setRegistered_city(String registered_city) {
		this.registered_city = registered_city;
	}
	public String getRegistered_state() {
		return registered_state;
	}
	public void setRegistered_state(String registered_state) {
		this.registered_state = registered_state;
	}
	public String getRegistered_country() {
		return registered_country;
	}
	public void setRegistered_country(String registered_country) {
		this.registered_country = registered_country;
	}
	public String getOperating_address() {
		return operating_address;
	}
	public void setOperating_address(String operating_address) {
		this.operating_address = operating_address;
	}
	public String getOperating_city() {
		return operating_city;
	}
	public void setOperating_city(String operating_city) {
		this.operating_city = operating_city;
	}
	public String getOperating_state() {
		return operating_state;
	}
	public void setOperating_state(String operating_state) {
		this.operating_state = operating_state;
	}
	public int getOperating_pincode() {
		return operating_pincode;
	}
	public void setOperating_pincode(int operating_pincode) {
		this.operating_pincode = operating_pincode;
	}
	public String getOperating_country() {
		return operating_country;
	}
	public void setOperating_country(String operating_country) {
		this.operating_country = operating_country;
	}
	public String getBusiness_category() {
		return business_category;
	}
	public void setBusiness_category(String business_category) {
		this.business_category = business_category;
	}
	public String getPancard_no() {
		return pancard_no;
	}
	public void setPancard_no(String pancard_no) {
		this.pancard_no = pancard_no;
	}
	public String getPancard_name() {
		return pancard_name;
	}
	public void setPancard_name(String pancard_name) {
		this.pancard_name = pancard_name;
	}
	public Date getPancard_est_date() {
		return pancard_est_date;
	}
	public void setPancard_est_date(Date pancard_est_date) {
		this.pancard_est_date = pancard_est_date;
	}
	public int getRegistered_pincode() {
		return registered_pincode;
	}
	public void setRegistered_pincode(int registered_pincode) {
		this.registered_pincode = registered_pincode;
	}
	public String getOrganization_name() {
		return organization_name;
	}
	public void setOrganization_name(String organization_name) {
		this.organization_name = organization_name;
	}
}
