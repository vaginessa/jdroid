package com.jdroid.android.google.gcm;

import android.support.v4.util.Pair;

import com.jdroid.android.application.AbstractAppModule;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.debug.PreferencesAppender;

import java.util.List;

public abstract class AbstractGcmAppModule extends AbstractAppModule {

	public static final String MODULE_NAME = AbstractGcmAppModule.class.getName();

	public static AbstractGcmAppModule get() {
		return (AbstractGcmAppModule)AbstractApplication.get().getAppModule(MODULE_NAME);
	}

	private GcmContext gcmContext;
	private GcmDebugContext gcmDebugContext;
	private GcmMessageResolver gcmMessageResolver;
	private GcmListenerResolver gcmListenerResolver;

	public AbstractGcmAppModule() {
		gcmContext = createGcmContext();
		gcmMessageResolver = createGcmMessageResolver();
		gcmListenerResolver = createGcmListenerResolver();
	}

	protected GcmContext createGcmContext() {
		return new GcmContext();
	}

	public GcmContext getGcmContext() {
		return gcmContext;
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

	public abstract GcmMessageResolver createGcmMessageResolver();

	public GcmListenerResolver createGcmListenerResolver() {
		return new GcmListenerResolver();
	}

	public GcmListenerResolver getGcmListenerResolver() {
		return gcmListenerResolver;
	}

	public abstract void onRegisterOnServer(String registrationToken);

	protected List<String> getSubscriptionTopics() {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		GcmRegistrationService.start();

		AbstractApplication.get().getDebugContext().addCustomDebugInfoProperty(new Pair<String, Object>("GCM Sender Id", gcmContext.getSenderId()));
	}

	@Override
	public void onInstanceIdTokenRefresh() {
		GcmPreferences.clearRegistrationToken();
		GcmRegistrationService.start();
	}
}