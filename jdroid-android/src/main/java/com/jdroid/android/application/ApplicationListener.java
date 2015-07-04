package com.jdroid.android.application;

import android.content.Context;
import android.content.res.Configuration;

public interface ApplicationListener {

	public void onCreate();

	public void onConfigurationChanged(Configuration newConfig);

	public void onLowMemory();

	public void onTrimMemory(int level);

	public void attachBaseContext(Context base);
}
