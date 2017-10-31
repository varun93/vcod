package com.i2india.Controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.i2india.Domain.Bank;
import com.i2india.Domain.Consumer;
import com.i2india.Domain.Dispute;
import com.i2india.Domain.Merchant;
import com.i2india.Domain.MerchantAccount;
import com.i2india.Domain.MerchantContract;
import com.i2india.Domain.Settlement;
import com.i2india.Domain.Transactions;
import com.i2india.Domain.User;
import com.i2india.ErrorUtils.CustomApplicationException;
import com.i2india.ErrorUtils.CustomDataException;
import com.i2india.ErrorUtils.CustomException;
import com.i2india.ErrorUtils.CustomValidationException;
import com.i2india.ErrorUtils.ErrorConstants;
import com.i2india.ErrorUtils.StandardResponse;
import com.i2india.SecurityUtils.MCrypt;
import com.i2india.Service.OrderService;
import com.i2india.Service.UserService;
import com.i2india.ServiceUtils.DisputeStates;
import com.i2india.ServiceUtils.Library;
import com.i2india.ServiceUtils.Message;
import com.i2india.ServiceUtils.OrderState;
import com.i2india.ServiceUtils.SuccessConstants;
import com.i2india.ServiceUtils.ValidationHelper;

/*
 * disable CORS filter while deploying for production
 */

@Controller
public class TestController{

	@Autowired
	private UserService userService;

	@Autowired
	private OrderService orderService;

	static Logger log = Logger.getLogger(TestController.class.getName());

	@RequestMapping(value={"customer/hello"},method = RequestMethod.GET)	
	public @ResponseBody ArrayList<User> printHello() {
		return userService.getAllUsers();
	}
	
	/*
		End point for activation token generation
	 */
	@RequestMapping(value="account/activationtoken/{userId}",method = RequestMethod.GET)	
	public @ResponseBody String generateToken(@PathVariable int userId) {
		return userService.generateActivateTokenUser(userId);
	}
	
	/*
		End point for activating a user.
		The Roles to a user are set at this step. assigning a Role to a user . . .
	 */
	@RequestMapping(value="account/activationtoken/activate/{link}",method = RequestMethod.GET)	
	public @ResponseBody StandardResponse activateUser(@PathVariable String link) {
		   log.debug("Link = "+link);
		   userService.activateUser(link);
		   Message message = new Message();
		   message.setMessage(SuccessConstants.USER_ACTIVATED);
		   StandardResponse standardResponse = new StandardResponse("success",message);
		   log.debug("Success message : "+message.getMessage());
		   return standardResponse;
	}
	
	   /*
	   The URL which Merchant will hit. ie:flipkart This populates order This intiates a transaction in our servers.
	   This returns a JSP page on response. The JSP page has javascript code for redirection to bank website
	   This follows decryption approach ie: The paramters are received in encrypted format like SBI's
	   using 128bit AES encryption.
	   The parameter merchant id is used decrypt the encrypted data.
	   This module has a specific security filter.
	   */
	
