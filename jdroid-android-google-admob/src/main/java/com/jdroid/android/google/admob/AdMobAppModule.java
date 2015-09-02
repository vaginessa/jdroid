package com.jdroid.android.google.admob;

import com.jdroid.android.application.AbstractAppModule;
import com.jdroid.android.debug.PreferencesAppender;
import com.jdroid.java.collections.Lists;

import java.util.List;

public class AdMobAppModule extends AbstractAppModule {

	private static final AdMobAppModule INSTANCE = new AdMobAppModule();

	private AdMobAppContext adMobAppContext;

	public static AdMobAppModule get() {
		return INSTANCE;
	}

	private AdMobAppModule() {
		adMobAppContext = createAdMobAppContext();
	}

	protected AdMobAppContext createAdMobAppContext() {
		return new AdMobAppContext();
	}

	public AdMobAppContext getAdMobAppContext() {
		return adMobAppContext;
	}

	@Override
	public List<PreferencesAppender> getPreferencesAppenders() {
		List<PreferencesAppender> appenders = Lists.newArrayList();
		appenders.add(createAdsDebugPrefsAppender());
		return appenders;
	}

	protected AdsDebugPrefsAppender createAdsDebugPrefsAppender() {
		return new AdsDebugPrefsAppender();
	}
}