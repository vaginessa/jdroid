package com.jdroid.java.http;

public interface HttpWebServiceProcessor {

	/**
	 * The logic to be executed when this processor is added to the WebService. It could be executed on the WebService creation or
	 * after the processor is added to the WebService.
	 *
	 * @param webService The WebService where the processor is executed
	 */
	public void onInit(WebService webService);
	
	public void beforeExecute(WebService webService);
	
	public void afterExecute(WebService webService, HttpResponseWrapper httpResponse);
	
}