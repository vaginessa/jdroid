package com.jdroid.java.http;

import com.jdroid.java.collections.Lists;

import java.util.List;

public class DefaultServer implements Server {
	
	private String name;
	private String baseUrl;
	private Boolean supportsSsl;
	
	public DefaultServer(String name, String baseUrl, Boolean supportsSsl) {
		this.name = name;
		this.baseUrl = baseUrl;
		this.supportsSsl = supportsSsl;
	}
	
	public DefaultServer(String baseUrl) {
		this(DefaultServer.class.getSimpleName(), baseUrl, true);
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String getBaseUrl() {
		return baseUrl;
	}
	
	@Override
	public Boolean supportsSsl() {
		return supportsSsl;
	}
	
	@Override
	public Boolean isProduction() {
		return true;
	}
	
	@Override
	public List<HttpServiceProcessor> getHttpServiceProcessors() {
		return Lists.<HttpServiceProcessor>newArrayList(DefaultHttpResponseValidator.get());
	}
	
	@Override
	public Server instance(String name) {
		return null;
	}
}
