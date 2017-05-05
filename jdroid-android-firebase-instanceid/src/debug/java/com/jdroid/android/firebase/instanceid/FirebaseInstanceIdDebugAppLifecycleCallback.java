package com.jdroid.android.firebase.instanceid;

import android.content.Context;
import android.support.v4.util.Pair;

import com.jdroid.android.debug.info.DebugInfoAppender;
import com.jdroid.android.debug.info.DebugInfoHelper;
import com.jdroid.android.lifecycle.ApplicationLifecycleCallback;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.concurrent.ExecutorUtils;

import java.util.List;

public class FirebaseInstanceIdDebugAppLifecycleCallback extends ApplicationLifecycleCallback {
	
	@Override
	public void onCreate(Context context) {
		DebugInfoHelper.addDebugInfoAppender(new DebugInfoAppender() {
			@Override
			public List<Pair<String, Object>> getDebugInfoProperties() {
				List<Pair<String, Object>> properties = Lists.newArrayList();
				properties.add(new Pair<String, Object>("Instance ID", InstanceIdHelper.getInstanceId()));
				return properties;
			}
		});
		// Load properties on a worker thread
		ExecutorUtils.execute(new Runnable() {
			@Override
			public void run() {
				InstanceIdHelper.getInstanceId();
			}
		});
	}
}
