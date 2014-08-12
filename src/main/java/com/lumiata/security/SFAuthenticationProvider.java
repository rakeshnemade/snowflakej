/**
 *<p>
 *This program is confidential and proprietary to Lumiata Inc. and
 *may not be reproduced, published, or disclosed to others without
 *company authorization. Copyright 2013-2016 by Lumiata Inc.
 */

package com.lumiata.security;

import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.lumiata.handler.SFHandler;
import com.lumiata.util.CryptoLibrary;
import com.lumiata.util.SFUtil;

/**
 * @author rakesh
 *
 */

public class SFAuthenticationProvider implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
	    String username = authentication.getName();
	    String password = (String) authentication.getCredentials();
	    
	    if(username != null && password != null) {
	    	Map<String, Object> user = SFHandler.instance().getUser(username, "user");
	    	if(user == null || user.isEmpty()) {
	    		throw new UsernameNotFoundException("Invalid Username");
	    	} else {
	    		String userpass = CryptoLibrary.getInstance().decrypt(user.get("password").toString());
	    		if(!password.equals(userpass)) {
	    			throw new BadCredentialsException("Invalid Password");
	    		}
	    		
	    		List<GrantedAuthority> grantedAuths = SFUtil.getAuthorities(user);
	            Authentication auth = new UsernamePasswordAuthenticationToken(username, password, grantedAuths);
	            
	            System.out.println("Auth :: "+ auth.getPrincipal());
	            return auth;
	    	}
	    }
		return null;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
