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
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.http.MimeType;
import com.jdroid.java.repository.Repository;
import com.jdroid.java.utils.FileUtils;
import com.jdroid.java.utils.LoggerUtils;

public class DebugLogHelper {
	
	public static void log(final String text) {
		if (LoggerUtils.isEnabled()) {
			ExecutorUtils.execute(new Runnable() {
				
				@Override
				public void run() {
					
					try {
						Repository<DebugLog> repository = AbstractApplication.get().getRepositoryInstance(
							DebugLog.class);
						repository.add(new DebugLog(text));
					} catch (Exception e) {
						AbstractApplication.get().getExceptionHandler().logHandledException(e);
					}
				}
			});
		}
	}
	
	public static void clean() {
		ExecutorUtils.execute(new Runnable() {
			
			@Override
			public void run() {
				
				try {
					Repository<DebugLog> repository = AbstractApplication.get().getRepositoryInstance(DebugLog.class);
					repository.removeAll();
				} catch (Exception e) {
					AbstractApplication.get().getExceptionHandler().logHandledException(e);
				}
			}
		});
	}
	
	public static void initPreferences(final Activity activity, PreferenceScreen preferenceScreen) {
		
		if (AbstractApplication.get().isDebugLogRepositoryEnabled()) {
			
			PreferenceCategory preferenceCategory = new PreferenceCategory(activity);
			preferenceCategory.setTitle(R.string.debugDatabase);
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
			
			preference = new Preference(activity);
			preference.setTitle(R.string.cleanDebugData);
			preference.setSummary(R.string.cleanDebugData);
			preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
				
				@Override
				public boolean onPreferenceClick(Preference preference) {
					DebugLogHelper.clean();
					return true;
				}
			});
			preferenceCategory.addPreference(preference);
		}
	}
}
