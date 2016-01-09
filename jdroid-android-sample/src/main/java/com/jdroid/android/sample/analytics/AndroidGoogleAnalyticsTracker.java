package com.jdroid.android.sample.analytics;

import com.google.android.gms.analytics.HitBuilders;
import com.jdroid.android.google.analytics.GoogleAnalyticsTracker;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.utils.IdGenerator;

import java.util.Map;

public class AndroidGoogleAnalyticsTracker extends GoogleAnalyticsTracker implements AppAnalyticsTracker {
	
	public static final String EXAMPLE_CATEGORY = "exampleCategory";
	public static final String EXAMPLE_LABEL = "exampleLabel";
	
	/**
	 * @see GoogleAnalyticsTracker#init(java.util.Map, java.util.Map)
	 */
	@Override
	protected void init(Map<String, Integer> customDimensionsMap, Map<String, Integer> customMetricsMap) {
		customDimensionsMap.put(CustomDimension.INSTALLATION_SOURCE.name(), 1);
		customDimensionsMap.put(CustomDimension.DEVICE_TYPE.name(), 2);
		customDimensionsMap.put(CustomDimension.APP_LOADING_SOURCE.name(), 3);
		customDimensionsMap.put(CustomDimension.DEVICE_YEAR_CLASS.name(), 4);
	}
	
	/**
	 * @see AppAnalyticsTracker#trackExampleEvent()
	 */
	@Override
	public void trackExampleEvent() {
		sendEvent(EXAMPLE_CATEGORY, "exampleAction", EXAMPLE_LABEL);
	}

	@Override
	public void trackExampleTransaction() {
		HitBuilders.TransactionBuilder transactionBuilder = new HitBuilders.TransactionBuilder();
		transactionBuilder.setCurrencyCode("USD");
		transactionBuilder.setRevenue(1000);
		transactionBuilder.setTax(10);
		transactionBuilder.setShipping(5);
		transactionBuilder.setTransactionId("tx" + DateUtils.nowMillis());
		sendTransaction(transactionBuilder);
	}

	@Override
	public void trackExampleTiming() {
		trackTiming(EXAMPLE_CATEGORY, "exampleVariable", EXAMPLE_LABEL, IdGenerator.getRandomLongId());
	}
	
}
