package com.jdroid.android.google.analytics;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.context.UsageStats;
import com.jdroid.java.collections.Maps;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.utils.LoggerUtils;

import java.util.Map;

public class GoogleAnalyticsHelper {

	private static final org.slf4j.Logger LOGGER = LoggerUtils.getLogger(GoogleAnalyticsHelper.class);

	// 30 minutes
	private static final int SESSION_TIMEOUT = 1800;

	private Tracker tracker;

	private Map<String, Integer> customDimensionsMap = Maps.newHashMap();
	private Map<String, Integer> customMetricsMap = Maps.newHashMap();

	private Map<String, String> commonCustomDimensionsValues = Maps.newHashMap();

	public GoogleAnalyticsHelper() {
		GoogleAnalytics analytics = GoogleAnalytics.getInstance(AbstractApplication.get());
		if (AbstractApplication.get().getAppContext().isGoogleAnalyticsDebugEnabled()) {
			analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
		} else {
			analytics.getLogger().setLogLevel(Logger.LogLevel.ERROR);
		}
		tracker = analytics.newTracker(AbstractApplication.get().getAppContext().getGoogleAnalyticsTrackingId());
		tracker.setSessionTimeout(SESSION_TIMEOUT);
		tracker.enableAdvertisingIdCollection(isAdvertisingIdCollectionEnabled());
		init(customDimensionsMap, customMetricsMap);
	}

	protected void init(Map<String, Integer> customDimensionsMap, Map<String, Integer> customMetricsMap) {
		// Do nothing
	}

	protected Boolean isAdvertisingIdCollectionEnabled() {
		return true;
	}

	public void sendEvent(String category, String action, String label) {
		sendEvent(category, action, label, null, null);
	}

	public void sendEvent(String category, String action, String label, Map<String, String> customDimensions) {
		sendEvent(category, action, label, null, customDimensions);
	}

	public void sendEvent(String category, String action, String label, Long value, Map<String, String> customDimensions) {
		HitBuilders.EventBuilder eventBuilder = new HitBuilders.EventBuilder();

		addCustomDimension(eventBuilder, commonCustomDimensionsValues);
		addCustomDimension(eventBuilder, customDimensions);

		eventBuilder.setCategory(category);
		eventBuilder.setAction(action);
		eventBuilder.setLabel(label);
		if (value != null) {
			eventBuilder.setValue(value);
		}

		tracker.send(eventBuilder.build());
		LOGGER.debug("Event sent. Category [" + category + "] Action [" + action + "] Label [" + label + "]"
				+ (value != null ? " Value" + value + "]" : ""));
	}

	public void sendScreenView(String screenName) {
		sendScreenView(new HitBuilders.AppViewBuilder(), screenName);
	}

	public void sendScreenView(HitBuilders.AppViewBuilder appViewBuilder, String screenName) {

		addCustomDimension(appViewBuilder, commonCustomDimensionsValues);

		tracker.setScreenName(screenName);
		tracker.send(appViewBuilder.build());
		LOGGER.debug("Screen view sent. Screen name [" + screenName + "]");
	}

	public void sendTransaction(HitBuilders.TransactionBuilder transactionBuilder) {
		sendTransaction(transactionBuilder, null);
	}

	public void sendTransaction(HitBuilders.TransactionBuilder transactionBuilder, Map<String, String> customDimensions) {

		addCustomDimension(transactionBuilder, commonCustomDimensionsValues);
		addCustomDimension(transactionBuilder, customDimensions);

		tracker.send(transactionBuilder.build());
		LOGGER.debug("Transaction sent. " + transactionBuilder.build());
	}

	public void sendTransactionItem(HitBuilders.ItemBuilder itemBuilder) {
		tracker.send(itemBuilder.build());
		LOGGER.debug("Transaction item sent. " + itemBuilder.build());
	}

	public void trackTiming(String category, String variable, String label, long value) {
		// Avoid timing trackings when the app is in background to avoid session times data corruption.
		// TODO Improve this to track timings 30 seconds after of the last onResume,
		// so we don´t lose the tracking when an use case finish in background
		if (!(ignoreBackgroundTimingTrackings() && AbstractApplication.get().isInBackground())) {
			HitBuilders.TimingBuilder timingBuilder = new HitBuilders.TimingBuilder();
			timingBuilder.setCategory(category);
			timingBuilder.setVariable(variable);
			timingBuilder.setLabel(label);
			timingBuilder.setValue(value);
			tracker.send(timingBuilder.build());
			LOGGER.debug("Timing sent. Category [" + category + "] Variable [" + variable + "] Label [" + label
					+ "] Value [" + value + "]");
		}
	}

