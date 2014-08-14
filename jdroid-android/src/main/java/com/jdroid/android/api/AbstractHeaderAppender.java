package com.jdroid.android.api;

import com.jdroid.android.AbstractApplication;
import com.jdroid.android.context.SecurityContext;
import com.jdroid.android.domain.User;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.java.http.HttpResponseWrapper;
import com.jdroid.java.http.HttpWebServiceProcessor;
import com.jdroid.java.http.MimeType;
import com.jdroid.java.http.WebService;

public class AbstractHeaderAppender implements HttpWebServiceProcessor {
	
	private static final String API_VERSION_HEADER = "api-version";
	
	private static final String USER_AGENT_HEADER_VALUE = "android";
	
	public static final String USER_TOKEN_HEADER = "x-user-token";
	public static final String DEVICE_ID_HEADER = "deviceId";
	public static final String CLIENT_APP_VERSION_HEADER = "clientAppVersion";
	public static final String CLIENT_OS_VERSION_HEADER = "clientOsVersion";
	
	/**
	 * @see com.jdroid.java.http.HttpWebServiceProcessor#beforeExecute(com.jdroid.java.http.WebService)
	 */
	@Override
	public void beforeExecute(WebService webService) {
		
		// User Agent header
		webService.setUserAgent(USER_AGENT_HEADER_VALUE);
		
		// API Version header
		webService.addHeader(API_VERSION_HEADER, AbstractApplication.get().getAppContext().getServerApiVersion());
		
		webService.addHeader(WebService.CONTENT_TYPE_HEADER, MimeType.JSON_UTF8);
		webService.addHeader(WebService.ACCEPT_HEADER, MimeType.JSON);
		webService.addHeader(WebService.ACCEPT_ENCODING_HEADER, WebService.GZIP_ENCODING);
		
		// User Token
		User user = SecurityContext.get().getUser();
		if (user != null) {
			webService.addHeader(USER_TOKEN_HEADER, user.getUserToken());
		}
		
		webService.addHeader(DEVICE_ID_HEADER, AndroidUtils.getDeviceUUID());
		webService.addHeader(CLIENT_APP_VERSION_HEADER, AndroidUtils.getVersionCode().toString());
		webService.addHeader(CLIENT_OS_VERSION_HEADER, AndroidUtils.getApiLevel().toString());
	}
	
	/**
	 * @see com.jdroid.java.http.HttpWebServiceProcessor#afterExecute(com.jdroid.java.http.WebService,
	 *      com.jdroid.java.http.HttpResponseWrapper)
	 */
	@Override
	public void afterExecute(WebService webService, HttpResponseWrapper httpResponse) {
		// Do Nothing
	}
	
}
