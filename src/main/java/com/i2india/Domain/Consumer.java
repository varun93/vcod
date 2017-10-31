package com.i2india.Domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@SuppressWarnings("serial")
@Entity
@Table(name="consumer")
//Override the default Hibernation delete and set the deleted flag rather than deleting the record from the db.
@SQLDelete(sql="UPDATE consumer SET is_deleted = '1' WHERE consumer_id = ?")
//Filter added to retrieve only records that have not been soft deleted.
@Where(clause="is_deleted <> '1'")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@PrimaryKeyJoinColumn(name="consumer_id")
public class Consumer extends User implements Serializable{

	@Column(name = "address")	
	private String address;
	@Column(name = "city")
	private String city;
	@Column(name = "country")
	private String country;
	@Column(name = "mobile")
	private long mobile;
	@Column(name = "landline")
	private long landline;
	@Column(name = "firstname")
	private String firstname;

	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public long getMobile() {
		return mobile;
	}
	
	public void setMobile(long l) {
		this.mobile = l;
	}
	public long getLandline() {
		return landline;
	}
	public void setLandline(long landline) {
		this.landline = landline;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	
}
