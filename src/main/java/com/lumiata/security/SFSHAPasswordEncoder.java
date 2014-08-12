/**
 *<p>
 *This program is confidential and proprietary to Lumiata Inc. and
 *may not be reproduced, published, or disclosed to others without
 *company authorization. Copyright 2013-2016 by Lumiata Inc.
 */

package com.lumiata.security;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.lumiata.util.CryptoLibrary;



/**
 * @author rakesh
 *
 */

public class SFSHAPasswordEncoder implements PasswordEncoder {

	private static CryptoLibrary cryptoLib = CryptoLibrary.getInstance();
	
	@Override
	public String encode(CharSequence rawPassword) {
		return cryptoLib.encrypt(rawPassword.toString());
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		String decrypPassword = cryptoLib.decrypt(encodedPassword);
		String rawPass = rawPassword.toString();
		return rawPass.equals(decrypPassword);
	}

}
