/**
 *<p>
 *This program is confidential and proprietary to Lumiata Inc. and
 *may not be reproduced, published, or disclosed to others without
 *company authorization. Copyright 2013-2016 by Lumiata Inc.
 */

package com.lumiata.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * @author rakesh
 *
 */

public class RestAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authnetication) throws IOException, ServletException {
		UserDetails userDetails = (UserDetails) authnetication.getPrincipal();
		
		//request.getSession().
		System.out.println(userDetails);
	}

}
