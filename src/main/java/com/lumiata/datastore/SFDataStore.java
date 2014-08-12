/**
 *<p>
 *This program is confidential and proprietary to Lumiata Inc. and
 *may not be reproduced, published, or disclosed to others without
 *company authorization. Copyright 2013-2016 by Lumiata Inc.
 */

package com.lumiata.datastore;

import java.io.IOException;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.spy.memcached.ConnectionObserver;
import net.spy.memcached.internal.OperationFuture;
import net.spy.memcached.ops.OperationStatus;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import com.couchbase.client.CouchbaseClient;
import com.couchbase.client.CouchbaseConnectionFactory;
import com.couchbase.client.CouchbaseConnectionFactoryBuilder;
import com.couchbase.client.vbucket.ConfigurationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lumiata.search.SFQueryBuilder;
import com.lumiata.search.SFSearch;
import com.lumiata.util.LoadConfigurationListener;
import com.lumiata.util.SFConstants;
import com.lumiata.util.SFUtil;

/**
 * @author rakesh
 *
 */

public class SFDataStore {
	
	private static final Logger logger = Logger.getLogger(SFDataStore.class);
	private static Map<String, CouchbaseClient> CB_CLIENT = new TreeMap<String, CouchbaseClient>();
	
	public String add(JsonNode jsonNode, String bucket) {
		String _ID = null;
		try {
			String collecton = jsonNode.path(SFConstants.COLLECTION).asText();
			_ID = jsonNode.path(SFConstants.ID).asText();
			
			ObjectNode contNode = addContainerNode(collecton, jsonNode);
			
			CouchbaseClient client = getCBConnection(bucket);
			ObjectMapper mapper = new ObjectMapper();
			OperationFuture<Boolean> operationFuture = this.performCRUDOperation(client, _ID, 
					0, mapper.writeValueAsString(contNode), 5, "ADD");
			IndexResponse indexResponse = null;
			if(operationFuture.getStatus().isSuccess()) {
				logger.info("Added record to couchbase :: " + operationFuture.getKey());
				_ID = operationFuture.getKey();
				if(!isINDEXED(bucket, _ID)) {
					indexResponse = this.indexDataInES(contNode, bucket, _ID, "ADD");
					logger.info("Added record to elasticsearch :: " + indexResponse.getId());
					_ID = indexResponse.getId();
				}
			}
			/*if(operationFuture.getStatus().isSuccess()) {
				logger.info("Added record to couchbase :: " + operationFuture.getKey());
				indexResponse = this.indexDataInES(contNode, bucket, _ID, "ADD");
			}
			if(indexResponse != null && indexResponse.getId().equals(operationFuture.getKey())){
				logger.info("Added record to elasticsearch :: " + indexResponse.getId());
				_ID = indexResponse.getId();
			}*/
		} catch (JsonProcessingException e) {
			logger.error("Error adding to Couchbase: "+ e.getMessage());
		} catch(Exception e) {
			logger.error("Error adding to Couchbase: "+ e.getMessage());
		}
		return _ID;
	}
	
	public String update(JsonNode jsonNode, String bucket) { 
		String _ID = null;
		try {
			String collecton = jsonNode.path(SFConstants.COLLECTION).asText();
			_ID = jsonNode.path(SFConstants.ID).asText();
			
			ObjectNode contNode = addContainerNode(collecton, jsonNode);

			CouchbaseClient client = getCBConnection(bucket);
			ObjectMapper mapper = new ObjectMapper();
			OperationFuture<Boolean> operationFuture = this.performCRUDOperation(client, _ID, 0, 
					mapper.writeValueAsString(contNode), 5, "UPDATE");
			IndexResponse indexResponse = null;
			if(operationFuture.getStatus().isSuccess()) {
			   logger.info("Updated record from couchbase :: " + operationFuture.getKey());
				_ID = operationFuture.getKey();
				if(!isINDEXED(bucket, _ID)) {
					indexResponse = this.indexDataInES(contNode, bucket, _ID, "ADD");
					logger.info("Updated record from elasticsearch :: " + indexResponse.getId());
					_ID = indexResponse.getId();
				}
			}
			/*if(operationFuture.getStatus().isSuccess()) {
				logger.info("Updated record from couchbase :: " + operationFuture.getKey());
				indexResponse = this.indexDataInES(contNode, bucket, _ID, "ADD");
			}
			if(indexResponse != null && indexResponse.getId().equals(operationFuture.getKey())){
				logger.info("Updated record from elasticsearch :: " + indexResponse.getId());
				_ID = indexResponse.getId();
			}*/
		} catch(Exception e) {
			logger.error("Error updating data : "+ e.getMessage());
		}
		return _ID;
	}
	
