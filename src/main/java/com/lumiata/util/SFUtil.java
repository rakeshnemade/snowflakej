/**
 *<p>
 *This program is confidential and proprietary to Lumiata Inc. and
 *may not be reproduced, published, or disclosed to others without
 *company authorization. Copyright 2013-2016 by Lumiata Inc.
 */

package com.lumiata.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lumiata.handler.SFHandler;

/**
 * @author rakesh
 *
 */

public class SFUtil {
	
	private static final Logger logger = Logger.getLogger(SFUtil.class);
	
	public static Map<String, Object> convertStringToMap(String string) {
		Map<String, Object> mapObject = null;
		
		try {
			mapObject = new ObjectMapper().readValue(string, new TypeReference<HashMap<String, Object>>() {});
		} catch (JsonParseException e) {
			logger.error(e.getMessage());
		} catch (JsonMappingException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return mapObject;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> getSortedPropertyDef(Map<String, Object> colldef) {
		List<Map<String, Object>> propertydefinitions = (List<Map<String, Object>>)colldef.get(SFConstants.PROPERTYDEFINITIONS);
		Collections.sort (propertydefinitions, new HashMapComparator()) ;
		return propertydefinitions;
	}
	
	@SuppressWarnings("unchecked")
	public static List<GrantedAuthority> getAuthorities(Map<String, Object> user) {
		List<GrantedAuthority> grantedAuths = new ArrayList<>();
		List<String> groups = (List<String>)user.get("groups");
		List<Map<String, Object>> listGroups = SFHandler.instance().getGroupsByGroupNames(groups, "group");
		Set<String> roles = new TreeSet<String>();
		for(Map<String, Object> group : listGroups) {
			List<String> groupRoles = (List<String>)group.get("roles");
			roles.addAll(groupRoles);
		}
		for(String roleName : roles) {
			grantedAuths.add(new SimpleGrantedAuthority(roleName));
		}
		return grantedAuths;
	}
	
	public static String getBUCKET(Map<String, Object> COLLDEF, String COLLECTION) {
        String largedataset = (String)COLLDEF.get("largedataset");

        if (null != largedataset && "true".equals(largedataset) && null != COLLECTION) {
            return COLLECTION;
        } else {
        	String defaultBUCKET = SFConstants.DEFAULT_BUCKET;
            if (null != COLLECTION && 
            		("".equals(COLLECTION.trim()) || defaultBUCKET.equals(COLLECTION) || "default".equals(COLLECTION))) {
            	return defaultBUCKET;
            }
            String gBUCKET = (String)COLLDEF.get("**GROUPBUCKET**");
            return gBUCKET;
        }

    }
	
}

@SuppressWarnings("rawtypes")
class HashMapComparator implements Comparator {
    public int compare (Object object1, Object object2) {
        //if ( flag == false ) {
    	Integer obj1Value = 999;
    	Integer obj2Value = 999;
    	String obj1 = (String) ((HashMap)object1).get("displayorder");
    	if(obj1 != null && !obj1.trim().isEmpty()) {
    		obj1Value = Integer.parseInt(obj1);
    	}
    	String obj2 = (String) ((HashMap)object2).get("displayorder");
    	if(obj2 != null && !obj2.trim().isEmpty()) {
    		obj2Value = Integer.parseInt(obj2);
    	}
    	return obj1Value.compareTo ( obj2Value ) ;
    }
}

