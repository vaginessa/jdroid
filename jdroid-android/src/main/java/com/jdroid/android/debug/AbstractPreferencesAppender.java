package com.jdroid.android.debug;

import com.jdroid.java.collections.Lists;

import java.util.List;

public abstract class AbstractPreferencesAppender implements PreferencesAppender {

	@Override
	public Boolean isEnabled() {
		return true;
	}

	@Override
	public List<String> getRequiredPermissions() {
		return Lists.newArrayList();
	}
}
