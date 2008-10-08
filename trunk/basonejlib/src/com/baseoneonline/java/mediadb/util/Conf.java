package com.baseoneonline.java.mediadb.util;

import java.util.Properties;


public class Conf {

	private static final String SPLIT_CHAR = ";";
	
	private static volatile Properties props = new Properties();
	
	public static int getInt(String key) {
		return Integer.parseInt(getString(key));
	}
	
	public static float getFloat(String key) {
		return Float.parseFloat(getString(key));
	}
	
	public static String[] getStringArray(String key) {
		return getString(key).split(SPLIT_CHAR);
	}
	
	public static String getString(String key) {
		String re = props.getProperty(key);
		if (null == re) {
			throw new NullPointerException("Property '"+key+"' was not defined.");
		}
		return re;
	}
	
	public static 
	

}
