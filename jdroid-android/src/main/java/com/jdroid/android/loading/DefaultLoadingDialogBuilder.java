package com.jdroid.android.loading;

import android.app.Activity;
import android.app.Dialog;

public class DefaultLoadingDialogBuilder extends LoadingDialogBuilder {
	
	@Override
	public Dialog build(Activity activity) {
		LoadingDialog loadingDialog = new LoadingDialog(activity);
		loadingDialog.setCancelable(isCancelable());
		return loadingDialog;
	}
}