package com.jdroid.android.uil.debug;

import com.jdroid.android.debug.PreferencesAppender;
import com.jdroid.java.collections.Lists;

import java.util.List;

public class UilDebugContext {

	public List<PreferencesAppender> getPreferencesAppenders() {
		return Lists.<PreferencesAppender>newArrayList(new ImageLoaderDebugPrefsAppender());
	}
}
