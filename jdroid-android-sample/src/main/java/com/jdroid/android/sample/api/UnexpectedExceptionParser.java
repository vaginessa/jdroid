package com.jdroid.android.sample.api;

import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.http.parser.Parser;

import java.io.InputStream;

public class UnexpectedExceptionParser implements Parser {

	@Override
	public Object parse(InputStream inputStream) {
		throw new UnexpectedException("Error");
	}

	@Override
	public Object parse(String input) {
		throw new UnexpectedException("Error");
	}
}