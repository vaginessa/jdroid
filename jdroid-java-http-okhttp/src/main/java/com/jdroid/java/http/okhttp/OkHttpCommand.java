package com.jdroid.java.http.okhttp;

import com.jdroid.java.exception.ConnectionException;
import com.jdroid.java.exception.UnexpectedException;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.ProtocolException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

public abstract class OkHttpCommand<P, R> {

	public R execute(P param) {
		try {
			return doExecute(param);
		} catch (SocketTimeoutException e) {
			throw new ConnectionException(e, true);
		} catch (ConnectException e) {
			throw new ConnectionException(e, false);
		} catch (UnknownHostException e) {
			throw new ConnectionException(e, false);
		} catch (InterruptedIOException e) {
			throw new ConnectionException(e, true);
		}  catch (NoRouteToHostException e) {
			throw new ConnectionException(e, false);
		} catch (SocketException e) {
			String message = e.getMessage();
			if (message != null) {
				if (message.equals("Software caused connection abort")) {
					throw new ConnectionException(e, false);
				}
			}

			Throwable cause = e.getCause();
			if (cause != null) {
				message = cause.getMessage();
				if (message != null) {
					if (message.contains("isConnected failed: EHOSTUNREACH (No route to host)")) {
						throw new ConnectionException(e, false);
					} else if (message.contains("recvfrom failed: ETIMEDOUT (Connection timed out)")) {
						throw new ConnectionException(e, true);
					} else if (message.contains("recvfrom failed: ECONNRESET (Connection reset by peer)")) {
						throw new ConnectionException(e, false);
					} else if (message.contains("recvfrom failed: ECONNREFUSED (Connection refused)")) {
						throw new ConnectionException(e, false);
					} else if (message.contains("sendto failed: ETIMEDOUT (Connection timed out)")) {
						throw new ConnectionException(e, true);
					} else if (message.equals("Connection reset")) {
						throw new ConnectionException(e, true);
					}
				}
			}
			throw new UnexpectedException(e);
		} catch (SSLHandshakeException e) {
			String message = e.getMessage();
			if (message != null &&
					message.equals("com.android.org.bouncycastle.jce.exception.ExtCertPathValidatorException: Could not validate certificate: null")) {
				throw new ConnectionException(e, false);
			}
			throw new UnexpectedException(e);
		} catch (SSLException e) {
			String message = e.getMessage();
			if (message != null) {
				if (message.startsWith("Read error:") && message.endsWith("I/O error during system call, Connection reset by peer")) {
					throw new ConnectionException(e, true);
				} else if (message.startsWith("Read error:") && message.endsWith("I/O error during system call, Connection timed out")) {
					throw new ConnectionException(e, true);
				} else if (message.startsWith("SSL handshake aborted:") && message.endsWith("I/O error during system call, Connection reset by peer")) {
					throw new ConnectionException(e, false);
				} else if (message.equals("Connection closed by peer")) {
					throw new ConnectionException(e, false);
				}
			}
			throw new UnexpectedException(e);
		} catch (ProtocolException e) {
			String message = e.getMessage();
			if (message != null && message.equals("Too many follow-up requests: 21")) {
				throw new ConnectionException(e, false);
			}
			throw new UnexpectedException(e);
		} catch (IOException e) {
			String message = e.getMessage();
			if (message != null && message.contains("unexpected end of stream on")) {
				throw new ConnectionException(e, true);
			}
			throw new UnexpectedException(e);
		}
	}

	protected abstract R doExecute(P param) throws IOException;
}
