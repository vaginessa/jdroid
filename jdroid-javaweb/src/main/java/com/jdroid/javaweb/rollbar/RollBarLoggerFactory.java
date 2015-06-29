package com.jdroid.javaweb.rollbar;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RollBarLoggerFactory implements ILoggerFactory {

	@Override
	public Logger getLogger(String name) {
		return new RollBarLogger(LoggerFactory.getLogger(name));
	}
}
