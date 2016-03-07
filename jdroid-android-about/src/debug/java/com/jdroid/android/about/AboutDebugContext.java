package com.jdroid.android.about;

import com.jdroid.android.debug.PreferencesAppender;
import com.jdroid.java.collections.Lists;

import java.util.List;

public class AboutDebugContext {

	public List<PreferencesAppender> getPreferencesAppenders() {
		List<PreferencesAppender> appenders = Lists.newArrayList();
		appenders.add(new AppInviteDebugPrefsAppender());
		return appenders;
	}
}
