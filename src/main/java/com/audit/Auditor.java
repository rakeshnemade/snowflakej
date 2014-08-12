package com.audit;

/**
 *<p>
 *This program is confidential and proprietary to Lumiata Inc. and
 *may not be reproduced, published, or disclosed to others without
 *company authorization. Copyright 2013-2016 by Lumiata Inc.
 */

/**
 * @author rakesh
 *
 */
import java.net.URI;
import java.net.URISyntaxException;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Provider
public class Auditor implements ContainerRequestFilter {

	private static final Logger logger = Logger.getLogger(Auditor.class);

	public ContainerRequest filter(ContainerRequest request) {
		String baseURI = request.getBaseUri().toString(); //http://localhost:9090/snowflake/rest/
		String requestURI = request.getRequestUri().toString(); //http://localhost:9090/snowflake/rest/sf
		String checkVersion = requestURI.substring(baseURI.length(), requestURI.length());
		try {
			if(!checkVersion.startsWith("V") && !checkVersion.startsWith("api")) {
				requestURI = requestURI.substring(0, baseURI.length()) + "V1/" + requestURI.substring(baseURI.length(), requestURI.length());
				request.setUris(new URI(baseURI), new URI(requestURI));
			}
		} catch (URISyntaxException e) {
			logger.error(e.getMessage());
		}
		
		//Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
		//System.out.println("User Name :: " +auth.getPrincipal().toString());
		
		
		String method =  request.getMethod();
        logger.debug(method + " : " + requestURI);
	    return request;
	}

}
