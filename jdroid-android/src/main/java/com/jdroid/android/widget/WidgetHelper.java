package com.jdroid.android.widget;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.utils.SharedPreferencesHelper;
import com.jdroid.java.concurrent.ExecutorUtils;

import java.util.List;

public class WidgetHelper {

	private static final String WIDGET_PREFERENCES = "widgets";
	private static final String WIDGET_NAMES = "widgetsNames";

	public synchronized static void onWidgetRemoved(final String widgetName) {
		ExecutorUtils.execute(new Runnable() {
			@Override
			public void run() {
				SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.get(WIDGET_PREFERENCES);
				List<String> widgets = sharedPreferencesHelper.loadPreferenceAsStringList(WIDGET_NAMES);
				if (widgets.contains(widgetName)) {
					widgets.remove(widgetName);
					sharedPreferencesHelper.savePreference(WIDGET_NAMES, widgets);
					AbstractApplication.get().getAnalyticsSender().trackWidgetRemoved(widgetName);
				}
			}
		});
	}

	public synchronized static void onWidgetAdded(final String widgetName) {

		ExecutorUtils.execute(new Runnable() {
			@Override
			public void run() {
				SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.get(WIDGET_PREFERENCES);
				List<String> widgets = sharedPreferencesHelper.loadPreferenceAsStringList(WIDGET_NAMES);
				if (!widgets.contains(widgetName)) {
					sharedPreferencesHelper.appendPreferenceAsync(WIDGET_NAMES, widgetName);
					AbstractApplication.get().getAnalyticsSender().trackWidgetAdded(widgetName);
				}
			}
		});

	}
}
