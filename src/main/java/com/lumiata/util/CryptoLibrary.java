/**
 *<p>
 *This program is confidential and proprietary to Lumiata Inc. and
 *may not be reproduced, published, or disclosed to others without
 *company authorization. Copyright 2013-2016 by Lumiata Inc.
 */

package com.lumiata.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author rakesh
 *
 */
public class CryptoLibrary {

	private Cipher encryptCipher;
	private Cipher decryptCipher;
	private static CryptoLibrary crptoLib = null;
	protected static final Log logger = LogFactory.getLog(CryptoLibrary.class);
	
	private CryptoLibrary() throws SecurityException {

		char[] pass = "lumiata".toCharArray();

		byte[] salt = {
			(byte) 0xa3, (byte) 0x21, (byte) 0x24, (byte) 0x2c,
			(byte) 0xf2, (byte) 0xd2, (byte) 0x3e, (byte) 0x19 
		};

		int iterations = 3;
		init(pass, salt, iterations);
	}
	
	public static CryptoLibrary getInstance() {
		if(crptoLib == null)
			crptoLib = new CryptoLibrary();
		return crptoLib;
	}

	public void init(char[] pass, byte[] salt, int iterations) throws SecurityException	{

		try {
			PBEParameterSpec ps = new javax.crypto.spec.PBEParameterSpec(salt, iterations);
			SecretKeyFactory kf = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
			SecretKey k = kf.generateSecret(new javax.crypto.spec.PBEKeySpec(pass));
			encryptCipher = Cipher.getInstance(k.getAlgorithm());
			encryptCipher.init(Cipher.ENCRYPT_MODE, k, ps);
			decryptCipher = Cipher.getInstance(k.getAlgorithm());
			decryptCipher.init(Cipher.DECRYPT_MODE, k, ps);
		}catch (Exception e){
			throw new SecurityException("Could not initialize CryptoLibrary: " + e.getMessage());
		}
	}

	/**
	 * 
	 * convenience method for encrypting a string.
	 * @param str
	 * Description of the Parameter
	 * @return String the encrypted string.
	 * @exception SecurityException
	 * Description of the Exception
	 */
	public synchronized String encrypt(String str) throws SecurityException {
		try {
			byte[] utf8 = str.getBytes("UTF8");
			byte[] enc = encryptCipher.doFinal(utf8);
			return Base64.encodeBase64URLSafeString(enc);
		} catch (Exception e){
			throw new SecurityException("Could not encrypt: " + e.getMessage());
		}
	}

	/**
	 * convenience method for encrypting a string.
	 * @param str
	 * Description of the Parameter
	 * @return String the encrypted string.
	 * @exception SecurityException
	 * Description of the Exception
	 */

	public synchronized String decrypt(String str) throws SecurityException {
		try {
			byte[] dec = Base64.decodeBase64(str);
			byte[] utf8 = decryptCipher.doFinal(dec);
			return new String(utf8, "UTF8");
		}catch (Exception e){
			throw new SecurityException("Could not decrypt: " + e.getMessage());
		}
	}
	public synchronized String encryptSHA(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
	
		MessageDigest messageDigest=null;
		String hash=null;
		messageDigest=MessageDigest.getInstance("SHA");
		messageDigest.update(password.getBytes("UTF-8"));
		byte [] bytes =messageDigest.digest();
		hash=(new Base64().encodeToString(bytes));			
		return hash;
	}
	public static void main(String[] args) {
		try {
			CryptoLibrary cl = new CryptoLibrary();
			String user = "superuser";
			String pass = "superuser";
			String password="superuser";
			String euser = cl.encrypt(user);
			String epass = cl.encrypt(pass);
			String duser = cl.decrypt(euser);
			String dpass = cl.decrypt(epass);
			String passMD5=cl.encryptSHA(password);
			System.out.println("User: " + user + " --> " + euser + " --> " + duser);
			System.out.println("Pass: " + pass + " --> " + epass + " --> " + dpass);
			System.out.println(passMD5);
		} catch (Exception e){
			logger.error(e);
		}
	}
}

