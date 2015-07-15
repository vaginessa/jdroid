package com.jdroid.javaweb.rollbar;

import com.jdroid.java.http.DefaultServer;
import com.jdroid.java.http.MimeType;
import com.jdroid.java.http.Server;
import com.jdroid.java.http.HttpService;
import com.jdroid.java.http.post.BodyEnclosingHttpService;
import com.jdroid.javaweb.api.ServerApiService;

public class RollBarApiService extends ServerApiService {

	public void sendItem(String body) {
		BodyEnclosingHttpService httpService = newPostService();
		httpService.setSsl(true);
		httpService.addHeader(HttpService.CONTENT_TYPE_HEADER, MimeType.JSON);
		httpService.addHeader(HttpService.ACCEPT_HEADER, MimeType.JSON);
		httpService.setBody(body);
		httpService.execute();
	}

	@Override
	protected Server getServer() {
		return new DefaultServer("api.rollbar.com/api/1/item/");
	}
}