	@PreAuthorize("hasRole('ROLE_WRITE')")
	@RequestMapping(value = "gateway/checkin", method = RequestMethod.POST)
	public String addOrder(ModelMap map,@RequestParam(value="encParm", required=true) String encParm,@RequestParam(value="merchantid", required=true) int merchantId) 
	{
	   try
	   {
		   log.debug("merchantId = "+merchantId);
		   Merchant merchant = userService.getMerchant(merchantId);
		   //Decrypt
		   MCrypt mCrypt = new MCrypt(merchant.getSecret_key());
		   encParm = new String(mCrypt.decrypt(encParm));
		   log.debug("decrypted paramters "+ encParm);
		   
		   String parm[] = encParm.split("\\|"); // Pipe is the separator
		   log.debug("Number of paramters after split = "+parm.length);
		   //After splitting if required parameters are less stop processing
		   //Also Detect special characters also if found stop processing
		   if(parm.length!=14)
		   {  
			   throw new CustomValidationException(ErrorConstants.INVALID_REQUEST_PARAMETERS,ErrorConstants.STR_INVALID_REQUEST_PARAMETERS);
		   }	   
		   String firstName = parm[5];
		   String consumerUsername = parm[6];	
		   String phoneNumber = parm[7];
		   //Check for consumer account, If consumer doesn't exsist create an account and email him the activation code.
		   Consumer consumer = userService.getConsumerByName(consumerUsername,firstName,phoneNumber); 
		   // We have to know to which bank this request has to forwarded. Depending on the request we can decide,
		   Bank bank = userService.getBank(56); 
		   
		   log.debug("Bank Id = "+bank.getUser_id());
		   
		   try
		   {
			   ArrayList<String> outputParamList = orderService.checkIn(parm, consumer, merchant,bank);
			   String encryptTrans =Library.getParamterInString(outputParamList, bank.getSecret_key()); //Encrypt with the Bank's secret key

			   //This map is used to pass information to JSP page.
			   map.addAttribute("EncryptTrans",encryptTrans);
			   map.addAttribute("merchIdVal",bank.getBank_merchant_id());	
			   log.debug("Bank.Merchant_Id= "+bank.getBank_merchant_id());
			   //A cancel link needed
			   
			   
		   }catch(NumberFormatException ex)
		   {
			   map.addAttribute("message", "Invalid data");
			   log.debug(ex);
			   log.error(ex);
			   return "Error";
		   }
		   
		   return "checkoutbank";
	
	}catch(CustomException e)
	{
        log.debug(e);
        log.error(e);
		map.addAttribute("message",e.getErrorMessage());
	    return "Error";
	}catch (Exception ex)
	{
	    log.debug(ex);
	    log.error(ex);
		ex.printStackTrace();
		map.addAttribute("message", "Processing failed");
		return "Error";
	}
	   
	}
	   /*
	   The URL bank will hit on successful/failed transaction 
	   It will be mostly a bank specific URL
	   One call back URL is enough.
	   This is not a REST API it generates a JSP response with js code which will redirect to callback url sent by merchant
	   on request
	   We might use a Bank spefic URL in case of dealing with multiple banks.
	    */	  
	  @RequestMapping(value ={ "gateway/response/success", "gateway/response/fail"}, method = RequestMethod.POST)
		public String success(ModelMap map,@RequestParam(value="encData", required=false) String encData) 
		{
		  try
		  {
			  //Decrypt the data using key the secret key given by bank
			   Bank bank = userService.getBank(56);
			   
			   MCrypt mCrypt = new MCrypt(bank.getSecret_key());
			   try {
				encData = new String(mCrypt.decrypt(encData));
			   } catch (Exception e1) {
			    log.debug(e1);
			    log.error(e1);
				throw new CustomApplicationException(ErrorConstants.INTERNAL_ERROR,ErrorConstants.STR_INTERNAL_ERROR);
			   }
		       log.debug(bank.getUser_id());
			   log.debug("Data after decrtption : "+encData);
			   
			   String parm[] = encData.split("\\|");
		       log.debug("Number of parameters after split "+ parm.length);
			   //After splitting if required parameters are less stop processing
			   //Also Detect special characters also if found stop processing
			   if(parm.length!=8)
				   throw new CustomValidationException(ErrorConstants.INVALID_REQUEST_PARAMETERS,ErrorConstants.STR_INVALID_REQUEST_PARAMETERS);
			   
			   //Process the response
			   Transactions transaction = orderService.getTransactions(Integer.parseInt(parm[0]));
			   //Return a encrypted response to merchant
			   String encryptResponse =  orderService.checkOut(bank,transaction,parm);
			   
		       log.debug("Updated transaction's id "+ transaction.getTransaction_id());
		       log.debug("encrypted Response : "+encryptResponse);

		       //Map is used add data to JSP page.
			   map.addAttribute("EncryptResponse",encryptResponse);
			   
			   if(transaction.getStatus().equals(OrderState.SUCCESS))
			   {
				   map.addAttribute("url",transaction.getMerchant_success_url());
			       log.debug("Redirection URL: "+transaction.getMerchant_success_url());	   
			   }
			   else
			   {
				   map.addAttribute("url",transaction.getMerchant_fail_url());
			       log.debug("Redirection URL: "+transaction.getMerchant_fail_url());
			   }

			   
			   return "response";
		  }catch(NumberFormatException ex)
		  {
			    log.debug(ex);
			    log.error(ex);
			   map.addAttribute("message", "Proceesing failed");
			   return "Error";
		  }catch(CustomException ex)
		  {
			    log.debug(ex);
			    log.error(ex);
			  map.addAttribute("message", ex.getErrorMessage());
			  return "Error";
		  }
		  catch(Exception ex)
		  {
			    log.debug(ex);
			    log.error(ex);
			  ex.printStackTrace();
			  map.addAttribute("message", "Intzzernal Server Error");
			  return "Error";
		  }
		}
	  
	  
	
	   
	   
	   //These is a dummy endpoints for canceling transactions
	   @RequestMapping(value = "merchant/transactions/cancel/{transactionId}", method = RequestMethod.GET)
		public @ResponseBody StandardResponse cancelOrder(@PathVariable int transactionId,@RequestParam(value="merchantid", required=true) int merchant_id,@RequestParam(value="amount", required=true) int amount,@RequestParam(value="reason", required=true) String reason)
	   {
 		  
 		   orderService.cancelOrder(transactionId,merchant_id,amount,reason);
 		   Message message = new Message();
		   message.setMessage(SuccessConstants.ORDER_REFUND);
		   StandardResponse standardResponse = new StandardResponse("success",message);
		   return standardResponse;
	   }
	   
	   
	   //This is a dummy end point for completing refund request. Assumed that there is response for refund request
	   @RequestMapping(value = "/refundorder", method = RequestMethod.GET)
		public @ResponseBody String completeCancelOrder(@RequestParam(value="transactionid", required=true) int transactionid)
	   {
		   if (orderService.completeCancelOrder(transactionid))
		   {
			    return "Transaction processed ! ";
		   }else
			   
			   return "Transaction failed ! ";
	   }
	   
