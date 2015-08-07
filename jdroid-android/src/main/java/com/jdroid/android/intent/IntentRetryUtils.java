package com.jdroid.android.intent;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.SystemClock;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.utils.AlarmUtils;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

public class IntentRetryUtils {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(IntentRetryUtils.class);
	
	// Used internally to count retries.
	private static final String EXTRA_CURRENT_RETRY_COUNT = "currentRetryCount";
	private static final String EXTRA_CURRENT_BACKOFF = "currentBackOff";
	
	// The default backoff in milliseconds
	private static final long DEFAULT_BACKOFF = 5000;
	
	// The maximum number of tries to send a report.
	private static final int DEFAULT_MAXIMUM_RETRY_COUNT = 10;
	
	public static Boolean retry(Intent intent) {
		return retry(intent, DEFAULT_MAXIMUM_RETRY_COUNT);
	}
	
	public static Boolean retry(Intent intent, Integer maximumRetryCount) {
		return retry(intent, maximumRetryCount, DEFAULT_BACKOFF);
	}
	
	public static Boolean retry(Intent intent, Integer maximumRetryCount, Long startBackoff) {
		
		if (intent == null) {
			// Discard retry
			LOGGER.warn("The retry intent is null and will be discarded.");
			return false;
		}
		
		int count = intent.getIntExtra(EXTRA_CURRENT_RETRY_COUNT, 0);
		long backoff = intent.getLongExtra(EXTRA_CURRENT_BACKOFF, startBackoff);
		
		intent.putExtra(EXTRA_CURRENT_RETRY_COUNT, count + 1);
		intent.putExtra(EXTRA_CURRENT_BACKOFF, backoff * 2);
		
		PendingIntent operation = PendingIntent.getService(AbstractApplication.get(), 0, intent,
			PendingIntent.FLAG_CANCEL_CURRENT);
		if (count >= maximumRetryCount) {
			// Discard retry
			LOGGER.warn("Operation reached the maximum retry count and will be discarded.");
			return false;
		}
		// Retry using an exponential backoff
		AlarmUtils.scheduleElapsedRealtimeAlarm(SystemClock.elapsedRealtime() + backoff, operation);
		return true;
	}
	
	public static void schedule(Intent intent, Long frequency) {
		
		PendingIntent operation = PendingIntent.getService(AbstractApplication.get(), 0, intent,
			PendingIntent.FLAG_CANCEL_CURRENT);
		
		AlarmUtils.scheduleElapsedRealtimeAlarm(SystemClock.elapsedRealtime() + frequency, operation);
	}
	
}
