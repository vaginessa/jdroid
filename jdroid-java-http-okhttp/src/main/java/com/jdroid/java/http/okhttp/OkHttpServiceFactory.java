package com.jdroid.java.http.okhttp;

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

public class OkHttpServiceFactory implements HttpServiceFactory {
	
	@Override
	public HttpService newGetService(Server server, List<Object> urlSegments,
										   List<HttpServiceProcessor> httpServiceProcessors) {
		return new OkGetHttpService(server, urlSegments, httpServiceProcessors);
	}
	
	@Override
	public BodyEnclosingHttpService newPostService(Server server, List<Object> urlSegments,
														   List<HttpServiceProcessor> httpServiceProcessors) {
		return new OkPostHttpService(server, urlSegments, httpServiceProcessors);
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
		return new OkPutHttpService(server, urlSegments, httpServiceProcessors);
	}
	
	@Override
	public BodyEnclosingHttpService newPatchService(Server baseURL, List<Object> urlSegments,
															List<HttpServiceProcessor> httpServiceProcessors) {
		return new OkPatchHttpService(baseURL, urlSegments, httpServiceProcessors);
	}
	
	@Override
	public HttpService newDeleteService(Server server, List<Object> urlSegments,
											  List<HttpServiceProcessor> httpServiceProcessors) {
		return new OkDeleteHttpService(server, urlSegments, httpServiceProcessors);
	}
}
