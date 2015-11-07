package com.jdroid.javaweb.google.gcm;

import com.jdroid.java.collections.Lists;
import com.jdroid.java.http.DefaultServer;
import com.jdroid.java.http.HttpService;
import com.jdroid.java.http.HttpServiceProcessor;
import com.jdroid.java.http.MimeType;
import com.jdroid.java.http.Server;
import com.jdroid.java.http.post.BodyEnclosingHttpService;
import com.jdroid.java.marshaller.MarshallerProvider;
import com.jdroid.javaweb.api.ServerApiService;

import java.util.HashMap;
import java.util.List;

public class GcmApiService extends ServerApiService {

	static {
		MarshallerProvider.get().addMarshaller(GcmMessage.class, new GcmMessageMarshaller());
		MarshallerProvider.get().addMarshaller(HashMap.class, new GcmDataMarshaller());
	}

	public GcmResponse sendMessage(GcmMessage gcmMessage, String googleServerApiKey) {
		BodyEnclosingHttpService httpService = newPostService("send");
		httpService.addHeader(HttpService.CONTENT_TYPE_HEADER, MimeType.JSON);
		httpService.addHeader("Authorization", "key=" + googleServerApiKey);
		httpService.setSsl(true);
		marshall(httpService, gcmMessage);
		return httpService.execute(new GcmResponseParser());
	}

	@Override
	protected Server getServer() {
		return new DefaultServer("gcm", "gcm-http.googleapis.com/gcm", true);
		//return new DefaultServer("gcm", "localhost:8081/service/api/gcm", false);
	}

	@Override
	protected List<HttpServiceProcessor> getHttpServiceProcessors() {
		return Lists.<HttpServiceProcessor>newArrayList(GcmHttpResponseValidator.get());
	}
}
