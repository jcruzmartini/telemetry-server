package com.techner.tau.server.core.guice;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;
import com.techner.tau.server.core.processor.receptor.ReceptorProcessor;
import com.techner.tau.server.domain.interfaces.Message;

/**
 * Modulo para registrar los procesadores de recepcion para cada tipo de SMS
 * 
 * @author juan
 * 
 */
public class ProcessorReceptorModule extends AbstractModule {

	@Override
	protected void configure() {
		MapBinder<String, ReceptorProcessor> mapbinder = MapBinder.newMapBinder(binder(), String.class,
				ReceptorProcessor.class);
		for (Message.Type type : Message.Type.values()) {
			// Algunos tipos de mensajes no tienen procesadores de recepcion
			if (null != type.getReceptorProcessor()) {
				mapbinder.addBinding(type.getCode()).to(type.getReceptorProcessor());
			}
		}
	}

}
