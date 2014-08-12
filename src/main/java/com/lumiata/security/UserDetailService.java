/**
 *<p>
 *This program is confidential and proprietary to Lumiata Inc. and
 *may not be reproduced, published, or disclosed to others without
 *company authorization. Copyright 2013-2016 by Lumiata Inc.
 */

package com.lumiata.security;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.lumiata.handler.SFHandler;
import com.lumiata.util.SFUtil;

/**
 * @author rakesh
 *
 */

public class UserDetailService implements UserDetailsService{

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = null;
    	Map<String, Object> userMap = SFHandler.instance().getUser(username, "user");
    	if(userMap == null || userMap.isEmpty()) {
    		throw new UsernameNotFoundException("Bad Username");
    	} else {
    		String password = userMap.get("password").toString();
    		List<GrantedAuthority> grantedAuths = SFUtil.getAuthorities(userMap);
    		user = new User(username, password, grantedAuths); 
    	}
		
		return user;
	}
}
