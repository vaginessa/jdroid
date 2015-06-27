package com.jdroid.java.api;

import com.jdroid.java.http.HttpWebServiceProcessor;
import com.jdroid.java.http.MultipartWebService;
import com.jdroid.java.http.Server;
import com.jdroid.java.http.WebService;
import com.jdroid.java.http.post.EntityEnclosingWebService;

import java.util.List;

public interface ApiHttpFactory {
	
	public WebService newGetService(Server server, List<Object> urlSegments,
										   List<HttpWebServiceProcessor> httpWebServiceProcessors);
	
	public EntityEnclosingWebService newPostService(Server server, List<Object> urlSegments,
														   List<HttpWebServiceProcessor> httpWebServiceProcessors);
	
	public MultipartWebService newMultipartPostService(Server server, List<Object> urlSegments, List<HttpWebServiceProcessor> httpWebServiceProcessors);
	
	public MultipartWebService newMultipartPutService(Server server, List<Object> urlSegments, List<HttpWebServiceProcessor> httpWebServiceProcessors);
	
	public EntityEnclosingWebService newFormPostService(Server server, List<Object> urlSegments, List<HttpWebServiceProcessor> httpWebServiceProcessors);
	
	public EntityEnclosingWebService newPutService(Server server, List<Object> urlSegments,
														  List<HttpWebServiceProcessor> httpWebServiceProcessors);
	
	public EntityEnclosingWebService newPatchService(Server baseURL, List<Object> urlSegments,
															List<HttpWebServiceProcessor> httpWebServiceProcessors);
	
	public WebService newDeleteService(Server server, List<Object> urlSegments,
											  List<HttpWebServiceProcessor> httpWebServiceProcessors);
}
