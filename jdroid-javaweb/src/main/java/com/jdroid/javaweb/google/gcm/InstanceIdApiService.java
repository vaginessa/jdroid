package com.jdroid.javaweb.google.gcm;

import com.jdroid.java.http.DefaultServer;
import com.jdroid.java.http.HttpService;
import com.jdroid.java.http.MimeType;
import com.jdroid.java.http.Server;
import com.jdroid.javaweb.api.ServerApiService;

public class InstanceIdApiService extends ServerApiService {

	public void verify(String instanceIdToken, String googleServerApiKey) {
		HttpService httpService = newGetService(instanceIdToken);
		httpService.addQueryParameter("details", "true");
		httpService.addHeader(HttpService.CONTENT_TYPE_HEADER, MimeType.JSON);
		httpService.addHeader("Authorization", "key=" + googleServerApiKey);
		httpService.setSsl(true);
		httpService.execute(new InstanceIdParser());
	}

	@Override
	protected Server getServer() {
		return new DefaultServer("instanceId", "iid.googleapis.com/iid/info", true);
	}

}