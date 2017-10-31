package com.i2india.Controller;


import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.i2india.Domain.Bank;
import com.i2india.Domain.Consumer;
import com.i2india.Domain.Merchant;
import com.i2india.Domain.User;
import com.i2india.ErrorUtils.CustomException;
import com.i2india.ErrorUtils.CustomSecurityException;
import com.i2india.ErrorUtils.ErrorConstants;
import com.i2india.ErrorUtils.StandardResponse;
import com.i2india.SecurityUtils.AuthToken;
import com.i2india.ServiceUtils.Message;
import com.i2india.security.CustomAuthenticationProvider;

@Controller
public class HelloController {

	@Autowired
	private CustomAuthenticationProvider customerLoginAuthenticationProvider;


	static Logger log = Logger.getLogger(HelloController.class.getName());

	public void setAuthenticationProvider(
			CustomAuthenticationProvider authenticationProvider) {
		this.customerLoginAuthenticationProvider = authenticationProvider;
	}

	@RequestMapping(value = "/merchant/gateway", method = RequestMethod.GET)
	public String merchantPage() {

		log.debug("in merchant method");

		return "merchant";

	}

	@RequestMapping(value = "/unauthorized", method = RequestMethod.GET)
	public String forbiddenPoint() {
		throw new CustomSecurityException(ErrorConstants.UNAUTHORIZED_ACCESS,ErrorConstants.STR_UNAUTHORIZED_ACCESS);
	}
	
	@RequestMapping(value = "/failure", method = RequestMethod.GET)
	public String gatewayFailure(ModelMap map) {
		map.addAttribute("message", "Unauthorized access !");
		
		return "Error";
		
	
	}
	
	@RequestMapping(value = "/bank", method = RequestMethod.GET)
	public String bank(String userId) {

		

		return "home";

	}

	@RequestMapping(value = "/account/login", method = RequestMethod.POST)
	public @ResponseBody StandardResponse obtainToken(@RequestParam("username")String username,@RequestParam("password") String password) {


		log.debug("username is "+username);
		log.debug("password is"+ password);
		Authentication authentication = null;
		AuthToken auth = new AuthToken();
		// use the AuthenticationManager to help with authentication
		Authentication token= new UsernamePasswordAuthenticationToken(username, password);
		try
		{
			authentication = customerLoginAuthenticationProvider.authenticate(token);
			SecurityContextHolder.getContext().setAuthentication(null);

		}
		catch(AuthenticationException exception)
		{

			exception.printStackTrace();
			throw new CustomException(ErrorConstants.BAD_CREDENTIALS, exception.getMessage());
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
			throw new CustomException(ErrorConstants.UKNOWN_ERROR,"source of error not identified");
		}
		User user = (User) authentication.getPrincipal();

		
		Message message = new Message();

		
		HashMap<String, String> data= new HashMap<String,String>();
		data.put("authToken", auth.getAuthToken(user.getUser_id()));
		data.put("name", user.getName());

		if(user instanceof Merchant)
		data.put("type", "merchant");
		else if(user instanceof Consumer)
		data.put("type", "buyer");
		else if(user instanceof Bank)
		data.put("type", "bank");
		else
		throw new CustomException(ErrorConstants.UKNOWN_ERROR,"source of error not identified");
		message.setAdditionalData(data);
		
		return new StandardResponse("success",message);
	}

	
	



}