	    //Endpoint for getting merchant application and account info for Angular JS app to process
	    @PreAuthorize("hasRole('ROLE_READ')")
  		@RequestMapping(value = "/merchant/info", method = RequestMethod.GET)
		public @ResponseBody StandardResponse merchantAdvancedInfo(HttpServletRequest request)
	    {
  			Authentication  authentication = null;
  			Merchant merchant = null;
  			try{
  				authentication = SecurityContextHolder.getContext().getAuthentication();
  				merchant = (Merchant) authentication.getPrincipal();
  			}catch(NullPointerException ex){
			    log.debug(ex);
			    log.error(ex);
  				throw new CustomApplicationException(ErrorConstants.INTERNAL_ERROR,  ErrorConstants.STR_INTERNAL_ERROR);
  			}
  			
			Message message = new Message();
			HashMap<String, Object> data= new HashMap<String,Object>();
			
			data.put("merchant", merchant);
			data.put("merchantaccounts", userService.getAllMerchantAccounts(merchant));
			log.debug(merchant.getUser_id());
			message.setAdditionalData(data);
	 		
  			return new StandardResponse("success",message);
	    }
	    	//End point for creating a new merchant
	   		@RequestMapping(value = {"/account/merchant/add"}, method = RequestMethod.POST)
	 		public @ResponseBody StandardResponse addMerchant(@ModelAttribute(value="Merchant") Merchant merchant)
	 	    {
		   	
				//Validation
				ValidationHelper.validateEmails(merchant.getUsername());
				ValidationHelper.validatePassword(merchant.getPassword());
				ValidationHelper.validateForStringEmptyAndNull(merchant.getName());
		
	 		   Message message = new Message();
	 		   message.setMessage(SuccessConstants.USER_ADD_MERCHANT);
	 		   try
	 		   {
	 			   log.debug("user merchant "+merchant.getUsername());
			   	   userService.addMerchant(merchant);
	 			   
	 		   }catch(ConstraintViolationException ex)
	 		   {
				    log.debug(ex);
				    log.error(ex);
	 		      throw new CustomValidationException(ErrorConstants.USER_ALREADY_EXSIST,  ErrorConstants.STR_USER_ALREADY_EXSIST);
	 		   }
			   StandardResponse standardResponse = new StandardResponse(SuccessConstants.SUCCESS,message);
			   return standardResponse;
	 	   }
	   		
	   	   //This end point should be accessed by only person's with admin permission	
	   	   //A dummy end point for adding a contract for merchant
           @PreAuthorize("hasRole('ROLE_WRITE')") //This as to be admin_wrie
	 	   @RequestMapping(value = "/merchant/contract/add", method = RequestMethod.GET)
	 	   public @ResponseBody StandardResponse addMerchantContract()
	 	   {
	  			Authentication  authentication = null;
	  			Merchant merchant =  null;
	  			try{
	  				authentication = SecurityContextHolder.getContext().getAuthentication();
	  				merchant = (Merchant) authentication.getPrincipal();
	  			}catch(NullPointerException ex){
				    log.debug(ex);
				    log.error(ex);
	  				throw new CustomApplicationException(ErrorConstants.INTERNAL_ERROR,  ErrorConstants.STR_INTERNAL_ERROR);
	  			}
	  			log.debug("mechant id : "+merchant.getUser_id());
	 			MerchantContract merchatContract = new MerchantContract();
	 			merchatContract.setContract_date(new Date());
	 			merchatContract.setPaymode("CC");
	 			merchatContract.setRate(0.6);
	 			merchatContract.setMerchant(merchant);
	 		   
	 		   try
	 		   {
	 		   //Dummy merchant contract
	 			   userService.addMerchantContract(merchatContract);
	 		   }catch(Exception ex)
	 		   {
				    log.debug(ex);
				    log.error(ex);
	 			  throw new CustomApplicationException(ErrorConstants.INTERNAL_ERROR, ErrorConstants.STR_INTERNAL_ERROR);
	 		   }
	 		   
	 		   Message message = new Message();
	 		   message.setMessage(SuccessConstants.MERCHANT_CONTRACT_ADDED);
			   StandardResponse standardResponse = new StandardResponse(SuccessConstants.SUCCESS,message);
			   return standardResponse;
	 	   }
	 	  
