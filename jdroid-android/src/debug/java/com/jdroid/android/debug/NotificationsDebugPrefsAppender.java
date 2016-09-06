package com.jdroid.android.debug;

import android.app.Activity;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceGroup;

import com.jdroid.android.R;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.notification.NotificationBuilder;
import com.jdroid.android.notification.NotificationUtils;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.collections.Maps;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.utils.IdGenerator;

import java.util.List;
import java.util.Map;

public class NotificationsDebugPrefsAppender extends PreferencesAppender {

	private List<String> urlsToTest = Lists.newArrayList();

	public NotificationsDebugPrefsAppender() {
		this.urlsToTest = AbstractApplication.get().getDebugContext().getUrlsToTest();
	}

	@Override
	public int getNameResId() {
		return R.string.jdroid_notifications;
	}
	
	@Override
	public void initPreferences(Activity activity, PreferenceGroup preferenceGroup) {
		for (final String url : urlsToTest) {
			Preference preference = new Preference(activity);
			preference.setTitle(url);
			preference.setSummary(url);
			preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {

				@Override
				public boolean onPreferenceClick(Preference preference) {
					Map<String, String> data = Maps.newHashMap();
					data.put(NotificationBuilder.TICKER, "Sample Ticker");
					data.put(NotificationBuilder.CONTENT_TITLE, "Sample Content Title");
					data.put(NotificationBuilder.CONTENT_TEXT, url);
					data.put(NotificationBuilder.URL, url);

					NotificationBuilder builder = new NotificationBuilder("notificationFromBundle", data);
					builder.setSmallIcon(AbstractApplication.get().getLauncherIconResId());
					builder.setWhen(DateUtils.nowMillis());

					NotificationUtils.sendNotification(IdGenerator.getIntId(), builder);
					return true;
				}
			});
			preferenceGroup.addPreference(preference);
		}
	}

	@Override
	public Boolean isEnabled() {
		return !Lists.isNullOrEmpty(urlsToTest);
	}
}
