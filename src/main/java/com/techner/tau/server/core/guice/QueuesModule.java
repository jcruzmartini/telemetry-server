package com.techner.tau.server.core.guice;

import java.util.concurrent.ThreadPoolExecutor;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.techner.tau.server.core.annotation.AlertCheckerPoolExecutor;
import com.techner.tau.server.core.annotation.DailyMeasuresPoolExecutor;
import com.techner.tau.server.core.annotation.HourlyMeasuresPoolExecutor;
import com.techner.tau.server.core.annotation.MailerPoolExecutor;
import com.techner.tau.server.core.annotation.NotificationsPoolExecutor;
import com.techner.tau.server.core.annotation.PeriodicalNotificationsPoolExecutor;
import com.techner.tau.server.core.annotation.TweetsPoolExecutor;
import com.techner.tau.server.core.provider.AlertCheckerQueueProvider;
import com.techner.tau.server.core.provider.DailyMeasureCalculatorQueueProvider;
import com.techner.tau.server.core.provider.HourlyMeasureCalculatorQueueProvider;
import com.techner.tau.server.core.provider.MailerQueueProvider;
import com.techner.tau.server.core.provider.NotificationsCalculatorQueueProvider;
import com.techner.tau.server.core.provider.PeriodicalNotificationsQueueProvider;
import com.techner.tau.server.core.provider.TweetsQueueProvider;

/**
 * Modulo para los pooles de trabajos
 * 
 * @author juan
 * 
 */
public class QueuesModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ThreadPoolExecutor.class).annotatedWith(HourlyMeasuresPoolExecutor.class)
				.toProvider(HourlyMeasureCalculatorQueueProvider.class).in(Scopes.SINGLETON);
		bind(ThreadPoolExecutor.class).annotatedWith(DailyMeasuresPoolExecutor.class)
				.toProvider(DailyMeasureCalculatorQueueProvider.class).in(Scopes.SINGLETON);
		bind(ThreadPoolExecutor.class).annotatedWith(NotificationsPoolExecutor.class)
				.toProvider(NotificationsCalculatorQueueProvider.class).in(Scopes.SINGLETON);
		bind(ThreadPoolExecutor.class).annotatedWith(TweetsPoolExecutor.class).toProvider(TweetsQueueProvider.class)
				.in(Scopes.SINGLETON);
		bind(ThreadPoolExecutor.class).annotatedWith(MailerPoolExecutor.class).toProvider(MailerQueueProvider.class)
				.in(Scopes.SINGLETON);
		bind(ThreadPoolExecutor.class).annotatedWith(PeriodicalNotificationsPoolExecutor.class)
				.toProvider(PeriodicalNotificationsQueueProvider.class).in(Scopes.SINGLETON);
		bind(ThreadPoolExecutor.class).annotatedWith(AlertCheckerPoolExecutor.class)
				.toProvider(AlertCheckerQueueProvider.class).in(Scopes.SINGLETON);
	}

}
