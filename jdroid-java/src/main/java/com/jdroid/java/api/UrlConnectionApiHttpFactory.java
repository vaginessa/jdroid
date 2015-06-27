package com.jdroid.java.api;

import com.jdroid.java.http.HttpWebServiceProcessor;
import com.jdroid.java.http.MultipartWebService;
import com.jdroid.java.http.Server;
import com.jdroid.java.http.WebService;
import com.jdroid.java.http.post.EntityEnclosingWebService;
import com.jdroid.java.http.urlconnection.delete.DeleteUrlConnectionHttpWebService;
import com.jdroid.java.http.urlconnection.get.GetUrlConnectionHttpWebService;
import com.jdroid.java.http.urlconnection.patch.PatchUrlConnectionHttpWebService;
import com.jdroid.java.http.urlconnection.post.PostUrlConnectionHttpWebService;
import com.jdroid.java.http.urlconnection.put.PutUrlConnectionHttpWebService;

import java.util.List;

public class UrlConnectionApiHttpFactory implements ApiHttpFactory {
	
	@Override
	public WebService newGetService(Server server, List<Object> urlSegments,
										   List<HttpWebServiceProcessor> httpWebServiceProcessors) {
		return new GetUrlConnectionHttpWebService(server, urlSegments, httpWebServiceProcessors);
	}
	
	@Override
	public EntityEnclosingWebService newPostService(Server server, List<Object> urlSegments,
														   List<HttpWebServiceProcessor> httpWebServiceProcessors) {
		return new PostUrlConnectionHttpWebService(server, urlSegments, httpWebServiceProcessors);
	}
	
	@Override
	public MultipartWebService newMultipartPostService(Server server, List<Object> urlSegments, List<HttpWebServiceProcessor> httpWebServiceProcessors) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public MultipartWebService newMultipartPutService(Server server, List<Object> urlSegments, List<HttpWebServiceProcessor> httpWebServiceProcessors) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public EntityEnclosingWebService newFormPostService(Server server, List<Object> urlSegments, List<HttpWebServiceProcessor> httpWebServiceProcessors) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public EntityEnclosingWebService newPutService(Server server, List<Object> urlSegments,
														  List<HttpWebServiceProcessor> httpWebServiceProcessors) {
		return new PutUrlConnectionHttpWebService(server, urlSegments, httpWebServiceProcessors);
	}
	
	@Override
	public EntityEnclosingWebService newPatchService(Server baseURL, List<Object> urlSegments,
															List<HttpWebServiceProcessor> httpWebServiceProcessors) {
		return new PatchUrlConnectionHttpWebService(baseURL, urlSegments, httpWebServiceProcessors);
	}
	
	@Override
	public WebService newDeleteService(Server server, List<Object> urlSegments,
											  List<HttpWebServiceProcessor> httpWebServiceProcessors) {
		return new DeleteUrlConnectionHttpWebService(server, urlSegments, httpWebServiceProcessors);
	}
}
