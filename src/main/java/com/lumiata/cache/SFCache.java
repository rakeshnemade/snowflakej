/**
 *<p>
 *This program is confidential and proprietary to Lumiata Inc. and
 *may not be reproduced, published, or disclosed to others without
 *company authorization. Copyright 2013-2016 by Lumiata Inc.
 */

package com.lumiata.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * @author rakesh
 *
 */

public class SFCache {
	
	private static SFCache instance;
	private static Map<String, Map<String, Object>> collectionMap;
	
	private SFCache(){};
	
	public static SFCache getInstance() {
		if (instance == null) {
			instance = new SFCache();
		}
		return instance;
	}

	/**
	 * @return the collectionMap
	 */
	public Map<String, Map<String, Object>> getCollectionMap() {
		if(collectionMap == null) {
			collectionMap = new HashMap<String, Map<String, Object>>();
		}
		return collectionMap;
	}
	
	/**
	 * 
	 * @param datatype
	 * @return
	 */
	public Map<String, Object> getCollectionDef(String datatype) {
		if(collectionMap == null || !collectionMap.containsKey(datatype)) {
			return null;
		}
		return collectionMap.get(datatype);
	}
	

}
