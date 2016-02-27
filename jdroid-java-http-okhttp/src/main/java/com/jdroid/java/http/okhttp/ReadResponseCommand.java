package com.jdroid.java.http.okhttp;

import com.jdroid.java.http.HttpService;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import okhttp3.Response;

public class ReadResponseCommand extends OkHttpCommand<Response, InputStream> {

	@Override
	protected InputStream doExecute(Response response) throws IOException {
		InputStream inputStream = null;
		if (response.code() != 204) {
			inputStream = response.body().byteStream();
			String contentEncoding = response.header(HttpService.CONTENT_ENCODING_HEADER);
			if (inputStream != null && (contentEncoding != null) && contentEncoding.equalsIgnoreCase(HttpService.GZIP_ENCODING)) {
				inputStream = new GZIPInputStream(inputStream);
			}
		}
		return inputStream;
	}
}
