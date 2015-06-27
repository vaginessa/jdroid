package com.jdroid.java.http.urlconnection;

import com.jdroid.java.http.HttpWebServiceProcessor;
import com.jdroid.java.http.Server;
import com.jdroid.java.http.post.EntityEnclosingWebService;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.List;

public abstract class UrlConnectionEntityEnclosingWebService extends UrlConnectionHttpWebService implements EntityEnclosingWebService {

	private String entity;

	public UrlConnectionEntityEnclosingWebService(Server server, List<Object> urlSegments, List<HttpWebServiceProcessor> httpWebServiceProcessors) {
		super(server, urlSegments, httpWebServiceProcessors);
	}

	@Override
	protected void onConfigureUrlConnection(HttpURLConnection httpURLConnection) throws IOException {
		if (entity != null) {
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setFixedLengthStreamingMode(entity.getBytes().length);
			OutputStream out = new BufferedOutputStream(httpURLConnection.getOutputStream());
			out.write(entity.getBytes());
			out.close();
		}
	}

	@Override
	public void setEntity(String content) {
		this.entity = content;
	}
}
