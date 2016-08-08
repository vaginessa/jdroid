package com.jdroid.android.sample.api;

import com.jdroid.android.api.AndroidApiService;
import com.jdroid.android.google.gcm.device.Device;
import com.jdroid.android.google.gcm.device.DeviceMarshaller;
import com.jdroid.java.http.HttpService;
import com.jdroid.java.http.HttpServiceFactory;
import com.jdroid.java.http.okhttp.OkHttpServiceFactory;
import com.jdroid.java.http.post.BodyEnclosingHttpService;
import com.jdroid.java.marshaller.MarshallerProvider;

public class SampleApiService extends AndroidApiService {

	static {
		MarshallerProvider.get().addMarshaller(Device.class, new DeviceMarshaller());
	}

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

	public void addDevice(Device device, Boolean updateLastActiveTimestamp) {
		BodyEnclosingHttpService httpService = newPostService("gcm", "device");
		httpService.addQueryParameter("updateLastActiveTimestamp", updateLastActiveTimestamp);
		marshall(httpService, device);
		httpService.execute();
	}

	public void removeDevice() {
		HttpService httpService = newDeleteService("gcm", "device");
		httpService.execute();
	}

	public void sendPush(String registrationToken) {
		HttpService httpService = newGetService("gcm", "send");
		httpService.addQueryParameter("registrationToken", registrationToken);
		httpService.addQueryParameter("messageKeyExtraName", "messageKey");
		httpService.addQueryParameter("messageKey", "sampleMessage");
		httpService.addQueryParameter("timestampEnabled", "true");
		httpService.execute();
	}

	@Override
	public HttpServiceFactory createHttpServiceFactory() {
		return new OkHttpServiceFactory();
	}
}
