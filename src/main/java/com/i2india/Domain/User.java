package com.i2india.Domain;


import static javax.persistence.GenerationType.AUTO;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@SuppressWarnings("serial")
@Entity
@Table(name="user")
//Override the default Hibernation delete and set the deleted flag rather than deleting the record from the db.
@SQLDelete(sql="UPDATE user SET is_deleted = '1' WHERE user_id = ?")
//Filter added to retrieve only records that have not been soft deleted.
@Where(clause="is_deleted <> '1'")
@Inheritance(strategy=InheritanceType.JOINED)

public class User  implements Serializable,UserDetails {
 
	@Id
	@GeneratedValue(strategy=AUTO)
    @Column(name="user_id")
	private int user_id;
	
	
    @Column(name="username")
    private String username;
 
    @Column(name="password")
    private String password;
    
    @Column(name="name")
    private String name;
    
    @Column(name="mobile")
    private long mobile;
    
    @ManyToMany(cascade = {CascadeType.ALL},fetch = FetchType.EAGER)
    @JoinTable(name="user_role", 
                joinColumns={@JoinColumn(name="user_id")}, 
                inverseJoinColumns={@JoinColumn(name="role_id")})
    private Set<UserRole> roles;
    
    
    @Column(name="status", columnDefinition="enum('ACTIVE','PENDING','CANCELLED','SUSPENDED')")
    private String status;
    
    @Column(name="registration", columnDefinition="enum('PARTIAL', 'COMPLETE' )")
    private String registration;
    
    @Transient
    private boolean accountNonExpired = true;
    @Transient
    private boolean accountNonLocked = true;
    @Transient
    private boolean credentialsNonExpired = true;
    @Transient
    private boolean enabled = true;
    
    @Column(name="last_login")
    private Date last_login;
    
    @Column(name="registered_date")
    private Date registered_date;
    
    public int getUser_id() {
        return user_id;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public Date getLast_login() {
    	return last_login;
    }
    public Date getRegistered_date(){
    	return registered_date;
    }
    
    

    public Set<UserRole> getRoles() {
		return roles;
	}
	public void setRoles(Set<UserRole> roles) {
		this.roles = roles;
	}
	
	
	
	@Override
	@Transient
    public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<Permission> authorities = new HashSet<Permission>();
		
        for (UserRole role : roles) {
            for (UserRight right : role.getUserRight()) {
                Permission permission = new Permission("ROLE_" + right.getName());
                authorities.add(permission);
            }
        }
        return authorities;
    }
    
    public void setAuthorities(List<UserRole> authorities) {
        
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }
     
    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }
 
    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }
     
    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }
 
    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }
     
    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }
 
    
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
 
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setUser_id(Integer id) {
        this.user_id = id;
    }
    
    public void setLast_login(Date date) {
        this.last_login=date;
    }
    
    public void setRegistered_date(Date date){
    	this.registered_date = date;
    }

    
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRegistration() {
		return registration;
	}
	public void setRegistration(String registration) {
		this.registration = registration;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getMobile() {
		return mobile;
	}
	public void setMobile(long mobile) {
		this.mobile = mobile;
	}
	
    
    
    
}