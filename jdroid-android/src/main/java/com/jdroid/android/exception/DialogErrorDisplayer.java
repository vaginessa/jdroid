package com.jdroid.android.exception;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.exception.AbstractException;

public class DialogErrorDisplayer implements ErrorDisplayer {

	private static final String GO_BACK_KEY = "goBack";

	@Override
	public void displayError(String title, String description, Throwable throwable) {
		Activity activity = AbstractApplication.get().getCurrentActivity();
		if (activity != null) {
			ErrorDialogFragment.show((FragmentActivity)activity, title, description, goBackOnError(throwable));
		}
	}

	private Boolean goBackOnError(Throwable throwable) {
		if (throwable instanceof AbstractException) {
			AbstractException abstractException = (AbstractException)throwable;
			return abstractException.hasParameter(GO_BACK_KEY) ? abstractException.<Boolean>getParameter(GO_BACK_KEY)
					: goBackOnErrorByDefault(abstractException);
		} else {
			return goBackOnErrorByDefault(throwable);
		}
	}

	protected Boolean goBackOnErrorByDefault(Throwable throwable) {
		return true;
	}

	public static void markAsGoBackOnError(AbstractException abstractException) {
		abstractException.addParameter(GO_BACK_KEY, true);
	}

	public static void markAsNotGoBackOnError(AbstractException abstractException) {
		abstractException.addParameter(GO_BACK_KEY, false);
	}
}
