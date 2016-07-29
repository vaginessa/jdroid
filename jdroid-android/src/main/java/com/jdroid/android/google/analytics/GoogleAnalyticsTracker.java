package com.jdroid.android.google.analytics;

import android.app.Activity;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.HitBuilders.AppViewBuilder;
import com.google.android.gms.analytics.HitBuilders.SocialBuilder;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.jdroid.android.analytics.AbstractAnalyticsTracker;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.experiments.ExperimentHelper;
import com.jdroid.android.experiments.ExperimentHelper.Experiment;
import com.jdroid.android.experiments.ExperimentHelper.ExperimentVariant;
import com.jdroid.android.social.AccountType;
import com.jdroid.android.social.SocialAction;
import com.jdroid.android.usecase.AbstractUseCase;
import com.jdroid.android.utils.DeviceUtils;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.util.List;
import java.util.Map.Entry;

public class GoogleAnalyticsTracker extends AbstractAnalyticsTracker {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(GoogleAnalyticsTracker.class);
	
	public static final String NOTIFICATION_CATEGORY = "notification";
	private static final String FEEDBACK_CATEGORY = "feedback";
	private static final String WIDGET_CATEGORY = "widget";

	public static final String SOCIAL = "social";
	
	private Boolean firstTrackingSent = false;

	private GoogleAnalyticsHelper googleAnalyticsHelper;

	public enum CustomDimension {
		LOGIN_SOURCE,
		IS_LOGGED,
		INSTALLATION_SOURCE, // User scope
		REFERRER, // Hit scope
		DEVICE_TYPE, // User scope
		DEVICE_YEAR_CLASS; // User scope
	}
	
	public GoogleAnalyticsTracker() {
		googleAnalyticsHelper = GoogleAnalyticsAppModule.get().getGoogleAnalyticsHelper();
	}

	@Override
	public Boolean isEnabled() {
		return AbstractApplication.get().getAppContext().isGoogleAnalyticsEnabled();
	}
	
	@Override
	public void onActivityStart(Class<? extends Activity> activityClass, String referrer, Object data) {
		
		synchronized (GoogleAnalyticsTracker.class) {
			
			AppViewBuilder appViewBuilder = new HitBuilders.AppViewBuilder();
			if (referrer != null) {
				if (!googleAnalyticsHelper.hasCommonCustomDimension(CustomDimension.REFERRER.name()) || googleAnalyticsHelper.isSessionExpired()) {
					googleAnalyticsHelper.addCommonCustomDimension(CustomDimension.REFERRER.name(), referrer);
				}
			} else if (!googleAnalyticsHelper.hasCommonCustomDimension(CustomDimension.REFERRER.name())) {
				googleAnalyticsHelper.addCommonCustomDimension(CustomDimension.REFERRER.name(), "normal");
			}


			if (!firstTrackingSent) {
				googleAnalyticsHelper.addCustomDimension(appViewBuilder, CustomDimension.DEVICE_YEAR_CLASS, DeviceUtils.getDeviceYearClass().toString());
				googleAnalyticsHelper.addCustomDimension(appViewBuilder, CustomDimension.DEVICE_TYPE, DeviceUtils.getDeviceType());
				
				for (Entry<Experiment, ExperimentVariant> entry : ExperimentHelper.getExperimentsMap().entrySet()) {
					Experiment experiment = entry.getKey();
					ExperimentVariant experimentVariant = entry.getValue();
					googleAnalyticsHelper.sendEvent("abTesting", "load", experiment.getId() + "-" + experimentVariant.getId());
				}
				
				String installationSource = AbstractApplication.get().getInstallationSource();
				if (installationSource != null) {
					googleAnalyticsHelper.addCustomDimension(appViewBuilder, CustomDimension.INSTALLATION_SOURCE, installationSource);
					
					onAppLoadTrack(appViewBuilder, data);
					firstTrackingSent = true;
				}
			}
			onActivityStartTrack(appViewBuilder, data);
			googleAnalyticsHelper.sendScreenView(appViewBuilder, activityClass.getSimpleName());
		}
	}
	
	protected void onAppLoadTrack(AppViewBuilder appViewBuilder, Object data) {
		// Do nothing
	}
	
	protected void onActivityStartTrack(AppViewBuilder appViewBuilder, Object data) {
		// Do nothing
	}
	
	@Override
	public void onFragmentStart(String screenViewName) {
		synchronized (GoogleAnalyticsTracker.class) {
			AppViewBuilder appViewBuilder = new HitBuilders.AppViewBuilder();
			googleAnalyticsHelper.sendScreenView(appViewBuilder, screenViewName);
		}
	}
	
