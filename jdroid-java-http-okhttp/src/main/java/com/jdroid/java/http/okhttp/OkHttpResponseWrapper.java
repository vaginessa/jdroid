package com.jdroid.java.http.okhttp;

import com.jdroid.java.http.HttpResponseWrapper;

import java.io.InputStream;

import okhttp3.Response;

public class OkHttpResponseWrapper extends HttpResponseWrapper {

	private Response response;
	private InputStream inputStream;

	public OkHttpResponseWrapper(Response response) {
		this.response = response;
	}

	@Override
	public int getStatusCode() {
		return response.code();
	}

	@Override
	public String getStatusReason() {
		return response.message();
	}

	@Override
	public String getHeader(String name) {
		return response.header(name);
	}

	@Override
	public InputStream getInputStream() {
		if (inputStream == null) {
			inputStream = new ReadResponseCommand().execute(response);
		}
		return inputStream;
	}

	public Response getResponse() {
		return response;
	}
}
