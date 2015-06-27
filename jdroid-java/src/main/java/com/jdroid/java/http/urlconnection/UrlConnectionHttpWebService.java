package com.jdroid.java.http.urlconnection;

import com.jdroid.java.exception.ConnectionException;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.http.AbstractWebService;
import com.jdroid.java.http.HttpResponseWrapper;
import com.jdroid.java.http.HttpWebServiceProcessor;
import com.jdroid.java.http.Server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public abstract class UrlConnectionHttpWebService extends AbstractWebService {

	private HttpURLConnection urlConnection;

	public UrlConnectionHttpWebService(Server server, List<Object> urlSegments, List<HttpWebServiceProcessor> httpWebServiceProcessors) {
		super(server, urlSegments, httpWebServiceProcessors);
	}

	@Override
	protected HttpResponseWrapper doExecute(String urlString) {
		try {
			URL url = new URL(urlString);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod(getHttpMethod().name());
			urlConnection.setConnectTimeout(getConnectionTimeout() != null ? getConnectionTimeout() : 0);
			urlConnection.setReadTimeout(getReadTimeout() != null ? getReadTimeout() : 0);
			addHeaders(urlConnection);
			onConfigureUrlConnection(urlConnection);
			return new UrlConnectionHttpResponseWrapper(urlConnection);
		} catch (MalformedURLException e) {
			throw new UnexpectedException(e);
		} catch (FileNotFoundException e) {
			throw new UnexpectedException(e);
		} catch (ConnectException e) {
			throw new ConnectionException(e, false);
		} catch (SocketTimeoutException e) {
			throw new ConnectionException(e, true);
		} catch (IOException e) {
			throw new UnexpectedException(e);
			// TODO Ver cuales de estos son problemas de conexion a internet.
		}
	}

	protected void onConfigureUrlConnection(HttpURLConnection httpURLConnection) throws IOException {
		// Do nothing
	}

	@Override
	protected void doFinally() {
		if (urlConnection != null) {
			urlConnection.disconnect();
		}
	}

	private void addHeaders(URLConnection urlConnection) {
		for (Map.Entry<String, String> entry : getHeaders().entrySet()) {
			urlConnection.setRequestProperty(entry.getKey(), entry.getValue());
		}
	}
}