           //End point for updating password
	 	  @PreAuthorize("hasRole('ROLE_UPDATE')")
	 	   @RequestMapping(value = "/merchant/settings/password/update", method = RequestMethod.POST)
	 	   public @ResponseBody StandardResponse merchantChangePassword( 
	 			  @RequestParam(value="oldpassword",required=true, defaultValue="") String oldPassword,
	 			  @RequestParam(value="newpassword",required=true, defaultValue="") String newPassword)
	 	   {
	 		   
	  			Authentication  authentication = null;
	  			Merchant merchant = null;
	  			try{
	  				authentication = SecurityContextHolder.getContext().getAuthentication();
	  				merchant = (Merchant) authentication.getPrincipal();
	  			}catch(NullPointerException ex){
				    log.debug(ex);
				    log.error(ex);
	  				throw new CustomApplicationException(ErrorConstants.INTERNAL_ERROR,  ErrorConstants.STR_INTERNAL_ERROR);
	  			}
	  			log.debug("mechant id : "+merchant.getUser_id());
	  			userService.changePassword(merchant, oldPassword, newPassword);
		 		ValidationHelper.validatePassword(newPassword);
	  			
	 		   Message message = new Message();
	 		   message.setMessage(SuccessConstants.USER_PASSWORD_CHANGED);
			   StandardResponse standardResponse = new StandardResponse(SuccessConstants.SUCCESS,message);
			   return standardResponse;
	 	   }
	 	   
	 	  
	 	  //Updating merchant application details on complete a DB flag will be set to Registration completed.
	 	  
		   @PreAuthorize("hasRole('ROLE_UPDATE')")
	 	   @RequestMapping(value = "/merchant/info/update", method = RequestMethod.POST)
	 		public @ResponseBody StandardResponse updateMerchant(@ModelAttribute(value="Merchant") Merchant merchant)
	 	   {
	  			Authentication  authentication = null;
	  			Merchant merchantUser = null;
	  			try{
	  				authentication = SecurityContextHolder.getContext().getAuthentication();
	  				merchantUser = (Merchant) authentication.getPrincipal();
	  				
	  			}catch(NullPointerException ex){
				    log.debug(ex);
				    log.error(ex);
	  				throw new CustomApplicationException(ErrorConstants.INTERNAL_ERROR,  ErrorConstants.STR_INTERNAL_ERROR);
	  			}
	  			log.debug("mechant id : "+merchant.getUser_id());
	  			merchant.setUser_id(merchantUser.getUser_id());
		   	  //Validate
		   	  ValidationHelper.validateForStringEmptyAndNull(merchant.getBusiness_category(),merchant.getBusiness_type(),merchant.getOfficial_email(),merchant.getRegistered_address(),merchant.getBusiness_category(),merchant.getOperating_address(),merchant.getOperating_city(),merchant.getOperating_state(),merchant.getRegistered_address(),merchant.getRegistered_city(),merchant.getRegistered_country(),merchant.getRegistered_state(),merchant.getPancard_name(),merchant.getPancard_no());
	 		  ValidationHelper.validateForDateNull(merchant.getPancard_est_date());
	 		  ValidationHelper.validateEmails(merchant.getOfficial_email());
	 		  ValidationHelper.validateForZero(merchant.getLandline(),merchant.getOfficial_mobile(),merchant.getRegistered_pincode(),merchant.getOperating_pincode());
	 		   try
	 		   {
			   	   userService.updateMerchantWizard(merchant);
	 			   
	 		   }catch(ConstraintViolationException ex)
	 		   { 
	 			   log.debug(ex);
	 			   log.error(ex);
	 		      throw new CustomValidationException(ErrorConstants.USER_INVALID_MERCHANT_DETAILS, ErrorConstants.STR_USER_INVALID_MERCHANT_DETAILS);
	 		   }
	 		   
	 		   Message message = new Message();
	 		   message.setMessage(SuccessConstants.UPDATE_MERCHANT_WIZARD);
			   StandardResponse standardResponse = new StandardResponse(SuccessConstants.SUCCESS,message);
			   return standardResponse;
	 	   }
	   
		   //end point for adding bank account for merchant.
		   //Only one bank account per merchant is allowed in the program . 
		   @PreAuthorize("hasRole('ROLE_WRITE')")	 	  
	 	   @RequestMapping(value = "/merchant/bankacoount/add", method = RequestMethod.POST)
	 		public @ResponseBody StandardResponse addMerchantAccount(@ModelAttribute(value="MerchantAccount") MerchantAccount merchantAccount)
	 	   {
	  			Authentication  authentication = null;
	  			Merchant merchant = null;
	  			try{
	  				authentication = SecurityContextHolder.getContext().getAuthentication();
	  				merchant = (Merchant) authentication.getPrincipal();
	  			}catch(NullPointerException ex){
				    log.debug(ex);
				    log.error(ex);
	  				throw new CustomApplicationException(ErrorConstants.INTERNAL_ERROR,  ErrorConstants.STR_INTERNAL_ERROR);
	  			}
		 		//Validate 
		 		ValidationHelper.validateForZero(merchantAccount.getAccount_number());
		 		ValidationHelper.validateForStringEmptyAndNull(merchantAccount.getBank_address(),merchantAccount.getBank_name(),merchantAccount.getBranch(),merchantAccount.getIfsc_code());
	 		    ValidationHelper.validateForRange(0, 1, merchantAccount.getIsPrimary());
	 		   log.debug("mechant id : "+merchant.getUser_id());
	 		   userService.addMerchantAccount(merchantAccount, merchant);

	 		   Message message = new Message();
	 		   message.setMessage(SuccessConstants.ADD_MERCHANT_ACCOUNT);
			   StandardResponse standardResponse = new StandardResponse(SuccessConstants.SUCCESS,message);
			   return standardResponse;
	 	   }
	 	   
