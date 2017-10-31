package com.i2india.Domain;

import static javax.persistence.GenerationType.AUTO;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name="rights")
//Override the default Hibernation delete and set the deleted flag rather than deleting the record from the db.
@SQLDelete(sql="UPDATE user_role SET is_deleted = '1' WHERE right_id = ?")
//Filter added to retrieve only records that have not been soft deleted.
@Where(clause="is_deleted <> '1'")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class UserRight implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 697453452431809696L;


	@Id
	@GeneratedValue(strategy=AUTO)
    @Column(name="right_id")
    private int right_id;
	
	
    @Column(name="name")
	private String name;


	public int getRight_id() {
		return right_id;
	}


	public void setRight_id(int right_id) {
		this.right_id = right_id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	
}
