package com.jdroid.android.google.admob;

import com.jdroid.android.application.AbstractAppModule;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.debug.PreferencesAppender;
import com.jdroid.java.collections.Lists;

import java.util.List;

public class AdMobAppModule extends AbstractAppModule {

	public static final String MODULE_NAME = AdMobAppModule.class.getName();

	public static AdMobAppModule get() {
		return (AdMobAppModule)AbstractApplication.get().getAppModule(MODULE_NAME);
	}

	private AdMobAppContext adMobAppContext;

	public AdMobAppModule() {
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