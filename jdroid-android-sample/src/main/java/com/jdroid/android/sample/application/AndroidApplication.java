package com.jdroid.android.sample.application;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.v4.app.Fragment;

import com.firebase.client.Firebase;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.jdroid.android.about.AboutAppModule;
import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.activity.ActivityHelper;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.application.AppModule;
import com.jdroid.android.context.AppContext;
import com.jdroid.android.crashlytics.CrashlyticsAppModule;
import com.jdroid.android.debug.DebugContext;
import com.jdroid.android.facebook.FacebookAppModule;
import com.jdroid.android.firebase.FirebaseAppModule;
import com.jdroid.android.firebase.fcm.AbstractFcmAppModule;
import com.jdroid.android.firebase.remoteconfig.FirebaseRemoteConfigAppModule;
import com.jdroid.android.fragment.FragmentHelper;
import com.jdroid.android.google.admob.AdMobAppModule;
import com.jdroid.android.google.analytics.GoogleAnalyticsAppModule;
import com.jdroid.android.google.geofences.GeofenceTransitionListener;
import com.jdroid.android.google.geofences.GeofencesHelper;
import com.jdroid.android.notification.NotificationBuilder;
import com.jdroid.android.notification.NotificationUtils;
import com.jdroid.android.repository.UserRepository;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.debug.AndroidDebugContext;
import com.jdroid.android.sample.firebase.fcm.AndroidFcmAppModule;
import com.jdroid.android.sample.google.analytics.AndroidGoogleAnalyticsAppModule;
import com.jdroid.android.sample.repository.UserRepositoryImpl;
import com.jdroid.android.sample.ui.AndroidActivityHelper;
import com.jdroid.android.sample.ui.AndroidFragmentHelper;
import com.jdroid.android.sample.ui.about.AndroidAboutAppModule;
import com.jdroid.android.sample.ui.google.admob.SampleAdMobAppModule;
import com.jdroid.android.sample.ui.home.HomeActivity;
import com.jdroid.android.sample.ui.sqlite.SampleSQLiteEntity;
import com.jdroid.android.sample.ui.sqlite.SampleSQLiteRepository;
import com.jdroid.android.sample.ui.uri.SampleUriWatcher;
import com.jdroid.android.sqlite.SQLiteHelper;
import com.jdroid.android.twitter.TwitterAppModule;
import com.jdroid.java.domain.Identifiable;
import com.jdroid.java.http.okhttp.OkHttpServiceFactory;
import com.jdroid.java.repository.Repository;
import com.jdroid.java.utils.IdGenerator;

import java.util.Map;

public class AndroidApplication extends AbstractApplication {
	
	public static AndroidApplication get() {
		return (AndroidApplication)AbstractApplication.INSTANCE;
	}

	public AndroidApplication() {
		setHttpServiceFactory(new OkHttpServiceFactory());
	}

	@Override
	public void onCreate() {
		super.onCreate();

		getUriMapper().addUriWatcher(new SampleUriWatcher());

		Firebase.setAndroidContext(this);

		GeofencesHelper.addGeofenceTransitionListener(new GeofenceTransitionListener() {
			@Override
			public void onTransitionEnter(GeofencingEvent geofencingEvent) {
				sendNotification(geofencingEvent, "Geofence Enter");
			}

			@Override
			public void onTransitionDwell(GeofencingEvent geofencingEvent) {
				sendNotification(geofencingEvent, "Geofence Dwell");
			}

			@Override
			public void onTransitionExit(GeofencingEvent geofencingEvent) {
				sendNotification(geofencingEvent, "Geofence Exit");
			}

			private void sendNotification(GeofencingEvent geofencingEvent, String transition) {
				NotificationBuilder builder = new NotificationBuilder("sampleGeoFenceNotification");
				builder.setSmallIcon(AbstractApplication.get().getNotificationIconResId());

				builder.setContentTitle(transition);
				StringBuilder contentBuilder = new StringBuilder();
				for (Geofence geofence : geofencingEvent.getTriggeringGeofences()) {
					contentBuilder.append(geofence.getRequestId());
				}
				builder.setContentText(contentBuilder.toString());

				NotificationUtils.sendNotification(IdGenerator.getIntId(), builder);
			}
		});
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
		appModulesMap.put(GoogleAnalyticsAppModule.MODULE_NAME, new AndroidGoogleAnalyticsAppModule());
		appModulesMap.put(FirebaseAppModule.MODULE_NAME, new FirebaseAppModule());
		appModulesMap.put(CrashlyticsAppModule.MODULE_NAME, new CrashlyticsAppModule());
		appModulesMap.put(AdMobAppModule.MODULE_NAME, new SampleAdMobAppModule());
		appModulesMap.put(FacebookAppModule.MODULE_NAME, new FacebookAppModule());
		appModulesMap.put(AbstractFcmAppModule.MODULE_NAME, new AndroidFcmAppModule());
		appModulesMap.put(FirebaseRemoteConfigAppModule.MODULE_NAME, new FirebaseRemoteConfigAppModule());
		appModulesMap.put(AboutAppModule.MODULE_NAME, new AndroidAboutAppModule());
		appModulesMap.put(TwitterAppModule.MODULE_NAME, new TwitterAppModule());
	}

	@Override
	public int getLauncherIconResId() {
		return R.mipmap.ic_launcher;
	}

	@Override
	public int getNotificationIconResId() {
		return R.drawable.ic_notification;
	}

	@Override
	public String getManifestPackageName() {
		return "com.jdroid.android.sample";
	}
}
