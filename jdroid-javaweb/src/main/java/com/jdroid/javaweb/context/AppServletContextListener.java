package com.jdroid.javaweb.context;

import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.javaweb.exception.DefaultExceptionHandler;
import com.jdroid.javaweb.rollbar.RollBarLoggerFactory;

import org.apache.log4j.helpers.LogLog;
import org.slf4j.Logger;
import org.springframework.util.ClassUtils;
import org.springframework.util.Log4jConfigurer;

import java.io.FileNotFoundException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Listener to load from the classpath the log4j configuration file.
 */
public class AppServletContextListener implements ServletContextListener {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(AppServletContextListener.class);
	
	/**
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {

		LoggerUtils.setEnabled(true);
		LoggerUtils.setDefaultLoggerFactory(new RollBarLoggerFactory());

		Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler());

		try {
			// If the file log4j.deployment.xml is present in the classpath, it is used
			// else the file log4j.xml is used
			String log4jPath = "classpath:log4j.xml";
			if (ClassUtils.getDefaultClassLoader().getResource("log4j.xml") == null) {
				log4jPath = "classpath:log4j.deployment.xml";
			}
			Log4jConfigurer.initLogging(log4jPath);
			LOGGER.info("Starting Logging.");
		} catch (FileNotFoundException ex) {
			LogLog.error(ex.getMessage());
		}
	}
	
	/**
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		LOGGER.info("Shutdown Logging.");
		Log4jConfigurer.shutdownLogging();
	}
}
