package com.jdroid.android.sample.application;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.jdroid.android.about.AboutContext;
import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.activity.ActivityHelper;
import com.jdroid.android.analytics.AnalyticsSender;
import com.jdroid.android.analytics.AnalyticsTracker;
import com.jdroid.android.analytics.ExperimentHelper;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.application.AppModule;
import com.jdroid.android.context.AppContext;
import com.jdroid.android.crashlytics.CrashlyticsAppModule;
import com.jdroid.android.debug.DebugContext;
import com.jdroid.android.facebook.FacebookAppModule;
import com.jdroid.android.fragment.FragmentHelper;
import com.jdroid.android.google.admob.AdMobAppModule;
import com.jdroid.android.google.gcm.GcmContext;
import com.jdroid.android.repository.UserRepository;
import com.jdroid.android.sample.analytics.AndroidAnalyticsSender;
import com.jdroid.android.sample.debug.AndroidDebugContext;
import com.jdroid.android.sample.experiment.AndroidExperiment;
import com.jdroid.android.sample.gcm.AndroidGcmContext;
import com.jdroid.android.sample.repository.UserRepositoryImpl;
import com.jdroid.android.sample.ui.AndroidActivityHelper;
import com.jdroid.android.sample.ui.AndroidFragmentHelper;
import com.jdroid.android.sample.ui.about.AndroidAboutContext;
import com.jdroid.android.sample.ui.ads.AdsUriHandler;
import com.jdroid.android.sample.ui.home.HomeActivity;
import com.jdroid.android.uri.NoSegmentsUriHandler;

import java.util.List;

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

	@Override
	public Class<? extends Activity> getHomeActivityClass() {
		return HomeActivity.class;
	}

	@NonNull
	@Override
	protected AppContext createAppContext() {
		return new AndroidAppContext();
	}

	@Override
	public ActivityHelper createActivityHelper(AbstractFragmentActivity activity) {
		return new AndroidActivityHelper(activity);
	}
	
	@Override
	public FragmentHelper createFragmentHelper(Fragment fragment) {
		return new AndroidFragmentHelper(fragment);
	}
	
	@NonNull
	@Override
	protected AnalyticsSender<? extends AnalyticsTracker> createAnalyticsSender() {
		return AndroidAnalyticsSender.get();
	}

	@Override
	protected DebugContext createDebugContext() {
		return new AndroidDebugContext();
	}

	@Override
	protected GcmContext createGcmContext() {
		return new AndroidGcmContext();
	}

	@Override
	protected AboutContext createAboutContext() {
		return new AndroidAboutContext();
	}

	@Override
	public UserRepository getUserRepository() {
		return new UserRepositoryImpl();
	}

	@Override
	protected void initAppModule(List<AppModule> appModules) {
		appModules.add(CrashlyticsAppModule.get());
		appModules.add(AdMobAppModule.get());
		appModules.add(FacebookAppModule.get());
	}
}
