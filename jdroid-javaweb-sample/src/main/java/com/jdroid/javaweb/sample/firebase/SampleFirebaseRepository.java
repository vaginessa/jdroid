package com.jdroid.javaweb.sample.firebase;

import com.jdroid.java.firebase.FirebaseRepository;
import com.jdroid.java.firebase.auth.CustomTokenFirebaseAuthenticationStrategy;
import com.jdroid.java.firebase.auth.FirebaseAuthenticationStrategy;
import com.jdroid.javaweb.sample.context.ServerApplication;

public class SampleFirebaseRepository extends FirebaseRepository<SampleFirebaseEntity> {

	@Override
	protected FirebaseAuthenticationStrategy createFirebaseAuthenticationStrategy() {
		return new CustomTokenFirebaseAuthenticationStrategy() {
			@Override
			protected String getAuthToken() {
				return ServerApplication.get().getAppContext().getFirebaseAuthToken();
			}
		};
	}

	@Override
	protected String getFirebaseUrl() {
		return ServerApplication.get().getAppContext().getFirebaseUrl();
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
