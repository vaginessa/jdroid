package com.jdroid.java.http.urlconnection;

import com.jdroid.java.http.HttpResponseWrapper;
import com.jdroid.java.http.WebService;
import com.jdroid.java.utils.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.zip.GZIPInputStream;

public class UrlConnectionHttpResponseWrapper extends HttpResponseWrapper {

	private HttpURLConnection httpURLConnection;

	private String statusReason;
	private int statusCode;
	private InputStream inputStream;

	public UrlConnectionHttpResponseWrapper(HttpURLConnection httpURLConnection) throws IOException {
		this.httpURLConnection = httpURLConnection;

		inputStream = httpURLConnection.getInputStream();

		String contentEncoding = getHeader(WebService.CONTENT_ENCODING_HEADER);
		if (inputStream != null && (contentEncoding != null) && contentEncoding.equalsIgnoreCase(WebService.GZIP_ENCODING)) {
			inputStream = new GZIPInputStream(inputStream);
		}

		statusCode = httpURLConnection.getResponseCode();
		statusReason = httpURLConnection.getResponseMessage();
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
		return statusCode;
	}

	@Override
	public String getStatusReason() {
		return statusReason;
	}

	@Override
	public String getHeader(String name) {
		return httpURLConnection.getHeaderField(name);
	}

	@Override
	public InputStream getInputStream() {
		return inputStream;
	}
}
