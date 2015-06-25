package com.jdroid.android.http.apache;

import com.jdroid.java.http.apache.DefaultHttpClientFactory;
import com.jdroid.java.http.apache.HttpClientFactory;

import org.apache.http.impl.client.DefaultHttpClient;

public class AndroidHttpClientFactory extends DefaultHttpClientFactory {
	
	private static HttpClientFactory INSTANCE;
	
	public static HttpClientFactory get() {
		if (INSTANCE == null) {
			INSTANCE = new AndroidHttpClientFactory();
		}
		return INSTANCE;
	}
	
	@Override
	public DefaultHttpClient createHttpClient(Integer connectionTimeout, Integer readTimeout, String userAgent) {
		DefaultHttpClient httpClient = super.createHttpClient(connectionTimeout, readTimeout, userAgent);
		httpClient.setRedirectHandler(new AndroidDefaultRedirectHandler());
		return httpClient;
	}
	
}
