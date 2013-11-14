package com.jdroid.java.http;

import java.util.List;

/**
 * 
 * @author Maxi Rosson
 */
public interface Server {
	
	public String getName();
	
	public String getBaseUrl();
	
	public Boolean supportsSsl();
	
	public Boolean isProduction();
	
	public List<HttpWebServiceProcessor> getHttpWebServiceProcessors();
	
	public Server instance(String name);
	
}
