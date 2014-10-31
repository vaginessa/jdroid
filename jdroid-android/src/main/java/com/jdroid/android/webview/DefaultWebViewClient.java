package com.jdroid.android.webview;

import android.net.http.SslError;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.jdroid.android.AbstractApplication;
import com.jdroid.java.exception.ConnectionException;
import com.jdroid.java.exception.UnexpectedException;

public class DefaultWebViewClient extends WebViewClient {
	
	private Boolean errorReceived = false;
	
	/**
	 * @see android.webkit.WebViewClient#onReceivedError(android.webkit.WebView, int, java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
		if ((errorCode != WebViewClient.ERROR_CONNECT) && (errorCode != WebViewClient.ERROR_HOST_LOOKUP)) {
			AbstractApplication.get().getExceptionHandler().handleThrowable(
				new UnexpectedException("WebView error: " + errorCode + ". " + description));
		} else {
			AbstractApplication.get().getExceptionHandler().handleThrowable(new ConnectionException(description));
		}
		view.setVisibility(View.GONE);
		errorReceived = true;
	}
	
	/**
	 * @see android.webkit.WebViewClient#onReceivedSslError(android.webkit.WebView, android.webkit.SslErrorHandler,
	 *      android.net.http.SslError)
	 */
	@Override
	public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
		handler.proceed();
	}
	
	public Boolean isErrorReceived() {
		return errorReceived;
	}
}
