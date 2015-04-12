package com.jdroid.sample.android.debug;

import android.support.v4.util.Pair;

import com.jdroid.android.debug.DebugContext;
import com.jdroid.android.fragment.AbstractPreferenceFragment;
import com.jdroid.java.collections.Lists;

import java.util.List;

public class AndroidDebugContext extends DebugContext {

	@Override
	public Class<? extends AbstractPreferenceFragment> getDebugSettingsFragmentClass() {
		return AndroidDebugSettingsFragment.class;
	}

	@Override
	public List<Pair<String, Object>> getCustomDebugInfoProperties() {
		List<Pair<String, Object>> customProperties = Lists.newArrayList();
		customProperties.add(new Pair("Sample Key", "Sample Value"));
		return customProperties;
	}
}
