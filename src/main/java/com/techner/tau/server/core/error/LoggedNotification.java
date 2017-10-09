package com.techner.tau.server.core.error;

import java.util.Map;

/**
 * An application notification.
 */
public class LoggedNotification {

	/** The application environment. E.g., production or staging */
	protected String applicationEnvironment;

	/** Free text message */
	protected String message;

	/** Stack trace as a String. */
	protected String stackTrace;

	/** Thread that triggered the notification. */
	protected long threadId;

	/** Name of the machine that triggered the notification. */
	protected String hostname;

	/** Name of the class where the error occurred. */
	protected String className;

	/** A map of server variables. */
	protected Map<String, String> server;

	/**
	 * A map of environment variables. Env is reserved for application state
	 * rather than system properties.
	 */
	protected Map<String, String> environment;

	/** File that generated the notification. */
	protected String file;

	/** The line number of the notification. */
	protected int lineNumber;

	/** The method that generated the notification. */
	protected String method;

	/** Any HTTP parameters that existed when the notification was triggered. */
	protected String parameters;

	/** Remote IP address (for web apps). */
	protected String ipAddress;

	/** Referring resource (for web apps). */
	protected String referrer;

	/** The user agent of the visitor (for web apps). */
	protected String userAgent;

	/** URL of the resource that triggered the notification (for web apps). */
	protected String url;

	/**
	 * Construct a Notification from an exception and the GSON implementation.
	 * 
	 * @param ex
	 *            The exception that was thrown
	 */
	public LoggedNotification(Throwable ex) {

		if (ex != null) {

			// Unwrap the exception and find the root cause.
			while (ex.getCause() != null) {
				ex = ex.getCause();
			}

			message = String.format("[%s] %s", ex.getClass().getSimpleName(), ex.getLocalizedMessage());

			StackTraceElement[] lines = ex.getStackTrace();
			if (lines.length > 0) {
				className = lines[0].getClassName();
				file = lines[0].getFileName();
				lineNumber = lines[0].getLineNumber();
				method = lines[0].getMethodName();
			}

			StringBuilder sb = new StringBuilder();
			for (StackTraceElement el : lines) {
				sb.append(el.toString());
				sb.append("\n");
			}

			stackTrace = sb.toString();
		}
	}

	/**
	 * @return the applicationEnvironment
	 */
	public String getApplicationEnvironment() {
		return applicationEnvironment;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return the stackTrace
	 */
	public String getStackTrace() {
		return stackTrace;
	}

	/**
	 * @return the threadId
	 */
	public long getThreadId() {
		return threadId;
	}

	/**
	 * @return the hostname
	 */
	public String getHostname() {
		return hostname;
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @return the server
	 */
	public Map<String, String> getServer() {
		return server;
	}

	/**
	 * @return the environment
	 */
	public Map<String, String> getEnvironment() {
		return environment;
	}

	/**
	 * @return the file
	 */
	public String getFile() {
		return file;
	}

	/**
	 * @return the lineNumber
	 */
	public int getLineNumber() {
		return lineNumber;
	}

	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * @return the parameters
	 */
	public String getParameters() {
		return parameters;
	}

	/**
	 * @return the ipAddress
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * @return the referrer
	 */
	public String getReferrer() {
		return referrer;
	}

	/**
	 * @return the userAgent
	 */
	public String getUserAgent() {
		return userAgent;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
}
