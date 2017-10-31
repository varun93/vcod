package com.i2india.SecurityUtils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;


public class KeyEncryption {

	
	private final static String salt="zxcsalt";	
	static Logger log = Logger.getLogger(KeyEncryption.class.getName());

	public static String getEncryptedToken(String plainText, String encryptionKey) throws Exception {

		byte[] key = encryptionKey.getBytes("UTF-8");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		final String encryptedString = Base64.encodeBase64String(cipher.doFinal(plainText.getBytes()));
		return encryptedString;

	}

	public static String getDecryptedToken(String ciphr, String encryptionKey) throws Exception{

		byte[] key = encryptionKey.getBytes("UTF-8");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
		final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		final String decryptedString = new String(cipher.doFinal(Base64.decodeBase64(ciphr)));
		return decryptedString;
	
   }
	
	@SuppressWarnings("null")
	public static String hashPassword(String password) {
		String hashword = null;
		if(password!=null || !password.equals(""))
			password = password + salt;
		
		try {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(password.getBytes());
		BigInteger hash = new BigInteger(1, md5.digest());
		hashword = hash.toString(16);
		} catch (NoSuchAlgorithmException nsae) {
		// ignore
		}
		return hashword;
	}

   public static void main(String args[]) throws Exception
	{
		log.debug(KeyEncryption.getEncryptedToken("varun123", "abcdefghijklmnop"));
		//log.debug(KeyEncryption.getDecryptedToken("k3PIMDb6Ho4Pq+83Nny9sgt63RiggW1atNZ1Rhez5so=","abcdefghijklmnop"));

	}




}
