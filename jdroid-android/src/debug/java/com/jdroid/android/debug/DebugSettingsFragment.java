package com.jdroid.android.debug;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.jdroid.android.R;
import com.jdroid.android.fragment.AbstractPreferenceFragment;
import com.jdroid.android.gcm.GcmMessage;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.collections.Maps;
import com.jdroid.java.http.Server;

import java.util.List;
import java.util.Map;

public class DebugSettingsFragment extends AbstractPreferenceFragment {
	
	private Map<Class<? extends Server>, List<? extends Server>> serversMap = Maps.newLinkedHashMap();
	private Map<GcmMessage, EmulatedGcmMessageIntentBuilder> gcmMessagesMap = Maps.newHashMap();
	
	/**
	 * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.debug_preferences);
	}
	
	/**
	 * @see android.app.Fragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		initServers(serversMap);
		initGcmMessages(gcmMessagesMap);
		
		List<PreferencesAppender> appenders = Lists.newArrayList();
		addAppender(appenders, createServersDebugPrefsAppender());
		addAppender(appenders, createHttpMocksDebugPrefsAppender());
		addAppender(appenders, createNavDrawerDebugPrefsAppender());
		addAppender(appenders, createAdsDebugPrefsAppender());
		addAppender(appenders, createExperimentsDebugPrefsAppender());
		addAppender(appenders, createDatabaseDebugPrefsAppender());
		addAppender(appenders, createLogsDebugPrefsAppender());
		addAppender(appenders, createImageLoaderDebugPrefsAppender());
		addAppender(appenders, createHttpCacheDebugPrefsAppender());
		addAppender(appenders, createExceptionHandlingDebugPrefsAppender());
		addAppender(appenders, createInAppBillingDebugPrefsAppender());
		addAppender(appenders, createGcmDebugPrefsAppender());
		
		appenders.addAll(getCustomPreferencesAppenders());
		
		for (PreferencesAppender preferencesAppender : appenders) {
			if (preferencesAppender.isEnabled()) {
				preferencesAppender.initPreferences(getActivity(), getPreferenceScreen());
			}
		}
		
		ListView listView = findView(android.R.id.list);
		
		View debugInfoView = new DebugInfoView(getActivity());
		listView.addFooterView(debugInfoView);
		
		View customDebugInfoView = getCustomDebugInfoView();
		if (customDebugInfoView != null) {
			listView.addFooterView(debugInfoView);
		}
	}
	
	private void addAppender(List<PreferencesAppender> appenders, PreferencesAppender preferencesAppender) {
		if (preferencesAppender != null) {
			appenders.add(preferencesAppender);
		}
	}
	
	protected void initServers(Map<Class<? extends Server>, List<? extends Server>> serversMap) {
		// Do nothing
	}
	
	protected ServersDebugPrefsAppender createServersDebugPrefsAppender() {
		return new ServersDebugPrefsAppender(serversMap);
	}
	
	protected void initGcmMessages(Map<GcmMessage, EmulatedGcmMessageIntentBuilder> gcmMessagesMap) {
		// Do nothing
	}
	
	protected GcmDebugPrefsAppender createGcmDebugPrefsAppender() {
		return new GcmDebugPrefsAppender(gcmMessagesMap);
	}
	
	protected InAppBillingDebugPrefsAppender createInAppBillingDebugPrefsAppender() {
		return new InAppBillingDebugPrefsAppender();
	}
	
	protected ExceptionHandlingDebugPrefsAppender createExceptionHandlingDebugPrefsAppender() {
		return new ExceptionHandlingDebugPrefsAppender();
	}
	
	protected HttpCacheDebugPrefsAppender createHttpCacheDebugPrefsAppender() {
		return new HttpCacheDebugPrefsAppender();
	}
	
	protected ImageLoaderDebugPrefsAppender createImageLoaderDebugPrefsAppender() {
		return new ImageLoaderDebugPrefsAppender();
	}
	
	protected DatabaseDebugPrefsAppender createDatabaseDebugPrefsAppender() {
		return new DatabaseDebugPrefsAppender();
	}
	
	protected LogsDebugPrefsAppender createLogsDebugPrefsAppender() {
		return new LogsDebugPrefsAppender();
	}
	
	protected ExperimentsDebugPrefsAppender createExperimentsDebugPrefsAppender() {
		return new ExperimentsDebugPrefsAppender();
	}
	
	protected AdsDebugPrefsAppender createAdsDebugPrefsAppender() {
		return new AdsDebugPrefsAppender();
	}
	
	protected NavDrawerDebugPrefsAppender createNavDrawerDebugPrefsAppender() {
		return new NavDrawerDebugPrefsAppender();
	}
	
	protected HttpMocksDebugPrefsAppender createHttpMocksDebugPrefsAppender() {
		return new HttpMocksDebugPrefsAppender();
	}
	
	protected List<PreferencesAppender> getCustomPreferencesAppenders() {
		return Lists.newArrayList();
	}
	
	protected View getCustomDebugInfoView() {
		return null;
	}
}