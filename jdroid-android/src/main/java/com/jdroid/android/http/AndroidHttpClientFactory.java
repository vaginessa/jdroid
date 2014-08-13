package com.jdroid.android.http;

import org.apache.http.impl.client.DefaultHttpClient;
import com.jdroid.java.http.apache.DefaultHttpClientFactory;
import com.jdroid.java.http.apache.HttpClientFactory;

public class AndroidHttpClientFactory extends DefaultHttpClientFactory {
	
	private static HttpClientFactory INSTANCE;
	
	public static HttpClientFactory get() {
		if (INSTANCE == null) {
			INSTANCE = new AndroidHttpClientFactory();
		}
		return INSTANCE;
	}
	
	/**
	 * @see com.jdroid.java.http.apache.HttpClientFactory#createHttpClient(java.lang.Boolean, java.lang.Integer,
	 *      java.lang.String)
	 */
	@Override
	public DefaultHttpClient createHttpClient(Boolean ssl, Integer timeout, String userAgent) {
		DefaultHttpClient httpClient = super.createHttpClient(ssl, timeout, userAgent);
		httpClient.setRedirectHandler(new AndroidDefaultRedirectHandler());
		return httpClient;
	}
	
}
