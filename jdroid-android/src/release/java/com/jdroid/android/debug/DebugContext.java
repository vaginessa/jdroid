package com.jdroid.android.debug;


import com.jdroid.android.sqlite.SQLiteHelper;
import com.jdroid.java.domain.Identifiable;
import com.jdroid.java.http.mock.AbstractMockWebService;
import com.jdroid.android.fragment.AbstractPreferenceFragment;
import com.jdroid.java.repository.Repository;

import java.util.Map;

public class DebugContext {

	public void launchActivityDebugSettingsActivity() {
		// Do nothing
	}

	public AbstractMockWebService getAbstractMockWebServiceInstance(Object... urlSegments) {
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
