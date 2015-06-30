package com.jdroid.java.http;

import java.util.List;

public interface Server {
	
	public String getName();
	
	public String getBaseUrl();
	
	public Boolean supportsSsl();
	
	public Boolean isProduction();
	
	public List<HttpServiceProcessor> getHttpServiceProcessors();
	
	public Server instance(String name);
	
}
