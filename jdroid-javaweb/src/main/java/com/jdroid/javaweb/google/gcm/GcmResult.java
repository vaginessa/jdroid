package com.jdroid.javaweb.google.gcm;

public class GcmResult {

	// String specifying a unique ID for each successfully processed message.
	private String messageId;

	// Optional string specifying the canonical registration token for the client app that the message
	// was processed and sent to. Sender should use this value as the registration token for future requests.
	// Otherwise, the messages might be rejected.
	private String registrationId;

	// String specifying the error that occurred when processing the message for the recipient.
	// The possible values are https://developers.google.com/cloud-messaging/http-server-ref#table9
	private String error;

	public String getMessageId() {
		return messageId;
	}

	public String getRegistrationId() {
		return registrationId;
	}

	public String getError() {
		return error;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}

	public void setError(String error) {
		this.error = error;
	}
}
