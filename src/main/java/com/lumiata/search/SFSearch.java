package com.lumiata.search;

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
 */


import java.util.ArrayList;
import java.util.List;

import com.lumiata.util.SFConstants;
/**
 * Data object for holding List of Search criteria (List<LumiCriteria>)
 * @author rakesh
 *
 */
public class SFSearch {
	
	private List<SFCriteria> listCriteria = new ArrayList<SFCriteria>();
	private List<String> fields = new ArrayList<String>();
	private List<String> idList = new ArrayList<String>();
	private String bucket = SFConstants.DEFAULT_BUCKET;
	private String collection = SFConstants.DEFAULT_BUCKET;
	String nestedpath = "";
	boolean nested = false;
	
	public static SFSearch instance() {
		return new SFSearch();
	}
	
	/**
	 * @return the listCriteria
	 */
	public List<SFCriteria> getListCriteria() {
		return listCriteria;
	}

	/**
	 * @return the fields
	 */
	public List<String> getFields() {
		return fields;
	}

	/**
	 * @return the idList
	 */
	public List<String> getIdList() {
		return idList;
	}

	/**
	 * @return the bucket
	 */
	public String getBucket() {
		return bucket;
	}

	/**
	 * @param bucket the bucket to set
	 */
	public SFSearch setBucket(String bucket) {
		this.bucket = bucket;
		return this;
	}

	/**
	 * @return the collection
	 */
	public String getCollection() {
		return collection;
	}

	/**
	 * @param collection the collection to set
	 */
	public SFSearch setCollection(String collection) {
		this.collection = collection;
		return this;
	}

	/**
	 * @return the nestedpath
	 */
	public String getNestedpath() {
		return nestedpath;
	}

	/**
	 * @param nestedpath the nestedpath to set
	 */
	public SFSearch setNestedpath(String nestedpath) {
		this.nestedpath = nestedpath;
		return this;
	}

	/**
	 * @return the nested
	 */
	public boolean isNested() {
		return nested;
	}

	/**
	 * @param nested the nested to set
	 */
	public SFSearch setNested(boolean nested) {
		this.nested = nested;
		return this;
	}
	
}
