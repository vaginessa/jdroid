package com.jdroid.java.http.okhttp;


import java.io.IOException;

import okhttp3.Response;

public class ExecuteRequestCommand extends OkHttpCommand<OkHttpService, OkHttpResponseWrapper> {

	@Override
	protected OkHttpResponseWrapper doExecute(OkHttpService okHttpService) throws IOException {
		Response response = okHttpService.client.newCall(okHttpService.request).execute();
		return new OkHttpResponseWrapper(response);
	}
}
