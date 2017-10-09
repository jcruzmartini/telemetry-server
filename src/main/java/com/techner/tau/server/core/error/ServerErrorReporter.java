package com.techner.tau.server.core.error;

import com.google.inject.Inject;
import com.techner.tau.server.core.mailer.MailerService;

public class ServerErrorReporter implements ErrorReporter {

	/*** mailer service */
	private final MailerService service;

	/**
	 * Constructor
	 * 
	 * @param service
	 */
	@Inject
	public ServerErrorReporter(MailerService service) {
		this.service = service;
	}

	@Override
	public void sendErrorReport(Throwable e) {
		service.sendErrorReportMail(e);
	}

}
