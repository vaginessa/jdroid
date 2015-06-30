package com.jdroid.java.http;

import com.jdroid.java.http.post.EntityEnclosingHttpService;

import java.util.List;

public interface HttpServiceFactory {
	
	public HttpService newGetService(Server server, List<Object> urlSegments,
										   List<HttpServiceProcessor> httpServiceProcessors);
	
	public EntityEnclosingHttpService newPostService(Server server, List<Object> urlSegments,
														   List<HttpServiceProcessor> httpServiceProcessors);
	
	public MultipartHttpService newMultipartPostService(Server server, List<Object> urlSegments, List<HttpServiceProcessor> httpServiceProcessors);
	
	public MultipartHttpService newMultipartPutService(Server server, List<Object> urlSegments, List<HttpServiceProcessor> httpServiceProcessors);
	
	public EntityEnclosingHttpService newFormPostService(Server server, List<Object> urlSegments, List<HttpServiceProcessor> httpServiceProcessors);
	
	public EntityEnclosingHttpService newPutService(Server server, List<Object> urlSegments,
														  List<HttpServiceProcessor> httpServiceProcessors);
	
	public EntityEnclosingHttpService newPatchService(Server baseURL, List<Object> urlSegments,
															List<HttpServiceProcessor> httpServiceProcessors);
	
	public HttpService newDeleteService(Server server, List<Object> urlSegments,
											  List<HttpServiceProcessor> httpServiceProcessors);
}
