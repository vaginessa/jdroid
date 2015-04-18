package com.jdroid.sample.android.api;

import com.jdroid.android.api.AndroidApiService;
import com.jdroid.java.http.WebService;

public class SampleApiService extends AndroidApiService {

	public SampleResponse httpGetSample() {
		WebService webService = newGetService("sample", "get");
		webService.addQueryParameter("param1", "value1");
		webService.addHeader("header1", "value1");
		webService.setUserAgent("sampleUserAgent");
		return webService.execute(new SampleJsonParser());
	}

	public void httpPostSample() {
		WebService webService = newPostService("sample", "post");
		webService.addQueryParameter("param1", "value1");
		webService.addHeader("header1", "value1");
		webService.setUserAgent("sampleUserAgent");
		webService.execute();
	}

	public void httpPutSample() {
		WebService webService = newPutService("sample", "put");
		webService.addQueryParameter("param1", "value1");
		webService.addHeader("header1", "value1");
		webService.setUserAgent("sampleUserAgent");
		webService.execute();
	}

	public void httpDeleteSample() {
		WebService webService = newDeleteService("sample", "delete");
		webService.addQueryParameter("param1", "value1");
		webService.addHeader("header1", "value1");
		webService.setUserAgent("sampleUserAgent");
		webService.execute();
	}

	public void httpPatchSample() {
		WebService webService = newPatchService("sample", "patch");
		webService.addQueryParameter("param1", "value1");
		webService.addHeader("header1", "value1");
		webService.setUserAgent("sampleUserAgent");
		webService.execute();
	}
}
