package com.foodit.server.common;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppServletContextListener implements ServletContextListener, HttpSessionListener {
	private static final Logger logger = LoggerFactory.getLogger(AppServletContextListener.class);

	private static int totalActiveSessions;

	@Override
	public void contextInitialized(ServletContextEvent event) {

		try {

			MainApplication.getInstance().init(event.getServletContext());

		} catch (Exception e) {
			logger.error("Failed to start Main Application", e);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		logger.info("ServletContextListener destroyed");
	}

	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
		totalActiveSessions++;
		logger.info("sessionCreated - add one session into counter");
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {
		totalActiveSessions--;
		logger.info("sessionDestroyed - deduct one session from counter");
	}

	// TODO Poll this and put on pubsub
	public static int getTotalActiveSession() {
		return totalActiveSessions;
	}

}