package com.jdroid.android.debug;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.jdroid.android.R;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.application.AppModule;
import com.jdroid.android.fragment.AbstractPreferenceFragment;
import com.jdroid.android.permission.PermissionHelper;
import com.jdroid.java.collections.Lists;

import java.util.List;

public class DebugSettingsFragment extends AbstractPreferenceFragment {

	private List<PermissionHelper> permissionHelpers = Lists.newArrayList();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.debug_preferences);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		DebugContext debugContext = AbstractApplication.get().getDebugContext();

		List<PreferencesAppender> appenders = Lists.newArrayList();
		addAppender(appenders, debugContext.createServersDebugPrefsAppender());
		addAppender(appenders, debugContext.createHttpMocksDebugPrefsAppender());
		addAppender(appenders, debugContext.createNavDrawerDebugPrefsAppender());
		addAppender(appenders, debugContext.createExperimentsDebugPrefsAppender());
		addAppender(appenders, debugContext.createDatabaseDebugPrefsAppender());
		addAppender(appenders, debugContext.createLogsDebugPrefsAppender());
		addAppender(appenders, debugContext.createImageLoaderDebugPrefsAppender());
		addAppender(appenders, debugContext.createHttpCacheDebugPrefsAppender());
		addAppender(appenders, debugContext.createExceptionHandlingDebugPrefsAppender());
		addAppender(appenders, debugContext.createInfoDebugPrefsAppender());
		addAppender(appenders, debugContext.createRateAppDebugPrefsAppender());
		addAppender(appenders, debugContext.createUsageStatsDebugPrefsAppender());
		addAppender(appenders, debugContext.createUriMapperPrefsAppender());

		appenders.addAll(debugContext.getCustomPreferencesAppenders());

		for (AppModule each : AbstractApplication.get().getAppModules()) {
			appenders.addAll(each.getPreferencesAppenders());
		}

		for (PreferencesAppender preferencesAppender : appenders) {
			if (preferencesAppender.isEnabled()) {
				for (String each : preferencesAppender.getRequiredPermissions()) {
					permissionHelpers.add(new PermissionHelper((FragmentActivity)getActivity(), each, 1));
				}
				preferencesAppender.initPreferences(getActivity(), getPreferenceScreen());
			}
		}

		for (PermissionHelper each : permissionHelpers) {
			each.checkPermission(true);
		}
	}
	
	private void addAppender(List<PreferencesAppender> appenders, PreferencesAppender preferencesAppender) {
		if (preferencesAppender != null) {
			appenders.add(preferencesAppender);
		}
	}
}