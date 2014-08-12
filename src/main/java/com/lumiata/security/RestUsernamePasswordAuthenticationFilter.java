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

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author rakesh
 *
 */

public class RestUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	@Override
	 protected void successfulAuthentication(HttpServletRequest request,
	         HttpServletResponse response, Authentication authResult)
	         throws IOException, ServletException {
	     super.successfulAuthentication(request, response, authResult);
	     
	     System.out.println("==successful login==");
	 }

	 @Override
	 protected void unsuccessfulAuthentication(HttpServletRequest request,
	         HttpServletResponse response, AuthenticationException failed)
	         throws IOException, ServletException {
	     super.unsuccessfulAuthentication(request, response, failed);
	     request.getParameter(getUsernameParameter());
	 
	     System.out.println("==failed login==");
	 }


}
