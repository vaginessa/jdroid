package com.jdroid.android.google.admob;

import android.support.v4.app.Fragment;

import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.activity.ActivityDelegate;
import com.jdroid.android.application.AbstractAppModule;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.debug.PreferencesAppender;
import com.jdroid.android.fragment.FragmentDelegate;

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

	@Override
	public ActivityDelegate createActivityDelegate(AbstractFragmentActivity abstractFragmentActivity) {
		return new AdMobActivityDelegate(abstractFragmentActivity);
	}

	@Override
	public FragmentDelegate createFragmentDelegate(Fragment fragment) {
		return new AdMobFragmentDelegate(fragment);
	}
}