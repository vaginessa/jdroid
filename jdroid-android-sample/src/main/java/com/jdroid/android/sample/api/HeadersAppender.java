package com.jdroid.android.sample.api;

import com.jdroid.android.http.AbstractHeaderAppender;

public class HeadersAppender extends AbstractHeaderAppender {
	
	private static final HeadersAppender INSTANCE = new HeadersAppender();
	
	private HeadersAppender() {
		// nothing...
	}
	
	public static HeadersAppender get() {
		return INSTANCE;
	}
	
}
