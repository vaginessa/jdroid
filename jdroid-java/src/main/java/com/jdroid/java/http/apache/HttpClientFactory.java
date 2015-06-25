package com.jdroid.java.http.apache;

import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Factory interface to create {@link DefaultHttpClient}s.
 */
public interface HttpClientFactory {
	
	/**
	 * Creates a {@link DefaultHttpClient} and sets a default timeout and user agent for it.
	 * 
	 * @return {@link DefaultHttpClient} The created client.
	 */
	public DefaultHttpClient createHttpClient();
	
	/**
	 * Creates a {@link DefaultHttpClient} and sets a timeout for it.
	 *
	 * @param connectionTimeout The connection timeout in milliseconds.
	 * @param readTimeout The connection timeout in milliseconds.
	 * @param userAgent The user agent
	 *
	 * @return {@link DefaultHttpClient} The created client.
	 */
	public DefaultHttpClient createHttpClient(Integer connectionTimeout, Integer readTimeout, String userAgent);
}
