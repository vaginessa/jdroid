package com.jdroid.java.http;

public interface HttpWebServiceProcessor {
	
	public void beforeExecute(WebService webService);
	
	public void afterExecute(WebService webService, HttpResponseWrapper httpResponse);
	
}