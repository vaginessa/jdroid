package com.jdroid.android.debug;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.R;
import com.jdroid.android.sqlite.SQLiteHelper;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.http.MimeType;
import com.jdroid.java.utils.FileUtils;

public class DatabaseDebugPrefsAppender implements PreferencesAppender {
	
	/**
	 * @see com.jdroid.android.debug.PreferencesAppender#initPreferences(android.app.Activity,
	 *      android.preference.PreferenceScreen)
	 */
	@Override
	public void initPreferences(final Activity activity, PreferenceScreen preferenceScreen) {
		
		PreferenceCategory preferenceCategory = new PreferenceCategory(activity);
		preferenceCategory.setTitle(R.string.database);
		preferenceScreen.addPreference(preferenceCategory);
		
		Preference preference = new Preference(activity);
		preference.setTitle(R.string.downloadDatabase);
		preference.setSummary(R.string.downloadDatabase);
		preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@SuppressWarnings("resource")
			@Override
			public boolean onPreferenceClick(Preference preference) {
				File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
				dir.mkdirs();
				
				File file = new File(dir, AndroidUtils.getApplicationName() + ".sqlite");
				try {
					FileUtils.copyStream(new FileInputStream(SQLiteHelper.getDatabaseFile(activity)), file);
					Intent intent = new Intent(Intent.ACTION_SEND);
					intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
					intent.setType(MimeType.OCTET_STREAM);
					activity.startActivity(intent);
				} catch (FileNotFoundException e) {
					throw new UnexpectedException(e);
				}
				return true;
			}
		});
		preferenceCategory.addPreference(preference);
	}
	
	/**
	 * @see com.jdroid.android.debug.PreferencesAppender#isEnabled()
	 */
	@Override
	public Boolean isEnabled() {
		return AbstractApplication.get().isDatabaseEnabled();
	}
}
