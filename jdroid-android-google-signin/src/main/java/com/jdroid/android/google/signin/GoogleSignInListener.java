package com.jdroid.android.google.signin;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.GoogleApiClient;

public interface GoogleSignInListener extends GoogleApiClient.OnConnectionFailedListener {

	void onGoogleSignIn(GoogleSignInAccount googleSignInAccount);

	void onGoogleSignOut();

	void onGoogleAccessRevoked();
}
