package com.jdroid.android.google.gcm;

import android.app.Activity;
import android.os.Bundle;

import com.jdroid.android.application.AbstractActivityLifecycleListener;
import com.jdroid.android.application.AbstractAppModule;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.application.ActivityLifecycleListener;
import com.jdroid.android.debug.PreferencesAppender;

import java.util.List;

public abstract class AbstractGcmAppModule extends AbstractAppModule {

	public static final String MODULE_NAME = AbstractGcmAppModule.class.getName();

	public static AbstractGcmAppModule get() {
		return (AbstractGcmAppModule)AbstractApplication.get().getAppModule(MODULE_NAME);
	}

	private GcmDebugContext gcmDebugContext;
	private GcmMessageResolver gcmMessageResolver;
	private GcmListenerResolver gcmListenerResolver;
	private Boolean gcmInitialized = false;
	private ActivityLifecycleListener activityLifecycleListener;

	public AbstractGcmAppModule() {
		gcmMessageResolver = createGcmMessageResolver();
		gcmListenerResolver = createGcmListenerResolver();
		activityLifecycleListener = createActivityLifecycleListener();
	}

	protected GcmDebugContext createGcmDebugContext() {
		return new GcmDebugContext();
	}

	public GcmDebugContext getGcmDebugContext() {
		synchronized (AbstractApplication.class) {
			if (gcmDebugContext == null) {
				gcmDebugContext = createGcmDebugContext();
			}
		}
		return gcmDebugContext;
	}

	@Override
	public List<PreferencesAppender> getPreferencesAppenders() {
		return getGcmDebugContext().getPreferencesAppenders();
	}

	public GcmMessageResolver getGcmMessageResolver() {
		return gcmMessageResolver;
	}

	public abstract GcmSender getGcmSender();

	public abstract GcmMessageResolver createGcmMessageResolver();

	public GcmListenerResolver createGcmListenerResolver() {
		return new GcmListenerResolver();
	}

	public GcmListenerResolver getGcmListenerResolver() {
		return gcmListenerResolver;
	}

	protected ActivityLifecycleListener createActivityLifecycleListener() {
		return new AbstractActivityLifecycleListener() {
			@Override
			public void onCreateActivity(Activity activity) {
				if (!gcmInitialized) {
					startGcmRegistration(true);
					gcmInitialized = true;
				}
			}
		};
	}

	public abstract void onRegisterOnServer(String registrationToken, Boolean updateLastActiveTimestamp, Bundle bundle);

	protected List<String> getSubscriptionTopics() {
		return null;
	}

	@Override
	public void onInstanceIdTokenRefresh() {
		startGcmRegistration(false);
	}

	@Override
	public void onGooglePlayServicesUpdated() {
		startGcmRegistration(false);
	}

	@Override
	public void onInitializeGcmTasks() {
		startGcmRegistration(false);
	}

	@Override
	public ActivityLifecycleListener getActivityLifecycleListener() {
		return activityLifecycleListener;
	}

	public void startGcmRegistration(Boolean updateLastActiveTimestamp) {
		createGcmRegistrationCommand().start(updateLastActiveTimestamp);
	}

	protected GcmRegistrationCommand createGcmRegistrationCommand() {
		return new GcmRegistrationCommand();
	}
}