package com.jdroid.android.debug;

import android.app.Activity;
import android.os.Environment;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceGroup;

import com.jdroid.android.R;
import com.jdroid.android.permission.PermissionHelper;
import com.jdroid.android.utils.AppUtils;
import com.jdroid.android.utils.ExternalAppsUtils;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class UriMapperPrefsAppender extends AbstractPreferencesAppender {

	private Integer htmlRawId;

	public UriMapperPrefsAppender(Integer htmlRawId) {
		this.htmlRawId = htmlRawId;
	}
	public UriMapperPrefsAppender() {
		this(null);
	}

	@Override
	public void initPreferences(final Activity activity, PreferenceGroup preferenceGroup) {

		if (htmlRawId == null) {
			htmlRawId = activity.getResources().getIdentifier("url_samples", "raw", activity.getPackageName());
		}

		PreferenceCategory preferenceCategory = new PreferenceCategory(activity);
		preferenceCategory.setTitle(R.string.uriMapper);
		preferenceGroup.addPreference(preferenceCategory);

		Preference preference = new Preference(activity);
		preference.setTitle(R.string.downloadUrlSample);
		preference.setSummary(R.string.downloadUrlSample);
		preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				try {
					File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
					dir.mkdirs();

					InputStream openInputStream = activity.getResources().openRawResource(htmlRawId);
					File file = new File(dir, AppUtils.getApplicationId() + "_url_samples.html");
					FileUtils.copyStream(openInputStream, file);
					openInputStream.close();

					ExternalAppsUtils.openOnBrowser(activity, file);
				} catch (IOException e) {
					throw new UnexpectedException(e);
				}

				return true;
			}
		});
		preferenceCategory.addPreference(preference);
	}

	@Override
	public List<String> getRequiredPermissions() {
		return Lists.newArrayList(PermissionHelper.WRITE_EXTERNAL_STORAGE_PERMISSION);
	}
}
