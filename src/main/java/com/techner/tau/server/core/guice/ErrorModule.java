package com.techner.tau.server.core.guice;

import com.google.inject.AbstractModule;
import com.techner.tau.server.core.error.ErrorReporter;
import com.techner.tau.server.core.error.ServerErrorReporter;

public class ErrorModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ErrorReporter.class).to(ServerErrorReporter.class);
	}

}
