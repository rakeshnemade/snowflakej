package com.lumiata.datastore;

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


import org.apache.commons.configuration.PropertiesConfiguration;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import com.lumiata.util.LoadConfigurationListener;

/**
 * This class is an elasticsearch transport client implementing Singleton pattern.
 * @author rakesh
 *
 */
public class ESClient {
	
	private static Client client;
	private static ESClient esClient;
	
	private ESClient(){}
	
	public static ESClient getInstance(){
		if(esClient == null){
			esClient = new ESClient();
		}
		return esClient;
	}
	
	public Client getESClient() {
		if(client == null){
			PropertiesConfiguration config = LoadConfigurationListener.getConfigurations();
			String serverName = config.getString("elasticsearch.server.uri");
			String clusterName = config.getString("elasticsearch.server.cluster");
			Settings settings = ImmutableSettings.settingsBuilder()
			        .put("cluster.name", clusterName)
			        .build();
			client = new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress(serverName, 9300));
		}
		return client;
	}
}
