package com.jdroid.android.gcm;

import org.slf4j.Logger;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.jdroid.java.utils.LoggerUtils;

/**
 * {@link BroadcastReceiver} that receives GCM messages and delivers them to an application-specific {@link GcmService}
 * subclass.
 */
public class GcmBroadcastReceiver extends BroadcastReceiver {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(GcmBroadcastReceiver.class);
	
	@Override
	public final void onReceive(Context context, Intent intent) {
		LOGGER.trace("onReceive: " + intent.getAction());
		GcmService.start(intent);
		setResult(Activity.RESULT_OK, null, null);
	}
}
