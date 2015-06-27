package com.jdroid.android.http.apache;

import com.jdroid.java.api.ApacheApiHttpFactory;
import com.jdroid.java.http.apache.HttpClientFactory;

public class AndroidApacheApiHttpFactory extends ApacheApiHttpFactory {

	@Override
	protected HttpClientFactory getHttpClientFactoryInstance() {
		return AndroidHttpClientFactory.get();
	}
}
