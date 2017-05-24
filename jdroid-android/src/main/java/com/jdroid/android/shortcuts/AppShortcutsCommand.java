package com.jdroid.android.shortcuts;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.jdroid.android.google.gcm.ServiceCommand;
import com.jdroid.android.utils.AndroidUtils;

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
		AppShortcutsHelper.setDynamicShortcuts(AppShortcutsHelper.getInitialShortcutInfos());
		return GcmNetworkManager.RESULT_SUCCESS;
	}
}
