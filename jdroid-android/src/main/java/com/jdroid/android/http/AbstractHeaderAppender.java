package com.jdroid.android.http;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.context.SecurityContext;
import com.jdroid.android.domain.User;
import com.jdroid.android.google.instanceid.InstanceIdHelper;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.java.http.HttpResponseWrapper;
import com.jdroid.java.http.HttpService;
import com.jdroid.java.http.HttpServiceProcessor;
import com.jdroid.java.http.MimeType;
import com.jdroid.java.utils.LocaleUtils;

public class AbstractHeaderAppender implements HttpServiceProcessor {
	
	private static final String API_VERSION_HEADER = "api-version";

	private static final String USER_AGENT_HEADER_VALUE = "android";
	
	public static final String USER_TOKEN_HEADER = "x-user-token";
	public static final String INSTANCE_ID_HEADER = "instanceId";
	public static final String CLIENT_APP_VERSION_HEADER = "clientAppVersion";
	public static final String CLIENT_OS_VERSION_HEADER = "clientOsVersion";

	@Override
	public void onInit(HttpService httpService) {
		// Do Nothing
	}

	/**
	 * @see HttpServiceProcessor#beforeExecute(HttpService)
	 */
	@Override
	public void beforeExecute(HttpService httpService) {
		
		// User Agent header
		httpService.setUserAgent(USER_AGENT_HEADER_VALUE);

		addLanguageHeader(httpService);
		addApiVersionHeader(httpService);
		addUserTokenHeader(httpService);

		httpService.addHeader(HttpService.CONTENT_TYPE_HEADER, MimeType.JSON_UTF8);
		httpService.addHeader(HttpService.ACCEPT_HEADER, MimeType.JSON);
		httpService.addHeader(HttpService.ACCEPT_ENCODING_HEADER, HttpService.GZIP_ENCODING);

		httpService.addHeader(INSTANCE_ID_HEADER, InstanceIdHelper.getInstanceId());
		httpService.addHeader(CLIENT_APP_VERSION_HEADER, AndroidUtils.getVersionCode().toString());
		httpService.addHeader(CLIENT_OS_VERSION_HEADER, AndroidUtils.getApiLevel().toString());
	}

	protected void addLanguageHeader(HttpService httpService) {
		httpService.addHeader(HttpService.ACCEPT_LANGUAGE_HEADER,  LocaleUtils.getAcceptLanguage());
	}

	protected void addApiVersionHeader(HttpService httpService) {
		httpService.addHeader(API_VERSION_HEADER, AbstractApplication.get().getAppContext().getServerApiVersion());
	}

	protected void addUserTokenHeader(HttpService httpService) {
		User user = SecurityContext.get().getUser();
		if (user != null) {
			httpService.addHeader(USER_TOKEN_HEADER, user.getUserToken());
		}
	}


	/**
	 * @see HttpServiceProcessor#afterExecute(HttpService,
	 *      com.jdroid.java.http.HttpResponseWrapper)
	 */
	@Override
	public void afterExecute(HttpService httpService, HttpResponseWrapper httpResponse) {
		// Do Nothing
	}
	
}
