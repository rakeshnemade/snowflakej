/**
 *<p>
 *This program is confidential and proprietary to Lumiata Inc. and
 *may not be reproduced, published, or disclosed to others without
 *company authorization. Copyright 2013-2016 by Lumiata Inc.
 */

package com.lumiata.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AbstractAccessDecisionManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;

import com.lumiata.handler.SFHandler;
import com.lumiata.util.SFConstants;

/**
 * @author rakesh
 *
 */

public class CustomAccessDecisionManager extends AbstractAccessDecisionManager {
	
	@SuppressWarnings("rawtypes")
	protected CustomAccessDecisionManager(List<AccessDecisionVoter> decisionVoters) {
		super(decisionVoters);
	}
	
	@Override
	public void decide(Authentication authentication, Object filter, Collection<ConfigAttribute> roles)
			throws AccessDeniedException, InsufficientAuthenticationException {
		
		if ((filter == null) || !this.supports(filter.getClass())) {
			throw new IllegalArgumentException("Object must be a FilterInvocation");
		}
		if(authentication.getName() != null && "superuser".equals(authentication.getName())) {
			return;
		}
		
		String url = ((FilterInvocation) filter).getRequestUrl();
		System.out.println("URL :: " + url);
		//String contexto = ((FilterInvocation) filter).getRequest().getContextPath();
		
		int deny = 0;
		
		/*for (AccessDecisionVoter voter : getDecisionVoters()) {
			int result = voter.vote(authentication, filter, roles);
			if (logger.isDebugEnabled()) {
				logger.debug("Voter: " + voter + ", returned: " + result);
			}

			switch (result) {
			case AccessDecisionVoter.ACCESS_GRANTED:
				return;
			case AccessDecisionVoter.ACCESS_DENIED:
				deny++;
				break;
			default:
				break;
			}
		}*/
		
		int result = validatePermission(authentication, url);
		switch (result) {
		case AccessDecisionVoter.ACCESS_GRANTED:
			return;
		case AccessDecisionVoter.ACCESS_DENIED:
			deny++;
			break;
		default:
			break;
		}

		if (deny > 0) {
			throw new AccessDeniedException(messages.getMessage("AbstractAccessDecisionManager.accessDenied", 
					"Access is denied"));
		}

		// To get this far, every AccessDecisionVoter abstained
		checkAllowIfAllAbstainDecisions();
	}
	
	@SuppressWarnings("unchecked")
	private int validatePermission(Authentication authentication, String url) {
		int permission = -1;
		
		List<GrantedAuthority> grantedAuths = (List<GrantedAuthority>)authentication.getAuthorities();
		
		List<String> roleNames = new ArrayList<String>();
		for(GrantedAuthority grantedAuth : grantedAuths) {
			roleNames.add(grantedAuth.getAuthority());
			System.out.println("ROLES :: " + grantedAuth.getAuthority());
		}
		
		List<Map<String, Object>> listRoles = SFHandler.instance().getRolesByRoleNames(roleNames, "role");
		List<String> urlLabels = new ArrayList<String>();
		for(Map<String, Object> role : listRoles) {
			List<String> labels = (List<String>)role.get(SFConstants.PERMITTEDURLS);
			if(labels != null) {
				urlLabels.addAll(labels);
			}
		}
		
		List<Map<String, Object>> apiLinks = SFHandler.instance().getApiLinksByUrlLabel(urlLabels, "allapplinks");
		for(Map<String, Object> apiLink : apiLinks) {
			String permittedurl = apiLink.get(SFConstants.URL).toString();
			if(permittedurl != null) {
				if(url.contains(permittedurl)) {
					permission = 1;
					break;
				}
			}
		}
		if(!authentication.getPrincipal().toString().equals("anonymousUser") && permission < 0) {
			if(url.contains("index") || url.contains("home") || url.contains("all") || url.contains("search") || url.contains("save")){
				permission = 1;
			}
		}
		System.out.println("Permission :: " + permission);
		return permission;
	}

}
