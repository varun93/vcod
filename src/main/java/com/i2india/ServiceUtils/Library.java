package com.i2india.ServiceUtils;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.CharSet;
import org.apache.log4j.Logger;

import com.i2india.Controller.TestController;
import com.i2india.ErrorUtils.CustomException;
import com.i2india.ErrorUtils.ErrorConstants;
import com.i2india.SecurityUtils.MCrypt;



public final class Library {

	static Logger log = Logger.getLogger(TestController.class.getName());
	
	public static String getParamterInString(ArrayList<String> parms,String secretKey) throws Exception
	{
		int i;
		String encParam = new String();
		try {
			
			for(i=0;i<parms.size()-1;i++)
				encParam = encParam + parms.get(i) + "|";
			encParam = encParam + parms.get(i);
		} catch (Exception e) {
			return null;
		}
		
		log.debug("Parameters before encrypting = "+encParam);

		MCrypt mCrypt = new MCrypt(secretKey);
	    encParam = MCrypt.bytesToHex(mCrypt.encrypt(encParam));
		
		log.debug("Parameters after encrypting = "+encParam);
		return encParam;
	}
	
}
