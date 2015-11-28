package com.jdroid.java.http.okhttp;

import com.squareup.okhttp.Response;

import java.io.IOException;

public class ExecuteRequestCommand extends OkHttpCommand<OkHttpService, OkHttpResponseWrapper> {

	@Override
	protected OkHttpResponseWrapper doExecute(OkHttpService okHttpService) throws IOException {
		Response response = okHttpService.client.newCall(okHttpService.request).execute();
		return new OkHttpResponseWrapper(response);
	}
}
