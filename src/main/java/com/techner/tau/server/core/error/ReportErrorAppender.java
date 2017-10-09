package com.techner.tau.server.core.error;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;

import com.google.inject.Inject;

/**
 * Appnder para ser usado por log4j
 * 
 * @author juan
 * 
 */
public class ReportErrorAppender extends AppenderSkeleton {

	private final ErrorReporter reporter;

	/**
	 * @param injector
	 */
	@Inject
	public ReportErrorAppender(ErrorReporter reporter) {
		setThreshold(Level.ERROR);
		this.reporter = reporter;
	}

	@Override
	public void close() {
		// Nothing to do.
	}

	@Override
	public boolean requiresLayout() {
		return false;
	}

	@Override
	protected void append(LoggingEvent event) {

		ThrowableInformation e = event.getThrowableInformation();
		if (e == null) {
			return;
		}
		// Avoid sending messages about ourself. This prevents infinite loops.
		if (event.getLoggerName().contains("com.techner.tau.server.core.error")
				|| event.getLoggerName().contains("com.techner.tau.server.core.mailer")) {
			return;
		}
		reporter.sendErrorReport(e.getThrowable());
	}
}
