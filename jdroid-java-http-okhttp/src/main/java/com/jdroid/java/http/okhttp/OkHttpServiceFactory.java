package com.jdroid.java.http.okhttp;

import com.jdroid.java.collections.Lists;
import com.jdroid.java.http.HttpService;
import com.jdroid.java.http.HttpServiceFactory;
import com.jdroid.java.http.HttpServiceProcessor;
import com.jdroid.java.http.MultipartHttpService;
import com.jdroid.java.http.Server;
import com.jdroid.java.http.okhttp.delete.OkDeleteHttpService;
import com.jdroid.java.http.okhttp.get.OkGetHttpService;
import com.jdroid.java.http.okhttp.patch.OkPatchHttpService;
import com.jdroid.java.http.okhttp.post.OkPostHttpService;
import com.jdroid.java.http.okhttp.put.OkPutHttpService;
import com.jdroid.java.http.post.BodyEnclosingHttpService;

import java.util.List;

import okhttp3.Interceptor;

public class OkHttpServiceFactory implements HttpServiceFactory {

	private List<Interceptor> networkInterceptors = Lists.newArrayList();

	@Override
	public HttpService newGetService(Server server, List<Object> urlSegments, List<HttpServiceProcessor> httpServiceProcessors) {
		OkGetHttpService service = new OkGetHttpService(server, urlSegments, httpServiceProcessors);
		service.setNetworkInterceptors(networkInterceptors);
		return service;
	}
	
	@Override
	public BodyEnclosingHttpService newPostService(Server server, List<Object> urlSegments, List<HttpServiceProcessor> httpServiceProcessors) {
		OkPostHttpService service = new OkPostHttpService(server, urlSegments, httpServiceProcessors);
		service.setNetworkInterceptors(networkInterceptors);
		return service;
	}
	
	@Override
	public MultipartHttpService newMultipartPostService(Server server, List<Object> urlSegments, List<HttpServiceProcessor> httpServiceProcessors) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public MultipartHttpService newMultipartPutService(Server server, List<Object> urlSegments, List<HttpServiceProcessor> httpServiceProcessors) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public BodyEnclosingHttpService newFormPostService(Server server, List<Object> urlSegments, List<HttpServiceProcessor> httpServiceProcessors) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public BodyEnclosingHttpService newPutService(Server server, List<Object> urlSegments, List<HttpServiceProcessor> httpServiceProcessors) {
		OkPutHttpService service = new OkPutHttpService(server, urlSegments, httpServiceProcessors);
		service.setNetworkInterceptors(networkInterceptors);
		return service;
	}
	
	@Override
	public BodyEnclosingHttpService newPatchService(Server baseURL, List<Object> urlSegments, List<HttpServiceProcessor> httpServiceProcessors) {
		OkPatchHttpService service = new OkPatchHttpService(baseURL, urlSegments, httpServiceProcessors);
		service.setNetworkInterceptors(networkInterceptors);
		return service;
	}
	
	@Override
	public HttpService newDeleteService(Server server, List<Object> urlSegments, List<HttpServiceProcessor> httpServiceProcessors) {
		OkHttpService service = new OkDeleteHttpService(server, urlSegments, httpServiceProcessors);
		service.setNetworkInterceptors(networkInterceptors);
		return service;
	}

	public void setNetworkInterceptors(List<Interceptor> networkInterceptors) {
		this.networkInterceptors = networkInterceptors;
	}

	public void addNetworkInterceptor(Interceptor networkInterceptor) {
		this.networkInterceptors.add(networkInterceptor);
	}
}
