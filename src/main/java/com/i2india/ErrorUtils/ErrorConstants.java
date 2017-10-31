package com.i2india.ErrorUtils;

public class ErrorConstants {


	//integer version of error codes
	public static final int PRODUCT_NOT_FOUND = 2001;
	public static final int SERVICE_NOT_AVAILABLE = 2003;
	public static final int INTERNAL_ERROR = 2005;
	public static final int UKNOWN_ERROR  = 2007;
	public static final int FORM_FIELD_ERROR = 2009;
	public static final int TRANSACTION_ERROR = 2011;
	public static final int BAD_CREDENTIALS = 2013;
	public static final int FORBIDDEN_ENTRY_POINT = 2015;
	public static final int UNAUTHORIZED_ACCESS = 2016;
	
	//String version of error codes 
	public static final String STR_PRODUCT_NOT_FOUND = "product_not_found";
	public static final String STR_SERVICE_NOT_AVAILABLE = "service_not_available";
	public static final String STR_INTERNAL_ERROR = "internal_errror";
	public static final String STR_UKNOWN_ERROR  = "unknown_error";
	public static final String STR_FORM_FIELD_ERROR = "field_error";
	public static final String STR_TRANSACTION_ERROR = "transaction_error";
	public static final String STR_FORBIDDEN_ENTRY_POINT ="entry_forbidden_unthorised";
	public static final String STR_UNAUTHORIZED_ACCESS = "Unauthorized ! You dont have permission to perform this activity!";
	
	//Errors on gateway
	public static final int MERCHANT_INVALID_ORDER_ID = 1001;
	public static final String STR_MERCHANT_INVALID_ORDER_ID = "Merchant sent order id which was already proceesd !";
	public static final int INVALID_MERCHANT_ID = 1002;
	public static final String STR_INVALID_MERCHANT_ID =  "Invalid Merchant ID !";
	public static final int INVALID_REQUEST_PARAMETERS = 1003;
	public static final String STR_INVALID_REQUEST_PARAMETERS =  "Invalid Request Parameters!";	
	
	//Common Errors
	
	public static final int INVALID_DATE= 3201;
	public static final String STR_INVALID_DATE = "Invalid transaction date !";
	public static final int INVALID_DATA = 3202;
	public static final String STR_INVALID_DATA = "Data invalid in the request !";
	
	
	//Errors for cancel orders
	public static final int TRANSACTION_NOT_FOUND = 3001;
	public static final int TRANSACTION_ALREADY_PROCESSED = 3002;
	public static final int TRANSACTION_REFUND_INITIATED_ALREADY = 3003;
	public static final int TRANSACTION_REFUND_INVALID_AMOUNT = 3004;
	public static final int TRANSACTION_NO_CONTRACT_FOUND = 3005;
	public static final int SETTLEMENT_NOT_FOUND = 3006;
	public static final int REFUND_NOT_FOUND = 3007;
	
	
	//String version of error codes 	
	public static final String STR_TRANSACTION_NOT_FOUND = "Transaction not found !";
	public static final String STR_TRANSACTION_ALREADY_PROCESSED = "Transaction already processed !";
	public static final String STR_TRANSACTION_REFUND_INITIATED_ALREADY = "Refund Intiated already !";
	public static final String STR_TRANSACTION_REFUND_INVALID_AMOUNT = "Invalid refund amount !";	
	public static final String STR_TRANSACTION_NO_CONTRACT_FOUND = "Error occured during processing the payment !";
	public static final String STR_SETTLEMENT_NOT_FOUND = "Settlement Not found !";
	public static final String STR_REFUND_NOT_FOUND = "Refund Not found !";;
	
	//Add Merchant Error Codes
	public static final int USER_INVALID_USERNAME = 3101;
	public static final int USER_INVALID_PASSWORD = 3102;
	public static final int USER_ACCOUNT_ALREADY_ACTIVATED = 3103;
	public static final int USER_INVALID_MERCHANT_DETAILS = 3104;
	public static final int USER_ALREADY_EXSIST = 3104;
	public static final int USER_ACTIVATE_ERROR=3105;
	public static final int USER_INVALID_ID=3106;
	public static final int USER_INVALID_OLD_PASSWORD = 3107;
	
	//String version
	public static final String STR_USER_INVALID_USERNAME = "Invalid username !";
	public static final String STR_USER_INVALID_PASSWORD = "Invalid password !";
	public static final String STR_USER_ACCOUNT_ALREADY_ACTIVATED = "Your account already activated ! ";
	public static final String STR_USER_INVALID_MERCHANT_DETAILS = "Invalid merchant details !";
	public static final String STR_USER_ALREADY_EXSIST = "User already exsist!";
	public static final String STR_USER_ACTIVATE_ERROR = "Invalid Activation token ! ";
	public static final String STR_USER_INVALID_ID="Invalid Used Id";
	public static final String STR_USER_INVALID_OLD_PASSWORD = "Invalid old password !";
	//REFUND error codes
	
	public static final int REFUND_INVALID_TRANSACTION_ID = 3301;
	
	//String version
	public static final String STR_REFUND_INVALID_TRANSACTION_ID = "Invalid transaction id !";

	//Merchant Dashboard Errors
	public static final int MERCHANT_ONE_BANK_ACCOUNT = 3400;
	public static final String STR_MERCHANT_ONE_BANK_ACCOUNT = "Merchant can have only one bank account at this point of time !";
	
	
	//Validation Errors
	public static final int STRING_EMPTY =4000;
	public static final int STRING_NULL =4001;
	public static final int RANGE_ERROR =4002;
	public static final int DATE_NULL = 4003;
	public static final int NUM_ZERO = 4004;
	
	public static final String STR_STRING_EMPTY = "Paramters cannot be empty !";
	public static final String STR_STRING_NULL = "Some required parameters in the request are missing !";
	public static final String STR_RANGE_ERROR = "Parameter(s) are not within valid range";
	public static final String STR_DATE_NULL = "Some required Date parameters are not present in the request !";
	public static final String STR_NUM_ZERO = "Some required paramters are missing !";
	
	
	//Dispute Resoultion
	public static final int INVALID_TRANSACTION_ID = 5001;
	public static final int INVALID_DISPUTE_AMOUNT = 5002;
	
	public static final  String STR_INVALID_TRANSACTION_ID = "Invalid transaction id !";
	public static final String STR_INVALID_DISPUTE_AMOUNT = "Invalid dispute amount";
}