		   //endpoint for updating merchant bank account detailes
		   @PreAuthorize("hasRole('ROLE_UPDATE')&&@MerchantAuthorizationService.hasMerchantAccountUpdatePermission(#merchantAccount)")
	 	   @RequestMapping(value = "/merchant/bankaccount/update", method = RequestMethod.POST)
	 		public @ResponseBody StandardResponse updateMerchantAccount(@ModelAttribute(value="MerchantAccount") MerchantAccount merchantAccount)
	 	   {
		 		//Validate 
		 		ValidationHelper.validateForZero(merchantAccount.getAccount_number());
		 		ValidationHelper.validateForStringEmptyAndNull(merchantAccount.getBank_address(),merchantAccount.getBank_name(),merchantAccount.getBranch(),merchantAccount.getIfsc_code());
	 		    merchantAccount.setIsPrimary(1); // Precaution
	 		   log.debug("mechant account id : "+merchantAccount.getAccount_id());
	 		   userService.updateMerchantAccount(merchantAccount);

	 		   Message message = new Message();
	 		   message.setMessage(SuccessConstants.MERCHANT_UPDATE_ACCOUNT);
			   StandardResponse standardResponse = new StandardResponse(SuccessConstants.SUCCESS,message);
			   return standardResponse;
	 	   }	   
	 	   
	 	   
	 	   //This a end point to show list of transactions filter
		   //The below endpoint needs to implement data accesss authorization at query level for optimzation reasons.
	 	   @PreAuthorize("hasRole('ROLE_READ')")
	 	   @RequestMapping(value = "/merchant/transactions/list", method ={RequestMethod.GET})
	 		public @ResponseBody StandardResponse getMerchantTransactions(@RequestParam(value="status", required=false, defaultValue="") String status,
	 				@RequestParam(value="fromdate",required=false, defaultValue="") @DateTimeFormat(pattern="dd/MM/yyyy") String fromDateParm,
	 				@RequestParam(value="todate",required=false, defaultValue="") @DateTimeFormat(pattern="dd/MM/yyyy") String toDateParm,
	 				@RequestParam(value="ondate",required=false, defaultValue="") @DateTimeFormat(pattern="dd/MM/yyyy") String onDateParm,
	 				@RequestParam(value="start", required=false, defaultValue="0") int start,
	 				@RequestParam(value="size", required=false, defaultValue="10") int size,
	 	   			@RequestParam(value="transactionid", required=false, defaultValue="-1") int transactionId,
	 	   			@RequestParam(value="startamount", required=false, defaultValue="-1") float startAmount,
	 	   			@RequestParam(value="endamount", required=false, defaultValue="-1") float endAmount,
	 	   		    @RequestParam(value="merchantorderid", required=false, defaultValue="") String merchantOrderId,
	 	   		    @RequestParam(value="customeremail", required=false, defaultValue="") String customerEmail,
	 	   		    @RequestParam(value="sort", required=false, defaultValue="") String sort,
	 	   		    @RequestParam(value="order", required=false, defaultValue="") String order
	 	   		    ){
	 		   
	 		   
	 		   //Parameter filtering
	  			Authentication  authentication = null;
	  			Merchant merchant = null;
	  			try{
	  				authentication = SecurityContextHolder.getContext().getAuthentication();
	  				merchant = (Merchant) authentication.getPrincipal();
	  			}catch(NullPointerException ex){
				    log.debug(ex);
				    log.error(ex);
	  				throw new CustomApplicationException(ErrorConstants.INTERNAL_ERROR,  ErrorConstants.STR_INTERNAL_ERROR);
	  			}
	 		   
	  			
	  		   log.debug("mechant id : "+merchant.getUser_id());
	 		   @SuppressWarnings("rawtypes")
	 		   Map Response=null;
	 		   //The parameter filtering is done at query level
	 		   Response = orderService.getAllTransactionsForMerchant(merchant, status,start,size,fromDateParm,toDateParm,onDateParm,transactionId,startAmount,endAmount,merchantOrderId,customerEmail,sort,order);
			   StandardResponse standardResponse = new StandardResponse("success",Response);
			   return standardResponse;
	 	   }
	 	   
