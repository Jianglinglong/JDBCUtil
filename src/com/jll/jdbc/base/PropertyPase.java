package com.jll.jdbc.base;

import java.util.ResourceBundle;

public class PropertyPase {
	private static PropertyPase proPces = null;
	private static ResourceBundle bundle;
	private static ThreadLocal<PropertyPase> threadLocal = new ThreadLocal<PropertyPase>();

	private PropertyPase() {
		bundle = ResourceBundle.getBundle("mysql");
	}

	public static PropertyPase getInstance() {
		proPces = threadLocal.get();
		if (proPces == null) {
			proPces = new PropertyPase();
			threadLocal.set(proPces);
		}
		return proPces;
	}

	public String getValue(String key) {
		return bundle.getString(key);
	}
}
