package com.jdroid.android.dialog;

import com.jdroid.android.utils.ExternalAppsUtils;

public class AppInfoDialogFragment extends AlertDialogFragment {

	@Override
	protected void onPositiveClick() {
		ExternalAppsUtils.openAppInfo(getActivity());
	}
}