	 	   //List of refunds and disputes filter
	 	   //The below endpoint needs to implement data accesss authorization at query level for optimzation reasons.
		   @PreAuthorize("hasRole('ROLE_READ')")
	 	   @RequestMapping(value = {"/merchant/disputes/list","/merchant/refunds/list",}, method ={RequestMethod.GET})
	 		public @ResponseBody StandardResponse getMerchantDisputes(@RequestParam(value="status", required=false, defaultValue="") String status,
	 				@RequestParam(value="fromdate",required=false, defaultValue="") @DateTimeFormat(pattern="dd/MM/yyyy") String fromDateParm,
	 				@RequestParam(value="todate",required=false, defaultValue="") @DateTimeFormat(pattern="dd/MM/yyyy") String toDateParm,
	 				@RequestParam(value="ondate",required=false, defaultValue="") @DateTimeFormat(pattern="dd/MM/yyyy") String onDateParm,
	 				@RequestParam(value="start", required=false, defaultValue="0") int start,
	 				@RequestParam(value="size", required=false, defaultValue="10") int size,
	 	   			@RequestParam(value="disputeid", required=false, defaultValue="-1") int disputeId,
	 	   			@RequestParam(value="startamount", required=false, defaultValue="-1") double startAmount,
	 	   			@RequestParam(value="endamount", required=false, defaultValue="-1") double endAmount,
	 	   		    @RequestParam(value="merchantorderid", required=false, defaultValue="") String merchantOrderId,
	 	   		 	@RequestParam(value="customeremail", required=false, defaultValue="") String customerEmail,
	 	   			@RequestParam(value="transactionid", required=false, defaultValue="-1") int transactionId,
	 	   			@RequestParam(value="type", required=false, defaultValue="") String type,
	 	   		    @RequestParam(value="sort", required=false, defaultValue="") String sort,
	 	   		    @RequestParam(value="order", required=false, defaultValue="") String order,
	 	   		    HttpServletRequest request
	 	   		 ){
	 		   //Parameter filtering
			   	if(request.getRequestURI().contains("/refunds/list"))
			   		type=DisputeStates.MERCHANT_REFUND;
	  			Authentication  authentication = null;
	  			Merchant merchant = null;
	  			try{
	  				authentication = SecurityContextHolder.getContext().getAuthentication();
	  				merchant = (Merchant) authentication.getPrincipal();
	  			}catch(NullPointerException ex){
				    log.debug(ex);
				    log.error(ex);
	  				throw new CustomApplicationException(ErrorConstants.INTERNAL_ERROR,  ErrorConstants.STR_INTERNAL_ERROR);
	  			}
	 		   
	  		   log.debug("mechant id : "+merchant.getUser_id());
	 		   @SuppressWarnings("rawtypes")
	 		   Map data=null;
		 	   data = orderService.getAllDisputesForMerchant(merchant, status,start,size,fromDateParm,toDateParm,onDateParm,disputeId,startAmount,endAmount,merchantOrderId,customerEmail,transactionId,type,sort,order);
			   StandardResponse standardResponse = new StandardResponse("success",data);
			   return standardResponse;
	 	   }
	 	   
		   //List of settlements filter
		   //The below endpoint needs to implement data accesss authorization at query level for optimzation reasons.
		   @PreAuthorize("hasRole('ROLE_READ')")
	 	   @RequestMapping(value = "/merchant/settlements/list", method ={RequestMethod.GET})
	 		public @ResponseBody StandardResponse getMerchantSettlements(@RequestParam(value="status", required=false, defaultValue="") String status,
	 				@RequestParam(value="fromdate",required=false, defaultValue="") @DateTimeFormat(pattern="dd/MM/yyyy") String fromDateParm,
	 				@RequestParam(value="todate",required=false, defaultValue="") @DateTimeFormat(pattern="dd/MM/yyyy") String toDateParm,
	 				@RequestParam(value="ondate",required=false, defaultValue="") @DateTimeFormat(pattern="dd/MM/yyyy") String onDateParm,
	 				@RequestParam(value="start", required=false, defaultValue="0") int start,
	 				@RequestParam(value="size", required=false, defaultValue="10") int size,
	 	   			@RequestParam(value="settlementid", required=false, defaultValue="-1") int settlementId,
	 	   			@RequestParam(value="startamount", required=false, defaultValue="-1") double startAmount,
	 	   			@RequestParam(value="endamount", required=false, defaultValue="-1") double endAmount,
	 	   		    @RequestParam(value="sort", required=false, defaultValue="") String sort,
	 	   		    @RequestParam(value="order", required=false, defaultValue="") String order
	 	   		 ){
	 		   //Parameter filtering
	  			Authentication  authentication = null;
	  			Merchant merchant = null;
	  			try{
	  				authentication = SecurityContextHolder.getContext().getAuthentication();
	  				merchant = (Merchant) authentication.getPrincipal();
	  				
	  			}catch(NullPointerException ex){
				    log.debug(ex);
				    log.error(ex);
	  				throw new CustomApplicationException(ErrorConstants.INTERNAL_ERROR,  ErrorConstants.STR_INTERNAL_ERROR);
	  			}
	 		   
	 		   
	  			
	  			log.debug("mechant id : "+merchant.getUser_id());
	 		   @SuppressWarnings("rawtypes")
	 		   Map data=null;
		 	   data = orderService.getAllSettlementsForMerchant(merchant, status, start, size, fromDateParm, toDateParm, onDateParm, settlementId, startAmount, endAmount,sort,order);
			   StandardResponse standardResponse = new StandardResponse("success",data);
			   return standardResponse;
	 	   }
	 	   