	public void delete(String _ID, String bucket)  {
		OperationFuture<Boolean> operationFuture = null;
		try {
			CouchbaseClient client = getCBConnection(bucket);
			operationFuture = client.delete(_ID);
			IndexResponse indexResponse = null;
			logger.info("Deleted record from couchbase " + operationFuture.getStatus());
			if(operationFuture.getStatus().isSuccess()) {
				indexResponse = this.indexDataInES(null, bucket, _ID, "DELETE");
				logger.info("Deleted record from elasticsearch "+indexResponse.getId());
			}
		} catch(Exception e) {
			logger.error("Error deleting data :: "+ e.getMessage());
		}
	}
	
	public JsonNode getJsonNodeById(SFSearch sfSearch) {
		String _ID = sfSearch.getIdList().get(0);
		JsonNode jsonNode = null;
		CouchbaseClient client = null;
		client = getCBConnection(sfSearch.getBucket());
		Object object = client.get(_ID);
		
		/*Client esClient = ESClient.getInstance().getESClient();
		GetRequestBuilder grb = esClient.prepareGet().setIndex(sfSearch.getBucket()).setId(_ID);
		GetResponse response = grb.execute().actionGet();
		String str = response.getSourceAsString();*/
		JsonNode contNode;
		try {
			//contNode = new ObjectMapper().readTree(object.toString());
			contNode = new ObjectMapper().readTree((String)object);
			String containerfor = contNode.path(SFConstants.CONTAINERFOR).asText();
			jsonNode = contNode.path(containerfor);
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		
		return jsonNode;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getDataById(SFSearch sfSearch) {
		String _ID = sfSearch.getIdList().get(0);
		Map<String, Object> result = null;
		CouchbaseClient client = null;
		try {
			client = getCBConnection(sfSearch.getBucket());
			Object object = client.get(_ID);
			Map<String, Object> mapObject = SFUtil.convertStringToMap((String)object);
			String containerfor = (String)mapObject.get(SFConstants.CONTAINERFOR);
			result = (Map<String, Object>) mapObject.get(containerfor);
			
			/*Client esClient = ESClient.getInstance().getESClient();
			GetRequestBuilder grb = esClient.prepareGet().setIndex(sfSearch.getBucket()).setId(_ID);
			GetResponse response = grb.execute().actionGet();
			Map<String, Object> mapObject = response.getSource();
			String containerfor = (String)mapObject.get(SFConstants.CONTAINERFOR);
			result = (Map<String, Object>) mapObject.get(containerfor);*/
		} catch (Exception e) {
			logger.error(e);
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getDataList(SFSearch sfSearch) {
		
		List<Map<String,Object>> result = new ArrayList<Map<String, Object>>();;
		List<String> fields = sfSearch.getFields();
		if(fields == null || fields.isEmpty()){
			fields.add("_id");
		}
		String[] arrFields = fields.toArray(new String[fields.size()]);
		System.out.println(arrFields);
		
		Client esClient = ESClient.getInstance().getESClient();
		
		SearchRequestBuilder srb = esClient.prepareSearch(sfSearch.getBucket())
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setQuery(SFQueryBuilder.buildQuery(sfSearch)) // Query
		        .setFrom(0).setSize(25).setExplain(false);
		SearchResponse response = srb.execute().actionGet();
		
		SearchHits searchHits = response.getHits();
		List<String> idList = new ArrayList<String>();
		for(SearchHit searchHit : searchHits) {
			String docId = searchHit.getId();
			if(docId != null && !docId.isEmpty()) {
				idList.add(docId);
			}
		}
		
		sfSearch.getIdList().addAll(idList);
		Map<String, Object> mapString = getData(sfSearch); // Returns String, String
		
		if(mapString != null) {
			for (Map.Entry<String, Object> entry : mapString.entrySet()) {
				Map<String, Object> mapObject = SFUtil.convertStringToMap((String) entry.getValue());
				String containerfor = (String)mapObject.get(SFConstants.CONTAINERFOR);
				Map<String, Object> actualObjectMap = (Map<String, Object>) mapObject.get(containerfor);
				result.add(actualObjectMap);
			}
		}
		
		
		/*for(SearchHit searchHit : searchHits) {
			if(searchHit != null) {
				Map<String, Object> mapObject = (Map<String, Object>)searchHit.getSource().get("doc");
				String containerfor = (String)mapObject.get(SFConstants.CONTAINERFOR);
				Map<String, Object> actualObjectMap = (Map<String, Object>) mapObject.get(containerfor);
				result.add(actualObjectMap);
			}
		}*/
		return result;
	}
	
	public List<JsonNode> getJsonDataList(SFSearch sfSearch) {
		
		List<JsonNode> result = new ArrayList<JsonNode>();;
		List<String> fields = sfSearch.getFields();
		if(fields == null || fields.isEmpty()){
			fields.add("_id");
		}
		String[] arrFields = fields.toArray(new String[fields.size()]);
		System.out.println(arrFields);
		
		Client esClient = ESClient.getInstance().getESClient();
		
		SearchRequestBuilder srb = esClient.prepareSearch(sfSearch.getBucket())
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setQuery(SFQueryBuilder.buildQuery(sfSearch)) // Query
		        .setFrom(0).setSize(25).setExplain(false);
		SearchResponse response = srb.execute().actionGet();
		
		SearchHits searchHits = response.getHits();
		List<String> idList = new ArrayList<String>();
		for(SearchHit searchHit : searchHits) {
			String docId = searchHit.getId();
			if(docId != null && !docId.isEmpty()) {
				idList.add(docId);
			}
		}
		
		sfSearch.getIdList().addAll(idList);
		Map<String, Object> mapString = getData(sfSearch); // Returns String, String
		
		if(mapString != null) {
			for (Map.Entry<String, Object> entry : mapString.entrySet()) {
				try {
					JsonNode contNode;
					contNode = new ObjectMapper().readTree((String) entry.getValue());
					String containerfor = contNode.path(SFConstants.CONTAINERFOR).asText();
					JsonNode jsonNode = contNode.path(containerfor);
					result.add(jsonNode);
				} catch (JsonProcessingException e) {
					logger.error(e.getMessage());
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
		}
		
		return result;
	}
	
	
	private Map<String, Object> getData(SFSearch sfSearch) {
		List<String> idList = sfSearch.getIdList();
		if(idList == null || idList.isEmpty()){
			return null;
		}
		
		Map<String,Object> result = null;
		CouchbaseClient client = null;
		try {
			client = getCBConnection(sfSearch.getBucket());
			Collections.reverse(idList);
			result = client.getBulk(idList);
		} catch (Exception e) {
			logger.error(e);
		}
		return result;
	}
	
	/**
     * Connect to the server, or servers given.
     * @param serverAddress the server addresses to connect with.
     * @throws IOException if there is a problem with connecting.
     * @throws URISyntaxException
     * @throws ConfigurationException
     */
    private  CouchbaseClient getCBConnection(String bucketName) {
    	final String _BUCKETNAME = (bucketName == null || bucketName.isEmpty()) ? SFConstants.DEFAULT_BUCKET : bucketName;
    	
    	if(CB_CLIENT.containsKey(bucketName)) return CB_CLIENT.get(bucketName);
        CouchbaseClient client = null;
        
        PropertiesConfiguration config = LoadConfigurationListener.getConfigurations();
        
		try {
			String strCouchbaseServer1 = config.getString("couchbase.server.uri");
			URI base1 = new URI(String.format("http://%s:8091/pools", strCouchbaseServer1));
			
			List<URI> baseURIs = new ArrayList<URI>();
	        baseURIs.add(base1);
	        CouchbaseConnectionFactoryBuilder builder = new CouchbaseConnectionFactoryBuilder();
	        builder.setOpTimeout(10000); // wait up to 10 seconds for an operation to succeed
	        builder.setOpQueueMaxBlockTime(5000);
			CouchbaseConnectionFactory connectionFactory = builder.buildCouchbaseConnection(baseURIs, bucketName, "", "");
			client = new CouchbaseClient(connectionFactory);
	        client.addObserver(new ConnectionObserver() {
	            public void connectionLost(SocketAddress sa) {
	            	logger.debug("Connection lost to " + sa.toString());
	            	CB_CLIENT.remove(_BUCKETNAME);
	            	getCBConnection(_BUCKETNAME);
	            }
	            public void connectionEstablished(SocketAddress sa, int reconnectCount) {
	            	logger.debug("Connection established with " + sa.toString());
	            	logger.debug("Reconnected count: " + reconnectCount);
	            }
	        });
		} catch (URISyntaxException e) {
			logger.error(e);
		} catch (ConfigurationException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
		
		CB_CLIENT.put(bucketName, client);
		
        return client;
    }
    
    private OperationFuture<Boolean> performCRUDOperation(CouchbaseClient client, String key, int exp, Object value, int tries, String operationType) {
        OperationFuture<Boolean> result = null;
        OperationStatus status;
        int backoffexp = 0;

        try {
            do {
                if (backoffexp > tries) throw new RuntimeException("Could not perform a CRUD after "+ tries +" tries.");
                
				if (operationType.equals("ADD")) {
					result = client.add(key, exp, value);
				} else if (operationType.equals("UPDATE")) {
					result = client.replace(key, exp, value);
				} else if (operationType.equals("DELETE")) {
					result = client.delete(key);
				}
                
                status = result.getStatus();
            
                if (status.isSuccess()) break;
            
                if (backoffexp > 0) {
                  double backoffMillis = Math.pow(2, backoffexp);
                  backoffMillis = Math.min(1000, backoffMillis); // 1 sec max
                  Thread.sleep((int) backoffMillis);
                  logger.error("Backing off, tries so far : " + backoffexp);
                }
            
                backoffexp++;

                if (!status.isSuccess()) {
                	logger.error("Failed with status: " + status.getMessage());
                }

                } while (status.getMessage().equals("Temporary failure"));

        } catch (InterruptedException ex) {
           logger.error("Interrupted while trying to set.  Exception:" + ex.getMessage());
        }
		return result;
    }
    
    private IndexResponse indexDataInES(JsonNode jsonNode, String index, String _ID, String operationType) {
    	ObjectMapper mapper = new ObjectMapper();
    	IndexResponse indexResponse = null;
		try {
			Client esClient = ESClient.getInstance().getESClient();
			
			if(operationType.equals("ADD") || operationType.equals("UPDATE")) {
				indexResponse = esClient.prepareIndex()
						.setIndex(index)
						.setType("doc")
						.setId(_ID)
						.setSource(mapper.writeValueAsString(jsonNode))
						.execute()
						.actionGet();
				esClient.admin().indices().prepareRefresh(index).execute().actionGet();
			} else if(operationType.equals("DELETE")) {
				esClient.prepareDelete()
				.setIndex(index)
				.setType("doc")
				.setId(_ID)
				.execute()
				.actionGet();
				esClient.admin().indices().prepareRefresh(index).execute().actionGet();
			}
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
		return indexResponse;
    }
    
    public boolean isINDEXED(String index, String id) {
        try {
        	Client esClient = ESClient.getInstance().getESClient();
        	GetResponse response = esClient.prepareGet()
        			.setIndex(index)
        			.setType(SFConstants.DEFAULT_TYPE)
        			.setId(id)
        			.execute()
        			.actionGet();
        	if(response != null && id.equals(response.getId())) {
        		return true;
        	}
            return false;
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
        return false;
    }
    
    @SuppressWarnings("deprecation")
	private ObjectNode addContainerNode(String container, JsonNode jsonNode) {
    	ObjectNode contNode = JsonNodeFactory.instance.objectNode();
		contNode.put(SFConstants.CONTAINERFOR, container);
		contNode.put(container, jsonNode);
		return contNode;
    }

}