	@Override
	public void trackNotificationDisplayed(String notificationName) {
		googleAnalyticsHelper.sendEvent(NOTIFICATION_CATEGORY, "display", notificationName);
	}
	
	@Override
	public void trackNotificationOpened(String notificationName) {
		googleAnalyticsHelper.sendEvent(NOTIFICATION_CATEGORY, "open", notificationName);
	}

	@Override
	public void trackEnjoyingApp(Boolean enjoying) {
		googleAnalyticsHelper.sendEvent(FEEDBACK_CATEGORY, "enjoying", enjoying.toString());
	}

	@Override
	public void trackRateOnGooglePlay(Boolean rate) {
		googleAnalyticsHelper.sendEvent(FEEDBACK_CATEGORY, "rate", rate.toString());
	}

	@Override
	public void trackGiveFeedback(Boolean feedback) {
		googleAnalyticsHelper.sendEvent(FEEDBACK_CATEGORY, "giveFeedback", feedback.toString());
	}

	@Override
	public void trackUseCaseTiming(Class<? extends AbstractUseCase> useCaseClass, long executionTime) {
		googleAnalyticsHelper.trackTiming("UseCase", useCaseClass.getSimpleName(), useCaseClass.getSimpleName(), executionTime);
	}

	@Override
	public void trackServiceTiming(String trackingVariable, String trackingLabel, long executionTime) {
		googleAnalyticsHelper.trackTiming("Service", trackingVariable, trackingLabel, executionTime);
	}

	@Override
	public void trackUriOpened(String referrerCategory, String screenName) {
		googleAnalyticsHelper.sendEvent("uri", "open" + screenName, referrerCategory);
	}

	// Widgets

	public void trackWidgetAdded(String widgetName) {
		googleAnalyticsHelper.sendEvent(WIDGET_CATEGORY, "add", widgetName);
	}

	public void trackWidgetRemoved(String widgetName) {
		googleAnalyticsHelper.sendEvent(WIDGET_CATEGORY, "remove", widgetName);
	}
	
	@Override
	public void trackSocialInteraction(AccountType accountType, SocialAction socialAction, String socialTarget) {
		
		String category = SOCIAL;
		String network = "Undefined";
		if (accountType != null) {
			category += accountType.getFriendlyName();
			network = accountType.getFriendlyName();
		}
		googleAnalyticsHelper.sendEvent(category, socialAction.getName(), socialTarget);
		
		SocialBuilder socialBuilder = new SocialBuilder();
		socialBuilder.setNetwork(network);
		socialBuilder.setAction(socialAction.getName());
		socialBuilder.setTarget(socialTarget);
		
		googleAnalyticsHelper.getTracker().send(socialBuilder.build());
		LOGGER.debug("Social interaction sent. Network [" + network + "] Action [" + socialAction.getName()
				+ "] Target [" + socialTarget + "]");
	}
	
	@Override
	public void trackFatalException(Throwable throwable, List<String> tags) {
		HitBuilders.ExceptionBuilder builder = new HitBuilders.ExceptionBuilder();
		String description = new StandardExceptionParser(AbstractApplication.get(), null).getDescription(Thread.currentThread().getName(), throwable);
		builder.setDescription(description);
		builder.setFatal(true);
		googleAnalyticsHelper.getTracker().send(builder.build());
		googleAnalyticsHelper.dispatchLocalHits();
		LOGGER.debug("Fatal exception sent. Description [" + description + "]");
	}

	@Override
	public void trackHandledException(Throwable throwable, List<String> tags) {
		HitBuilders.ExceptionBuilder builder = new HitBuilders.ExceptionBuilder();
		String description = new StandardExceptionParser(AbstractApplication.get(), null).getDescription(Thread.currentThread().getName(), throwable);
		builder.setDescription(description);
		builder.setFatal(false);
		googleAnalyticsHelper.getTracker().send(builder.build());
		LOGGER.debug("Non fatal exception sent. Description [" + description + "]");
	}

	public void sendSocialInteraction(AccountType accountType, SocialAction socialAction, String socialTarget) {
		
		SocialBuilder socialBuilder = new SocialBuilder();
		socialBuilder.setNetwork(accountType.getFriendlyName());
		socialBuilder.setAction(socialAction.getName());
		socialBuilder.setTarget(socialTarget);
		
		googleAnalyticsHelper.getTracker().send(socialBuilder.build());
		LOGGER.debug("Social interaction sent. Network [" + accountType.getFriendlyName() + "] Action ["
				+ socialAction.getName() + "] Target [" + socialTarget + "]");
	}

	public GoogleAnalyticsHelper getGoogleAnalyticsHelper() {
		return googleAnalyticsHelper;
	}
}
