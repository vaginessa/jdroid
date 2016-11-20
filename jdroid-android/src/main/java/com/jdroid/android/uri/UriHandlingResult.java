package com.jdroid.android.uri;

import java.io.Serializable;

public class UriHandlingResult implements Serializable {

	private Boolean uriHandled = false;
	private Boolean newActivityOpened = false;

	public Boolean isHandled() {
		return uriHandled;
	}

	public void setUriHandled(Boolean uriHandled) {
		this.uriHandled = uriHandled;
	}

	public void setNewActivityOpened(Boolean newActivityOpened) {
		this.newActivityOpened = newActivityOpened;
	}

	public Boolean isNewActivityOpened() {
		return newActivityOpened;
	}
}