		   /*
		    * Transactions,settlements and disputes by IDs
		    * 
		    */
	 	    @PreAuthorize("@MerchantAuthorizationService.hasTransactionAccessPermission(#transactionId)&&hasRole('ROLE_READ')")
	 		@RequestMapping(value="/merchant/transactions/{transactionId}",method = RequestMethod.GET)	
	 		public @ResponseBody StandardResponse getTransactionById(@PathVariable int transactionId) {
	 		   Map<String, Object> data=new HashMap<String, Object>();
	 		   Transactions transaction = orderService.getTransactionById(transactionId);
	 		   if(transaction==null)
	 			   throw new CustomDataException(ErrorConstants.TRANSACTION_NOT_FOUND,ErrorConstants.STR_TRANSACTION_NOT_FOUND);
	 		   data.put("Transaction",transaction);
	 		   data.put("customer_email", transaction.getConsumer().getUsername());
	 		   StandardResponse standardResponse = new StandardResponse("success",data);
			   return standardResponse;
	 		}
	 		
	 		
	 	    @PreAuthorize("@MerchantAuthorizationService.hasSettlementAccessPermission(#settlementId)&&hasRole('ROLE_READ')")
	 		@RequestMapping(value="/merchant/settlements/{settlementId}",method = RequestMethod.GET)	
	 		public @ResponseBody StandardResponse getSettlementById(@PathVariable int settlementId) {
	 		   Map<String, Settlement> data=new HashMap<String, Settlement>();
	 		   Settlement settlement = orderService.getSettlementById(settlementId);
	 		   if(settlement==null)
	 			   throw new CustomDataException(ErrorConstants.SETTLEMENT_NOT_FOUND,ErrorConstants.STR_SETTLEMENT_NOT_FOUND);
	 		   data.put("Settlement",settlement);
	 		   StandardResponse standardResponse = new StandardResponse("success",data);
			   return standardResponse;
	 		}
	 		
	 	    @PreAuthorize("@MerchantAuthorizationService.hasDisputeAccessPermission(#disputeId)&&hasRole('ROLE_READ')")
	 		@RequestMapping(value="/merchant/disputes/{disputeId}",method = RequestMethod.GET)	
	 		public @ResponseBody StandardResponse getTransaction(@PathVariable int disputeId) {
	 		   Map<String, Dispute> data=new HashMap<String, Dispute>();
	 		   Dispute dispute = orderService.getDisputeById(disputeId);
	 		   if(dispute==null)
	 			   throw new CustomDataException(ErrorConstants.REFUND_NOT_FOUND,ErrorConstants.STR_REFUND_NOT_FOUND);
	 		   data.put("Dispute",dispute);
	 		   StandardResponse standardResponse = new StandardResponse("success",data);
			   return standardResponse;
	 		}
	 	   
	 	  /*
	 	   * Customer portal transactions and disputes
	 	   */
	 	  
	 	  /*
	 	   * Customer portal transactions 
	 	   */
	 	   @PreAuthorize("hasRole('ROLE_READ')")	 	    
	 	   @RequestMapping(value = "/customer/transactions/list", method ={RequestMethod.GET})
	 		public @ResponseBody StandardResponse getCustomerTransactions(@RequestParam(value="status", required=false, defaultValue="") String status,
	 				@RequestParam(value="fromdate",required=false, defaultValue="") @DateTimeFormat(pattern="dd/MM/yyyy") String fromDateParm,
	 				@RequestParam(value="todate",required=false, defaultValue="") @DateTimeFormat(pattern="dd/MM/yyyy") String toDateParm,
	 				@RequestParam(value="ondate",required=false, defaultValue="") @DateTimeFormat(pattern="dd/MM/yyyy") String onDateParm,
	 				@RequestParam(value="start", required=false, defaultValue="0") int start,
	 				@RequestParam(value="size", required=false, defaultValue="10") int size,
	 	   			@RequestParam(value="transactionid", required=false, defaultValue="-1") int transactionId,
	 	   			@RequestParam(value="startamount", required=false, defaultValue="-1") float startAmount,
	 	   			@RequestParam(value="endamount", required=false, defaultValue="-1") float endAmount,
	 	   		    @RequestParam(value="merchantorderid", required=false, defaultValue="") String merchantOrderId,
	 	   		    @RequestParam(value="organizationname", required=false, defaultValue="") String organizationName,
	 	   		    @RequestParam(value="sort", required=false, defaultValue="") String sort,
	 	   		    @RequestParam(value="order", required=false, defaultValue="") String order
	 	   		    ){
	 		   
	 		   
	 		   //Parameter filtering
	  			Authentication  authentication = null;
	  			Consumer consumer = null;
	  			try{
	  				authentication = SecurityContextHolder.getContext().getAuthentication();
	  				consumer = (Consumer) authentication.getPrincipal();
	  			}catch(NullPointerException ex){
				    log.debug(ex);
				    log.error(ex);
	  				throw new CustomApplicationException(ErrorConstants.INTERNAL_ERROR,  ErrorConstants.STR_INTERNAL_ERROR);
	  			}
	 		   
	  			
	  		   log.debug("consumer id : "+consumer.getUser_id());
	 		   @SuppressWarnings("rawtypes")
	 		   Map Response=null;
	 		   //The parameter filtering is done at query level
	 		   Response = orderService.getAllTransactionsForConsumer(consumer, status,start,size,fromDateParm,toDateParm,onDateParm,transactionId,startAmount,endAmount,merchantOrderId,organizationName,sort,order);
			   StandardResponse standardResponse = new StandardResponse("success",Response);
			   return standardResponse;
	 	   }
	 	   
