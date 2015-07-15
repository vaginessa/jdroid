package com.jdroid.java.http;

import com.jdroid.java.http.post.BodyEnclosingHttpService;

import java.util.List;

public interface HttpServiceFactory {
	
	public HttpService newGetService(Server server, List<Object> urlSegments,
										   List<HttpServiceProcessor> httpServiceProcessors);
	
	public BodyEnclosingHttpService newPostService(Server server, List<Object> urlSegments,
														   List<HttpServiceProcessor> httpServiceProcessors);
	
	public MultipartHttpService newMultipartPostService(Server server, List<Object> urlSegments, List<HttpServiceProcessor> httpServiceProcessors);
	
	public MultipartHttpService newMultipartPutService(Server server, List<Object> urlSegments, List<HttpServiceProcessor> httpServiceProcessors);
	
	public BodyEnclosingHttpService newFormPostService(Server server, List<Object> urlSegments, List<HttpServiceProcessor> httpServiceProcessors);
	
	public BodyEnclosingHttpService newPutService(Server server, List<Object> urlSegments,
														  List<HttpServiceProcessor> httpServiceProcessors);
	
	public BodyEnclosingHttpService newPatchService(Server baseURL, List<Object> urlSegments,
															List<HttpServiceProcessor> httpServiceProcessors);
	
	public HttpService newDeleteService(Server server, List<Object> urlSegments,
											  List<HttpServiceProcessor> httpServiceProcessors);
}
