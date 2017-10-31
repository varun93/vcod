package com.i2india.Domain;

import static javax.persistence.GenerationType.AUTO;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name="role")
//Override the default Hibernation delete and set the deleted flag rather than deleting the record from the db.
@SQLDelete(sql="UPDATE user_role SET is_deleted = '1' WHERE role_id = ?")
//Filter added to retrieve only records that have not been soft deleted.
@Where(clause="is_deleted <> '1'")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })

public class UserRole implements GrantedAuthority,Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=AUTO)
    @Column(name="role_id")
    private int role_id;
	
    @Column(name="name")
	private String name;
    
    @ManyToMany(cascade = {CascadeType.ALL},fetch = FetchType.EAGER)
    @JoinTable(name="role_rights", 
                joinColumns={@JoinColumn(name="role_id")}, 
                inverseJoinColumns={@JoinColumn(name="right_id")})
    private Set<UserRight> userRight;

	public Set<UserRight> getUserRight() {
		return userRight;
	}

	public void setUserRight(Set<UserRight> userRight) {
		this.userRight = userRight;
	}

	public int getRole_id() {
		return role_id;
	}

	public void setRole_id(int role_id) {
		this.role_id = role_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	 @Override
	    public String getAuthority() {
	        return this.name;
	    }
	 
}
