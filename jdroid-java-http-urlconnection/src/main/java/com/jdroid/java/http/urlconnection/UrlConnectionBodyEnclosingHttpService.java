package com.jdroid.java.http.urlconnection;

import com.jdroid.java.http.HttpServiceProcessor;
import com.jdroid.java.http.Server;
import com.jdroid.java.http.post.BodyEnclosingHttpService;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.List;

public abstract class UrlConnectionBodyEnclosingHttpService extends UrlConnectionHttpService implements BodyEnclosingHttpService {

	private String body;

	public UrlConnectionBodyEnclosingHttpService(Server server, List<Object> urlSegments, List<HttpServiceProcessor> httpServiceProcessors) {
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
