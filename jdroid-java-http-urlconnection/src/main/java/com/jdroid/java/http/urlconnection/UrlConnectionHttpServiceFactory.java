package com.jdroid.java.http.urlconnection;

import com.jdroid.java.http.HttpServiceFactory;
import com.jdroid.java.http.HttpServiceProcessor;
import com.jdroid.java.http.MultipartHttpService;
import com.jdroid.java.http.Server;
import com.jdroid.java.http.HttpService;
import com.jdroid.java.http.post.BodyEnclosingHttpService;
import com.jdroid.java.http.urlconnection.delete.UrlConnectionDeleteHttpService;
import com.jdroid.java.http.urlconnection.get.UrlConnectionGetHttpService;
import com.jdroid.java.http.urlconnection.patch.UrlConnectionPatchHttpService;
import com.jdroid.java.http.urlconnection.post.UrlConnectionPostHttpService;
import com.jdroid.java.http.urlconnection.put.UrlConnectionPutHttpService;

import java.util.List;

public class UrlConnectionHttpServiceFactory implements HttpServiceFactory {
	
	@Override
	public HttpService newGetService(Server server, List<Object> urlSegments,
										   List<HttpServiceProcessor> httpServiceProcessors) {
		return new UrlConnectionGetHttpService(server, urlSegments, httpServiceProcessors);
	}
	
	@Override
	public BodyEnclosingHttpService newPostService(Server server, List<Object> urlSegments,
														   List<HttpServiceProcessor> httpServiceProcessors) {
		return new UrlConnectionPostHttpService(server, urlSegments, httpServiceProcessors);
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
	public BodyEnclosingHttpService newPutService(Server server, List<Object> urlSegments,
														  List<HttpServiceProcessor> httpServiceProcessors) {
		return new UrlConnectionPutHttpService(server, urlSegments, httpServiceProcessors);
	}
	
	@Override
	public BodyEnclosingHttpService newPatchService(Server baseURL, List<Object> urlSegments,
															List<HttpServiceProcessor> httpServiceProcessors) {
		return new UrlConnectionPatchHttpService(baseURL, urlSegments, httpServiceProcessors);
	}
	
	@Override
	public HttpService newDeleteService(Server server, List<Object> urlSegments,
											  List<HttpServiceProcessor> httpServiceProcessors) {
		return new UrlConnectionDeleteHttpService(server, urlSegments, httpServiceProcessors);
	}
}
