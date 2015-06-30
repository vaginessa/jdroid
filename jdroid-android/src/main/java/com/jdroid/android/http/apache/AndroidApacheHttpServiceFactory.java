package com.jdroid.android.http.apache;

import com.jdroid.java.http.apache.ApacheHttpServiceFactory;
import com.jdroid.java.http.apache.HttpClientFactory;

public class AndroidApacheHttpServiceFactory extends ApacheHttpServiceFactory {

	@Override
	protected HttpClientFactory getHttpClientFactoryInstance() {
		return AndroidHttpClientFactory.get();
	}
}
