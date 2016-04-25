package com.jdroid.android.google.inappbilling;

import com.jdroid.android.debug.PreferencesAppender;
import com.jdroid.java.collections.Lists;

import java.util.List;

public class InAppBillingDebugContext {

	public List<PreferencesAppender> getPreferencesAppenders() {
		List<PreferencesAppender> appenders = Lists.newArrayList();
		appenders.add(createInAppBillingDebugPrefsAppender());
		return appenders;
	}

	protected InAppBillingDebugPrefsAppender createInAppBillingDebugPrefsAppender() {
		return new InAppBillingDebugPrefsAppender();
	}
}