	protected Boolean ignoreBackgroundTimingTrackings() {
		return true;
	}

	protected void addCustomDimension(HitBuilders.AppViewBuilder appViewBuilder, GoogleAnalyticsTracker.CustomDimension customDimension, String dimension) {
		addCustomDimension(appViewBuilder, customDimension.name(), dimension);
	}

	protected void addCustomDimension(HitBuilders.AppViewBuilder appViewBuilder, String customDimension, String dimension) {
		Integer index = customDimensionsMap.get(customDimension);
		addCustomDimension(appViewBuilder, index, dimension);
	}

	protected void addCustomDimension(HitBuilders.AppViewBuilder appViewBuilder, Integer index, String dimension) {
		if (index != null) {
			LOGGER.debug("Added custom dimension: " + index + " - " + dimension);
			appViewBuilder.setCustomDimension(index, dimension);
		}
	}

	protected void addCustomDimension(HitBuilders.AppViewBuilder appViewBuilder, Map<String, String> customDimensions) {
		if (customDimensions != null) {
			for (Map.Entry<String, String> entry : customDimensions.entrySet()) {
				addCustomDimension(appViewBuilder, entry.getKey(), entry.getValue());
			}
		}
	}

	protected void addCustomDimension(HitBuilders.EventBuilder eventBuilder, Map<String, String> customDimensions) {
		if (customDimensions != null) {
			for (Map.Entry<String, String> entry : customDimensions.entrySet()) {
				addCustomDimension(eventBuilder, entry.getKey(), entry.getValue());
			}
		}
	}

	protected void addCustomDimension(HitBuilders.EventBuilder eventBuilder, GoogleAnalyticsTracker.CustomDimension customDimension, String dimension) {
		addCustomDimension(eventBuilder, customDimension.name(), dimension);
	}

	protected void addCustomDimension(HitBuilders.EventBuilder eventBuilder, String customDimension, String dimension) {
		Integer index = customDimensionsMap.get(customDimension);
		addCustomDimension(eventBuilder, index, dimension);
	}

	protected void addCustomDimension(HitBuilders.EventBuilder eventBuilder, Integer index, String dimension) {
		if (index != null) {
			LOGGER.debug("Added custom dimension: " + index + " - " + dimension);
			eventBuilder.setCustomDimension(index, dimension);
		}
	}

	protected void addCustomDimension(HitBuilders.TransactionBuilder transactionBuilder, Map<String, String> customDimensions) {
		if (customDimensions != null) {
			for (Map.Entry<String, String> entry : customDimensions.entrySet()) {
				addCustomDimension(transactionBuilder, entry.getKey(), entry.getValue());
			}
		}
	}

	protected void addCustomDimension(HitBuilders.TransactionBuilder transactionBuilder, String customDimension, String dimension) {
		Integer index = customDimensionsMap.get(customDimension);
		addCustomDimension(transactionBuilder, index, dimension);
	}

	protected void addCustomDimension(HitBuilders.TransactionBuilder transactionBuilder, Integer index, String dimension) {
		if (index != null) {
			LOGGER.debug("Added custom dimension: " + index + " - " + dimension);
			transactionBuilder.setCustomDimension(index, dimension);
		}
	}

	protected void addCustomMetric(HitBuilders.AppViewBuilder appViewBuilder, String customDimensionKey, Float metric) {
		Integer index = customMetricsMap.get(customDimensionKey);
		addCustomMetric(appViewBuilder, index, metric);
	}

	protected void addCustomMetric(HitBuilders.AppViewBuilder appViewBuilder, Integer index, Float metric) {
		if (index != null) {
			LOGGER.debug("Added custom metric: " + index + " - " + metric);
			appViewBuilder.setCustomMetric(index, metric);
		}
	}

	public Boolean isSessionExpired() {
		return (DateUtils.nowMillis() - UsageStats.getLastStopTime()) > (4 * SESSION_TIMEOUT * DateUtils.MILLIS_PER_SECOND);
	}

	public void dispatchLocalHits() {
		GoogleAnalytics.getInstance(AbstractApplication.get()).dispatchLocalHits();
	}

	public void addCommonCustomDimension(String key, String value) {
		commonCustomDimensionsValues.put(key, value);
	}

	public Boolean hasCommonCustomDimension(String key) {
		return commonCustomDimensionsValues.containsKey(key);
	}

	public Tracker getTracker() {
		return tracker;
	}
}
