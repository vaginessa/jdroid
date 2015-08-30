package com.jdroid.android.sample.usecase;

import com.google.android.gms.plus.model.people.Person;
import com.jdroid.android.google.plus.GooglePlusAuthenticationUseCase;
import com.jdroid.java.concurrent.ExecutorUtils;

public class LoginGooglePlusAuthenticationUseCase extends GooglePlusAuthenticationUseCase {

	private static final long serialVersionUID = 5044635723842208710L;

	@Override
	protected void onConnectToGooglePlus(Person person, String account) {
		// Sign in on server here

		// Wait 3 seconds to simulate a request
		ExecutorUtils.sleep(3);
	}

	@Override
	protected void onDisconnectFromGooglePlus() {
		// Do nothing
	}

}