package com.jdroid.android;

import com.jdroid.android.debug.DebugContext;

public class TestDebugContext extends DebugContext {

	public static Boolean HTTP_MOCK_ENABLED = true;

	@Override
	public Boolean isHttpMockEnabled() {
		return HTTP_MOCK_ENABLED;
	}
}
