package com.jdroid.android.debug.firebase.instanceid;

import android.content.Context;

import com.jdroid.android.firebase.instanceid.InstanceIdHelper;
import com.jdroid.android.lifecycle.ApplicationLifecycleCallback;
import com.jdroid.java.concurrent.ExecutorUtils;

public class FirebaseInstanceIdDebugAppLifecycleCallback extends ApplicationLifecycleCallback {
	
	@Override
	public void onCreate(Context context) {
		// Load properties on a worker thread
		ExecutorUtils.execute(new Runnable() {
			@Override
			public void run() {
				InstanceIdHelper.getInstanceId();
			}
		});
	}
}
