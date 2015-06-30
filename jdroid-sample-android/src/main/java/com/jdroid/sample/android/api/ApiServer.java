package com.jdroid.sample.android.api;

import com.jdroid.android.AbstractApplication;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.http.HttpServiceProcessor;
import com.jdroid.java.http.Server;

import java.util.List;

public enum ApiServer implements Server {
	
	PROD("url", ":8080", "/jdroid-sample-server/api", true, true),
	UAT("jdroid-sample-server-staging.herokuapp.com", null, "/jdroid-sample-server/api", true, false),
	DEV(null, ":8080", "/jdroid-sample-server/api", true, false);

	private String domain;
	private String port;
	private String segment;
	private Boolean supportsSsl;
	private Boolean isProduction;
	
	private ApiServer(String domain, String port, String segment, Boolean supportsSsl, Boolean isProduction) {
		this.domain = domain;
		this.port = port;
		this.segment = segment;
		this.supportsSsl = supportsSsl;
		this.isProduction = isProduction;
	}
	
	/**
	 * @see com.jdroid.java.http.Server#getName()
	 */
	@Override
	public String getName() {
		return name();
	}
	
	/**
	 * @see com.jdroid.java.http.Server#getBaseUrl()
	 */
	@Override
	public String getBaseUrl() {
		if (domain == null) {
			return AbstractApplication.get().getAppContext().getLocalIp() + port + segment;
		} else {
			StringBuilder urlBuilder = new StringBuilder();
			urlBuilder.append(domain);
			if (port != null) {
				urlBuilder.append(port);
			}
			if (segment != null) {
				urlBuilder.append(segment);
			}
			return urlBuilder.toString();
		}
	}
	
	/**
	 * @see com.jdroid.java.http.Server#supportsSsl()
	 */
	@Override
	public Boolean supportsSsl() {
		return supportsSsl;
	}
	
	/**
	 * @see com.jdroid.java.http.Server#isProduction()
	 */
	@Override
	public Boolean isProduction() {
		return isProduction;
	}
	
	/**
	 * @see com.jdroid.java.http.Server#getHttpServiceProcessors()
	 */
	@Override
	public List<HttpServiceProcessor> getHttpServiceProcessors() {
		return Lists.newArrayList(HeadersAppender.get(), HttpResponseValidator.get());
	}
	
	/**
	 * @see com.jdroid.java.http.Server#instance(java.lang.String)
	 */
	@Override
	public Server instance(String name) {
		return valueOf(name);
	}
}