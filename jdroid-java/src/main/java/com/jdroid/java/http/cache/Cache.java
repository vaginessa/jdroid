package com.jdroid.java.http.cache;

import java.util.Map;

public interface Cache {
	
	public Integer getPriority();
	
	public Float getMinimumSize();
	
	public Float getMaximumSize();
	
	public String getName();
	
	public Map<String, String> getDefaultContent();
	
}
