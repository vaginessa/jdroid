package com.jdroid.javaweb.rollbar;

import com.jdroid.java.concurrent.LowPriorityThreadFactory;
import com.jdroid.javaweb.context.Application;

import org.slf4j.Logger;
import org.slf4j.Marker;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RollBarLogger implements Logger {

	private static final RollBarApiService service = new RollBarApiService();

	private Logger wrappedLogger;

	private Executor executor = Executors.newFixedThreadPool(1, new LowPriorityThreadFactory());

	public enum Level {
		DEBUG, INFO, WARNING, ERROR, CRITICAL
	}

	public RollBarLogger(Logger wrappedLogger) {
		this.wrappedLogger = wrappedLogger;
	}

	@Override
	public String getName() {
		return wrappedLogger.getName();
	}

	@Override
	public boolean isTraceEnabled() {
		return wrappedLogger.isTraceEnabled();
	}

	@Override
	public void trace(String msg) {
		wrappedLogger.trace(msg);
	}

	@Override
	public void trace(String format, Object arg) {
		wrappedLogger.trace(format, arg);
	}

	@Override
	public void trace(String format, Object arg1, Object arg2) {
		wrappedLogger.trace(format, arg1, arg2);
	}

	@Override
	public void trace(String format, Object[] argArray) {
		wrappedLogger.trace(format, argArray);
	}

	@Override
	public void trace(String msg, Throwable t) {
		wrappedLogger.trace(msg, t);
	}

	@Override
	public boolean isTraceEnabled(Marker marker) {
		return wrappedLogger.isTraceEnabled(marker);
	}

	@Override
	public void trace(Marker marker, String msg) {
		wrappedLogger.trace(marker, msg);
	}

	@Override
	public void trace(Marker marker, String format, Object arg) {
		wrappedLogger.trace(marker, format, arg);
	}

	@Override
	public void trace(Marker marker, String format, Object arg1, Object arg2) {
		wrappedLogger.trace(marker, format, arg1, arg2);
	}

	@Override
	public void trace(Marker marker, String format, Object[] argArray) {
		wrappedLogger.trace(marker, format, argArray);
	}

	@Override
	public void trace(Marker marker, String msg, Throwable t) {
		wrappedLogger.trace(marker, msg, t);
	}

	@Override
	public boolean isDebugEnabled() {
		return wrappedLogger.isDebugEnabled();
	}

	@Override
	public void debug(String msg) {
		wrappedLogger.debug(msg);
	}

	@Override
	public void debug(String format, Object arg) {
		wrappedLogger.debug(format, arg);
	}

	@Override
	public void debug(String format, Object arg1, Object arg2) {
		wrappedLogger.debug(format, arg1, arg2);
	}

	@Override
	public void debug(String format, Object[] argArray) {
		wrappedLogger.debug(format, argArray);
	}

	@Override
	public void debug(String msg, Throwable t) {
		wrappedLogger.debug(msg, t);
	}

	@Override
	public boolean isDebugEnabled(Marker marker) {
		return wrappedLogger.isDebugEnabled(marker);
	}

	@Override
	public void debug(Marker marker, String msg) {
		wrappedLogger.debug(marker, msg);
	}

	@Override
	public void debug(Marker marker, String format, Object arg) {
		wrappedLogger.debug(marker, format, arg);
	}

	@Override
	public void debug(Marker marker, String format, Object arg1, Object arg2) {
		wrappedLogger.debug(marker, format, arg1, arg2);
	}

	@Override
	public void debug(Marker marker, String format, Object[] argArray) {
		wrappedLogger.debug(marker, format, argArray);
	}

	@Override
	public void debug(Marker marker, String msg, Throwable t) {
		wrappedLogger.debug(marker, msg, t);
	}

	@Override
	public boolean isInfoEnabled() {
		return wrappedLogger.isInfoEnabled();
	}

	@Override
	public void info(String msg) {
		wrappedLogger.info(msg);
	}

	@Override
	public void info(String format, Object arg) {
		wrappedLogger.info(format, arg);
	}

	@Override
	public void info(String format, Object arg1, Object arg2) {
		wrappedLogger.info(format, arg1, arg2);
	}

	@Override
	public void info(String format, Object[] argArray) {
		wrappedLogger.info(format, argArray);
	}

	@Override
	public void info(String msg, Throwable t) {
		wrappedLogger.info(msg, t);
	}

	@Override
	public boolean isInfoEnabled(Marker marker) {
		return wrappedLogger.isInfoEnabled(marker);
	}

	@Override
	public void info(Marker marker, String msg) {
		wrappedLogger.info(marker, msg);
	}

	@Override
	public void info(Marker marker, String format, Object arg) {
		wrappedLogger.info(marker, format, arg);
	}

	@Override
	public void info(Marker marker, String format, Object arg1, Object arg2) {
		wrappedLogger.info(marker, format, arg1, arg2);
	}

	@Override
	public void info(Marker marker, String format, Object[] argArray) {
		wrappedLogger.info(marker, format, argArray);
	}

	@Override
	public void info(Marker marker, String msg, Throwable t) {
		wrappedLogger.info(marker, msg, t);
	}

	@Override
	public boolean isWarnEnabled() {
		return wrappedLogger.isWarnEnabled();
	}

	@Override
	public void warn(String msg) {
		wrappedLogger.warn(msg);
		log(Level.WARNING, msg, null);
	}

	@Override
	public void warn(String format, Object arg) {
		wrappedLogger.warn(format, arg);
	}

	@Override
	public void warn(String format, Object[] argArray) {
		wrappedLogger.warn(format, argArray);
	}

	@Override
	public void warn(String format, Object arg1, Object arg2) {
		wrappedLogger.warn(format, arg1, arg2);
	}

	@Override
	public void warn(String msg, Throwable t) {
		wrappedLogger.warn(msg, t);
		log(Level.WARNING, msg, t);
	}

	@Override
	public boolean isWarnEnabled(Marker marker) {
		return wrappedLogger.isWarnEnabled(marker);
	}

	@Override
	public void warn(Marker marker, String msg) {
		wrappedLogger.warn(marker, msg);
		log(Level.WARNING, msg, null);
	}

	@Override
	public void warn(Marker marker, String format, Object arg) {
		wrappedLogger.warn(marker, format, arg);
	}

	@Override
	public void warn(Marker marker, String format, Object arg1, Object arg2) {
		wrappedLogger.warn(marker, format, arg1, arg2);
	}

	@Override
	public void warn(Marker marker, String format, Object[] argArray) {
		wrappedLogger.warn(marker, format, argArray);
	}

	@Override
	public void warn(Marker marker, String msg, Throwable t) {
		wrappedLogger.warn(marker, msg, t);
		log(Level.WARNING, msg, t);
	}

	@Override
	public boolean isErrorEnabled() {
		return wrappedLogger.isErrorEnabled();
	}

	@Override
	public void error(String msg) {
		wrappedLogger.error(msg);
		log(Level.ERROR, msg, null);
	}

	@Override
	public void error(String format, Object arg) {
		wrappedLogger.error(format, arg);
	}

	@Override
	public void error(String format, Object arg1, Object arg2) {
		wrappedLogger.error(format, arg1, arg2);
	}

	@Override
	public void error(String format, Object[] argArray) {
		wrappedLogger.error(format, argArray);
	}

	@Override
	public void error(String msg, Throwable t) {
		wrappedLogger.error(msg, t);
		log(Level.ERROR, msg, t);
	}

	@Override
	public boolean isErrorEnabled(Marker marker) {
		return wrappedLogger.isErrorEnabled(marker);
	}

	@Override
	public void error(Marker marker, String msg) {
		wrappedLogger.error(marker, msg);
		log(Level.ERROR, msg, null);
	}

	@Override
	public void error(Marker marker, String format, Object arg) {
		wrappedLogger.error(marker, format, arg);
	}

	@Override
	public void error(Marker marker, String format, Object arg1, Object arg2) {
		wrappedLogger.error(marker, format, arg1, arg2);
	}

	@Override
	public void error(Marker marker, String format, Object[] argArray) {
		wrappedLogger.error(marker, format, argArray);
	}

	@Override
	public void error(Marker marker, String msg, Throwable t) {
		wrappedLogger.error(marker, msg, t);
		log(Level.ERROR, msg, t);
	}

	private void log(final Level level, final String message, final Throwable throwable) {
		if (Application.get().getAppContext().isRollBarEnabled()) {
			executor.execute(new Runnable() {

				@Override
				public void run() {
					try {
						RollBarNotifyBuilder builder = new RollBarNotifyBuilder();
						builder.setAccessToken(Application.get().getAppContext().getRollBarAccessToken());
						builder.setEnvironment(Application.get().getAppContext().getAppName() + "-" + Application.get().getAppContext().getBuildType());
						builder.setLevel(level);
						builder.setMessage(message);
						builder.setThrowable(throwable);
						String payload = builder.build();
						service.sendItem(payload);
						// TODO Implement retry logic here
					} catch (Throwable e) {
						wrappedLogger.error("There was an error notifying the error no RollBar.", e);
					}
				}

			});
		}
	}
}
