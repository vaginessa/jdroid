package com.jdroid.android.uil;

import com.jdroid.android.application.AbstractAppModule;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.debug.PreferencesAppender;
import com.jdroid.android.uil.debug.UilDebugContext;

import java.util.List;

public class UilAppModule extends AbstractAppModule {

	public static final String MODULE_NAME = UilAppModule.class.getName();

	public static UilAppModule get() {
		return (UilAppModule)AbstractApplication.get().getAppModule(MODULE_NAME);
	}

	private UilDebugContext uilDebugContext;

	protected UilDebugContext createUilDebugContext() {
		return new UilDebugContext();
	}

	public UilDebugContext getUilDebugContext() {
		synchronized (AbstractApplication.class) {
			if (uilDebugContext == null) {
				uilDebugContext = createUilDebugContext();
			}
		}
		return uilDebugContext;
	}

	@Override
	public List<PreferencesAppender> getPreferencesAppenders() {
		return getUilDebugContext().getPreferencesAppenders();
	}
}