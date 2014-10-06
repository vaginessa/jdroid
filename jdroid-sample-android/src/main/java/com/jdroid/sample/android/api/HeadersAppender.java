package com.jdroid.sample.android.api;

import com.jdroid.android.api.AbstractHeaderAppender;

public class HeadersAppender extends AbstractHeaderAppender {
	
	private static final HeadersAppender INSTANCE = new HeadersAppender();
	
	private HeadersAppender() {
		// nothing...
	}
	
	public static HeadersAppender get() {
		return INSTANCE;
	}
	
}
