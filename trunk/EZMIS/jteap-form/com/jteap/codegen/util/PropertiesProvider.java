package com.jteap.codegen.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;


/**
 * 
 * @author tantyou
 */
@SuppressWarnings("unchecked")
public class PropertiesProvider {

	static Properties props;
	
	private PropertiesProvider(){}
	
	
	private static void initProperties() {
		try {
			props = loadAllProperties("/com/jteap/codegen/util/generator.properties");
			String basepackage = props.getProperty("basepackage");
			String basepackage_dir = basepackage.replace('.', '/');
			
			props.put("basepackage_dir", basepackage_dir);
			
			for(Iterator it = props.entrySet().iterator();it.hasNext();) {
				Map.Entry entry = (Map.Entry)it.next();
				System.out.println("[Property] "+entry.getKey()+"="+entry.getValue());
			}
			
			System.out.println();
			
		}catch(IOException e) {
			throw new RuntimeException("Load Properties error",e);
		}
	}
	
	public static Properties getProperties() {
		if(props == null)
			initProperties();
		return props;
	}
	
	public static String getProperty(String key, String defaultValue) {
		return getProperties().getProperty(key, defaultValue);
	}

	public static String getProperty(String key) {
		return getProperties().getProperty(key);
	}

	public static Properties loadAllProperties(String resourceName) throws IOException {
		Properties properties = new Properties();
		URL url = PropertiesProvider.class.getResource(resourceName);
		InputStream is = null;
		try {
			URLConnection con = url.openConnection();
			con.setUseCaches(false);
			is = con.getInputStream();
			properties.load(is);
		}
		finally {
			if (is != null) {
				is.close();
			}
		}
		return properties;
	}
}
