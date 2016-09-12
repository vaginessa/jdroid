package com.jdroid.android.sample.ui.firebase.database;

import com.jdroid.android.firebase.database.FirebaseDatabaseRepository;

public class SampleFirebaseRepository extends FirebaseDatabaseRepository<SampleFirebaseEntity> {

//	@Override
//	protected FirebaseAuthenticationStrategy createFirebaseAuthenticationStrategy() {
//		return new CustomTokenFirebaseAuthenticationStrategy() {
//			@Override
//			protected String getAuthToken() {
//				return AndroidApplication.get().getAppContext().getFirebaseAuthToken();
//			}
//		};
//	}

	@Override
	protected String getPath() {
		return "samples";
	}

	@Override
	protected Class<SampleFirebaseEntity> getEntityClass() {
		return SampleFirebaseEntity.class;
	}
}
