package com.jdroid.java.http.okhttp;

import com.jdroid.java.http.HttpResponseWrapper;
import com.jdroid.java.http.HttpService;
import com.jdroid.java.utils.FileUtils;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public class OkHttpResponseWrapper extends HttpResponseWrapper {

	private Response response;

	private InputStream inputStream;

	public OkHttpResponseWrapper(Response response) throws IOException {
		this.response = response;
		if (response.code() != 204) {
			inputStream = response.body().byteStream();
			String contentEncoding = response.header(HttpService.CONTENT_ENCODING_HEADER);
			if (inputStream != null && (contentEncoding != null) && contentEncoding.equalsIgnoreCase(HttpService.GZIP_ENCODING)) {
				inputStream = new GZIPInputStream(inputStream);
			}
		}
	}

	@Override
	public String getContent() {
		String content = null;
		try {
			if (inputStream != null) {
				content = FileUtils.toString(inputStream);
			}
		} finally {
			FileUtils.safeClose(inputStream);
		}
		return content;
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
		return inputStream;
	}
}
