package com.jdroid.android.google.admob;

import com.jdroid.android.debug.PreferencesAppender;
import com.jdroid.java.collections.Lists;

import java.util.List;

public class AdMobDebugContext {

	public List<PreferencesAppender> getPreferencesAppenders() {
		List<PreferencesAppender> appenders = Lists.newArrayList();
		appenders.add(new AdsDebugPrefsAppender());
		return appenders;
	}
}
