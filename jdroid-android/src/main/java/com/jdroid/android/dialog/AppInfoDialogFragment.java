package com.jdroid.android.dialog;

import android.support.v4.app.FragmentActivity;

import com.jdroid.android.R;
import com.jdroid.android.utils.ExternalAppsUtils;

public class AppInfoDialogFragment extends AlertDialogFragment {

	public static void show(FragmentActivity fragmentActivity, int titleResId, int messageResId, String permission) {
		String screenViewName = AppInfoDialogFragment.class.getSimpleName() + "-" + permission;
		show(fragmentActivity, new AppInfoDialogFragment(), null, fragmentActivity.getString(titleResId), fragmentActivity.getString(messageResId),
				fragmentActivity.getString(R.string.cancel), null, fragmentActivity.getString(R.string.deviceSettings),
				true, screenViewName, null);
	}

	@Override
	protected void onPositiveClick() {
		ExternalAppsUtils.openAppInfo(getActivity());
	}
}
