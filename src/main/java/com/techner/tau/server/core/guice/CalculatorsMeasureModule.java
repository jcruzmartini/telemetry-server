package com.techner.tau.server.core.guice;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.MapBinder;
import com.techner.tau.server.core.calculator.measure.MeasureCalculator;
import com.techner.tau.server.core.calculator.measure.daily.DailyMeasureCalculatorTask;
import com.techner.tau.server.core.calculator.measure.daily.DailyMeasureCalculatorTaskImpl;
import com.techner.tau.server.core.calculator.measure.hourly.HourlyMeasureCalculatorTask;
import com.techner.tau.server.core.calculator.measure.hourly.HourlyMeasureCalculatorTaskImpl;

/**
 * Modulo para registrar los calculadores de las distintas alertas
 * 
 * @author juan
 * 
 */
public class CalculatorsMeasureModule extends AbstractModule {

	@Override
	protected void configure() {

		install(new FactoryModuleBuilder().implement(HourlyMeasureCalculatorTask.class,
				HourlyMeasureCalculatorTaskImpl.class).build(HourlyMeasureCalculatorTask.Factory.class));
		install(new FactoryModuleBuilder().implement(DailyMeasureCalculatorTask.class,
				DailyMeasureCalculatorTaskImpl.class).build(DailyMeasureCalculatorTask.Factory.class));

		MapBinder<String, MeasureCalculator> mapbinder = MapBinder.newMapBinder(binder(), String.class,
				MeasureCalculator.class);
		for (MeasureCalculator.Calculators calculator : MeasureCalculator.Calculators.values()) {
			mapbinder.addBinding(calculator.getVariable()).to(calculator.getCalculator());
		}
	}
}
