package com.jdroid.android.google.admob;

import com.jdroid.android.application.AbstractAppModule;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.debug.PreferencesAppender;

import java.util.List;

public class AdMobAppModule extends AbstractAppModule {

	public static final String MODULE_NAME = AdMobAppModule.class.getName();

	public static AdMobAppModule get() {
		return (AdMobAppModule)AbstractApplication.get().getAppModule(MODULE_NAME);
	}

	private AdMobAppContext adMobAppContext;
	private AdMobDebugContext adMobDebugContext;

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
		return getGcmDebugContext().getPreferencesAppenders();
	}

	public AdMobDebugContext getGcmDebugContext() {
		synchronized (AbstractApplication.class) {
			if (adMobDebugContext == null) {
				adMobDebugContext = createAdMobDebugContext();
			}
		}
		return adMobDebugContext;
	}

	protected AdMobDebugContext createAdMobDebugContext() {
		return new AdMobDebugContext();
	}
}