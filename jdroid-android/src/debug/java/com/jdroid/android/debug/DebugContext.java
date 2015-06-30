package com.jdroid.android.debug;

import android.support.v4.util.Pair;

import com.jdroid.android.AbstractApplication;
import com.jdroid.android.ActivityLauncher;
import com.jdroid.android.debug.mocks.AndroidJsonMockHttpService;
import com.jdroid.android.fragment.AbstractPreferenceFragment;
import com.jdroid.android.log.DatabaseLog;
import com.jdroid.android.log.DatabaseLogsRepository;
import com.jdroid.android.sqlite.SQLiteHelper;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.domain.Identifiable;
import com.jdroid.java.http.mock.AbstractMockHttpService;
import com.jdroid.java.repository.Repository;

import java.util.List;
import java.util.Map;

public class DebugContext {

	public void launchActivityDebugSettingsActivity() {
		ActivityLauncher.launchActivity(DebugSettingsActivity.class);
	}

	public AbstractMockHttpService getAbstractMockHttpServiceInstance(Object... urlSegments) {
		return new AndroidJsonMockHttpService(urlSegments);
	}

	public void initDebugRepositories(
			Map<Class<? extends Identifiable>, Repository<? extends Identifiable>> repositories, SQLiteHelper dbHelper) {
		if (AbstractApplication.get().isDebugLogRepositoryEnabled() && !AbstractApplication.get().getAppContext().isProductionEnvironment()) {
			repositories.put(DatabaseLog.class, new DatabaseLogsRepository(dbHelper));
		}
	}

	public Class<? extends AbstractPreferenceFragment> getDebugSettingsFragmentClass() {
		return DebugSettingsFragment.class;
	}

	public List<Pair<String, Object>> getCustomDebugInfoProperties() {
		return Lists.newArrayList();
	}
}
