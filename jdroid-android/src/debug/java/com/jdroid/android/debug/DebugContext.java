package com.jdroid.android.debug;

import android.support.v4.util.Pair;

import com.jdroid.android.AbstractApplication;
import com.jdroid.android.ActivityLauncher;
import com.jdroid.android.debug.mocks.AndroidJsonMockHttpService;
import com.jdroid.android.google.gcm.GcmMessage;
import com.jdroid.android.log.DatabaseLog;
import com.jdroid.android.log.DatabaseLogsRepository;
import com.jdroid.android.sqlite.SQLiteHelper;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.collections.Maps;
import com.jdroid.java.domain.Identifiable;
import com.jdroid.java.http.Server;
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

	public List<Pair<String, Object>> getCustomDebugInfoProperties() {
		return Lists.newArrayList();
	}

	public ServersDebugPrefsAppender createServersDebugPrefsAppender() {
		return new ServersDebugPrefsAppender(getServersMap());
	}

	public Map<Class<? extends Server>, List<? extends Server>> getServersMap() {
		return Maps.newHashMap();
	}
	
	public GcmDebugPrefsAppender createGcmDebugPrefsAppender() {
		return new GcmDebugPrefsAppender(getGcmMessagesMap());
	}

	public Map<GcmMessage, EmulatedGcmMessageIntentBuilder> getGcmMessagesMap() {
		return Maps.newHashMap();
	}
	
	public InAppBillingDebugPrefsAppender createInAppBillingDebugPrefsAppender() {
		return new InAppBillingDebugPrefsAppender();
	}
	
	public ExceptionHandlingDebugPrefsAppender createExceptionHandlingDebugPrefsAppender() {
		return new ExceptionHandlingDebugPrefsAppender();
	}
	
	public HttpCacheDebugPrefsAppender createHttpCacheDebugPrefsAppender() {
		return new HttpCacheDebugPrefsAppender();
	}
	
	public ImageLoaderDebugPrefsAppender createImageLoaderDebugPrefsAppender() {
		return new ImageLoaderDebugPrefsAppender();
	}
	
	public DatabaseDebugPrefsAppender createDatabaseDebugPrefsAppender() {
		return new DatabaseDebugPrefsAppender();
	}
	
	public LogsDebugPrefsAppender createLogsDebugPrefsAppender() {
		return new LogsDebugPrefsAppender();
	}
	
	public ExperimentsDebugPrefsAppender createExperimentsDebugPrefsAppender() {
		return new ExperimentsDebugPrefsAppender();
	}
	
	public AdsDebugPrefsAppender createAdsDebugPrefsAppender() {
		return new AdsDebugPrefsAppender();
	}
	
	public NavDrawerDebugPrefsAppender createNavDrawerDebugPrefsAppender() {
		return new NavDrawerDebugPrefsAppender();
	}
	
	public HttpMocksDebugPrefsAppender createHttpMocksDebugPrefsAppender() {
		return new HttpMocksDebugPrefsAppender();
	}
	
	public InfoDebugPrefsAppender createInfoDebugPrefsAppender() {
		return new InfoDebugPrefsAppender();
	}
	
	public List<PreferencesAppender> getCustomPreferencesAppenders() {
		return Lists.newArrayList();
	}
}
