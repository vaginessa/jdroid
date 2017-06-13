package com.jdroid.android.firebase.fcm;

import android.os.Bundle;
import android.support.annotation.WorkerThread;

import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.activity.ActivityDelegate;
import com.jdroid.android.application.AbstractAppModule;
import com.jdroid.android.application.AbstractApplication;

import java.util.List;

public abstract class AbstractFcmAppModule extends AbstractAppModule {

	public static final String MODULE_NAME = AbstractFcmAppModule.class.getName();

	public static AbstractFcmAppModule get() {
		return (AbstractFcmAppModule)AbstractApplication.get().getAppModule(MODULE_NAME);
	}

	private FcmMessageResolver fcmMessageResolver;
	private FcmListenerResolver fcmListenerResolver;
	private Boolean fcmInitialized = false;

	public AbstractFcmAppModule() {
		fcmMessageResolver = createFcmMessageResolver();
		fcmListenerResolver = createFcmListenerResolver();
	}

	public FcmMessageResolver getFcmMessageResolver(String from) {
		return fcmMessageResolver;
	}

	public abstract List<FcmSender> getFcmSenders();

	public abstract FcmMessageResolver createFcmMessageResolver();

	public FcmListenerResolver createFcmListenerResolver() {
		return new FcmListenerResolver();
	}

	public FcmListenerResolver getFcmListenerResolver() {
		return fcmListenerResolver;
	}

	@Override
	public ActivityDelegate createActivityDelegate(AbstractFragmentActivity abstractFragmentActivity) {
		return !fcmInitialized ? new ActivityDelegate(abstractFragmentActivity) {

			@Override
			public void onCreate(Bundle savedInstanceState) {
				if (!fcmInitialized) {
					startFcmRegistration(true, true);
					fcmInitialized = true;
				}
			}
		} : null;
	}

	protected List<String> getSubscriptionTopics() {
		return null;
	}
	
	@WorkerThread
	@Override
	public void onInstanceIdTokenRefresh() {
		startFcmRegistration(false, false);
	}

	@Override
	public void onGooglePlayServicesUpdated() {
		startFcmRegistration(false, true);
	}

	@Override
	public void onInitializeGcmTasks() {
		startFcmRegistration(false, true);
	}

	public void startFcmRegistration(Boolean updateLastActiveTimestamp, Boolean isInstantExecutionRequired) {
		FcmRegistrationCommand fcmRegistrationCommand = createFcmRegistrationCommand();
		fcmRegistrationCommand.setInstantExecutionRequired(isInstantExecutionRequired);
		fcmRegistrationCommand.start(updateLastActiveTimestamp);
	}

	protected FcmRegistrationCommand createFcmRegistrationCommand() {
		return new FcmRegistrationCommand();
	}
}