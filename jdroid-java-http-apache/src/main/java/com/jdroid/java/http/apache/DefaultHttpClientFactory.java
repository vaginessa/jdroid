package com.jdroid.java.http.apache;

import com.jdroid.java.utils.StringUtils;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;

public class DefaultHttpClientFactory implements HttpClientFactory {
	
	private static HttpClientFactory INSTANCE;
	
	public static HttpClientFactory get() {
		if (INSTANCE == null) {
			INSTANCE = new DefaultHttpClientFactory();
		}
		return INSTANCE;
	}
	
	/**
	 * @see com.jdroid.java.http.apache.HttpClientFactory#createHttpClient()
	 */
	@Override
	public DefaultHttpClient createHttpClient() {
		return createHttpClient(null, null, null);
	}

	@Override
	public DefaultHttpClient createHttpClient(Integer connectionTimeout, Integer readTimeout, String userAgent) {
		DefaultHttpClient client = new DefaultHttpClient();
		if (connectionTimeout != null) {
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, connectionTimeout);
		}

		if (readTimeout != null) {
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, readTimeout);
		}

		if (StringUtils.isNotBlank(userAgent)) {
			client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, userAgent);
		}
		return client;
	}
}