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

	public List<Pair<String, Object>> getCustomDebugInfoProperties() {
		return null;
	}

	public ServersDebugPrefsAppender createServersDebugPrefsAppender() {
		return null;
	}

	public Map<Class<? extends Server>, List<? extends Server>> getServersMap() {
		return null;
	}

	public GcmDebugPrefsAppender createGcmDebugPrefsAppender() {
		return null;
	}

	public Map<GcmMessage, EmulatedGcmMessageIntentBuilder> getGcmMessagesMap() {
		return null;
	}

	public InAppBillingDebugPrefsAppender createInAppBillingDebugPrefsAppender() {
		return null;
	}

	public ExceptionHandlingDebugPrefsAppender createExceptionHandlingDebugPrefsAppender() {
		return null;
	}

	public HttpCacheDebugPrefsAppender createHttpCacheDebugPrefsAppender() {
		return null;
	}

	public ImageLoaderDebugPrefsAppender createImageLoaderDebugPrefsAppender() {
		return null;
	}

	public DatabaseDebugPrefsAppender createDatabaseDebugPrefsAppender() {
		return null;
	}

	public LogsDebugPrefsAppender createLogsDebugPrefsAppender() {
		return null;
	}

	public ExperimentsDebugPrefsAppender createExperimentsDebugPrefsAppender() {
		return null;
	}

	public AdsDebugPrefsAppender createAdsDebugPrefsAppender() {
		return null;
	}

	public NavDrawerDebugPrefsAppender createNavDrawerDebugPrefsAppender() {
		return null;
	}

	public HttpMocksDebugPrefsAppender createHttpMocksDebugPrefsAppender() {
		return null;
	}

	public InfoDebugPrefsAppender createInfoDebugPrefsAppender() {
		return null;
	}

	public List<PreferencesAppender> getCustomPreferencesAppenders() {
		return null;
	}

}
