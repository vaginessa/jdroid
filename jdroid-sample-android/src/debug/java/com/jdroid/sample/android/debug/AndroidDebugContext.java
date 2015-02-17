package com.jdroid.sample.android.debug;

import com.jdroid.android.debug.DebugContext;
import com.jdroid.android.fragment.AbstractPreferenceFragment;

public class AndroidDebugContext extends DebugContext {

	@Override
	public Class<? extends AbstractPreferenceFragment> getDebugSettingsFragmentClass() {
		return AndroidDebugSettingsFragment.class;
	}
}
