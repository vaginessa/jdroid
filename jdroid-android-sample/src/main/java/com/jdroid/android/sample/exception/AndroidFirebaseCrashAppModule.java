package com.jdroid.android.sample.exception;

import com.jdroid.android.firebase.crash.FirebaseCrashAppContext;
import com.jdroid.android.firebase.crash.FirebaseCrashAppModule;

public class AndroidFirebaseCrashAppModule extends FirebaseCrashAppModule {

	@Override
	protected FirebaseCrashAppContext createFirebaseCrashAppContext() {
		return new AndroidFirebaseCrashAppContext();
	}
}
