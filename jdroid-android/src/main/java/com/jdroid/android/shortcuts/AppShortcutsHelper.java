package com.jdroid.android.shortcuts;

import android.annotation.TargetApi;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.os.Build;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AppShortcutsHelper {

	private static final Logger LOGGER = LoggerUtils.getLogger(AppShortcutsHelper.class);

	public static final int MAX_SHORTCUT_COUNT_PER_ACTIVITY = 4;

	@TargetApi(Build.VERSION_CODES.N_MR1)
	public static void reportShortcutUsed(String shortcutId) {
		if (AndroidUtils.getApiLevel() >= Build.VERSION_CODES.N_MR1) {
			ShortcutManager shortcutManager = AbstractApplication.get().getSystemService(ShortcutManager.class);
			shortcutManager.reportShortcutUsed(shortcutId);
		}
	}

	@TargetApi(Build.VERSION_CODES.N_MR1)
	public static void setDynamicShortcuts(List<ShortcutInfo> shortcutInfos) {
		if (AndroidUtils.getApiLevel() >= Build.VERSION_CODES.N_MR1) {
			ShortcutManager shortcutManager = AbstractApplication.get().getSystemService(ShortcutManager.class);
			Collections.sort(shortcutInfos, new Comparator<ShortcutInfo>() {
				@Override
				public int compare(ShortcutInfo o1, ShortcutInfo o2) {
					return o1.getRank() > o2.getRank() ? 1 : -1;
				}
			});
			if (shortcutInfos.size() > MAX_SHORTCUT_COUNT_PER_ACTIVITY) {
				shortcutInfos = shortcutInfos.subList(0, MAX_SHORTCUT_COUNT_PER_ACTIVITY);
				LOGGER.warn("App Shortcuts limit [" + MAX_SHORTCUT_COUNT_PER_ACTIVITY + "] reached. Ignoring some of them");
			}
			shortcutManager.setDynamicShortcuts(shortcutInfos);
		}
	}
}
