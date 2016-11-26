package com.jdroid.android.sample.api;

import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.http.parser.Parser;

import java.io.InputStream;
import java.net.SocketTimeoutException;

public class ConnectionExceptionParser implements Parser {

	@Override
	public Object parse(InputStream inputStream) {
		throw new UnexpectedException(new SocketTimeoutException());
	}

	@Override
	public Object parse(String input) {
		throw new UnexpectedException(new SocketTimeoutException());
	}
}