package com.techner.tau.server.core.error;

import java.lang.Thread.UncaughtExceptionHandler;

import com.google.inject.Inject;

/***
 * Majedor de errores y enviador de excepciones a los administradores
 * 
 * @author juan
 * 
 */
public class ServerUncaughtExceptionHandler implements UncaughtExceptionHandler {

	/** Error reporter */
	private final ErrorReporter reporter;

	/**
	 * Constructor
	 * 
	 * @param reporter
	 */
	@Inject
	public ServerUncaughtExceptionHandler(ErrorReporter reporter) {
		this.reporter = reporter;
	}

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		reporter.sendErrorReport(e);
	}

}
