package com.lumiata.util;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class LoadConfigurationListener implements ServletContextListener {

	private static ServletContext context;
	private static PropertiesConfiguration config = null;
	
	public void contextDestroyed(ServletContextEvent sce) {
		context = sce.getServletContext();
		
	}

	public void contextInitialized(ServletContextEvent sce) {
		try {
			config = new  PropertiesConfiguration("configurations.properties");
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public static PropertiesConfiguration getConfigurations() {
		return config;
	}
	
	public static ServletContext getServletContext(){
		return context;
	}

}
