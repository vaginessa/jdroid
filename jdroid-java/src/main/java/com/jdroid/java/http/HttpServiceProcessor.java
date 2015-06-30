package com.jdroid.java.http;

public interface HttpServiceProcessor {

	/**
	 * The logic to be executed when this processor is added to the HttpService. It could be executed on the HttpService creation or
	 * after the processor is added to the HttpService.
	 *
	 * @param httpService The HttpService where the processor is executed
	 */
	public void onInit(HttpService httpService);
	
	public void beforeExecute(HttpService httpService);
	
	public void afterExecute(HttpService httpService, HttpResponseWrapper httpResponse);
	
}