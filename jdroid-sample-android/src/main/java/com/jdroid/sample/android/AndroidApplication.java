package com.jdroid.sample.android;

import android.app.Activity;
import android.support.v4.app.Fragment;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.activity.ActivityHelper;
import com.jdroid.android.analytics.AnalyticsSender;
import com.jdroid.android.analytics.AnalyticsTracker;
import com.jdroid.android.fragment.AbstractPreferenceFragment;
import com.jdroid.android.fragment.FragmentHelper;
import com.jdroid.android.gcm.GcmMessageResolver;
import com.jdroid.sample.android.analytics.AndroidAnalyticsSender;
import com.jdroid.sample.android.gcm.AndroidGcmResolver;
import com.jdroid.sample.android.ui.HomeActivity;
import com.jdroid.sample.android.ui.debug.AndroidDebugSettingsFragment;

public class AndroidApplication extends AbstractApplication {
	
	public static AndroidApplication get() {
		return (AndroidApplication)AbstractApplication.INSTANCE;
	}
	
	/**
	 * @see com.jdroid.android.AbstractApplication#getHomeActivityClass()
	 */
	@Override
	public Class<? extends Activity> getHomeActivityClass() {
		return HomeActivity.class;
	}
	
	/**
	 * @see com.jdroid.android.AbstractApplication#createActivityHelper(com.jdroid.android.activity.AbstractFragmentActivity)
	 */
	@Override
	public ActivityHelper createActivityHelper(AbstractFragmentActivity activity) {
		return new AndroidActivityHelper(activity);
	}
	
	/**
	 * @see com.jdroid.android.AbstractApplication#createFragmentHelper(android.support.v4.app.Fragment)
	 */
	@Override
	public FragmentHelper createFragmentHelper(Fragment fragment) {
		return new AndroidFragmentHelper(fragment);
	}
	
	/**
	 * @see com.jdroid.android.AbstractApplication#getGcmResolver()
	 */
	@Override
	public GcmMessageResolver getGcmResolver() {
		return AndroidGcmResolver.get();
	}
	
	/**
	 * @see com.jdroid.android.AbstractApplication#createAnalyticsSender()
	 */
	@Override
	protected AnalyticsSender<? extends AnalyticsTracker> createAnalyticsSender() {
		return AndroidAnalyticsSender.get();
	}
	
	/**
	 * @see com.jdroid.android.AbstractApplication#getDebugSettingsFragmentClass()
	 */
	@Override
	public Class<? extends AbstractPreferenceFragment> getDebugSettingsFragmentClass() {
		return AndroidDebugSettingsFragment.class;
	}
}
