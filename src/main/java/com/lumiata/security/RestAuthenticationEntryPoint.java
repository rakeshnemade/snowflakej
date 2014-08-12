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

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * @author rakesh
 *
 */

public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, 
			AuthenticationException auth) throws IOException, ServletException {
		System.out.println("in rest entry point");
		String username = request.getParameter("j_username");
		System.out.println(username);
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Unauthorised"); 
	}

}
