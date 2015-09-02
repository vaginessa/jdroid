package com.jdroid.android.google.gcm;

import android.support.v4.util.Pair;

import com.jdroid.android.application.AbstractAppModule;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.debug.PreferencesAppender;

import java.util.List;

public abstract class AbstractGcmAppModule extends AbstractAppModule {

	protected static AbstractGcmAppModule INSTANCE;

	private GcmContext gcmContext;
	private GcmDebugContext gcmDebugContext;
	private GcmMessageResolver gcmMessageResolver;

	public static AbstractGcmAppModule get() {
		return INSTANCE;
	}

	public AbstractGcmAppModule() {
		INSTANCE = this;
		gcmContext = createGcmContext();
		gcmMessageResolver = createGcmMessageResolver();
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

	protected abstract GcmMessageResolver createGcmMessageResolver();

	@Override
	public void onCreate() {
		super.onCreate();

		AbstractApplication.get().getDebugContext().addCustomDebugInfoProperty(new Pair<String, Object>("Google Project Id", gcmContext.getGoogleProjectId()));
	}
}