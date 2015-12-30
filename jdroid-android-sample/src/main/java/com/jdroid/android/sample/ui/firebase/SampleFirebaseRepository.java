package com.jdroid.android.sample.ui.firebase;

import com.jdroid.android.sample.application.AndroidApplication;
import com.jdroid.java.firebase.FirebaseRepository;
import com.jdroid.java.firebase.auth.CustomTokenFirebaseAuthenticationStrategy;
import com.jdroid.java.firebase.auth.FirebaseAuthenticationStrategy;

public class SampleFirebaseRepository extends FirebaseRepository<SampleFirebaseEntity> {

	@Override
	protected FirebaseAuthenticationStrategy createFirebaseAuthenticationStrategy() {
		return new CustomTokenFirebaseAuthenticationStrategy() {
			@Override
			protected String getAuthToken() {
				return AndroidApplication.get().getAppContext().getFirebaseAuthToken();
			}
		};
	}

	@Override
	protected String getFirebaseUrl() {
		return AndroidApplication.get().getAppContext().getFirebaseUrl();
	}

	@Override
	protected String getPath() {
		return "samples";
	}

	@Override
	protected Class<SampleFirebaseEntity> getEntityClass() {
		return SampleFirebaseEntity.class;
	}
}
