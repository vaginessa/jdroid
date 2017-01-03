package com.jdroid.android.about;

import android.support.annotation.NonNull;

import com.jdroid.android.about.analytics.AboutAnalyticsSender;
import com.jdroid.android.about.analytics.AboutAnalyticsTracker;
import com.jdroid.android.about.analytics.FirebaseAboutAnalyticsTracker;
import com.jdroid.android.about.analytics.GoogleAboutAnalyticsTracker;
import com.jdroid.android.application.AbstractAppModule;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.debug.PreferencesAppender;
import com.jdroid.java.analytics.AnalyticsSender;
import com.jdroid.java.analytics.AnalyticsTracker;
import com.jdroid.java.collections.Lists;

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
		return getAboutDebugContext().getPreferencesAppenders();
	}

	public AboutDebugContext getAboutDebugContext() {
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

	@NonNull
	@Override
	public AnalyticsSender<? extends AnalyticsTracker> createModuleAnalyticsSender(List<? extends AnalyticsTracker> analyticsTrackers) {
		return new AboutAnalyticsSender((List<AboutAnalyticsTracker>)analyticsTrackers);
	}

	@Override
	public List<? extends AnalyticsTracker> createModuleAnalyticsTrackers() {
		return Lists.newArrayList(new GoogleAboutAnalyticsTracker(), new FirebaseAboutAnalyticsTracker());
	}

	@NonNull
	@Override
	public AboutAnalyticsSender getAnalyticsSender() {
		return (AboutAnalyticsSender)super.getAnalyticsSender();
	}
}