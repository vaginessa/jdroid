package com.jdroid.java.http.apache;

import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.http.HttpServiceProcessor;
import com.jdroid.java.http.Server;
import com.jdroid.java.http.post.EntityEnclosingHttpService;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;

import java.io.UnsupportedEncodingException;
import java.util.List;

public abstract class ApacheEntityEnclosingHttpService extends ApacheHttpService implements
		EntityEnclosingHttpService {
	
	private HttpEntity entity;
	
	public ApacheEntityEnclosingHttpService(HttpClientFactory httpClientFactory, Server server,
											List<Object> urlSegments, List<HttpServiceProcessor> httpServiceProcessors) {
		super(httpClientFactory, server, urlSegments, httpServiceProcessors);
	}
	
	/**
	 * @see ApacheHttpService#createHttpUriRequest(java.lang.String)
	 */
	@Override
	protected HttpUriRequest createHttpUriRequest(String url) {
		// New HttpEntityEnclosingRequestBase for send request.
		HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase = createHttpEntityEnclosingRequestBase(url);
		
		// set body for request.
		addEntity(httpEntityEnclosingRequestBase);
		
		return httpEntityEnclosingRequestBase;
	}
	
	protected abstract HttpEntityEnclosingRequestBase createHttpEntityEnclosingRequestBase(String uri);
	
	protected void addEntity(HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase) {
		httpEntityEnclosingRequestBase.setEntity(entity);
	}
	
	public void setEntity(HttpEntity entity) {
		this.entity = entity;
	}
	
	/**
	 * @see EntityEnclosingHttpService#setBody(String)
	 */
	@Override
	public void setBody(String body) {
		try {
			entity = new StringEntity(body, HTTP.UTF_8);
			ApacheHttpService.LOGGER.debug("Entity: " + body);
		} catch (UnsupportedEncodingException e) {
			throw new UnexpectedException(e);
		}
	}
	
	public HttpEntity getEntity() {
		return entity;
	}
}
