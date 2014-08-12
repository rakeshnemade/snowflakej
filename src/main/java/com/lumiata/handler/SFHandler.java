/**
 *<p>
 *This program is confidential and proprietary to Lumiata Inc. and
 *may not be reproduced, published, or disclosed to others without
 *company authorization. Copyright 2013-2016 by Lumiata Inc.
 */

package com.lumiata.handler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lumiata.datastore.SFDataStore;
import com.lumiata.search.SFCriteria;
import com.lumiata.search.SFSearch;
import com.lumiata.util.CryptoLibrary;
import com.lumiata.util.SFConstants;
import com.lumiata.util.SFUtil;

/**
 * @author rakesh
 *
 */

public class SFHandler {
	
private static final Logger logger = Logger.getLogger(SFHandler.class);
	
	public static SFHandler instance() {
		return new SFHandler();
	}

	public Map<String, Object> getCollectionDef(String datatypetype) {
		Map<String, Object> result = null;
		SFSearch sfSearch = new SFSearch();
		SFCriteria sfCriteria1 = SFCriteria.instance().setField(SFConstants.DATATYPE).setValue(SFConstants.COLLECTION_DATATYPE);
		sfSearch.getListCriteria().add(sfCriteria1);
		
		SFCriteria sfCriteria2 = SFCriteria.instance().setField(SFConstants.COLLECTION).setValue(SFConstants.DEFAULT_BUCKET);
		sfSearch.getListCriteria().add(sfCriteria2);
		
		SFCriteria sfCriteria3 = SFCriteria.instance().setField(SFConstants.DATATYPETYPE).setValue(datatypetype);
		sfSearch.getListCriteria().add(sfCriteria3);
		
		SFDataStore sfDatastore = new SFDataStore();
		result = sfDatastore.getDataList(sfSearch).get(0);
		
		return result;
	}
	
	public Map<String, Object> getDataById(String Id, String datatypetype) {
		Map<String, Object> objMap = null;
		Map<String, Object> colldef = getCollectionDef(datatypetype);
		
		String bucketName = (String) colldef.get("**GROUPBUCKET**");
		
		if(Id != null && !Id.trim().isEmpty()) {
			SFSearch sfSearch = SFSearch.instance().setBucket(bucketName);
			sfSearch.getIdList().add(Id);
			
			SFDataStore sfDatastore = new SFDataStore();
			objMap = sfDatastore.getDataById(sfSearch);
		}
		return objMap;
	}
	
	
	public List<Map<String, Object>> getDataList(String datatypetype) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		
		Map<String, Object> colldef = getCollectionDef(datatypetype);
		
		String bucketName = (String) colldef.get("**GROUPBUCKET**");
		String collection = (String) colldef.get("name");
		SFSearch sfSearch = SFSearch.instance();
		sfSearch.setBucket(bucketName).setCollection(collection);
		
		SFCriteria sfCriteria1 = SFCriteria.instance().setField(SFConstants.DATATYPE).setValue(datatypetype);
		sfSearch.getListCriteria().add(sfCriteria1);
		
		SFCriteria sfCriteria2 = SFCriteria.instance().setField(SFConstants.COLLECTION).setValue(collection);
		sfSearch.getListCriteria().add(sfCriteria2);
		
		SFDataStore sfDatastore = new SFDataStore();
		result = sfDatastore.getDataList(sfSearch);
		
