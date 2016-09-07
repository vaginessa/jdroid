package com.jdroid.android.sample.application;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.v4.app.Fragment;

import com.firebase.client.Firebase;
import com.jdroid.android.about.AboutAppModule;
import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.activity.ActivityHelper;
import com.jdroid.android.analytics.AnalyticsSender;
import com.jdroid.android.analytics.AnalyticsTracker;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.application.AppModule;
import com.jdroid.android.context.AppContext;
import com.jdroid.android.crashlytics.CrashlyticsAppModule;
import com.jdroid.android.debug.DebugContext;
import com.jdroid.android.experiments.ExperimentHelper;
import com.jdroid.android.facebook.FacebookAppModule;
import com.jdroid.android.fragment.FragmentHelper;
import com.jdroid.android.google.admob.AdMobAppModule;
import com.jdroid.android.google.analytics.GoogleAnalyticsAppModule;
import com.jdroid.android.google.analytics.GoogleAnalyticsTracker;
import com.jdroid.android.firebase.FirebaseAppModule;
import com.jdroid.android.firebase.fcm.AbstractFcmAppModule;
import com.jdroid.android.repository.UserRepository;
import com.jdroid.android.sample.BuildConfig;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.analytics.AndroidFirebaseAnalyticsTracker;
import com.jdroid.android.sample.analytics.AndroidGoogleAnalyticsTracker;
import com.jdroid.android.sample.analytics.AppAnalyticsSender;
import com.jdroid.android.sample.analytics.AppAnalyticsTracker;
import com.jdroid.android.sample.debug.AndroidDebugContext;
import com.jdroid.android.sample.exception.AndroidCrashlyticsAppModule;
import com.jdroid.android.sample.experiment.AndroidExperiment;
import com.jdroid.android.sample.fcm.AndroidFcmAppModule;
import com.jdroid.android.sample.repository.UserRepositoryImpl;
import com.jdroid.android.sample.ui.AndroidActivityHelper;
import com.jdroid.android.sample.ui.AndroidFragmentHelper;
import com.jdroid.android.sample.ui.about.AndroidAboutAppModule;
import com.jdroid.android.sample.ui.ads.SampleAdMobAppModule;
import com.jdroid.android.sample.ui.firebase.AndroidFirebaseAppModule;
import com.jdroid.android.sample.ui.home.HomeActivity;
import com.jdroid.android.sample.ui.sqlite.SampleSQLiteEntity;
import com.jdroid.android.sample.ui.sqlite.SampleSQLiteRepository;
import com.jdroid.android.sample.ui.uri.SampleUriWatcher;
import com.jdroid.android.sqlite.SQLiteHelper;
import com.jdroid.android.twitter.TwitterAppModule;
import com.jdroid.java.domain.Identifiable;
import com.jdroid.java.repository.Repository;

import java.util.List;
import java.util.Map;

public class AndroidApplication extends AbstractApplication {
	
	public static AndroidApplication get() {
		return (AndroidApplication)AbstractApplication.INSTANCE;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		getUriMapper().addUriWatcher(new SampleUriWatcher());

		ExperimentHelper.registerExperiments(AndroidExperiment.SAMPLE_EXPERIMENT);

		Firebase.setAndroidContext(this);

		if (AbstractApplication.get().getAppContext().isGoogleAnalyticsEnabled()) {
			GoogleAnalyticsAppModule.get().getGoogleAnalyticsHelper().addCustomDimensionDefinition(GoogleAnalyticsTracker.CustomDimension.INSTALLATION_SOURCE.name(), 1);
			GoogleAnalyticsAppModule.get().getGoogleAnalyticsHelper().addCustomDimensionDefinition(GoogleAnalyticsTracker.CustomDimension.DEVICE_TYPE.name(), 2);
			GoogleAnalyticsAppModule.get().getGoogleAnalyticsHelper().addCustomDimensionDefinition(GoogleAnalyticsTracker.CustomDimension.REFERRER.name(), 3);
			GoogleAnalyticsAppModule.get().getGoogleAnalyticsHelper().addCustomDimensionDefinition(GoogleAnalyticsTracker.CustomDimension.DEVICE_YEAR_CLASS.name(), 4);
		}
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);

		MultiDex.install(this);

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

	@NonNull
	public AndroidAppContext getAppContext() {
		return (AndroidAppContext)super.getAppContext();
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
	protected AnalyticsSender<? extends AnalyticsTracker> createAnalyticsSender(List<? extends AnalyticsTracker> analyticsTrackers) {
		return new AppAnalyticsSender((List<AppAnalyticsTracker>)analyticsTrackers);
	}

	@Override
	protected AnalyticsTracker createGoogleAnalyticsTracker() {
		return new AndroidGoogleAnalyticsTracker();
	}

	@Override
	protected AnalyticsTracker createFirebaseAnalyticsTracker() {
		return new AndroidFirebaseAnalyticsTracker();
	}

	@NonNull
	@Override
	public AppAnalyticsSender getAnalyticsSender() {
		return (AppAnalyticsSender)super.getAnalyticsSender();
	}

	@Override
	protected DebugContext createDebugContext() {
		return new AndroidDebugContext();
	}

	@Override
	public UserRepository getUserRepository() {
		return new UserRepositoryImpl();
	}

	@Override
	public Boolean isDatabaseEnabled() {
		return true;
	}

	@Override
	protected void initDatabaseRepositories(Map<Class<? extends Identifiable>, Repository<? extends Identifiable>> repositories, SQLiteHelper dbHelper) {
		repositories.put(SampleSQLiteEntity.class, new SampleSQLiteRepository(dbHelper));
	}

	@Override
	protected void initAppModule(Map<String, AppModule> appModulesMap) {
		appModulesMap.put(GoogleAnalyticsAppModule.MODULE_NAME,  new GoogleAnalyticsAppModule());
		appModulesMap.put(FirebaseAppModule.MODULE_NAME,  new AndroidFirebaseAppModule());
		appModulesMap.put(CrashlyticsAppModule.MODULE_NAME, new AndroidCrashlyticsAppModule());
		appModulesMap.put(AdMobAppModule.MODULE_NAME, new SampleAdMobAppModule());
		appModulesMap.put(FacebookAppModule.MODULE_NAME, new FacebookAppModule());
		appModulesMap.put(AbstractFcmAppModule.MODULE_NAME, new AndroidFcmAppModule());
		appModulesMap.put(AboutAppModule.MODULE_NAME,  new AndroidAboutAppModule());
		appModulesMap.put(TwitterAppModule.MODULE_NAME,  new TwitterAppModule(BuildConfig.TWITTER_OAUTH_CONSUMER_KEY, BuildConfig.TWITTER_OAUTH_CONSUMER_SECRET));
	}

	@Override
	public int getLauncherIconResId() {
		return R.mipmap.ic_launcher;
	}
}
