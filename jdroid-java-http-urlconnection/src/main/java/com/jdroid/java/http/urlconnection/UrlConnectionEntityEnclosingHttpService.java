package com.jdroid.java.http.urlconnection;

import com.jdroid.java.http.HttpServiceProcessor;
import com.jdroid.java.http.Server;
import com.jdroid.java.http.post.EntityEnclosingHttpService;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.List;

public abstract class UrlConnectionEntityEnclosingHttpService extends UrlConnectionHttpService implements EntityEnclosingHttpService {

	private String body;

	public UrlConnectionEntityEnclosingHttpService(Server server, List<Object> urlSegments, List<HttpServiceProcessor> httpServiceProcessors) {
		super(server, urlSegments, httpServiceProcessors);
	}

	@Override
	protected void onConfigureUrlConnection(HttpURLConnection httpURLConnection) throws IOException {
		if (body != null) {
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setFixedLengthStreamingMode(body.getBytes().length);
			OutputStream out = new BufferedOutputStream(httpURLConnection.getOutputStream());
			out.write(body.getBytes());
			out.close();
		}
	}

	@Override
	public void setBody(String body) {
		this.body = body;
	}
}
