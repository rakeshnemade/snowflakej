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

/**
 * Data object for holding the search criteria
 * @author rakesh
 *
 */
public class SFCriteria {
	private String bool = "";  //MUST, MUSTNOT, SHOULD
	private String operation = ""; //TERM, WILDCARD, PREFIX, FUZZY, RANGE, QUERYSTRING, TEXT
	private String action = ""; //eq, noteq, gt, gte, lt, lte, between 
	private String field = "";
	private Object value;
	
	public static SFCriteria instance() {
		return new SFCriteria();
	}

	/**
	 * @return the bool
	 */
	public String getBool() {
		return bool;
	}

	/**
	 * @param bool the bool to set
	 */
	public SFCriteria setBool(String bool) {
		this.bool = bool;
		return this;
	}

	/**
	 * @return the operation
	 */
	public String getOperation() {
		return operation;
	}

	/**
	 * @param operation the operation to set
	 */
	public SFCriteria setOperation(String operation) {
		this.operation = operation;
		return this;
	}
	
	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public SFCriteria setAction(String action) {
		this.action = action;
		return this;
	}

	/**
	 * @return the field
	 */
	public String getField() {
		return field;
	}

	/**
	 * @param field the field to set
	 */
	public SFCriteria setField(String field) {
		this.field = field;
		return this;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public SFCriteria setValue(Object value) {
		this.value = value;
		return this;
	}
	
	
}