		return result;
	}
	
	public List<Map<String, Object>> searchData(String keyword, String datatypetype) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		
		Map<String, Object> colldef = getCollectionDef(datatypetype);
		
		String bucketName = (String) colldef.get("**GROUPBUCKET**");
		String collection = (String) colldef.get("name");
		SFSearch sfSearch = SFSearch.instance();
		sfSearch.setBucket(bucketName).setCollection(collection);
		
		SFCriteria sfCriteria1 = SFCriteria.instance().setField(SFConstants.DATATYPE).setValue(datatypetype);
		sfSearch.getListCriteria().add(sfCriteria1);
		
		SFCriteria sfCriteria2 = SFCriteria.instance().setField(SFConstants.COLLECTION).setValue(collection);
		sfSearch.getListCriteria().add(sfCriteria2);
		
		
		if(keyword != null && !keyword.trim().isEmpty()) {
			SFCriteria sfCriteria3 = SFCriteria.instance().setOperation(SFConstants.QUERYSTRING).setValue(keyword);
			sfSearch.getListCriteria().add(sfCriteria3);
		}
		
		SFDataStore sfDatastore = new SFDataStore();
		result = sfDatastore.getDataList(sfSearch);
		return result;
	}
	
	public Map<String, Object> getUser(String username, String datatypetype) {
		List<Map<String, Object>> result = null;
		Map<String, Object> colldef = getCollectionDef(datatypetype);
		String bucketName = (String) colldef.get("**GROUPBUCKET**");
		String collection = (String) colldef.get("name");
		SFSearch sfSearch = SFSearch.instance();
		sfSearch.setBucket(bucketName).setCollection(collection);
		
		SFCriteria sfCriteria1 = SFCriteria.instance().setField(SFConstants.DATATYPE).setValue(datatypetype);
		sfSearch.getListCriteria().add(sfCriteria1);
		
		SFCriteria sfCriteria2 = SFCriteria.instance().setField(SFConstants.COLLECTION).setValue(collection);
		sfSearch.getListCriteria().add(sfCriteria2);
		
		SFCriteria sfCriteria3 = SFCriteria.instance().setField(SFConstants.USERNAME).setValue(username);
		sfSearch.getListCriteria().add(sfCriteria3);
		
		SFDataStore sfDatastore = new SFDataStore();
		result = sfDatastore.getDataList(sfSearch);
		if(!result.isEmpty()) {
			return result.get(0);
		}
		return new HashMap<String, Object>();
	}
	
	public List<Map<String, Object>> getGroupsByGroupNames(List<String> groupnames, String datatypetype) {
		List<Map<String, Object>> result = null;
		Map<String, Object> colldef = getCollectionDef(datatypetype);
		String bucketName = (String) colldef.get("**GROUPBUCKET**");
		String collection = (String) colldef.get("name");
		SFSearch sfSearch = SFSearch.instance();
		sfSearch.setBucket(bucketName).setCollection(collection);
		
		SFCriteria sfCriteria1 = SFCriteria.instance().setField(SFConstants.DATATYPE).setValue(datatypetype);
		sfSearch.getListCriteria().add(sfCriteria1);
		
		SFCriteria sfCriteria2 = SFCriteria.instance().setField(SFConstants.COLLECTION).setValue(collection);
		sfSearch.getListCriteria().add(sfCriteria2);
		
		SFCriteria sfCriteria3 = SFCriteria.instance().setField(SFConstants.GROUPNAME).setValue(groupnames);
		sfSearch.getListCriteria().add(sfCriteria3);
		
		SFDataStore sfDatastore = new SFDataStore();
		result = sfDatastore.getDataList(sfSearch);
		
		return result;
	}
	
	public List<Map<String, Object>> getRolesByRoleNames(List<String> rolenames, String datatypetype) {
		List<Map<String, Object>> result = null;
		Map<String, Object> colldef = getCollectionDef(datatypetype);
		String bucketName = (String) colldef.get("**GROUPBUCKET**");
		String collection = (String) colldef.get("name");
		SFSearch sfSearch = SFSearch.instance();
		sfSearch.setBucket(bucketName).setCollection(collection);
		
		SFCriteria sfCriteria1 = SFCriteria.instance().setField(SFConstants.DATATYPE).setValue(datatypetype);
		sfSearch.getListCriteria().add(sfCriteria1);
		
		SFCriteria sfCriteria2 = SFCriteria.instance().setField(SFConstants.COLLECTION).setValue(collection);
		sfSearch.getListCriteria().add(sfCriteria2);
		
		SFCriteria sfCriteria3 = SFCriteria.instance().setField(SFConstants.ROLENAME).setValue(rolenames);
		sfSearch.getListCriteria().add(sfCriteria3);
		
		SFDataStore sfDatastore = new SFDataStore();
		result = sfDatastore.getDataList(sfSearch);
		
		return result;
	}
	
	public List<Map<String, Object>> getApiLinksByUrlLabel(List<String> urllabels, String datatypetype) {
		List<Map<String, Object>> result = null;
		Map<String, Object> colldef = getCollectionDef(datatypetype);
		String bucketName = (String) colldef.get("**GROUPBUCKET**");
		String collection = (String) colldef.get("name");
		SFSearch sfSearch = SFSearch.instance();
		sfSearch.setBucket(bucketName).setCollection(collection);
		
		SFCriteria sfCriteria1 = SFCriteria.instance().setField(SFConstants.DATATYPE).setValue(datatypetype);
		sfSearch.getListCriteria().add(sfCriteria1);
		
		SFCriteria sfCriteria2 = SFCriteria.instance().setField(SFConstants.COLLECTION).setValue(collection);
		sfSearch.getListCriteria().add(sfCriteria2);
		
		SFCriteria sfCriteria3 = SFCriteria.instance().setField(SFConstants.URLLABEL).setValue(urllabels);
		sfSearch.getListCriteria().add(sfCriteria3);
		
		SFDataStore sfDatastore = new SFDataStore();
		result = sfDatastore.getDataList(sfSearch);
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public String addData(JsonNode jsonNode, String colltype) {
		String _ID = null;
		((ObjectNode) jsonNode).put(SFConstants.ID, generateUniqueId());
		
		Map<String, Object> colldef = getCollectionDef(colltype);
		
		List<Map<String, Object>> propertydefinitions = (List<Map<String, Object>>)colldef.get(SFConstants.PROPERTYDEFINITIONS);
		for(Map<String, Object> propertyDef : propertydefinitions) {
			String propertyName = (String)propertyDef.get("**NESTED**");
			if(!jsonNode.hasNonNull(propertyName)) {
				String defaultValue = propertyDef.get("defaultvalue").toString();
				if(propertyName.equals("**DATATYPE**")) {
					((ObjectNode) jsonNode).put(propertyName, colldef.get("datatypetype").toString());
				} else if(propertyName.equals("collection")) {
					((ObjectNode) jsonNode).put(propertyName, colldef.get("name").toString());
				} else if(propertyName.equals("effectivedate")) {
					((ObjectNode) jsonNode).put(propertyName, getCurrentDate());
				} else if(propertyName.equals("moddate")) {
					((ObjectNode) jsonNode).put(propertyName, String.valueOf(new Date().getTime()));
				} else if(propertyName.equals("rev")) {
					((ObjectNode) jsonNode).put(propertyName, "0");
				} else if(defaultValue != null && !defaultValue.isEmpty()) {
					((ObjectNode) jsonNode).put(propertyName, defaultValue);
				} else {
					((ObjectNode) jsonNode).put(propertyName, "");
				}
			} else {
				if(propertyName.equals("password")) {
					String value = jsonNode.path(propertyName).asText();
					String encVal = CryptoLibrary.getInstance().encrypt(value);
					logger.debug("Password before :: " + value + " After :: " + encVal);
					((ObjectNode) jsonNode).put(propertyName, encVal);
				}
			}
		}
		
		/*((ObjectNode) jsonNode).put(SFConstants.PASSWORDLASTCHANGEDATE, "");
		List<String> passHist = new ArrayList<String>();
		passHist.add(jsonNode.get(SFConstants.PASSWORD).asText());
		ArrayNode array = new ObjectMapper().valueToTree(passHist);
		((ObjectNode) jsonNode).putArray(SFConstants.PASSWORDHISTORY).addAll(array);
		((ObjectNode) jsonNode).put(SFConstants.VERIFICATIONCODE, "");*/
		
		SFDataStore sfDatastore = new SFDataStore();
		String bucketName = (String) colldef.get("**GROUPBUCKET**");
		_ID = sfDatastore.add(jsonNode, bucketName);
		logger.debug("Adding " + colltype + " to " + bucketName + " bucket with id :: " + _ID);
		System.out.println("Adding " + colltype + " to " + bucketName + " bucket with id :: " + _ID);
		return _ID;
	}
	
	@SuppressWarnings("deprecation")
	public String updateData(JsonNode jsonNode, String colltype) {
		String _ID = null;
		
		Map<String, Object> colldef = getCollectionDef(colltype);
		
		String bucketName = (String) colldef.get("**GROUPBUCKET**");
		_ID = jsonNode.path(SFConstants.ID).asText();
		SFSearch sfSearch = SFSearch.instance().setBucket(bucketName);
		sfSearch.getIdList().add(_ID);
		
		SFDataStore sfDatastore = new SFDataStore();
		JsonNode dbNode = sfDatastore.getJsonNodeById(sfSearch);
		
		Iterator<String> iterator = dbNode.fieldNames();
		while(iterator.hasNext()) {
			String fieldName = iterator.next();
			if(!jsonNode.hasNonNull(fieldName)) {
				if(fieldName.equals("rev")) {
					int rev = Integer.parseInt(dbNode.path(SFConstants.REV).asText());
					((ObjectNode) jsonNode).put(SFConstants.REV, String.valueOf(rev+1));
				} else if(fieldName.equals("moddatelong")) {
					((ObjectNode) jsonNode).put(SFConstants.MODDATELONG, getCurrentDate());
				} else {
					((ObjectNode) jsonNode).put(fieldName, dbNode.path(fieldName));
				}
			} else {
				if(fieldName.equals("password")) {
					String value = jsonNode.path(fieldName).asText();
					if(value != null && !value.trim().isEmpty()) {
						String encVal = CryptoLibrary.getInstance().encrypt(value);
						logger.debug("Password before :: " + value + " After :: " + encVal);
						((ObjectNode) jsonNode).put(fieldName, encVal);
					} else {
						((ObjectNode) jsonNode).put(fieldName, dbNode.path(fieldName));
					}
				}
			}
		}
		
		_ID = sfDatastore.update(jsonNode, bucketName);
		
		if(null != _ID) {
			updateChild(dbNode, jsonNode, colldef);
		}
		
		return _ID;
	}
	
	public void deleteData(String Id, String datatypetype) {
		Map<String, Object> colldef = getCollectionDef(datatypetype);
		
		String bucketName = (String) colldef.get("**GROUPBUCKET**");
		
		if(Id != null && !Id.trim().isEmpty()) {
			SFDataStore sfDatastore = new SFDataStore();
			sfDatastore.delete(Id, bucketName);
		}
	}
	
	@SuppressWarnings({ })
	public void updateChild(JsonNode dbNode, JsonNode dataNode, Map<String, Object> updCollDef) {
		List<Map<String, Object>> propertydefinitions = SFUtil.getSortedPropertyDef(updCollDef);
		String updateCollection = null;
		String updateProperty = null;
		String oldVal = null;
		String filedName = null;
		String newVal = null;
		for(Map<String, Object> propertyDef : propertydefinitions) {
			updateCollection = (String)propertyDef.get("updatecollection");
			updateProperty = (String)propertyDef.get("updateproperty");
			if(updateCollection != null && updateProperty != null) {
				filedName = (String)propertyDef.get("**NESTED**");
				break;
			}
		}
		
		if(updateCollection == null || updateProperty == null || filedName == null) {
			return;
		}
		
		oldVal = dbNode.path(filedName).asText();
		newVal = dataNode.path(filedName).asText();
		
		Map<String, Object> colldef = getCollectionDef(updateCollection);
		String bucketName = (String) colldef.get("**GROUPBUCKET**");
		String collection = (String) colldef.get("name");
		SFSearch sfSearch = SFSearch.instance();
		sfSearch.setBucket(bucketName).setCollection(collection);
		
		SFCriteria sfCriteria1 = SFCriteria.instance().setField(SFConstants.DATATYPE).setValue(updateCollection);
		sfSearch.getListCriteria().add(sfCriteria1);
		SFCriteria sfCriteria2 = SFCriteria.instance().setField(SFConstants.COLLECTION).setValue(collection);
		sfSearch.getListCriteria().add(sfCriteria2);
		SFCriteria sfCriteria3 = SFCriteria.instance().setField(updateProperty).setValue(oldVal);
		sfSearch.getListCriteria().add(sfCriteria3);
		
		SFDataStore sfDatastore = new SFDataStore();
		List<JsonNode> dataList = sfDatastore.getJsonDataList(sfSearch);
		for(JsonNode jsonNode : dataList) {
			JsonNode values = jsonNode.path(updateProperty);
			List<String> listGrp = new ArrayList<String>();
			Iterator<JsonNode> itr = values.elements();
			while(itr.hasNext()) {
				String grp = itr.next().asText();
				if(grp.equals(oldVal)) {
					listGrp.add(newVal);
				} else {
					listGrp.add(grp);
				}
			}
			
			ArrayNode array = new ObjectMapper().valueToTree(listGrp);
			((ObjectNode) jsonNode).putArray(updateProperty).addAll(array);
			sfDatastore.update(jsonNode, bucketName);
		}
	}
	
	private String generateUniqueId() {      
		UUID uuid = UUID.randomUUID();
		String strID = uuid.toString();
		int uid = strID.hashCode();
		String filterStr = "" + uid;
		strID = filterStr.replaceAll("-", "");
		return strID;
    }
	
	private String getCurrentDate() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1);
		Date date = cal.getTime();             
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = sdFormat.format(date);
		return strDate;
	}
}
