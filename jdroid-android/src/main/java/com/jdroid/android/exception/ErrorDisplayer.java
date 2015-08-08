package com.jdroid.android.exception;

public interface ErrorDisplayer {

	public void displayError(String title, String description, Throwable throwable);

}
