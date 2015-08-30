package com.jdroid.android.sample.ui.google.plus;

import com.jdroid.android.google.plus.GooglePlusAuthenticationUseCase;
import com.jdroid.android.google.plus.GooglePlusHelperFragment;
import com.jdroid.android.sample.usecase.LoginGooglePlusAuthenticationUseCase;
import com.jdroid.java.exception.AbstractException;

public class LoginGooglePlusHelperFragment extends GooglePlusHelperFragment {

	@Override
	protected GooglePlusAuthenticationUseCase createGooglePlusAuthenticationUseCase() {
		return getInstance(LoginGooglePlusAuthenticationUseCase.class);
	}

	@Override
	public void onStartUseCase() {
		showLoading();
	}

	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#goBackOnError(com.jdroid.java.exception.AbstractException)
	 */
	@Override
	public Boolean goBackOnError(AbstractException abstractException) {
		return false;
	}
}