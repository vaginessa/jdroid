package com.jdroid.android.shortcuts;

import android.annotation.TargetApi;
import android.content.pm.ShortcutManager;
import android.os.Build;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.utils.AndroidUtils;

public class AppShortcutsHelper {

	public static final int MAX_SHORTCUT_COUNT_PER_ACTIVITY = 4;

	@TargetApi(Build.VERSION_CODES.N_MR1)
	public static void reportShortcutUsed(String shortcutId) {
		if (AndroidUtils.getApiLevel() >= Build.VERSION_CODES.N_MR1) {
			ShortcutManager shortcutManager = AbstractApplication.get().getSystemService(ShortcutManager.class);
			shortcutManager.reportShortcutUsed(shortcutId);
		}
	}
}
