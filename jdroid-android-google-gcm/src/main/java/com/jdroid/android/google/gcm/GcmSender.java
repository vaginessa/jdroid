package com.jdroid.android.google.gcm;

import com.jdroid.java.http.Server;

public interface GcmSender extends Server {

	/**
	 * @return A unique numerical value created when you configure your API project (given as "Project Number" in the Google
	 * Developers Console). The sender ID is used in the registration process to identify an app server that is permitted
	 * to send messages to the client app.
	 */
	String getSenderId();
}
