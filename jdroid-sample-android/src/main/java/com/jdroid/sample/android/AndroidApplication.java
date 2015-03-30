package com.jdroid.sample.android;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.jdroid.android.AbstractApplication;
import com.jdroid.android.about.SpreadTheLoveFragment;
import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.activity.ActivityHelper;
import com.jdroid.android.analytics.AnalyticsSender;
import com.jdroid.android.analytics.AnalyticsTracker;
import com.jdroid.android.analytics.ExperimentHelper;
import com.jdroid.android.context.AppContext;
import com.jdroid.android.debug.DebugContext;
import com.jdroid.android.fragment.FragmentHelper;
import com.jdroid.android.gcm.GcmMessageResolver;
import com.jdroid.android.uri.NoSegmentsUriHandler;
import com.jdroid.sample.android.analytics.AndroidAnalyticsSender;
import com.jdroid.sample.android.debug.AndroidDebugContext;
import com.jdroid.sample.android.experiment.AndroidExperiment;
import com.jdroid.sample.android.gcm.AndroidGcmResolver;
import com.jdroid.sample.android.ui.HomeActivity;
import com.jdroid.sample.android.ui.about.AndroidSpreadTheLoveFragment;
import com.jdroid.sample.android.uri.AdsUriHandler;

public class AndroidApplication extends AbstractApplication {
	
	public static AndroidApplication get() {
		return (AndroidApplication)AbstractApplication.INSTANCE;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		getUriMapper().addUriHandler(new NoSegmentsUriHandler());
		getUriMapper().addUriHandler(new AdsUriHandler());

		ExperimentHelper.registerExperiment(AndroidExperiment.SAMPLE_EXPERIMENT);
	}

	/**
	 * @see com.jdroid.android.AbstractApplication#getHomeActivityClass()
	 */
	@Override
	public Class<? extends Activity> getHomeActivityClass() {
		return HomeActivity.class;
	}

	@Override
	protected AppContext createAppContext() {
		return new AndroidAppContext();
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

	@Override
	protected DebugContext createDebugContext() {
		return new AndroidDebugContext();
	}

	/**
	 * @see com.jdroid.android.AbstractApplication#getSpreadTheLoveFragmentClass()
	 */
	@Override
	public Class<? extends SpreadTheLoveFragment> getSpreadTheLoveFragmentClass() {
		return AndroidSpreadTheLoveFragment.class;
	}

	@Override
	public Boolean isImageLoaderEnabled() {
		return true;
	}
}
