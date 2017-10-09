package com.techner.tau.server.core.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.techner.tau.server.core.mailer.MailJobFactory;
import com.techner.tau.server.core.mailer.Mailer;
import com.techner.tau.server.core.mailer.MailerService;

public class MailModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Mailer.class).to(MailerService.class).in(Scopes.SINGLETON);
		install(new FactoryModuleBuilder().build(MailJobFactory.class));
	}

}
