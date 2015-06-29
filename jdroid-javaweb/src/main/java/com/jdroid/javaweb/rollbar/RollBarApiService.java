package com.jdroid.javaweb.rollbar;

import com.jdroid.java.http.DefaultServer;
import com.jdroid.java.http.MimeType;
import com.jdroid.java.http.Server;
import com.jdroid.java.http.WebService;
import com.jdroid.java.http.post.EntityEnclosingWebService;
import com.jdroid.javaweb.api.ServerApiService;

public class RollBarApiService extends ServerApiService {

	public void sendItem(String body) {
		EntityEnclosingWebService webService = newPostService();
		webService.setSsl(true);
		webService.addHeader(WebService.CONTENT_TYPE_HEADER, MimeType.JSON);
		webService.addHeader(WebService.ACCEPT_HEADER, MimeType.JSON);
		webService.setBody(body);
		webService.execute();
	}

	@Override
	protected Server getServer() {
		return new DefaultServer("api.rollbar.com/api/1/item/");
	}
}
