package com.lumiata.util;

import org.apache.commons.configuration.PropertiesConfiguration;

/** Copyright 2014 Lumiata 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Data object for all the constants.
 * @author rakesh
 *
 */
public class SFConstants {
	
	private static PropertiesConfiguration config = LoadConfigurationListener.getConfigurations();
	
	public static final String DEFAULT_BUCKET = config.getString("couchbase.server.defaultbucket");
	public static final String DEFAULT_TYPE = "doc";
	
	//Elasticsearch Query Types
	public static final String TERM = "term";
	public static final String WILDCARD = "wildcard";
	public static final String PREFIX = "prefix";
	public static final String FUZZY = "fuzzy";
	public static final String RANGE = "range";
	public static final String QUERYSTRING = "querystring";
	public static final String TEXT = "text";
	public static final String NESTED = "nested";
	public static final String MUST = "must";
	public static final String MUSTNOT = "mustnot";
	public static final String SHOULD = "should";
	
	//Common Field Names
	public static final String CONTAINERFOR = "**CONTAINERFOR**";
	public static final String DOCUMENTTYPE = "**DOCUMENTTYPE**";
	public static final String OBJECTTYPE = "**OBJECTTYPE**";
	public static final String DATATYPE = "**DATATYPE**";
	public static final String ID = "id";
	public static final String COLLECTION = "collection";
	public static final String EFFECTIVEDATE = "effectivedate";
	public static final String ENTRYSTATUS = "entrystatus";
	public static final String MODDATELONG = "moddatelong";
	public static final String STATUS = "status";
	public static final String NAME = "name";
	public static final String REV = "rev";
	public static final String _NESTED = "**NESTED**";
	
	//Collection Field Names
	public static final String DATATYPETYPE = "datatypetype";
	
	//User Field Names
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String GROUPS = "groups";
	public static final String ACCOUNTLOCKED = "accountlocked";
	public static final String PASSWORDLASTCHANGEDATE = "passwordlastchangedate";
	public static final String PASSWORDHISTORY = "passwordhistory";
	public static final String VERIFICATIONCODE = "verificationcode";
	public static final String DEFAULTLANGUAGE = "defaultlanguage";
	
	
	//Group Field Names
	public static final String GROUPNAME = "groupname";
	public static final String GROUPDESC = "groupdesc";
	public static final String GROUPROLES = "grouproles";
	
	//Role Field Names
	public static final String ROLENAME = "rolename";
	public static final String ROLEDESC = "roledesc";
	public static final String ROLETYPE = "roletype";
	public static final String PERMITTEDURLS = "permittedurls";
	
	public static final String URL = "url";
	public static final String URLLABEL = "urllabel";
	
	//Default Field Values
	public static final String PROPERTYDEFINITIONS = "propertydefinitions";
	public static final String DEFAULT_STATUS = "active";
	public static final String COLLECTION_DATATYPE = "collection";
	public static final String USER_DATATYPE = "user";
	public static final String GROUP_DATATYPE = "group";
	public static final String ROLE_DATATYPE = "role";
	public static final String DOCUMENT = "document";
	public static final String DEFAULT_OBJECT = "com.i2028.Document.Document";
	public static final String LANGUAGE = "English";
	public static final String DEFAULT_REV = "0";
	
	//Request Attributes
	public static final String datatype = "datatype";
	public static final String PROPERTYDEFLIST = "propertydeflist";
}
