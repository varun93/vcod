package com.i2india.SecurityUtils;

import java.util.ArrayList;
import java.util.List;
import com.i2india.Service.UserService;


//yet to add more methods once sure about the type of data we are dealing  with

public class MerchantAuthentication {


	private static final String  DELIMITER = "\\|";
	private UserService userService;



	public MerchantAuthentication(UserService service)
	{
		
		this.userService = service;
	}



	//tested and validated
	private String getMerchantKey(Integer merchantID)
	{

		
		return userService.getMerchantKey(merchantID);

	}

	//tested and validated
	public boolean validateMerchant(Integer merchantID,String token) 
	{

		String key = this.getMerchantKey(merchantID);

		Integer merchantid=null;

		try {
			MCrypt mCrypt = new MCrypt(key);
			
			merchantid = Integer.parseInt(new String(mCrypt.decrypt(token)).split(DELIMITER)[0]);

		} catch (Exception e) {
			return false;
		}

		return (merchantID.equals(merchantid));


	}


	//or return a HashMap with proper mapping key value pair
	//@Tested and Validated 
	public List<String> getMerchantParameters(String token,Integer merchantID)
	{

		ArrayList<String> list = new ArrayList<String>();

		try {
			String[] merchantParams = KeyEncryption.getDecryptedToken(token, userService.getMerchantKey(merchantID)).split(DELIMITER);
			for(int i=0;i<merchantParams.length;i++)
				list.add(merchantParams[i]);
		} catch (Exception e) {
			list = null;
		}

		return list;
	}



//say flipkart for example
	public  String getMerchantId(String token)
	{

		String merchantID;

		String merchantKey = getMerchantId(token);
		try
		{
			merchantID = (KeyEncryption.getDecryptedToken(token, merchantKey).split(DELIMITER)[0]);
		}
		catch (Exception e)
		{
			merchantID = null;
		}

		return merchantID;
	}


	//for sbi for example

	public static String getEncryptedToken(ArrayList<String> parameters,String encryptionKey) throws Exception
	{
		StringBuilder builder = new StringBuilder();
		for(int i=0;i<parameters.size();i++)
		{

			if(i!=(parameters.size()-1))
			{
				builder.append(parameters.get(i)+"|");
			}
			else
				builder.append(parameters.get(i));
		}


		return KeyEncryption.getEncryptedToken(builder.toString(), encryptionKey);

	}

	/*
	//Test code
	public static void main(String args[]) throws Exception
	{

		MerchantAuthentication authentication = new MerchantAuthentication();
		ArrayList<String> list = new ArrayList<String>();
		list.add("12345");
		list.add("somerandomdata");
		String token=null;
		try {
			token = authentication.getEncryptedToken(list, "abcdefghijklmnop");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.debug(token);



		String username = KeyEncryption.getDecryptedToken(token, "abcdefghijklmnop");

		//log.debug(Integer.parseInt(username.split("\\|")[0]));

		

		//log.debug(authentication.getMerchantParameters(token, "varun123"));

		//log.debug(authentication.isMerchantValid("varun123",token));

		
	}
	*/



}



