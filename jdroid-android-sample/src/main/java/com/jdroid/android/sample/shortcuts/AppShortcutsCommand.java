package com.jdroid.android.sample.shortcuts;

import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.sample.ui.home.HomeItem;
import com.jdroid.android.google.gcm.ServiceCommand;
import com.jdroid.android.shortcuts.AppShortcutsHelper;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.android.utils.LocalizationUtils;
import com.jdroid.java.collections.Lists;

import java.util.List;

public class AppShortcutsCommand extends ServiceCommand {

	@Override
	public void start() {
		if (AndroidUtils.getApiLevel() >= Build.VERSION_CODES.N_MR1) {
			super.start();
		}
	}

	@RequiresApi(api = Build.VERSION_CODES.N_MR1)
	@Override
	protected int execute(Bundle bundle) {
		List<ShortcutInfo> shortcutInfos = Lists.newArrayList();
		int rank = 0;
		for (HomeItem item : HomeItem.values()) {

			Intent intent = item.getIntent();
			intent.setAction(Intent.ACTION_VIEW);

			ShortcutInfo.Builder shortcutInfoBuilder = new ShortcutInfo.Builder(AbstractApplication.get(), item.name());
			shortcutInfoBuilder.setShortLabel(LocalizationUtils.getString(item.getNameResource()));
			shortcutInfoBuilder.setLongLabel(LocalizationUtils.getString(item.getNameResource()));
			shortcutInfoBuilder.setIcon(Icon.createWithResource(AbstractApplication.get(), item.getIconResource()));
			shortcutInfoBuilder.setIntent(intent);
			shortcutInfoBuilder.setRank(rank);
			shortcutInfos.add(shortcutInfoBuilder.build());
			rank++;
		}
		AppShortcutsHelper.setDynamicShortcuts(shortcutInfos);

		return GcmNetworkManager.RESULT_SUCCESS;
	}
}
