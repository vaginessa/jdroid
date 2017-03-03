package com.jdroid.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.util.Locale;

public class LocaleChangedReceiver extends BroadcastReceiver {

	private final static Logger LOGGER = LoggerUtils.getLogger(LocaleChangedReceiver.class);

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent != null && Intent.ACTION_LOCALE_CHANGED.equals(intent.getAction())) {
			LOGGER.info("Changed locale to " + Locale.getDefault());
			AbstractApplication.get().onLocaleChanged();
		}
	}
}
