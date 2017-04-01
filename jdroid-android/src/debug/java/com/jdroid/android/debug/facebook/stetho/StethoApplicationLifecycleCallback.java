package com.jdroid.android.debug.facebook.stetho;

import android.content.Context;
import android.util.Log;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.application.lifecycle.ApplicationLifecycleCallback;
import com.jdroid.android.context.BuildConfigUtils;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.http.HttpServiceFactory;
import com.jdroid.java.utils.ReflectionUtils;

public class StethoApplicationLifecycleCallback extends ApplicationLifecycleCallback {

	private static final String OKHTTP_SERVICE_FACTORY = "com.jdroid.java.http.okhttp.OkHttpServiceFactory";
	private static final String OKHTTP_INTERCEPTOR = "okhttp3.Interceptor";
	private static final String STETHO_OKHTTP_INTERCEPTOR = "com.facebook.stetho.okhttp3.StethoInterceptor";
	private static final String ADD_NETWORK_INTERCEPTOR = "addNetworkInterceptor";
	
	@Override
	public void onProviderInit(Context context) {
		try {
			if (BuildConfigUtils.getBuildConfigBoolean("STETHO_ENABLED", false)) {
				if (BuildConfigUtils.getBuildConfigBoolean("JDROID_JAVA_OKHTTP_ENABLED", false)) {
					HttpServiceFactory okhttpServiceFactory = ReflectionUtils.newInstance(OKHTTP_SERVICE_FACTORY);
					Class<?> interceptorClass = ReflectionUtils.getClass(OKHTTP_INTERCEPTOR);
					Object stethoInterceptor = ReflectionUtils.newInstance(STETHO_OKHTTP_INTERCEPTOR);
					ReflectionUtils.invokeMethod(OKHTTP_SERVICE_FACTORY, okhttpServiceFactory, ADD_NETWORK_INTERCEPTOR,
							Lists.<Class<?>>newArrayList(interceptorClass), Lists.newArrayList(stethoInterceptor));
					AbstractApplication.get().setHttpServiceFactory(okhttpServiceFactory);
				}
				AbstractApplication.get().addAppModulesMap(StethoAppModule.class.getSimpleName(), new StethoAppModule());
			}
		} catch (Exception e) {
			Log.e(StethoApplicationLifecycleCallback.class.getName(), "Error initializing StethoInitProvider", e);
		}
	}

}
