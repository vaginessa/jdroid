package com.jdroid.android.sample.analytics;

import com.google.android.gms.analytics.HitBuilders;
import com.jdroid.android.google.analytics.GoogleAnalyticsHelper;
import com.jdroid.android.google.analytics.GoogleAnalyticsTracker;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.utils.RandomUtils;

import java.util.Map;

public class AndroidGoogleAnalyticsTracker extends GoogleAnalyticsTracker implements AppAnalyticsTracker {
	
	public static final String EXAMPLE_CATEGORY = "exampleCategory";
	public static final String EXAMPLE_LABEL = "exampleLabel";

	@Override
	protected GoogleAnalyticsHelper createGoogleAnalyticsHelper() {
		return new GoogleAnalyticsHelper() {
			@Override
			protected void init(Map<String, Integer> customDimensionsMap, Map<String, Integer> customMetricsMap) {
				customDimensionsMap.put(CustomDimension.INSTALLATION_SOURCE.name(), 1);
				customDimensionsMap.put(CustomDimension.DEVICE_TYPE.name(), 2);
				customDimensionsMap.put(CustomDimension.APP_LOADING_SOURCE.name(), 3);
				customDimensionsMap.put(CustomDimension.DEVICE_YEAR_CLASS.name(), 4);
			}
		};
	}

	@Override
	public void trackExampleEvent() {
		getGoogleAnalyticsHelper().sendEvent(EXAMPLE_CATEGORY, "exampleAction", EXAMPLE_LABEL);
	}

	@Override
	public void trackExampleTransaction() {
		HitBuilders.TransactionBuilder transactionBuilder = new HitBuilders.TransactionBuilder();
		transactionBuilder.setCurrencyCode("USD");
		transactionBuilder.setRevenue(1000);
		transactionBuilder.setTax(10);
		transactionBuilder.setShipping(5);
		transactionBuilder.setTransactionId("tx" + DateUtils.nowMillis());
		getGoogleAnalyticsHelper().sendTransaction(transactionBuilder);
	}

	@Override
	public void trackExampleTiming() {
		getGoogleAnalyticsHelper().trackTiming(EXAMPLE_CATEGORY, "exampleVariable", EXAMPLE_LABEL, RandomUtils.getLong());
	}
	
}
