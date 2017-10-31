package com.i2india.security;

import java.util.Calendar;
import java.util.Collection;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;

import com.i2india.Domain.Merchant;
import com.i2india.ServiceUtils.AccountStatus;

public class CustomVoter implements AccessDecisionVoter<Object> {

	private String rolePrefix = "ROLE_";

	private static final int SUNRISE_HOUR = 9;
	private static final int SUNSET_HOUR = 21; 

	public String getRolePrefix() {
		return rolePrefix;
	}

	public void setRolePrefix(String rolePrefix) {
		this.rolePrefix = rolePrefix;
	}

	@Override
	public boolean supports(ConfigAttribute attribute) {
		if ((attribute.getAttribute() != null)
				&& attribute.getAttribute().startsWith(getRolePrefix())) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean supports(Class arg0) {
		return true;
	}

	@Override
	public int vote(Authentication authentication, Object object, Collection attributes) {
/*			Merchant merchant = (Merchant) authentication.getPrincipal();
			if(merchant.getRegistration().equals(AccountStatus.COMPLETE))
				return ACCESS_GRANTED;
			else
				return ACCESS_DENIED;*/
		return ACCESS_GRANTED;
	}

	private boolean isDaylight(int currentHour) {
		return currentHour>=SUNRISE_HOUR && currentHour<=SUNSET_HOUR;
	}

}