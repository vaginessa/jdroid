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

public abstract class AbstractUrlConnectionApiService extends AbstractApiService {
	
	@Override
	protected WebService newGetServiceImpl(Server server, List<Object> urlSegments,
										   List<HttpWebServiceProcessor> httpWebServiceProcessors) {
		return new GetUrlConnectionHttpWebService(server, urlSegments, httpWebServiceProcessors);
	}

	@Override
	protected EntityEnclosingWebService newPostServiceImpl(Server server, List<Object> urlSegments,
														   List<HttpWebServiceProcessor> httpWebServiceProcessors) {
		return new PostUrlConnectionHttpWebService(server, urlSegments, httpWebServiceProcessors);
	}

	@Override
	protected MultipartWebService newMultipartPostServiceImpl(Server server, List<Object> urlSegments, List<HttpWebServiceProcessor> httpWebServiceProcessors) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected MultipartWebService newMultipartPutServiceImpl(Server server, List<Object> urlSegments, List<HttpWebServiceProcessor> httpWebServiceProcessors) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected EntityEnclosingWebService newFormPostServiceImpl(Server server, List<Object> urlSegments, List<HttpWebServiceProcessor> httpWebServiceProcessors) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected EntityEnclosingWebService newPutServiceImpl(Server server, List<Object> urlSegments,
														  List<HttpWebServiceProcessor> httpWebServiceProcessors) {
		return new PutUrlConnectionHttpWebService(server, urlSegments, httpWebServiceProcessors);
	}

	@Override
	protected EntityEnclosingWebService newPatchServiceImpl(Server baseURL, List<Object> urlSegments,
															List<HttpWebServiceProcessor> httpWebServiceProcessors) {
		return new PatchUrlConnectionHttpWebService(baseURL, urlSegments, httpWebServiceProcessors);
	}

	@Override
	protected WebService newDeleteServiceImpl(Server server, List<Object> urlSegments,
											  List<HttpWebServiceProcessor> httpWebServiceProcessors) {
		return new DeleteUrlConnectionHttpWebService(server, urlSegments, httpWebServiceProcessors);
	}
}
