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


import java.util.List;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import com.lumiata.util.SFConstants;

/**
 * This will parse the search criteria (LumiSearch object) to create the 
 * elasticsearch query.
 * @author rakesh
 *
 */
public class SFQueryBuilder {
	
	@SuppressWarnings({ "null" })
	public static QueryBuilder buildQuery(SFSearch sfSearch){
		List<SFCriteria> searchCriteriaList = sfSearch.getListCriteria();
		if(searchCriteriaList == null && searchCriteriaList.isEmpty()){
			return null;
		}
		BoolQueryBuilder boolQB = QueryBuilders.boolQuery();
		String collection = sfSearch.getCollection();
		for(SFCriteria sfCriteria : searchCriteriaList){
			QueryBuilder qb = getQueryBuilder(sfCriteria, collection);
			switch (sfCriteria.getBool()) {
				case SFConstants.MUSTNOT:
					boolQB.mustNot(qb);
					break;
				case SFConstants.SHOULD:
					boolQB.should(qb);
					break;
				default:
					boolQB.must(qb);
					break;
			}
		}
		
		if(sfSearch.isNested()) {
			QueryBuilder queryBuilder = QueryBuilders.nestedQuery("doc." + collection + "." + sfSearch.getNestedpath(), boolQB);
			return queryBuilder;
		} 
		
		return boolQB;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	private static QueryBuilder getQueryBuilder(SFCriteria sfCriteria, String collection){
		QueryBuilder queryBuilder = null;
		String operation = sfCriteria.getOperation() == null ? "" : sfCriteria.getOperation();
		String field = "doc." + collection + "." + sfCriteria.getField();
		String value = null;
		List<String> values = null;
		if(sfCriteria.getValue() instanceof String) {
			value = sfCriteria.getValue().toString();
		} else if(sfCriteria.getValue() instanceof List) {
			values = (List<String>)sfCriteria.getValue();
		}

		switch (operation) {
			case SFConstants.WILDCARD:  
				queryBuilder = QueryBuilders.wildcardQuery(field, value);
			break;
			case SFConstants.PREFIX:  
				queryBuilder = QueryBuilders.prefixQuery(field, value);
			break;
			case SFConstants.FUZZY:  
				queryBuilder = QueryBuilders.fuzzyQuery(field, value);
			break;
			case SFConstants.RANGE:  
				if ("gt".equals(sfCriteria.getAction())) {
					queryBuilder = QueryBuilders.rangeQuery(field).gt(sfCriteria.getValue());
	            } else if ("gte".equals(sfCriteria.getAction())) {
	            	queryBuilder = QueryBuilders.rangeQuery(field).gte(sfCriteria.getValue());
	            } else if ("lt".equals(sfCriteria.getAction())) {
	            	queryBuilder = QueryBuilders.rangeQuery(field).lt(sfCriteria.getValue());
	            } else if ("lte".equals(sfCriteria.getAction())) {
	            	queryBuilder = QueryBuilders.rangeQuery(field).lte(sfCriteria.getValue());
	            } else if ("between".equals(sfCriteria.getAction())) {
	            	List<Object> list = (List<Object>) sfCriteria.getValue();
	            	Object left = list.get(0);
	            	Object right = list.get(1);
	            	queryBuilder = QueryBuilders.rangeQuery(field).gte(left).lte(right);
	            }
			break;
			case SFConstants.QUERYSTRING:  
				queryBuilder = QueryBuilders.queryString(value);
			break;
			case SFConstants.TEXT:  
				queryBuilder = QueryBuilders.text(field, value);
			break;
			default:
				if(value != null) {
					queryBuilder = QueryBuilders.termQuery(field, value);
				} else {
					queryBuilder = QueryBuilders.termsQuery(field, values);
				}
			break;
			
			
			//Need to implement logic for Geo and Nested
		}
		
		return queryBuilder;
	}
}
