package com.jdroid.android.exception;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.jdroid.android.R;
import com.jdroid.android.application.AbstractApplication;

public class SnackbarErrorDisplayer implements ErrorDisplayer {

	private int parentLayoutId = R.id.fragmentContainer;
	private int duration = Snackbar.LENGTH_LONG;
	private int actionTextResId;
	private View.OnClickListener onClickListener;

	@Override
	public void displayError(String title, String description, Throwable throwable) {
		Activity activity = AbstractApplication.get().getCurrentActivity();
		if (activity != null) {
			Snackbar snackbar = Snackbar.make(activity.findViewById(parentLayoutId), description, duration);
			if (onClickListener != null) {
				snackbar.setAction(actionTextResId, onClickListener);
			}
			snackbar.show();
		}
	}

	public void setActionTextResId(@StringRes int actionTextResId) {
		this.actionTextResId = actionTextResId;
	}

	public void setOnClickListener(View.OnClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}

	public void setParentLayoutId(@IdRes int parentLayoutId) {
		this.parentLayoutId = parentLayoutId;
	}
}
