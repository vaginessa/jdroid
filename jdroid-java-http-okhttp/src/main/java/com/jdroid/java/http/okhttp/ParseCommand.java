package com.jdroid.java.http.okhttp;

import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.http.parser.Parser;

import java.io.IOException;
import java.io.InputStream;

public class ParseCommand extends OkHttpCommand<InputStream, Object> {

	private Parser parser;

	public ParseCommand(Parser parser) {
		this.parser = parser;
	}

	@Override
	protected Object doExecute(InputStream inputStream) throws IOException {
		try {
			return parser.parse(inputStream);
		} catch (UnexpectedException e) {
			Throwable throwable = e.getCause();
			if (throwable instanceof IOException) {
				throw (IOException)throwable;
			} else {
				throw e;
			}
		}
	}
}
