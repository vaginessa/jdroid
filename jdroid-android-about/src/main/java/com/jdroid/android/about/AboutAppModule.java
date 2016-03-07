package com.jdroid.android.about;

import com.jdroid.android.application.AbstractAppModule;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.debug.PreferencesAppender;

import java.util.List;

public class AboutAppModule extends AbstractAppModule {

	public static final String MODULE_NAME = AboutAppModule.class.getName();

	public static AboutAppModule get() {
		return (AboutAppModule)AbstractApplication.get().getAppModule(MODULE_NAME);
	}

	private AboutContext aboutContext;
	private AboutDebugContext aboutDebugContext;

	public AboutAppModule() {
		aboutContext = createAboutContext();
	}

	protected AboutContext createAboutContext() {
		return new AboutContext();
	}

	public AboutContext getAboutContext() {
		return aboutContext;
	}

	@Override
	public List<PreferencesAppender> getPreferencesAppenders() {
		return getGcmDebugContext().getPreferencesAppenders();
	}

	public AboutDebugContext getGcmDebugContext() {
		synchronized (AbstractApplication.class) {
			if (aboutDebugContext == null) {
				aboutDebugContext = createAboutDebugContext();
			}
		}
		return aboutDebugContext;
	}

	protected AboutDebugContext createAboutDebugContext() {
		return new AboutDebugContext();
	}
}