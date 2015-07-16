package com.jdroid.sample.android.api;

import com.jdroid.android.api.AndroidApiService;
import com.jdroid.java.http.HttpService;
import com.jdroid.java.http.HttpServiceFactory;
import com.jdroid.java.http.okhttp.OkHttpServiceFactory;
import com.jdroid.java.http.post.BodyEnclosingHttpService;

public class SampleApiService extends AndroidApiService {

	public SampleResponse httpGetSample() {
		HttpService httpService = newGetService("sample", "get");
		httpService.addQueryParameter("param1", "value1");
		httpService.addHeader("header1", "value1");
		httpService.setUserAgent("sampleUserAgent");
		return httpService.execute(new SampleJsonParser());
	}

	public void httpPostSample() {
		BodyEnclosingHttpService httpService = newPostService("sample", "post");
		httpService.addQueryParameter("param1", "value1");
		httpService.setBody("{}");
		httpService.addHeader("header1", "value1");
		httpService.setUserAgent("sampleUserAgent");
		httpService.execute();
	}

	public void httpPutSample() {
		BodyEnclosingHttpService httpService = newPutService("sample", "put");
		httpService.addQueryParameter("param1", "value1");
		httpService.setBody("{}");
		httpService.addHeader("header1", "value1");
		httpService.setUserAgent("sampleUserAgent");
		httpService.execute();
	}

	public void httpDeleteSample() {
		HttpService httpService = newDeleteService("sample", "delete");
		httpService.addQueryParameter("param1", "value1");
		httpService.addHeader("header1", "value1");
		httpService.setUserAgent("sampleUserAgent");
		httpService.execute();
	}

	public void httpPatchSample() {
		BodyEnclosingHttpService httpService = newPatchService("sample", "patch");
		httpService.addQueryParameter("param1", "value1");
		httpService.setBody("{}");
		httpService.addHeader("header1", "value1");
		httpService.setUserAgent("sampleUserAgent");
		httpService.execute();
	}

	@Override
	public HttpServiceFactory createHttpServiceFactory() {
		return new OkHttpServiceFactory();
	}
}
