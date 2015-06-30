package com.jdroid.android.debug;

import android.support.v4.util.Pair;

import com.jdroid.android.fragment.AbstractPreferenceFragment;
import com.jdroid.android.sqlite.SQLiteHelper;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.domain.Identifiable;
import com.jdroid.java.http.mock.AbstractMockHttpService;
import com.jdroid.java.repository.Repository;

import java.util.List;
import java.util.Map;

public class DebugContext {

	public void launchActivityDebugSettingsActivity() {
		// Do nothing
	}

	public AbstractMockHttpService getAbstractMockHttpServiceInstance(Object... urlSegments) {
		return null;
	}

	public void initDebugRepositories(
			Map<Class<? extends Identifiable>, Repository<? extends Identifiable>> repositories, SQLiteHelper dbHelper) {
		// Do nothing
	}

	public Class<? extends AbstractPreferenceFragment> getDebugSettingsFragmentClass() {
		return null;
	}

	public List<Pair<String, Object>> getCustomDebugInfoProperties() {
		return Lists.newArrayList();
	}
}
