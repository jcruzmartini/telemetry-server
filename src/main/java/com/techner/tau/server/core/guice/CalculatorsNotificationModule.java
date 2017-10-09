package com.techner.tau.server.core.guice;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.MapBinder;
import com.techner.tau.server.core.calculator.notification.NotificationCalculatorTask;
import com.techner.tau.server.core.calculator.notification.NotificationCalculatorTaskImpl;
import com.techner.tau.server.core.calculator.notification.NotificationsCalculator;

/**
 * Modulo para registrar los calculadores de las distintas notificaciones
 * 
 * @author juan
 * 
 */
public class CalculatorsNotificationModule extends AbstractModule {

	@Override
	protected void configure() {

		install(new FactoryModuleBuilder().implement(NotificationCalculatorTask.class,
				NotificationCalculatorTaskImpl.class).build(NotificationCalculatorTask.Factory.class));

		MapBinder<String, NotificationsCalculator> mapbinder = MapBinder.newMapBinder(binder(), String.class,
				NotificationsCalculator.class);
		for (NotificationsCalculator.Calculators calculator : NotificationsCalculator.Calculators.values()) {
			mapbinder.addBinding(calculator.getNotificacion()).to(calculator.getCalculator());
		}
	}
}