	 	    @PreAuthorize("hasRole('ROLE_READ')")
	 	    @RequestMapping(value = {"/customer/disputes/list","/customer/refunds/list",}, method ={RequestMethod.GET})
	 		public @ResponseBody StandardResponse getConsumerDisputes(@RequestParam(value="status", required=false, defaultValue="") String status,
	 				@RequestParam(value="fromdate",required=false, defaultValue="") @DateTimeFormat(pattern="dd/MM/yyyy") String fromDateParm,
	 				@RequestParam(value="todate",required=false, defaultValue="") @DateTimeFormat(pattern="dd/MM/yyyy") String toDateParm,
	 				@RequestParam(value="ondate",required=false, defaultValue="") @DateTimeFormat(pattern="dd/MM/yyyy") String onDateParm,
	 				@RequestParam(value="start", required=false, defaultValue="0") int start,
	 				@RequestParam(value="size", required=false, defaultValue="10") int size,
	 	   			@RequestParam(value="disputeid", required=false, defaultValue="-1") int disputeId,
	 	   			@RequestParam(value="startamount", required=false, defaultValue="-1") double startAmount,
	 	   			@RequestParam(value="endamount", required=false, defaultValue="-1") double endAmount,
	 	   		    @RequestParam(value="merchantorderid", required=false, defaultValue="") String merchantOrderId,
	 	   		 	@RequestParam(value="organizationname", required=false, defaultValue="") String organizationName,
	 	   			@RequestParam(value="transactionid", required=false, defaultValue="-1") int transactionId,
	 	   			@RequestParam(value="type", required=false, defaultValue="") String type,
	 	   		    @RequestParam(value="sort", required=false, defaultValue="") String sort,
	 	   		    @RequestParam(value="order", required=false, defaultValue="") String order,
	 	   		    HttpServletRequest request
	 	   		 ){
	 		   //Parameter filtering
			   	if(request.getRequestURI().contains("/refunds/list"))
			   		type=DisputeStates.MERCHANT_REFUND;
	  			Authentication  authentication = null;
	  			Consumer consumer = null;
	  			try{
	  				authentication = SecurityContextHolder.getContext().getAuthentication();
	  				consumer = (Consumer) authentication.getPrincipal();
	  			}catch(NullPointerException ex){
				    log.debug(ex);
				    log.error(ex);
	  				throw new CustomApplicationException(ErrorConstants.INTERNAL_ERROR,  ErrorConstants.STR_INTERNAL_ERROR);
	  			}
	 		   
	  		   log.debug("consumer id : "+consumer.getUser_id());
	 		   @SuppressWarnings("rawtypes")
	 		   Map data=null;
		 	   data = orderService.getAllDisputesForConsumer(consumer, status,start,size,fromDateParm,toDateParm,onDateParm,disputeId,startAmount,endAmount,merchantOrderId,organizationName,transactionId,type,sort,order);
			   StandardResponse standardResponse = new StandardResponse("success",data);
			   return standardResponse;
	 	   }
	 	   
	 	   @PreAuthorize("hasRole('ROLE_WRITE')&&@CustomerAuthorizationService.hasTransactionAccessPermission(#transactionId)")
	  	   @RequestMapping(value = "/customer/disputes/add", method ={RequestMethod.POST})
	 	   public @ResponseBody StandardResponse addConsumerDispute(@ModelAttribute(value="Dispute") Dispute dispute,@RequestParam(value="transactionid", required=false, defaultValue="") int transactionId)
	  	   {
	 		   ValidationHelper.validateForStringEmptyAndNull(dispute.getTitle(),dispute.getDescription());
	  		   orderService.addConsumerDispute(dispute, transactionId);
	 		   Message message = new Message();
	 		   message.setMessage(SuccessConstants.CONSUMER_ADD_DISPUTE);
			   StandardResponse standardResponse = new StandardResponse("success",message);
			   return standardResponse;
	  	   }
	 	  
	 	   
}