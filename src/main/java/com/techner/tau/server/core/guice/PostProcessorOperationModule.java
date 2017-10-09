package com.techner.tau.server.core.guice;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;
import com.techner.tau.server.core.processor.operation.OperationTypes;
import com.techner.tau.server.core.processor.operation.PostOperationProcessor;

/**
 * Modulo para registrar los post procesadores de operaciones
 * 
 * @author juan
 * 
 */
public class PostProcessorOperationModule extends AbstractModule {

	@Override
	protected void configure() {
		MapBinder<String, PostOperationProcessor> mapbinder = MapBinder.newMapBinder(binder(), String.class,
				PostOperationProcessor.class);
		for (OperationTypes type : OperationTypes.values()) {
			mapbinder.addBinding(type.getCode()).to(type.getPostProcessor());
		}
	}
}
