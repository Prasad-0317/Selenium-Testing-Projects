package com.parameter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyReader {
    private static Properties properties;

    public static Properties initProperties(){
    	if(properties == null) {
    		properties = new Properties();
    	
	        try {
	            FileInputStream fis = new FileInputStream("src/test/resources/PropertyData/Object.properties");
	            properties = new Properties();
	            properties.load(fis);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
    	}
    	return properties;
    }
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}